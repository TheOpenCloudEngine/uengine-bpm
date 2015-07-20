var org_uengine_webservice_ApplyProperties = function(objectId, className){
	this.objectId = objectId;
	this.className = className;

	debugger;
	
	this.object = mw3.objects[this.objectId];
	var object = this.object;
//	mw3.removeObject(objectId);	이렇게 지우는게 맞는건지 헷갈림
	var canvasObject;
	if( object != null && object.viewType != null && "blockView" == object.viewType ){
		canvasObject = mw3.getAutowiredObject('org.uengine.codi.mw3.webProcessDesigner.InstanceMonitorPanel');
	}else	if( object != null && object.viewType != null && ("definitionView" == object.viewType || "definitionEditor" == object.viewType  || "definitionDiffEdit" == object.viewType  || "definitionDiffView" == object.viewType)){
		if( object.editorId ){
			canvasObject = mw3.getAutowiredObject('org.uengine.codi.mw3.webProcessDesigner.ProcessViewer@'+object.editorId);
		}else{
			canvasObject = mw3.getAutowiredObject('org.uengine.codi.mw3.webProcessDesigner.ProcessViewer');
		}
	}else{
		if( object.editorId ){
			canvasObject = mw3.getAutowiredObject('org.uengine.codi.mw3.webProcessDesigner.ProcessDesignerContentPanel@'+object.editorId);
		}else{
			canvasObject = mw3.getAutowiredObject('org.uengine.modeling.Canvas');
		}
	}
	
	if( canvasObject == null ){
		canvasObject = mw3.getAutowiredObject('org.uengine.codi.mw3.webProcessDesigner.ValueChainDesignerContentPanel');
	}
	var canvasObjectFaceHelper = mw3.getFaceHelper(canvasObject.__objectId);
	var canvas = canvasObjectFaceHelper.canvas;
	
	var contentValue = this.object.content;
	var element = document.getElementById(this.object.id);
	if(contentValue && contentValue.__className=="org.uengine.kernel.Role"){
		canvas.drawLabel(element, contentValue.displayName.text);
		$('#' + this.object.id).data('role', contentValue);
	}else if(contentValue && contentValue.__className=="org.uengine.kernel.ValueChain"){
		canvas.drawLabel(element, contentValue.name.text);
		$('#' + this.object.id).data('valuechain', contentValue);
	}else if(contentValue && contentValue.__className=="org.uengine.kernel.graph.Transition"){
		canvas.drawLabel(element, contentValue.transitionName);
		$('#' + this.object.id).data('transition', contentValue);
	}else if(contentValue && contentValue.__className=="org.uengine.kernel.bpmn.Pool"){
		canvas.drawLabel(element, contentValue.description.text);
		$('#' + this.object.id).data('pool', contentValue);
		// 동적으로 엑티비티를 그려주는 메서드 호출이지만, 스펙에서 제외시킴
//		if( confirm('reload web service with activity?') ){
		var poolview = mw3.getAutowiredObject('org.uengine.kernel.bpmn.view.PoolView');
		var pool = mw3.getAutowiredObject('org.uengine.kernel.bpmn.Pool');
		pool = contentValue;
		if( poolview ){
			poolview.element = contentValue;
			poolview.drawActivitysOnDesigner();
		}
//		}
	}else if(contentValue && contentValue.__className=="org.uengine.kernel.graph.PoolTransition"){
		canvas.drawLabel(element, contentValue.transitionName);
		$('#' + this.object.id).data('poolTransition', contentValue);
	}else{
		// activity
		canvas.drawLabel(element, contentValue.description.text);
		$('#' + this.object.id).data('element', contentValue);
	}
	
	$('#' + this.object.id).trigger('apply');
};