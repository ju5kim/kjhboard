package com.kjh.board.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.kjh.board.dao.BoardDAO;
import com.kjh.board.vo.ImageVO;
import com.kjh.board.vo.KjhBoardVO;
import com.kjh.board.vo.PageVO;

import net.coobird.thumbnailator.Thumbnails;

@Transactional
@Service
public class BoardServiceImpl implements BoardService {
	private static final Logger log = LoggerFactory.getLogger(BoardServiceImpl.class);
	private static final String save_path_front = "c:\\spring\\";

	@Autowired
	BoardDAO boardDAO;

	@Override
	public List<KjhBoardVO> board_select_list() {
		List list = boardDAO.board_select_list();
		return list;

	}

	@Override
	public List<KjhBoardVO> board_select_list_page(PageVO pvo) {
		page_basic_setting(pvo, 10, 10);
		page_first_start(pvo); // 둘다 null인 경우
		click_next_prev_btn(pvo); // now_page가 null인 경우
		page_calculator(pvo); // 둘다 있는 경우와 함께 계산
		List<KjhBoardVO> list = boardDAO.board_select_list_page(pvo);
		// DB조회할 때는 pvo에 row만 사용
		// page값들은 셋팅되어 넘어간다.
		return list;
	}
	/*
	 * // 방금 든 생각을 말로 정리 // 클라이언트에서 // 1. 페이지 버튼을 누를 때 마다 now_group과 now_page를 동시에
	 * 주도록한 경우는 // page_first_start에서 (초창기 1,1 값 셋팅하기위한) // if문에서 now_group의 null만
	 * 체크했다. // // 그리고 페이지 버튼을 누른경우는 다른것들 안걸치고 // calculator실행 // 넥스트버튼이나 이전 버튼의 경우
	 * page값이 null // 결론 ::: 위의 경우는 조건을 3가지만 건다. // 초기화 제외하고 메서드 3가지
	 * 
	 * // 2. 페이지 버튼을 누를 때 now_page만 주도록한 경우는 // page_first_start에서 (초창기 1,1값 셋팅하기위한)
	 * // if now_page과 now_group가 모두 null인 경우만 체크한다. // // 결론 ::: 위의 경우는 조건을 4가지 건다.
	 * // 아래에 매서드를 하나더 추가해준다.
	 * 
	 * 
	 * //페이지버튼을 눌럿을때 그룹이 null만 나올때 public PageVO click_page_btn(PageVO pvo) {
	 * if(pvo.getNow_group() == null || pvo.getNow_group() == "") { //1페이지면 1그룹 1~10
	 * 올림(페이지 / (float)그룹당 페이지) //11페이지면 2그룹 11~20 올림(페이지 / (float)그룹당 페이지) int
	 * now_page = Integer.parseInt(pvo.getNow_page()); int group_per_pages =
	 * Integer.parseInt(pvo.getGroup_per_pages()); int now_group= (int)
	 * Math.ceil((now_page/(float)group_per_pages));
	 * pvo.setNow_group(String.valueOf(now_group)); } }
	 * 
	 * // 그런데 여기서 갑자기 든 생각은 // 나는 조건문을 메서드화 해서 작업을 했다. // 하나의 기능당 하나의 매서드를 해야한다고
	 * 어디선가 보았다. // 했는데 이게 맞는 건지(성능면이나 실무적인면에서) // // 아니면 그냥 조건문을 여러번 하면서 작업하는게 맞는
	 * 건지 // 아니면 작업은 그대로 조건문만 상위로 빼서 service메서드에서 // 하는게 맞는 건지 //
	 */

	public PageVO page_basic_setting(PageVO pvo, int page_per_rows, int group_per_pages) {
		pvo.setPage_per_rows(String.valueOf(page_per_rows));// 한페이지에 보여질 row
		pvo.setGroup_per_pages(String.valueOf(group_per_pages));// 한그룹에 보여질 page
		return pvo;// 이제는 null이 아니라 1, 1이다.
	}

	// 최초 조회시 셋팅
	// now_page = null , now_group = null 이 들어온다.
	public PageVO page_first_start(PageVO pvo) {
		if (pvo.getNow_group() == null || pvo.getNow_group() == "") {
			pvo.setNow_group("1");
			pvo.setNow_page("1");
		}
		return pvo;
	}

