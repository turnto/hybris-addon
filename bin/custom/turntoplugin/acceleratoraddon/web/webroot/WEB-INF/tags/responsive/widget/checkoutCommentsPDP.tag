<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="isOnTurntoCheckoutChatter" value="${flags.get('turntoCheckoutChatter').getFlag() eq 'true'}"/>

<c:if test="${flags.get('turntoCheckoutChatter').getFlag() eq 'true'}">
    <span id="TurnToChatterContent"></span>
</c:if>

<c:if test="${isOnTurntoCheckoutChatter}">
    <script type="text/javascript">
        if (turnToConfig != undefined) {
            turnToConfig['chatter'] = {
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
            }
        } else {
            var turnToConfig = {
                siteKey: "${siteKey}",
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
                }
            }
        }

        var TurnToChatterSku = "${product.code}";

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
    </script>
</c:if>