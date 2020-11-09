<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<h1>사용자 정의 로그인 페이지</h1>
	<h1>
		<c:out value="${error}"></c:out>
		<c:out value="${logout}"></c:out>
	</h1>
	<%String requestAuthType = request.getAuthType();
	System.out.println(requestAuthType);
	System.out.println(request.getPathInfo());
	System.out.println(request.getContextPath());
	System.out.println(request.getQueryString());
	System.out.println(request.getRequestURL());//외부경로
	System.out.println(request.getRequestURI());//내부경로?
	
	%>
	<form method="post" action="/board/sample/admin">
		<div>
			<input type="text" name="username" value="admin">
		</div>
		<div>
			<input type="text" name="password" value="admin">
		</div>
		<div>
			<input type="submit" name="submit">
		</div>
		<div>
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
		</div>
	</form>
</body>
</html>