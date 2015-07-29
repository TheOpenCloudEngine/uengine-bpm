var org_uengine_modeling_modeler_condition_ConditionPanel = function(objectId, className){
    // default setting
    this.objectId = objectId;
    this.className = className;
    this.object = mw3.objects[this.objectId];
    this.objectDivId = mw3._getObjectDivId(this.objectId);
    this.objectDiv = $('#' + this.objectDivId);

    this.objectDiv.css({
        width: '600px'
    });

};

