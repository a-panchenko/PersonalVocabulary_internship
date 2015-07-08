<%@include file="libs.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="pgtitle.signup" /></title>
<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">

</head>
<body>
<div class="signUpLogIn">
<form action="signup" method="POST">
	 <table>
		<tr>
			<td><spring:message code="label.email"/>: </td> 
			<td><input type="text" name="email" /></td>
		</tr>
		<tr>
			<td><spring:message code="label.password"/>: </td>
			<td><input type="password" name="password" /> </td>			
		</tr>
		<tr>
			<td ><input type="submit" value="<spring:message code="label.registerbttn"></spring:message>"/></td>
			<td ><label><a href="${pageContext.request.contextPath}/"><spring:message code="label.loginbttn"/></a></label></td>
		</tr>
	</table>
	</form>		
</div>
</body>
</html>