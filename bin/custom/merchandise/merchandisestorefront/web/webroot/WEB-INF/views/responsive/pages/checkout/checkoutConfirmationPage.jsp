<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="multi" tagdir="/WEB-INF/tags/addons/b2ccheckoutaddon/responsive/checkout/multi" %>


<template:page pageTitle="${pageTitle}" hideHeaderLinks="true">

    <c:url value="${continueUrl}" var="continueShoppingUrl" scope="session"/>

    <cms:pageSlot position="TopContent" var="feature" element="div">
        <cms:component component="${feature}"/>
    </cms:pageSlot>


    <div class="container">
        <div class="checkout-success">
            <div class="checkout-success-headline">
                <span class="glyphicon glyphicon-lock"></span> <spring:theme
                    code="checkout.orderConfirmation.checkoutSuccessful"/>
            </div>

            <div class="checkout-success-body">
                <div class="checkout-success-body-headline"><spring:theme
                        code="checkout.orderConfirmation.thankYouForOrder"/></div>
                <p><spring:theme code="text.account.order.orderNumber" text="Order number is {0}"
                                 arguments="${orderData.code}"/></p>

                <p><spring:theme code="checkout.orderConfirmation.copySentTo" arguments="${email}"/></p>

                <p><multi:pickupGroups2 orderData="${orderData}"/></p>

                <button class="btn btn-default continueShoppingButton"
                        data-continue-shopping-url="${continueShoppingUrl}"><spring:theme
                        code="checkout.orderConfirmation.continueShopping"/></button>

            </div>

        </div>
    </div>
    <br>

    <div id="TurnToRecentComments"></div>
    <div id="TT3commentCapture"></div>
    <%--<div id="TTcommentCapture"></div>--%>
    <div>
            <%--@elvariable id="allItems" type="java.util.List<de.hybris.platform.commercefacades.order.data.OrderEntryData>"--%>
        <c:forEach items="${allItems}" var="item">
            <c:set value="${item.product.images}" var="img"/>
            <c:forEach items="${img}" var="i">
                <c:if test="${i.format eq 'product'}">
                    <c:set value="${i}" var="image"/>
                </c:if>
            </c:forEach>

            <input type="hidden" class="product"
                   data-id="${item.product.code}"
                   data-name="${item.product.name}"
                   data-price="${item.product.price.value}"
                   data-url="${item.product.url}/?site=hybris"
                   data-img="${image.url}">
        </c:forEach>
    </div>

    <cms:pageSlot position="SideContent" var="feature" element="div">
        <cms:component component="${feature}"/>
    </cms:pageSlot>
</template:page>

<script type="text/javascript">
    var turnToConfig = {
        siteKey: "${siteKey}",
        orderConfFlowPauseSeconds: 3,
        postPurchaseFlow: true,
        commentCaptureShowUsername: true
/*        recentComments: {
            height: "300px",
            width: "310px",
            // category: "movies",
            // low or high
            frequency: "high",
            // Only used when frequency = low
            timeWindowInHours: 48,
            // crawlUp, zoomUp, none
            animationType: "none",
            animationFreqInMs: 5000
            // Optional: Change "What's this" copy
            ,titleMaxLength: 60
            ,nameMaxLength: 20
            // Optional: The URL of the full page checkout chatter
            //,fullPageUrl: ""
            // Optional: Change header copy
            //,header: "My custom header here"
        }*/
    };

    (function () {
        var tt = document.createElement('script');
        tt.type = 'text/javascript';
        tt.async = true;
        tt.src = document.location.protocol + "//static.www.turnto.com/traServer${currentVersion}/trajs/" + turnToConfig.siteKey + "/tra.js";
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(tt, s);
    })();

</script>

<script type="text/javascript" src="//static.www.turnto.com/tra${currentVersion}/turntoFeed.js"></script>

<script type="text/javascript">
    TurnToFeed.addFeedPurchaseOrder({
        orderId: "${orderData.code}",
        email: "${email}",
        firstName: "${user.firstName}",
        lastName: "${user.lastName}"
    });
    $.each($('.product'), function (i, item) {
        TurnToFeed.addFeedLineItem({
            title: $(item).data('name'),
            url: $(item).data('url'),
            sku: $(item).data('id'),
            price: $(item).data('price'),
            itemImageUrl: $(item).data('img')
        });
    });
    if (${flags.get('turntoOrderReporting').getFlag()}) TurnToFeed.sendFeed();
</script>
