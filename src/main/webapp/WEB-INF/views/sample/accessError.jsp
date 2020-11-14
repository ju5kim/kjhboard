<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>

<meta charset="EUC-KR">
<title>Insert title here</title>
<script type="text/javascript">
function main_go(){
location.href="/board/board_list"
}
</script>
</head>
<body>
<h1>
<c:out value="${msg}"></c:out>
</h1>
<button onclick="main_go()">메인 페이지 이동</button>
</body>
</html>