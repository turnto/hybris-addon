<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<script src="//ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>--%>
<c:set var="SSO" value="${flags.get('SSO').getFlag() eq 'true'}"/>
<c:if test="${SSO}">
    <script type="text/javascript">
        if (turnToConfig != undefined) {
            turnToConfig['registration'] = {
                localGetLoginStatusFunction: localGetLoginStatusFunction,
                localRegistrationUrl: "<c:url value="/turntoLogin"/>",
                <%--localRegistrationUrl: "<c:url value="/login"/>",--%>
                localGetUserInfoFunction: localGetUserInfoFunction,
                localLogoutFunction: localLogoutFunction
            }
            turnToConfig['eventHandlers'] = {
                reviewSubmit: function () {
                     location.reload();
                }
            }
        } else {
            var turnToConfig = {
                siteKey: "${siteKey}",
                registration: {
                    localGetLoginStatusFunction: localGetLoginStatusFunction,
                    <%--localRegistrationUrl: "<c:url value="/login"/>",--%>
                    localRegistrationUrl: "<c:url value="/turntoLogin"/>",
                    localGetUserInfoFunction: localGetUserInfoFunction,
                    localLogoutFunction: localLogoutFunction

                },
                eventHandlers: {
                    reviewSubmit: function () {
                       location.reload();
                    }

                }
            }
        }
        var authToken = "${users.user_auth_token}";

        function localGetLoginStatusFunction(callbackFn) {
            var url = "<c:url value="/sso"/>";
            $.ajax({
                url: url,
                dataType: 'json',
                type: 'GET',
                success: function (data) {
                    if(data.user_auth_token) {
                        callbackFn({user_auth_token: data.user_auth_token});
                    } else {
                        callbackFn({user_auth_token: null});
                    }
                }
            });
        }

        function localGetUserInfoFunction(callbackFn) {
            var auth = getAuth();
            var url = "<c:url value="/ssoData"/>";
            if (auth) {
                jQuery.post(url).success(function(data) {
                    var obj = {};
                    if (auth != 'anonymous') {
                        obj.user_auth_token = data.user_auth_token;
                        obj.first_name = data.first_name;
                        obj.last_name = data.last_name;
                        obj.email = data.email;
                        obj.email_confirmed = data.email_confirmed; // true or false
                        obj.nickname = data.nickname;
                        obj.profile_attributes = data.profile_attributes;
                        obj.issued_at = data.issued_at;
                    }
                    callbackFn(obj,  data.signature);

                });

            }
        }

        function getAuth() {
            var result="";
            var url = "<c:url value="/sso"/>";
            $.ajax({
                url:url,
                async: false,
                success:function(data) {
                    result = data;
                }
            });
            return result;
        }

        function localLogoutFunction() {
            <%--var url = "<c:url value="/logout"/>";--%>
            <%--window.location.replace(url);--%>
        }

    </script>
</c:if>