<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<script type="text/javascript">
function registerform(){
	location.href="/board/register_form";
}
</script>
</head>
<body>
<!-- 
 로그인
아이디 
비밀번호
로그인 취소
네이버 로그인
카카오 로그인
회원가입
-->
<h1>로그인</h1><br>
<form action="/board/loginOK" method="post">
<input id="m_id" name="m_id" type="text"><br>
<input id="m_pw" name="m_pw" type="password"><br>
<input type="submit" id="login_btn" name="login_btn" value="로그인"><input type="reset" value="취소"><br>
</form>
<h6>네이버로그인</h6><br>
<h6>카카오로그인</h6><br>
<button id="register_btn" name="register_btn" onclick="registerform()">회원가입</button>
</body>
</html>