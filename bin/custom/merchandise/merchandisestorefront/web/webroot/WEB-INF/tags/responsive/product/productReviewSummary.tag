<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ attribute name="showLinks" required="false" type="java.lang.Boolean" %>
<%@ attribute name="starsClass" required="false" type="java.lang.String" %>
<%@ taglib prefix="teasers" tagdir="/WEB-INF/tags/addons/turntoplugin/responsive/teasers"%>

<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>


<div class="rating js-ratingCalc ${starsClass}" data-rating='{"rating":"${product.averageRating}","total":5}'>
	<teasers:reviewTeaser/>
<%--	<div class="rating-stars">
		<span class="js-ratingIcon glyphicon glyphicon-star"></span>
	</div>--%>


<%--	<c:if test="${not empty product.reviews}">
		<spring:theme code="review.based.on"
			arguments="${fn:length(product.reviews)}" />
	</c:if>
	<c:choose>
		<c:when test="${showLinks}" >
			<c:if test="${not empty product.reviews}">
				<a href="#tabreview" class="js-openTab"><spring:theme code="review.see.reviews" /></a>
			</c:if>
			<a href="#tabreview" class="js-writeReviewTab"><spring:theme code="review.write.title" /></a>
		</c:when>
		<c:otherwise>
			<spring:theme code="review.reviews" />
		</c:otherwise>
	</c:choose>--%>
	
</div>