	// 다음과 이전 버튼을 눌럿을 시 셋팅
	// now_group값이 들어오지만 now_page는 null이 들어온다.
	public PageVO click_next_prev_btn(PageVO pvo) {
		if (pvo.getNow_page() == null || pvo.getNow_page() == "") {
			int now_group = Integer.parseInt(pvo.getNow_group());
			int group_per_page = Integer.parseInt(pvo.getGroup_per_pages());
			int now_page = now_group * group_per_page - (group_per_page - 1);
			pvo.setNow_page(String.valueOf(now_page));
		}
		return pvo;
	}

	// 여기서 계산되어야하는건 start_row,end_row,start_page,end_page가 계산되어야 한다.
	public PageVO page_calculator(PageVO pvo) {
		int now_page = Integer.parseInt(pvo.getNow_page());
		int now_group = Integer.parseInt(pvo.getNow_group());
		// 위에 두값은 디폴트 값이 1이 있어야 한다. 그래야 맨 처음 화면이 출력 될 것이다.
		// 디폴트 값을 vo에 자체적으로 설정해야하는지 아니면 service단에서 설정해야하는지 모르겠다.
		int pager_per_rows = Integer.parseInt(pvo.getPage_per_rows());
		int group_per_pages = Integer.parseInt(pvo.getGroup_per_pages());

		int start_row;
		int end_row;
		int real_end_row;

		end_row = now_page * pager_per_rows;
		start_row = end_row - (pager_per_rows - 1);
		real_end_row = boardDAO.board_select_all_count();
		if (end_row > real_end_row) {
			end_row = real_end_row;
		}
		pvo.setStart_row(start_row + "");
		pvo.setEnd_row(String.valueOf(end_row));

		int start_page;
		int end_page;
		int real_end_page;

		end_page = now_group * group_per_pages;
		start_page = end_page - (group_per_pages - 1);
		real_end_page = (int) Math.ceil((real_end_row / (float) pager_per_rows));
		if (end_page > real_end_page) {
			end_page = real_end_page;
		}
		pvo.setStart_page(String.valueOf(start_page));
		pvo.setEnd_page(String.valueOf(end_page));
		return pvo;
	}

	@Override
	public KjhBoardVO board_select_one(KjhBoardVO kbvo) {
		kbvo = boardDAO.board_select_one(kbvo);
		return kbvo;
	}

	@Override
	public int board_insert(KjhBoardVO kbvo) {
		int result = boardDAO.board_insert(kbvo);
		return result;
	}

	@Override
	public KjhBoardVO kbvo_setting(MultipartHttpServletRequest multipartRequest) {
		KjhBoardVO kbvo = new KjhBoardVO();
		Map map = new HashMap();
		Enumeration enumer = multipartRequest.getParameterNames();
		while (enumer.hasMoreElements()) { // 파라미터 들을 돌면서 그 값들을 vo에 담아야하는데. 지금 vo가 하나면 되는데 하나가 아니다.
			String name = (String) enumer.nextElement();
			log.info("멀티파트에 파라미터네임들 ::: " + name);
			String value = multipartRequest.getParameter(name);
			map.put(name, value);
		}
		kbvo.setB_subject((String) map.get("b_subject"));
		kbvo.setB_content((String) map.get("b_content"));
		kbvo.setM_num((String) map.get("m_num"));
		Iterator iterator = multipartRequest.getFileNames();

		return kbvo;
	}

	// 이게 일단 일반 글 테이블에 넣고(완료되었다면) (이미지파일이 있다면)이미지 테이블에도 넣고 그 다음에 파일업로드를 해야한다.
	@Override
	public List<ImageVO> imagevo_setting(MultipartHttpServletRequest multipartRequest, String b_num)
			throws IllegalStateException, IOException {
		List<ImageVO> list = new ArrayList<ImageVO>();
		ImageVO imagevo = new ImageVO();

		Iterator iterator = multipartRequest.getFileNames();
		while (iterator.hasNext()) {
			String file_name = (String) iterator.next();
			MultipartFile multipartfile = multipartRequest.getFile(file_name);
			String real_file_name = multipartfile.getOriginalFilename();
			imagevo.setImage_file_name(real_file_name); // vo에 담는것
			imagevo.setB_num(b_num);
			list.add(imagevo);// 이게 지금 반복문 밖에서 imagevo객체가있는데 list에 imagevo객체가 차례대로 제대로 담길려나?

//			File file_temp = new File(save_path_front + "temp\\" + real_file_name);
//			File file_real = new File(save_path_front+"글번호\\"+real_file_name);
//			if(multipartfile.getSize()>0) { //임시폴더에 파일을 먼저 만들고
//				file_temp.getParentFile().mkdirs();
//				multipartfile.transferTo(file_temp);
//				if(true) { // 만약 이미지테이블에 데이터가 들어 갔다면 실제 파일 경로에 저장한다..
//					file_real.getParentFile().mkdirs();
//					FileUtils.moveFileToDirectory(file_temp, file_real, true);
//					//multipartfile.transferTo(file_real);
//				}
//				file_temp.delete(); //업로드가 되었건 안되었건 임시저장소 파일은 삭제 해야한다.
//			}

		}

		return list;
	}
	@Override
	public List<ImageVO> select_image(String b_num) {
		List<ImageVO> list = boardDAO.image_select_list(b_num);

		return list;
	}

