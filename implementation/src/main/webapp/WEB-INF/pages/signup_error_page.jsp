<%@include file="libs.jsp" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><spring:message code="pgtitle.signuperror" /></title>
<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
</head>

<body>
	<div class="signUpLogIn">
		<p align="center">
			<h1><spring:message htmlEscape="false" code="signuperror.text"
				text="default text" /></h1>
		</p>


		<c:choose>
			<c:when test="${IllegalArgumentException=='true'}">
				<div id="illegal_error">
					<p align="center">
						<spring:message code="signuperror.IllegalArgumentException">
						</spring:message>
					</p>
				</div>
			</c:when>

			<c:when test="${UserNotFoundException=='true'}">
				<div id="notfound_error">
					<p align="center">
						<spring:message code="signuperror.UserNotFoundException">
						</spring:message>
					</p>
				</div>
			</c:when>
			
			<c:when test="${UsernameNotFoundException=='true'}">
				<div id="notfound_error">
					<p align="center">
						<spring:message code="signuperror.UserNotFoundException">
						</spring:message>
					</p>
				</div>
			</c:when>

			<c:when test="${DataAccessException=='true'}">
				<div id="db_error">
					<p align="center">
						<spring:message code="signuperror.DataAccessException">
						</spring:message>
					</p>
				</div>
			</c:when>
			
			<c:when test="${SignupException=='true'}">
				<div id="db_error">
					<p align="center">
						<spring:message code="signuperror.SignupException">
						</spring:message>
					</p>
				</div>
			</c:when>

			<c:otherwise>
				<div id="error">
					<p align="center">${errMsg}</p>
				</div>
			</c:otherwise>
		</c:choose>

		<p align="center">
			<spring:message htmlEscape="false" code="signuperror.text2"
				text="default text" />
		</p>
		<p align="right">
			 <a href="${pageContext.request.contextPath}"><spring:message htmlEscape="false" code="label.backbttn" /></a>
		</p>
	</div>
</body>
</html>
