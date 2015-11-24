var org_uengine_modeling_Symbol = function(objectId, className){
	this.objectId = objectId;
	this.className = className;
	this.objectDivId = mw3._getObjectDivId(this.objectId);
	this.objectDiv = $('#' + this.objectDivId);

///	this.objectDiv.css('height', '30px');

	this.draggable = function(command){
		this.objectDiv.find('img').draggable({
			appendTo: "body",
			helper: function( event ) {
				return $(this).clone();
			},
			zIndex: 100,
			start: function(event, ui) {
				eval(command);
				isDroppable = true;
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