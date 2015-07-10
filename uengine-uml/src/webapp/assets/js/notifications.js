$(function () {

    /* We create a click event for notifications, but you can modify easily this event to fit your needs */
    $(".notification").click(function (e) {
        e.preventDefault();

        /**** INFO MESSAGE TYPE ****/
        if ($(this).data("type") == 'info') {
            jNotify(
                $(this).data("message"), {
                    HorizontalPosition: $(this).data("horiz-pos"),
                    VerticalPosition: $(this).data("verti-pos"),
                    ShowOverlay: $(this).data("overlay") ? $(this).data("overlay") : false,
                    TimeShown: $(this).data("timeshown") ? $(this).data("timeshown") : 2000,
                    OpacityOverlay: $(this).data("opacity") ? $(this).data("opacity") : 0.5,
                    MinWidth: $(this).data("min-width") ? $(this).data("min-width") : 250
                });
        }

        /**** SUCCESS MESSAGE TYPE ****/
        else if ($(this).data("type") == 'success') {
            jSuccess(
                $(this).data("message"), {
                    HorizontalPosition: $(this).data("horiz-pos"),
                    VerticalPosition: $(this).data("verti-pos"),
                    ShowOverlay: $(this).data("overlay") ? $(this).data("overlay") : false,
                    TimeShown: $(this).data("timeshown") ? $(this).data("timeshown") : 2000,
                    OpacityOverlay: $(this).data("opacity") ? $(this).data("opacity") : 0.5,
                    MinWidth: $(this).data("min-width") ? $(this).data("min-width") : 250
                });
        }

        /**** ERROR MESSAGE TYPE ****/
        else if ($(this).data("type") == 'error') {
            jError(
                $(this).data("message"), {
                    HorizontalPosition: $(this).data("horiz-pos"),
                    VerticalPosition: $(this).data("verti-pos"),
                    ShowOverlay: $(this).data("overlay") ? $(this).data("overlay") : false,
                    TimeShown: $(this).data("timeshown") ? $(this).data("timeshown") : 2000,
                    OpacityOverlay: $(this).data("opacity") ? $(this).data("opacity") : 0.5,
                    MinWidth: $(this).data("min-width") ? $(this).data("min-width") : 250
                });
        }
    });


    /****  Example with Callback Function  ****/
    $("#notif-callback").click(function (e) {
        e.preventDefault();
        jNotify(
            '<i class="fa fa-info-circle" style="color:#00A2D9;padding-right:8px"></i> You have successfully clicked on the notification button. Congratulation!', {
                HorizontalPosition: 'right',
                VerticalPosition: 'bottom',
                ShowOverlay: false,
                TimeShown: 3000,
                onClosed: function () {
                    alert('I am a function called when notif is closed !')
                }
            });
    });

});