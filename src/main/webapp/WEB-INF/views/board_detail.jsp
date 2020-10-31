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
	<div>댓글목록</div>
	<br>
	<%
		String m_num = (String) session.getAttribute("m_num");
	if (m_num != "") {
	%>
	<form id="" name="" action="/board/reply_insert">
		<div><%=m_num %></div>
		<input type="text" id="comment" name="comment"> <br>
		<input			type="submit" id="" value="등록">
	</form>

	<%
		}
	%>
	<br>
	<%
		List reply_list = (List) request.getAttribute("reply_list");
	if (reply_list.size() > 0) { //댓글 목록 출력
	%>
	<table>
		<%
			for (int i = 0; i < reply_list.size(); i++) {//출력된 댓글들 중 하나 클릭하면  대댓글 여부 체크하고 출력
			CommentsVO commentsVO = (CommentsVO)reply_list.get(i);
		%>
		<tr>
			<td><div><%= commentsVO.getM_num()%></div> </td>
				<td><div><%=commentsVO.getC_content()%></div></td> <td><div><%=commentsVO.getC_reg_date()%></div><td> 제목표시하기-제목을 눌렀을 때 대댓글이 있다면 아래에 늘어나서 표시되게 한다.
		</tr>
		<%
			}
		}
		%>
	</table>
</body>
</html>