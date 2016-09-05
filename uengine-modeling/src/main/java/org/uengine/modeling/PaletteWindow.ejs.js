var org_uengine_modeling_PaletteWindow = function (objectId, className) {
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

    var bindExpandEvent = function (dialog) {
        var content = dialog.find('.ui-dialog-content');
        var expandBtn = dialog.find('.ui-dialog-titlebar-close');
        if ($(expandBtn).data('collape')) {
            expandBtn.html('<img src="resources/images/symbol/slider-plus.png">');
        } else {
            expandBtn.html('<img src="resources/images/symbol/slider-minus.png">');
        }
        expandBtn.append();
        expandBtn.unbind('click');
        expandBtn.bind('click', function () {
            //접혀있는 상태라면
            var height;
            if ($(this).data('collape')) {
                height = $(this).data('collape');
                dialog.height(height);
                content.show();
                $(this).data('collape', false);
                expandBtn.html('<img src="resources/images/symbol/slider-minus.png">');
            }
            //접혀있지 않은 상태라면
            else {
                height = dialog.height();
                $(this).data('collape', height);
                content.hide();
                dialog.height(40);
                expandBtn.html('<img src="resources/images/symbol/slider-plus.png">');
            }
            saveAllDialogsPosition();
        });
    };
    var bindRemoveEvent = function (container) {
        container.unbind('remove');
        container.on("remove", function () {
            slider.dialog("destroy");
            slider.remove();
        });
    };

    var bindMoveEvent = function (dialog) {
        var content = dialog.find('.ui-dialog-content');
        content.unbind('dialogdragstop');
        content.on('dialogdragstop', function (event, ui) {
            saveAllDialogsPosition();
        });
    };

    var bindResizeEvent = function (dialog) {
        var content = dialog.find('.ui-dialog-content');
        content.unbind('dialogresizestart');
        content.on('dialogresizestart', function (event, ui) {
            saveAllDialogsPosition();
        });
    };

    var saveAllDialogsPosition = function () {
        var dialogs = $('.ui-dialog');
        dialogs.each(function () {
            var dialog = $(this);
            var height, expand;
            var expandBtn = dialog.find('.ui-dialog-titlebar-close');
            //접혀있는 상태라면
            if (expandBtn.data('collape')) {
                height = expandBtn.data('collape');
                expand = false;
            }
            //접혀있지 않은 상태라면
            else {
                height = dialog.height();
                expand = true;
            }
            var currentPosition = {
                title: dialog.find('.ui-dialog-title').html(),
                height: height,
                width: dialog.width(),
                left: dialog.offset().left,
                top: dialog.offset().top,
                expand: expand
            };
            var position = getStoredDialogPosition(dialog);
            //기존 정보가 없다면 새로 인서트
            if (!position) {
                saveDialogPosition(dialog, currentPosition);
            }
            //기존 정보가 있다면 오버라이드
            else {
                removeStoredDialogPosition(dialog);
                saveDialogPosition(dialog, currentPosition);
            }
        });
    };

    var getPositions = function () {
        if (!localStorage) {
            return null;
        }
        if (!localStorage.getItem('palletPositions')) {
            localStorage.setItem('palletPositions', JSON.stringify([]));
        }
        return JSON.parse(localStorage.getItem('palletPositions'));
    };

    var removeStoredDialogPosition = function (dialog) {
        var title = dialog.find('.ui-dialog-title').html();
        var positions = getPositions();
        if (!positions) {
            return;
        }
        var newPositions = [];
        $.each(positions, function (idx, position) {
            if (position.title !== title) {
                newPositions.push(position);
            }
        });
        localStorage.setItem('palletPositions', JSON.stringify(newPositions));
    };

    var saveDialogPosition = function (dialog, position) {
        var title = dialog.find('.ui-dialog-title').html();
        var positions = getPositions();
        if (!positions) {
            return;
        }
        positions.push(position);
        localStorage.setItem('palletPositions', JSON.stringify(positions));
    };

    var getStoredDialogPosition = function (dialog) {
        var title = dialog.find('.ui-dialog-title').html();
        var storedDialogPosition;
        var positions = getPositions();
        if (!positions) {
            return null;
        }
        $.each(positions, function (idx, position) {
            if (position.title == title) {
                storedDialogPosition = position;
            }
        });
        return storedDialogPosition;
    };

    var setDialogPositionByStorage = function (dialog) {
        var position = getStoredDialogPosition(dialog);
        dialog.css('top', position.top + 'px');
        dialog.css('left', position.left + 'px');
        dialog.width(position.width);
        dialog.height(position.height);

        var btn = dialog.find('.ui-dialog-titlebar-close');
        var content = dialog.find('.ui-dialog-content');
        content.height(position.height-30);
        //확장상태였다면
        if (position.expand) {
            content.show();
            btn.data('collape', false);
        }
        //닫힘상태였다면
        else {
            btn.data('collape', dialog.height());
            content.hide();
            dialog.height(40);
        }
    };

    var setDialogPositionByIndex = function (dialog, index) {
        var mainPage = $('div[classname="org.uengine.essencia.portal.Explorer"]');
        if (!mainPage.length) {
            return;
        }
        var top = mainPage.offset().top;
        var left = mainPage.offset().left;
        var y = top + (index * 40);
        var x = left;
        dialog.css('top', y + 'px');
        dialog.css('left', x + 'px');
        var btn = dialog.find('.ui-dialog-titlebar-close');
        var content = dialog.find('.ui-dialog-content');
        if (!btn.data('collape')) {
            btn.data('collape', dialog.height());
            content.hide();
            dialog.height(40);
        }
    };

    slider.dialog({
        title: me.object.name,
        width: 200,
        height: 300,
        dialogClass: "no-close"
    });

    var dialogs = $('.ui-dialog');
    dialogs.each(function (i) {
        var dialog = $(this);
        var storedDialogPosition = getStoredDialogPosition(dialog);
        if (!storedDialogPosition) {
            setDialogPositionByIndex(dialog, i);
        } else {
            setDialogPositionByStorage(dialog);
        }
        bindExpandEvent(dialog);
        bindMoveEvent(dialog);
        bindResizeEvent(dialog);
    });
    bindRemoveEvent(container);
};