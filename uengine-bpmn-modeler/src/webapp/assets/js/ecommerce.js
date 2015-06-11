$(function () {

/*------------------------------------------------------------------------------------*/
/*------------------------------  ECOMMERCE DASHBOARD --------------------------------*/

    if($('body').data('page') == 'ecommerce_dashboard'){


        /* Delete a product */
        $('#products-table a.delete').live('click', function (e) {
            e.preventDefault();
            if (confirm("Are you sure to delete this product ?") == false) {
                return;
            }
            $(this).parent().parent().fadeOut();
        });

        /* Delete a review */
        $('#product-review a.delete').live('click', function (e) {
            e.preventDefault();
            if (confirm("Are you sure to delete this comment ?") == false) {
                return;
            }
            $(this).parent().parent().fadeOut();
        });

        /* Validate a review */
        $('#product-review a.edit').live('click', function (e) {
            e.preventDefault();

            $(this).parent().parent().find('.label').removeClass('label-info').addClass('label-success').html('Approved');
            $(this).fadeOut();
        });

/*
<span class="label label-success w-300">Approved</span>

*/

        /* We have to recreate charts on resize to make them responsive */
        $(window).resize(function () {


        });

        //******************** REVENUE CHART ********************//
        function randomValue() {
            return (Math.floor(Math.random() * (1 + 24))) + 8;
        }

        var data1 = [
            [1, 5 + randomValue()], [2, 10 + randomValue()], [3, 10 + randomValue()], [4, 15 + randomValue()], [5, 20 + randomValue()], [6, 25 + randomValue()], [7, 30 + randomValue()], [8, 35 + randomValue()], [9, 40 + randomValue()], [10, 45 + randomValue()], [11, 50 + randomValue()], [12, 55 + randomValue()], [13, 60 + randomValue()], [14, 70 + randomValue()], [15, 75 + randomValue()], [16, 80 + randomValue()], [17, 85 + randomValue()], [18, 90 + randomValue()], [19, 95 + randomValue()], [20, 100 + randomValue()]
        ];
        var data2 = [
            [6, 1425], [7, 1754], [8, 1964], [9, 2145], [10, 2550], [11, 2210], [12, 1760], [13, 1820], [14, 1880], [15, 1985],  [16, 2240]
        ];

        var plot = $.plot(
            $('#chart_revenue'), [{
                label: "Revenue",
                data: data1,
                color: '#0090D9',
                points: {
                    fillColor: "#0090D9"
                }
            }], {
                grid: {
                    color: '#fff',
                    borderColor: "transparent",
                    clickable: true,
                    hoverable: true
                },
                series: {
                    lines: {
                        show: true,
                        fill: false,
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    show: false
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
        $("#chart_revenue").bind("plothover", function (event, pos, item) {
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
    /*-----------------------------------  PRODUCTS --------------------------------------*/

    if($('body').data('page') == 'products'){
        
        var opt = {};
  
         // Tools: export to Excel, CSV, PDF & Print
        opt.sDom = "<'row m-t-10'<'col-md-6'f><'col-md-6'T>r>t<'row'<'col-md-6'><'col-md-6 align-right'p>>",
        opt.oLanguage = { "sSearch": "" } ,
        opt.iDisplayLength = 15,

        opt.oTableTools = {
            "sSwfPath": "assets/plugins/datatables/swf/copy_csv_xls_pdf.swf",
            "aButtons": ["csv", "xls", "pdf", "print"]
        };
        opt.aoColumnDefs = [
              { 'bSortable': false, 'aTargets': [ 6,7,8,9 ] }
           ];
        

        var oTable = $('#products-table').dataTable(opt);
        oTable.fnDraw();

        /* Add a placeholder to searh input */
        $('.dataTables_filter input').attr("placeholder", "Search a product...");

        /* Delete a product */
        $('#products-table a.delete').on('click', function (e) {
            e.preventDefault();
            if (confirm("Are you sure to delete this product ?") == false) {
                return;
            }
            var nRow = $(this).parents('tr')[0];
            oTable.fnDeleteRow(nRow);
            // alert("Deleted! Do not forget to do some ajax to sync with backend :)");
        });

    }

        if($('body').data('page') == 'products-ajax'){
        
        var opt = {};
        opt.ajax = "assets/ajax/data-table.txt";

     
        // Tools: export to Excel, CSV, PDF & Print
        opt.sDom = "<'row m-t-10'<'col-md-6'f><'col-md-6'T>r>t<'row'<'col-md-6'><'col-md-6 align-right'p>>",
        opt.oLanguage = { "sSearch": "" } ,
        opt.iDisplayLength = 15,
        opt.fnDrawCallback =  function( oSettings ) {
         $('.progress-bar').progressbar();
       };
        opt.oTableTools = {
            "sSwfPath": "assets/plugins/datatables/swf/copy_csv_xls_pdf.swf",
            "aButtons": ["csv", "xls", "pdf", "print"]
        };
        opt.aoColumnDefs = [
              { 'bSortable': false, 'aTargets': [ 6,7,8,9 ] }
           ];
        

        var oTable = $('#products-table').dataTable(opt);
        oTable.fnDraw();

        /* Add a placeholder to searh input */
        $('.dataTables_filter input').attr("placeholder", "Search a product...");

        /* Delete a product */
        $('#products-table a.delete').on('click', function (e) {
            e.preventDefault();
            if (confirm("Are you sure to delete this product ?") == false) {
                return;
            }
            var nRow = $(this).parents('tr')[0];
            oTable.fnDeleteRow(nRow);
            // alert("Deleted! Do not forget to do some ajax to sync with backend :)");
        });

    }

    if($('body').data('page') == 'product_view'){


        /* Delete a review */
        $('#product-review a.delete').live('click', function (e) {
            e.preventDefault();
            if (confirm("Are you sure to delete this comment ?") == false) {
                return;
            }
            $(this).parent().parent().fadeOut();
        });

        /* Delete an image */
        $('#product-review a.delete-img').live('click', function (e) {
            e.preventDefault();
            if (confirm("Are you sure to delete this image ?") == false) {
                return;
            }
            $(this).parent().parent().fadeOut();
        });
        
    }

    /*------------------------------------------------------------------------------------*/
    /*------------------------------------  ORDERS ---------------------------------------*/

    if($('body').data('page') == 'orders'){

        var opt = {};
        // Tools: export to Excel, CSV, PDF & Print
        opt.sDom = "<'row m-t-10'<'col-md-6'f><'col-md-6'T>r>t<'row'<'col-md-6'><'col-md-6 align-right'p>>",
        opt.oLanguage = { "sSearch": "" } ,
        opt.iDisplayLength = 15,
        opt.oTableTools = {
            "sSwfPath": "assets/plugins/datatables/swf/copy_csv_xls_pdf.swf",
            "aButtons": ["csv", "xls", "pdf", "print"]
        };
        opt.aoColumnDefs = [
              { 'bSortable': false, 'aTargets': [ 9 ] }
           ];

        var oTable = $('#products-table').dataTable(opt);
        oTable.fnDraw();

        /* Add a placeholder to searh input */
        $('.dataTables_filter input').attr("placeholder", "Search an order...");

        /* Delete a product */
        $('#products-table a.delete').live('click', function (e) {
            e.preventDefault();
            if (confirm("Are you sure to delete this product ?") == false) {
                return;
            }
            var nRow = $(this).parents('tr')[0];
            oTable.fnDeleteRow(nRow);
            // alert("Deleted! Do not forget to do some ajax to sync with backend :)");
        });

    }

    if($('body').data('page') == 'shopping_cart'){

        var pop = $('.popbtn');
        var main_image = $('#main-image');

        pop.popover({
            trigger: 'manual',
            html: true,
            container: 'body',
            placement: 'bottom',
            animation: false,
            content: function() {
                return $('#popover').html();
            }
        });

        pop.on('click', function(e) {
            pop.popover('toggle');
            pop.not(this).popover('hide');
        });

        $(window).on('resize', function() {
            pop.popover('hide');
        });

        /* Show Image item onclick */
        $('.shop-item').on('click', function(){
            current_image = $(this).data('image');
            current_image_src = 'assets/img/shopping/' + current_image + '.png';
            main_image.fadeOut(200);
            setTimeout(function() {
                main_image.attr('src', current_image_src);
                main_image.fadeIn();
            }, 350);
        });


        function setCurrentProgressTab($rootwizard, $nav, $tab, $progress, index) {
            $tab.prevAll().addClass('completed');
            $tab.nextAll().removeClass('completed');
            var items = $nav.children().length,
                pct = parseInt((index + 1) / items * 100, 10),
                $first_tab = $nav.find('li:first-child'),
                margin = (1 / (items * 2) * 100) + '%';
            if ($first_tab.hasClass('active')) {
                $progress.width(0);
            } else {
                $progress.width(((index - 1) / (items - 1)) * 100 + '%');
                
            }
            $progress.parent().css({
                marginLeft: margin,
                marginRight: margin
            });
        }

        $(document).ready(function() {
            $('#rootwizard').bootstrapWizard({
                'tabClass': 'bwizard-steps',
                'nextSelector': '.button-next',
                'previousSelector': '.button-prev',
            });
        });


    }


});