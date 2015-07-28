var org_uengine_modeling_modeler_condition_ConditionTreeNodeView = function(objectId, className){
    // default setting
    this.objectId = objectId;
    this.className = className;
};

org_uengine_modeling_modeler_condition_ConditionTreeNodeView.prototype = {
    getValue : function(){
        var object = mw3.objects[this.objectId];
        return object;
    }
};