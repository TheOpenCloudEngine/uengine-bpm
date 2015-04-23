var org_uengine_kernel_ParameterContextPanel = function(objectId, className){
	// default setting
	this.objectId = objectId;
	this.className = className;
	
};

org_uengine_kernel_ParameterContextPanel.prototype = {
		removeItem : function(){
			var object = mw3.objects[this.objectId];
			if( object.selectedContext == null ){
				alert('선택된 변수가 없습니다.');
			}else{
				object.removeActivityVariable();
			}
		}
};