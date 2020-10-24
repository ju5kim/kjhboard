<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<script type="text/javascript">
	function regbtn(){
		var val_pw = document.m_pw;
		var val_pwr= document.m_pwr;
		if(val_pw != val_pwr){
			alert("아이디가 같지 않습니다.");
		}else{
			
			document.register_form.action="/board/registerOK";
			document.register_form.submit();	
		}						
	
		
	}
	
</script>
</head>
<body>

<h1>회원가입</h1>
<form id="register_form" name="register_form" method="post">
아이디<input id="m_id" name="m_id" type="text"><br>
비밀번호<input id="m_pw" name="m_pw" type="text"><br>
비밀번호확인 <input id="m_pwr" name="m_pwr" type="text"><br>
이름<input id="m_name" name="m_name" type="text"><br>
휴대폰<input id="m_phone0" name="m_phone0" type="text">-<input id="m_phone1" name="m_phone1" type="text">-<input id="m_phone2" name="m_phone2" type="text"><br>
이메일주소<input id="m_email0" name="m_email0" type="text">@<input id="m_email1" name="m_email1" type="text"> <br><br>
우편번호<input id="m_addr0" name="m_addr0"><br>
주소<input id="m_addr1" name="m_addr1"><br>
상세주소<input id="m_addr2" name="m_addr2"><br>
<button onclick="regbtn()">가입</button><button>취소</button>
</form>
<button>메인화면으로</button>
</body>
</html>