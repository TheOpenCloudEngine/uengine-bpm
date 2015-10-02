var org_uengine_modeling_resource_editor_ClassEditor = function(objectId){
    this.objectId = objectId;
    this.objectDivId = mw3._getObjectDivId(this.objectId);
    this.objectDiv = $('#' + this.objectDivId);

    this.objectDiv.css({
        height: '88%',
        overflow: 'auto'
    });
}