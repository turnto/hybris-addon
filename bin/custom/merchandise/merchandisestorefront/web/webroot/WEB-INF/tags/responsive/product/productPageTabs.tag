<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="tabs js-tabs tabs-responsive">

    <div class="tabhead">
        <a href=""><spring:theme code="product.product.details"/></a> <span
            class="glyphicon"></span>
    </div>
    <div class="tabbody">
        <product:productDetailsTab product="${product}"/>
    </div>


    <c:if test="${flags.get('checkboxQA').getFlag() eq 'true'}">
        <div class="tabhead">
            <a href=""><spring:theme code="product.product.spec"/></a> <span
                class="glyphicon"></span>
        </div>
    </c:if>

    <div class="tabbody">
        <product:productDetailsClassifications product="${product}"/>
    </div>

    <c:if test="${flags.get('checkboxRating').getFlag() eq 'true'}">
        <div id="tabreview" class="tabhead">
            <a href=""><spring:theme code="review.reviews"/></a> <span
                class="glyphicon"></span>
        </div>
    </c:if>
    <div class="tabbody">
        <product:productPageReviewsTab product="${product}"/>
    </div>

    <cms:pageSlot position="Tabs" var="tabs">
        <cms:component component="${tabs}"/>
    </cms:pageSlot>

</div>
