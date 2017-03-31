<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="userTT" tagdir="/WEB-INF/tags/addons/turntoplugin/responsive/content"%>
<link href='https://fonts.googleapis.com/css?family=Lato:300,400%7CRoboto:100,400,300,500,600,700%7COpen+Sans:400,500,600' rel='stylesheet' type='text/css'>
<link href='https://fonts.googleapis.com/css?family=Roboto:400,100,300,500,700' rel='stylesheet' type='text/css'>
<link href='https://fonts.googleapis.com/css?family=Open+Sans:400,300,300italic,400italic,600,600italic,700,700italic,800,800italic' rel='stylesheet' type='text/css'>
<link type="text/css" rel="stylesheet" media="screen" href="${commonResourcePath}/_ui/css/turntoplugin.css"/>
<c:url value="/turn/j_spring_security_check" var="loginActionUrl" />

<style>

    #TurnToChatterContent blockquote.TT4quote {
        border: 0;
    }

    .TurnToReviewsTeaser {
        font-size: 13px !important;
        width: 400px;
    }

    .TurnToReviewsTeaser a {
        margin-left: 0 !important;
    }

    .TurnToReviewsTeaser .TTratingLinks {
        padding-left: 10px;
    }

    #TT4questionTextWrap #TT2questionText {
        height: 25px !important;
        overflow: hidden !important;
    }

    .AddToCart-ShareOnSocialNetworkAction {
        margin-top: 8px;
    }

    #sortOptions1 {
        width: 200px;
    }

    .product-listing a.buyer-comments {
        display: block;
        color: inherit;
        text-decoration: underline;
        margin-bottom: 10px;
    }

    #TurnToChatterContent .TT4quote-body {
        word-wrap: break-word !important;
    }

    #TurnToPinboardContent .TTpinComments{
        word-wrap: break-word !important;
    }

    #TTpartnerRegWindow {
        width: 860px;
        height:400px;
    }

    .chatter
    {
        padding-top: 30px ;
    }

    input[type="text"], input[type="password"] {
        background: #e1e1e1;
        border: none;
        width: 100%;
        height: 50px;
        padding-left: 20px;
        font-weight: 500;
        margin-bottom: 24px;
        border-radius: 0;
        color: #555;
    }
    label {
        display: inline-block;
        max-width: 100%;
        margin-bottom: 5px;
        font-weight: bold;
    }
    input[type="submit"], button[type="submit"] {
        height: 50px;
        line-height: 48px;
        background: #ffe55c;
        color: #333;
        width: 100%;
        font-size: 14px;
        text-transform: uppercase;
        font-weight: bold;
        letter-spacing: 1px;
        border-radius: 0 !important;
        border: solid 1px #ffe55c;
    }
    .alert-danger {
        border: 1px solid #c64444;
        color: #c64444;
    }

    .description h3 {
        font-size: 32px;
        line-height: 40px;
        font-weight: 300;
        font-family: "Roboto", "Helvetica Neue", Helvetica, Arial, sans-serif;
        padding: 10px 0;
        margin: 0;
    }

    label {
        font-family: "Roboto", "Helvetica Neue", Helvetica, Arial, sans-serif;
    }
    .login-section {
        padding: 0 15px;
    }
    .alert.negative {
        font-family: "Roboto", "Helvetica Neue", Helvetica, Arial, sans-serif;
        padding: 10px;
        box-sizing: border-box;
    }

</style>
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