var org_uengine_uml_model_ObjectInstance = function(objectId, className) {
    this.objectId = objectId;
    this.className = className;



}

org_uengine_uml_model_ObjectInstance.prototype.getValue = function(){

    var gottenFromUI = mw3.getObjectFromUI(this.objectId);

    var objectMetadata = gottenFromUI.classDefinition;

    gottenFromUI.valueMap = {};

    for(var i=0; i<objectMetadata.fieldDescriptors.length; i++){
        var fd = objectMetadata.fieldDescriptors[i];

        gottenFromUI.valueMap[fd.name] = gottenFromUI[fd.name];
    }

    return gottenFromUI;
}