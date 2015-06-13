/* Summernote inline editing functions */
var edit = function () {
    $('.click2edit').summernote({
        focus: true
    });
};

var save = function () {
    var aHTML = $('.click2edit').code(); //save HTML If you need(aHTML: array).
    $('.click2edit').destroy();
};
