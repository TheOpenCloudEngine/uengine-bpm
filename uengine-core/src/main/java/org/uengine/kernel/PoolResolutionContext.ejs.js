var org_uengine_kernel_PoolResolutionContext = function(objectId, className){
    
    this.objectId = objectId;
    this.className = className;
	
};

org_uengine_kernel_PoolResolutionContext.prototype = {
    toOpener : function(target){
        var object = mw3.objects[this.objectId];
		if(target && target.__className == 'org.uengine.codi.mw3.marketplace.AppMapping' ){
	        object.linkedId = target.appId + '';
		}
		object.refresh();
    }
};

