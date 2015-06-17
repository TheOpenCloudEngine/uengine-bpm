var org_uengine_modeling_Symbol = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);
	
	this.draggable = function(command){
		this.objectDiv.find('img').draggable({
			appendTo: "body",
			helper: function( event ) {
				return $(this).clone();
			},
			zIndex: 100,
			start: function(event, ui) {
				eval(command);
			},
			drag: function() {
			},
			stop: function() {
			}
		});
	};
	
	this.startLoading = function(){

	};

	this.endLoading = function(){

	};

};