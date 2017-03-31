<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="//ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
<c:set var="SSO" value="${flags.get('SSO').getFlag() eq 'true'}"/>
<c:if test="${SSO}">
     <script type="text/javascript">
          if (turnToConfig != undefined) {
               turnToConfig['registration'] = {
                    localGetLoginStatusFunction: localGetLoginStatusFunction,
                    localRegistrationUrl: "<c:url value="/turntoLogin"/>",
                    localGetUserInfoFunction: localGetUserInfoFunction,
                    localLogoutFunction: localLogoutFunction
               }
          } else {
               var turnToConfig = {
                    siteKey: "${siteKey}",
                    registration: {
                         localGetLoginStatusFunction: localGetLoginStatusFunction,
                         localRegistrationUrl: "<c:url value="/turntoLogin"/>",
                         localGetUserInfoFunction: localGetUserInfoFunction,
                         localLogoutFunction: localLogoutFunction

                    }
               }
          }
          var authToken = "${users.user_auth_token}";
          if (authToken == 'anonymous') {
               turnToConfig['setupType'] = "overlay";
          }

          function localGetLoginStatusFunction(callbackFn) {
               var authToken = "${users.user_auth_token}";

               if (authToken == 'anonymous') {
                    checkUser(authToken);
                    callbackFn({user_auth_token: null});
                    return;
               }
               callbackFn({user_auth_token: authToken});
          }

          function localGetUserInfoFunction(callbackFn) {
               var obj = {};
               var authToken = "${users.user_auth_token}";
               if (authToken != 'anonymous') {
                    obj.user_auth_token = authToken;
                    obj.first_name = "${users.first_name}";
                    obj.last_name = "${users.last_name}";
                    obj.email = "${users.email}";
                    obj.email_confirmed = "${users.email_confirmed}";// true or false
                    obj.nickname = "${users.nickname}";
                    obj.profile_attributes = "${users.profile_attributes}";
                    obj.issued_at = "${users.issued_at}";
               }
               callbackFn(obj, "${users.signature}");
          }

          function localLogoutFunction() {
               var url = "<c:url value="/logout"/>";
               window.location.replace(url);
          }

          function checkUser(authToken) {
               var url = "<c:url value="/sso"/>";
               $.ajax({
                    url: url,
                    dataType: 'json',
                    type: 'GET',
                    success: function (data) {
                         if (data.user_auth_token!=null && authToken != data.user_auth_token) {
                              parent.location.reload();
                         }

                    }
               });
          }
     </script>
</c:if>