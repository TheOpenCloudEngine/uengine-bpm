var org_uengine_modeling_Canvas = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.object = mw3.objects[this.objectId];
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $(document.getElementById(this.objectDivId));
	this.metaworksContext = mw3.objectContexts[this.objectId].__metaworksContext;

	this.objectDiv.addClass('mw3_resize').addClass('canvas').css('height','100%');
	this.history = [];
	this.historyInit = true;

	this.canvasDivId = 'canvas_' + objectId;
	this.tracingTag = 0;

	if(this.object){
		var faceHelper = this;
		faceHelper.load();
	}

};

org_uengine_modeling_Canvas.prototype = {
	startLoading : function(){

	},

	endLoading : function(){

	},

	load : function(){
		OG.common.Constants.CANVAS_BACKGROUND = "#fff";
		OG.Constants.ENABLE_CANVAS_OFFSET = true;



		this.canvas = new OG.Canvas(this.canvasDivId);
		if(this.metaworksContext.when == mw3.WHEN_EDIT || this.metaworksContext.when == mw3.WHEN_NEW){
			this.canvas.initConfig({
				selectable       : true,
				dragSelectable   : true,
				movable          : true,
				resizable        : true,
				connectable      : true,
				selfConnectable  : false,
				connectCloneable : false,
				connectRequired  : true,
				labelEditable    : true,
				groupDropable    : true,
				collapsible      : true,
				enableHotKey     : true,
				enableContextMenu: true
			});
			this.canvas._RENDERER.setCanvasSize([this.object.width, this.object.height]);

		}else{
			this.canvas.initConfig({
				selectable       : true,
				dragSelectable   : false,
				movable          : false,
				resizable        : false,
				connectable      : false,
				selfConnectable  : false,
				connectCloneable : false,
				connectRequired  : false,
				labelEditable    : false,
				groupDropable    : false,
				collapsible      : false,
				enableHotKey     : false,
				enableContextMenu: false
			});

		}
		this.canvas.setCurrentCanvas(this.canvas);

		mw3.canvas = this.canvas;

		this.eventBinding();


	},

	eventBinding: function(){
		var canvasDivObj = document.getElementById(this.canvasDivId);

		$(canvasDivObj).bind('connectShape', {objectId: this.objectId}, function(event, edge, from, to){
			mw3.getFaceHelper(event.data.objectId).createTransitionView(edge, from, to);
		});
		$(canvasDivObj).bind('removeShape', {objectId: this.objectId}, function(event, element){
			mw3.getFaceHelper(event.data.objectId).removeElement(element);
		});
		$(canvasDivObj).bind('pasteShape', {objectId: this.objectId}, function(event, copiedElement, selectedElement){
			for(var i=0; i<copiedElement.length; i++){
				var elementViewId = 'org.uengine.modeling.ElementView@' + copiedElement[i].id;
				var elementView = mw3.getAutowiredObject(elementViewId);
				var element = {};
				mw3.copyObjectToObject(element, elementView.element);
				element.tracingTag = null;

				element.name.text = elementView.label;

				var newElementView = {
					__className: elementView.__className,
					shapeId: elementView.shapeId,
					label: elementView.label,
					id: selectedElement[i].id,
					metaworksContext: elementView.metaworksContext,
					element: element
				};

				mw3.getFaceHelper(event.data.objectId).toAppend(newElementView);
				mw3.onLoadFaceHelperScript();

				mw3.call(newElementView.__objectId, 'duplicate');

			}
		});

		$(canvasDivObj).bind('change.' + this.objectId, {objectId: this.objectId}, function(event, element){
			if(console)
				console.log('change');
			mw3.getFaceHelper(event.data.objectId).addHistory();
		});

		$(canvasDivObj).bind("keydown." + this.objectId, {objectId: this.objectId}, function (event) {
			if(event.shiftKey && (event.ctrlKey || event.metaKey) && event.keyCode === KeyEvent.DOM_VK_Z){
				mw3.getFaceHelper(event.data.objectId).redo();
			}else if((event.ctrlKey || event.metaKey) && event.keyCode === KeyEvent.DOM_VK_Z){
				mw3.getFaceHelper(event.data.objectId).undo();
			}
		});

		this.objectDiv.bind('drawShape', {objectId: this.objectId}, function(event, element){
			mw3.getFaceHelper(event.data.objectId).initHistory();

			mw3.getFaceHelper(event.data.objectId).addRedoList();
		});

		//정렬 기능을 위하여
		this.objectDiv.bind('alignLeft', {objectId: this.objectId}, function(event){
			mw3.getFaceHelper(event.data.objectId).getCanvas().alignLeft();
		});
		this.objectDiv.bind('alignRight', {objectId: this.objectId}, function(event){
			mw3.getFaceHelper(event.data.objectId).getCanvas().alignRight();
		});
		this.objectDiv.bind('alignTop', {objectId: this.objectId}, function(event){
			mw3.getFaceHelper(event.data.objectId).getCanvas().alignTop();
		});
		this.objectDiv.bind('alignBottom', {objectId: this.objectId}, function(event){
			mw3.getFaceHelper(event.data.objectId).getCanvas().alignBottom();
		});

		// Label 정렬 기능
		this.objectDiv.bind('labelHorizontal', {objectId: this.objectId}, function(event, data){
			mw3.getFaceHelper(event.data.objectId).getCanvas()._HANDLER.setLabelHorizontalSelectedShape(data);
		});
		this.objectDiv.bind('labelVertical', {objectId: this.objectId}, function(event, data){
			mw3.getFaceHelper(event.data.objectId).getCanvas()._HANDLER.setLabelVerticalSelectedShape(data);
		});
		this.objectDiv.bind('selectAll', {objectId: this.objectId}, function(event){
			mw3.getFaceHelper(event.data.objectId).getCanvas()._HANDLER.selectAll();
		});
		this.objectDiv.bind('zoomIn', {objectId: this.objectId}, function(event){
			mw3.getFaceHelper(event.data.objectId).getCanvas()._HANDLER.zoomIn();
		});
		this.objectDiv.bind('zoomOut', {objectId: this.objectId}, function(event){
			mw3.getFaceHelper(event.data.objectId).getCanvas()._HANDLER.zoomOut();
		});
		this.objectDiv.bind('actualSize', {objectId: this.objectId}, function(event){
			mw3.getFaceHelper(event.data.objectId).getCanvas()._HANDLER._RENDERER.setScale(1);
		});
		this.objectDiv.bind('deleteElement', {objectId: this.objectId}, function(event){
			mw3.getFaceHelper(event.data.objectId).getCanvas()._HANDLER.deleteSelectedShape();
		});
		this.objectDiv.bind('copyElement', {objectId: this.objectId}, function(event){
			mw3.getFaceHelper(event.data.objectId).getCanvas()._HANDLER.copySelectedShape();
		});
		this.objectDiv.bind('pasteElement', {objectId: this.objectId}, function(event){
			mw3.getFaceHelper(event.data.objectId).getCanvas()._HANDLER.pasteSelectedShape();
		});
		this.objectDiv.bind('boldLabel', {objectId: this.objectId}, function(event){
			mw3.getFaceHelper(event.data.objectId).getCanvas()._HANDLER.setFontWeightSelectedShape('bold');
		});
		this.objectDiv.bind('italicLabel', {objectId: this.objectId}, function(event){
			mw3.getFaceHelper(event.data.objectId).getCanvas()._HANDLER.setFontStyleSelectedShape('italic');
		});
		this.objectDiv.bind('labelPosition', {objectId: this.objectId}, function(event, data){
			mw3.getFaceHelper(event.data.objectId).getCanvas()._HANDLER.setLabelPositionSelectedShape(data);
		});
		this.objectDiv.bind('opacity', {objectId: this.objectId}, function(event, data){
			mw3.getFaceHelper(event.data.objectId).getCanvas()._HANDLER.setFillOpacitySelectedShape(data);
		});
		this.objectDiv.bind('createPool', {objectId: this.objectId}, function(event, poolElement){
			mw3.getFaceHelper(event.data.objectId).createPoolView(poolElement);
		});
		this.objectDiv.bind('setScale', {objectId: this.objectId}, function(event, scale){
			mw3.getFaceHelper(event.data.objectId).getCanvas().setScale(scale);
		});
		this.objectDiv.bind('setFontFamily', {objectId: this.objectId}, function(event, fontFamily){
			mw3.getFaceHelper(event.data.objectId).getCanvas()._HANDLER.setFontFamilySelectedShape(fontFamily);
		});
		this.objectDiv.bind('setFontColor', {objectId: this.objectId}, function(event, fontColor){
			mw3.getFaceHelper(event.data.objectId).getCanvas()._HANDLER.setFontColorSelectedShape(fontColor);
		});
		this.objectDiv.bind('setFontSize', {objectId: this.objectId}, function(event, fontSize){
			mw3.getFaceHelper(event.data.objectId).getCanvas()._HANDLER.setFontSizeSelectedShape(fontSize);
		});
		this.objectDiv.bind('showProperty', {objectId: this.objectId}, function(event, fontSize){
			mw3.getFaceHelper(event.data.objectId).getCanvas()._HANDLER.showProperty();
		});
	},

	getCanvas : function(){
		return this.canvas;
	},

	initHistory : function(){
		if(this.historyInit){
			this.history = [this.getCanvas().toJSON()];
			this.historyIndex = 0;
		}
	},

	addHistory : function(){
		this.historyInit = false;

		this.history.splice(this.historyIndex+1);
		this.history.push(this.getCanvas().toJSON());
		this.historyIndex = this.history.length-1;
	},

	undo : function(){
		if(this.historyIndex > 0)
			this.canvas.loadJSON(this.history[--this.historyIndex]);
	},
	redo : function(){
		if(this.historyIndex < this.history.length-1)
			this.canvas.loadJSON(this.history[++this.historyIndex]);
	},

	toAppend : function(object){
		this.historyInit = false;

		object.x = mw3.dropX - $("#canvas_" + this.objectId)[0].offsetLeft + $("#canvas_" + this.objectId)[0].scrollLeft - $("#canvas_" + this.objectId).offsetParent().offset().left;
		object.y = mw3.dropY - $('#canvas_' + this.objectId)[0].offsetTop + $('#canvas_' + this.objectId)[0].scrollTop - $('#canvas_' + this.objectId).offsetParent().offset().top;

		var isArray = Object.prototype.toString.call(object) == '[object Array]';

		if(!isArray){
			var tmpObject = object;

			object = [];
			object.push(tmpObject);
		}

		for(var i=0; i < object.length; i++){
			this.addView(object[i]);
		}
	},
	/*toAppend : function(object){
	 this.historyInit = false;

	 object.x = mw3.dropX - $("#canvas_" + this.objectId)[0].offsetLeft + $("#canvas_" + this.objectId)[0].scrollLeft - $("#canvas_" + this.objectId).offsetParent().offset().left;
	 object.y = mw3.dropY - $('#canvas_' + this.objectId)[0].offsetTop + $('#canvas_' + this.objectId)[0].scrollTop - $('#canvas_' + this.objectId).offsetParent().offset().top;

	 if(object.element){
	 var elementViewListId = mw3.getChildObjectId(this.objectId, 'elementViewList');
	 var elementViewListObject = mw3.objects[elementViewListId];

	 elementViewListObject.__faceHelper.add(object);
	 }else if(object.relation){
	 var relationViewListId = mw3.getChildObjectId(this.objectId, 'relationViewList');
	 var relationViewListObject = mw3.objects[relationViewListId];

	 relationViewListObject.__faceHelper.add(object);
	 }else if(object.length > 0){

	 if(object[0].element){
	 var elementViewListId = mw3.getChildObjectId(this.objectId, 'elementViewList');
	 var elementViewListObject = mw3.objects[elementViewListId];
	 for(var i=0; i < object.length; i++){
	 var view = object[i];
	 elementViewListObject.__faceHelper.add(view);
	 }

	 }
	 if(object[0].relation){
	 var relationViewListId = mw3.getChildObjectId(this.objectId, 'relationViewList');
	 var relationViewListObject = mw3.objects[relationViewListId];
	 for(var i=0; i < object.length; i++){
	 var view = object[i];
	 relationViewListObject.__faceHelper.add(view);
	 }

	 }
	 }
	 },*/

	addView: function(object){

		if(object.element){
			var elementViewListId = mw3.getChildObjectId(this.objectId, 'elementViewList');
			var elementViewListObject = mw3.objects[elementViewListId];

			elementViewListObject.__faceHelper.add(object);
		}else if(object.relation){
			var relationViewListId = mw3.getChildObjectId(this.objectId, 'relationViewList');
			var relationViewListObject = mw3.objects[relationViewListId];

			relationViewListObject.__faceHelper.add(object);
		}
	},

	createPoolView: function(poolElement){
		var pool = {
			__className: 'org.uengine.kernel.Pool'
		};
		var poolView = {
			__className: 'org.uengine.kernel.designer.ui.PoolView',
			id: poolElement.id,
			element: pool
		};

		var elementViewListId = mw3.getChildObjectId(this.objectId, 'elementViewList');
		var elementViewListObject = mw3.objects[elementViewListId];

		elementViewListObject.__faceHelper.add(poolView);
	},

	createTransitionView: function(edge, from, to){
		var existEdge = mw3.getAutowiredObject('org.uengine.modeling.RelationView@' + edge.id);

		if(existEdge){
			return null;
		}

		var sourceElementView = mw3.getAutowiredObject('org.uengine.modeling.ElementView@' + from.id);
		var targetElementView = mw3.getAutowiredObject('org.uengine.modeling.ElementView@' + to.id);


		if(!sourceElementView) throw new Error("can't find sourceElementView of [" + from.id + "]");
		if(!targetElementView) throw new Error("can't find targetElementView of [" + to.id + "]");

		var shapeId = from.shape.SHAPE_ID.split('.')[2];
		var transitionView;

		if(from.shape.SHAPE_ID == "OG.shape.bpmn.Value_Chain"){
			var transition = {
				__className: 'org.uengine.modeling.Relation',
				sourceElement: sourceElementView.element,
				targetElement: targetElementView.element
			};
			var transitionView = {
				__className: 'org.uengine.modeling.RelationView',
				id: edge.id,
				from: $(edge).attr('_from'),
				to: $(edge).attr('_to'),
				relation: transition
			};
		}else if(from.shape.SHAPE_ID == "OG.shape.bpmn.Value_Chain_Module"){
			var transition = {
				__className: 'org.uengine.modeling.Relation',
				sourceElement: sourceElementView.element,
				targetElement: targetElementView.element
			};
			var transitionView = {
				__className: 'org.uengine.modeling.RelationView',
				id: edge.id,
				from: $(edge).attr('_from'),
				to: $(edge).attr('_to'),
				relation: transition
			};
		}else if(shapeId == "essencia"){
			var transition = {
				__className: 'org.uengine.uml.model.CompositionRelation',
				sourceElement: sourceElementView.element,
				targetElement: targetElementView.element
			};
			var transitionView = {
				__className: 'org.uengine.uml.ui.CompositionRelationView',
				id: edge.id,
				from: $(edge).attr('_from'),
				to: $(edge).attr('_to'),
				relation: transition
			};
		}else{
			var transition = {
				__className: 'org.uengine.kernel.bpmn.SequenceFlow',
				sourceElement: sourceElementView.element,
				targetElement: targetElementView.element,
				source: sourceElementView.element.tracingTag,
				target: targetElementView.element.tracingTag
			};
			var transitionView = {
				__className: 'org.uengine.kernel.bpmn.view.SequenceFlowView',
				id: edge.id,
				from: $(edge).attr('_from'),
				to: $(edge).attr('_to'),
				relation: transition
			};
		}

		var relationViewListId = mw3.getChildObjectId(this.objectId, 'relationViewList');
		var relationViewListObject = mw3.objects[relationViewListId];

		relationViewListObject.__faceHelper.add(transitionView);
	},

	removeElement : function(element){
		if(element.getAttribute('_shape') != 'EDGE'){
			var removeElementView = mw3.getAutowiredObject('org.uengine.modeling.ElementView@' + element.id);
			var elementViewListId = mw3.getChildObjectId(this.objectId, 'elementViewList');
			var elementViewListObject = mw3.objects[elementViewListId];
			elementViewListObject.__faceHelper.remove(removeElementView);
		}else{
			var removeRelationView = mw3.getAutowiredObject('org.uengine.modeling.RelationView@' + element.id);
			var relatioinViewListId = mw3.getChildObjectId(this.objectId, 'relationViewList');
			var relatioinViewListObject = mw3.objects[relatioinViewListId];
			relatioinViewListObject.__faceHelper.remove(removeRelationView);
		}
	},

	addRedoList : function(){
	}

	/*,
	 getValue : function(){
	 var object = mw3.objects[this.objectId];

	 if(false){
	 var data = document.body.getElementsByTagName("svg")[0].cloneNode(true);
	 data = (new XMLSerializer).serializeToString(data);

	 this.object.svgData = data;
	 }
	 }
	 */
};

