$(function () {

    /*  Search Icons Function  */
    if ($('input#icon-finder').length) {
        $('input#icon-finder').val('').quicksearch('#glyphicons-list .glyphicon-item,#fontawesome-list .fa-item,#zocial-list .social-btn,#zocial-list .social-btn-small');
    }

});