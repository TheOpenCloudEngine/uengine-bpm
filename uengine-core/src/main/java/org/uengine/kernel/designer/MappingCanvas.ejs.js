var org_uengine_kernel_designer_MappingCanvas = function (objectId, className) {
    // default setting
    this.objectId = objectId;
    this.className = className;
    var linekedInfo = new ArrayList();
    this.linekedInfo = linekedInfo;
    var faceHelper = this;
    var canvas = null;

    var object = mw3.objects[this.objectId];
    if (object == null || typeof object == 'undefined') {
        this.object = {
            __objectId: this.objectId,
            __className: this.className
        };
    } else {
        this.object = object;
    }

    OG.common.Constants.CANVAS_BACKGROUND = "#fff";
    this.canvasId = object.canvasId;
    canvas = new OG.Canvas(this.canvasId);
    canvas._CONFIG.DEFAULT_STYLE.EDGE["edge-type"] = "straight";
    canvas._CONFIG.DEFAULT_STYLE.EDGE_SHADOW["edge-type"] = "straight";
    canvas.initConfig({
        selectable: true,
        dragSelectable: true,
        movable: true,
        resizable: false,
        connectable: true,
        selfConnectable: false,
        connectCloneable: false,
        connectRequired: true,
        labelEditable: false,
        groupDropable: false,
        collapsible: false,
        enableHotKey: false,
        enableContextMenu: false,
        autoExtensional: false
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
    var leftTreeObj = this.parentDivObj.find("#" + this.leftTreeId);
    var leftTreeHeight = 0;
    var rightTreeHeight = 0;

    var canvasDivObj = $('#' + this.canvasId);
    var canvasWidth = canvasDivObj.width();

    leftTreeObj.bind('loaded', {align: 'left'}, function (event) {
        leftTreeHeight = $(this).find('.filemgr-tree').height();
        if (leftTreeHeight > rightTreeHeight) {
            canvas.setCanvasSize([canvasWidth, leftTreeHeight]);
        }
        //faceHelper.drawTerminals(this.id, true, canvas , null , false);
    }).bind('expanded', function () {
        faceHelper.drawTerminals(this.id, true, canvas, null, false);
    }).bind('collapsed', function () {
        faceHelper.drawTerminals(this.id, true, canvas, null, false);
    }).bind('toAppended', function () {
        faceHelper.drawTerminals(this.id, true, canvas, null, true);
    });

    var rightTreeObj = this.parentDivObj.find("#" + this.rightTreeId);
    rightTreeObj.bind('loaded', {align: 'right'}, function (event) {
        rightTreeHeight = $(this).find('.filemgr-tree').height();
        if (rightTreeHeight > leftTreeHeight) {
            canvas.setCanvasSize([canvasWidth, rightTreeHeight]);
        }
        faceHelper.drawTerminals(this.id, false, canvas, null, false);
    }).bind('expanded', function () {
        faceHelper.drawTerminals(this.id, false, canvas, null, false);
    }).bind('collapsed', function () {
        faceHelper.drawTerminals(this.id, false, canvas, null, false);
    }).bind('toAppended', function () {
        faceHelper.drawTerminals(this.id, false, canvas, null, true);
    });

    var mappingTab;
    var aTags = $(document).find('a');
    aTags.each(function(){
        if($(this).text() === 'Mapping'){
            mappingTab = $(this);
        }
    });
    if(mappingTab){
        mappingTab.click(function () {
            setTimeout(function () {
                var treeDivId = leftTreeObj.attr('id');
                faceHelper.drawTerminals(treeDivId, true, canvas, null, false);

                treeDivId = rightTreeObj.attr('id');
                faceHelper.drawTerminals(treeDivId, false, canvas, null, false);
            }, 100);
        });
    }

    canvas.onConnectShape(function (event, edgeElement, fromElement, toElement) {
        var linkedInfoStr = fromElement.id + "," + toElement.id;
        linekedInfo.add(linkedInfoStr);
        faceHelper.linekedInfo = linekedInfo;
    });

    canvas.onDisconnectShape(function (event, edgeElement, fromElement, toElement) {
        var linkedInfoStr = fromElement.id + "," + toElement.id;
        for (var i = 0; i < linekedInfo.size(); i++) {
            if (linekedInfo.get(i) == linkedInfoStr) {
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
    drawTerminals: function (treeDivId, isLeft, canvas, callback, isAppend) {
        var treeObjectId = null;
        if (isLeft) {
            treeObjectId = this.parentDivObj.find("#" + this.leftTreeId + " .filemgr-tree").attr('objectId');
        } else {
            treeObjectId = this.parentDivObj.find("#" + this.rightTreeId + " .filemgr-tree").attr('objectId');
        }
        var leftTreeLoding = this.leftTreeLoaded;
        var rightTreeLoding = this.rightTreeLoaded;
        var canvasId = this.canvasId;
        $('#' + treeDivId + ' .item-fix').each(function (idx, item) {
            if (!($(this).hasClass('root'))) {
                var treeNodeObject = $(this);
                var offset = treeNodeObject.offset();
                var canvasOffset = $('#' + canvasId).offset();
                var objectId = $(this).attr('objectId');
                var object = mw3.objects[objectId];
                var id = object.id;
                var shapeId = (isLeft ? 'FROM_' : 'TO_') + id;
                //- 를 . 으로 변경
                var name = object.id.replace(/-/gi, ".");
                var isView = true;
                var edgeIds;
                var closedParentObjectId = mw3.getFaceHelper(treeObjectId).getClosedParentNodes(objectId);
                var closedParentObject = mw3.objects[closedParentObjectId];
                // 동적으로 새롭게 붙은 노드들은 무조건 도형을 그려야 하기때문에 isAppend 를 체크하여줌
                if (object.type != 'folder' && closedParentObject != null && !isAppend) {
                    isView = false;
                }
                //일부 트리노드가 화면밖으로 빠져나갈경우가 있음.
                if ($(this).offset().top - $('#' + canvasId).offset().top < 0) {
                    isView = false;
                }
                if (isView) {
                    var position = [(isLeft ? 6 : 274), ( $(this).offset().top - $('#' + canvasId).offset().top ) + item.offsetHeight / 2];
                    var shapeElement = canvas.drawShape(
                        position,
                        (isLeft ? new OG.From() : new OG.To()),
                        [5, 5],
                        {"r": 5},
                        shapeId
                    );
                    canvas.setCustomData(shapeElement, {
                        type: isLeft ? 'variable' : 'argument',
                        name: name
                    });

                    edgeIds = $(shapeElement).attr(isLeft ? "_toedge" : "_fromedge");
                    if (edgeIds) {
                        $.each(edgeIds.split(","), function (indx, edgeId) {
                            var edge = canvas.getElementById(edgeId);
                            edge.shape.geom.style.map['stroke-dasharray'] = '';
                        });
                    }
                    $(shapeElement).click("destroy");
                    canvas.removeAllGuide();
                    canvas.redrawConnectedEdge(shapeElement);
                    canvas.show(shapeElement);
                } else {
                    var shapeElement = canvas.getElementById(shapeId);
                    if (shapeElement) {
                        var parentNode = $('.item-fix[objectId=' + closedParentObjectId + ']');
                        shapeElement = canvas.drawShape(
                            [(isLeft ? 5 : 275), ( parentNode.offset().top - $('#' + canvasId).offset().top ) + parentNode[0].offsetHeight / 2],
                            (isLeft ? new OG.From() : new OG.To()),
                            [5, 5],
                            {"r": 5},
                            shapeId
                        );

                        canvas.setCustomData(shapeElement, {
                            type: isLeft ? 'variable' : 'argument',
                            name: name
                        });

                        edgeIds = $(shapeElement).attr(isLeft ? "_toedge" : "_fromedge");
                        if (edgeIds) {
                            $.each(edgeIds.split(","), function (indx, edgeId) {
                                var edge = canvas.getElementById(edgeId);
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
                if (isLeft && !leftTreeLoding) {
                    leftTreeLoding = true;
                } else if (!isLeft && !rightTreeLoding) {
                    rightTreeLoding = true;
                }
            }
        });

        $('#' + treeDivId + ' .item-fix:last').each(function (idx, item) {
            if (( $(this).offset().top - $('#' + canvasId).offset().top ) > $('#' + canvasId).height()) {
                canvas.setCanvasSize([$('#' + canvasId).width(), ( $(this).offset().top - $('#' + canvasId).offset().top ) + 30]);
            }
        });

        this.leftTreeLoaded = leftTreeLoding;
        this.rightTreeLoaded = rightTreeLoding;
        if (!this.loadDrawed) {
            if (leftTreeLoding && rightTreeLoding) {
                this.drawLine();
            }
        }
    },
    drawUnExtendedTerminal: function (shapeId, isLeft, parentElement) {
        var canvas = this.icanvas;
        var shapeElement = canvas.drawShape(
            [(isLeft ? 5 : 275), ($(parentElement).offset().top - $('#' + this.canvasId).offset().top ) + 4],
            (isLeft ? new OG.From() : new OG.To()),
            [5, 5],
            {"r": 5},
            shapeId
        );

        $(shapeElement).click("destroy");
        canvas.removeAllGuide();
        canvas.hide(shapeElement);
    },
    drawTransformerMappingLine: function (transformerMapping, toShape) {
        var linkedArgumentName = transformerMapping.linkedArgumentName;
        var transformer = transformerMapping.transformer;
        var transformerClassName = transformer.__className;
        var transformerJson = JSON.parse(transformer.transformerJson);
        var argumentSourceMap = transformer.argumentSourceMap;
        var drawData = this.getTransformerDrawDataByClassName(transformerClassName);

        var transformerPanel = this.icanvas.getElementById(transformerJson.id);
        if (!transformerPanel) {
            this.icanvas.drawTransformer([transformerJson.x, transformerJson.y],
                drawData.label, drawData.inputs, drawData.outputs, drawData, transformerJson.id);
            transformerPanel = this.icanvas.getElementById(transformerJson.id);
        }

        //linkedArgument 와 toShape 의 연결
        var fromShape = this.getTerminalFromTransFormByName(transformerPanel, linkedArgumentName);
        if (fromShape) {
            this.icanvas.connect(fromShape, toShape);
        }

        //argumentSourceMap 의 연결
        for (var key in argumentSourceMap) {
            var inputTerminal = this.getTerminalFromTransFormByName(transformerPanel, key);
            var value = argumentSourceMap[key];
            //레프트 트리에 연결되어있다.
            if (typeof value === 'string') {
                var leftTerminal = this.findLeftTerminalByName(value);
                if (leftTerminal) {
                    this.icanvas.connect(leftTerminal, inputTerminal);
                }
            }
            //트랜스포머에 연결되어있다.
            else {
                this.drawTransformerMappingLine(value, inputTerminal);
            }
        }
    },
    drawLine: function () {
        var me = this;
        this.loadDrawed = true;
        var mappingElements = this.object.mappingElements;
        if (typeof mappingElements == 'undefined') {
            return;
        }
        if (mappingElements != null) {
            $.each(mappingElements, function (index, mappingElement) {
                var toShape = me.findRightTerminalByName(mappingElement.argument.text);
                //트랜스포머 매핑
                if (mappingElement.transformerMapping) {
                    me.drawTransformerMappingLine(mappingElement.transformerMapping, toShape);
                }
                //variable 매핑
                if (mappingElement.variable.name) {
                    var fromShape = me.findLeftTerminalByName(mappingElement.variable.name);
                    me.icanvas.connect(fromShape, toShape);
                }
            });
        }
    },
    findRightTerminalByName: function (name) {
        var terminal;
        var shapes = this.icanvas._RENDERER.getAllShapes();
        $.each(shapes, function (index, element) {
            if (!element.data) {
                return;
            }
            if (element.data.type == 'argument') {
                if (element.data.name == name) {
                    terminal = element;
                }
            }
        });
        return terminal;
    },
    findLeftTerminalByName: function (name) {
        var terminal;
        var shapes = this.icanvas._RENDERER.getAllShapes();
        $.each(shapes, function (index, element) {
            if (!element.data) {
                return;
            }
            if (element.data.type == 'variable') {
                if (element.data.name == name) {
                    terminal = element;
                }
            }
        });
        return terminal;
    },
    findParentNode: function (nodeId) {
        var parentNodeId = nodeId.substring(0, nodeId.lastIndexOf("-"));
        var shapeNode = this.icanvas.getElementById(parentNodeId);
        if (shapeNode == null) {
            return this.findParentNode(parentNodeId);
        } else {
            return shapeNode;
        }
    },
    getLinkedArgumentsEdges: function () {
        var argumentsEdges = [];
        var to, toShape, customData;
        var canvas = this.icanvas;
        var edges = canvas._RENDERER.getAllEdges();
        $.each(edges, function (index, edge) {
            to = $(edge).attr("_to");
            toShape = canvas._RENDERER._getShapeFromTerminal(to);
            customData = canvas.getCustomData(toShape);
            if (customData) {
                if (customData['type'] == 'argument') {
                    argumentsEdges.push(edge);
                }
            }
        });
        return argumentsEdges;
    },
    getLinkedElements: function (edge) {
        var canvas = this.icanvas;
        var from = $(edge).attr("_from");
        var to = $(edge).attr("_to");
        var fromShape = canvas._RENDERER._getShapeFromTerminal(from);
        var toShape = canvas._RENDERER._getShapeFromTerminal(to);
        return {
            from: {
                element: fromShape,
                data: canvas.getCustomData(fromShape)
            },
            to: {
                element: toShape,
                data: canvas.getCustomData(toShape)
            }
        }
    },
    getTerminalFromTransFormByName: function (transform, name) {
        var childs = this.icanvas._RENDERER.getChilds(transform);
        var terminal;
        $.each(childs, function (index, child) {
            if (!child.data) {
                return;
            }
            if (child.data.name == name)
                terminal = child;
        });
        return terminal;
    },
    getTransformFromTerminal: function (element) {
        return this.icanvas._RENDERER.getParent(element);
    },
    getTerminalsFromTerminal: function (element) {
        var transform = this.getTransformFromTerminal(element);
        var childs = this.icanvas._RENDERER.getChilds(transform);
        var terminals = [];
        $.each(childs, function (index, child) {
            if (!child.data) {
                return;
            }
            if (!child.data.type) {
                return;
            }
            if (child.data.type != 'input' && child.data.type != 'output') {
                return;
            }
            terminals.push(child)
        });
        return terminals;
    },
    getInputTerminalsFromOutTerminal: function (element) {
        var outputTerminals = [];
        var terminals = this.getTerminalsFromTerminal(element);
        $.each(terminals, function (index, terminal) {
            if (terminal.data.type == 'input') {
                outputTerminals.push(terminal)
            }
        });
        return outputTerminals;
    },
    createTransformerMapping: function (element) {
        var me = this;
        var fromType = element.data.type;
        var processValiable;
        var transformerMapping;
        var transformer;
        var argumentSourceMap = {};

        var linkedArgumentName = element.data.name;
        var className = element.data.className;
        var transform = this.getTransformFromTerminal(element);
        var boundary = this.icanvas._RENDERER.getBoundary(transform);
        var centroid = boundary.getCentroid();

        //동일한 트랜스폼 객체 내부의 input 터미널들마다 argumentSourceMap 의 키밸류를 구성.
        var inputTerminals = this.getInputTerminalsFromOutTerminal(element);

        $.each(inputTerminals, function (index, inputTerminal) {
            var prevEdges = me.icanvas._RENDERER.getPrevEdges(inputTerminal);
            if (prevEdges.length) {
                var prevEdge = prevEdges[0];
                var linkedElements = me.getLinkedElements(prevEdge);
                var linkedFrom = linkedElements['from'];
                var fromType = linkedFrom.data.type;
                var fromName = linkedFrom.data.name;
                //edge 의 from 이 좌측트리요소일경우
                if (fromType == 'variable') {
                    argumentSourceMap[inputTerminal.data.name] = fromName;
                }
                //edge 의 from 이 트랜스폼일경우
                if (fromType == 'output') {
                    argumentSourceMap[inputTerminal.data.name] = me.createTransformerMapping(linkedFrom.element);
                }
            }
        });

        transformer = {
            __className: className,
            name: element.data.label,
            transformerJson: JSON.stringify({
                id: transform.id,
                x: centroid.x,
                y: centroid.y
            }),
            argumentSourceMap: argumentSourceMap
        };
        transformerMapping = {
            __className: 'org.uengine.processdesigner.mapper.TransformerMapping',
            transformer: transformer,
            linkedArgumentName: linkedArgumentName
        };

        return transformerMapping;
    },
    getValue: function () {
        var me = this;
        // startsWith 정의
        if (!String.prototype.startsWith) {
            String.prototype.startsWith = function (str) {
                return !this.indexOf(str);
            };
        }

        if (this.callCount == 1) {
            var returnObject = this.object;
            returnObject.mappingElements = [];
            var argumentsEdges = this.getLinkedArgumentsEdges();
            $.each(argumentsEdges, function (index, argumentsEdge) {
                var linkedElements = me.getLinkedElements(argumentsEdge);
                var linkedFrom = linkedElements['from'];
                var linkedTo = linkedElements['to'];
                var fromType = linkedFrom.data.type;
                var direction = 'in';
                var processValiable;
                var transformerMapping;
                var mappingElement;

                //edge 의 from 이 좌측트리요소일경우
                if (fromType == 'variable') {
                    processValiable = {
                        __className: 'org.uengine.kernel.ProcessVariable',
                        name: linkedFrom.data.name
                    };
                }
                //edge 의 from 이 트랜스폼일경우
                if (fromType == 'output') {
                    transformerMapping = me.createTransformerMapping(linkedFrom.element)
                }

                var argumentText = {
                    __className: 'org.uengine.contexts.TextContext',
                    text: linkedTo.data.name
                };
                if (processValiable) {
                    mappingElement = {
                        __className: 'org.uengine.kernel.ParameterContext',
                        direction: direction,
                        argument: argumentText,
                        variable: processValiable
                    };
                }
                if (transformerMapping) {
                    mappingElement = {
                        __className: 'org.uengine.kernel.ParameterContext',
                        direction: direction,
                        argument: argumentText,
                        transformerMapping: transformerMapping
                    };
                }
                returnObject.mappingElements.push(mappingElement);
            });
            this.object = returnObject;
            this.callCount = -1;
        }
        return this.object;
    },
    getTransformerByName: function () {

    },
    setTransformerByName: function () {

    },
    getTransformerDrawDataByClassName: function (className) {
        var data;
        var drawDatas = this.transformerDrawData();
        $.each(drawDatas, function (index, drawData) {
            if (drawData.className == className) {
                data = drawData;
            }
        });
        return data;
    },
    transformerDrawData: function () {
        return [
            {
                group: 'Math',
                label: 'Max',
                className: 'org.uengine.processdesigner.mapper.transformers.MaxTransformer',
                inputs: ['in1', 'in2'],
                outputs: ['out']
            },
            {
                group: 'Math',
                label: 'Min',
                className: 'org.uengine.processdesigner.mapper.transformers.MinTransformer',
                inputs: ['in1', 'in2'],
                outputs: ['out']
            },
            {
                group: 'Math',
                label: 'To Number',
                className: 'org.uengine.processdesigner.mapper.transformers.NumberTransformer',
                inputs: ['in1'],
                outputs: ['out']
            },
            {
                group: 'Math',
                label: 'Sum',
                className: 'org.uengine.processdesigner.mapper.transformers.SumTransformer',
                inputs: ['in1', 'in2', 'in3', 'in4', 'in5'],
                outputs: ['out']
            },
            {
                group: 'Math',
                label: 'Floor',
                className: 'org.uengine.processdesigner.mapper.transformers.FloorTransformer',
                inputs: ['in1'],
                outputs: ['out']
            },
            {
                group: 'Math',
                label: 'Round',
                className: 'org.uengine.processdesigner.mapper.transformers.RoundTransformer',
                inputs: ['in1'],
                outputs: ['out']
            },
            {
                group: 'Math',
                label: 'Ceil',
                className: 'org.uengine.processdesigner.mapper.transformers.CeilTransformer',
                inputs: ['in1'],
                outputs: ['out']
            },
            {
                group: 'Math',
                label: 'Abs',
                className: 'org.uengine.processdesigner.mapper.transformers.AbsTransformer',
                inputs: ['in1'],
                outputs: ['out']
            },
            {
                group: 'String',
                label: 'Concat',
                className: 'org.uengine.processdesigner.mapper.transformers.ConcatTransformer',
                inputs: ['in1', 'in2', 'in3', 'in4', 'in5'],
                outputs: ['out']
            },
            {
                group: 'String',
                label: 'Replace',
                className: 'org.uengine.processdesigner.mapper.transformers.ReplaceTransformer',
                inputs: ['in1'],
                outputs: ['out']
            },
            {
                group: 'String',
                label: 'NumberFormat',
                className: 'org.uengine.processdesigner.mapper.transformers.NumberFormatTransformer',
                inputs: ['in1', 'in2'],
                outputs: ['out']
            }
        ];
    }
    ,
    setContextMenu: function (canvas) {
        var items = {};
        items['Delete'] = {
            name: 'Delete', callback: function () {
                canvas._HANDLER.deleteSelectedShape();
            }
        };
        var drawData = this.transformerDrawData();
        $.each(drawData, function (index, data) {
            var groupName = data.group;
            var label = data.label;
            var className = data.className;
            var inputs = data.inputs;
            var outputs = data.outputs;
            if (!items[groupName]) {
                items[groupName] = {
                    name: groupName,
                    items: {}
                };
            }
            var group = items[groupName];
            group.items[label] = {
                name: label,
                callback: function () {
                    var root = canvas._RENDERER.getRootGroup();
                    var offset = $(root).data('contextOffset');
                    canvas.drawTransformer([offset.x, offset.y], label, inputs, outputs, data);
                }
            }
        });

        $.contextMenu({
            selector: '#' + canvas._RENDERER.getRootElement().id,
            build: function ($trigger, e) {
                var root = canvas._RENDERER.getRootGroup();
                var shape;
                var newElement;
                var eventOffset = canvas._HANDLER._getOffset(event);
                $(root).data('contextOffset', eventOffset);
                $(canvas._RENDERER.getContainer()).focus();
                return {
                    items: items
                };
            }
        });
    }
};