	@Override
	public List<ImageVO> image_insert(List<ImageVO> list, MultipartHttpServletRequest multipartHttpServletRequest) {
		for (int i = 0; i < list.size(); i++) {
			ImageVO imagevo = (ImageVO) list.get(i);
			int result = boardDAO.image_insert(imagevo);
			if (result > 0) {
				file_upload(imagevo, multipartHttpServletRequest);

			} else {
				log.info("이미지테이블 삽입 하지 않았습니다. ");
			}
			// 여기서 이미지 vo 리스트를 리턴하느냐 파일저장경로를 리턴하는가?
		}
		return list;
	}// 이제 여기는 테이블에 삽입이 완료된 vo만 있다.

	public void file_upload(ImageVO imagevo, MultipartHttpServletRequest multipartHttpServletRequest) {
		Iterator iterator = multipartHttpServletRequest.getFileNames();
		while (iterator.hasNext()) {
			String file_tag_name = (String) iterator.next();
			MultipartFile multipartFile = multipartHttpServletRequest.getFile(file_tag_name);
			String real_file_name = multipartFile.getOriginalFilename();
			File image_file = new File(save_path_front + imagevo.getB_num() + "\\" + real_file_name);
			if (multipartFile.getSize() > 0) { // 파일이 존재한다면
				if (!image_file.exists()) {
					image_file.getParentFile().mkdirs();
				}
				try {
					multipartFile.transferTo(image_file);
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
					log.info("파일업로드 실패");
				}
			} //

		}
	}

	@Override
	public KjhBoardVO board_insert_select(KjhBoardVO kbvo) {
		int result = boardDAO.board_insert(kbvo); // 여기서 kbvo가 셋팅퇴어 나온다.
		kbvo = boardDAO.board_select_one(kbvo);
		return kbvo;
	}

	@Override
	public KjhBoardVO board_insert_select_file(KjhBoardVO kbvo, List list) {
		int result = boardDAO.board_insert(kbvo);
		String b_num = kbvo.getB_num();
		for (int i = 0; i < list.size(); i++) {
			ImageVO imagevo = (ImageVO) list.get(i);
			imagevo.setB_num(b_num);
			int result1 = boardDAO.image_insert(imagevo);
			if (result1 > 0) { // 이미지테이블에 잘 들어갔다면 여기서 파일 업로드 하기
//				file_upload(imagevo,); 
				// 테이블에 넣고 파일업로드를 할려고 했는데 파일 업로드를 먼저하고 테이블에 넣어야겠다.
			}
		}

		// 이전에 이미지vo 파일 이름은 이미지 vo에 담겨 있어야한다.
		// 여기서 이미지vo를 활용해서 파일업로드 작업을 한다.
		// 그런데 리턴 할때 boardVO에 담아서 리턴을 하는데 imageVO도 리턴을 해야 파일을 출력할 수 있을 것 같다.
		// 그리고 셀렉트 할때는 board테이블과 image테이블을 조인한 값으로 출력을 해야할거 같다.
		// 이미지테이블에 값넣고 리턴 받기
//		file_upload(imageVO , MultipartHttpServletRequest multi); // 이미지vo에 값으로 파일 업로드 작동하기 리턴은 int?
		// 하고 셀렉을 할떄는 조인해서 결과값을 출력해야하는데...
		// 출력을 할때 필요한게 글번호와 파일이름이 필요하다.
		return kbvo;
	}

	@Override
	public int board_update(KjhBoardVO kbvo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int board_delete(KjhBoardVO kbvo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String board_insert_with_map(Map map) {
		// TODO Auto-generated method stub
		return null;
	}

}
