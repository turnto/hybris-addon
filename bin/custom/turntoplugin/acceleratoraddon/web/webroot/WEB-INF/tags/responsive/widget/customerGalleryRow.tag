<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="isOnCustomerGalleryRowWidget" value="${flags.get('customerGalleryRowWidget').getFlag() eq 'true'}"/>

<c:if test="${isOnCustomerGalleryRowWidget}">
    <div id="TurnToGalleryContent"></div>
</c:if>

<c:if test="${isOnCustomerGalleryRowWidget}">
    <script type="text/javascript">
        var TurnToGallerySkus = ["${product.code}"];
        if (turnToConfig != undefined) {
            turnToConfig['gallery'] = {
                // configuration options...
                // title: ‘Custom Title’ <- example
            }
        } else {
            var turnToConfig = {
                siteKey: "${siteKey}",
                gallery: {
                    // configuration options...
                    // title: ‘Custom Title’ <- example
                }
            }
        }

        if (${isOnCustomerGalleryRowWidget and currentVersion eq "4_3"}) {
            (function () {
                var gallery = document.createElement('script');
                gallery.type = 'text/javascript';
                gallery.async = true;
                gallery.src = document.location.protocol + "//static.www.turnto.com/traServer${currentVersion}/galleryjs/" + turnToConfig.siteKey + "/turnto-gallery.js/en_US";

                var s = document.getElementsByTagName('script')[0];
                s.parentNode.insertBefore(gallery, s);
            })();
        }

    </script>
</c:if>