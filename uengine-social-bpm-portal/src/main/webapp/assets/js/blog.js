$(function () {

/*------------------------------------------------------------------------------------*/
/*------------------------------  BLOG DASHBOARD --------------------------------*/

    if($('body').data('page') == 'blog'){

        //jvectormap data
        var visitorsData = {
            "US": 398, //USA
            "SA": 400, //Saudi Arabia
            "CA": 1000, //Canada
            "DE": 500, //Germany
            "FR": 760, //France
            "CN": 300, //China
            "AU": 700, //Australia
            "BR": 600, //Brazil
            "IN": 800, //India
            "GB": 320, //Great Britain
            "RU": 3000 //Russia
        };
        //World map by jvectormap
        $('#world-map').vectorMap({
            map: 'world_mill_en',
            backgroundColor: "#fff",
            regionStyle: {
                initial: {
                    fill: '#e4e4e4',
                    "fill-opacity": 1,
                    stroke: 'none',
                    "stroke-width": 0,
                    "stroke-opacity": 1
                }
            },
            series: {
                regions: [{
                        values: visitorsData,
                        scale: ["#3c8dbc", "#2D79A6"], //['#3E5E6B', '#A6BAC2'],
                        normalizeFunction: 'polynomial'
                    }]
            },
            onRegionLabelShow: function(e, el, code) {
                if (typeof visitorsData[code] != "undefined")
                    el.html(el.html() + ': ' + visitorsData[code] + ' new visitors');
            }
        });



        //Sparkline charts
        var myvalues = [15, 19, 20, -22, -33, 27, 31, 27, 19, 30, 21];
        $('#sparkline-1').sparkline(myvalues, {
            type: 'bar',
            barColor: '#18A689',
            negBarColor: "#cd6a6a",
            height: '20px'
        });
        myvalues = [15, 19, 20, 22, -2, -10, -7, 27, 19, 30, 21];
        $('#sparkline-2').sparkline(myvalues, {
            type: 'bar',
            barColor: '#18A689',
            negBarColor: "#cd6a6a",
            height: '20px'
        });
        myvalues = [15, -19, -20, 22, 33, 27, 31, 27, 19, 30, 21];
        $('#sparkline-3').sparkline(myvalues, {
            type: 'bar',
            barColor: '#18A689',
            negBarColor: "#cd6a6a",
            height: '20px'
        });
        myvalues = [15, 19, 20, 22, 33, -27, -31, 27, 19, 30, 21];
        $('#sparkline-4').sparkline(myvalues, {
            type: 'bar',
            barColor: '#18A689',
            negBarColor: "#cd6a6a",
            height: '20px'
        });
        myvalues = [15, 19, 20, 22, 33, 27, 31, -27, -19, 30, 21];
        $('#sparkline-5').sparkline(myvalues, {
            type: 'bar',
            barColor: '#18A689',
            negBarColor: "#cd6a6a",
            height: '20px'
        });
        myvalues = [15, 19, -20, 22, -13, 27, 31, 27, 19, 30, 21];
        $('#sparkline-6').sparkline(myvalues, {
            type: 'bar',
            barColor: '#18A689',
            negBarColor: "#cd6a6a",
            height: '20px'
        });


        //******************** VISITS CHART ********************//
        function randomValue() {
            return (Math.floor(Math.random() * (1 + 24))) + 8;
        }

        var data1 = [
            [1, 5 + randomValue()], [2, 10 + randomValue()], [3, 10 + randomValue()], [4, 15 + randomValue()], [5, 20 + randomValue()], [6, 25 + randomValue()], [7, 30 + randomValue()], [8, 35 + randomValue()], [9, 40 + randomValue()], [10, 45 + randomValue()], [11, 50 + randomValue()], [12, 55 + randomValue()], [13, 60 + randomValue()], [14, 70 + randomValue()], [15, 75 + randomValue()], [16, 80 + randomValue()], [17, 85 + randomValue()], [18, 90 + randomValue()], [19, 95 + randomValue()], [20, 100 + randomValue()]
        ];
        var data2 = [
            [1, 1425], [2, 1754], [3, 1964], [4, 2145], [5, 2550], [6, 2210], [7, 1760], [8, 1820], [9, 1880], [10, 1985],  [11, 2240],  [12, 2435]
        ];

        var plot = $.plot(
            $('#chart_visits'), [{
                data: data2,
                label: [
                    ["January"],
                    ["February"],
                    ["March"],
                    ["April"],
                    ["May"],
                    ["June"],
                    ["July"],
                    ["August"],
                    ["September"],
                    ["October"],
                    ["November"],
                    ["December"]
                ],
                showLabels: true,
                color: '#3584b2',
                points: {
                    fillColor: "#3584b2"
                }
            }], {
                grid: {
                    color: '#fff',
                    borderColor: "transparent",
                    clickable: true,
                    hoverable: true
                },
                series: {
                    bars: {
                        show: true,
                        barWidth: 0.4,
                        fill: true,
                        fillColor: 'rgba(53,132,178,0.7)'
                    },
                    points: {
                        show: false
                    }
                },
                xaxis: {
                    mode: "time",
                    monthNames: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
                    axisLabel: 'Month',
                },
                yaxis: {
                    tickColor: '#e8e8e8'
                },
                legend: {
                    show: false
                },
                tooltip: true
            });

        var previousPoint = null;
        $("#chart_visits").bind("plothover", function (event, pos, item) {
            $("#x").text(pos.x.toFixed(0));
            $("#y").text(pos.y.toFixed(0));
            if (item) {
                if (previousPoint != item.dataIndex) {
                    previousPoint = item.dataIndex;
                    $("#flot-tooltip").remove();
                    var x = item.datapoint[0].toFixed(0),
                        y = item.datapoint[1].toFixed(0);
                    showTooltip(item.pageX, item.pageY, y + " visits in " + item.series.label[item.dataIndex]);
                }
            } else {
                $("#flot-tooltip").remove();
                previousPoint = null;
            }
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
        };

    }


    /*------------------------------------------------------------------------------------*/
    /*-------------------------------  POSTS / ARTICLES ----------------------------------*/

    /* Select checked row */
    $('input:checkbox').on('click', function () {
       if ($(this).prop('checked') ==  true){
            $(this).prop('checked', true);
            $(this).parent().parent().parent().addClass('selected');
        } else {
            $(this).prop('checked', false);
            $(this).parent().parent().parent().removeClass('selected');
        }
    });

    /* Toggle All Checkbox Function */
    $('.check_all').on('click', function () {
       if ($(this).prop('checked') ==  true){
            $(this).closest('table').find('input:checkbox').prop('checked', true);
            $(this).closest('table').find('tr').addClass('selected');
        } else {
            $(this).closest('table').find('input:checkbox').prop('checked', false);
            $(this).closest('table').find('tr').removeClass('selected');
        }
    });


    if($('body').data('page') == 'posts'){

        var opt = {};
        // Tools: export to Excel, CSV, PDF & Print
        opt.sDom = "<'row m-t-10'<'col-md-6'><'col-md-6'Tf>r>t<'row'<'col-md-6'><'col-md-6 align-right'p>>",
        opt.oLanguage = { "sSearch": "","sZeroRecords": "No articles found" } ,
        opt.iDisplayLength = 15,
        opt.oTableTools = {
            "sSwfPath": "assets/plugins/datatables/swf/copy_csv_xls_pdf.swf",
            "aButtons": ["csv", "xls"]
        };
        opt.aaSorting = [[ 4, 'asc' ]];
        opt.aoColumnDefs = [
              { 'bSortable': false, 'aTargets': [0] }
           ];

        var oTable = $('#posts-table').dataTable(opt);
        oTable.fnDraw();

        /* Add a placeholder to searh input */
        $('.dataTables_filter input').attr("placeholder", "Search an article...");

        /* Delete a product */
        $('#posts-table a.delete').live('click', function (e) {
            e.preventDefault();
            if (confirm("Are you sure to delete this article ?") == false) {
                return;
            }
            var nRow = $(this).parents('tr')[0];
            oTable.fnDeleteRow(nRow);
            // alert("Deleted! Do not forget to do some ajax to sync with backend :)");
        });

    }


    /*------------------------------------------------------------------------------------*/
    /*------------------------------------  EVENTS ----------------------------------------*/

    if($('body').data('page') == 'events'){

        $('#reportrange').daterangepicker( {
              ranges: {
                 'Today': [moment(), moment()],
                 'Yesterday': [moment().subtract('days', 1), moment().subtract('days', 1)],
                 'Last 7 Days': [moment().subtract('days', 6), moment()],
                 'Last 30 Days': [moment().subtract('days', 29), moment()],
                 'This Month': [moment().startOf('month'), moment().endOf('month')],
                 'Last Month': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
              },
              startDate: moment().subtract('days', 29),
              endDate: moment()
            },
            function(start, end) {
                $('#reportrange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
            }
        );


        var opt = {};
        // Tools: export to Excel, CSV, PDF & Print
        opt.sDom = "<'row m-t-10'<'col-md-6'><'col-md-6'T>r>t<'row'<'col-md-6'><'col-md-6 align-right'p>>",
        opt.oLanguage = { "sSearch": "","sZeroRecords": "No event found" } ,
        opt.iDisplayLength = 6,
        opt.oTableTools = {
            "sSwfPath": "assets/plugins/datatables/swf/copy_csv_xls_pdf.swf",
            "aButtons": []
        };
        opt.aaSorting = [[ 5, 'asc' ]];
        opt.aoColumnDefs = [
              { 'bSortable': false, 'aTargets': [0] }
           ];

        var oTable = $('#events-table').dataTable(opt);
        oTable.fnDraw();

        /* Add a placeholder to searh input */
        $('.dataTables_filter input').attr("placeholder", "Search an event...");

        /* Delete a product */
        $('#events-table a.delete').live('click', function (e) {
            e.preventDefault();
            if (confirm("Are you sure to delete this event ?") == false) {
                return;
            }
            var nRow = $(this).parents('tr')[0];
            oTable.fnDeleteRow(nRow);
            // alert("Deleted! Do not forget to do some ajax to sync with backend :)");
        });

    }


    if($('body').data('page') == 'event'){

        $('#reportrange').daterangepicker( {
              ranges: {
                 'Today': [moment(), moment()],
                 'Yesterday': [moment().subtract('days', 1), moment().subtract('days', 1)],
                 'Last 7 Days': [moment().subtract('days', 6), moment()],
                 'Last 30 Days': [moment().subtract('days', 29), moment()],
                 'This Month': [moment().startOf('month'), moment().endOf('month')],
                 'Last Month': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
              },
              startDate: moment().subtract('days', 29),
              endDate: moment()
            },
            function(start, end) {
                $('#reportrange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
            }
        );


        var opt = {};

        /* Delete a product */
        $('#comments-table a.delete').live('click', function (e) {
            e.preventDefault();
            if (confirm("Are you sure to delete this event ?") == false) {
                return;
            }
            var nRow = $(this).parents('tr')[0];
            oTable.fnDeleteRow(nRow);
            // alert("Deleted! Do not forget to do some ajax to sync with backend :)");
        });

    }




});