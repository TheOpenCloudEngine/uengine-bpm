<%@include file="mw3_common.jsp" %>

<script>

	function loadMetaworksObject(){


		var type = request.getParameter('type');
		if(type == 'runner'){
			var projectId = request.getParameter('projectId');
			var className = request.getParameter('className');

			var runner = new MetaworksObject({
				__className : "org.uengine.codi.mw3.Runner",
				projectId : projectId,
				className : className
			}, 'body');

			runner.run();

		}else if(type == 'SSOLogin'){
			mw3.setWhen(mw3.WHEN_EDIT);
			mw3.setHow('login');
			mw3.setWhere(mw3.WHERE_EVER);
			var ssoService = request.getParameter("service")!=null?request.getParameter("service"):"";
			var startcodi = new MetaworksObject({
				__className : "org.uengine.codi.mw3.StartCodi",
				key : "loader",
				ssoService: ssoService
			}, 'body');
		}else{
			var startcodi = new MetaworksObject({
				__className : "org.uengine.codi.mw3.StartCodi",
				key : 'loader'
			}, 'body');
		}


	}


</script>