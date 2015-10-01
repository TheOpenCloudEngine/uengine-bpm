var org_uengine_kernel_bpmn_face_ProcessVariablePanel = function(objectId, className){
    this.objectId = objectId;
    this.objectDivId = mw3._getObjectDivId(this.objectId);
    this.objectDiv = $('#' + this.objectDivId);

    this.objectDiv.css({
        height: '30%',
        overflow: 'auto'
    });
}