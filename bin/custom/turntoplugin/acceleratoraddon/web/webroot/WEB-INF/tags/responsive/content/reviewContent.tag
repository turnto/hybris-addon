<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="isOnRating" value="${flags.get('checkboxRating').getFlag() eq 'true'}"/>
<c:set var="isOverlayRating"
       value="${isOnRating and flags.get('checkboxRating').getSetupType().getCode() eq \"overlay\"}"/>
<c:set var="isOnbuyerComments" value="${flags.get('buyerComments').getFlag() eq 'true'}"/>
<c:set var="isTurnOffCheckboxQA" value="${flags.get('checkboxQA').getFlag() eq 'false'}"/>

<c:choose>
    <c:when test="${flags.get('checkboxRating').getFlag() and flags.get('checkboxRating').getSetupType().getCode() eq 'staticEmbed'}">
        ${reviewContent}
    </c:when>
    <c:when test="${flags.get('checkboxRating').getFlag() and flags.get('checkboxRating').getSetupType().getCode() ne 'overlay'}">
        <div id="TurnToReviewsContent"></div>
    </c:when>
</c:choose>

<c:if test="${isOnRating}">
    <script type="text/javascript">

        if (turnToConfig != undefined) {
            turnToConfig['reviewsSetupType'] = "${flags.get('checkboxRating').getSetupType().getCode()}";
            turnToConfig['itemInputTeaserFunc'] = customItemInputTeaserFunc;
            turnToConfig['setTeaserCookieOnView'] = true;
            turnToConfig['loadRteaserAfterChatter'] = false;
        } else {
            var turnToConfig = {
                siteKey: "${siteKey}",
                reviewsSetupType: "${flags.get('checkboxRating').getSetupType().getCode()}",
                setTeaserCookieOnView: true,
                loadRteaserAfterChatter: false
            }
        }

        var TurnToItemSku = "${product.code}";

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

        (function () {
            var t = document.createElement('script');
            t.type = 'text/javascript';
            t.async = true;
            t.src = document.location.protocol + "//static.www.turnto.com/sitedata/" + turnToConfig.siteKey + "/v${currentVersion}/" + TurnToItemSku + "/d/itemjs";
            var s = document.getElementsByTagName('script')[0];
            s.parentNode.insertBefore(t, s);
        })();


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
            var buyerComments = ${isOnbuyerComments};
            var isOnQA = ${isOnQA};

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

                if (isOnQA && commentCnt > 0) {
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
</c:if>

