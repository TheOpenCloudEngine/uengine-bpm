<%@include file="mw3_common.jsp" %>

<script>

    function loadMetaworksObject(){

        var instanceId = request.getParameter('instanceId');
        var accessToken = request.getParameter('accessToken');

        new MetaworksObject({
            __className : "org.uengine.social.StandaloneProcessInstanceMonitor",
            instanceId: instanceId,
            accessToken: accessToken,

        }, 'body');

    }

</script>