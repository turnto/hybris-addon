<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="isOnTurntoOrderReporting" value="${flags.get('turntoOrderReporting').getFlag() eq 'true'}"/>
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

<c:if test="${isOnTurntoOrderReporting}">
    <script type="text/javascript">
        var turnToConfig = {
            siteKey: "${siteKey}",
            orderConfFlowPauseSeconds: 3,
            postPurchaseFlow: true,
            commentCaptureShowUsername: true
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
        TurnTojQuery.each(TurnTojQuery('.product'), function (i, item) {
            TurnToFeed.addFeedLineItem({
                title: $(item).data('name'),
                url: $(item).data('url'),
                sku: $(item).data('id'),
                price: $(item).data('price'),
                itemImageUrl: $(item).data('img')
            });
        });

        if (${isOnTurntoOrderReporting}) {
            TurnToFeed.sendFeed()
        }

    </script>
</c:if>