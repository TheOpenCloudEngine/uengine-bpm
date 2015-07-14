var org_uengine_kernel_view_DynamicDrawGeom = function(objectId, className){

	debugger;
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
            canvasObject = mw3.getAutowiredObject('org.uengine.modeling.Canvas');
        }
    }
    this.canvasObjectId = canvasObject.__objectId;
    this.canvasObjectFaceHelper = mw3.getFaceHelper(canvasObject.__objectId);
    this.canvas = this.canvasObjectFaceHelper.canvas;
    this.element;
	
};

org_uengine_kernel_view_DynamicDrawGeom.prototype = {
	loaded: function(){
		debugger;
		var object = mw3.objects[this.objectId];

		var poolview = mw3.getAutowiredObject('org.uengine.kernel.bpmn.view.PoolView');
		var poolElement = document.getElementById(poolview.id);
		if( poolElement ){
			
			var coordinateX = poolElement.shape.geom.boundary._leftCenter.x; // 기준 x
			var coordinateY = poolElement.shape.geom.boundary._leftCenter.y; // 기준 y
			var poolwidth = poolElement.shape.geom.boundary._width; // pool element width
			var poolHeight = poolElement.shape.geom.boundary._height; // pool element height
			// TODO 상위 pool 안쪽에 있는 모든 엑티비티 제거
			var elements = this.canvas._RENDERER.getElementsByBBox(poolElement.shape.geom.getBoundary());
			if( elements && elements.length > 0 ){
				for(var idx =0; idx < elements.length; idx++){
					var ele = elements[idx];
					if( ele.id != poolElement.id){
						this.canvas.removeShape(ele);
					}
				}
			}
			
			// 첫번째로 그려질 엑티비티의 위치정보 계산
			var firstGeomX = coordinateX + 90;
			var firstGeomY = coordinateY;
			
			var tracingTag = 0;
			if (this.canvasObjectFaceHelper.tracingTag) {
				tracingTag = this.canvasObjectFaceHelper.tracingTag;
			}
			var canvasDivObj = $('#canvas_' + this.canvasObjectId);
			if (object && object.activityList) {
				for (var i = 0; i < object.activityList.length; i++) {
					var activity = object.activityList[i];
					var activityView = activity.elementView;
					activityView.element.elementView = null;
					
					if( i != 0 ){
						firstGeomX = firstGeomX*1 + activityView.width*1 + 50 // 50을 더해주는 이유는 간격
					}
					// x, y 좌표값 셋팅
					activityView.x = firstGeomX;
					activityView.y = firstGeomY;
					// 엑티비티에 tracingTag 부여
					activity.tracingTag = ++tracingTag;
					activityView.tracingTag = activity.tracingTag;
					var html = mw3.locateObject(activityView , activityView.__className);
					canvasDivObj.append(html);
					mw3.onLoadFaceHelperScript();
					
					if( i == (object.activityList.length -1) ){
						// 마지막으로 한번더 늘려준다
                        firstGeomX = firstGeomX*1 + activityView.width*1 ;
                    }
				}
				
				if( (firstGeomX - coordinateX) > poolwidth ){
					this.canvas._RENDERER.resize(poolElement, [0,0,0,(firstGeomX - coordinateX) - poolwidth]);
					this.canvas._RENDERER.removeGuide(poolElement);
				}
				if (this.canvasObjectFaceHelper.tracingTag) {
					this.canvasObjectFaceHelper.tracingTag = tracingTag;
				}
			}
		}
		
	}
};