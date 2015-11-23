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

    <div id="TTcommentCapture"></div>
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

<script type="text/javascript" src="//static.www.turnto.com/tra4_2/turntoFeed.js"></script>
<script type="text/javascript" src="//static.www.turnto.com/traServer4_2/trajs/2qtC5sJ5gVYcfvesite/tra.js"></script>

<script type="text/javascript">
    var turnToConfig = {
        siteKey: "2qtC5sJ5gVYcfvesite",
        orderConfFlowPauseSeconds: 3,
        postPurchaseFlow: true,
        embedCommentCapture: true
    };

    TurnToFeed.addFeedPurchaseOrder({orderId: "${orderData.code}", email: "${user.displayUid}", firstName: "${user.firstName}", lastName: "${user.lastName}"});
    $.each($('.product'), function (i, item) {
        TurnToFeed.addFeedLineItem({
            title: $(item).data('name'),
            url: 'https://turnto.zaelab.com:9001/store' + $(item).data('url'),
            sku: $(item).data('id'),
            price: $(item).data('price'),
            itemImageUrl: 'http://turnto.zaelab.com:9001' + $(item).data('img')
        });
    });

    TurnToFeed.sendFeed();
</script>

