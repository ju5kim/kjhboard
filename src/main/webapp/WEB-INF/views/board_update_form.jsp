<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.kjh.board.vo.KjhBoardVO" %>
<%@ page import="com.kjh.board.vo.ImageVO" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글 수정 화면</title>
<script type="text/javascript">
function board_list_btn(){ // 글 목록 이동 
	location.href="/board/board_list";
}
function board_update_btn(){
	this.board.action ="/board/board_update"
	this.enctype="multipart/form-data";
	this.board.submit();
}
</script>
<script type="text/javascript">
function add_btn(){
	var count=0;
	var input = document.createElement('input');
	input.setAttribute("type","file");
	input.setAttribute("name","b_images"+count);
	input.setAttribute("value","파일업로드");
	var div = document.getElementById("div_file");
	div.appendChild(input);
}
function board_delete_btn(){
	var form = document.getElementById('board');
	form.setAttribute("action","/board/board_delete");
	document.form.submuit();
}

</script>
</head>
<body>

	<%
	System.out.println("update 화면  jsp 실행>>>> ");
	KjhBoardVO kbvo = (KjhBoardVO) request.getAttribute("kbvo");
	List list = (List) request.getAttribute("imagevo_list");
	%>
	<form name="board" id="board" enctype="multipart/form-data" method="post">
	<table border="1" >
		<tr>
			<td>글번호</td>
			<td><input id="b_num" name="b_num" value="<%=kbvo.getB_num()%>" readonly="readonly"></td>
		</tr>
		<tr>
			<td>글제목</td>
			<td><input id="b_subject" name="b_subject" value="<%=kbvo.getB_subject()%>"></td>
		</tr>
		<tr>
			<td>글내용</td>
			<td><input id="b_content" name="b_content" value="<%=kbvo.getB_content()%>"></td>
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
				<input type="button" value="첨부파일추가하기" onclick="add_btn()">
				
				<input id="image" name="image" type="file" value="파일 업로드">
				
			</td>
		
		

			<%
				}
			%>
		</tr>
		<tr>
		<td></td>
		<td><div id="div_file"> </div>
		</td>
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
	<button onclick="board_update_btn()">수정하기</button>
	<button onclick="board_delete_btn()">삭제하기</button>
	</form>
	<button onclick="board_list_btn()">글 목록으로 이동하기</button>

</body>
</html>