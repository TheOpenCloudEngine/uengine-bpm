var org_uengine_kernel_ParameterContext = function(objectId, className){
    // default setting
    this.objectId = objectId;
    this.className = className;
    
    this.clickedIdx = -1;
	var object = mw3.objects[this.objectId];
	if( object && object.metaworksContext && object.metaworksContext.how == 'list'){
		var divId = mw3._getObjectDivId(this.objectId);
		var divObj = $('#' + divId);
		var parameterContextPanel = mw3.getAutowiredObject('org.uengine.kernel.ParameterContextPanel');
		divObj.click({objectId : this.objectId}, function(event){     
		      $('.parameterContext_td').css('background','none');
              $(this).find('.parameterContext_td').css('background','#e5e5e5');
			  parameterContextPanel.selectedContext = object;
	    });
	}
};
