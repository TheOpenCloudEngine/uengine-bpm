var org_uengine_modeling_ElementView = function (objectId, className) {
    this.objectId = objectId;
    this.className = className;
    this.object = mw3.objects[this.objectId];
    this.objectDivId = mw3._getObjectDivId(this.objectId);
    this.objectDiv = $(document.getElementById(this.objectDivId));
    if (!this.object) return true;

    this.isNew = true;
    this.metadata = mw3.getMetadata(this.className);


    this.getValue = function () {
        if (this.element) {
            if ($('#' + this.element.id).length == 0)
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
            if ($(this.element).parent().attr('id') === $(this.rootgroup).attr('id')) {
                this.object.parent = 'null';
            } else {
                this.object.parent = $(this.element).parent().attr('id');
            }
            return this.object;
        }
    };


    this.getLabel = function () {
        if (this.object.element && this.object.element.name)
            this.object.label = this.object.element.name;
        //mw3.getObjectNameValue(this.object.element, true);

        return unescape(this.object.label ? this.object.label : '');
    };

    this.getCanvas = function () {
        var canvasId = mw3.getClosestObject(this.objectId, "org.uengine.modeling.Canvas").__objectId;
        var object = mw3.objects[canvasId];
        return object.canvas;
    };

    this.getRenderer = function () {
        return this.getCanvas()._RENDERER;
    };

    this.setParent = function (elementId, parentId) {
        if (!elementId || !parentId) {
            return;
        }
        if (!this.canvas.groupReservations) {
            this.canvas.groupReservations = {};
        }
        var element = this.renderer.getElementById(elementId);
        var parentElement = this.renderer.getElementById(parentId);

        if (!element) {
            return;
        }

        //부모가 캔버스에 아직 그려지지 않았을 경우 예약을 걸어놓는다.
        if (!parentElement) {
            if (!this.canvas.groupReservations[parentId]) {
                this.canvas.groupReservations[parentId] = [];
            }
            if (this.canvas.groupReservations[parentId].indexOf(elementId) === -1) {
                this.canvas.groupReservations[parentId].push(elementId);
            }
        }

        //자신에게 예약이 걸려있는것이 있다면 그룹을 맺는다.
        var reservations = this.canvas.groupReservations[elementId];
        if (!reservations) {
            return;
        }

        for (var i = 0; i < reservations.length; i++) {
            var reservedElementId = reservations[i];
            var reservedElement = this.renderer.getElementById(reservedElementId);
            if (reservedElement) {
                element.appendChild(reservedElement);
            }
        }
    };

    this.init = function () {
        this.canvas = this.getCanvas();
        this.renderer = this.getRenderer();
        this.element = null;
        this.rootgroup = this.renderer.getRootGroup();
        this.byDrop = this.object.byDrop;

        //verification data first.
        if (this.object.shapeId == null)
            throw new Error("No shape Id is set for " + this.object);


        //concern 별 색상 적용을 위해 by soo
        //이 부분과
        var concern, concernColor, lineColor;
        if (this.object && this.object.element && this.object.element.concern != null) {
            concern = this.object.element.concern;

            if (concern == "Customer") {
                concernColor = "#2ecc71 ";
                lineColor = "#27ae60 ";
            }
            else if (concern == "Solution") {
                concernColor = "#f1c40f ";
                lineColor = "#f39c12 ";
            }
            else if (concern == "Endeavor") {
                concernColor = "#3498db ";
                lineColor = "#2980b9 ";
            }
        }


        var existElement = this.canvas.getElementById(this.object.id);
        if (existElement) {
            this.canvas.drawLabel(existElement, this.getLabel());
            this.element = existElement;
            this.isNew = false;

            //concern 별 color 적용 by soo
            //이 부분
            if (concern != null)
                this.canvas._RENDERER.setShapeStyle(this.element, {
                    "fill": concernColor,
                    "fill-opacity": 1,
                    "stroke": lineColor
                });

        } else {

            var shape = eval('new ' + this.object.shapeId);
            shape.label = this.getLabel();

            if (this.object.instStatus) {
                if ("Completed" == this.object.instStatus || "Running" == this.object.instStatus) {
                    shape.status = this.object.instStatus;
                }
            }

            var style = this.object.style;
            var boundary;

            //concern 별 color 적용 by soo
            //이 부분을 추가하시면 될 것 같습니다.
            if (style == "null" && concern != null) {
                style = new OG.geometry.Style({
                    fill: concernColor,
                    "fill-opacity": 1,
                    'stroke': lineColor
                });
            }

            var preventDrop = this.byDrop ? false : true;
            this.element = this.canvas.drawShape([this.object.x, this.object.y],
                shape,
                [parseInt(this.object.width, 10), parseInt(this.object.height, 10)],
                OG.JSON.decode(unescape(style)),
                this.object.id,
                this.object.parent,
                preventDrop);

            this.setParent(this.element.id, this.object.parent);
            if (this.renderer.isLane(this.element)) {
                this.renderer.fitLaneOrder(this.element);
            }

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

        //캔버스가 리모트 모드이고 프로퍼티 변경으로 인해 새로 그려진 엘리먼트일경우 브로드캐스트 수행
        if (this.canvas.getRemotable()) {
            if (this.object.changed) {
                OG.RemoteHandler.broadCastCanvas(this.canvas, function (canvas) {

                });
            }
        }

        this.bindMapping();
    };

    this.bindMapping = function () {
        var metadata = mw3.getMetadata(this.className);
        var contextMenus = [];
        for (var methodName in metadata.serviceMethodContextMap) {
            if (mw3.isHiddenMethodContext(this.metadata.serviceMethodContextMap[methodName], this.object))
                continue;

            var methodContext = metadata.serviceMethodContextMap[methodName];

            if (methodContext.eventBinding) {
                for (var eventNameIndex in methodContext.eventBinding) {
                    var eventName = methodContext.eventBinding[eventNameIndex];

                    this.bind(eventName);
                }
            }

            if (methodContext.mouseBinding) {
                var which = 3;
                if (methodContext.mouseBinding == "right")
                    which = 3;
                else if (methodContext.mouseBinding == "left")
                    which = 1;

                if (methodContext.mouseBinding == "drop") {

                    $(this.element).droppable({
                        greedy: true,
                        tolerance: 'geom'
                    }).attr('droppable', true);

                    var command = "if(mw3.objects['" + this.objectId + "']!=null) mw3.call(" + this.objectId + ", '" + methodName + "')";
                    $(this.element).on('drop.' + this.objectId, {command: command}, function (event, ui) {
                        if (Object.prototype.toString.call(ui.draggable[0]) != "[object SVGGElement]") {
                            if (Object.prototype.toString.call(ui.draggable[0]) != "[object SVGRectElement]") {
                                eval(event.data.command);
                            }
                        }
                    });
                } else {
                    // click(mouse right) is contextmenu block
                    if (which == 3) {

                        $(this.element).bind('contextmenu', function (event) {
                            return false;
                        });
                    }

                    $(this.element).on((which == 3 ? 'mouseup' : 'click') + '.' + this.objectId, {
                        which: which,
                        objectId: this.objectId
                    }, function (event) {
                        $(document.getElementById(mw3._getObjectDivId(event.data.objectId))).trigger(event);
                    });

                }
            }

            if(methodContext.inContextMenu){
                contextMenus[contextMenus.length] = methodContext;
            }
        }

        // implements the context menu events
        var items = {}; var touched = false;
        for(var i in contextMenus){
            var contextMenuMethod = contextMenus[i];

            var command = "mw3.objects[" + this.objectId + "]." + contextMenuMethod.methodName + "()"
            console.log("command="+command);

            items[command] =
            {
                name: contextMenuMethod.displayName ? contextMenuMethod.displayName : contextMenuMethod.methodName
            };

            touched = true;
        }

        if(touched){
            $("#" + this.element.id).off('contextmenu');

            $.contextMenu({
                position: function (opt, x, y) {
                    opt.$menu.css({top: y + 10, left: x + 10});
                },
                selector: "#" + this.element.id,
                callback: function (key, options) {
                    console.log("key="+key);
                    eval(key);

                },
                items: items
            });

            console.log('touched')
        }
    }




    this.bind = function (name) {
        try{

            var events = $(this.element).data("events")[name];

            for(var i in events){
                var event = events[i];
                if(event.namespace == this.objectId)
                    return; //already existing event
            }

        }catch(e){}

        $(this.element).bind(name + '.' + this.objectId, {objectId: this.objectId}, function (event, ui) {
            $(document.getElementById(mw3._getObjectDivId(event.data.objectId))).trigger(event.type);
            event.stopPropagation();
        });
    };

    this.destroy = function () {
        if ($(this.element).attr('droppable'))
            $(this.element).droppable("destroy");

        $(this.element).unbind('.' + this.objectId);
    };

    this.autoResizeCanvas = function (boundary) {
        var rootBBox = this.canvas._RENDERER.getRootBBox();
        if (rootBBox.width < (boundary._centroid.x + boundary._width) * this.canvas._CONFIG.SCALE) {
            this.canvas._RENDERER.setCanvasSize([boundary._centroid.x + boundary._width, rootBBox.height]);
        }
        if (rootBBox.height < (boundary._centroid.y + boundary._height) * this.canvas._CONFIG.SCALE) {
            this.canvas._RENDERER.setCanvasSize([rootBBox.width, boundary._centroid.y + boundary._height]);
        }
    };

    this.init();
};