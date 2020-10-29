package com.kjh.board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

import com.kjh.board.HomeController;
import com.kjh.board.service.BoardService;
import com.kjh.board.service.MemberService;
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

	// 게시글 목록으로 가는 페이지
	@RequestMapping(value = "/board_list")
	public String boardlist(HttpServletRequest request, PageVO pvo) {
		log.info("화면에서 넘어오는 값");
		log.info("now_page :::" + pvo.getNow_page());
		log.info("now_group :::" + pvo.getNow_group());
		List<KjhBoardVO> list = boardservice.board_select_list_page(pvo);
		log.info("값 셋팅 실행 후");
		log.info(pvo.getNow_page());
		log.info(pvo.getNow_group());
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
	 * @RequestMapping(value = "/write_insert", method = RequestMethod.POST) //
	 * public String write_insert(MultipartHttpServletRequest
	 * multipartRequest,KjhBoardVO kbvo,) { public String
	 * write_insert(MultipartHttpServletRequest multipartRequest, HttpSession
	 * session) { // List<MultipartFile> file_list = mpRequest.getFiles("file");
	 * ModelAndView mav = new ModelAndView(); log.info("글쓰기 컨트롤러 시작"); Map map = new
	 * HashMap(); String m_num = (String) session.getAttribute("m_num"); KjhBoardVO
	 * kbvo = new KjhBoardVO(); Enumeration enumer =
	 * multipartRequest.getParameterNames(); while (enumer.hasMoreElements()) { //
	 * 파라미터 들을 돌면서 그 값들을 vo에 담아야하는데. 지금 vo가 하나면 되는데 하나가 아니다. String name = (String)
	 * enumer.nextElement(); String value = multipartRequest.getParameter(name);
	 * map.put(name, value); } kbvo.setB_subject((String) map.get("b_subject"));
	 * kbvo.setB_content((String) map.get("b_content")); kbvo.setM_num((String)
	 * map.get("m_num")); kbvo = boardservice.board_insert_select(kbvo);
	 * multipartRequest.setAttribute("kbvo", kbvo); // mav.addObject("kbvo",kbvo);
	 * // mav.setViewName("/board_detail");// 바로 화면으로 전송 //
	 * mav.setViewName("/board/board_detail"); get으로 보낼때 return
	 * "forward:/board_detail"; // 다시 글 쓰기가 완료되면 글 상세페이지로 이동하고 글 상세페이지에서 글 수정 및 삭제를
	 * 한다. }
	 */
	@RequestMapping(value = "/write_insert", method = RequestMethod.POST)
	public String write_insert(MultipartHttpServletRequest multipartRequest, HttpSession session)
			throws IllegalStateException, IOException {
		log.info("글쓰기 컨트롤러 시작");
		String m_num = (String) session.getAttribute("m_num");
		KjhBoardVO kbvo;
		ImageVO imagevo;
		List imagevo_list = new ArrayList();
		kbvo = boardservice.kbvo_setting(multipartRequest);
		kbvo = boardservice.board_insert_select(kbvo);// 처음에 인서트하고 해당 글번호로 조회한 값을 조회해서 kbvo에 담아서 리턴한다.
		String b_num = kbvo.getB_num();
		imagevo_list = boardservice.imagevo_setting(multipartRequest, b_num);
		imagevo_list = boardservice.image_insert(imagevo_list, multipartRequest);

		multipartRequest.setAttribute("kbvo", kbvo);
		multipartRequest.setAttribute("imagevo_list", imagevo_list);
		return "forward:/board_detail";
	}

	/*
	 * //글쓰기 되면서 파일 업로드 파일 업로드 public ResponseEntity
	 * board_insert_with_file(MultipartHttpServletRequest multipartRequest,
	 * HttpServletResponse response) throws IllegalStateException, IOException {
	 * List image_list = new ArrayList();
	 * multipartRequest.setCharacterEncoding("UTF-8"); Map<String, Object> board_map
	 * = new HashMap<String, Object>(); Enumeration enu =
	 * multipartRequest.getParameterNames(); // 파라미터들을 map에 넣기 위해서 while
	 * (enu.hasMoreElements()) { // 이넘에 남아있는 동안 돌아간다. String name = (String)
	 * enu.nextElement(); String value = multipartRequest.getParameter(name);
	 * board_map.put(name, value); } List image_real_file_names =
	 * file_upload(multipartRequest); board_map.put("image_real_file_names",
	 * image_real_file_names);
	 * 
	 * String b_num = (String) board_map.get("b_num"); String message;
	 * 
	 * ResponseEntity resentt = null; HttpHeaders respons_header = new
	 * HttpHeaders(); respons_header.add("Content-Type", "text/html;charset=utf-8");
	 * try { // boardservice.modArticle(board_map); if (image_real_file_names !=
	 * null && image_real_file_names.size() != 0) { File scr_file = new
	 * File("임시저장소의 이미지 파일full"); File dest_dir = new File("실제 저장할 폴더+글번호폴더");
	 * FileUtils.moveFileToDirectory(scr_file, dest_dir, true);
	 * 
	 * String original_file_name = (String) board_map.get("");// 지금 맵안에 list가 들어가
	 * 있다.ㄴ File old_file = new File("실제정소+글번호+이미지 파일full"); old_file.delete(); }
	 * message = "<script>"; message += " alert('완료');"; message +=
	 * " location.href='" + multipartRequest.getContextPath() +
	 * "/board/kjhboard?articleNO=" + b_num + "';"; message += " </script>"; resentt
	 * = new ResponseEntity(message, respons_header, HttpStatus.CREATED); } catch
	 * (Exception e) { File srcFile = new File("임시저장소 이미지 파일full");
	 * srcFile.delete(); message = "<script>"; message += " alert('오류가 발생했습니다.');";
	 * message += " location.href='" + multipartRequest.getContextPath() +
	 * "/board/kjhboard?articleNO=" + b_num + "';"; message += " </script>"; resentt
	 * = new ResponseEntity(message, respons_header, HttpStatus.CREATED); } return
	 * resentt; }
	 * 
	 * public List file_upload(MultipartHttpServletRequest request) throws
	 * IllegalStateException, IOException { List list = new ArrayList(); Iterator
	 * iterator = request.getFileNames(); String save_path =
	 * "c\\kjhboard\\images\\"; // 여기서 글번호를 받아서 추가해야한다. while (iterator.hasNext()) {
	 * String file_tag_name = (String) iterator.next(); MultipartFile multi_file =
	 * request.getFile(file_tag_name); String orig_file_name =
	 * multi_file.getOriginalFilename(); list.add(orig_file_name); File f = new
	 * File(save_path + orig_file_name);
	 * 
	 * if (multi_file.getSize() != 0) { if (!f.exists()) {
	 * f.getParentFile().mkdirs(); } multi_file.transferTo(f); // 이걸 임시 저장소를 추가해서
	 * 이미지 파일을 생성했다? } } return list; }
	 */
	// 글을 클릭했을때 상세페이지로 이동
	@RequestMapping(value = "/board_detail", method = { RequestMethod.POST, RequestMethod.GET })
//	public String write_detail(KjhBoardVO kbvo, HttpServletRequest request) { // 매개변수를 vo를 받으면 get이 자꾸 작동해서 에러난거임
	public String write_detail(HttpServletRequest request) {
		KjhBoardVO kbvo = (KjhBoardVO) request.getAttribute("kbvo");
		if (kbvo == null) {
			String b_num = (String) request.getParameter("b_num");
			kbvo = new KjhBoardVO();
			kbvo.setB_num(b_num);
			kbvo = boardservice.board_select_one(kbvo);
			List<ImageVO> list = boardservice.select_image(b_num);
			request.setAttribute("imagevo_list", list);
			request.setAttribute("kbvo", kbvo);
		}
		
		
		return "board_detail";
	}

	@RequestMapping(value = "/download")
	public void file_down_send(@RequestParam("image_file_name") String image_file_name,
			@RequestParam("b_num") String b_num, HttpServletResponse response) throws IOException {
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
