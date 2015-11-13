var turnToConfig = {
        siteKey: "2qtC5sJ5gVYcfvesite",
        setupType: "staticEmbed",
        embedCommentCapture: true,
        postPurchaseFlow: true,
        itemInputTeaserFunc: customItemInputTeaserFunc,
        reviewsTeaserFunc: customReviewsTeaser,
        setTeaserCookieOnView: true,
        reviewsSetupType: "staticEmbed"
    },
    TurnToItemSku = getSKU();


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