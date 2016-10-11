var org_uengine_kernel_view_ActivityView = function(objectId, className){

	org_uengine_modeling_ElementView.apply(this, new Array(objectId, className));

	var modeler = mw3.getAutowiredObject('org.uengine.modeling.Modeler');
	this.object.element.tracingTag = modeler.getFaceHelper().getTracingTag();

	// draw markers and badges
	{
		var me = this.canvas._RENDERER, rElement = me._getREleById(OG.Util.isElement(this.element) ? this.element.id : this.element),
			geometry = rElement ? rElement.node.shape.geom : null,
			envelope, _upperLeft, _bBoxRect, _rect, _rect1,
			_size = me._CONFIG.COLLAPSE_SIZE,
			_hSize = _size / 2;

		//draw task image
		{
			_rect1 = me._getREleById(rElement.id + OG.Constants.TASKTYPE_SUFFIX);
			if (_rect1) {
				me._remove(_rect1);
			}

			envelope = geometry.getBoundary();
			_upperLeft = envelope.getUpperLeft();

			_rect1 = me._PAPER.image("resources/images/symbol/" + this.object.element.__className + ".png", _upperLeft.x + 5, _upperLeft.y + 5, 20, 20);

			me._add(_rect1, rElement.id + OG.Constants.TASKTYPE_SUFFIX);
			_rect1.insertAfter(rElement);
			rElement.appendChild(_rect1);
		}

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