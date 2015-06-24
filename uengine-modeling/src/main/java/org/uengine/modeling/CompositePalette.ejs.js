var org_uengine_modeling_CompositePalette = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.object = mw3.objects[this.objectId];
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);

    //
	//this.objectDiv.css({
	//	height: '2000px',
	//	overflow: 'scroll'
	//});


}