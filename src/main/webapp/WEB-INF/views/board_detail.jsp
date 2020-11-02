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
	/*function reply_re_form(i){
		
	var reply_table = document.getElementById('reply_table');
	var reply_re_tr = document.getElementById('reply_re'+i); // tr태그

	var reply_re_tr_td0 = document.createElement('td0');
	reply_re_tr.appendChild(reply_re_tr_td0);
	var reply_re_tr_td = document.createElement('td');
	reply_re_tr.appendChild(reply_re_tr_td);
	
	var reply_re_tr_td_form = document.createElement('form');
	reply_re_tr_td_form.setAttribute("action","/board/reply_re_insert");
	reply_re_tr_td.appendChild(reply_re_tr_td_form);
	
	var reply_re_tr_td_form_input = document.createElement('input');
//	document.reply_re_tr_td_form_input.setAttribute("type", "text"); 이렇게 하면 에러 난다.
	reply_re_tr_td_form_input.setAttribute("type", "text");
	reply_re_tr_td_form_input.setAttribute("name", "c_content");
	reply_re_tr_td_form.appendChild(reply_re_tr_td_form_input);
	
	
	//글 해당 글에서 회원값
	var reply_re_tr_td_form_input_m_num = document.createElement('input');
	reply_re_tr_td_form_input_m_num.setAttribute("type", "hidden");
	reply_re_tr_td_form_input_m_num.setAttribute("name", "c_content");
	reply_re_tr_td_form.appendChild(reply_re_tr_td_form_input_m_num);
	
	
	
	var reply_re_tr_td_form_submit = document.createElement('input');
	reply_re_tr_td_form_submit.setAttribute("type","submit");
	reply_re_tr_td_form_submit.setAttribute("value","등록하기");
	reply_re_tr_td_form.appendChild(reply_re_tr_td_form_submit);
	}
	*/
	
	function reply_re_form(i){
	/*	var reply_re_form_tag = document.getElementById('reply_re_form_tag'+i);
		var c_content = reply_re_form_tag.getElementById('c_content');
		var submit = reply_re_form_tag.getElementById('submit');
		c_content.setAttribute("type","text");
		submit.setAttribute("type","submit");
		 에러 reply_re_form_tag.getElementById is not a function
	*/	
	
		//var reply_re_form_tag = document.getElementById('reply_re_form_tag'+i);
		//reply_re_form_tag.c_content.type  = "text";  에러 c_content가 Cannot read property 'c_content' of undefined
		//var c_content = document.reply_re_form_tag.getElementByName('c_content'); 에러 Cannot read property 'getElementByName' of undefined
		//var c_content = reply_re_form_tag.getElementsByTagName("c_content");
		//var submit = reply_re_form_tag.getElementsByTagName("submit");
	
//		c_content.type = "text";
//		submit.type ="submit";
		//var submit = reply_re_form_tag.submit;
		//c_content.setAttribute("type","text");
		
		//submit.setAttribute("type","submit");
		//위에는 다 에러
			alert(">>>>>");
		var hidden_td = document.getElementById('hidden_td'+i);
			hidden_td.removeAttribute("hidden");
			alert(c_content);
		var input = document.forms['reply_re_form_tag'+i].elements['c_content'];
		input.setAttribute("type","text");
		var submit = document.forms['reply_re_form_tag'+i].elements['submit'];
		submit.setAttribute("type","submit");
	
	
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
	<table border="1">
		<tr>
			<td>글번호</td>
			<td><%=kbvo.getB_num()%></td>
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
					<img alt="표시불가"
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
	<button>수정하기</button>
	<button>글 목록으로 이동하기</button>
	<br>
	<h3>댓글 쓰기</h3>
	<%
		String m_num = (String) session.getAttribute("m_num");
	if (m_num != "") {
	%>
	<form id="" name="" action="/board/reply_insert">
		내정보 : <input type="text" id="m_num" name="m_num" value="<%=m_num%>"
			readonly="readonly"> <input type="text" id="c_content"
			name="c_content"> <input type="submit" id="submit"
			name="submit" value="등록"> <input type="hidden" id="b_num"
			name="b_num" value="<%=kbvo.getB_num()%>">
	</form>

	<%
		}
	%>
	<br>
	<div>댓글목록</div>
	<table border="1" id="reply_table">
		<tr>
			<td>작성자</td>
			<td>댓글내용</td>
			<td>작성일자</td>
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
			String c_num = commentsVO.getC_c_num();
		%>
		<tr onclick="reply_re_form(<%=i%>)">
			<td><%=commentsVO.getM_num()%></td>
			<td><%=commentsVO.getC_content()%></td>
			<td><%=commentsVO.getC_reg_date()%></td>
		</tr>
		<tr>
			<td hidden="true" id="hidden_td<%=i%>">
				<form id="reply_re_form_tag<%=i%>" name="reply_re_form_tag<%=i%>"
					action="/board/reply_re_insert">
					<input type="hidden" name="m_num" value="<%=m_num%>">
					<!-- 현재 로그인한 세션의 회원번호 -->
					<input type="hidden" name="b_num" value="<%=b_num%>"> <input
						type="hidden" name="c_num" value="<%=c_num%>"> <input
						type="hidden" name="c_content" id="c_content"> <input
						type="hidden" name="submit" id="submit" value="등록하기">
				</form>
			</td>
		</tr>
		<%
			}
		}
		}
		%>

	</table>
	제목표시하기-제목을 눌렀을 때 대댓글이 있다면 아래에 늘어나서 표시되게 한다.
</body>
</html>