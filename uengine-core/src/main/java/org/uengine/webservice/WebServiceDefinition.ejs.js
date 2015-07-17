var org_uengine_webservice_WebServiceDefinition = function(objectId, className){
    
    this.objectId = objectId;
    this.className = className;
    
};

org_uengine_webservice_WebServiceDefinition.prototype = {
    toOpener : function(target){
        var object = mw3.objects[this.objectId];
        debugger;
        if(target && target.__className == 'org.uengine.uengine.MethodProperty' ){
            object.targetMethod = target;
        }
        object.changeMappingContext();
    }
};

