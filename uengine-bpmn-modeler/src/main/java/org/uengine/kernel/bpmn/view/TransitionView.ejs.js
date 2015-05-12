var org_uengine_kernel_graph_view_TransitionView = function(objectId, className){
	
	var faceHelper = this;

	this.objectId = objectId;
	this.className = className;
	this.object = mw3.objects[this.objectId];
	
	if(!this.object) return true;
	
	this.metadata = mw3.getMetadata(this.className);
	
	this.canvas = faceHelper.getCanvas();
	this.element = null;
	
	faceHelper.init();
};

org_uengine_kernel_graph_view_TransitionView.prototype = {
	getValue : function(){
		this.object.label = this.element.shape.label;
		this.object.x = this.element.shape.geom.getBoundary().getCentroid().x;
		this.object.y = this.element.shape.geom.getBoundary().getCentroid().y;
		this.object.width = this.element.shape.geom.getBoundary().getWidth();
		this.object.height = this.element.shape.geom.getBoundary().getHeight();
		this.object.id = this.element.id;
		this.object.shapeId = this.element.shape.SHAPE_ID;
	},
	
	getLabel : function(){
		this.object.label = mw3.getObjectNameValue(this.object.element);
		
		return unescape(this.object.label!=null?this.object.label:'');
	},
	getCanvas : function(){
		var object = mw3.getAutowiredObject('org.uengine.modeling.Canvas');
		return object.getFaceHelper().getCanvas();
	},	
	init : function(){
		var shape = eval('new ' + this.object.shapeId + '(\'' + this.getLabel() + '\')');
		
		var existElement = document.getElementById(this.object.id);
		if(existElement){
			this.canvas.drawLabel(existElement, this.getLabel());
			
			this.element = existElement;
		}else{
			this.element = this.canvas.drawShape([this.object.x, this.object.y], 
                    shape, 
                    [parseInt(this.object.width, 10), parseInt(this.object.height, 10)],
                    null,
                    this.object.id,
                    null,
                    null);
			
			this.object[this.metadata.keyFieldDescriptor.name] = this.element.id;
			
			mw3.putObjectIdKeyMapping(this.objectId, this.object, true);
		}
		
		var metadata = mw3.getMetadata(this.className);
		for(var methodName in metadata.serviceMethodContextMap){
			var methodContext = metadata.serviceMethodContextMap[methodName];
			
			if(methodContext.eventBinding){
				for(var eventNameIndex in methodContext.eventBinding){
					var eventName = methodContext.eventBinding[eventNameIndex];
					
					this.bind(eventName);
				}
			}
			
		}
	},
	
	bind : function(name){
		$(this.element).bind(name, {objectId: this.objectId}, function(event, ui){
			$(document.getElementById(mw3._getObjectDivId(event.data.objectId))).trigger(event.type);
		});		
	}
};