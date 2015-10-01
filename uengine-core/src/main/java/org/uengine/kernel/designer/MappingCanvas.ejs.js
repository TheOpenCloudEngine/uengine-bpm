var org_uengine_kernel_designer_MappingCanvas= function(objectId, className){
	// default setting
	this.objectId = objectId;
	this.className = className;
	var linekedInfo = new ArrayList();
	this.linekedInfo = linekedInfo;
	var faceHelper = this;
	var canvas = null;
	
	var object = mw3.objects[this.objectId];
	if( object == null || typeof object == 'undefined'){
		this.object = {
				__objectId : this.objectId,
				__className : this.className
		};
	}else{
		this.object = object;
	}
	
	OG.shape.From = function (label) {
        OG.shape.From.superclass.call(this, label);
        this.SHAPE_ID = 'OG.shape.From';
    };
    OG.shape.From.prototype = new OG.shape.CircleShape();
    OG.shape.From.superclass = OG.shape.CircleShape;
    OG.shape.From.prototype.constructor = OG.shape.From;
    OG.From = OG.shape.From;

    OG.shape.From.prototype.createTerminal = function () {
        if (!this.geom) {
            return [];
        }

        var envelope = this.geom.getBoundary();
        if(object.inout){
        	return [
        	        new OG.Terminal(envelope.getCentroid(), OG.Constants.TERMINAL_TYPE.C, OG.Constants.TERMINAL_TYPE.INOUT)
        	        ];
        }else{
        	return [
        	        new OG.Terminal(envelope.getCentroid(), OG.Constants.TERMINAL_TYPE.C, OG.Constants.TERMINAL_TYPE.OUT)
        	        ];
        }
    };
    OG.shape.From.prototype.clone = function () {
        return new OG.shape.From(this.label);
    };

    // define To Shape
    OG.shape.To = function (label) {
        OG.shape.To.superclass.call(this, label);
        this.SHAPE_ID = 'OG.shape.To';
    };
    OG.shape.To.prototype = new OG.shape.CircleShape();
    OG.shape.To.superclass = OG.shape.CircleShape;
    OG.shape.To.prototype.constructor = OG.shape.To;
    OG.To = OG.shape.To;

    OG.shape.To.prototype.createTerminal = function () {
        if (!this.geom) {
            return [];
        }

        var envelope = this.geom.getBoundary();
        // direction == true 이면 양방향 통신을 한다는 가정
        if(object.inout){
        	return [
        	        new OG.Terminal(envelope.getCentroid(), OG.Constants.TERMINAL_TYPE.C, OG.Constants.TERMINAL_TYPE.INOUT)
        	        ];
        }else{
        	return [
        	        new OG.Terminal(envelope.getCentroid(), OG.Constants.TERMINAL_TYPE.C, OG.Constants.TERMINAL_TYPE.IN)
        	        ];
        }
    };
    OG.shape.To.prototype.clone = function () {
        return new OG.shape.To(this.label);
    };

//	OG.Constants.DEFAULT_STYLE.EDGE["edge-type"] = "straight";
	
	OG.common.Constants.CANVAS_BACKGROUND = "#fff";
    OG.Constants.ENABLE_CANVAS_OFFSET = true; // Layout 사용하지 않을 경우 true 로 지정
    this.canvasId = object.canvasId;
    canvas = new OG.Canvas(this.canvasId);
    canvas._CONFIG.DEFAULT_STYLE.EDGE["edge-type"] = "straight";
    canvas._CONFIG.DEFAULT_STYLE.EDGE_SHADOW["edge-type"] = "straight";
    canvas.initConfig({
        selectable      : true,
        dragSelectable  : false,
        movable         : true,
        resizable       : false,
        connectable     : true,
        connectCloneable: false,
        connectRequired : true,
        labelEditable   : false,
        groupDropable   : false,
        collapsible     : false,
        enableHotKey    : true,
        enableContextMenu : false
    });
  
    this.setContextMenu(canvas);
    
    this.leftTreeLoaded = false;
    this.rightTreeLoaded = false;
    this.loadDrawed = false;
    this.divId = mw3._getObjectDivId(this.objectId);
	this.divObj = $('#' + this.divId);
	// canvas는 단독으로 동작하는게 아니고 상위 화면을 가지고있기때문에 parent 로 구하여 작업한다.
	this.parentDivObj = this.divObj.parent();
	this.leftTreeId = object.leftTreeId;
	this.rightTreeId = object.rightTreeId;
    var leftTreeObj = this.parentDivObj.find("#" + this.leftTreeId );
    var leftTreeHeight = 0;
    var rightTreeHeight = 0;
    
    var canvasDivObj = $('#'+this.canvasId);
    var canvasWidth = canvasDivObj.width();
    
    leftTreeObj.bind('loaded', {align : 'left'}, function(event){
    	leftTreeHeight = $(this).find('.filemgr-tree').height();
    	if( leftTreeHeight > rightTreeHeight){
    		canvas.setCanvasSize([canvasWidth, leftTreeHeight]);	
    	}
		faceHelper.drawTerminals(this.id, true, canvas , null , false);
    }).bind('expanded', function(){
    	faceHelper.drawTerminals(this.id, true, canvas , null , false);
    }).bind('collapsed', function(){
    	faceHelper.drawTerminals(this.id, true, canvas , null , false);
    }).bind('toAppended', function(){
    	faceHelper.drawTerminals(this.id, true, canvas , null , true);
    });
    
    var rightTreeObj = this.parentDivObj.find("#"+ this.rightTreeId);
    rightTreeObj.bind('loaded', {align : 'right'}, function(event){
    	rightTreeHeight = $(this).find('.filemgr-tree').height();
    	if( rightTreeHeight > leftTreeHeight){
    		canvas.setCanvasSize([canvasWidth, rightTreeHeight]);	
    	}
		faceHelper.drawTerminals(this.id, false, canvas , null , false);
    }).bind('expanded', function(){
    	faceHelper.drawTerminals(this.id, false, canvas , null , false);
    }).bind('collapsed', function(){
    	faceHelper.drawTerminals(this.id, false, canvas , null , false);
    }).bind('toAppended', function(){
    	faceHelper.drawTerminals(this.id, false, canvas , null , true);
    });
    
    canvas.onConnectShape(function (event, edgeElement, fromElement, toElement) {
        var linkedInfoStr = fromElement.id + "," + toElement.id;
        linekedInfo.add(linkedInfoStr);
        faceHelper.linekedInfo = linekedInfo;
    });

    canvas.onDisconnectShape(function (event, edgeElement, fromElement, toElement) {
        var linkedInfoStr = fromElement.id + "," + toElement.id;
        for(var i = 0; i < linekedInfo.size(); i++){
        	if( linekedInfo.get(i) == linkedInfoStr){
        		linekedInfo.remove(i);
        	}
        }
        faceHelper.linekedInfo = linekedInfo;
    });
    
    this.icanvas = canvas;	
    // TODO 추후 삭제 - 임시로직 ( getValue 가 여러번 호출되기때문에 한번만 호출을 해주도록 임시변수 셋팅함
    this.callCount = 1;
};

