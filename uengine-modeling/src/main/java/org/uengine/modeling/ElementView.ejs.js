var org_uengine_modeling_ElementView = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.object = mw3.objects[this.objectId];
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $(document.getElementById(this.objectDivId));
	if(!this.object) return true;
	
	this.isNew = true;
	this.metadata = mw3.getMetadata(this.className);
	
	
	this.getValue = function(){
		if(this.element){
			if($('#' + this.element.id).length == 0)
				return {__objectId: this.objectId, __className: this.className};
			
			this.element = document.getElementById(this.element.id);
			this.object.label = this.element.shape.label;
			this.object.x = this.element.shape.geom.getBoundary().getCentroid().x;
			this.object.y = this.element.shape.geom.getBoundary().getCentroid().y;
			this.object.width = this.element.shape.geom.getBoundary().getWidth();
			this.object.height = this.element.shape.geom.getBoundary().getHeight();
			this.object.id = this.element.id;
			this.object.shapeId = this.element.shape.SHAPE_ID;
			this.object.style = escape(OG.JSON.encode(this.element.shapeStyle));
			this.object.toEdge = $(this.element).attr('_toedge');
			this.object.fromEdge = $(this.element).attr('_fromedge');			
			this.object.index = $(this.element).prevAll().length;
			
			return this.object;
		}
	}
	
	
	this.getLabel = function(){
		if(this.object.element)
			this.object.label = this.object.element.name;
		//mw3.getObjectNameValue(this.object.element, true);
		
		return unescape(this.object.label?this.object.label:'');
	}
	
	this.getCanvas = function(){
		
		//var canvasId = this.objectDiv.closest('.canvas').attr('objectId');
		var canvasId = this.objectDiv.closest('.canvas').attr('id').split("_")[1];
		
		return mw3.getFaceHelper(canvasId).getCanvas();
	}
	
	this.init = function(){
        this.canvas = this.getCanvas();

        this.element = null;

		//verification data first.
		if(this.object.shapeId == null)
			throw new Error("No shape Id is set for " + this.object);

        var existElement = document.getElementById(this.object.id);
        if(existElement){
            this.canvas.drawLabel(existElement, this.getLabel());
            this.element = existElement;
            this.isNew = false;
            
        }else{


			var shape = eval('new ' + this.object.shapeId);
			shape.label = this.getLabel();

            var style = this.object.style;
            var boundary;

            this.element = this.canvas.drawShape([this.object.x, this.object.y], 
                    shape, 
                    [parseInt(this.object.width, 10), parseInt(this.object.height, 10)],
                    OG.JSON.decode(unescape(style)),
                    this.object.id,
                    null,
                    null);
            
            boundary = this.element.shape.geom.boundary;
    
            this.autoResizeCanvas(boundary);
            
            this.object[this.metadata.keyFieldDescriptor.name] = this.element.id;
            
            mw3.putObjectIdKeyMapping(this.objectId, this.object, true);
        }
        

        if (this.object.toEdge) {
            $(this.element).attr('_toedge', this.object.toEdge);
        }
        
        if (this.object.fromEdge) {
            $(this.element).attr('_fromedge', this.object.fromEdge);
        }

        boundary = this.element.shape.geom.boundary;

        this.autoResizeCanvas(boundary);
        
        this.object[this.metadata.keyFieldDescriptor.name] = this.element.id;
        
        mw3.putObjectIdKeyMapping(this.objectId, this.object, true);
        
        $(this.element).trigger('loaded.' + this.element.id);
        
        this.bindMapping();
    }
    
	this.bindMapping = function(){
		var metadata = mw3.getMetadata(this.className);
		for(var methodName in metadata.serviceMethodContextMap){
			if(mw3.isHiddenMethodContext(this.metadata.serviceMethodContextMap[methodName], this.object))
		 		continue;
			
			var methodContext = metadata.serviceMethodContextMap[methodName];
			
			if(methodContext.eventBinding){
				for(var eventNameIndex in methodContext.eventBinding){
					var eventName = methodContext.eventBinding[eventNameIndex];
					
					this.bind(eventName);
				}
			}
			
			if(methodContext.mouseBinding){
   				var which = 3;
   				if(methodContext.mouseBinding == "right")
   					which = 3;
   				else if(methodContext.mouseBinding == "left")
   					which = 1;
   				
				if(methodContext.mouseBinding == "drop"){
					
					$(this.element).droppable({
							greedy: true,
							tolerance: 'geom'				
					}).attr('droppable', true);
	
					var command = "if(mw3.objects['"+ this.objectId +"']!=null) mw3.call("+this.objectId+", '"+methodName+"')";
					$(this.element).on('drop.' + this.objectId, {command: command}, function(event, ui){
						if(Object.prototype.toString.call(ui.draggable[0]) != "[object SVGGElement]"){
							if(Object.prototype.toString.call(ui.draggable[0]) != "[object SVGRectElement]"){
								eval(event.data.command);
							}
						}									
					});
				}else{
					// click(mouse right) is contextmenu block
	   			 	if(which == 3){
	   			 	
		   			 	$(this.element).bind('contextmenu', function(event){
		   			 		return false;
		   			 	}); 
	   			 	}
	   			 				
   			 		$(this.element).on((which==3?'mouseup':'click') + '.'+this.objectId, {which: which, objectId: this.objectId}, function(event){
   			 			$(document.getElementById(mw3._getObjectDivId(event.data.objectId))).trigger(event);
   			 		});
	   			 	
				}
			}
		}				
	}
	
	this.bind = function(name){
		$(this.element).bind(name + '.' + this.objectId, {objectId: this.objectId}, function(event, ui){
			$(document.getElementById(mw3._getObjectDivId(event.data.objectId))).trigger(event.type);
		});		
	}
	
	this.destroy = function(){
		if($(this.element).attr('droppable'))
			$(this.element).droppable( "destroy" );
		
		$(this.element).unbind('.' + this.objectId);
	}
	
	this.autoResizeCanvas = function(boundary){
		var rootBBox = this.canvas._RENDERER.getRootBBox();
		if(rootBBox.width < (boundary._centroid.x + boundary._width) * this.canvas._CONFIG.SCALE){
			this.canvas._RENDERER.setCanvasSize([boundary._centroid.x + boundary._width, rootBBox.height]);
		}
		if(rootBBox.height < (boundary._centroid.y + boundary._height) * this.canvas._CONFIG.SCALE){
			this.canvas._RENDERER.setCanvasSize([rootBBox.width, boundary._centroid.y + boundary._height]);
		}
	}
	
	this.init();
};