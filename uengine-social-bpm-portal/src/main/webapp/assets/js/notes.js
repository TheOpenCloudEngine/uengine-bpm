
$(function (){

    var notes = notes || {};

    /* Display current datetime and hours */
    function CurrentDate(container){
        var monthNames = [ "January", "February", "March", "April", "May", "June","July", "August", "September", "October", "November", "December" ];
        var dayNames= ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];
        var date = new Date();
        date.setDate(date.getDate() + 1);     
        var day = date.getDate();
        var month = date.getMonth();
        var hours = date.getHours();
        var minutes = date.getMinutes();
        var ampm = hours >= 12 ? 'pm' : 'am';
        hours = hours % 12;
        hours = hours ? hours : 12; // the hour '0' should be '12'
        minutes = minutes < 10 ? '0'+minutes : minutes;
        var strTime = dayNames[date.getDay()] + " " + date.getDate() + ' ' + monthNames[date.getMonth()] + ', ' + hours + ':' + minutes + ' ' + ampm;
        $(container).text(strTime);
    }

    CurrentDate('#currentDate');

    /*  Search Notes Function  */
    if ($('input#notes-finder').length) {
        $('input#notes-finder').val('').quicksearch('.note-item');
    }

    notes.$container = $(".page-notes");
    $.extend(notes, {
        noTitleText: "No title",
        noDescriptionText: "(No content)",
        $currentNote: $(null),
        $currentNoteTitle: $(null),
        $currentNoteDescription: $(null),
        addNote: function () {
            var $note = $('<div class="note-item media current fade in"><button class="close" data-dismiss="alert">×</button><div><div><h4 class="note-name">Untitled</h4></div><p class="note-desc">No content</p><p><small class="pull-right note-date"></small></p></div></div>');
            notes.$notesList.prepend($note);
            notes.$notesList.find('.note-item').removeClass('current');
            $note.addClass('current');
            notes.$writeNote.focus();
            notes.checkCurrentNote();
            CurrentDate('.note-date'); 
            customScroll();
        },
        checkCurrentNote: function () {
            var $current_note = notes.$notesList.find('div.current').first();
            
            if ($current_note.length) {
                notes.$currentNote = $current_note;
                notes.$currentNoteTitle = $current_note.find('.note-name');
                notes.$currentNoteDescription = $current_note.find('.note-desc');
                var $space = notes.$currentNoteTitle.text().indexOf( "\r" );
                $note_title = notes.$currentNoteTitle.html();
                /* If there are no breaklines, we add one */
                if($space == -1) {
                    $note_title = notes.$currentNoteTitle.append('&#13;').html();
                }
                var completeNote = $note_title + $.trim(notes.$currentNoteDescription.html());
                $space = $note_title.indexOf( "\r" );
                notes.$writeNote.val(completeNote).trigger('autosize.resize');

            } else {
                var first = notes.$notesList.find('div:first:not(.no-notes)');
                if (first.length) {
                    first.addClass('current');
                    notes.checkCurrentNote();
                } else {
                    notes.$writeNote.val('');
                    notes.$currentNote = $(null);
                    notes.$currentNoteTitle = $(null);
                    notes.$currentNoteDescription = $(null);
                }
            }
        },
        updateCurrentNoteText: function () {
            var text = $.trim(notes.$writeNote.val());
            if (notes.$currentNote.length) {
                var title = '',
                    description = '';
                if (text.length) {
                    var _text = text.split("\n"),
                        currline = 1;
                    for (var i = 0; i < _text.length; i++) {
                        if (_text[i]) {
                            if (currline == 1) {
                                title = _text[i];
                            } else
                            if (currline == 2) {
                                description = _text[i];
                            }
                            currline++;
                        }
                        if (currline > 2)
                            break;
                    }
                }
                notes.$currentNoteTitle.text(title.length ? title : notes.noTitleText);
                notes.$currentNoteDescription.text(description.length ? description : notes.noDescriptionText);
                
            } else
            if (text.length) {
                notes.addNote();
            }
        }
    });
    if (notes.$container.length > 0) {
        notes.$notesList = notes.$container.find('#notes-list');
        notes.$txtContainer = notes.$container.find('.note-write');
        notes.$writeNote = notes.$txtContainer.find('textarea');
        notes.$addNote = notes.$container.find('#add-note');
        notes.$addNote.on('click', function (ev) {
            notes.addNote();
            notes.$writeNote.val('');
        });
        notes.$writeNote.on('keyup', function (ev) {
            notes.updateCurrentNoteText();
        });
        notes.checkCurrentNote();
        notes.$notesList.on('click', '.note-item', function (ev) {
            ev.preventDefault();
            notes.$notesList.find('.note-item').removeClass('current');
            $(this).addClass('current');
            notes.checkCurrentNote();
        });
    }

    var messages_list = $('.list-notes');
    var message_detail = $('.detail-note');

    noteTextarea();

    $(window).bind('enterBreakpoint768', function () {
        $('.page-notes .withScroll').each(function () {
            $(this).mCustomScrollbar("destroy");
            $(this).removeClass('withScroll');
            $(this).height('');
        });

        if (messages_list.height() > message_detail.height()) $('.detail-note  .panel-body').height(messages_list.height() - 119);
        if (messages_list.height() < message_detail.height()) $('.list-notes .panel-body').height(message_detail.height() - 2);

    });

    $(window).bind('enterBreakpoint1200', function () {
        messages_list.addClass('withScroll');
        message_detail.addClass('withScroll');
        customScroll();
    });

    /* Show / hide note if screen size < 991 px */
    $('#notes-list').on('click', '.note-item', function () {
        if ($(window).width() < 991) {
            $('.list-notes').fadeOut();
            $('.detail-note').css('padding-left', '15px');
            $('.detail-note').fadeIn();
        }
    });

    $('#go-back').on('click', function () {
        $('.list-notes').fadeIn();
        $('.detail-note').css('padding-left', '0');
        $('.detail-note').fadeOut();
    });


    });

function noteTextarea(){
    $('.note-write textarea').height($(window).height() - 144);
}

/****  On Resize Functions  ****/
$(window).resize(function () {

    if ($(window).width() > 991) {
        noteTextarea();
        $('.list-notes').show();
        $('.detail-note').css('padding-left', '0');
        $('.detail-note').show();
    }

});