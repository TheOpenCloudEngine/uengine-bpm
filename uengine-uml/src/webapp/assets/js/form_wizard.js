$(function () {

    /****  Inline Form Wizard with Validation  ****/
    $(".form-wizard").steps({
        bodyTag: "section",
        onStepChanging: function (event, currentIndex, newIndex) {
            // Always allow going backward even if the current step contains invalid fields!
            if (currentIndex > newIndex) {
                return true;
            }
            // Forbid suppressing "Warning" step if the user is to young
            if (newIndex === 3 && Number($("#age").val()) < 18) {
                return false;
            }
            var form = $(this);
            // Clean up if user went backward before
            if (currentIndex < newIndex) {
                // To remove error styles
                $(".body:eq(" + newIndex + ") label.error", form).remove();
                $(".body:eq(" + newIndex + ") .error", form).removeClass("error");
            }
            // Disable validation on fields that are disabled or hidden.
            form.validate().settings.ignore = ":disabled,:hidden";
            // Start validation; Prevent going forward if false
            return form.valid();
        },
        onStepChanged: function (event, currentIndex, priorIndex) {
            // Suppress (skip) "Warning" step if the user is old enough.
            if (currentIndex === 2 && Number($("#age").val()) >= 18) {
                $(this).steps("next");
            }
            // Suppress (skip) "Warning" step if the user is old enough and wants to the previous step.
            if (currentIndex === 2 && priorIndex === 3) {
                $(this).steps("previous");
            }
        },
        onFinishing: function (event, currentIndex) {
            var form = $(this);
            // Disable validation on fields that are disabled.
            // At this point it's recommended to do an overall check (mean ignoring only disabled fields)
            form.validate().settings.ignore = ":disabled";

            // Start validation; Prevent form submission if false
            return form.valid();
        },
        onFinished: function (event, currentIndex) {
            var form = $(this);
            // Submit form input
            form.submit();
        }
    }).validate({
        errorPlacement: function (error, element) {
            element.before(error);
        },
        rules: {
            confirm: {
                equalTo: "#password"
            }
        }
    });

    
        
  



    /**** Modal Form Wizard with Validation ****/


    $.fn.wizard.logging = true;
    var wizard = $('#satellite-wizard').wizard({
        keyboard: false,
        contentHeight: 400,
        contentWidth: 700,
        backdrop: 'static'
    });

    $('#fqdn').on('input', function () {
        if ($(this).val().length != 0) {
            $('#ip').val('').attr('disabled', 'disabled');
            $('#fqdn, #ip').parents('.form-group').removeClass('has-error has-success');
        } else {
            $('#ip').val('').removeAttr('disabled');
        }
    });

    var pattern = /\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\b/;
    x = 46;

    $('#ip').on('input', function () {
        if ($(this).val().length != 0) {
            $('#fqdn').val('').attr('disabled', 'disabled');
        } else {
            $('#fqdn').val('').removeAttr('disabled');
        }
    }).keypress(function (e) {
        if (e.which != 8 && e.which != 0 && e.which != x && (e.which < 48 || e.which > 57)) {
            console.log(e.which);
            return false;
        }
    }).keyup(function () {
        var $this = $(this);
        if (!pattern.test($this.val())) {
            //$('#validate_ip').text('Not Valid IP');
            console.log('Not Valid IP');
            $this.parents('.form-group').removeClass('has-error has-success').addClass('has-error');
            while ($this.val().indexOf("..") !== -1) {
                $this.val($this.val().replace('..', '.'));
            }
            x = 46;
        } else {
            x = 0;
            var lastChar = $this.val().substr($this.val().length - 1);
            if (lastChar == '.') {
                $this.val($this.val().slice(0, -1));
            }
            var ip = $this.val().split('.');
            if (ip.length == 4) {
                //$('#validate_ip').text('Valid IP');
                console.log('Valid IP');
                $this.parents('.form-group').removeClass('has-error').addClass('has-success');
            }
        }
    });

    wizard.on('closed', function () {
        wizard.reset();
    });

    wizard.on("reset", function () {
        wizard.modal.find(':input').val('').removeAttr('disabled');
        wizard.modal.find('.form-group').removeClass('has-error').removeClass('has-succes');
        wizard.modal.find('#fqdn').data('is-valid', 0).data('lookup', 0);
    });

    wizard.on("submit", function (wizard) {
        var submit = {
            "hostname": $("#new-server-fqdn").val()
        };

        this.log('seralize()');
        this.log(this.serialize());
        this.log('serializeArray()');
        this.log(this.serializeArray());

        setTimeout(function () {
            wizard.trigger("success");
            wizard.hideButtons();
            wizard._submitting = false;
            wizard.showSubmitCard("success");
            wizard.updateProgressBar(0);
        }, 2000);
    });

    wizard.el.find(".wizard-success .im-done").click(function () {
        wizard.hide();
        setTimeout(function () {
            wizard.reset();
        }, 250);

    });

    wizard.el.find(".wizard-success .create-another-server").click(function () {
        wizard.reset();
    });

    $(".wizard-group-list").click(function () {
        alert("Disabled for demo.");
    });

    $('#open-wizard').click(function (e) {
        e.preventDefault();
        wizard.show();
    });


});

function validateServerLabel(el) {
    var name = el.val();
    var retValue = {};

    if (name == "") {
        retValue.status = false;
        retValue.msg = "Please enter a label";
    } else {
        retValue.status = true;
    }

    return retValue;
};

function validateFQDN(el) {
    var $this = $(el);
    var retValue = {};

    if ($this.is(':disabled')) {
        // FQDN Disabled
        retValue.status = true;
    } else {
        if ($this.data('lookup') === 0) {
            retValue.status = false;
            retValue.msg = "Preform lookup first";
        } else {
            if ($this.data('is-valid') === 0) {
                retValue.status = false;
                retValue.msg = "Lookup Failed";
            } else {
                retValue.status = true;
            }
        }
    }
    return retValue;
};

function lookup() {
    // Normally a ajax call to the server to preform a lookup
    $('#fqdn').data('lookup', 1);
    $('#fqdn').data('is-valid', 1);
    $('#ip').val('127.0.0.1');
};