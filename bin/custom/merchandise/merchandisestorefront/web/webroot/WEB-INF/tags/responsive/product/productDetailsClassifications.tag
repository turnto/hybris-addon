<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="product-classifications">
    <c:choose>
        <c:when test="${flags.get('checkboxQA').getFlag() and flags.get('checkboxQA').getSetupType().getCode() eq 'staticEmbed' and flags.get('checkboxQA').getFlag() and isSiteKeyInvalid eq 'false'}">
            ${qaContent}
        </c:when>
        <c:when test="${flags.get('checkboxQA').getFlag() and flags.get('checkboxQA').getSetupType().getCode() ne 'overlay'}">
            <div id="TurnToContent"></div>
        </c:when>
    </c:choose>
    <%--<c:if test="${not empty product.classifications}">
        <c:forEach items="${product.classifications}" var="classification">
            <div class="headline">${classification.name}</div>
                <table class="table">
                    <tbody>
                        <c:forEach items="${classification.features}" var="feature">
                            <tr>
                                <td class="attrib">${feature.name}</td>

                                <td>
                                    <c:forEach items="${feature.featureValues}" var="value" varStatus="status">
                                        ${value.value}
                                        <c:choose>
                                            <c:when test="${feature.range}">
                                                ${not status.last ? '-' : feature.featureUnit.symbol}
                                            </c:when>
                                            <c:otherwise>
                                                ${feature.featureUnit.symbol}
                                                ${not status.last ? '<br/>' : ''}
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
        </c:forEach>
    </c:if>--%>
</div>