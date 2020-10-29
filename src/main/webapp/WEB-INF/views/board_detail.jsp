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
	<input type="text" id="" name="" value="수정하기">
	<input type="text" id="" value="글목록으로 가기">

</body>
</html>