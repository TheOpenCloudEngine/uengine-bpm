package org.uengine.kernel.bpmn;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.metaworks.dwr.MetaworksRemoteService;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.uengine.kernel.*;

import javax.net.ssl.SSLContext;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * Created by uengine on 2017. 12. 4..
 */
public class ServiceTask extends DefaultActivity {


    private boolean skipIfNotFound;

    @Override
    protected void executeActivity(ProcessInstance instance) throws Exception {

        /**
         * get the target server:
         *  1. try full uri from uriTemplate,
         *  2. find full url from rolemapping data,
         *  3. try searching from EUREKA server by the endpoint of roleMapping
         *  4. try searching from EUREKA server by the role name.
         *  ******/

        if(checkLocalCall(instance)){
            return;
        }

        Role role = getRole();

        String fullURITemplate = null;
        String serviceId = null;

        if (role == null || (getUriTemplate() != null && getUriTemplate().startsWith("http"))) {
            fullURITemplate = getUriTemplate();
        } else {
            RoleMapping roleMapping = getRole().getMapping(instance);

            if (roleMapping != null && roleMapping.getEndpoint() != null) {

                if (roleMapping.getEndpoint().startsWith("http")) {
                    fullURITemplate = roleMapping.getEndpoint() + getUriTemplate();
                } else {
                    serviceId = roleMapping.getEndpoint();
                }
            }

            if (fullURITemplate == null) {

                if (serviceId == null)
                    serviceId = role.getName();

                LoadBalancerClient loadBalancerClient = MetaworksRemoteService.getInstance().getComponent(LoadBalancerClient.class);

                DiscoveryClient discoveryClient = MetaworksRemoteService.getInstance().getComponent(DiscoveryClient.class);
                List<String> services = discoveryClient.getServices();

                if (loadBalancerClient != null) {
                    ServiceInstance serviceInstance = loadBalancerClient.choose(serviceId);

                    if (serviceInstance != null) {
                        String baseUrl = serviceInstance.getUri().toString();

                        fullURITemplate = baseUrl + (getUriTemplate().startsWith("/") ? getUriTemplate() : "/" + getUriTemplate());
                    }
                }
            }
        }

        if (fullURITemplate == null) throw new UEngineException("Target Service URI to call is not found: " + (getRole()!=null ? getRole().getName() : getUriTemplate()));


        /**
         *  creating real uri by evaluating the url pattern
         *  ******/

//        ExpressionParser parser = new SpelExpressionParser();
//
//        StandardEvaluationContext context = new StandardEvaluationContext(instance);
//        context.setVariable("activity", this);
//
//        String realURI =
//                parser.parseExpression(fullURITemplate,
//                        new TemplateParserContext()).getValue(String.class);

        String realURI = evaluateContent(instance, fullURITemplate).toString();

        URL url = null;
        try {
            url = new URL(realURI);
        } catch (MalformedURLException e) {
            throw new UEngineException("URI is malformed: " + realURI, e);
        }


        /**
         *  call the uri with configured method and payload
         *  ******/

        String payload = null;
        String result = null;

        try {
            RestTemplate restTemplate;

            if(isNoValidationForSSL()){
                restTemplate = noValidatingRestTemplate();

            }else{
                restTemplate = new RestTemplate();
            }

            HttpMethod httpMethod = HttpMethod.GET;

            if ("POST".equals(getMethod())){
                httpMethod = HttpMethod.POST;
            }else if ("PUT".equals(getMethod())){
                httpMethod = HttpMethod.PUT;
            }else if ("DELETE".equals(getMethod())){
                httpMethod = HttpMethod.DELETE;
            }

            if ("GET,DELETE".indexOf(getMethod()) == -1){
                payload =
                        evaluateContent(instance, getInputPayloadTemplate()).toString();
            }else{
                payload = null;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            //load headers
            for(HttpHeader header : getHeaders()){
                String headerKey = header.getName();
                String value = header.getValue();

                headers.set(headerKey, evaluateContent(instance, value).toString());
            }

            HttpEntity<String> body = new HttpEntity<String>(payload, headers);

            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

            // send request and parse result
            ResponseEntity<String> response = restTemplate
                    .exchange(realURI, httpMethod, body, String.class);

            result = response.getBody();

            /**
             *  parse the result by JSON PATH for each parameter contexts
             *  ******/

            if (result != null && getOutputMapping()!=null) {


                for (ParameterContext parameterContext : getOutputMapping()) {
                    Object value = null;
                    try {
                        value = JsonPath.read(result, parameterContext.getArgument().getText());
                    }catch(PathNotFoundException e){ //forgive for the path not found, it means just null
                        value = null;
                    }

                    parameterContext.getVariable().set(instance, "", (Serializable) value);
                }
            }

        } catch (HttpClientErrorException e) {
            if (isSkipIfNotFound() && "404".equals(e.getStatusCode())) {
                //skip
            } else {
                throw new UEngineException("A HTTP Exception: ", e);
            }
        }

        super.executeActivity(instance);
    }

    private RestTemplate noValidatingRestTemplate() {

        try{
            TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                    return true;
                }

            };

            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);
            RestTemplate restTemplate = new RestTemplate(requestFactory);

