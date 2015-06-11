var messages_list = $('#messages-list');
var message_detail = $('#message-detail');

$(window).bind('enterBreakpoint768', function () {

    $('.page-mailbox .withScroll').each(function () {
        $(this).mCustomScrollbar("destroy");
        $(this).removeClass('withScroll');
        $(this).height('');
    });

    if (messages_list.height() > message_detail.height()) $('#message-detail .panel-body').height(messages_list.height() - 119);
    if (messages_list.height() < message_detail.height()) $('#messages-list .panel-body').height(message_detail.height());

});

$(window).bind('enterBreakpoint1200', function () {
    messages_list.addClass('withScroll');
    message_detail.addClass('withScroll');
    customScroll();
});

$('.message-item').on('click', function () {
    if ($(window).width() < 991) {
        $('.list-messages').fadeOut();
        $('.detail-message').css('padding-left', '15px');
        $('.detail-message').fadeIn();
    }
});

$('#go-back').on('click', function () {
    $('.list-messages').fadeIn();
    $('.detail-message').css('padding-left', '0');
    $('.detail-message').fadeOut();
});


/****  On Resize Functions  ****/
$(window).resize(function () {

    if ($(window).width() > 991) {
        $('.list-messages').show();
        $('.detail-message').css('padding-left', '0');
        $('.detail-message').show();
    }

});