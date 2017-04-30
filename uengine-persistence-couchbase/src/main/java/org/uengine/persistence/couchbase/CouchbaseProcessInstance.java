package org.uengine.persistence.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.view.ViewResult;
import org.uengine.kernel.EJBProcessInstance;
import org.uengine.kernel.ProcessDefinition;

import java.util.Map;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.x;

/**
 * Created by jangjinyoung on 2017. 3. 27..
 */
public class CouchbaseProcessInstance extends EJBProcessInstance {
    public CouchbaseProcessInstance(ProcessDefinition procDef, String name, Map options) throws Exception {
        super(procDef, name, options);
    }

    public static void main (String... args) throws Exception{
// Connect to localhost

        CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
                .queryEnabled(true) //that's the important part
                .build();
        CouchbaseCluster cluster = CouchbaseCluster.create(env, "127.0.0.1");
        //Cluster cluster = CouchbaseCluster.create();

// Open the default bucket and the "beer-sample" one
        Bucket defaultBucket = cluster.openBucket();
        Bucket beerSampleBucket = cluster.openBucket("beer-sample");

        JsonObject user = JsonObject.empty()
                .put("instId", "50")
                .put("key", "White")
                .put("value", "chemistry teacher");

        JsonDocument stored = beerSampleBucket.upsert(JsonDocument.create("instId-fullkey(execscope:key)", user));

        JsonDocument walter = beerSampleBucket.get("instId-fullkey(execscope:key)");
        System.out.println("Found: " + walter.content().getString("key"));


        user = JsonObject.empty()
                .put("instId", "50")
                .put("key", "xxxx")
                .put("value", "value 2");

        beerSampleBucket.upsert(JsonDocument.create("instId-fullkey(xxxx:xxxx)", user));

        N1qlQueryResult result =
                beerSampleBucket.query(N1qlQuery.simple("SELECT * FROM `beer-sample` WHERE `instId`=\"50\""));//select("*").from("beer-sample").where(x("instId").eq(x("50")))));

//        QueryResult result =
//                beerSampleBucket.query(Query.simple("SELECT * FROM `beer-sample` WHERE `instId`=\"50\""));

        for (N1qlQueryRow row : result) {
            JsonObject doc = row.value();

            System.out.println(((JsonObject)doc.get("beer-sample")).getString("instId"));
        }

// Disconnect and clear all allocated resources
        cluster.disconnect();


    }
}
