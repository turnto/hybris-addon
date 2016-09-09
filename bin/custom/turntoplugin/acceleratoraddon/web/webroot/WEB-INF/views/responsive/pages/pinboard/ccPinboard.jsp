<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>

<template:page pageTitle="${pageTitle}">
    <c:if test="${flags.get('ccPinboard').getFlag() eq 'true'}">
        <div id="TurnToPinboardContent"></div>

        <script type="text/javascript">

            var turnToConfig = {
                siteKey: "${siteKey}",
                pinboard: {
                    contentType: 'checkoutComments',//checkoutComments
                    title: 'Customer Comments',
                    maxDaysOld: -1
                }

            };

            (function () {
                var tt = document.createElement('script');
                tt.type = 'text/javascript';
                tt.async = true;
                tt.src = document.location.protocol + "//static.www.turnto.com/traServer${currentVersion}/pinboardjs/" + turnToConfig.siteKey + "/turnto-pinboard.js";
                var s = document.getElementsByTagName('script')[0];
                s.parentNode.insertBefore(tt, s);
            })();

        </script>
    </c:if>
</template:page>
