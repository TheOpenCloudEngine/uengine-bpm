
var org_uengine_kernel_view_WebServiceActivityView = function(objectId, className){
    
    this.objectId = objectId;
    this.className = className;
    
    var object = mw3.objects[this.objectId];
    var canvasObject;
    if( object != null && object.viewType != null && "blockView" == object.viewType ){
        canvasObject = mw3.getAutowiredObject('org.uengine.codi.mw3.webProcessDesigner.InstanceMonitorPanel');
    }else   if( object != null && object.viewType != null && ("definitionView" == object.viewType || "definitionEditor" == object.viewType  || "definitionDiffEdit" == object.viewType  || "definitionDiffView" == object.viewType)){
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
    this.canvasObjectId = canvasObject.__objectId;
    var canvasObjectFaceHelper = mw3.getFaceHelper(canvasObject.__objectId);
    this.canvas = canvasObjectFaceHelper.icanvas;
    this.element;
};

org_uengine_kernel_view_WebServiceActivityView.prototype = {
        loaded : function(){
            var object = mw3.objects[this.objectId];
            var element = null;
            var canvas = this.canvas;
            if( object && !object.drawByCanvas){
                var initText = "";
                if( !(object.label == null || object.label == 'undefined') ){
                    initText = unescape(object.label);
                }else if( object.activity && object.activity.description != null && object.activity.description.text != null ){
                    initText = object.activity.description.text;
                }
                
                var shape = null;
                if( initText && initText != "" && initText != null){
                    shape = eval('new ' + object.shapeId + '(\''+initText +'\')');
                }else{
                    shape = eval('new ' + object.shapeId + '()');
                }
                var id = object.id;
                var style = object.style;
                if( object.instStatus ){
                    if ("Completed" == object.instStatus || "Running" == object.instStatus) {
                        shape.status = object.instStatus;
                    }
                }
                if (object.exceptionType && object.exceptionType != 'None') {
                    shape.exceptionType = object.exceptionType;
                }
    
                element = canvas.drawShape([
                                                 object.x, object.y 
                                                 ], 
                                                 shape, [parseInt(object.width, 10), parseInt(object.height, 10)] , OG.JSON.decode(unescape(style)), id, null, false);
                // object.activityClass : Activity , object.__className : ActivityView
                $(element).attr("_classname", object.activityClass);
                $(element).attr("_classType", object.classType);
                $(element).attr("_tracingTag",object.tracingTag);
                if( object.activity ){
                    $(element).data('activity', object.activity);
                    // object.activity.activityView = null; 을 꼭 해주어야함.. activity가 activityView 를 들고있고, activityView가 activity를 들고있는 구조라서..
                    object.activity.activityView = null;
                }else if( typeof $(element).attr("_classname") != 'undefined' &&  typeof $(element).data("activity") == 'undefined' ){
                    var activityData = {__className : $(element).attr("_classname"), tracingTag : $(element).attr("_tracingTag")};
                    $(element).data('activity', activityData);
                }
                
            }else{
                element = object.element;
                
                var activityData = {__className : $(element).attr("_classname"), tracingTag : $(element).attr("_tracingTag")};
                $(element).data('activity', activityData);
                
                object.element = null;
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
            
            $(element).attr("_viewClass", object.__className);
            this.element = element;
            if( typeof object.activityClass != 'undefined'){
                $(element).unbind('dblclick');
                $(element).on({
                    dblclick: function (event) {    
                        if(event.stopPropagation){
                            event.stopPropagation();
                        }
                        var divId = 'properties_' + this.objectId;
                        $('body').append("<div id='" + divId + "'></div>");
                        var metaworksContext = {
                                __className : 'org.metaworks.MetaworksContext',
                                when : 'edit'
                        };
                        
                        var propertiesWindow = {
                                __className : 'org.uengine.codi.mw3.webProcessDesigner.PropertiesWindow',
                                open : true,
                                width : 860,
                                height : 600,
                                panel : $(this).data('activity'),
                                metaworksContext : metaworksContext
                        };
                        
                        object['propertiesWindow'] = propertiesWindow;
                        object.id = $(this).attr('id');
						
						// 기존 ActivityView 와 다른 점. 자신의 상위에 있는 Pool을 찾아서 들고간다. ( pool 에서 가져오는 스펙은 변경됨 )
//						var poolId = '';
//						var thisEle = this;
//						var poolElement;
//						$(canvas.getRootElement()).find("[_shape=GROUP]").each(function (index, element) {
//			                if(element.shape instanceof OG.shape.HorizontalPoolShape){
//			                    var i, elements = canvas.getElementsByBBox(element.shape.geom.getBoundary());
//			                    
//			                    for(i=0;i<elements.length;i++){
//			                        if(elements[i] == thisEle)
//			                            poolElement = element;
//			                    }
//			                }
//			            });
//						if( poolElement ){
//							var poolObject = $('#'+$(poolElement).attr('id')).data('pool');
//							object.pool = poolObject;
//						}

						// 기존 ActivityView 와 다른 점. 자신이 연결된 RestMessageEvent 를 찾는다.
						var getShapeFromTerminal = function (terminal) {
			                var terminalId = OG.Util.isElement(terminal) ? terminal.id : terminal;
			                if (terminalId) {
			                    return canvas.getRenderer().getElementById(terminalId.substring(0, terminalId.indexOf(OG.Constants.TERMINAL_SUFFIX.GROUP)));
			                } else {
			                    return null;
			                }
			            };
						var toEdge = $(this).attr('_toedge');
						// toEdge 가 여러개 일수 있다.
						var toEdgeRef = toEdge.split(",");
						var toElement = null;
						if( toEdgeRef && toEdgeRef.length > 1 ){
							for(var i=0; i < toEdgeRef.length; i++ ){
								var toTeminal = $('#'+toEdgeRef[i]).attr('_to');
		                        var toElementRef = getShapeFromTerminal(toTeminal);
								var acts = $(toElementRef).data('activity');
								if( acts && acts.__className == 'org.uengine.kernel.ReceiveRestMessageEventActivity' ){
									toElement = toElementRef;
								}
							}
						}else{
							var toTeminal = $('#'+toEdge).attr('_to');
							toElement = getShapeFromTerminal(toTeminal);
						}
						var poolId = '';
                        var poolElement;
                        $(canvas.getRootElement()).find("[_shape=GROUP]").each(function (index, element) {
                            if(element.shape instanceof OG.shape.HorizontalPoolShape){
                                var i, elements = canvas.getElementsByBBox(element.shape.geom.getBoundary());
                                
                                for(i=0;i<elements.length;i++){
                                    if(elements[i] == toElement)
                                        poolElement = element;
                                }
                            }
                        });
                        if( poolElement ){
                            var poolObject = $('#'+$(poolElement).attr('id')).data('pool');
                            object.pool = poolObject;
							var receiveRestMessageEventActivity = $(toElement).data('activity');
							if( receiveRestMessageEventActivity ){
								object.connectedService = receiveRestMessageEventActivity.name.text
							}
	                        object.showProperties();
                        }else{
							
						}
                        
                    }
                });
            }
		},
        validation : function(message){
			if('release' == message){
                this.canvas.setExceptionType(this.element, '');
            }else{
                this.canvas.setExceptionType(this.element, 'error');
            }
        }
};