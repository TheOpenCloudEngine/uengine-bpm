$(function () {

    /*  Slider with decimal option  */
    $("#slider3").rangeSlider({
        formatter: function (val) {
            var value = Math.round(val),
                decimal = value - Math.round(val);
            return decimal == 0 ? value.toString() + ".0" : value.toString();
        }
    });

    /*  Slider with min and max values  */
    $("#slider4").rangeSlider({
        range: {
            min: 10,
            max: 40
        }
    });

    /*  Slider with ruler  */
    $("#slider5").rangeSlider({
        scales: [
            // Primary scale
            {
                first: function (val) {
                    return val;
                },
                next: function (val) {
                    return val + 10;
                },
                stop: function (val) {
                    return false;
                },
                label: function (val) {
                    return val;
                },
                format: function (tickContainer, tickStart, tickEnd) {
                    tickContainer.addClass("myCustomClass");
                }
            },
            // Secondary scale
            {
                first: function (val) {
                    return val;
                },
                next: function (val) {
                    if (val % 10 === 9) {
                        return val + 2;
                    }
                    return val + 1;
                },
                stop: function (val) {
                    return false;
                },
                label: function () {
                    return null;
                }
            }
        ]
    });

    /*  Slider with date ruler  */
    var months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"];

    $("#slider6").dateRangeSlider({
        bounds: {
            min: new Date(2012, 0, 1),
            max: new Date(2012, 11, 31, 12, 59, 59)
        },
        defaultValues: {
            min: new Date(2012, 1, 10),
            max: new Date(2012, 7, 22)
        },
        scales: [{
            first: function (value) {
                return value;
            },
            end: function (value) {
                return value;
            },
            next: function (value) {
                var next = new Date(value);
                return new Date(next.setMonth(value.getMonth() + 2));
            },
            label: function (value) {
                return months[value.getMonth()];
            },
            format: function (tickContainer, tickStart, tickEnd) {
                tickContainer.addClass("myCustomClass");
            }
        }]
    });

});