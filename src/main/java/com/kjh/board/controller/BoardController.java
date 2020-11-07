package com.kjh.board.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.kjh.board.service.BoardService;
import com.kjh.board.service.CommentService;
import com.kjh.board.service.MemberService;
import com.kjh.board.service.NaverLoginServicImpl;
import com.kjh.board.util.SHA256Util;
import com.kjh.board.vo.CommentsVO;
import com.kjh.board.vo.ImageVO;
import com.kjh.board.vo.KjhBoardVO;
import com.kjh.board.vo.KjhMemberVO;
import com.kjh.board.vo.PageVO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnails;

@Log4j
@Controller
public class BoardController {
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);
	@Autowired
	MemberService memberservice;

	@Autowired
	BoardService boardservice;

	@Autowired
	CommentService commentservice;

	@Autowired
	NaverLoginServicImpl naverloginservice;

	@Autowired
	SHA256Util sha256Util;

	// 게시글 목록으로 가는 페이지
	@RequestMapping(value = "/board_list")
	public String boardlist(HttpServletRequest request, PageVO pvo) {
//		log.info("화면에서 넘어오는 값");
//		log.info("now_page :::" + pvo.getNow_page());
//		log.info("now_group :::" + pvo.getNow_group());
		List<KjhBoardVO> list = boardservice.board_select_list_page(pvo);
//		log.info("값 셋팅 실행 후");
//		log.info(pvo.getNow_page());
//		log.info(pvo.getNow_group());
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
			log.info("로그인 완료");
			return "forward:/board_list";
		}
		log.info("로그인 실패");
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

		String salt = sha256Util.generateSalt();
		kjhMemberVO.setSalt(salt);
		String pw_sha = sha256Util.getEncrypt(kjhMemberVO.getM_pw(), salt);
		kjhMemberVO.setM_pw(pw_sha);

		boolean result = memberservice.mem_insert(kjhMemberVO, request);
		if (result == true) {
			log.info("회원가입성공");
		} else {
			log.info("회원가입 실패");
			mav.setViewName("register_form");
		}
		mav.setViewName("login_form");
		return mav;
	}

	// 마이페이지 이동 하면서 값을 가지고 와서 한다.
	@RequestMapping(value = "/mypage")
	public String myPage(HttpSession session, HttpServletRequest request, KjhMemberVO kvo) {
		log.info("마이페이지 컨트롤러 실행");
		String m_num = (String) session.getAttribute("m_num");
		log.info("m_num ::: " + m_num);
		kvo.setM_num(m_num);
		kvo = memberservice.mem_select_kvo(kvo);
		request.setAttribute("kvo", kvo);
		log.info("mypage 컨트롤러 실행완료");
		return "mypage";
	}

	/// 마이페이지에서 회원정보 수정
	@RequestMapping(value = "/my_update")
	public String mypage_update(KjhMemberVO kjhMemberVO, HttpServletRequest request) {
		int result = memberservice.mem_update(kjhMemberVO, request);
		log.info("업데이트실행 후 결과 값" + result);
		request.setAttribute("result", result);
		HttpSession session = request.getSession();
		session.setAttribute("m_num", kjhMemberVO.getM_num());
		return "forward:/mypage";
	}

	// 회원 탈퇴 실행시
	@RequestMapping(value = "/my_delete")
	public String my_delete(KjhMemberVO kjhmemberVO, HttpSession session) {
		log.info(kjhmemberVO.getM_id());
		boolean result = memberservice.mem_delete(kjhmemberVO);
		session.invalidate();
		return "forward:/board_list";
	}

	// 글쓰기페이지 이동
	@RequestMapping(value = "/write_form")
	public String write_form() {
		return "write_form";
	}

	/*
	 * // 글쓰기완료되면 실행 // 글을 쓸때 이미지를 파일을 멀티파트 리퀘스트로 // 파일 업로드로 파일을 서버(내컴퓨터)에 저장한다. //
	 * 그런데 그걸 저장하고 클라이언트 화면에서 보여지게 하기 위해서는 // 파일 경로와 파일 이름이 필요한데. // 글번호에 따라서 파일경로를
	 * 다르게 해준다. // 그리고 글을 먼저 입력하고 그 다음 입력된 글번호를 조회해서 다시 이미지테이블에 넣는다. // 그럼 이미지 테이블을
	 * 조회해서 글번호와 파일이름을 조회해서 리턴하면 클라이언트 화면에 보여질 수 있겠다. // // 다시 한번 정리하자면 // 클라이언트에서
	 * 넘어오는 값은 request 파일태그이름과 실제파일이름 // 클라이언트로 보내야하는 값은 파일경로와 파일이름 // // 중간에서 해야하는
	 * 작업은 생성해서 서버 저장공간에 넣는 작업, 넣은걸 조회하는 작업 // 이 중간 작업에서 필요한 것들이 테이블과 VO // 글쓰기시 DB에
	 * 글 테이블과 이미지 테이블에 저장되어야한다. 그리고 해당 글번호를 받아와서 서버에 폴더를 글번호로 저장되게 한다.
	 * 
	 * 그럼 글울 조회할때(글상세페이지) 해당 글테이블과 이미지 테이블을 정보를 가지고 온다. 그 정보들을 이용해서 글과 이미지를 표시한다.
	 * 
	 * 음... vo를 2개만들까? 아니면 하나의 vo에 다 담아서 표시할까? 글 하나에는 vo가 하나인것도 나쁘지 않을 거 같다. 그런데
	 * 이미지가 여러개이면 이미지 테이블 값도 여러개 니까 유동적으로 되야한다. vo안에다가 list를 담으로면 어떨까? 이미지파일을 넣을 떄
	 * 이미지 이름에 글번호를 넣는것은 어떤가? 이미지 이름을 글번호와 날짜를 추가하면 데이터를 알아보기가 더 쉬울거 같기는 하다.
	 * 
	 * 나는 일단 vo를 2개 만들어서 작업을 하는 것을 해보자.
	 * 
	 */
	/*
	 * 파라미터 들을 돌면서 그 값들을 vo에 담아야하는데. 지금 vo가 하나면 되는데 하나가 아니다. //
	 * mav.setViewName("/board_detail");// 바로 화면으로 전송 //
	 * mav.setViewName("/board/board_detail"); get으로 보낼때 return
	 * "forward:/board_detail"; // 다시 글 쓰기가 완료되면 글 상세페이지로 이동하고 글 상세페이지에서 글 수정 및 삭제를
	 * 한다. }
	 */
	@RequestMapping(value = "/write_insert", method = RequestMethod.POST)
	public String write_insert(MultipartHttpServletRequest multipartRequest, HttpSession session)
			throws IllegalStateException, IOException {
		log.info("글쓰기 컨트롤러 시작 :::");
		String m_num = (String) session.getAttribute("m_num");
		KjhBoardVO kbvo;
		ImageVO imagevo;

		kbvo = boardservice.kbvo_setting(multipartRequest);
		kbvo = boardservice.board_insert_select(kbvo);// 처음에 인서트하고 해당 글번호로 조회한 값을 조회해서 kbvo에 담아서 리턴한다.
		String b_num = kbvo.getB_num();
		List<ImageVO> imageVO_list = new ArrayList();
		imageVO_list = boardservice.imagevo_setting(multipartRequest, b_num);
		for (ImageVO imageVO : imageVO_list) {
			log.info("imagevo_setting 후imageVO_list에 있는 값들:::: ");
			log.info(imageVO.getB_num());
			log.info(imageVO.getImage_file_name());
		}

		imageVO_list = boardservice.image_insert(imageVO_list, multipartRequest);

		multipartRequest.setAttribute("kbvo", kbvo);
		multipartRequest.setAttribute("imagevo_list", imageVO_list);
		return "forward:/board_detail"; // 컨트롤러로 이동
	}

	// 글을 클릭했을때 상세페이지로 이동
	@RequestMapping(value = "/board_detail", method = { RequestMethod.POST, RequestMethod.GET })
