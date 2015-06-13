var crop_1;

$(function () {
    cropImage();
});

$(window).resize(function () {
    img_width = $('.jcrop-holder img').parent().parent().parent().width() - 40;
    $('.jcrop-holder img').width(img_width);
    $('.jcrop-holder img').height('auto');
});

$('#chat-toggle').on('click', function () {
 
    $('#main-content .col-md-6').fadeOut().fadeIn();

    setTimeout(function () {
        if($('#menu-right').hasClass('mm-opened')){
            img_width = $('.jcrop-holder img').parent().parent().parent().width() - 40;
            $('.jcrop-holder img').width(img_width);
            $('.jcrop-holder img').height('auto');
            $('#preview-pane').css('margin-right', '135px'); 
        }
        else{
            img_width = $('.jcrop-holder img').parent().parent().parent().width() - 40;
            $('.jcrop-holder img').width(img_width);
            $('.jcrop-holder img').height('auto');
            $('#preview-pane').css('margin-right', '10px'); 
        }
    }, 500); 
});

function cropImage() {
    // Create variables (in this scope) to hold the API and image size
    var boundx,
        boundy,
        api;

    // Grab some information about the preview pane
    $preview = $('#preview-pane'),
    $pcnt = $('#preview-pane .preview-container'),
    $pimg = $('#preview-pane .preview-container img'),

    xsize = $pcnt.width(),
    ysize = $pcnt.height();

    console.log('init', [xsize, ysize]);

    $('#image_crop1').Jcrop({
        onChange: updatePreview,
        onSelect: updatePreview,
        aspectRatio: xsize / ysize
    }, function () {
        // Use the API to get the real image size
        var bounds = this.getBounds();
        boundx = bounds[0];
        boundy = bounds[1];
        // Store the API in the jcrop_api variable
        crop_1 = this;

        // Move the preview into the jcrop container for css positioning
        $preview.appendTo(crop_1.ui.holder);
    });

    function updatePreview(c) {
        if (parseInt(c.w) > 0) {
            var rx = xsize / c.w;
            var ry = ysize / c.h;

            $pimg.css({
                width: Math.round(rx * boundx) + 'px',
                height: Math.round(ry * boundy) + 'px',
                marginLeft: '-' + Math.round(rx * c.x) + 'px',
                marginTop: '-' + Math.round(ry * c.y) + 'px'
            });
        }
    };


    $('#image_crop2').Jcrop({
        // start off with jcrop-light class
        bgOpacity: 0.5,
        bgColor: 'transparent',
        addClass: 'jcrop-light'
    }, function () {
        crop_2 = this;
        crop_2.setSelect([130, 65, 130 + 350, 65 + 285]);
        crop_2.setOptions({
            bgFade: true
        });
        crop_2.ui.selection.addClass('jcrop-selection');
    });


    /* Change Background Opacity on Button click */
    $('#buttonbar').on('click', 'button', function (e) {
        var $t = $(this),
            $g = $t.closest('.btn-group');
        $g.find('button.active').removeClass('active');
        $t.addClass('active');
        $g.find('[data-setclass]').each(function () {
            var $th = $(this),
                c = $th.data('setclass'),
                a = $th.hasClass('active');
            if (a) {
                crop_2.ui.holder.addClass(c);
                switch (c) {

                case 'jcrop-light':
                    crop_2.setOptions({
                        bgColor: 'white',
                        bgOpacity: 0.5
                    });
                    break;

                case 'jcrop-dark':
                    crop_2.setOptions({
                        bgColor: 'black',
                        bgOpacity: 0.4
                    });
                    break;

                case 'jcrop-normal':
                    crop_2.setOptions({
                        bgColor: $.Jcrop.defaults.bgColor,
                        bgOpacity: $.Jcrop.defaults.bgOpacity
                    });
                    break;
                }
            } else crop_2.ui.holder.removeClass(c);
        });
    });

}