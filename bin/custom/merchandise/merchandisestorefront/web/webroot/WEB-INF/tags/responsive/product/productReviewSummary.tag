<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="showLinks" required="false" type="java.lang.Boolean" %>
<%@ attribute name="starsClass" required="false" type="java.lang.String" %>
<%@ attribute name="flags" required="false" type="java.util.Map" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>

<c:set var="isOverlay"
       value="${flags.get('checkboxQA').getFlag() eq 'true' and flags.get('checkboxQA').getSetupType().getCode() eq \"overlay\"}"/>
<c:set var="isOverlayRating"
       value="${flags.get('checkboxRating').getFlag() eq 'true' and flags.get('checkboxRating').getSetupType().getCode() eq \"overlay\"}"/>
<link rel="stylesheet" href="https://static.www.turnto.com/css/teasers/inputteasers.css"/>
<link rel="stylesheet" href="${commonResourcePath}/css/custom.css"/>
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
            <c:if test="${flags.get('checkboxRating').getFlag() eq 'true'}">
                <th><span class="TurnToReviewsTeaser"></span></th>
            </c:if>
        </tr>
    </table>
</div>

<script type="text/javascript">
    var turnToConfig = {
                siteKey: "${siteKey}",
                setupType: "${flags.get('checkboxQA').getSetupType().getCode()}",
                reviewsSetupType: "${flags.get('checkboxRating').getSetupType().getCode()}",
                itemInputTeaserFunc: customItemInputTeaserFunc,
                chatter: {
                    columns: 4,
                    rowsInCollapsedView: 2,
                    rowsInExpandedView: 5,
                    title: "Why I Chose This:",
                    expandText: "Read all",
                    collapsedText: "Hide all",
                    minimumCommentCount: 1,
                    minimumCommentCharacterCount: 1,
                    sortOrder: "most recent",
                    minimumCommentWordCount: 0
                },
                embedCommentCapture: true,
                postPurchaseFlow: true,
                setTeaserCookieOnView: true
            },
            TurnToChatterSku = "${product.code}",
            TurnToItemSku = "${product.code}";

    if (!${isOverlayRating}) {
        turnToConfig.reviewsTeaserFunc = customReviewsTeaser;
    }

    (function () {
        var tt = document.createElement('script');
        tt.type = 'text/javascript';
        tt.async = true;
        tt.src = document.location.protocol + "//static.www.turnto.com/traServer4_2/trajs/" + turnToConfig.siteKey + "/tra.js";
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(tt, s);
    })();

    (function () {
        var t = document.createElement('script');
        t.type = 'text/javascript';
        t.async = true;
        t.src = document.location.protocol + "//static.www.turnto.com/sitedata/" + turnToConfig.siteKey + "/v4_2/" + TurnToItemSku + "/d/itemjs";
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(t, s);
    })();

    if (${flags.get('turntoCheckoutChatter').getFlag()}) {
        (function () {
            var t = document.createElement('script');
            t.type = 'text/javascript';
            t.async = true;
            t.src = document.location.protocol + "//static.www.turnto.com/traServer4_2/chatterjs/" + turnToConfig.siteKey + "/turnto-chatter.js";
            var s = document.getElementsByTagName('script')[0];
            s.parentNode.insertBefore(t, s);
        })();
    }
    function customItemInputTeaserFunc(clazz, data) {
        var clazzNam = clazz || "TurnToItemInputTeaser";
        var iteasers = TurnTojQuery("." + clazzNam);

        var htmlCode = '<div class="TTinputTeaserCust1"> <div class="TTteaserHeaderCust1">Need advice? More information?</div><div style="position:relative">' +
                '<div id="TTinputTeaserBoxCust1">' +
                '<a class="TTteaBubble1Cust1" href="javascript:void(0)" style="text-decoration:none"></a>' +
                '<input type="text" id="TTinputTeaserQCust1" style="width: 310px;" placeholder="Type in your question. We\'ll search for answers."></div>'
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
        $('#accessibletabsnavigation0-1').find('a').click();
    }

    function clickQaTabFromTeaser() {
        if (!${isOverlay}) {
            clickQaTab();
            var qaTabPos = TurnTojQuery('#accessibletabsnavigation0-1');
            window.scrollTo(0, qaTabPos.offset().top);
        }
    }

    function customReviewsTeaser(clazz, data) {
        var clazzNam = clazz || "TurnToReviewsTeaser";

        var iteasers = TurnTojQuery("." + clazzNam);

        // round the average rating to the nearest tenth
        var rating = Math.round((TurnToItemData.counts.ar + 0.25) * 100.0) / 100.0;
        rating = rating.toString();
        var decimal = parseInt(rating.substring(2, 3))
        rating = rating.substring(0, 1) + "-" + (decimal >= 5 ? '5' : '0');

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