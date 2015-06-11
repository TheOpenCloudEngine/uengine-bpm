var org_uengine_kernel_view_ValueChainView = function(objectId, className){
	
	this.objectId = objectId;
	this.className = className;
	
	var object = mw3.objects[this.objectId];
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
			canvasObject = mw3.getAutowiredObject('org.uengine.codi.mw3.webProcessDesigner.ProcessDesignerContentPanel');
		}
	}
	var canvasObjectFaceHelper = mw3.getFaceHelper(canvasObject.__objectId);
	this.canvas = canvasObjectFaceHelper.icanvas;
	
};

org_uengine_kernel_view_ValueChainView.prototype = {
		loaded : function(){
			var object = mw3.objects[this.objectId];
			var canvas = this.canvas;
			var element = null;
			var initText = ( object.label == null || object.label == 'undefined' ) ? "valuechain" : unescape(object.label);
			var shape = eval('new ' + object.shapeId + '(\''+initText +'\')');
			var id = object.id;
			var style = object.style;
        	element = canvas.drawShape([
        	                                 object.x, object.y 
        	                                 ], 
        	                                 shape, [parseInt(object.width, 10), parseInt(object.height, 10)] , OG.JSON.decode(unescape(style)), id, null, false);
        	
        	$(element).attr("_classname", object.activityClass);
        	$(element).attr("_viewClass", object.__className);
        	$(element).attr("_classType", object.classType);
        	if( object.valueChain ){
        		$(element).data('valuechain',object.valueChain);
        	}else if( typeof $(element).attr("_classname") != 'undefined' &&  typeof $(element).data("valuechain") == 'undefined' ){
        		var activityData = {__className : $(element).attr("_classname")};
        		$(element).data('valuechain', activityData);
        	}
        	if( !object.id ){
                // 새롭게 그려진 경우 ID를 부여하여 keymapping 시켜줌
                object.id = $(element).attr('id');
                var objKeys = mw3._createObjectKey(object, true);
                if(objKeys && objKeys.length){
                    for(var i=0; i<objKeys.length; i++){
                        mw3.objectId_KeyMapping[objKeys[i]] = Number(this.objectId);
                    }
                }
            }
        	$(element).unbind('dblclick').bind('dblclick' , function(event){
        			var name = {
        					__className : 'org.uengine.contexts.TextContext',
        					text : this.textContent
        			};
        			object.id = $(this).attr('id');
        			object.valueChain = $(this).data('valuechain');
        			object.valueChain.name = name;
        			object.showValueChainMonitor();
        	});
			
				
			/*
			$(element).attr('title', object.tooltip);
			$(element).hover(function(event, ui) {
				  $('body').append('<div id=\"shape_tooltip\" style=\"z-index: 1000; position: absolute; width: 200px; height: 30px; background-color: lightgray; text-align: center; padding: 15px 0px 0px; top: ' + event.pageY + 'px; left: ' + event.pageX + 'px\">' + object.tooltip + '</div>');
			}, function(){
				$('#shape_tooltip').remove();
			});
			*/
		}
};