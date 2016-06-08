<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<template:page pageTitle="${pageTitle}">

		<div class="no-space">
			<cms:pageSlot position="Section1" var="feature">
				<cms:component component="${feature}" />
			</cms:pageSlot>

			<div class="row">
				<cms:pageSlot position="Section2A" var="feature">
					<cms:component component="${feature}" element="div" class="col-xs-6 col-sm-3" />
				</cms:pageSlot>
				<cms:pageSlot position="Section2B" var="feature">
					<cms:component component="${feature}"  element="div" class="col-xs-6 col-sm-3" />
				</cms:pageSlot>
			</div>
		</div>

		<cms:pageSlot position="Section3" var="feature">
			<cms:component component="${feature}" />
		</cms:pageSlot>

		<c:if test="${flags.get('ccPinboard').getFlag() eq 'true' and isSiteKeyInvalid eq 'false'}">
			<c:choose>
				<c:when test="${currentVersion eq '4_3'}">
					<div id="TurnToPinboardContent"></div>
				</c:when>
				<c:otherwise>
					<div id="TurnToFullComments"></div>
				</c:otherwise>
			</c:choose>

		</c:if>

		<div class="no-space">
		
			<div class="row">
				<cms:pageSlot position="Section4" var="feature" >
					<cms:component component="${feature}"  element="div" class="col-xs-6 col-sm-3"/>
				</cms:pageSlot>
			</div>
			
			<cms:pageSlot position="Section5" var="feature">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</div>
		
</template:page>
<script type="text/javascript">
	var TurnToGallerySkus = ["${product.code}"];
	var turnToConfig = {
				siteKey: "${siteKey}",
				setupType: "${flags.get('checkboxQA').getSetupType().getCode()}",
				reviewsSetupType: "${flags.get('checkboxRating').getSetupType().getCode()}",
				fullComments: {
					height: "500px",
					width: "1380px",
					titleMaxLength: 60,
					nameMaxLength: 20,
					boxWidth: "200px",
					limit: 20,
					layoutMode: "justified",
					// Always used for vertical spacing.
					// For horizontal spacing:
					// If layoutMode == "centered" then this is the exact spacing
					// If layoutMode == "justified" this value is used as the,
					spacing: "50px",
					maxDaysOld: -1
				},
				gallery: {
					// configuration options...
					// title: ‘Custom Title’ <- example
				},
				pinboard: {
					contentType: 'checkoutComments'//checkoutComments
				},
				embedCommentCapture: true,
				postPurchaseFlow: true,
				setTeaserCookieOnView: true,
				loadRteaserAfterChatter: false
			},
			TurnToChatterSku = "${product.code}",
			TurnToItemSku = "${product.code}";

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
</script>