//	public String write_detail(KjhBoardVO kbvo, HttpServletRequest request) { // 매개변수를 vo를 받으면 get이 자꾸 작동해서 에러난거임
	public String write_detail(HttpServletRequest request) throws Exception {
		log.info("board_detail 컨트롤러 시작 :::");
		KjhBoardVO kbvo = (KjhBoardVO) request.getAttribute("kbvo");// 글쓰기 완료 후 넘어온다.

		if (kbvo == null || kbvo.getB_num() == "") { // 글번호 누른 경우,댓글을 등록한 경우, 대댓글을 등록한 경우,
			log.info("kbvo로 값이 넘어오지 않았음 ::: ");
			String b_num = (String) request.getParameter("b_num");
			kbvo = new KjhBoardVO();
			kbvo.setB_num(b_num);
			kbvo = boardservice.board_select_one(kbvo);
			List<ImageVO> imagevo_list = boardservice.select_image(b_num);
			// 0번 인덱스의 값으로 나머지 인덱스를 돌면서 값을 비교한다. 그런데 이것도 2중 for문으로 하면 뭔가 될거 같은데? 비교 알고리즘?
			if (imagevo_list != null && imagevo_list.size() > 0) {
				String image_file_name = imagevo_list.get(0).getImage_file_name();
				for (int i = 1; i < imagevo_list.size(); i++) {
					String image_file_name2 = imagevo_list.get(i).getImage_file_name();
					if (image_file_name.equals((image_file_name2))) {
						imagevo_list.remove(i);
					}
				}
			}
			List<CommentsVO> reply_list = commentservice.reply_select_all(b_num);// 댓글 목록 모두 출력하기
			List<List<CommentsVO>> reply_re_list = new ArrayList<List<CommentsVO>>();
			for (int i = 0; i < reply_list.size(); i++) {
				CommentsVO commentsVO = (CommentsVO) reply_list.get(i);
				List list = commentservice.reply_re_select_All(commentsVO);
				reply_re_list.add(list);
			}

//대댓글을 조회 할려면 b_num과 c_num값이 있어야 한다.List reply_re_list=commentservice.reply_re_select_All(commentsVO);
			request.setAttribute("reply_re_list", reply_re_list);
			request.setAttribute("reply_list", reply_list);
			request.setAttribute("imagevo_list", imagevo_list);
			request.setAttribute("kbvo", kbvo);
		}

		return "board_detail";
	}

	// 글 상세 페이지에서 이미지를 출력하는 컨트롤러 여기서 썸네일 이미지도 저장한다.
	@RequestMapping(value = "/download")
	public void file_down_send(@RequestParam("image_file_name") String image_file_name,
			@RequestParam("b_num") String b_num, HttpServletResponse response) throws IOException {
		log.info("file_down_send 컨트롤러 실행 ::::");
		log.info("이미지파일 b_num ::: " + b_num);
		log.info("이미지파일 파일 네임 :::" + image_file_name);

		OutputStream outputStream = response.getOutputStream();
		File image_file = new File("c:\\spring\\" + b_num + "\\" + image_file_name);
		if (image_file.exists()) {
			int index = image_file_name.indexOf(".");
			String subst_file_name = image_file_name.substring(0, index);
			File thumb = new File("c:\\spring\\" + b_num + "\\thumb\\" + subst_file_name);
			thumb.getParentFile().mkdirs();
			Thumbnails.of(image_file).size(150, 150).outputFormat("png").toFile(thumb);
			thumb = new File("c:\\spring\\" + b_num + "\\thumb\\" + subst_file_name + ".png");
			FileInputStream fileInputStream = new FileInputStream(thumb);
			byte[] buffer = new byte[1024 * 8];
			while (true) {
				int count = fileInputStream.read(buffer);
				if (count == -1) {
					break;
				}
				outputStream.write(buffer, 0, count);
			}
			fileInputStream.close();
			outputStream.close();
		}
		log.info("file_down_send 컨트롤러 종료 ::::");
	}

	// 글 수정 페이지로 이동
	@RequestMapping(value = "/board_update_form")
	public String board_update_form(HttpServletRequest request, String b_num) {
		log.info("board_update_form 컨트롤러 실행 ::: 화면이동만 ");
		KjhBoardVO kbvo = new KjhBoardVO();
		kbvo.setB_num(b_num);
		kbvo = boardservice.board_select_one(kbvo);
		List<ImageVO> imagevo_list = boardservice.select_image(b_num);
		if (imagevo_list.size() > 0) { // 이 if문을 쓰는 이유는 이미지가 없는 글일때 조회시 에러가 나기때문
			String image_file_name = imagevo_list.get(0).getImage_file_name();
			for (int i = 1; i < imagevo_list.size(); i++) {
				String image_file_name2 = imagevo_list.get(i).getImage_file_name();
				if (image_file_name.equals(image_file_name2)) {
					imagevo_list.remove(i);
				}
			}

		}

		request.setAttribute("kbvo", kbvo);
		request.setAttribute("imagevo_list", imagevo_list);
		return "board_update_form";
	}

	// 문제 발생
	//
	@RequestMapping(value = "/board_update")
	public String board_update(MultipartHttpServletRequest multipartRequest, HttpSession session)
			throws IllegalStateException, IOException {
		log.info("boar_update 컨트롤러 실행 :::::");
		KjhBoardVO kbvo = boardservice.kbvo_setting(multipartRequest);
		boardservice.board_update(kbvo);
		kbvo = boardservice.board_select_one(kbvo);

		List imageVO_list = boardservice.imagevo_setting(multipartRequest, kbvo.getB_num());
		// 여기서는 글번호와 파일 이름이 imageVO에 셋팅되어 list에 담긴다.
		ImageVO imageVO = (ImageVO) imageVO_list.get(0);
		imageVO = (ImageVO) imageVO_list.get(0);
		log.info("board_update 실행시 " + imageVO.getB_num());
		log.info("board_update 실행시 " + imageVO.getImage_file_name());

		imageVO_list = boardservice.image_update(imageVO_list, multipartRequest);
		boardservice.select_image(kbvo.getB_num());
		imageVO = (ImageVO) imageVO_list.get(0);
		log.info("board_update 실행시 " + imageVO.getB_num());
		log.info("board_update 실행시 " + imageVO.getImage_file_name());
		// 이미지 테이블에 업데이트하고 파일업로드까지 완료
		multipartRequest.setAttribute("kbvo", kbvo);
		multipartRequest.setAttribute("imagevo_list", imageVO_list);
		log.info("boar_update 컨트롤러 종료 :::::");
		return "board_update_form";
	}

	// 글 삭제
	@RequestMapping(value = "/board_delete")
	public String board_delete(String b_num) {
		KjhBoardVO kbvo = new KjhBoardVO();
		kbvo.setB_num(b_num);
		boardservice.board_delete(kbvo);
		return "forward:/board_list";
	}

	// 댓글 달기
	@RequestMapping(value = "/reply_insert")
	public String reply_insert(CommentsVO commentsVO, HttpServletRequest request, HttpSession session) {

		log.info("reply_insert 컨트롤러 시작 ::: ");
		String b_num = commentsVO.getB_num();// 최상위 글 번호
		String c_c_num = commentsVO.getC_c_num(); // 대 댓글 번호//평범하게 입력시 대 댓글은 null
		String m_num = commentsVO.getM_num();//
		String c_content = commentsVO.getC_content();//
		log.info("commentsVO.b_num ::: " + b_num);
		log.info("commentsVO.c_c_num ::: " + c_c_num);
		log.info("commentsVO.m_num ::: " + m_num);
		log.info("commentsVO.c_content ::: " + c_content);

//		여기서 주의 해야 하는게 form 태그의 내부에 값들만 넘어오고 화면에 있던 request객체는 넘어오지 않았다.
//		KjhBoardVO kbvo = (KjhBoardVO) request.getAttribute("kbvo");
//		log.info(kbvo.getB_num()); // 넘어 오지 않았다.
//		log.info(kbvo.getM_num());

		commentsVO = commentservice.reply_insert(commentsVO);

		log.info("서비스 실행 후값 ::: ");
		b_num = commentsVO.getB_num();// 최상위 글 번호
		c_c_num = commentsVO.getC_c_num(); // 대 댓글 번호//평범하게 입력시 대 댓글은 null
		m_num = commentsVO.getM_num();//
		c_content = commentsVO.getC_content();//
		log.info("commentsVO.b_num ::: " + b_num);
		log.info("commentsVO.c_c_num ::: " + c_c_num);
		log.info("commentsVO.m_num ::: " + m_num);
		log.info("commentsVO.c_content ::: " + c_content);

		return "forward:/board_detail"; // 이렇게 넘기면 commentsVO값이 넘어 가는 거 겠지?
		/*
		 * 화면이동은 ajax로 하는게 맞는거 같은데 일단 여기서는 댓글을 등록하고 나면 detail화면에서 다시 이동해서 댓글들이 나온다.
		 */
	}

	// 댓글 조회 한것을 위에 detail 컨트롤러에 적용시킴
	/*
	 * @RequestMapping public String reply_select_all(HttpServletRequest request) {
	 * String b_num = (String) request.getAttribute("b_num"); List reply_list =
	 * commentservice.reply_select_all(b_num);// 댓글 목록 모두 출력하기
	 * request.setAttribute("reply_list", reply_list); return null; }
	 */

	// 댓글 수정
	public String reply_update() {

		return null;
	}

	// 댓글 삭제
	public String reply_delete() {
		return null;
	}

	// 대댓글 달고
	@RequestMapping(value = "/reply_re_insert")
	public String reply_re_insert(CommentsVO commentsVO, HttpServletRequest request, HttpSession session) {
		log.info("reply_re_insert 컨트롤러 시작 ::::");
		String b_num = commentsVO.getB_num();// 최상위 글 번호
		String c_c_num = commentsVO.getC_c_num(); // 대 댓글 번호//평범하게 입력시 대 댓글은 null
		String m_num = commentsVO.getM_num();//
		String c_content = commentsVO.getC_content();//
		String c_num = commentsVO.getC_num();
		log.info("commentsVO.b_num ::: " + b_num);
		log.info("commentsVO.c_c_num ::: " + c_c_num);
		log.info("commentsVO.m_num ::: " + m_num);
		log.info("commentsVO.c_content ::: " + c_content);
		log.info("commentsVO.c_num ::: " + c_num);

		commentsVO = commentservice.reply_re_insert(commentsVO);

		return "forward:/board_detail";
	}

	// 대댓글 조회

	public String reply_re_select_All(CommentsVO commentsVO) {
		// 받아야하는게 b_num 과 c_num이다.
		List reply_re_list = commentservice.reply_re_select_All(commentsVO);

		return "board_detail";
	}

	// 대댓글 수정
	public String reply_re_update() {
		return null;
	}

	// 대댓글 삭제
	public String reply_re_delete() {
		return null;
	}

	// ---------------네이버 로그인을 위한 ----------------------------------
	/*
	 * 설명 : 네이버 로그인 API
	 * 
	 * 주요사용기술: json-simple라이브러리 작성 법 https://developers.naver.com/docs/login/api/ 에서
	 * JSP소스 1. naverlogin.jsp 는 로그인 화면에 붙여넣기 2. callback.jsp 에서 2번째 200번 상태를 확인하는
	 * if문에서 json-simple라이브러리를 활용해서 access_token 값을 뽑아 온다. 그 값을 request에 담아 보낸다.
	 * https://developers.naver.com/docs/login/profile/ 에서 JAVA소스 3. api에서 원하는 값을 모두
	 * 넣고 main에 적힌 소스를 활용한다.
	 * 
	 * 
	 * 주의 해야 할건 여기서 활용된것을 바탕으로 통신 성공을 하면 String 타입으로 값이 넘어오는데. 여기서 다시
	 * json-simple라이브러리를 활용해서 값을 뽑아온다. 그리고 DB값을 조회를 먼저 해서 값이 있다면 세션만 부여하고 넘기고 값이 없다면
	 * 회원가입을하고 세션을 부여하고 넘긴다.
	 * 
	 * 작성일: 20200820 작성자: 김주호
	 */
	@RequestMapping("/naverlogin")
	public ModelAndView naverlogin(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		// 여기서 부터 아래 responseBody 까지는 내가 하는게 아니라 api의 문서를 그대로 활용
		String access_token = (String) request.getAttribute("access_token");
		
		log.info("access_token:::" + access_token);
		String token = access_token; // 네이버 로그인 접근 토큰;
		String header = "Bearer " + token; // Bearer 다음에 공백 추가
		String apiURL = "https://openapi.naver.com/v1/nid/me";
		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("Authorization", header);
		String responseBody = get(apiURL, requestHeaders);
		log.info("responseBody ::: " + responseBody);

		// {"resultcode":"00","message":"success","response":{"id":"21746765","age":"30-39","email":"rlaj005@naver.com","name":"\uae40\uc8fc\ud638","birthday":"10-09"}}
		// 위에 값을 콘솔로 찍어본다. name 값이 유니코드인데 브라우저에서 자동으로 변환해서 읽고 json simple 라이브러리가 변환해준다.
		JSONObject jsonObject = new JSONObject();
		JSONParser parser = new JSONParser();
		KjhMemberVO kvo = new KjhMemberVO();
		try {
			jsonObject = (JSONObject) parser.parse(responseBody);
			String resultcode = (String) jsonObject.get("resultcode");
			String message = (String) jsonObject.get("message");
			jsonObject = (JSONObject) jsonObject.get("response"); // 여기서 response가 json객체 안에 json객체이기 때문에
			String id = (String) jsonObject.get("id");
			String age = (String) jsonObject.get("age");
			String email = (String) jsonObject.get("email");
			String name = (String) jsonObject.get("name");
			String birthday = (String) jsonObject.get("birthday");

			if (resultcode.equals("00") && message.equals("success")) {
				// 통신을 성공적으로 받았다면 
				//여기서 데이터가 없다면 회원가입을 하고
				// 데이터가 있다면 회원 가입을 하지않는다.
				kvo.setM_id(id);
				kvo.setM_name(name);
				kvo.setM_email(email);
			
//					nsVO=memberservice.naver_id_db_chek(mvo); 
					String m_num = naverloginservice.naver_id_db_chek(kvo);
					log.info("DB에서 ID체크하고 나온 값 ::: "+m_num);
				// 기존에 회원가입이 되어 있다면
				if (m_num != null) {
					HttpSession session = request.getSession();
					session.setAttribute("m_num", m_num);

				} else {//회원가입이 되어 있지 않다면
					log.info("회원가입이 되어 있지 않아서 실행");
					log.info(kvo.getM_id());
					log.info(kvo.getM_name());
					log.info(kvo.getM_email());
					int result = naverloginservice.naver_insert(kvo); // DB에 넣고
					if (result > 0) {
						log.info("회원가입완료");
						KjhMemberVO resultVO;
						
						resultVO = naverloginservice.naver_login(kvo);
						log.info("인서트된거 다시 실행 ::::");
						m_num = resultVO.getM_num();
						HttpSession session = request.getSession();
						session.setAttribute("m_num", m_num);
						session.setMaxInactiveInterval(-1);// 세션 무한대
					} else {
						log.info("insert가 실행되지 않았습니다.");
					} // end of if(insert result)
				} // end of if(naverSelect mnum)
			} else {
				log.info("네이버 API에서 값을 받아오지 못했습니다. ");
			} // end of if(resultcode,massage)
		} catch (Exception e) {
			log.info("json 객체 변환실패");
			e.printStackTrace();
			mav.setViewName("login_form");
		} // end of try catch
		mav.setViewName("forward:/board_list");
		return mav;
	}// end of naverlogin controller

	@RequestMapping(value = "/callback")
	public static ModelAndView callbackPage() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("callback");
		return mav;
	}

	public static String get(String apiUrl, Map<String, String> requestHeaders) {
		HttpURLConnection con = connect(apiUrl);
		try {
			con.setRequestMethod("GET");
			for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
				con.setRequestProperty(header.getKey(), header.getValue());
			}
			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
				return readBody(con.getInputStream());
			} else { // 에러 발생
				return readBody(con.getErrorStream());
			}
		} catch (IOException e) {
			throw new RuntimeException("API 요청과 응답 실패", e);
		} finally {
			con.disconnect();
		}
	}

	public static HttpURLConnection connect(String apiUrl) {
		try {
			URL url = new URL(apiUrl);
			return (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
		} catch (IOException e) {
			throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
		}
	}

	public static String readBody(InputStream body) {
		InputStreamReader streamReader = new InputStreamReader(body);
		try (BufferedReader lineReader = new BufferedReader(streamReader)) {
			StringBuilder responseBody = new StringBuilder();
			String line;
			while ((line = lineReader.readLine()) != null) {
				responseBody.append(line);
			}
			return responseBody.toString();
		} catch (IOException e) {
			throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
		}
	}
}
