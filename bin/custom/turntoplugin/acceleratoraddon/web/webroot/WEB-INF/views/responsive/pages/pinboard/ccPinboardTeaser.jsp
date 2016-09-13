<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>

<c:set var="isOnCcPinboardTeaser" value="${flags.get('ccPinboardTeaser').getFlag() eq 'true'}"/>
<c:if test="${isOnCcPinboardTeaser}">
    <div id="TurnToCommentsPinboardTeaser"></div>
</c:if>

<c:if test="${isOnCcPinboardTeaser}">
    <script type="text/javascript">
        var turnToConfig = {
            siteKey: "${siteKey}",
            commentsPinboardTeaser: {
                maxDaysOld: -1,
                promoPlacement: 'left-bottom',
                promoButtonURL:'https://v6-turnto.zaelab.com/store/pinboard/checkout_comments'
            }
        };

        (function () {
            var tt = document.createElement('script');
            tt.type = 'text/javascript';
            tt.async = true;
            tt.src = document.location.protocol + "//static.www.turnto.com/traServer${currentVersion}/pinboardteaserjs/" + turnToConfig.siteKey + "/turnto-pinboard-teaser.js/en_US";
            var s = document.getElementsByTagName('script')[0];
            s.parentNode.insertBefore(tt, s);
        })();

    </script>
</c:if>
