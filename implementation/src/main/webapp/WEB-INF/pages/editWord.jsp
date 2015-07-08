<%@include file="libs.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title><spring:message code="pgtitle.words"/></title>
    <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
    <script type="text/javascript" src="<c:url value="/resources/js/jquery-2.1.4.min.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/js/scripts.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/js/editWordScript.js" />"></script>
</head>
<body>
<div class="fixed-nav-bar">
    <div class="left-header">
			<%@include file="header-buttons/view-all-button.jsp"%>
			<%@include file="header-buttons/delete-button.jsp"%>
		</div>
		<div class="right-header">
			<%@include file="header-buttons/header-buttons.jsp" %>
		</div>
		</div>
    <div class="wrapper">
        <form>
            <table>
                <tr>
                    <td><label><spring:message code="label.word"></spring:message>:</label></td>
                    <td><input type="text" name="word" autofocus placeholder="<spring:message code="label.word"></spring:message>"
                               value="${word.word}"></td>

                </tr>
                <tr>
                    <td><label><spring:message code="label.translation"></spring:message>:</label></td>
                    <td><input name="translation" placeholder='<spring:message code="label.translation"></spring:message>' value="${word.translation}"></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <textarea name="description" placeholder="<spring:message code="label.description"></spring:message>">${word.description}</textarea>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <textarea name="example"placeholder="<spring:message code="label.examples"></spring:message>">${word.example}</textarea>
                    </td>
                </tr>
                <c:forEach items="${word.synonyms}" var="synonym">
                    <label class="synonym">${synonym}</label>
                </c:forEach>
                <tr id="synonymTD">
                    <td id="synonym_row" colspan="2">
                        <input id="synonym_input" type="text" placeholder="<spring:message code="label.synonyms"></spring:message>">
                        <input type="button" value="+" id="addSynonymButton" onclick="addSynonym()">
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="button" value="<spring:message code="label.savewrodbtn"></spring:message>" onclick="sendUpdateWordRequest(
                                '${pageContext.request.contextPath}/update_word',
                                '${pageContext.request.contextPath}/words',
                                '${word.wordId}')">
                    </td>
                    <td>
                        <input type="button" value="<spring:message code="label.cancelbtn"></spring:message>" onclick="goToPage('${pageContext.request.contextPath}/words')">
                    </td>
                </tr>
            </table>
        </form>
    </div>
</body>
</html>