            return restTemplate;
        } catch (Exception e) {
            throw new RuntimeException("failed to establish a RestTemplate for no-validating certificate");
        }

    }

    private boolean checkLocalCall(ProcessInstance instance) throws Exception {

        ProcessDefinition processDefinition = getProcessDefinition();

        Activity localCallTarget = null;

        if(processDefinition.getMessageFlows()!=null && processDefinition.getMessageFlows().size() > 0)
        for(MessageFlow messageFlow : processDefinition.getMessageFlows()){

            if(getTracingTag().equals(messageFlow.getSourceRef()) && messageFlow.isLocalCall()){
                //this is local call
                localCallTarget = processDefinition.getActivity(messageFlow.getTargetRef());

                break;
            }
        }

        if(localCallTarget!=null){
            fireComplete(instance);
            instance.execute(localCallTarget.getTracingTag()); // this must be after the fireComplete

            return true;
        }

        return false;
    }

    Role role;
        public Role getRole() {
            return role;
        }
        public void setRole(Role role) {
            this.role = role;
        }


    String uriTemplate;
        public String getUriTemplate() {
            return uriTemplate;
        }

        public void setUriTemplate(String uriTemplate) {
            this.uriTemplate = uriTemplate;
        }

    String inputPayloadTemplate;
        public String getInputPayloadTemplate() {
            return inputPayloadTemplate;
        }
        public void setInputPayloadTemplate(String inputPayloadTemplate) {
            this.inputPayloadTemplate = inputPayloadTemplate;
        }

    HttpHeader[] headers;

        public HttpHeader[] getHeaders() {
            return headers;
        }

        public void setHeaders(HttpHeader[] headers) {
            this.headers = headers;
        }

    String method;
        public String getMethod() {
            return method;
        }
        public void setMethod(String method) {
            this.method = method;
        }

    boolean noValidationForSSL;
        public boolean isNoValidationForSSL() {
            return noValidationForSSL;
        }
        public void setNoValidationForSSL(boolean noValidationForSSL) {
            this.noValidationForSSL = noValidationForSSL;
        }

    ParameterContext[] outputMapping;
        public ParameterContext[] getOutputMapping() {
            return outputMapping;
        }
        public void setOutputMapping(ParameterContext[] outputMapping) {
            this.outputMapping = outputMapping;
        }

    public boolean isSkipIfNotFound() {
        return skipIfNotFound;
    }

    public void setSkipIfNotFound(boolean skipIfNotFound) {
        this.skipIfNotFound = skipIfNotFound;
    }



}
