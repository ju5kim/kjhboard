<%@page import="com.kjh.board.vo.KjhMemberVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script type="text/javascript">
function go_board_list(){
	//둘다 세션객체값은 넘어 간다. 이전페이지 가기 했을때 조금 다를 뿐
 location.href="/board/board_list";
 //location.replace("/board/board_list");
}

function update(){ //여기서 html에 설정을 하지 않고 자바스크립트로 처리해 보았다.
	document.update_form.action="/board/my_update";
	document.update_form.submit();
}
function go_delete(){
	//location.href="/board/my_delete"; 
	//위처럼 하면 세션값은 넘어가지만  컨트롤러에 vo로 자동으로 셋팅되어 넘길려면 form태그로 넘겨야한다.
	document.update_form.action="/board/my_delete";
	document.update_form.submit();
}

$(document).ready(function(){
/*	Id의 경우는                     var 변수 이름 = document.getElementById("지정한 Id");
	태그를 이용할 땐              var 변수 이름 = document.getElementsByTagName("태그 명");
	name을 이용할 땐            var 변수 이름 = document.getElementsByName("지정한 name 명");
	
	태그에 접근해 속성값 설정시
	document.getElementById('test' ).setAttribute('href','https://kkamikoon.tistory.com' );
	$("#").attr("name","새로운네임")
	
	태그에 접근해 속성값 추출
	$("input").attr("id")
	$("#id1").attr("name")

*/	
	var result = document.getElementById("result").value;
	if(result>0){
		alert("회원정보 수정완료");
	}
		
});
</script>
<style type="text/css">
.dis {
	background-color: gray;
}
</style>
</head>
<body>
	<!-- 
마이 페이지
여기서는 세션값을 활용해서 db에서 
가입한 회원정보를 조회해서 표시해주자
마찬가지로 회원번호를 이용해서 상품내용이나 글쓴 내용을 표시해 줄 수 있을 것 이다.
DB에 저장된 값을 다시 꺼내서 출력해야한다.
 -->
	<%
		int result = 0;
	if (request.getAttribute("result") != null) {
		result = (int) request.getAttribute("result");
	}
	System.out.println("마이페이지에서 result 출력 :::" + result);

	KjhMemberVO kvo = (KjhMemberVO) request.getAttribute("kvo");
	String m_num = kvo.getM_num();
	String m_id = kvo.getM_id();
	String m_pw = kvo.getM_pw();
	String m_name = kvo.getM_name();

	String m_phone = kvo.getM_phone();
	String[] m_phon_arr = m_phone.split("-");
	String m_phone0 = m_phon_arr[0];
	String m_phone1 = m_phon_arr[1];
	String m_phone2 = m_phon_arr[2];

	String m_email = kvo.getM_email();
	String[] email_arr = m_email.split("@");
	String m_email0 = email_arr[0];
	String m_email1 = email_arr[1];

	String m_addr = kvo.getM_addr();
	String[] addr_arr = m_addr.split("/");
	String m_addr0 = addr_arr[0];
	String m_addr1 = addr_arr[1];
	String m_addr2 = addr_arr[2];

	/*리퀘스트에 값을 바인딩 했을 때 코드
	String m_num=(String)request.getAttribute("m_num");
	String m_id=(String)request.getAttribute("m_id");
	String m_pw=(String)request.getAttribute("m_pw");
	String m_name=(String)request.getAttribute("m_name");
	String m_email=(String)request.getAttribute("m_email");
	String m_addr=(String)request.getAttribute("m_addr");
	*/

	/* 에러 코드 
	kvo 객체를 사용은 사능하지만 
	속성에 값들이 바인딩 되어있지 않다.
	KjhMemberVO kvo;
	String m_num=kvo.getM_num();
	String m_id = kvo.getM_id();
	String m_pw=kvo.getM_pw();
	String m_name=kvo.getM_name();
	String m_email=kvo.getM_email();
	String m_addr=kvo.getM_addr();
	*/
	%>
	<%=session.getAttribute("m_num")%>


	<form id="update_form" name="update_form" method="post">
		<table>
			<tr>
				<td>회원번호</td>
				<td><input type="text" id="m_num" name="m_num" value=<%=m_num%>
					readonly="readonly" class="dis"></td>
			</tr>
			<tr>
				<td>아이디</td>
				<td><input type="text" id="m_id" name="m_id" value=<%=m_id%>
					readonly="readonly" class="dis"></td>
			</tr>
			<tr>
				<td>비밀번호</td>
				<td><input type="text" id="m_pw" name="m_pw" value=<%=m_pw%>
					readonly="readonly"></td>
			</tr>
			<tr>
				<td>비밀번호확인</td>
				<td><input type="text" id="m_pwr" name="m_pwr" value=<%=m_pw%>
					readonly="readonly"></td>
			</tr>
			<tr>
				<td>이름</td>
				<td><input type="text" id="m_name" name="m_name"
					value=<%=m_name%> readonly="readonly" class="dis"></td>
			</tr>
			<tr>
				<td>휴대폰</td>
				<td><input type="text" id="m_phone0" name="m_phone0"
					value=<%=m_phone0%>></td>
				<td><input type="text" id="m_phone1" name="m_phone1"
					value=<%=m_phone1%>></td>
				<td><input type="text" id="m_phone2" name="m_phone2"
					value=<%=m_phone2%>></td>
			</tr>
			<tr>
				<td>이메일</td>
				<td><input type="text" id="m_email0" name="m_email0"
					value=<%=m_email0%> readonly="readonly"></td>
			</tr>
			<tr>
				<td>이메일</td>
				<td><input type="text" id="m_email1" name="m_email1"
					value=<%=m_email1%> readonly="readonly"></td>
			</tr>
			<tr>
				<td>우편번호</td>
				<td><input type="text" id="m_addr0" name="m_addr0"
					value=<%=m_addr0%> readonly="readonly"></td>
			</tr>
			<tr>
				<td>주소</td>
				<td><input type="text" id="m_addr1" name="m_addr1"
					value=<%=m_addr1%> readonly="readonly"></td>
			</tr>
			<tr>
				<td>상세주소</td>
				<td><input type="text" id="m_addr2" name="m_addr2"
					value=<%=m_addr2%> readonly="readonly"></td>
			</tr>
		</table>
		<input type="button" value="수정" onclick="update()">
		<input type="button" value="탈퇴하기" onclick="go_delete()">
	</form>
	<br>
	<button onclick="go_board_list()">메인페이지이동</button>

	<input type="hidden" id="result" name="result" value="<%=result%>">
	
	
</body>
</html>