
var org_uengine_kernel_view_PoolView = function(objectId, className){
    
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
    this.canvasObjectId = canvasObject.__objectId;
    var canvasObjectFaceHelper = mw3.getFaceHelper(canvasObject.__objectId);
    this.canvas = canvasObjectFaceHelper.icanvas;
    this.element;
};

org_uengine_kernel_view_PoolView.prototype = {
	loaded: function(){
		var object = mw3.objects[this.objectId];
        var objectId = this.objectId;
        var canvas = this.canvas;
        var element = null;
        var initText = "";
        if( !(object.label == null || object.label == 'undefined') ){
            initText = unescape(object.label);
        }else if( object.pool && object.pool.description != null && object.pool.description.text != null ){
            initText = object.pool.description.text;
        }
        var shape = eval('new ' + object.shapeId + '(\''+initText +'\')');
        var id = object.id;
        var style = object.style;
        
        element = canvas.drawShape([
                                         object.x, object.y 
                                         ], 
                                         shape, [parseInt(object.width, 10), parseInt(object.height, 10)] , OG.JSON.decode(unescape(style)), id, null, false);
        
        $(element).attr("_classname", object.activityClass);
        $(element).attr("_viewClass", object.__className);
        $(element).attr("_classType", object.classType);
        
        if( object.pool ){
            $(element).data('pool', object.pool);
            object.pool.poolView = null;
        }else if( typeof $(element).attr("_classname") != 'undefined' &&  typeof $(element).data("pool") == 'undefined' ){
            var activityData = {__className : $(element).attr("_classname")};
            $(element).data('pool', activityData);
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
        
        $(element).unbind('dblclick');
        $(element).on({
            dblclick: function (event) {
                if(event.stopPropagation){
                    event.stopPropagation();
                }
                var divId = 'properties_' + objectId;
                $('body').append("<div id='" + divId + "'></div>");
                var metaworksContext = {
                        __className : 'org.metaworks.MetaworksContext',
                        when : 'edit'
                };
                
                var propertiesWindow = {
                        __className : 'org.uengine.codi.mw3.webProcessDesigner.PropertiesWindow',
                        open : true,
                        width : 860,
                        height : 600,
                        panel : $(this).data('pool'),
                        metaworksContext : metaworksContext
                };
                
                object['propertiesWindow'] = propertiesWindow;
                object.id = $(this).attr('id');
                object.showProperties();
                
            }
        });
	},
    validation : function(message){
	}
};