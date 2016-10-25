var org_uengine_modeling_resource_EditorPanel = function(objectId, className){
    var objectDivId = mw3._getObjectDivId(objectId);
    var element = document.getElementById(objectDivId);
    element.style.height = '100%';

    if(mw3.objects[objectId].resourceName){
        window.title = mw3.objects[objectId].resourceName;
    }

}