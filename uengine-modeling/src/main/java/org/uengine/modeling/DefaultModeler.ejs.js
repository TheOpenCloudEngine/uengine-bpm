var org_uengine_modeling_DefaultModeler = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.object = mw3.objects[this.objectId];
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);

//	this.objectDiv.addClass('mw3_layout');
	
	var layoutOption = {west__size: '220', togglerLength_open: 0, spacing_open: 0, spacing_closed: 0};
	if(this.objectDiv.height()) {
		var parentHeight = this.objectDiv.parent().css('height');
		this.objectDiv.height(parentHeight.substring(0, parentHeight.length - 2) * 0.87);
		this.layout = this.objectDiv.layout(layoutOption);
	}

	this.destroy = function(){
		if(this.layout)
			this.layout.destroy();
	}
	
	this.getTracingTag = function(){
		return ++this.object.lastTracingTag;
	}
}

org_uengine_modeling_DefaultModeler.prototype = {
		downloadPdf : function(){
			var data = document.body.getElementsByTagName("svg")[0].cloneNode(true);
			var object = mw3.objects[this.objectId];
			
			//canvas 사이즈가 작아 그림이 끊겨 나오는 문제( 다른 방식을 찾아봐야 할듯 )
			data.setAttribute('height', 768);
			data.setAttribute('width', 1024);
			data = (new XMLSerializer).serializeToString(data);

			//scale이 적용되어 있어 작게 나오는데 이걸 다시 1로 돌리는 일을 하기 위해서( 다른 방식을 찾아봐야 할듯 )
			var tempArray = data.split('transform="scale');
			var strArray = tempArray[1].split('"');
			var scale = strArray[0];	
			var pattern = new RegExp(scale, "i");
			var resultData = data.replace(pattern, '(1)');
			
			object.svgData=resultData;
			object.downloadPdf();
		}
};