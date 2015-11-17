# Demo




# Sub modules

* uengine-core is the process execution engine for uEngine-BPMS
* bpmn-model is Java class model for BPMN 2.0 generated from JAXB and the bridges (adapter) for uenigne-core model
* uengine-bpmn-modeler is a web based BPMN process modeler based on Metaworks3 and UMF (uEngine modeling framework)
* uengine-modeling is the Uengine Modeling Framework (UMF), which is a pure-Java based graph modeling framework based on OpenGraph (https://github.com/TheOpenCloudEngine/OpenGraph).
* uengine-client-example shows how you can integrate with uEngine BPM and your project.


# Building uEngine BPMS

Just type 'mvn install' after cloning this repo. that's it.


# Running uEngine BPMS

## Installing database
 install the database and change the database link in the configuration - uengine.properties in the <home>/uengine-social-bpm-portal/main/resources/org/uengine/uengine.properties like below:

`
codi.jdbc.url=jdbc:mysql://localhost:3306/uengine?useUnicode=true&characterEncoding=utf8&useOldAliasMetadataBehavior=true
codi.jdbc.username=root
codi.jdbc.password=
codi.jdbc.validationQuery=SELECT 1
`

change the codi.jdbc.url and the credentials with the database information you've installed.

And you will need to install default database schema, you can get the database DDL script here - https://github.com/TheOpenCloudEngine/process-codi/blob/master/src/main/webapp/resources/mysql/processcodi.sql

## Running tomcat

`
mvn tomcat7:run-war
`
and go to - localhost:8080/uengine-social-bpm-portal


# uEngine Process Meta-model

### The mixture of block-based model and graph-model  




