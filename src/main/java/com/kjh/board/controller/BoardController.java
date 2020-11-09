package com.kjh.board.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
		List<KjhBoardVO> list = boardservice.board_select_list_page(pvo);
		request.setAttribute("list", list);
		request.setAttribute("pvo", pvo);
		return "board_list";
	}

	// 로그인페이지로 이동만
	@RequestMapping(value = "/login_form")
	public String login_form() {
		return "login_form";
	}

	// 로그인 입력이 완료되면 실행
	@RequestMapping(value = "/loginOK", method = RequestMethod.POST)
	public String loginOK(KjhMemberVO kvo, HttpServletRequest request) {
		boolean login_result = memberservice.mem_longin(kvo, request);
		if (login_result) {
			return "forward:/board_list";
		}
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
		boolean mem_insert_result = memberservice.mem_insert(kjhMemberVO, request);
		if (mem_insert_result) {
		} else {
			mav.setViewName("register_form");
		}
		mav.setViewName("login_form");
		return mav;
	}

	// 마이페이지 이동 하면서 값을 가지고 와서 한다.
	@RequestMapping(value = "/mypage")
	public String myPage(HttpSession session, HttpServletRequest request, KjhMemberVO kvo) {
		String m_num = (String) session.getAttribute("m_num");
		kvo.setM_num(m_num);
		kvo = memberservice.mem_select_kvo(kvo);
		request.setAttribute("kvo", kvo);
		return "mypage";
	}

	/// 마이페이지에서 회원정보 수정
	@RequestMapping(value = "/my_update")
	public String mypage_update(KjhMemberVO kjhMemberVO, HttpServletRequest request) {
		int result = memberservice.mem_update(kjhMemberVO, request);
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

	@RequestMapping(value = "/write_insert", method = RequestMethod.POST)
	public String write_insert(MultipartHttpServletRequest multipartRequest, HttpSession session)
			throws IllegalStateException, IOException {
	
		KjhBoardVO kbvo;
		ImageVO imagevo;
		String m_num = (String) session.getAttribute("m_num");

		kbvo = boardservice.kbvo_setting(multipartRequest);
		kbvo = boardservice.board_insert_select(kbvo);// 처음에 인서트하고 해당 글번호로 조회한 값을 조회해서 kbvo에 담아서 리턴한다.
		String b_num = kbvo.getB_num();
		List<ImageVO> imageVO_list = new ArrayList<ImageVO>();
		imageVO_list = boardservice.imagevo_setting(multipartRequest, b_num);
		imageVO_list = boardservice.image_insert(imageVO_list, multipartRequest);
		multipartRequest.setAttribute("kbvo", kbvo);
		multipartRequest.setAttribute("imagevo_list", imageVO_list);
		return "forward:/board_detail"; // 컨트롤러로 이동
	}

	// 글을 클릭했을때 상세페이지로 이동
	@RequestMapping(value = "/board_detail", method = { RequestMethod.POST, RequestMethod.GET })
	public String write_detail(HttpServletRequest request) throws Exception {
		KjhBoardVO kbvo = (KjhBoardVO) request.getAttribute("kbvo");// 글쓰기 완료 후 넘어온다.
		if (kbvo == null || kbvo.getB_num() == "") { // 글번호 누른 경우,댓글을 등록한 경우, 대댓글을 등록한 경우,
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
			//b_num : 글번호
			//c_num : 댓글번호
			//글에 대한 댓글목록 출력하기
			List<CommentsVO> reply_list = commentservice.reply_select_all(b_num);
			//대댓글을 담을 리스트선언 //  
			List<List<CommentsVO>> reply_re_list = new ArrayList<List<CommentsVO>>();
			for (int i = 0; i < reply_list.size(); i++) {
				CommentsVO commentsVO = reply_list.get(i);
				List<CommentsVO> list = commentservice.reply_re_select_All(commentsVO);
				reply_re_list.add(list);
			}
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

		File image_file = new File("c:\\spring\\" + b_num + "\\" + image_file_name);
		if (image_file.exists()) {
			File thumb = create_thumb(image_file,image_file_name,b_num);
			FileInputStream fileInputStream = new FileInputStream(thumb);
			OutputStream outputStream = response.getOutputStream();
			file_out_put(fileInputStream, outputStream);
		}
	}
	
	public File create_thumb(File image_file,String image_file_name,String b_num) throws IOException {
			int index = image_file_name.indexOf(".");
			String subst_file_name = image_file_name.substring(0, index);
			File thumb = new File("c:\\spring\\" + b_num + "\\thumb\\" + subst_file_name);
			thumb.getParentFile().mkdirs();
			Thumbnails.of(image_file).size(150, 150).outputFormat("png").toFile(thumb);
			thumb = new File("c:\\spring\\" + b_num + "\\thumb\\" + subst_file_name + ".png");
			return thumb;
	}
	
	public void file_out_put(FileInputStream fileInputStream,OutputStream outputStream) throws IOException {
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
	
	// 글 수정 페이지로 이동
	@RequestMapping(value = "/board_update_form")
	public String board_update_form(HttpServletRequest request, String b_num) {
		KjhBoardVO kbvo = new KjhBoardVO();
		kbvo.setB_num(b_num);
		kbvo = boardservice.board_select_one(kbvo);
		List<ImageVO> imagevo_list = boardservice.select_image(b_num);
		if (imagevo_list.size() > 0) { //이미지가 있는 경우
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

	@RequestMapping(value = "/board_update")
	public String board_update(MultipartHttpServletRequest multipartRequest, HttpSession session)
			throws IllegalStateException, IOException {
		KjhBoardVO kbvo = boardservice.kbvo_setting(multipartRequest);
		boardservice.board_update(kbvo);
		kbvo = boardservice.board_select_one(kbvo);
		
		//글번호와 파일 이름이 imageVO에 셋팅되어 list에 담긴다.
		List imageVO_list = boardservice.imagevo_setting(multipartRequest, kbvo.getB_num());
		imageVO_list = boardservice.image_update(imageVO_list, multipartRequest);
		boardservice.select_image(kbvo.getB_num());
		// 이미지 테이블에 업데이트하고 파일업로드까지 완료
		multipartRequest.setAttribute("kbvo", kbvo);
		multipartRequest.setAttribute("imagevo_list", imageVO_list);
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
		commentsVO = commentservice.reply_insert(commentsVO);
		return "forward:/board_detail";
	}
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
		commentsVO = commentservice.reply_re_insert(commentsVO);
		return "forward:/board_detail";
	}

	// 대댓글 조회
	public String reply_re_select_All(CommentsVO commentsVO) {
		// 받아야하는게 b_num 과 c_num이다.
		List<CommentsVO> reply_re_list = commentservice.reply_re_select_All(commentsVO);
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
				// 여기서 데이터가 없다면 회원가입을 하고
				// 데이터가 있다면 회원 가입을 하지않는다.
				kvo.setM_id(id);
				kvo.setM_name(name);
				kvo.setM_email(email);

//					nsVO=memberservice.naver_id_db_chek(mvo); 
				String m_num = naverloginservice.naver_id_db_chek(kvo);
				log.info("DB에서 ID체크하고 나온 값 ::: " + m_num);
				// 기존에 회원가입이 되어 있다면
				if (m_num != null) {
					HttpSession session = request.getSession();
					session.setAttribute("m_num", m_num);

				} else {// 회원가입이 되어 있지 않다면
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
