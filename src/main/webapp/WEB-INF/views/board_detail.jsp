<%@page import="java.io.OutputStream"%>
<%@page import="com.kjh.board.vo.CommentsVO"%>
<%@page import="java.awt.font.ImageGraphicAttribute"%>
<%@page import="com.kjh.board.vo.ImageVO"%>
<%@page import="java.util.Enumeration"%>
<%@ page import="com.kjh.board.vo.KjhBoardVO"%>
<%@ page import="java.util.List"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글 상세페이지</title>
<script type="text/javascript">
	function reply_re_form(i){

		alert(" 자바 스크립트 처음 실행 시 click_count ::: " +click_count);
		var input = document.forms['reply_re_form_tag'+i].elements['click_count'];
		var click_count = input.getAttribute("value");
		alert(" 태그에서 getAttribute 한 click_count ::: " +click_count);
		
		if(click_count % 2 > 0 ){
			alert("클릭 카운트가 음수인 경우 ::: ");
			var hidden_tr = document.getElementById('hidden_tr'+i);
			hidden_tr.removeAttribute("hidden");
			var hidden_reply_re_list = document.getElementById('hidden_reply_re_list'+i);
			hidden_reply_re_list.removeAttribute("hidden");
			
			
			
			var input = document.forms['reply_re_form_tag'+i].elements['c_content'];
			input.setAttribute("type","text");
			var input2 = document.forms['reply_re_form_tag'+i].elements['m_num'];
			input2.setAttribute("type","text");
			var submit = document.forms['reply_re_form_tag'+i].elements['submit'];
			submit.setAttribute("type","submit");
			
		}else{
			alert(">>>>>");
			alert("클릭카운트가 양수 인 경우 :::");
			var hidden_tr = document.getElementById('hidden_tr'+i);
			hidden_tr.setAttribute("hidden","true");
			var hidden_reply_re_list = document.getElementById('hidden_reply_re_list'+i);
			hidden_reply_re_list.setAttribute("hidden","true");
			
			var input = document.forms['reply_re_form_tag'+i].elements['c_content'];
			input.setAttribute("type","hidden");
			var input2 = document.forms['reply_re_form_tag'+i].elements['m_num'];
			input2.setAttribute("type","hidden");
			var submit = document.forms['reply_re_form_tag'+i].elements['submit'];
			submit.setAttribute("type","hidden");
		}
		click_count++;
		var input = document.forms['reply_re_form_tag'+i].elements['click_count']
		input.setAttribute("value",click_count); //해당 태그에 값을 ++ 했다.
		alert("자바스크립트 종료 후 클릭 카운트  :::: "+click_count);
	}
</script>
<script type="text/javascript">
function board_list_btn(){ // 글 목록 이동 
	location.href="/board/board_list";
}
function board_update_btn(){
	//location.href="/board/board_update";
	this.board.action ="/board/board_update_form"
	this.board.submit();
}
function login_btn(){
	location.href="/board/login_form";
}
</script>

</head>
<body>
	<!-- 
