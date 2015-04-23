var org_uengine_kernel_view_GraphicView = function(objectId, className){
    
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
    var canvasObjectFaceHelper = mw3.getFaceHelper(canvasObject.__objectId);
    this.canvas = canvasObjectFaceHelper.icanvas;
    this.element;
};

org_uengine_kernel_view_GraphicView.prototype = {
        loaded : function(){
			var object = mw3.objects[this.objectId];
            var element = null;
            var canvas = this.canvas;
            if( object && !object.drawByCanvas){    // 디자이너 상에서 그려질 경우
                if(object.shapeType != 'EDGE'){ 
				    // 도형
	                var initText = ( object.label == null || object.label == 'undefined' ) ? "" :  unescape(object.label);
	                var shape = null;
	                if( initText && initText != "" && initText != null){
	                    shape = eval('new ' + object.shapeId + '(\''+initText +'\')');
	                }else{
	                    shape = eval('new ' + object.shapeId + '()');
	                }
	                var id = object.id;
	                var style = object.style;
	    
	                element = canvas.drawShape([
	                                                 object.x, object.y 
	                                                 ], 
	                                                 shape, [parseInt(object.width, 10), parseInt(object.height, 10)] , OG.JSON.decode(unescape(style)), id, null, false);
	                
				}else{
					var getShapeFromTerminal = function (terminal) {
		                var terminalId = OG.Util.isElement(terminal) ? terminal.id : terminal;
		                if (terminalId) {
		                    return canvas.getRenderer().getElementById(terminalId.substring(0, terminalId.indexOf(OG.Constants.TERMINAL_SUFFIX.GROUP)));
		                } else {
		                    return null;
		                }
		            };
					var initText = ( object.label == null || object.label == 'undefined' ) ? "" : unescape(object.label);
	                var style = object.style;
	                var fromTeminal = object.from;
	                var toTeminal = object.to;
					var fromElement = getShapeFromTerminal(fromTeminal);
	                var toElement = getShapeFromTerminal(toTeminal);
	                var fromElementId = $(fromElement).attr('id');
	                var toElementId = $(toElement).attr('id');
	                // 이 부분을 두번 안태우도록 ProcessDesignerContentPanel.ejs.js 에서 해당 attr로 비교를 함
	                $(fromElement).attr('_conneted'+fromElementId , fromElementId +'_'+ toElementId);
	                $(toElement).attr('_conneted'+toElementId , fromElementId +'_'+ toElementId);
	                element = canvas.connectWithTerminalId(fromTeminal, toTeminal , OG.JSON.decode(unescape(style)) , initText);
				}
            }else{
				if (object.shapeType != 'EDGE') {
				}else {
					// 선이 디자이너상에서 이미 그려지고, 추후 정보만 넘긴 경우
					// ProcessDesignercontentPanel.ejs.js 의 onConnectShape 에서 호출됨
					element = object.element;
					object.element = null;
				}
			}
			$(element).attr("_viewClass", object.__className);
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
            this.element = element;
        }
};