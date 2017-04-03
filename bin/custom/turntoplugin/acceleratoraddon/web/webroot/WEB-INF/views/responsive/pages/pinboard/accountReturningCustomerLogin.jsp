<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="userTT" tagdir="/WEB-INF/tags/addons/turntoplugin/responsive/content"%>
<link href='https://fonts.googleapis.com/css?family=Lato:300,400%7CRoboto:100,400,300,500,600,700%7COpen+Sans:400,500,600' rel='stylesheet' type='text/css'>
<link href='https://fonts.googleapis.com/css?family=Roboto:400,100,300,500,700' rel='stylesheet' type='text/css'>
<link href='https://fonts.googleapis.com/css?family=Open+Sans:400,300,300italic,400italic,600,600italic,700,700italic,800,800italic' rel='stylesheet' type='text/css'>
<link type="text/css" rel="stylesheet" media="screen" href="${commonResourcePath}/_ui/css/turntopopup.css"/>
<c:url value="/turn/j_spring_security_check" var="loginActionUrl" />
<div class="container">

    <div class="row">
        <div class="col-md-6 pull-left">
            <div class="login-section">
                <c:if test="${loginError eq 'true'}">
                    <div class="alert alert-danger alert-dismissable">
                        <common:globalMessages/>
                    </div>
                </c:if>
                <userTT:login actionNameKey="login.login" action="${loginActionUrl}"/>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    var turnToConfig = {
        siteKey: "${siteKey}",
        setupType: "overlay"
    };

    (function() {
        var tt = document.createElement('script'); tt.type = 'text/javascript'; tt.async = true;
        tt.src = document.location.protocol + "//static.www.turnto.com/traServer4_3/trajs/" + turnToConfig.siteKey + "/tra.js";
        var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(tt, s);
    })();



    window.onload = function() {
        var authToken = "${users.user_auth_token}";
        if (authToken != 'anonymous') {
            TurnTo.localAuthenticationComplete();
        }
    }


</script>