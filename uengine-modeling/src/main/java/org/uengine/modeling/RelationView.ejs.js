var org_uengine_modeling_RelationView = function(objectId, className){
	var faceHelper = this;

	this.objectId = objectId;
	this.className = className;
	this.object = mw3.objects[this.objectId];
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $(document.getElementById(this.objectDivId));

	this.canvas = faceHelper.getCanvas();
	this.element = null;

	this.metadata = mw3.getMetadata(this.className);

	faceHelper.init();
};

org_uengine_modeling_RelationView.prototype = {
	getValue : function(){
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

			this.object.from = $(this.element).attr('_from');
			this.object.to = $(this.element).attr('_to');

			var vertices = this.element.shape.geom.getVertices();
			this.object.vertices = vertices;

			this.object.value = '';
			for (var i = 0; i < vertices.length; i++) {
				this.object.value = this.object.value + vertices[i];
				if (i < vertices.length - 1) {
					this.object.value = this.object.value + ','
				}
			}


			this.object.geom = escape(this.element.shape.geom.toString());
			this.object.style = escape(OG.JSON.encode(this.element.shape.geom.style));

			if(this.object.from != undefined && this.object.to != undefined){

				var temp = this.object.from.split('_');
				var fromStr = temp[0] + '_' + temp[1] + '_' + temp[2];
				temp = this.object.to.split('_');
				var toStr = temp[0] + '_' + temp[1] + '_' + temp[2];

				var sourceElementView = mw3.getAutowiredObject('org.uengine.modeling.ElementView@' + fromStr, true);
				var targetElementView = mw3.getAutowiredObject('org.uengine.modeling.ElementView@' + toStr, true);

				if(sourceElementView != undefined && targetElementView != undefined){
					this.object.relation.sourceElement = sourceElementView.element;
					this.object.relation.targetElement = targetElementView.element;
					this.object.relation.source = sourceElementView.element.tracingTag;
					this.object.relation.target = targetElementView.element.tracingTag;
				}
			}
		}
	},

	getLabel : function(){
		return unescape(this.object.label!=null?this.object.label:'');
	},
	getCanvas : function(){

		//var canvasDiv = this.objectDiv.closest('.canvas');
		//var canvasId = canvasDiv.attr('objectId');
		//var canvasId = this.objectDiv.closest('.canvas').attr('id').split("_")[1];

		//var object = mw3.objects[canvasId];
		//return object.getFaceHelper().getCanvas();

		var canvasId = mw3.getAutowiredObject("org.uengine.modeling.Canvas").__objectId;
		return mw3.getFaceHelper(canvasId).getCanvas();
	},
	init : function(){
		var existElement = document.getElementById(this.object.id);
		var style = this.object.style;
		if(existElement){
			//this.canvas.drawLabel(existElement, this.object.label);

			this.element = existElement;

			this.bindMapping();
		}else if(this.object.from && this.object.to){
			var fromPos = this.object.from.indexOf('_TERMINAL_');
			var toPos = this.object.to.indexOf('_TERMINAL_');

			var fromElementId = this.object.from.substring(0, fromPos);
			var toElementId = this.object.to.substring(0, toPos);

			this.readyTargetElement(fromElementId, 'from');
			this.readyTargetElement(toElementId, 'to');
		}else if(this.object.from && !this.object.to){
			var list = JSON.parse('[' + this.object.value+ ']');
			var fromto = JSON.stringify(list[0]) + ',' + JSON.stringify(list[list.length - 1]);
			var shape = eval('new ' + this.object.shapeId + '(\'' + fromto + '\')');
			var geom = unescape(this.object.geom);

			if(geom){
				geom = OG.JSON.decode(geom);
				if (geom.type === OG.Constants.GEOM_NAME[OG.Constants.GEOM_TYPE.POLYLINE]) {
					geom = new OG.geometry.PolyLine(geom.vertices);
					shape.geom = geom;
				} else if (geom.type === OG.Constants.GEOM_NAME[OG.Constants.GEOM_TYPE.CURVE]) {
					geom = new OG.geometry.Curve(geom.controlPoints);
					shape.geom = geom;
				}
			}
			if (this.getLabel()) {
				shape.label = this.getLabel();
			}

			this.element = this.canvas.drawShape(null,
				shape,
				null,
				OG.JSON.decode(unescape(style)),
				this.object.id,
				null,
				null);

			if (this.object.from) {
				$(this.element).attr('_from', this.object.from);

				var pos = this.object.from.indexOf('_TERMINAL_');

				this.canvas._RENDERER.redrawShape($('#' + this.object.from.substring(0,pos))[0], null, true);
			}
		}else if(!this.object.from && this.object.to){
			var list = JSON.parse('[' + this.object.value+ ']');
			var fromto = JSON.stringify(list[0]) + ',' + JSON.stringify(list[list.length - 1]);
			var shape = eval('new ' + this.object.shapeId + '(\'' + fromto + '\')');
			var geom = unescape(this.object.geom);

			if(geom){
				geom = OG.JSON.decode(geom);
				if (geom.type === OG.Constants.GEOM_NAME[OG.Constants.GEOM_TYPE.POLYLINE]) {
					geom = new OG.geometry.PolyLine(geom.vertices);
					shape.geom = geom;
				} else if (geom.type === OG.Constants.GEOM_NAME[OG.Constants.GEOM_TYPE.CURVE]) {
					geom = new OG.geometry.Curve(geom.controlPoints);
					shape.geom = geom;
				}
			}
			if (this.getLabel()) {
				shape.label = this.getLabel();
			}

			this.element = this.canvas.drawShape(null,
				shape,
				null,
				OG.JSON.decode(unescape(style)),
				this.object.id,
				null,
				false);
			if (this.object.to) {
				$(this.element).attr('_to', this.object.to);

				var pos = this.object.to.indexOf('_TERMINAL_');

				this.canvas._RENDERER.redrawShape($('#' + this.object.to.substring(0,pos))[0], null, true);
			}
		}else{
			var shape = eval('new ' + this.object.shapeId);
			if (this.getLabel()) {
				shape.label = this.getLabel();
			}
			this.element = this.canvas.drawShape([this.object.x, this.object.y],
				shape,
				[parseInt(this.object.width, 10), parseInt(this.object.height, 10)],
				OG.JSON.decode(unescape(style)),
				this.object.id,
				null,
				null);

			this.object[this.metadata.keyFieldDescriptor.name] = this.element.id;

			mw3.putObjectIdKeyMapping(this.objectId, this.object, true);
		}
	},

	bindMapping : function(){
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
		$(this.element).bind(name + '.' + this.objectId, {objectId: this.objectId}, function(event, ui){
			$('#' + mw3._getObjectDivId(event.data.objectId)).trigger(event.type);
		});
	},

	destroy : function(){
		$(this.element).unbind('.' + this.objectId);
	},

	readyTargetElement : function(id, fieldName){
		if($('#' + id).length){
			this[fieldName] = true;
			this.draw();
		}else{
			$(this.canvas._CONTAINER).one('loaded.' + id, {updateFieldName: fieldName, objectId: this.objectId}, function(event){
				var faceHelper = mw3.getFaceHelper(event.data.objectId);
				faceHelper[fieldName] = true;
				faceHelper.draw();
			});
		}
	},

	draw: function(){
		var style = this.object.style;
		var fromPos = this.object.from.indexOf('_TERMINAL_');
		var toPos = this.object.to.indexOf('_TERMINAL_');
		var fromElementId = this.object.from.substring(0, fromPos);
		var toElementId = this.object.to.substring(0, toPos);
		var existElement = document.getElementById(this.object.id);

		if(!existElement){
			if($('#' + fromElementId).length && $('#' + toElementId).length){
				var geom;
				if(this.object.geom){
					geom = OG.JSON.decode(unescape(this.object.geom));
				}

				this.element = this.canvas.connectWithTerminalId(this.object.from, this.object.to , OG.JSON.decode(unescape(style)), this.getLabel(), this.object.id, this.object.shapeId, geom);
				this.bindMapping();
			}
		}
	}
};