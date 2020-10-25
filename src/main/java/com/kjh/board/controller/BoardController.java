package com.kjh.board.controller;

import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.impl.Log4JLogger;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.kjh.board.service.BoardService;
import com.kjh.board.service.MemberService;
import com.kjh.board.vo.KjhBoardVO;
import com.kjh.board.vo.KjhMemberVO;
import com.kjh.board.vo.PageVO;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;

@Log4j
@Controller
public class BoardController {

	@Autowired
	MemberService memberservice;

	@Autowired
	BoardService boardservice;

	// 게시글 목록으로 가는 페이지
	@RequestMapping(value = "/board_list")
	public String boardlist(HttpServletRequest request, PageVO pvo) {
		System.out.println("화면에서 넘어오는 값");
		System.out.println("now_page :::" + pvo.getNow_page());
		System.out.println("now_group :::" + pvo.getNow_group());
		List<KjhBoardVO> list = boardservice.board_select_list_page(pvo);
		System.out.println("값 셋팅 실행 후");
		System.out.println(pvo.getNow_page());
		System.out.println(pvo.getNow_group());
		System.out.println();
		request.setAttribute("list", list);
		request.setAttribute("pvo", pvo);
		return "board_list";
	}

	// 로그인페이지로 이동만
	@RequestMapping(value = "/login_form", method = RequestMethod.GET)
	public String login_form() {
		return "login_form";
	}

	// 로그인 입력이 완료되면 실행
	@RequestMapping(value = "/loginOK", method = RequestMethod.POST)
	public String loginOK(KjhMemberVO kvo, HttpServletRequest request) {
		boolean result = memberservice.mem_longin(kvo, request);
		if (result) {
			System.out.println("로그인 완료");
			return "forward:/board_list";
		}
		System.out.println("로그인 실패");
		return "login_form";
	}

	// 로그아웃버튼 누르면 실행되고 이동
	@RequestMapping(value = "/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("m_num");
		return "forward:/board_list";
	}

	// 회원가입페이지 이동
	@RequestMapping(value = "/register_form", method = RequestMethod.GET)
	public String register_form() {
		return "register_form";
	}

	// 회원가입 작성 완료 후 실행될 메서드
	@RequestMapping(value = "/registerOK", method = RequestMethod.POST)
	public ModelAndView registerOK(HttpServletRequest request, KjhMemberVO kjhMemberVO) {
		ModelAndView mav = new ModelAndView();
		boolean result = memberservice.mem_insert(kjhMemberVO, request);
		if (result == true) {
			System.out.println("회원가입성공");
		} else {
			System.out.println("회원가입 실패");
			mav.setViewName("register_form");
		}
		mav.setViewName("login_form");
		return mav;
	}

	// 마이페이지 이동 하면서 값을 가지고 와서 한다.
	@RequestMapping(value = "/mypage")
	public String myPage(HttpSession session, HttpServletRequest request, KjhMemberVO kvo) {
		System.out.println("마이페이지 컨트롤러 실행");
		String m_num = (String) session.getAttribute("m_num");
		System.out.println("m_num ::: " + m_num);
		kvo.setM_num(m_num);
		kvo = memberservice.mem_select_kvo(kvo);
		request.setAttribute("kvo", kvo);
		System.out.println("mypage 컨트롤러 실행완료");
		return "mypage";
	}

	/// 마이페이지에서 회원정보 수정
	@RequestMapping(value = "/my_update")
	public String mypage_update(KjhMemberVO kjhMemberVO, HttpServletRequest request) {
		int result = memberservice.mem_update(kjhMemberVO, request);
		System.out.println("업데이트실행 후 결과 값" + result);
		request.setAttribute("result", result);
		HttpSession session = request.getSession();
		session.setAttribute("m_num", kjhMemberVO.getM_num());
		return "forward:/mypage";
	}

	// 회원 탈퇴 실행시
	@RequestMapping(value = "/my_delete")
	public String my_delete(KjhMemberVO kjhmemberVO, HttpSession session) {
		System.out.println(kjhmemberVO.getM_id());
		boolean result = memberservice.mem_delete(kjhmemberVO);
		session.invalidate();
		return "forward:/board_list";
	}

	// 글쓰기페이지 이동
	@RequestMapping(value = "/write_form")
	public String write_form() {
		return "write_form";
	}

	// 글쓰기완료되면 실행
	// 글을 쓸때 이미지를 파일을 멀티파트 리퀘스트로 
	// 파일 업로드로 파일을 서버(내컴퓨터)에 저장한다.
	// 그런데 그걸 저장하고 클라이언트 화면에서 보여지게 하기 위해서는 
	// 파일 경로와 파일 이름이 필요한데.
	// 글번호에 따라서 파일경로를 다르게 해준다.
	// 그리고 글을 먼저 입력하고 그 다음 입력된 글번호를 조회해서 다시 이미지테이블에 넣는다.
	// 그럼 이미지 테이블을 조회해서 글번호와 파일이름을 조회해서 리턴하면 클라이언트 화면에 보여질 수 있겠다. 
	//
	// 다시 한번 정리하자면
	// 클라이언트에서 넘어오는 값은 request 파일태그이름과 실제파일이름
	// 클라이언트로 보내야하는 값은 파일경로와 파일이름
	//
	// 중간에서 해야하는 작업은 생성해서 서버 저장공간에 넣는 작업, 넣은걸 조회하는 작업 
	//   이 중간 작업에서 필요한 것들이 테이블과 VO 
	// 지금 말로 다 표현을 할려니까 더 해깔린다. 내일 정리하자
	@RequestMapping(value = "/write_insert")
	public String write_insert(KjhBoardVO kbvo, MultipartHttpServletRequest mpRequest, HttpSession session) {
		List<MultipartFile> file_list = mpRequest.getFiles("file");
		String m_num = (String) session.getAttribute("m_num");
		String save_path = "c:\\spring\\" + m_num;
		int result = boardservice.board_insert(kbvo);
		return "forward:/board_list";// 글쓰기 완료되어 메인페이지로 이동시켯다.
	}

	// 글을 클릭했을때 상세페이지로 이동
	@RequestMapping(value = "/board_detail")
	public String write_detail(KjhBoardVO kbvo, HttpServletRequest request) {
		System.out.println("상세페이지 컨트롤러 :::");
		System.out.println("getB_num ::" + kbvo.getB_num());
		System.out.println("getB_subject ::" + kbvo.getB_subject());
		System.out.println("getB_content ::" + kbvo.getB_content());

		kbvo = boardservice.board_select_one(kbvo);
		request.setAttribute("kbvo", kbvo);
		System.out.println("상세페이지 컨트롤러 service :::");
		System.out.println("getB_num ::" + kbvo.getB_num());
		System.out.println("getB_subject ::" + kbvo.getB_subject());
		System.out.println("getB_content ::" + kbvo.getB_content());

		return "board_detail";
	}

	// 글 수정 시 이동
	// @RequestMapping
	public String write_update() {
		return null;
	}

	// 글 삭제
	// @RequestMapping
	public String write_delete() {

		return null;
	}

	// 댓글 달기
	// @RequestMapping
	public String reply_insert() {
		return null;
	}

}
