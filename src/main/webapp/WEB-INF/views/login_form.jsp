<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.security.SecureRandom"%>
<%@ page import="java.math.BigInteger"%>
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
<input type="hidden" name="{_csrf.parameterName}" value="$_csrf.token">
<input type="submit" id="login_btn" name="login_btn" value="로그인"><input type="reset" value="취소"><br>

</form>
	<%
					String clientId = "wIALdFClLRItjrr2Eb15";//애플리케이션 클라이언트 아이디값";
				//String redirectURI = URLEncoder.encode("http://www.sajapanda.com/spw/callback.spw", "UTF-8");// 네이버에 등록한 값 콜백화면으로 가야된다.
				String redirectURI = URLEncoder.encode("http://localhost:8080/board/callback", "UTF-8");// 네이버에 등록한 값 콜백화면으로 가야된다.
				
				SecureRandom random = new SecureRandom();
				String state = new BigInteger(130, random).toString();
				String apiURL = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
				apiURL += "&client_id=" + clientId;
				apiURL += "&redirect_uri=" + redirectURI;
				apiURL += "&state=" + state;
				session.setAttribute("state", state);
				%>
네이버 로그인
				<div class="naver_btn">
					<a href="<%=apiURL%>">
				<img height="50"
						src="http://static.nid.naver.com/oauth/small_g_in.PNG" /></a>	
				</div>


<button id="register_btn" name="register_btn" onclick="registerform()">회원가입</button>
</body>
</html>