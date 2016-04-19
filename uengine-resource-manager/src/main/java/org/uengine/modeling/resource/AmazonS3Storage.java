package org.uengine.modeling.resource;

import com.thoughtworks.xstream.XStream;
import org.jets3t.service.Constants;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.model.StorageObject;
import org.jets3t.service.security.AWSCredentials;
import org.metaworks.MetaworksContext;
import org.metaworks.MetaworksException;
import org.oce.garuda.multitenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ExceptionDepthComparator;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoo.lim on 7/21/2015.
 */
public class AmazonS3Storage implements Storage{

    private final static String S3_SPERATOR = "/";

    String amazonS3Bucket;

    String awsAccessKey;

    String awsSecretAccessKey;

    public String getAmazonS3Bucket() {
        return amazonS3Bucket;
    }

    public void setAmazonS3Bucket(String amazonS3Bucket) {
        this.amazonS3Bucket = amazonS3Bucket;
    }

    public String getAwsAccessKey() {
        return awsAccessKey;
    }

    public void setAwsAccessKey(String awsAccessKey) {
        this.awsAccessKey = awsAccessKey;
    }

    public String getAwsSecretAccessKey() {
        return awsSecretAccessKey;
    }

    public void setAwsSecretAccessKey(String awsSecretAccessKey) {
        this.awsSecretAccessKey = awsSecretAccessKey;
    }

    RestS3Service restS3Service;

    @PostConstruct
    public void init() {
        AWSCredentials awsCredentials = new AWSCredentials(getAwsAccessKey(), getAwsSecretAccessKey());
        restS3Service = new RestS3Service(awsCredentials);

        try {
            restS3Service.getOrCreateBucket(getAmazonS3Bucket());
        } catch (S3ServiceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(IResource resource) {
        try {
            restS3Service.deleteObject(getAmazonS3Bucket(), getS3Path(resource.getPath()));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rename(IResource resource, String newName) {
        try {
            restS3Service.renameObject(getAmazonS3Bucket(), getS3Path(resource.getPath()), getS3Object(newName));
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void copy(IResource src, String desPath) throws ServiceException, IOException, NoSuchAlgorithmException {
        copy(getS3Path(src.getPath()), desPath);
    }

    //TODO: find out the signal for copying multiple files in the folder someday for performance issue.
    private void copy(String srcPath, String desPath) throws ServiceException, IOException, NoSuchAlgorithmException {
        S3Object[] s3Objects = restS3Service.listObjects(getAmazonS3Bucket(), srcPath, null);

        for(S3Object s3Object : s3Objects) {
            String path = s3Object.getKey();

//            if (path.endsWith("/")) {
//                copy(path, desPath);
//            }else{

                String desObjKey = desPath + path.substring(srcPath.length());
                restS3Service.copyObject(getAmazonS3Bucket(), path, getAmazonS3Bucket(), getS3Object(desObjKey), true);
            //}

        }
    }

    @Override
    public List<IResource> listFiles(IContainer containerResource) throws Exception {
        List<IResource> resourceList = new ArrayList<>();

        String containerPath = getS3Path(containerResource.getPath());
        S3Object[] s3Objects = restS3Service.listObjects(getAmazonS3Bucket(), containerPath, null);

        List<IResource> childList = null;
        for(S3Object s3Object : s3Objects){
            String path = getResourcePath(s3Object.getKey());
            if ( (containerResource.getPath() + "/").equals(path) )
                continue;
            if(path.endsWith("/")) {
                ContainerResource subContainerResource = (ContainerResource) containerResource.getClass().newInstance();

                subContainerResource.setPath(path.substring(0, path.length()-1));
                subContainerResource.setMetaworksContext(new MetaworksContext());
                if ( childList == null ) {
                    resourceList.add(subContainerResource);
                } else {
                    childList.add(subContainerResource);
                }
                childList = new ArrayList<>();
                subContainerResource.setChildren(childList);
            } else {
                if ( childList == null) {
                    resourceList.add(DefaultResource.createResource(path));
                } else {
                    childList.add(DefaultResource.createResource(path));
                }
            }
        }

        return resourceList;
    }

    @Override
    public void createFolder(IContainer containerResource) throws Exception {
        final StorageObject object = new StorageObject(getS3Path(containerResource.getPath()) + "/");
        object.setBucketName(getAmazonS3Bucket());
        object.setContentLength(0);
        object.setContentType("application/x-directory");
        restS3Service.putObject(getAmazonS3Bucket(), object);
//        S3Object object = new S3Object("object");
//        object.setName(getS3Path(containerResource.getPath()));
//        restS3Service.putObject(getAmazonS3Bucket(), object);

    }

    @Override
    public boolean exists(IResource resource) throws Exception {
        return restS3Service.isObjectInBucket(getAmazonS3Bucket(), getS3Path(resource.getPath()));
    }

    @Override
    public Object getObject(IResource resource) throws Exception {
        S3Object s3Object = restS3Service.getObject(getAmazonS3Bucket(), getS3Path(resource.getPath()));

        //XStream xstream = new XStream();

        Object object = Serializer.deserialize(s3Object.getDataInputStream());

        return object;
    }

    @Override
    public void save(IResource resource, Object object) throws Exception {
        //XStream xstream = new XStream();
        String objectXml = Serializer.serialize(object);

        // Create an object containing a greeting string as input stream data.
        S3Object s3Object = getS3Object(resource.getPath());
        ByteArrayInputStream objectIS = new ByteArrayInputStream(objectXml.getBytes(StandardCharsets.UTF_8));
        s3Object.setDataInputStream(objectIS);
        s3Object.setContentLength(objectXml.getBytes(StandardCharsets.UTF_8).length);
        s3Object.setContentType("text/" + resource.getType());

        s3Object = restS3Service.putObject(getAmazonS3Bucket(),s3Object);

        //xstream.toXML(s3Object,System.out);
    }

    @Override
    public InputStream getInputStream(IResource resource) throws Exception {
        S3Object s3Object = restS3Service.getObject(getAmazonS3Bucket(), getS3Path(resource.getPath()));
        return s3Object.getDataInputStream();
    }

    @Override
    public OutputStream getOutputStream(IResource resource) throws Exception {
//        XStream xstream = new XStream();
//
//        S3Object s3Object = getS3Object(resource.getPath());
        return null; //TODO: must be implemented
    }

    private S3Object getS3Object(String path) throws IOException, NoSuchAlgorithmException {
        return new S3Object(getS3Path(path));
    }

    private String getS3Path(String path){
        String tenantBasePath = getTenantBasePath();

        String fullPath = (tenantBasePath + path).replace("/", S3_SPERATOR);

        return fullPath;
    }

    private String getResourcePath(String path){
        String basePath = getTenantBasePath();
        int len = basePath.length();
        return path.substring(len).replace(S3_SPERATOR,"/");
    }

    private String getTenantBasePath() {
        String tenantId = TenantContext.getThreadLocalInstance().getTenantId();

        if(tenantId==null){
            tenantId = "default";
        }

        return tenantId + "/";
    }

    @Override
    public void move(IResource src, IContainer container) {
        try {
            String bucketName = getAmazonS3Bucket();
            restS3Service.moveObject(bucketName, src.getName(), bucketName,
                    getS3Object(container.getPath() + "/" + src.getName()), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
