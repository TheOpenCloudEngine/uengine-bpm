var org_uengine_modeling_resource_EditorPanelTab = function(objectId, className){

    this.objectId = objectId;
    this.className = className;
    this.object = mw3.objects[objectId];

    this.eventId = mw3.addEventListener("located",

        function(object, data){
            if(object && object.__className && object.__className.indexOf("EditorPanel")){

                $("#objDiv_" + object.__objectId).detach().appendTo("#objDiv"+ data.editorPanelTab.__objectId);

            }
        }, {editorPanelTab: this.object}
    );

}


org_uengine_modeling_resource_EditorPanelTab.prototype.destroy = function(){
    mw3.removeEventListener(this.eventId);
}
