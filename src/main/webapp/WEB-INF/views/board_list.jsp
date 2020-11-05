<%@page import="com.kjh.board.vo.PageVO"%>
<%@page import="com.kjh.board.vo.KjhBoardVO"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 목록</title>
<script type="text/javascript">
	function go_reg_form() {
		location.href = "/board/register_form";
		//화면이동방법
	}
	function go_login_form() {
		location.href = "/board/login_form"
	}
	function go_logout() {
		location.href = "/board/logout"
	}
	function go_mypage() {
		location.href = "/board/mypage"
	}
	function go_write_form() {
		location.href = "/board/write_form"
	}
/*
function go_board_detail(i) {
		//location.href = "/board/board_detail"
		document.detail_form.action = "/board/board_detail" + i;
		document.detail_form.submit();
	}
*/	
	
</script>
<script type="text/javascript">
	function click_tr(i){
	var form = document.createElement('form');
	var input = document.createElement('input');
	var b_num = document.getElementById('b_num'+i).innerText;
	alert("b_num 값은 ::: "+b_num);
	input.setAttribute("name","b_num");
	input.setAttribute("value", b_num);
	form.appendChild(input);
	document.body.appendChild(form);
	form.setAttribute("method","get");
	form.setAttribute("action","/board/board_detail");
	form.submit();
}
</script>
</head>
<body>
	<%
		String m_num = (String) session.getAttribute("m_num");
	%>

	<%=m_num%>
	<!-- 
게시판-  가능 메인 페이지
||글번호|| 제목(댓글 수) ||작성일|| 최종수정일 ||작성자|| 조회수
회원아니더라도 보는건 가능 하지만 상세 페이지로는 갈 수없다
페이징 기능 
검색기능
 
맨위에 로그인 버튼이 있다. 로그인 버튼과 로그인 하면 js미니 게임이 있다.
-->
	<h1 align="center">게시판 목록</h1>

	<button>js미니 게임</button>


	<%
		if (m_num != null) {
	%>

	<button id="write" name="write" onclick="go_write_form()">글쓰기버튼</button>
	<button id="mypage" name="mypage" onclick="go_mypage()">마이페이지</button>
	<button id="logout" name="logout" onclick="go_logout()">로그아웃</button>
	<%
		} else {
	%>
	<button id="login" name="login" onclick="go_login_form()">로그인</button>
	<button id="register" name="register" onclick="go_reg_form()">회원가입</button>
	<%
		}
	%>


	<form id="list_form" name="list_form">
		<table id="boardlist" border="1">
			<thead align="center">
				<tr>
					<td>글번호</td>
					<td>제목(댓글수)</td>
					<td>작성일</td>
					<td>최종수정일</td>
					<td>작성자</td>
					<td>조회수</td>
				</tr>
			</thead>

			<tbody>
				<%
					PageVO pvo = (PageVO)request.getAttribute("pvo");
					List<KjhBoardVO> list = (List) request.getAttribute("list");
				for (int i = 0; i < list.size(); i++) {
					KjhBoardVO kbvo = list.get(i);
				%>

				<tr id="board<%=i%>" onclick="click_tr(<%=i%>)">
					
					<td id="b_num<%=i%>"><%=kbvo.getB_num()%></td>
					<td><input id="b_subject" name="b_subject"
						value="<%=kbvo.getB_subject()%>" readonly="readonly"></td>
					<td><input id="b_reg_date" name="b_reg_date"
						value="<%=kbvo.getB_reg_date()%>"readonly="readonly"></td>
					<td><input id="b_update_date" name="b_update_date"
						value="<%=kbvo.getB_update_date()%>"readonly="readonly"></td>
					<td><input id="m_num" name="m_num"
						value="<%=kbvo.getM_num()%>"readonly="readonly"></td>
					<td><input id="b_counts" name="b_counts"
						value="<%=kbvo.getB_counts()%>"readonly="readonly"></td>
				</tr>
				<%
					}
				%>
			</tbody>
			<tfoot id="tfoot">
			<tr>
	
			</tr>
			
			</tfoot>
		</table>
				<%
				
			int start_page = Integer.parseInt(pvo.getStart_page());
			int end_page = Integer.parseInt(pvo.getEnd_page());
			int now_group = Integer.parseInt(pvo.getNow_group());
			int group_per_page = Integer.parseInt(pvo.getGroup_per_pages());
			
			
			if(now_group>1){
			//여기서 now_page를 계산을 해야한다.
			//1일때1 2일때11 3일때 21 4일때 31;
			//그룹이 1 일때 now_page *10 -9;
			//2 일때 now_page *10-9;
			//3 일때 now_page *10-9;
			
			%>
				<a href="/board/board_list?now_group=<%=now_group-1%>">이전</a>
			<%
			}	
			
			for(int i=start_page; i<=end_page; i++){
			%>	
				<a  href="/board/board_list?now_page=<%=i%>&now_group=<%=now_group%>" ><%=i%></a> 
			<%
			}
			
			if(end_page%group_per_page==0){
			%>
				<a href="/board/board_list?now_group=<%=now_group+1%>">다음</a>	
			<%
			}
			%>
			
	</form>
	
</body>
</html>