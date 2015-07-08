<%@include file="libs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title><spring:message code="pgtitle.words"/></title>
    <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
    <script type="text/javascript" src="<c:url value="/resources/js/jquery-2.1.4.min.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/js/scripts.js" />"></script>
</head>
<body>
	<div class="fixed-nav-bar">
		<div class="left-header">
			<%@include file="header-buttons/add-word-button.jsp"%>			
		</div>
		<div class="right-header">
			<%@include file="header-buttons/header-buttons.jsp" %>
		</div>
		</div>
		<div class="wrapper">
			<form>
				<table id="words">
					<c:forEach items="${words}" var="word">
						<tr>
							<td><a href="${pageContext.request.contextPath}/get_word?wordId=${word.wordId}">${word.word}</a></td>
							<td>-</td>
							<td><a href="${pageContext.request.contextPath}/get_word?wordId=${word.wordId}">${word.translation}</a></td>
							<td><input type="button"
								value='<spring:message code="label.editwordbtn"></spring:message>' onclick="goToPage('${pageContext.request.contextPath}/edit_word?wordId=${word.wordId}')"></td>
							<td><input type="button"
								value='<spring:message code="label.deletewordbtn"></spring:message>' onclick="goToPage('${pageContext.request.contextPath}/delete_word?wordId=${word.wordId}')">
								</td>
						</tr>
					</c:forEach>
				</table>
			</form>
		</div>
</body>
</html>