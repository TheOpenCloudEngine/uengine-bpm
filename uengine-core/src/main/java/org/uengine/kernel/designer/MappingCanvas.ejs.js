var org_uengine_kernel_designer_MappingCanvas = function (objectId, className) {
    // default setting
    debugger;
    this.objectId = objectId;
    this.className = className;

    var object = mw3.objects[this.objectId];
    if (object == null || typeof object == 'undefined') {
        this.object = {
            __objectId: this.objectId,
            __className: this.className
        };
    } else {
        this.object = object;
    }
    this.canvasId = 'mapping-' + objectId;


    var mappingTab;
    var aTags = $(document).find('a');
    aTags.each(function () {
        if ($(this).text() === 'Mapping') {
            mappingTab = $(this);
        }
    });

    var me = this;
    if (mappingTab) {
        mappingTab.click(function () {
            setTimeout(function () {
                var tree = new EditorRenderer(me.canvasId);
                tree._CONFIG.CEHCKBOX = true;
                tree._CONFIG.CHANGE_NAME = true;
                tree._CONFIG.CHANGE_OWNER = true;
                tree._CONFIG.MOVE_SORTABLE = true;
                tree._CONFIG.MAPPING_ENABLE = true;
                tree._CONFIG.CREATE_FOLDER = true;
                tree._CONFIG.CREATE_ED = true;
                tree._CONFIG.PICK_ED = true;
                tree._CONFIG.DELETABLE = true;
                tree._CONFIG.AREA.lAc.display = true;
                tree._CONFIG.AREA.lOut.display = true;
                tree._CONFIG.AREA.rIn.display = true;
                tree._CONFIG.AREA.rAc.display = true;
                tree._CONFIG.AREA.rOut.display = false;

                tree.init();

                tree.updateData(tree.convertTreeData(JSON.parse(object.leftTreeJson).node, 'other'), true);
                tree.updateData(tree.convertTreeData(JSON.parse(object.rightTreeJson).node, 'my'), true);
                if (object.mappingElements != null) {
                    var mappingData = tree.convertMappingData(object.mappingElements);
                    tree.updateData(mappingData);
                } else {
                    tree.render();
                }
                me.tree = tree;

                //tree.renderViews();
            }, 100);
        });
    }
};

org_uengine_kernel_designer_MappingCanvas.prototype = {
    getValue: function () {
        var me = this;
        // startsWith 정의
        if (!String.prototype.startsWith) {
            String.prototype.startsWith = function (str) {
                return !this.indexOf(str);
            };
        }

        if (me.tree) {
            var mappingElements = me.tree.convertToJavaMappingData(me.tree.load());
            this.object.mappingElements = mappingElements;
        }
        return this.object;
    }
};