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

<c:set var="isTurnOffCheckboxQA"
       value="${flags.get('checkboxQA').getFlag() eq 'false'}"/>

<c:set var="isOverlayRating"
       value="${flags.get('checkboxRating').getFlag() eq 'true' and flags.get('checkboxRating').getSetupType().getCode() eq \"overlay\"}"/>

<c:set var="buyerComments" value="${flags.get('buyerComments').getFlag() eq 'true'}"/>

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
            <c:if test="${flags.get('checkboxRating').getFlag() eq 'true'}">
                <th><span class="TurnToReviewsTeaser"></span></th>
            </c:if>
        </tr>
    </table>
</div>

<script type="text/javascript">
    var TurnToGallerySkus = ["${product.code}"];
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
                    minimumCommentWordCount: 1
                },
                gallery: {
                    // configuration options...
                    // title: ‘Custom Title’ <- example
                },
                pinboard: {
                    contentType: 'visualContent',//checkoutComments
                    title: 'Gallery Pinboard',
                    skus: ["${product.code}"]
                },
                embedCommentCapture: true,
                postPurchaseFlow: true,
                setTeaserCookieOnView: true,
                loadRteaserAfterChatter: false
            },
            TurnToChatterSku = "${product.code}",
            TurnToItemSku = "${product.code}";

    if (!${isOverlayRating}) {
        turnToConfig.reviewsTeaserFunc = customReviewsTeaserDisplayWithCommentsLink;
    }

    (function () {
        var tt = document.createElement('script');
        tt.type = 'text/javascript';
        tt.async = true;
        tt.src = document.location.protocol + "//static.www.turnto.com/traServer${currentVersion}/trajs/" + turnToConfig.siteKey + "/tra.js";
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(tt, s);
    })();

    if (${currentVersion eq "4_3"}) {
        (function () {
            var tt = document.createElement('script');
            tt.type = 'text/javascript';
            tt.async = true;
            tt.src = document.location.protocol + "//static.www.turnto.com/traServer${currentVersion}/pinboardjs/" + turnToConfig.siteKey + "/turnto-pinboard.js";
            var s = document.getElementsByTagName('script')[0];
            s.parentNode.insertBefore(tt, s);
        })();
    }

    (function () {
        var t = document.createElement('script');
        t.type = 'text/javascript';
        t.async = true;
        t.src = document.location.protocol + "//static.www.turnto.com/sitedata/" + turnToConfig.siteKey + "/v${currentVersion}/" + TurnToItemSku + "/d/itemjs";
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(t, s);
    })();

    if (${currentVersion eq "4_3"}) {
        (function () {
            var gallery = document.createElement('script');
            gallery.type = 'text/javascript';
            gallery.async = true;
            gallery.src = document.location.protocol + "//static.www.turnto.com/traServer${currentVersion}/galleryjs/" + turnToConfig.siteKey + "/turnto-gallery.js/en_US";

            var s = document.getElementsByTagName('script')[0];
            s.parentNode.insertBefore(gallery, s);
        })();
    }


    if (${flags.get('turntoCheckoutChatter').getFlag()}) {
        (function () {
            var t = document.createElement('script');
            t.type = 'text/javascript';
            t.async = true;
            t.src = document.location.protocol + "//static.www.turnto.com/traServer${currentVersion}/chatterjs/" + turnToConfig.siteKey + "/turnto-chatter.js";
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


    function customReviewsTeaserDisplayWithCommentsLink(data) {
        var buyerComments = ${buyerComments};


        var iteasers = TurnTojQuery(".TurnToReviewsTeaser");

        // round the average rating to the nearest tenth
        var rating = Math.round((TurnToItemData.counts.ar + 0.25) * 100.0) / 100.0;
        rating = rating.toString();
        var decimal = parseInt(rating.substring(2, 3))
        rating = rating.substring(0, 1) + "-" + (decimal >= 5 ? '5' : '0')

        var commentCnt = TurnToItemData.counts.c;
        // this next if can be left out if you don't have the
        // TurnToChatterContent widget/div on the page
        if (turnToConfig != undefined && turnToConfig.loadRteaserAfterChatter == true) {
            commentCnt = TurnToItemData.counts.ccWdgtC;
        }

        var html = '<div>';
        // if don't want 5 empty stars to display when no reviews,
        // can wrap this next line in an if(TurnToItemData.counts.ar > 0)
        html += '<div class="TT2left TTratingBox TTrating-' + rating + '"></div>';
        if (TurnToItemData.counts.r == 0 && commentCnt == 0) {
            html += '<div><a class="TTwriteReview" href="javascript:void(0);">Be the first to write a review</a></div>';
        } else {
            html += '<div class="TTratingLinks">';
            if (TurnToItemData.counts.r > 0) {
                html += ' <a class="TTreadReviews" href="javascript:void(0)">Read ' + TurnToItemData.counts.r + ' Review' + (TurnToItemData.counts.r == 1 ? '' : 's') + '</a>';
            }

            if (buyerComments && commentCnt > 0) {
                if (TurnToItemData.counts.r > 0) {
                    html += ' | ';
                }
                html += ' <a class="TTreadComments" href="javascript: void(0)">';
                if (TurnToItemData.counts.r == 0) {
                    html += ' Read ';
                }
                html += commentCnt + ' Buyer Comment' + (commentCnt == 1 ? '' : 's') + '</a>';
            }

            html += ' or <a class="TTwriteReview" href="javascript:void(0)">Write a Review</a>' +
                    '</div>' +
                    '<div class="TTclear"></div>';

        }
        html += '</div>';
        iteasers.html(html);

        iteasers.find('.TTreadReviews').click(clickReviewsTabFromTeaser);
        iteasers.find('.TTwriteReview').click(function () {
            TurnTo.writeReview()
        });
        iteasers.find('.TTreadComments').click(clickCommentsFromTeaser);
    }

    function clickCommentsFromTeaser() {
        // your custom implementation should go here

        //var idForScrollTo = '#TurnToContent'; // use this one if qa widget is in page
        var idForScrollTo = '#TurnToChatterContent'; // use this one if checkout chatter widget is in page

        // this implementation assumes the widget is directly visible in page not under a tab
        TurnTojQuery('html, body').animate({
            scrollTop: TurnTojQuery(idForScrollTo).offset().top
        });
    }


    function clickReviewsTab(tab) {
        $(tab).find('a').click();
    }

    function clickReviewsTabFromTeaser() {
        var isOverlay = ${isOverlay};
        var isTurnOffCheckboxQA = ${isTurnOffCheckboxQA};

        var tab = (isOverlay || isTurnOffCheckboxQA) ? '#accessibletabsnavigation0-1' : '#accessibletabsnavigation0-2';
        clickReviewsTab(tab);
        var qaTabPos = TurnTojQuery(tab);
        window.scrollTo(0, qaTabPos.offset().top);
    }
</script>