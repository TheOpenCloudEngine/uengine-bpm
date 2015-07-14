if ($('body').data('page') == 'widgets2'){

    function randomValue() {
        return (Math.floor(Math.random() * (1 + 24))) + 8;
    }

    var data2 = [[1, randomValue()],[2, randomValue()],[3, 2 + randomValue()],[4, 5 + randomValue()],[5, 7 + randomValue()],[6, 10 + randomValue()],[7, 15 + randomValue()],[8, 28 + randomValue()],[9, 30 + randomValue()],[10, 38 + randomValue()],[11, 40 + randomValue()],[12, 50 + randomValue()],[13, 52 + randomValue()],[14, 60 + randomValue()],[15, 62 + randomValue()],[16, 65 + randomValue()],[17, 70 + randomValue()],[18, 80 + randomValue()],[19, 85 + randomValue()],[20, 90 + randomValue()]];

    var plot = $.plot($("#products-example"), [{
        data: data2, showLabels: true, label: "Product 1", labelPlacement: "below", canvasRender: true, cColor: "#FFFFFF"
    }], {
        series: {
            lines: {show: true, fill: true, fill: 1},
            fillColor: "rgba(0, 0, 0 , 1)",
            points: {show: true}
        },
        legend: {show: false},
        grid: {show: false, hoverable: true},
        colors: ["#00A2D9"]
    });

}



//************************* WEATHER WIDGET *************************//
/* We initiate widget with a city (can be changed) */
var city = 'Miami';
$.simpleWeather({
    location: city,
    woeid: '',
    unit: 'f',
    success: function (weather) {
        city = weather.city;
        region = weather.country;
        tomorrow_date = weather.tomorrow.date;
        weather_icon = '<i class="icon-' + weather.code + '"></i>';
        $(".weather-city").html(city);
        $(".weather-currently").html(weather.currently);
        $(".today-img").html('<i class="big-img-weather icon-' + weather.code + '"></i>');
        $(".today-temp").html(weather.low + '° / ' + weather.high + '°');
        $(".weather-region").html(region);
        $(".weather-day").html(tomorrow_date);
        $(".weather-icon").html(weather_icon);
        $(".1-days-day").html(weather.forecasts.one.day);
        $(".1-days-image").html('<i class="icon-' + weather.forecasts.one.code + '"></i>');
        $(".1-days-temp").html(weather.forecasts.one.low + '° / ' + weather.forecasts.one.high + '°');
        $(".2-days-day").html(weather.forecasts.two.day);
        $(".2-days-image").html('<i class="icon-' + weather.forecasts.two.code + '"></i>');
        $(".2-days-temp").html(weather.forecasts.two.low + '° / ' + weather.forecasts.two.high + '°');
        $(".3-days-day").html(weather.forecasts.three.day);
        $(".3-days-image").html('<i class="icon-' + weather.forecasts.three.code + '"></i>');
        $(".3-days-temp").html(weather.forecasts.three.low + '° / ' + weather.forecasts.three.high + '°');
        $(".4-days-day").html(weather.forecasts.four.day);
        $(".4-days-image").html('<i class="icon-' + weather.forecasts.four.code + '"></i>');
        $(".4-days-temp").html(weather.forecasts.four.low + '° / ' + weather.forecasts.four.high + '°');
    }
});

/* We get city from input on change */
$("#city-form").change(function () {
    city = document.getElementById("city-form").value;
    $.simpleWeather({
        location: city,
        woeid: '',
        unit: 'f',
        success: function (weather) {
            city = weather.city;
            region = weather.country;
            tomorrow_date = weather.tomorrow.date;
            weather_icon = '<i class="icon-' + weather.code + '"></i>';
            $(".weather-city").html(city);
            $(".weather-currently").html(weather.currently);
            $(".today-img").html('<i class="big-img-weather icon-' + weather.code + '"></i>');
            $(".today-temp").html(weather.low + '° / ' + weather.high + '°');
            $(".weather-region").html(region);
            $(".weather-day").html(tomorrow_date);
            $(".weather-icon").html(weather_icon);
            $(".1-days-day").html(weather.forecasts.one.day);
            $(".1-days-image").html('<i class="icon-' + weather.forecasts.one.code + '"></i>');
            $(".1-days-temp").html(weather.forecasts.one.low + '° / ' + weather.forecasts.one.high + '°');
            $(".2-days-day").html(weather.forecasts.two.day);
            $(".2-days-image").html('<i class="icon-' + weather.forecasts.two.code + '"></i>');
            $(".2-days-temp").html(weather.forecasts.two.low + '° / ' + weather.forecasts.two.high + '°');
            $(".3-days-day").html(weather.forecasts.three.day);
            $(".3-days-image").html('<i class="icon-' + weather.forecasts.three.code + '"></i>');
            $(".3-days-temp").html(weather.forecasts.three.low + '° / ' + weather.forecasts.three.high + '°');
            $(".4-days-day").html(weather.forecasts.four.day);
            $(".4-days-image").html('<i class="icon-' + weather.forecasts.four.code + '"></i>');
            $(".4-days-temp").html(weather.forecasts.four.low + '° / ' + weather.forecasts.four.high + '°');
        }
    });
});


