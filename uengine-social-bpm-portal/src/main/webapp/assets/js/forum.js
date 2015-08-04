$(function(){

	/* We initiate calendar for filter search */
	jQuery('#calendar').datetimepicker();

	$('.forum-questions .message-item').on('click', function(){
		 $('.forum-questions').fadeOut("slow", function(){
		 	 window.location="forum_answer.html"; 
		 });
	});

	$('.forum-category li').on('click', function(){
		 $('.forum-answer').fadeOut("slow", function(){
		 	 window.location="forum.html"; 
		 });
	});

	if($('.forum-questions').length){
		$('.forum-questions').fadeIn("slow");
	}

	if($('.forum-answer').length){
		$('.forum-answer').fadeIn("slow");
	}




	/* Questions Chart */
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
	    [12, 55 + randomValue()]
	];

	var plot = $.plot($("#chart_1"), [{
	    data: data1,
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
	    colors: ["#428BCA"]
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
	$("#chart_1").bind("plothover", function (event, pos, item) {
	    $("#x").text(pos.x.toFixed(0));
	    $("#y").text(pos.y.toFixed(0));
	    if (item) {
	        if (previousPoint != item.dataIndex) {
	            previousPoint = item.dataIndex;
	            $("#flot-tooltip").remove();
	            var x = item.datapoint[0].toFixed(0),
	                y = item.datapoint[1].toFixed(0);
	            showTooltip(item.pageX, item.pageY, y + " questions in " + item.series.label[item.dataIndex]);
	        }
	    } else {
	        $("#flot-tooltip").remove();
	        previousPoint = null;
	    }
	});

});


