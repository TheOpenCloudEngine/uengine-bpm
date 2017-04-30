package org.uengine.persistence.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import org.metaworks.MetaworksContext;
import org.uengine.modeling.resource.*;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jangjinyoung on 2017. 3. 28..
 */
public class CouchbaseStorage extends AbstractStorage {


    String serverIp;
        public String getServerIp() {
            return serverIp;
        }
        public void setServerIp(String serverIp) {
            this.serverIp = serverIp;
        }

    String bucketName;
        public String getBucketName() {
            return bucketName;
        }
        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }

    CouchbaseCluster cluster;

    @PostConstruct
    public void init(){
        CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
                .queryEnabled(true) //that's the important part
                .build();

        cluster = CouchbaseCluster.create(env, getServerIp());
//
//        try{
//            getBucket();
//        }catch (Exception e){
//            cluster.
//        }

    }

    protected Bucket getBucket(){
        return cluster.openBucket(getBucketName());
    }

    @Override
    public void delete(IResource resource) throws IOException {

        getBucket().remove(getAbsolutePath(resource));

    }

    @Override
    public void rename(IResource resource, String newName) {


    }

    @Override
    public void copy(IResource src, String desPath) throws Exception {
        String value = getSource(src);
        DefaultResource destResource = new DefaultResource(desPath);

        saveSource(destResource, value);
    }

    @Override
    public void move(IResource src, IContainer container) throws IOException {

    }

    @Override
    public List<IResource> listFiles(IContainer containerResource) throws Exception {

        List<IResource> resourceList = new ArrayList<IResource>();

        String tenantBasePath = getTenantBasePath();
        String abstractTenantBasePath = new File(tenantBasePath).getAbsolutePath();


        N1qlQueryResult result =
                getBucket().query(N1qlQuery.simple("SELECT META(t).id, t.type as type FROM `" + getBucketName() + "` AS t WHERE `parent`=\"" + getAbsolutePath(containerResource) + "\""));


        for (N1qlQueryRow row : result) {
            JsonObject doc = row.value();

            String path = doc.getString("id");
            String type = doc.getString("type");

            String relativePath = path.replace("\\", "/");
            relativePath = relativePath.substring(abstractTenantBasePath.length() + 1);

            if("folder".equals(type)){
                ContainerResource subContainerResource = (ContainerResource) containerResource.getClass().newInstance();

                subContainerResource.setPath(relativePath);
                subContainerResource.setMetaworksContext(new MetaworksContext());
                subContainerResource.setChildren(listFiles(subContainerResource));

                resourceList.add(subContainerResource);
            }else{
                resourceList.add(DefaultResource.createResource(relativePath));
            }
        }

        return resourceList;
    }

    @Override
    public void createFolder(IContainer containerResource) throws Exception {
        JsonObject jsonObject = JsonObject.empty()
                .put("parent", getParentAbsolutePath(containerResource))
                .put("type", "folder");

        JsonDocument stored = getBucket().upsert(JsonDocument.create(getAbsolutePath(containerResource), jsonObject));

    }

    @Override
    public boolean exists(IResource resource) throws Exception {
        return getBucket().exists(getAbsolutePath(resource));
    }

    @Override
    public Object getObject(IResource resource) throws Exception {

        String value = getSource(resource);

        return Serializer.deserialize(value);
    }

    private String getSource(IResource resource) {
        JsonDocument document = getBucket().get(getAbsolutePath(resource));
        return document.content().getString("value");
    }

    @Override
    public void save(IResource resource, Object object) throws Exception {
        saveSource(resource, Serializer.serialize(object));
    }

    private JsonDocument saveSource(IResource resource, String sourceString) throws Exception {
        JsonObject jsonObject = JsonObject.empty()
                .put("parent", getParentAbsolutePath(resource))
                .put("value", sourceString);



        JsonDocument stored = getBucket().upsert(JsonDocument.create(getAbsolutePath(resource), jsonObject));

        return stored;
    }

    @Override
    public InputStream getInputStream(IResource resource) throws Exception {
        String value = getSource(resource);

        ByteArrayInputStream bai = new ByteArrayInputStream(value.getBytes());

        return bai;
    }

    @Override
    public OutputStream getOutputStream(IResource resource) throws Exception {
        return null;
    }
}
