var org_uengine_kernel_view_ActivityView = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.object = mw3.objects[this.objectId];

	this.tracingTag = function(){
		var modeler = mw3.getAutowiredObject('org.uengine.modeling.Modeler');

		this.object.element.tracingTag = modeler.getFaceHelper().getTracingTag();

		mw3.putObjectIdKeyMapping(this.object.element.__objectId, this.object.element, true);
	}

	this.initApply = function(){
		org_uengine_modeling_ElementView.apply(this, new Array(this.objectId, this.className));

		//this.prototype = org_uengine_modeling_ElementView.prototype;

		if(this.object.element && (this.object.element.tracingTag == null || typeof this.object.element.tracingTag == "undefined")) {
			this.tracingTag();
		}

	}


	var superScript = 'dwr/metaworks/org/uengine/modeling/ElementView.ejs.js';
	if(!mw3.loadedScripts[superScript]){
		mw3.importScript(superScript, function(){
			mw3.getFaceHelper(objectId).initApply();
		})
	}else{
		this.initApply();
	}





}