게시판에서 해당 글을 클릭하면 보이는 페이지
구현 기능
1.댓글기능
2.글쓴사람은 해당 페이지 수정 삭제 가능
 -->
	<%
		System.out.println("디테일 jsp 실행>>>> ");
	KjhBoardVO kbvo = (KjhBoardVO) request.getAttribute("kbvo");
	List list = (List) request.getAttribute("imagevo_list");
	%>
	<form name="board">
	<table border="1" >
		<tr>
			<td>글번호</td>
			<td><input id="b_num" name="b_num" value="<%=kbvo.getB_num()%>" readonly="readonly"></td>
		</tr>
		<tr>
			<td>글제목</td>
			<td><%=kbvo.getB_subject()%></td>
		</tr>
		<tr>
			<td>글내용</td>
			<td><%=kbvo.getB_content()%></td>
		</tr>
		<tr>
			<td>이미지</td>
			<%
				for (int i = 0; i < list.size(); i++) {
				ImageVO imagevo = (ImageVO) list.get(i);
				String image_file_name = imagevo.getImage_file_name();
			%>
			<td>
				<div class="result-images">
					<img alt="표시불가" id="<%=image_file_name%>" name="<%=image_file_name%>"
						src="${contextPath}/board/download?image_file_name=<%=image_file_name%>&b_num=<%=kbvo.getB_num()%>">
				</div>
			</td>

			<%
				}
			%>
		</tr>
		<tr>
			<td>작성자</td>
			<td><%=kbvo.getM_num()%></td>
		</tr>
		<tr>
			<td>작성일</td>
			<td><%=kbvo.getB_reg_date()%></td>
		</tr>
	</table>

	<%
	String m_num = (String) session.getAttribute("m_num");
	String m_num2 = kbvo.getM_num();
	if(m_num2!=null&&m_num!=null){

		if(m_num.equals(m_num2)){
		%>
	<button onclick="board_update_btn()">글 수정 페이지로 이동</button>	
		<%
			}
	}else{
		m_num2="";
	}
	%>
	
	</form>
	<button onclick="board_list_btn()">글 목록으로 이동</button>
	<br>
	<h3>댓글 쓰기</h3>
	<%
	
	if (m_num != ""&&m_num!=null) {
	%>
	<form id="" name="" action="/board/reply_insert">
		내정보 : <input type="text" id="m_num" name="m_num" value="<%=m_num%>"
			readonly="readonly"> <input type="text" id="c_content"
			name="c_content"> <input type="submit" id="submit"
			name="submit" value="등록"> <input type="hidden" id="b_num"
			name="b_num" value="<%=kbvo.getB_num()%>">
	</form>

	<%
		}else{
			%>
			회원으로 로그인 해야 댓글을 달 수 있습니다.
		<button id="login_btn" onclick="login_btn()">로그인</button>
			<%
			
		}
	%>
	<br>
	<div>댓글목록</div>
	<table border="1" id="reply_table">
		<tr>
			<td align="center">작성자</td>
			<td align="center">댓글내용</td>
			<td align="center">작성일자</td>
		</tr>
		<%
			List reply_list = (List) request.getAttribute("reply_list");
		if (reply_list != null) {
			if (reply_list.size() > 0) { //댓글 목록 출력
		%>


		<%
			for (int i = 0; i < reply_list.size(); i++) {//출력된 댓글들 중 하나 클릭하면  대댓글 여부 체크하고 출력
			CommentsVO commentsVO = (CommentsVO) reply_list.get(i);
			String b_num = commentsVO.getB_num();
			String c_num = commentsVO.getC_num();
			String c_c_num = commentsVO.getC_c_num();
		%>
		<tr onclick="reply_re_form(<%=i%>)">
			<td><%=commentsVO.getM_num()%></td>
			<td><%=commentsVO.getC_content()%></td>
			<td><%=commentsVO.getC_reg_date()%></td>
		</tr>
		<tr hidden="true" id="hidden_tr<%=i%>">
			<td colspan="3">
				<form id="reply_re_form_tag<%=i%>" name="reply_re_form_tag<%=i%>"
					action="/board/reply_re_insert">
					<input type="hidden" name="m_num" value="<%=m_num%>"
						readonly="readonly">
					<!-- 현재 로그인한 세션의 회원번호 -->
					<input type="hidden" name="b_num" value="<%=b_num%>"> <input
						type="hidden" name="c_num" value="<%=c_num%>"> <input
						type="hidden" name="c_content" id="c_content"> <input
						type="hidden" name="submit" id="submit" value="등록하기"> <input
						type="hidden" name="click_count" id="click_count" value="1">
				</form>
			</td>
		</tr>
		<tr id="hidden_reply_re_list<%=i%>" hidden="hidden">
			<td></td>
			<td colspan="3"><table border="1">
					<tr>
						<td align="center">작성자 </td>
						<td align="center">내용 </td>
						
					</tr>
					<%
						List reply_re_list = (List) request.getAttribute("reply_re_list");
					List inner_list = (List) reply_re_list.get(i);
					for (int j = 0; j < inner_list.size(); j++) {
						CommentsVO commentsVO3 = (CommentsVO) inner_list.get(j);
					%>
					<tr>
						<td><%=commentsVO3.getM_num()%></td>
						<td><%=commentsVO3.getC_content()%></td>
					</tr>
					<%
						}
					%>
					<!--여기서 jsp for문 돌려서 조회한 값 보이게 하기  -->
				</table></td>
		</tr>
		<%
			}
		}
	}
		%>
	</table>
</body>
</html>