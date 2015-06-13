$(function () {

    /* Show / Hide action buttons on hover */
    var myTimeout;
    $('.comment').mouseenter(function() {
        var comment_footer = $(this).find('.comment-footer');
        myTimeout = setTimeout(function() {
        comment_footer.slideDown();
        }, 200);
    }).mouseleave(function() {
        clearTimeout(myTimeout);
        $(this).find('.comment-footer').slideUp();
    });

    /* Edit a comment */
    $('.edit').on('click', function(e){
      e.preventDefault();
      $('#modal-edit-comment').modal('show');
    });

    /* Delete a comment */
    $('.delete').on('click', function(){
      $(this).closest('.comment').hide();
    });

    /* Checkbox select */
    $('input:checkbox').on('ifClicked', function () {
        if ($(this).parent().hasClass('checked')) {
            $(this).closest('.comment').removeClass('selected');
            $(this).closest('.comment').find(':checkbox').attr('checked', false);
        } else {
            $(this).parent().addClass('checked');
            $(this).closest('.comment').addClass('selected');
            $(this).closest('.comment').find(':checkbox').attr('checked', true);
        }
    });


});