//************************* SALE PRODUCT 1 CHART *************************//
function randomValue() {
    return (Math.floor(Math.random() * (1 + 24))) + 8;
}

var data1 = [
    [1, 5 + randomValue()],
    [2, 10 + randomValue()],
    [3, 10 + randomValue()],
    [4, 15 + randomValue()],
    [5, 20 + randomValue()],
    [6, 25 + randomValue()],
    [7, 30 + randomValue()],
    [8, 35 + randomValue()],
    [9, 40 + randomValue()],
    [10, 45 + randomValue()],
    [11, 50 + randomValue()],
    [12, 55 + randomValue()],
    [13, 60 + randomValue()],
    [14, 70 + randomValue()],
    [15, 75 + randomValue()],
    [16, 80 + randomValue()],
    [17, 85 + randomValue()],
    [18, 90 + randomValue()],
    [19, 95 + randomValue()],
    [20, 100 + randomValue()]
];
var data2 = [
    [1, randomValue()],
    [2, randomValue()],
    [3, 2 + randomValue()],
    [4, 5 + randomValue()],
    [5, 7 + randomValue()],
    [6, 10 + randomValue()],
    [7, 15 + randomValue()],
    [8, 28 + randomValue()],
    [9, 30 + randomValue()],
    [10, 38 + randomValue()],
    [11, 40 + randomValue()],
    [12, 50 + randomValue()],
    [13, 52 + randomValue()],
    [14, 60 + randomValue()],
    [15, 62 + randomValue()],
    [16, 65 + randomValue()],
    [17, 70 + randomValue()],
    [18, 80 + randomValue()],
    [19, 85 + randomValue()],
    [20, 90 + randomValue()]
];

var plot = $.plot($("#chart_1"), [{
    data: data1,
    showLabels: true,
    label: "Product 1",
    labelPlacement: "below",
    canvasRender: true,
    cColor: "#FFFFFF"
}], {
    series: {
        lines: {
            show: true,
            fill: true,
            fill: 1
        },
        fillColor: "rgba(0, 0, 0 , 1)",
        points: {
            show: true
        }
    },
    legend: {
        show: false
    },
    grid: {
        show: false,
        hoverable: true
    },
    colors: ["#00A2D9"]
});


//************************* SALE PRODUCT 2 CHART *************************//
var plot = $.plot($("#chart_2"), [{
    data: data2,
    showLabels: true,
    label: "Product 2",
    labelPlacement: "below",
    color: '#C75757',
    canvasRender: true,
    cColor: "#FFFFFF"
}], {
    bars: {
        show: true,
        fill: true,
        lineWidth: 1,
        lineColor: '#121212',

        order: 1,
        fill: 0.8
    },
    legend: {
        show: false
    },
    grid: {
        show: false,
        hoverable: true
    }
});


//************************* SALE PRODUCT 1 & 2 CHART *************************//
var chartColor = $(this).parent().parent().css("color");
var plot = $.plot($("#chart_3"), [{
    data: data1,
    label: "Visits",
    lines: {
        show: true,
        fill: true,
        fillColor: "rgba(0, 162, 217, 0.1)",
        lineWidth: 3
    },
    points: {
        show: true,
        lineWidth: 3,
        fill: true
    },
    shadowSize: 0
}, {
    data: data2,
    bars: {
        show: true,
        fill: false,
        barWidth: 0.1,
        align: "center",
        lineWidth: 8
    }
}], {
    grid: {
        show: false,
        hoverable: true,
        clickable: true,
        tickColor: "#eee",
        borderWidth: 0
    },
    legend: {
        show: false
    },
    colors: ['#00A2D9', '#C75757'],
    xaxis: {
        ticks: 5,
        tickDecimals: 0
    },
    yaxis: {
        ticks: 5,
        tickDecimals: 0
    },
});

function showTooltip(x, y, contents) {
    $('<div id="flot-tooltip">' + contents + '</div>').css({
        position: 'absolute',
        display: 'none',
        top: y + 5,
        left: x + 5,
        color: '#fff',
        padding: '2px 5px',
        'background-color': '#717171',
        opacity: 0.80
    }).appendTo("body").fadeIn(200);
}

