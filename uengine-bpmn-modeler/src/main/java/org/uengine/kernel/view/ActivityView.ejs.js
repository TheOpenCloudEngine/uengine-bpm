var org_uengine_kernel_view_ActivityView = function(objectId, className){

	org_uengine_modeling_ElementView.apply(this, new Array(objectId, className));

	var modeler = mw3.getClosestObject(objectId, 'org.uengine.modeling.Modeler');
	this.object.element.tracingTag = modeler.getFaceHelper().getTracingTag();

	this.element.__className = className;

	// draw markers and badges
	{
		var me = this.canvas._RENDERER, rElement = me._getREleById(OG.Util.isElement(this.element) ? this.element.id : this.element),
			geometry = rElement ? rElement.node.shape.geom : null,
			envelope, _upperLeft, _bBoxRect, _rect, _rect1,
			_size = me._CONFIG.COLLAPSE_SIZE,
			_hSize = _size / 2;

		////draw task image
		//{
		//	_rect1 = me._getREleById(rElement.id + OG.Constants.TASKTYPE_SUFFIX);
		//	if (_rect1) {
		//		me._remove(_rect1);
		//	}
        //
		//	envelope = geometry.getBoundary();
		//	_upperLeft = envelope.getUpperLeft();
        //
		//	_rect1 = me._PAPER.image("resources/images/symbol/" + this.object.element.__className + ".png", _upperLeft.x + 5, _upperLeft.y + 5, 20, 20);
        //
		//	me._add(_rect1, rElement.id + OG.Constants.TASKTYPE_SUFFIX);
		//	_rect1.insertAfter(rElement);
		//	rElement.appendChild(_rect1);
		//}

		//draw validation error image
		if(modeler.metaworksContext.where!='instance')
		{
			var badge_suffix = "_ValidErr";

			_rect1 = me._getREleById(rElement.id + badge_suffix);
			if (_rect1) {
				me._remove(_rect1);
			}

			if(this.object.element.integrity){


				envelope = geometry.getBoundary();
				var position = envelope.getUpperRight();

				_rect1 = me._PAPER.image("images/opengraph/fail.png", position.x - 25, position.y + 5, 20, 20);

				me._add(_rect1, rElement.id + badge_suffix);
				_rect1.insertAfter(rElement);
				rElement.appendChild(_rect1);
			}
		}

	}

	//this.canvas.bind('redrawShape', this.element, function(){
	//	additionalDrawing();
	//});

}
extend(org_uengine_kernel_view_ActivityView, "org_uengine_modeling_ElementView");


org_uengine_kernel_view_ActivityView.prototype.additionalDrawing = function(){


}

///  ------ i don't know why following exists ------

OG.shape.bpmn.A_Task.prototype.drawCustomControl = function(handler, element) {
	if (!handler || !element) return;

	handler._RENDERER.selectedElement = element;

}

//3번 과 5번 사이에 4번을 실행하기 전에 className 을 전달해주기 위해서 또 오픈그래프를 오버라이딩 해야함 ㅠㅠ
OG.graph.Canvas.prototype.drawShape = function (position, shape, size, style, id, parentId, preventDrop, className) {

	//className 수정 부분
	var element = this._RENDERER.drawShape(position, shape, size, style, id, preventDrop, className);

	if (position && (shape.TYPE === OG.Constants.SHAPE_TYPE.EDGE)) {
		element = this._RENDERER.move(element, position);
	}

	if (parentId && this._RENDERER.getElementById(parentId)) {
		this._RENDERER.appendChild(element, parentId);
	}

	if (!this.CONFIG_INITIALIZED) {
		this.initConfig();
	}

	this._HANDLER.setClickSelectable(element, this._HANDLER._isSelectable(element.shape));
	this._HANDLER.setMovable(element, this._HANDLER._isMovable(element.shape));
	this._HANDLER.setGroupDropable(element);
	this._HANDLER.setConnectGuide(element, this._HANDLER._isConnectable(element.shape));

	if (this._HANDLER._isLabelEditable(element.shape)) {
		this._HANDLER.enableEditLabel(element);
	}

	if (element.shape.HaveButton) {   // + 버튼을 만들기 위해서
		this._HANDLER.enableButton(element);
	}

	if (this._CONFIG.GROUP_COLLAPSIBLE && element.shape.GROUP_COLLAPSIBLE) {
		this._HANDLER.enableCollapse(element);
	}

	if (!id) {
		this._RENDERER.addHistory();
	}
	this.updateSlider();
	return element;
};

