var org_uengine_uml_model_ObjectInstance = function(objectId, className) {
    this.objectId = objectId;
    this.className = className;



}

org_uengine_uml_model_ObjectInstance.prototype.getValue = function(){

    var valueFromUI = mw3.getObjectFromUI(this.objectId);

    if(valueFromUI == null)
        return null;

    var objectMetadata = valueFromUI.classDefinition;

    valueFromUI.valueMap = {};

    if(!(objectMetadata && objectMetadata.fieldDescriptors) && mw3.uengineUML) {
        objectMetadata = mw3.uengineUML.classDefinitions[valueFromUI.className];
    }

    for(var i=0; i<objectMetadata.fieldDescriptors.length; i++){
        var fd = objectMetadata.fieldDescriptors[i];

        valueFromUI.valueMap[fd.name] = valueFromUI[fd.name];
    }

    return valueFromUI;
}