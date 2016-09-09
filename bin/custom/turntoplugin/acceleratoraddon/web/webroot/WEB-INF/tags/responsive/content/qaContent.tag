<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="https://static.www.turnto.com/css/teasers/inputteasers.css"/>

<c:set var="isOnQA" value="${flags.get('checkboxQA').getFlag() eq 'true'}" scope="application"/>
<c:set var="isOverlay" value="${isOnQA and flags.get('checkboxQA').getSetupType().getCode() eq \"overlay\"}"
       scope="application"/>

<c:choose>
    <c:when test="${flags.get('checkboxQA').getFlag() and flags.get('checkboxQA').getSetupType().getCode() eq 'staticEmbed' and flags.get('checkboxQA').getFlag() and isSiteKeyInvalid eq 'false'}">
        ${qaContent}
    </c:when>
    <c:when test="${flags.get('checkboxQA').getFlag() and flags.get('checkboxQA').getSetupType().getCode() ne 'overlay'}">
        <div id="TurnToContent"></div>
    </c:when>
</c:choose>

<c:if test="${isOnQA}">
    <script type="text/javascript">

        if (turnToConfig != undefined) {
            turnToConfig['setupType'] = "${flags.get('checkboxQA').getSetupType().getCode()}";
            turnToConfig['itemInputTeaserFunc'] = customItemInputTeaserFunc;
            turnToConfig['setTeaserCookieOnView'] = true;
            turnToConfig['loadRteaserAfterChatter'] = false;
        } else {
            var turnToConfig = {
                siteKey: "${siteKey}",
                setupType: "${flags.get('checkboxQA').getSetupType().getCode()}",
                itemInputTeaserFunc: customItemInputTeaserFunc,
                setTeaserCookieOnView: true,
                loadRteaserAfterChatter: false
            }
        }

        var TurnToItemSku = "${product.code}";

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

    </script>
</c:if>

