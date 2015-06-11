$(function () {

    /*  Search Icons Function  */
    if ($('input#member-finder').length) {
        $('input#member-finder').val('').quicksearch('.member-entry');
    }

});