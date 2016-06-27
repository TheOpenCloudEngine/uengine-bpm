var org_uengine_modeling_modeler_palette_ProcessVariablePalette = function (objectId, className) {
    this.objectId = objectId;
    this.className = className;
    this.object = mw3.objects[this.objectId];
    this.objectDivId = mw3._getObjectDivId(this.objectId);
    this.objectDiv = $('#' + this.objectDivId);

    this.objectDiv.css({
        height: '100%',
        overflow: 'auto'
    });

    var container = this.objectDiv.parent();
    var me = this;
    var slider = this.objectDiv;
    slider.dialog({
        title: me.object.name,
        width: 400,
        height: 400,
        dialogClass: "no-close"
    });
    var sliderParent = slider.parent();

    //클로즈버튼 이벤트를 collape,expand 이벤트로..
    var expandBtn = sliderParent.find('.ui-dialog-titlebar-close');
    expandBtn.html('<span class="ui-button-icon-primary ui-icon ui-icon-circle-minus"></span>');
    expandBtn.append();
    expandBtn.unbind('click');
    expandBtn.bind('click', function () {
        //접혀있는 상태라면
        if ($(this).data('collape')) {
            var height = $(this).data('collape');
            sliderParent.height(height);
            slider.show();
            $(this).data('collape', false);
        }
        //접혀있지 않은 상태라면
        else {
            $(this).data('collape', sliderParent.height());
            slider.hide();
            sliderParent.height(40);
        }
    });

    container.on("remove", function () {
        slider.dialog("destroy");
        slider.remove();
    });

    var dialogs = $('.ui-dialog');
    var mainPage = $('div[classname="org.uengine.essencia.portal.Explorer"]');
    if(mainPage.length){
        var top = mainPage.offset().top;
        var left = mainPage.offset().left;
        dialogs.each(function(i){
            var y = top + (i*40);
            var x = left;
            $(this).css('top', y + 'px');
            $(this).css('left', x + 'px');
            var btn = $(this).find('.ui-dialog-titlebar-close');
            var content = $(this).find('.ui-dialog-content');
            if(!btn.data('collape')){
                btn.data('collape', $(this).height());
                content.hide();
                $(this).height(40);
            }
        });
    }
};