org_uengine_kernel_designer_MappingCanvas.prototype = {
		drawTerminals : function(treeDivId, isLeft , canvas, callback, isAppend) {
			var treeObjectId = null;
			if(isLeft){
				treeObjectId = this.parentDivObj.find("#"+this.leftTreeId +" .filemgr-tree").attr('objectId');
			}else{
				treeObjectId = this.parentDivObj.find("#"+this.rightTreeId +" .filemgr-tree").attr('objectId');
			}
			var leftTreeLoding = this.leftTreeLoaded;
			var rightTreeLoding = this.rightTreeLoaded;
			var canvasId = this.canvasId;
			$('#' + treeDivId+' .item-fix').each(function(idx, item) {
				if(!($(this).hasClass('root'))){
					var objectId = $(this).attr('objectId');
					var object = mw3.objects[objectId];
					var id = object.id;
					var shapeId = (isLeft ? 'FROM_' : 'TO_') + id;
					var isView = true;
					var closedParentObjectId = mw3.getFaceHelper(treeObjectId).getClosedParentNodes(objectId);
					var closedParentObject = mw3.objects[closedParentObjectId];
					// 동적으로 새롭게 붙은 노드들은 무조건 도형을 그려야 하기때문에 isAppend 를 체크하여줌
					if( object.type != 'folder' && closedParentObject != null && !isAppend){
						isView = false;
					}
					if( isView ){
						
						var shapeElement = canvas.drawShape(
		                    [(isLeft ? 5 : 275), ( $(this).offset().top - $('#'+canvasId).offset().top ) + item.offsetHeight / 2],
		                    (isLeft ? new OG.From() : new OG.To()),
		                    [5, 5],
		                    {"r":5},
		                    shapeId
						);
						
						edgeIds = $(shapeElement).attr(isLeft ? "_toedge" : "_fromedge");
						if (edgeIds) {
							$.each(edgeIds.split(","), function (indx, edgeId) {
								edge = canvas.getElementById(edgeId);
								edge.shape.geom.style.map['stroke-dasharray'] = '';
							});
						}
						$(shapeElement).click("destroy");
						canvas.removeAllGuide();
						canvas.redrawConnectedEdge(shapeElement);
						canvas.show(shapeElement);
					}else{
						var shapeElement = canvas.getElementById(shapeId);
						if (shapeElement) {
							var parentNode = $('.item-fix[objectId='+ closedParentObjectId+']');
							shapeElement = canvas.drawShape(
									[(isLeft ? 5 : 275), ( parentNode.offset().top - $('#'+canvasId).offset().top ) + parentNode[0].offsetHeight / 2],
									(isLeft ? new OG.From() : new OG.To()),
									[5, 5],
									{"r":5},
									shapeId
							);

							edgeIds = $(shapeElement).attr(isLeft ? "_toedge" : "_fromedge");
							if (edgeIds) {
								$.each(edgeIds.split(","), function (indx, edgeId) {
									edge = canvas.getElementById(edgeId);
									edge.shape.geom.style.map['stroke-dasharray'] = '--';
								});
							}
							$(shapeElement).click("destroy");
							canvas.removeAllGuide();
							canvas.redrawConnectedEdge(shapeElement);
							canvas.hide(shapeElement);
						}
					}
					// load triger 가 트리를 처음 그릴때 한번 타고, faceHelper를 모두 태운뒤에 한번더 이벤트가 온다
					// 두번째 이벤트에서 모두 트리를 그릴때 이 each문을 타기때문에 트리를 모두 그린후에 loaded를 체크해준다.
					if(isLeft && !leftTreeLoding){
						leftTreeLoding = true;
					}else if(!isLeft && !rightTreeLoding) {
						rightTreeLoding = true;
					}
				}
			});
			
			$('#' + treeDivId+' .item-fix:last').each(function(idx, item) {
				if( ( $(this).offset().top - $('#'+canvasId).offset().top ) > $('#'+canvasId).height()){
	                canvas.setCanvasSize([$('#'+canvasId).width(), ( $(this).offset().top - $('#'+canvasId).offset().top ) + 30]);   
	            }
			});			
						
			this.leftTreeLoaded = leftTreeLoding;
			this.rightTreeLoaded = rightTreeLoding;
			if(!this.loadDrawed){
				if(leftTreeLoding && rightTreeLoding){
					this.drawLine();
				}
			}
			// sended data role : " fromElementId , toElementId "//TODO, (in|out|inOut) " 
		},
		drawUnExtendedTerminal : function(shapeId , isLeft , parentElement){
			var canvas = this.icanvas;
			var shapeElement = canvas.drawShape(
					[(isLeft ? 5 : 275), ($(parentElement).offset().top - $('#'+this.canvasId).offset().top ) + 4 ],
					(isLeft ? new OG.From() : new OG.To()),
					[5, 5],
					{"r":5},
					shapeId
			);

			$(shapeElement).click("destroy");
			canvas.removeAllGuide();
			canvas.hide(shapeElement);
		},
		drawLine : function(){
			this.loadDrawed = true;
			var elements = this.object.mappingElements;
			if(typeof elements == 'undefined'){
				return;
			}
			if( elements != null ){
				for(var i = 0; i < elements.length; i++){
					var toId = 'TO_' + elements[i].argument.text;
					var fromId = 'FROM_' + elements[i].variable.selectedText;
					// "." 을 "-" 로 변경
					toId = toId.replace(/\./gi, "-");
					fromId = fromId.replace(/\./gi, "-");
					
					var fromShape = this.icanvas.getElementById(fromId);
					var toShape = this.icanvas.getElementById(toId);
					// 동적으로 생성되는 노드는 로드 시점에 아직 안그려져 있을수 있다.
					if( typeof(fromShape) == "undefined" || fromShape == null){
						var parentElement = this.findParentNode(fromId);
						// 선이 이어진 도형만 그린 후에 선을 연결시키고 숨겨준다. 
						this.drawUnExtendedTerminal(fromId, true, parentElement);
						fromShape = this.icanvas.getElementById(fromId);
					}
					if( typeof(toShape) == "undefined" || toShape == null){
						var parentElement = this.findParentNode(toId);
						this.drawUnExtendedTerminal(toId, false, parentElement);
						toShape = this.icanvas.getElementById(toId);
					}
					// 선을 그린다.
					this.icanvas.connect(fromShape, toShape);
				}
			}
		},
		findParentNode : function(nodeId){
			var parentNodeId = nodeId.substring(0, nodeId.lastIndexOf("-") );
			var shapeNode = this.icanvas.getElementById(parentNodeId);
			if( shapeNode == null ){
				return this.findParentNode(parentNodeId);
			}else{
				return shapeNode;
			}
		},
		getValue : function(){
			// startsWith 정의
			if(!String.prototype.startsWith){
			    String.prototype.startsWith = function (str) {
			        return !this.indexOf(str);
			    };
			}
			
			if( this.callCount == 1 ){
				var returnObject = this.object;
				returnObject.mappingElements = [];
				for(var i = 0; i < this.linekedInfo.size(); i++){
					var linkedInfoStr = this.linekedInfo.get(i);
					var fromId = '';
					var toId = '';
					if( linkedInfoStr != null ){
						var tempStr = linkedInfoStr.split(","); 
						fromId 	= tempStr[0];
						toId 	= tempStr[1];
						if (console && console.log) console.log('fromId = ' +fromId );
						if (console && console.log) console.log('toId = ' +toId );
						// argument : 받은 변수정보 => toId
						// variable : 준 변수 정보 => formId
						var fromText;
						var toText;
						var direction;
						if(returnObject.inout && fromId.startsWith('TO_')){
							// 새로 추가된 양방향 통신이 가능하도록.. (subprocessActivity)
							fromText = fromId.substring(3);
							toText = toId.substring(5);
							direction = 'out';
						}else{
							// 기존 mappingActivity, invocationActivity
							fromText = fromId.substring(5);
							toText = toId.substring(3);
							direction = 'in';
						}
						
						if (console && console.log) console.log('fromText = ' +fromText );
						if (console && console.log) console.log('toText = ' +toText );
						fromText = fromText.replace(/-/gi, ".");
						toText = toText.replace(/-/gi, ".");
						var processValiable = {
								__className : 'org.uengine.kernel.ProcessVariable',
								name : fromText
						};
						var argumentText = {
								__className : 'org.uengine.contexts.TextContext',
								text : toText
						};
						var mappingElement = {
							__className : 'org.uengine.kernel.ParameterContext',
							direction : direction,
							argument : argumentText,
							variable : processValiable
						};
						
						returnObject.mappingElements.push(mappingElement);
					}
				}
				this.object = returnObject;
				this.callCount = -1;
			}
			return this.object;
		},
		
		setContextMenu : function(canvas) {
			var me = canvas;
			$.contextMenu({
			selector: '#' + me._RENDERER.getRootElement().id,
			build   : function ($trigger, e) {
				var root = me._RENDERER.getRootGroup(), shape, newElement, eventOffset = me._HANDLER._getOffset(event);
				$(me._RENDERER.getContainer()).focus();
				return {
					items: {
						'Delete': {
							name: 'Delete', callback: function () {
								me._HANDLER.deleteSelectedShape();
							}
						},
						'Math': {
							name: 'Math',
							items: {
								'Max': {
									name: 'Max', callback: function () {
										me.drawTransformer([eventOffset.x, eventOffset.y], 'Max', 2, 1);
									}
								},
								'Min' : {
									name: 'Min', callback: function () {
										me.drawTransformer([eventOffset.x, eventOffset.y], 'Min', 2, 1);
									}
								},
								'To Number'        : {
									name: 'To Number', callback: function () {
										me.drawTransformer([eventOffset.x, eventOffset.y], 'To Number', 1, 1);
									}
								},
								'Sum'        : {
									name: 'Sum', callback: function () {
										me.drawTransformer([eventOffset.x, eventOffset.y], 'Sum', 5, 1);
									}
								},
								'Floor'        : {
									name: 'Floor', callback: function () {
										me.drawTransformer([eventOffset.x, eventOffset.y], 'Floor', 1, 1);
									}
								},
								'Round'       : {
									name: 'Round', callback: function () {
										me.drawTransformer([eventOffset.x, eventOffset.y], 'Round', 1, 1);
									}
								},
								'Ceil'       : {
									name: 'Ceil', callback: function () {
										me.drawTransformer([eventOffset.x, eventOffset.y], 'Ceil', 1, 1);
									}
								},
								'Abs'       : {
									name: 'Abs', callback: function () {
										me.drawTransformer([eventOffset.x, eventOffset.y], 'Abs', 1, 1);
									}
								}
							}
						},
						'String'    : {
							name: 'String',
							items: {
								'Concat': {
									name: 'Concat', callback: function () {
										me.drawTransformer([eventOffset.x, eventOffset.y], 'Concat', 5, 1);
									}
								},
								'Replace' : {
									name: 'Replace', callback: function () {
										me.drawTransformer([eventOffset.x, eventOffset.y], 'Replace', 1, 1);
									}
								},
								'NumberFormat'        : {
									name: 'NumberFormat', callback: function () {
										me.drawTransformer([eventOffset.x, eventOffset.y], 'NumberFormat', 2, 1);
									}
								}
							}
						},
						'XML'     : {
							name : 'XML',
							items: {
								'view_actualSize': {
									name: 'Actual Size', callback: function () {
										me._RENDERER.setScale(1);
									}
								},
								'sep2_1'         : '---------',
								'view_fitWindow' : {
									name: 'Fit Window', callback: function () {
										me.fitWindow();
									}
								},
								'sep2_2'         : '---------',
								'view_25'        : {
									name: '25%', callback: function () {
										me._RENDERER.setScale(0.25);
									}
								},
								'view_50'        : {
									name: '50%', callback: function () {
										me._RENDERER.setScale(0.5);
									}
								},
								'view_75'        : {
									name: '75%', callback: function () {
										me._RENDERER.setScale(0.75);
									}
								},
								'view_100'       : {
									name: '100%', callback: function () {
										me._RENDERER.setScale(1);
									}
								},
								'view_150'       : {
									name: '150%', callback: function () {
										me._RENDERER.setScale(1.5);
									}
								},
								'view_200'       : {
									name: '200%', callback: function () {
										me._RENDERER.setScale(2);
									}
								},
								'view_300'       : {
									name: '300%', callback: function () {
										me._RENDERER.setScale(3);
									}
								},
								'view_400'       : {
									name: '400%', callback: function () {
										me._RENDERER.setScale(4);
									}
								},
								'sep2_3'         : '---------',
								'view_zoomin'    : {
									name: 'Zoom In', callback: function () {
										me.zoomIn();
									}
								},
								'view_zoomout'   : {
									name: 'Zoom Out', callback: function () {
										me.zoomOut();
									}
								}
							}
						}
					}
				};
			}
		});
		}
};