<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Essencia Web UI</title>

		<!-- metaworks3 for ie7(json2) -->
		<script type="text/javascript" src="dwr/metaworks/scripts/json/json2.js"></script>
		
		<!-- metaworks3 for dwr engine -->
		<script type='text/javascript' src='dwr/engine.js'></script>
		<script type='text/javascript' src='dwr/util.js'></script>
		<script type='text/javascript' src='dwr/interface/Metaworks.js'></script>
		
		<!-- metaworks3 for jQuery -->
		<script type="text/javascript" src="dwr/metaworks/scripts/jquery/jquery-1.8.3.min.js"></script>
		
		<!-- metaworks3 for keyboard mapping -->
		<script type="text/javascript" src="dwr/metaworks/scripts/event/shortcut.js"></script>
		
		<!-- metaworks3 for context menu, import YUI -->
		<script type="text/javascript" src="dwr/metaworks/scripts/yui-3.2.0-min.js"></script>
		<script type="text/javascript" src="dwr/metaworks/scripts/yui/build/utilities/utilities.js"></script>
		<script type="text/javascript" src="dwr/metaworks/scripts/yui/build/container/container.js"></script>
		<script type="text/javascript" src="dwr/metaworks/scripts/yui/build/menu/menu.js"></script>
		 
		<!-- metaworks3 for ejs templete engine -->
		<script type="text/javascript" src="dwr/metaworks/scripts/ejs/ejs_production.js"></script>
		<script type="text/javascript" src="dwr/metaworks/scripts/ejs/ejs_debugger.js"></script>
		<script type="text/javascript" src="dwr/metaworks/scripts/ejs/view.js"></script>
		
		<!-- metaworks3 engine -->
		<script type="text/javascript" src="dwr/metaworks/scripts/metaworks.js"></script>
		
		<!-- init -->
		<script type="text/javascript">
				$(document).ready(function() {
					mw3.setWhen(mw3.WHEN_VIEW);
					mw3.template_error = function(e, actualFace) {
						if(e.lineNumber){
							if(e.lineText)
								var message = "["+actualFace+"] at line "+e.lineNumber+": "+e.lineText+": "+e.message;
							else
								var message = "["+actualFace+"] at line "+e.lineNumber+": "+e.message;
						}else
							var message = "["+actualFace+"] "+e.message;
					
						if(console){
							console.log('template_error');
							console.log(message);
							console.log(e);
						}
			 		};
			 		mw3.showError = function(objId, message, methodName){
			 			if(console){
			 				console.log('showError');
							console.log(message);
							console.log(objId);
							console.log(methodName);
						}
			 		};
			 		mw3.showInfo = function(objId, message){
			 		}
			 		
					var Request = function()
					{
					    this.getParameter = function( name )
					    {
					        var rtnval = "";
					        var nowAddress = unescape(location.href);
					        var parameters = (nowAddress.slice(nowAddress.indexOf("?")+1,nowAddress.length)).split("&");
			
					        for(var i = 0 ; i < parameters.length ; i++)
					        {
					            var varName = parameters[i].split("=")[0];
					            if(varName.toUpperCase() == name.toUpperCase())
					            {
					                rtnval = parameters[i].split("=")[1];
					                break;
					            }
					        }
					        return rtnval;
					    }
					}
					
			 		var request = new Request();
			 		
					var runner = new MetaworksObject({
    	                __className : "org.metaworks.GatewayRunner",
    	                fullClassName: request.getParameter("className")
            	    }, 'body');
			 		
					runner.run();
					
			 		mw3.onLoadFaceHelperScript();
				});
				
		</script>		
	</head>
	<body>
	</body>
</html>
