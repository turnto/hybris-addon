<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>

<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
</head>

<div class="col-md-9 col-lg-10">
    <div class="results">
        <h1><spring:theme code="search.page.searchText" arguments="${searchPageData.freeTextSearch}"/></h1>
    </div>

    <nav:searchSpellingSuggestion spellingSuggestion="${searchPageData.spellingSuggestion}"/>

    <nav:pagination top="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}"
                    searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}"
                    numberPagesShown="${numberPagesShown}"/>
    <ul class="product-listing product-list">
        <c:forEach items="${searchPageData.results}" var="product">
            <div> <img src="${commonResourcePath}/images/${rating[product.code]}.png"></div>
            <product:productListerItem product="${product}"/>
        </c:forEach>
    </ul>

    <div id="addToCartTitle" style="display:none">
        <div class="add-to-cart-header">
            <div class="headline">
                <span class="headline-text"><spring:theme code="basket.added.to.basket"/></span>
            </div>
        </div>
    </div>

    <nav:pagination top="false" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}"
                    searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}"
                    numberPagesShown="${numberPagesShown}"/>
</div>