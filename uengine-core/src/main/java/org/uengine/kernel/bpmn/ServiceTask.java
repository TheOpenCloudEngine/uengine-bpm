package org.uengine.kernel.bpmn;

import com.jayway.jsonpath.JsonPath;
import org.metaworks.dwr.MetaworksRemoteService;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.uengine.kernel.*;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

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

        Role role = getRole();

        String fullURITemplate = null;
        String serviceId = null;

        if (role == null || getUriTemplate().startsWith("http")) {
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

                        fullURITemplate = baseUrl + getUriTemplate();
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
            RestTemplate restTemplate = new RestTemplate();

            if ("GET".equals(getMethod())) {

                ResponseEntity<String> response = null;
                response = restTemplate.exchange(realURI,
                        HttpMethod.GET, null, String.class);

                result = response.getBody();

            } else if ("POST".equals(getMethod())) {

                payload =
                        evaluateContent(instance, getInputPayloadTemplate()).toString();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> body = new HttpEntity<String>(payload, headers);

                restTemplate.getMessageConverters()
                        .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

                // send request and parse result
                ResponseEntity<String> response = restTemplate
                        .exchange(realURI, HttpMethod.POST, body, String.class);

                result = response.getBody();
            }

            /**
             *  parse the result by JSON PATH for each parameter contexts
             *  ******/

            if (result != null && getOutputMapping()!=null) {


                for (ParameterContext parameterContext : getOutputMapping()) {
                    Object value = JsonPath.read(result, parameterContext.getArgument().getText());

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

    String method;
        public String getMethod() {
            return method;
        }
        public void setMethod(String method) {
            this.method = method;
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
