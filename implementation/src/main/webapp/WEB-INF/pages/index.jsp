<%@include file="libs.jsp" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org"
		xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"">
<title><spring:message code="pgtitle.login"/></title>
<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">

</head>
<body>
<div class="signUpLogIn">
	<c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
      <font color="red">
        <spring:message code="label.signinerror"/>
      </font>
      <c:remove scope="session" var="SPRING_SECURITY_LAST_EXCEPTION"/>
	</c:if>
		<form name='f' action="${pageContext.request.contextPath}/login" method="POST">
		<table>
			<tr>
				<td><spring:message code="label.email"/>:</td>
				<td><input type="email" id="username" name="username" /></td>
			</tr>
			<tr>
				<td><spring:message code="label.password"/>:</td>
				<td><input type="password" id="password" name="password" /></td>
			</tr>
			<tr>
				<td></td>
				<td><input name="remember-me" type="checkbox"/> <spring:message code="label.rememberme"/></td>
			</tr>
			<tr>
				<td><input name="submit" type="submit" value='<spring:message code="label.loginbttn"/>' /></td>
				<td><label><a href="${pageContext.request.contextPath}/registration"><spring:message code="label.registerbttn"/></a></label></td>			
			</tr>					
		</table>
	</form>	
</div>
</body>
</html>