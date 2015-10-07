var org_uengine_uml_model_ObjectInstance = function(objectId, className) {
    this.objectId = objectId;
    this.className = className;



}

org_uengine_uml_model_ObjectInstance.prototype.getValue = function(){

    var value = mw3.getObjectFromUI(this.objectId);

    if(value == null)
        return null;

    var objectMetadata = value.classDefinition;

    value.valueMap = {};

    if(!objectMetadata || !objectMetadata.fieldDescriptors){
        if(mw3.uengineUML){
            value.classDefinition = mw3.uengineUML.classDefinitions[value.className];
        }else{
            mw3.uengineUML = {classDefinitions:{}};
        }

        if(!value.classDefinition || !value.classDefinition.fieldDescriptors){
            value = value.fillClassDefinition(); //try to fill in
            mw3.uengineUML.classDefinitions[value.className] = value.classDefinition;
        }

        objectMetadata = value.classDefinition;
    }

    for(var i=0; i<objectMetadata.fieldDescriptors.length; i++){
        var fd = objectMetadata.fieldDescriptors[i];

        value.valueMap[fd.name] = value[fd.name];
    }

    return value;
}