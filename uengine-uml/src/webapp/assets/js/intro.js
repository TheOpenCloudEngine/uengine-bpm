$(function(){

	$("html, body").animate({
     scrollTop: 0
  }, 100);

	function startIntro(){

    var intro = introJs();
      intro.setOptions({
      	showBullets : true,
        steps: [
          {
            element: '#sidebar', position: "right",
            intro: "This menu permit you to navigate through all pages."
          },{
            element: '#panel-visitors', position: "bottom",
            intro: "Here you can view number your visits stats."
          },{
            element: '#panel-pages', position: "bottom",
            intro: "If you want to play again Intro demo."
          },{
            element: '#browser-stats', position: "left",
            intro: "You will have more details here with the visitors chart."
          },{
            element: '#to-do-list', position: "right",
            intro: "Here you can edit your tasks."
          }
          ,{
            element: '#contact-list', position: "left",
            intro: "You can manage your contacts list and send them emails."
          }
        ]
      });

      intro.start();
      $("html, body").scrollTop(0);
	}

	startIntro();

  /* Replay Intro on Button click */
  $('#start-intro').click(function () {
    startIntro();
  });

});


