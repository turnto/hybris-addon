<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="showLinks" required="false" type="java.lang.Boolean" %>
<%@ attribute name="starsClass" required="false" type="java.lang.String" %>

<%@ attribute name="product" required="true"
              type="de.hybris.platform.commercefacades.product.data.ProductData" %>

<link rel="stylesheet" href="https://static.www.turnto.com/css/teasers/inputteasers.css"/>
<div class="rating js-ratingCalc ${starsClass}" data-rating='{"rating":${product.averageRating},"total":5}'>

    <%--   <div class="rating-stars">
           <span class="js-ratingIcon glyphicon glyphicon-star"></span>
       </div>--%>

    <%--<c:if test="${not empty product.reviews}">
        <spring:theme code="review.based.on"
                      arguments="${fn:length(product.reviews)}"/>
    </c:if>
    <c:choose>
        <c:when test="${showLinks}">
            <c:if test="${not empty product.reviews}">
                <a href="#tabreview" class="js-openTab"><spring:theme code="review.see.reviews"/></a>
            </c:if>
            <a href="#tabreview" class="js-writeReviewTab"><spring:theme code="review.write.title"/></a>
        </c:when>
        <c:otherwise>
            <spring:theme code="review.reviews"/>
        </c:otherwise>
    </c:choose>--%>
    <table width="74%">
        <tr>
            <th><span class="TurnToReviewsTeaser"></span></th>
            <th><span class="TurnToItemInputTeaser"></span></th>
        </tr>
    </table>
</div>

<script type="text/javascript">
    var turnToConfig = {
                siteKey: "2qtC5sJ5gVYcfvesite",
                setupType: "staticEmbed",
                reviewsSetupType: "staticEmbed",
                itemInputTeaserFunc: customItemInputTeaserFunc,
                reviewsTeaserFunc: customReviewsTeaser,
                embedCommentCapture: true,
                postPurchaseFlow: true,
                setTeaserCookieOnView: true
            },
            TurnToItemSku = getSKU();

    (function () {
        var t = document.createElement('script');
        t.type = 'text/javascript';
        t.async = true;
        t.src = document.location.protocol + "//static.www.turnto.com/sitedata/2qtC5sJ5gVYcfvesite/v4_2/" + TurnToItemSku + "/d/itemjs";
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(t, s);
    })
    ();

    function getSKU() {
        var val = window.location.href,
                id = val.substr(val.indexOf("/p/") + 3);

        return id.split("?")[0];
    }

    function customItemInputTeaserFunc(clazz, data) {
        var clazzNam = clazz || "TurnToItemInputTeaser";
        var iteasers = TurnTojQuery("." + clazzNam);

        var htmlCode = '<div class="TTinputTeaserCust1"> <div class="TTteaserHeaderCust1">Need advice? More information?</div><div style="position:relative">' +
                '<div id="TTinputTeaserBoxCust1">' +
                    /* '<a class="TTteaBubble1Cust1" href="javascript:void(0)" style="text-decoration:none"></a>' +*/
                '<input type="text" id="TTinputTeaserQCust1" placeholder="Type in your question. We\'ll search for answers.">  <!--<a class="TTteaNext1Cust1" href="javascript:void(0)" style="display:none;text-decoration:none"></a>--></div>'
                + '<div class="TT2clearBoth"></div>'
                + ((data.counts.q > 0) ? '<div class="TTteaSearchlineCust2">or <a class="TTteaSearchLinkCust2" href="javascript:void(0)" style="text-decoration:underline">Browse ' + (data.counts.q + ' question' + (data.counts.q == 1 ? "" : "s") + ' and ' + data.counts.a + ' answer' + (data.counts.a == 1 ? "" : "s")) + '</a></div>' : "" )
                + '</div>'
                + '</div>';

        iteasers.html(htmlCode);

        TurnTojQuery("#TTinputTeaserQCust1").keypress(function (e) {
            if (e.which == 13) {
                clickQaTabFromTeaser();
                TurnTo.itemTeaserClick({fromInputTeaser: true, text: TurnTojQuery("#TTinputTeaserQCust1").val()});
            }
        }).focus(function () {
            TurnTojQuery(".TTteaNext1Cust1").show();
        });

        var clearHandler = function () {
            // Hide our custom "auto clear" X on IE 10
            if (TurnTojQuery.browser.msie && TurnTojQuery.browser.version.indexOf('.') && TurnTojQuery.browser.version.substr(0, TurnTojQuery.browser.version.indexOf('.')) == 10) {
                TurnTojQuery('#TTinputTeaserClear').hide();
                return;
            }

            if (TurnTojQuery('#TTinputTeaserQCust1').val().length == 0) {
                TurnTojQuery('#TTinputTeaserClear').css('visibility', 'hidden');
            } else {
                TurnTojQuery('#TTinputTeaserClear').css('visibility', 'visible');
            }
        };

        TurnTojQuery("#TTinputTeaserQCust1").keyup(clearHandler).blur(clearHandler);
        TurnTojQuery(".TTteaNext1Cust1").click(function () {
            clickQaTabFromTeaser();
            TurnTo.itemTeaserClick({fromInputTeaser: true, text: TurnTojQuery("#TTinputTeaserQCust1").val()});
        });
        TurnTojQuery(".TTteaSearchLinkCust2").click(function () {
            clickQaTabFromTeaser();
            TurnTo.itemTeaserClick({fromInputTeaser: false});
        });
    }

    function clickQaTab() {
        $('#accessibletabsnavigation0-4').find('a').click();
    }

    function clickQaTabFromTeaser() {
        clickQaTab();
        var qaTabPos = TurnTojQuery('#accessibletabsnavigation0-4');
        window.scrollTo(0, qaTabPos.offset().top);
    }

    function customReviewsTeaser(clazz, data) {
        var clazzNam = clazz || "TurnToReviewsTeaser";

        var iteasers = TurnTojQuery("." + clazzNam);

        // round the average rating to the nearest tenth
        var rating = Math.round((TurnToItemData.counts.ar + 0.25) * 100.0) / 100.0;
        rating = rating.toString();
        var decimal = parseInt(rating.substring(2, 3))
        rating = rating.substring(0, 1) + "-" + (decimal >= 5 ? '5' : '0')

        var html = '<div>' +
                '<div class="TT2left TTratingBox TTrating-' + rating + '">' +
                '    </div>' +
                '<div class="TTratingLinks">' +
                '    <a class="TTreadReviews" href="javascript:void(0)">Read ' + TurnToItemData.counts.r + ' Review' + (TurnToItemData.counts.r == 1 ? '' : 's') + '</a> or <a class="TTwriteReview" href="javascript:void(0)">Write a Review</a>' +
                '</div>' +
                '<div class="TTclear"></div>' +
                '</div>';
        iteasers.html(html);

        var teaserClickFn = function () {
            clickReviewsTabFromTeaser();
        };
        iteasers.find('.TTreadReviews').click(teaserClickFn);
        iteasers.find('.TTwriteReview').click(teaserClickFn);
    }

    function clickReviewsTab() {
        $('#accessibletabsnavigation0-2').find('a').click();
    }

    function clickReviewsTabFromTeaser() {
        clickReviewsTab();
        var qaTabPos = TurnTojQuery('#accessibletabsnavigation0-2');
        window.scrollTo(0, qaTabPos.offset().top);
    }
</script>