var previousPoint = null;
$("#chart_1, #chart_2, #chart_3").bind("plothover", function (event, pos, item) {
    $("#x").text(pos.x.toFixed(2));
    $("#y").text(pos.y.toFixed(2));
    if (item) {
        if (previousPoint != item.dataIndex) {
            previousPoint = item.dataIndex;
            $("#flot-tooltip").remove();
            var x = item.datapoint[0].toFixed(2),
                y = item.datapoint[1].toFixed(2);
            showTooltip(item.pageX, item.pageY, y + "0 $");
        }
    } else {
        $("#flot-tooltip").remove();
        previousPoint = null;
    }
});


//********************** LINE & BAR SWITCH CHART *********************//
var d1 = [
    [0, 950],
    [1, 1300],
    [2, 1600],
    [3, 1900],
    [4, 2100],
    [5, 2500],
    [6, 2200],
    [7, 2000],
    [8, 1950],
    [9, 1900],
    [10, 2000],
    [11, 2120]
];
var d2 = [
    [0, 450],
    [1, 500],
    [2, 600],
    [3, 550],
    [4, 600],
    [5, 800],
    [6, 900],
    [7, 800],
    [8, 850],
    [9, 830],
    [10, 1000],
    [11, 1150]
];

var tickArray = ['Janv', 'Fev', 'Mars', 'Apri', 'May', 'June', 'July', 'Augu', 'Sept', 'Nov'];

var graph_lines = [{
    label: "Line 1",
    data: d1,
    lines: {
        lineWidth: 2
    },
    shadowSize: 0,
    color: '#3598DB'
}, {
    label: "Line 1",
    data: d1,
    points: {
        show: true,
        fill: true,
        radius: 6,
        fillColor: "#3598DB",
        lineWidth: 3
    },
    color: '#fff'
}, {
    label: "Line 2",
    data: d2,
    animator: {
        steps: 300,
        duration: 1000,
        start: 0
    },
    lines: {
        fill: 0.4,
        lineWidth: 0,
    },
    color: '#18a689'
}, {
    label: "Line 2",
    data: d2,
    points: {
        show: true,
        fill: true,
        radius: 6,
        fillColor: "#99dbbb",
        lineWidth: 3
    },
    color: '#fff'
}, ];
var graph_bars = [{
    // Visits
    data: d1,
    color: '#5CB85C'
}, {
    // Returning Visits
    data: d2,
    color: '#00A2D9',
    points: {
        radius: 4,
        fillColor: '#00A2D9'
    }
}];

/** Line Chart **/
var line_chart = $.plotAnimator($('#graph-lines'), graph_lines, {
    xaxis: {
        tickLength: 0,
        tickDecimals: 0,
        min: 0,
        ticks: [
            [0, 'Jan'],
            [1, 'Fev'],
            [2, 'Mar'],
            [3, 'Apr'],
            [4, 'May'],
            [5, 'Jun'],
            [6, 'Jul'],
            [7, 'Aug'],
            [8, 'Sept'],
            [9, 'Oct'],
            [10, 'Nov'],
            [11, 'Dec']
        ],
        font: {
            lineHeight: 12,
            weight: "bold",
            family: "Open sans",
            color: "#8D8D8D"
        }
    },
    yaxis: {
        ticks: 3,
        tickDecimals: 0,
        tickColor: "#f3f3f3",
        font: {
            lineHeight: 13,
            weight: "bold",
            family: "Open sans",
            color: "#8D8D8D"
        }
    },
    grid: {
        backgroundColor: {
            colors: ["#fff", "#fff"]
        },
        borderColor: "transparent",
        borderWidth: 20,
        margin: 0,
        minBorderMargin: 10,
        labelMargin: 15,
        hoverable: true,
        clickable: true,
        mouseActiveRadius: 4
    },
    legend: {
        show: false
    }
});

$("#graph-lines").on("animatorComplete", function () {
    $("#lines, #bars").removeAttr("disabled");
});

$("#lines").on("click", function (e) {
    e.preventDefault();
    $('#bars').removeClass('active');
    $('#graph-bars').fadeOut();
    $(this).addClass('active');
    $("#lines, #bars").attr("disabled", "disabled");
    $('#graph-lines').fadeIn();
    line_chart = $.plotAnimator($('#graph-lines'),
        graph_lines, {
            xaxis: {
                tickLength: 0,
                tickDecimals: 0,
                min: 0,
                ticks: [
                    [0, 'Jan'],
                    [1, 'Fev'],
                    [2, 'Mar'],
                    [3, 'Apr'],
                    [4, 'May'],
                    [5, 'Jun'],
                    [6, 'Jul'],
                    [7, 'Aug'],
                    [8, 'Sept'],
                    [9, 'Oct'],
                    [10, 'Nov'],
                    [11, 'Dec']
                ],
                font: {
                    lineHeight: 12,
                    weight: "bold",
                    family: "Open sans",
                    color: "#8D8D8D"
                }
            },
            yaxis: {
                ticks: 3,
                tickDecimals: 0,
                tickColor: "#f3f3f3",
                font: {
                    lineHeight: 13,
                    weight: "bold",
                    family: "Open sans",
                    color: "#8D8D8D"
                }
            },
            grid: {
                backgroundColor: {
                    colors: ["#fff", "#fff"]
                },
                borderColor: "transparent",
                borderWidth: 20,
                margin: 0,
                minBorderMargin: 0,
                labelMargin: 15,
                hoverable: true,
                clickable: true,
                mouseActiveRadius: 4
            },
            legend: {
                show: false
            }
        });
});

