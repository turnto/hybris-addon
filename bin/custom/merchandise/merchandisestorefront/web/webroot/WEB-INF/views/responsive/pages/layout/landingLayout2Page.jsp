<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<template:page pageTitle="${pageTitle}">

    <div class="no-space">
        <cms:pageSlot position="Section1" var="feature">
            <cms:component component="${feature}"/>
        </cms:pageSlot>

        <div class="row">
            <cms:pageSlot position="Section2A" var="feature">
                <cms:component component="${feature}" element="div" class="col-xs-6 col-sm-3"/>
            </cms:pageSlot>
            <cms:pageSlot position="Section2B" var="feature">
                <cms:component component="${feature}" element="div" class="col-xs-6 col-sm-3"/>
            </cms:pageSlot>
        </div>
    </div>

    <cms:pageSlot position="Section3" var="feature">
        <cms:component component="${feature}"/>
    </cms:pageSlot>

    <c:if test="${flags.get('ccPinboard').getFlag() eq 'true' and isSiteKeyInvalid eq 'false'}">
        <div id="TurnToPinboardContent"></div>
    </c:if>

    <div class="no-space">

        <div class="row">
            <cms:pageSlot position="Section4" var="feature">
                <cms:component component="${feature}" element="div" class="col-xs-6 col-sm-3"/>
            </cms:pageSlot>
        </div>

        <cms:pageSlot position="Section5" var="feature">
            <cms:component component="${feature}"/>
        </cms:pageSlot>
    </div>

</template:page>
<script type="text/javascript">
    var turnToConfig = {
                siteKey: "${siteKey}",
                setupType: "${flags.get('checkboxQA').getSetupType().getCode()}",
                reviewsSetupType: "${flags.get('checkboxRating').getSetupType().getCode()}",
                pinboard: {
                    contentType: 'checkoutComments',//checkoutComments
                    maxDaysOld:-1
                },
                embedCommentCapture: true,
                postPurchaseFlow: true,
                setTeaserCookieOnView: true,

                loadRteaserAfterChatter: false
            },
            TurnToChatterSku = "${product.code}",
            TurnToItemSku = "${product.code}";

    (function () {
        var tt = document.createElement('script');
        tt.type = 'text/javascript';
        tt.async = true;
        tt.src = document.location.protocol + "//static.www.turnto.com/traServer${currentVersion}/trajs/" + turnToConfig.siteKey + "/tra.js";
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(tt, s);
    })();

    (function () {
        var tt = document.createElement('script');
        tt.type = 'text/javascript';
        tt.async = true;
        tt.src = document.location.protocol + "//static.www.turnto.com/traServer${currentVersion}/pinboardjs/" + turnToConfig.siteKey + "/turnto-pinboard.js";
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(tt, s);
    })();
</script>