OG.renderer.RaphaelRenderer.prototype.drawShape = function (position, shape, size, style, id, preventDrop, className) {
	var width = size ? size[0] : 100,
		height = size ? size[1] : 100,
		groupNode, geometry, text, image, html,
		me = this;

	if (shape instanceof OG.shape.GeomShape) {
		geometry = shape.createShape();

		// 좌상단으로 이동 및 크기 조정
		geometry.moveCentroid(position);
		geometry.resizeBox(width, height);

		groupNode = this.drawGeom(geometry, style, id);
		shape.geom = groupNode.geom;


	} else if (shape instanceof OG.shape.TextShape) {
		text = shape.createShape();

		groupNode = this.drawText(position, text, size, style, id);
		shape.text = groupNode.text;
		shape.angle = groupNode.angle;
		shape.geom = groupNode.geom;
	} else if (shape instanceof OG.shape.ImageShape) {
		image = shape.createShape();

		groupNode = this.drawImage(position, image, size, style, id);
		shape.image = groupNode.image;
		shape.angle = groupNode.angle;
		shape.geom = groupNode.geom;
	} else if (shape instanceof OG.shape.HtmlShape) {
		html = shape.createShape();

		groupNode = this.drawHtml(position, html, size, style, id);
		shape.html = groupNode.html;
		shape.angle = groupNode.angle;
		shape.geom = groupNode.geom;
	} else if (shape instanceof OG.shape.EdgeShape) {
		geometry = shape.geom || shape.createShape();

		groupNode = this.drawEdge(geometry, style, id);
		shape.geom = groupNode.geom;
	} else if (shape instanceof OG.shape.GroupShape) {
		geometry = shape.createShape();

		// 좌상단으로 이동 및 크기 조정
		geometry.moveCentroid(position);
		geometry.resizeBox(width, height);

		groupNode = this.drawGroup(geometry, style, id);
		shape.geom = groupNode.geom;
	}

	if (shape.geom) {
		groupNode.shape = shape;
	}
	groupNode.shapeStyle = (style instanceof OG.geometry.Style) ? style.map : style;
	$(groupNode).attr("_shape_id", shape.SHAPE_ID);

	// Draw for Task
	if (shape instanceof OG.shape.bpmn.A_Task) {
		if (groupNode.shape.LoopType != 'None')
			this.drawLoopType(groupNode);
		if (groupNode.shape.TaskType != 'None')
		//className 수정 부분
			this.drawTaskType(groupNode, className);

		//redrawShape 를 호출하는 오픈그래프의 여러가지 메소드에 적용할 수 있도록 element 에 어트리튜브로 저장해놓도록 한다.
		$(groupNode).attr('__className',className);

		if (groupNode.shape.status != 'None')
			this.drawStatus(groupNode);
	}

	if (shape instanceof OG.shape.bpmn.A_Subprocess) {
		if (groupNode.shape.status != 'None')
			this.drawStatus(groupNode);
		if (groupNode.shape.inclusion)
			this.drawCheckInclusion(groupNode);
	}

	if (shape instanceof OG.shape.bpmn.Value_Chain) {
		if (groupNode.shape.inclusion)
			this.drawCheckInclusion(groupNode);
	}

	if (shape instanceof OG.shape.bpmn.Value_Chain_Module) {
		if (groupNode.shape.inclusion)
			this.drawCheckInclusion(groupNode);
	}

	if (shape instanceof OG.shape.bpmn.E_Start) {
		if (groupNode.shape.inclusion)
			this.drawCheckInclusion(groupNode);
	}

	if (shape instanceof OG.shape.bpmn.E_End) {
		if (groupNode.shape.inclusion)
			this.drawCheckInclusion(groupNode);
	}

	// Draw Error
	if (groupNode.shape.exceptionType != '')
		this.drawExceptionType(groupNode);


	// Draw Label
	if (!(shape instanceof OG.shape.TextShape)) {
		this.drawLabel(groupNode);

		if (shape instanceof  OG.shape.EdgeShape) {
			this.drawEdgeLabel(groupNode, null, 'FROM');
			this.drawEdgeLabel(groupNode, null, 'TO');
		}
	}
	if (groupNode.geom) {
		if (OG.Util.isIE7()) {
			groupNode.removeAttribute("geom");
		} else {
			delete groupNode.geom;
		}
	}
	if (groupNode.text) {
		if (OG.Util.isIE7()) {
			groupNode.removeAttribute("text");
		} else {
			delete groupNode.text;
		}
	}
	if (groupNode.image) {
		if (OG.Util.isIE7()) {
			groupNode.removeAttribute("image");
		} else {
			delete groupNode.image;
		}
	}
	if (groupNode.angle) {
		if (OG.Util.isIE7()) {
			groupNode.removeAttribute("angle");
		} else {
			delete groupNode.angle;
		}
	}

	//신규 shape 이면 그룹위에 그려졌을 경우 그룹처리
	var setGroup = function () {
		var frontGroup = me.getFrontForBoundary(me.getBoundary(groupNode));

		if (!frontGroup) {
			return;
		}
		//draw 대상이 Edge 이면 리턴.
		if (me.isEdge(groupNode)) {
			return;
		}
		//draw 대상이 Lane 인 경우 리턴.
		if (me.isLane(groupNode)) {
			return;
		}
		//그룹이 Lane 인 경우 RootLane 으로 변경
		if (me.isLane(frontGroup)) {
			frontGroup = me.getRootLane(frontGroup);
		}
		if (!me._CONFIG.GROUP_DROPABLE || !frontGroup.shape.GROUP_DROPABLE) {
			return;
		}

		//그룹이 A_Task 일경우 반응하지 않는다.
		if (frontGroup.shape instanceof OG.shape.bpmn.A_Task) {
			return;
		}

		//자신일 경우 반응하지 않는다.
		if (frontGroup.id === groupNode.id) {
			return;
		}
		frontGroup.appendChild(groupNode);
	};
	if (!id) {
		setGroup();
	}

	//신규 Lane 또는 Pool 이 그려졌을 경우 처리
	if (!id && (me.isLane(groupNode) || me.isPool(groupNode))) {
		//if (preventDrop) {
		//    me.putInnerShapeToPool(groupNode);
		//} else {
		//    me.setDropablePool(groupNode);
		//}
		me.putInnerShapeToPool(groupNode);
	}

	if ($(shape).attr('auto_draw') == 'yes') {
		$(groupNode).attr('auto_draw', 'yes');
	}

	// drawShape event fire
	$(this._PAPER.canvas).trigger('drawShape', [groupNode]);

	return groupNode;
};