$("#graph-bars").on("animatorComplete", function () {
    $("#bars, #lines").removeAttr("disabled")
});

$("#bars").on("click", function (e) {
    e.preventDefault();
    $("#bars, #lines").attr("disabled", "disabled");
    $('#lines').removeClass('active');
    $('#graph-lines').fadeOut();
    $(this).addClass('active');
    $('#graph-bars').fadeIn().removeClass('hidden');
    bar_chart = $.plotAnimator($('#graph-bars'), graph_bars, {
        series: {
            bars: {
                show: true,
                barWidth: .9,
                align: 'center'
            },
            shadowSize: 0
        },
        xaxis: {
            tickColor: 'transparent',
            ticks: [
                [0, 'Jan'],
                [1, 'Fev'],
                [2, 'Mar'],
                [3, 'Apr'],
                [4, 'May'],
                [5, 'Jun'],
                [6, 'Jul'],
                [7, 'Aug'],
                [8, 'Sept'],
                [9, 'Oct'],
                [10, 'Nov'],
                [11, 'Dec']
            ],
            font: {
                lineHeight: 12,
                weight: "bold",
                family: "Open sans",
                color: "#9a9a9a"
            }
        },
        yaxis: {
            ticks: 3,
            tickDecimals: 0,
            tickColor: "#f3f3f3",
            font: {
                lineHeight: 13,
                weight: "bold",
                family: "Open sans",
                color: "#9a9a9a"
            }
        },
        grid: {
            backgroundColor: {
                colors: ["#fff", "#fff"]
            },
            borderColor: "transparent",
            margin: 0,
            minBorderMargin: 0,
            labelMargin: 15,
            hoverable: true,
            clickable: true,
            mouseActiveRadius: 4
        },
        legend: {
            show: false
        }
    });
});

$('#graph-bars').hide();

function showTooltip(x, y, contents) {
    $('<div id="flot-tooltip">' + contents + '</div>').css({
        position: 'absolute',
        display: 'none',
        top: y + 5,
        left: x + 5,
        color: '#fff',
        padding: '2px 5px',
        'background-color': '#717171',
        opacity: 0.80
    }).appendTo("body").fadeIn(200);
};

$("#graph-lines, #graph-bars").bind("plothover", function (event, pos, item) {
    $("#x").text(pos.x.toFixed(0));
    $("#y").text(pos.y.toFixed(0));
    if (item) {
        if (previousPoint != item.dataIndex) {
            previousPoint = item.dataIndex;
            $("#flot-tooltip").remove();
            var x = item.datapoint[0].toFixed(0),
                y = item.datapoint[1].toFixed(0);
            showTooltip(item.pageX, item.pageY, y + " visitors");
        }
    } else {
        $("#flot-tooltip").remove();
        previousPoint = null;
    }
});


//********************** REALTIME DATA CHART *********************//
var seriesData = [
    [],
    []
];
var random = new Rickshaw.Fixtures.RandomData(60);

for (var i = 0; i < 60; i++) {
    random.addData(seriesData);
}

var graph = new Rickshaw.Graph({
    element: document.getElementById("chart"),
    height: 310,
    renderer: 'area',
    series: [{
        color: '#C75757',
        data: seriesData[0],
        name: 'Server Load'
    }, {
        color: '#00A2D9',
        data: seriesData[1],
        name: 'CPU'
    }]
});

graph.render();

var hoverDetail = new Rickshaw.Graph.HoverDetail({
    graph: graph,
    xFormatter: function (x) {
        return new Date(x * 1000).toString();
    }
});

var legend = new Rickshaw.Graph.Legend({
    graph: graph,
    element: document.getElementById('legend')
});

var shelving = new Rickshaw.Graph.Behavior.Series.Toggle({
    graph: graph,
    legend: legend
});

var controls = new RenderControls({
    element: document.querySelector('form'),
    graph: graph
});

setInterval(function () {
    random.removeData(seriesData);
    random.addData(seriesData);
    graph.update();
}, 1000);

setTimeout(1000);