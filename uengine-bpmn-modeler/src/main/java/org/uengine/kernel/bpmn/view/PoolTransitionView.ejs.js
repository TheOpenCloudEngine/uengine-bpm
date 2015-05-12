var org_uengine_kernel_view_PoolTransitionView = function(objectId, className){
    
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
	this.canvasDivObj = $('#canvas_' + canvasObject.__objectId);
    this.canvas = canvasObjectFaceHelper.icanvas;
    this.element;
};

org_uengine_kernel_view_PoolTransitionView.prototype = {
        loaded : function(){
            var objectId = this.objectId;
            var object = mw3.objects[this.objectId];
            var element;
            var canvas = this.canvas;
			var canvasDivObj = this.canvasDivObj;
            var getShapeFromTerminal = function (terminal) {
                var terminalId = OG.Util.isElement(terminal) ? terminal.id : terminal;
                if (terminalId) {
                    return canvas.getRenderer().getElementById(terminalId.substring(0, terminalId.indexOf(OG.Constants.TERMINAL_SUFFIX.GROUP)));
                } else {
                    return null;
                }
            };
            if( object && !object.drawByCanvas ){
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
                //element = canvas.connect(fromElement, toElement , OG.JSON.decode(unescape(style)) , initText);
                element = canvas.connectWithTerminalId(fromTeminal, toTeminal , OG.JSON.decode(unescape(style)) , initText);
                
                if( object.poolTransition ){
                    $(element).data('poolTransition', object.poolTransition);
                    object.poolTransition.poolTransitionView = null;
                }
            }else{
                element = object.element;
                var fromElement = getShapeFromTerminal($(element).attr("_from"));
                var toElement = getShapeFromTerminal($(element).attr("_to"));
//              
                var fromTracingTag = $(fromElement).attr('_tracingTag');
                var toTracingTag = $(toElement).attr('_tracingTag');
                if( fromTracingTag && toTracingTag){
                    var transitionData = {
                            __className : 'org.uengine.kernel.graph.PoolTransition', 
                            source : fromTracingTag,
                            target : toTracingTag
                    };
                    $(element).data('poolTransition', transitionData);
                    object.poolTransition = transitionData;
                }
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
            $(element).unbind('dblclick');
            $(element).on({
                dblclick: function (event) {
                    if(event.stopPropagation){
                        event.stopPropagation();
                    }
					// 안쓰이는 스펙. 기존프로세스 변수와, 웹서비스 변수를 매퍼를 통해 연결하는 스펙이었는데 사용안함
//                    object.id = $(this).attr('id');
//					var poolTransition = $(this).data('poolTransition');
//					var sourceTrc = poolTransition.source;
//					var targetTrc = poolTransition.target;
//					var sourceEle = canvasDivObj.find("g[_tracingtag='"+sourceTrc+"']");
//					var targetEle = canvasDivObj.find("g[_tracingtag='"+targetTrc+"']");
//					if( sourceEle && targetEle){
//						var sourceEleId = $(sourceEle).attr('id');
//						var targetEleId = $(targetEle).attr('id');
//						poolTransition.sourceActivity = $('#'+sourceEleId).data('activity'); 
//						poolTransition.targetActivity = $('#'+targetEleId).data('activity');
//					}
//					
//                    object.poolTransition = poolTransition;
//					object.poolMapping();
                }
            });
        }
};