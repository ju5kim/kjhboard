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

<script type="text/javascript"
	src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
<script type="text/javascript">
//주소입력 api
function addrCheck(){
	var width = 500;
	var height = 600;
	daum.postcode.load(function(){
		new daum.Postcode({
			oncomplete: function(data){							
				console.log(data);
				console.log("새주소값 >>> : " + data.address);
				console.log("옛주소값 >>> : "+data.jibunAddress);
				console.log("빌딩값 >>> : " + data.buildingName);	
				document.register_form.m_addr0.value = data.zonecode;							
				document.register_form.m_addr1.value = data.address;
				document.register_form.m_addr2.value = data.buildingName;
			}	
		}).open({
			left: (window.screen.width / 2) - (width / 2),
			top: (window.screen.height / 2) - (height / 2),
		});						
	});				
}
</script>
<script type="text/javascript">
function main_go(){
	location.href="/board/board_list"
}
</script>
</head>
<body>

<h1>회원가입</h1>
<form id="register_form" name="register_form" method="post">
<table>
<tr> <td>
아이디 <td><input id="m_id" name="m_id" type="text"><br>
</tr>
<tr> <td> 
비밀번호 <td><input id="m_pw" name="m_pw" type="text"><br>
</tr>
<tr>
<td>비밀번호확인   <td><input id="m_pwr" name="m_pwr" type="text"><br>
</tr>
<tr> <td>
이름 <td><input id="m_name" name="m_name" type="text"><br>
</tr>
<tr>
<td>휴대폰<td><input id="m_phone0" name="m_phone0" type="text">-<input id="m_phone1" name="m_phone1" type="text">-<input id="m_phone2" name="m_phone2" type="text"><br> </td>
</tr>
<tr>
<td>이메일주소<td><input id="m_email0" name="m_email0" type="text">@<input id="m_email1" name="m_email1" type="text"> <br></td>
</tr>
<tr><td>
우편번호 <td><input id="m_addr0" name="m_addr0"><br><input type="button" value="주소검색" onclick="addrCheck()" class="btn"/><br>
</tr>
<tr><td>
주소<td><input id="m_addr1" name="m_addr1"><br></td>
</tr>
<tr><td>
상세주소<td><input id="m_addr2" name="m_addr2"><br></td>
</tr>
</table>
<button onclick="regbtn()"> 가  입 </button><button>취  소</button>
</form>
<br>
<button onclick="main_go()">메인화면으로</button>
</body>
</html>