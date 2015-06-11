$(function () {

    function sessionTimeout() {

        var $countdown;

        $('body').append('<div class="modal fade" id="session-timeout" tabindex="-1" role="dialog"><div class="modal-dialog"><div class="modal-content"><div class="modal-header bg-blue"><h4 class="modal-title" id="myModalLabel">Your session is about to expire</h4></div><div class="modal-body">The screen will be locked in <span id="idle-timeout-counter" class="w-700"></span> seconds.</p><p>Do you want to stay connected?</p></div><div class="modal-footer"><button id="idle-timeout-dialog-logout" type="button" class="btn btn-default">No, Logout</button><button id="idle-timeout-dialog-keepalive" type="button" class="btn btn-blue" data-dismiss="modal">Yes, Keep Working</button></div></div></div></div>');

        /* Start the idle timer plugin */
        $.idleTimeout('#session-timeout', '.modal-content button:last', {
            idleAfter: 5, // 5 seconds before a dialog appear (very short for demo purpose)
            timeout: 30000, // 30 seconds to timeout
            pollingInterval: 5, // 5 seconds
            keepAliveURL: 'assets/plugins/idle-timeout/keepalive.php',
            serverResponseEquals: 'OK',
            onTimeout: function () {
                window.location = "lockscreen.html";
            },
            onIdle: function () {
                $('#session-timeout').modal('show');
                $countdown = $('#idle-timeout-counter');

                $('#idle-timeout-dialog-keepalive').on('click', function () {
                    $('#session-timeout').modal('hide');
                });

                $('#idle-timeout-dialog-logout').on('click', function () {
                    $('#session-timeout').modal('hide');
                    $.idleTimeout.options.onTimeout.call(this);
                });
            },
            onCountdown: function (counter) {
                /* We update the counter */
                $countdown.html(counter);
            }
        });

    };

    sessionTimeout();

});