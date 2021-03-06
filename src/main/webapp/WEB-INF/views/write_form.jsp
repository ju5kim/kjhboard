<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>새글입력창</title>
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
	
	function click_main(){
		location.href="/board/board_list"
	}

</script>
</head>
<body>
	<!--
  새글 입력창
파일 업로드를 올릴 수 있다.
사진을 업로드할 수 있다.
글을 쓰기위해선 로그인을 해야한다.
사용자가 입력할 것들
글제목
글내용
파일업로드
-->
	<%
		String m_num= 
		(String) session.getAttribute("m_num");
	%>
	<%=m_num%>

	<form method="post" action="${contextPath}/board/write_insert" enctype="multipart/form-data" >
	<!-- 	
		<form method="post" action="${contextPath}/study/upload"
		enctype="multipart/form-data">
		 -->
		 <br>
		 <br>
		<table id="" border="1">
			<tr>
				<td width="200" align="center">글 제목</td>
				<td><input type="text" id="b_subject" name="b_subject" style="text-align:left; width:500px;"></td>
			</tr>
			<tr>
				<td width="200" align="center">글 내용</td>
				<td><input type="text" id="b_content" name="b_content" style="text-align:left; width:500px; height:200px; letter-spacing: 1px"></td>
			</tr>
			<tr>
				<td colspan="2"><input type="button" value="첨부파일추가하기" onclick="add_btn()"></td>
	
			</tr>
			<tr id="">
				<td><input type="file" id="b_images" name="b_images"
					value="첨부파일선택">
				<div id="div_file"></div>
				</td>
			</tr>
		</table>
		<br>
		<input type="submit" id="submit_btn" name="submit_btn" value="글쓰기완료">
		<input type="reset" id="reset_btn" name="reset_num_btn" value="취소">
		<input type="hidden" id="m_num" name="m_num" value="<%=m_num%>">
	</form>
	<button onclick="click_main()">메인으로 돌아가기</button>
</body>
</html>