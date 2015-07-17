var org_uengine_contexts_MappingTree = function(objectId, className){
	// default setting
	this.objectId = objectId;
	this.className = className;
	
	var object = mw3.objects[this.objectId];
	if(object != null && !object.preLoaded){
		object.init();
	}
};