org_uengine_modeling_Canvas.prototype.resize = function(){
	var isChange = false;
	var tempWidth = mw3.canvas.getRootBBox().width;
	var tempHeight = mw3.canvas.getRootBBox().height;

	var canvasWidth = document.getElementById('canvas_' + this.objectId).getBoundingClientRect().width;
	var canvasHeight = document.getElementById('canvas_' + this.objectId).getBoundingClientRect().height;

	if(tempWidth < canvasWidth){
		isChange = true;
		tempWidth = canvasWidth;
	}
	if(tempHeight < canvasHeight){
		isChange = true;
		tempHeight = canvasHeight;
	}

	if(isChange){
		mw3.canvas.setCanvasSize([tempWidth, tempHeight]);
	}

};

//org_uengine_modeling_Canvas.prototype.refresh = function(){
//	console.log('refresh');
//}

$.ui.intersect = (function() {
	function isOverAxis( x, reference, size ) {
		return ( x >= reference ) && ( x < ( reference + size ) );
	}

	return function( draggable, droppable, toleranceMode ) {

		if ( !droppable.offset ) {
			return false;
		}

		var draggableLeft, draggableTop,
			x1 = ( draggable.positionAbs || draggable.position.absolute ).left,
			y1 = ( draggable.positionAbs || draggable.position.absolute ).top,
			x2 = x1 + draggable.helperProportions.width,
			y2 = y1 + draggable.helperProportions.height,
			l = droppable.offset.left,
			t = droppable.offset.top,
			r = l + droppable.proportions().width,
			b = t + droppable.proportions().height;

		switch ( toleranceMode ) {
			case "fit":
				return ( l <= x1 && x2 <= r && t <= y1 && y2 <= b );
			case "intersect":
				return ( l < x1 + ( draggable.helperProportions.width / 2 ) && // Right Half
				x2 - ( draggable.helperProportions.width / 2 ) < r && // Left Half
				t < y1 + ( draggable.helperProportions.height / 2 ) && // Bottom Half
				y2 - ( draggable.helperProportions.height / 2 ) < b ); // Top Half
			case "pointer":
				draggableLeft = ( ( draggable.positionAbs || draggable.position.absolute ).left + ( draggable.clickOffset || draggable.offset.click ).left );
				draggableTop = ( ( draggable.positionAbs || draggable.position.absolute ).top + ( draggable.clickOffset || draggable.offset.click ).top );
				return isOverAxis( draggableTop, t, droppable.proportions().height ) && isOverAxis( draggableLeft, l, droppable.proportions().width );
			case "touch":
				return (
					( y1 >= t && y1 <= b ) || // Top edge touching
					( y2 >= t && y2 <= b ) || // Bottom edge touching
					( y1 < t && y2 > b ) // Surrounded vertically
					) && (
					( x1 >= l && x1 <= r ) || // Left edge touching
					( x2 >= l && x2 <= r ) || // Right edge touching
					( x1 < l && x2 > r ) // Surrounded horizontally
					);
			case "geom":
				var boundary = droppable.element[0].shape.geom.getBoundary();

				var draggableLeft = ((draggable.positionAbs || draggable.position.absolute).left + (draggable.clickOffset || draggable.offset.click).left),
					draggableTop = ((draggable.positionAbs || draggable.position.absolute).top + (draggable.clickOffset || draggable.offset.click).top);

				return ( l < x1 + ( draggable.helperProportions.width / 2 ) && // Right Half
				x2 - ( draggable.helperProportions.width / 2 ) < l + boundary._width && // Left Half
				t < y1 + ( draggable.helperProportions.height / 2 ) && // Bottom Half
				y2 - ( draggable.helperProportions.height / 2 ) < t + boundary._height ); // Top Half
			default:
				return false;
		}
	};
})();