//4번. ElementView.ejs.js 에서 그리면서 오픈그래프가 중간에 이 메소드를 호춣함.
OG.renderer.RaphaelRenderer.prototype.drawTaskType = function (element, className) {

	var me = this, rElement = this._getREleById(OG.Util.isElement(element) ? element.id : element),
		geometry = rElement ? rElement.node.shape.geom : null,
		envelope, _upperLeft, _bBoxRect, _rect, _rect1,
		_size = me._CONFIG.COLLAPSE_SIZE,
		_hSize = _size / 2;

	_rect1 = this._getREleById(rElement.id + OG.Constants.TASKTYPE_SUFFIX);
	if (_rect1) {
		this._remove(_rect1);
	}

	envelope = geometry.getBoundary();
	_upperLeft = envelope.getUpperLeft();

	//위에서 수정한 drawShape 메소드를 거치지 아니고하고 다른 이벤트를 통해서 호출되었을 경우(예)redrawShape 등)
	className = className ? className : $(element).attr('__className');


	_rect1 = this._PAPER.image("resources/images/symbol/" + className + ".png", _upperLeft.x + 5, _upperLeft.y + 5, 20, 20);

	this._add(_rect1, rElement.id + OG.Constants.TASKTYPE_SUFFIX);
	_rect1.insertAfter(rElement);
	rElement.appendChild(_rect1);

	return null;
};