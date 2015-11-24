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
		if (this.object.element && (this.object.element.tracingTag == null || typeof this.object.element.tracingTag == "undefined")) {
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



OG.shape.bpmn.A_Task.prototype.drawCustomControl = function(handler, element) {
	if (!handler || !element) return;

	//alert('xxx');

	handler._RENDERER.selectedElement = element;

	//if (element.shape instanceof OG.shape.bpmn.A_Task) {
    //
	//	//찍을 좌표 구하기
	//	//ur_x = element.shape.geom.boundary._upperLeft.x;
	//	//ur_y = element.shape.geom.boundary._upperLeft.y;
	//	//ur_x = element.shape.geom.boundary._width + ur_x;
     //   //
	//	//task = handler._RENDERER._PAPER.image("resources/images/symbol/guide_task.png", ur_x + 10, ur_y, 15, 15);
	//	//end = handler._RENDERER._PAPER.image("resources/images/symbol/guide_end.png", ur_x + 10, ur_y + 20, 15, 15);
    //
	//	var popup = {
	//		__className: "org.metaworks.widget.Popup",
    //
	//		panel: {
	//			__className: "org.metaworks.widget.Label",
	//			html: "<h3>test</h3>"
	//		}
    //
	//	};
    //
	//	mw3.locateObject(popup);
	//}

}