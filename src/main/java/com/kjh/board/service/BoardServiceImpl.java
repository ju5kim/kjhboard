package com.kjh.board.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kjh.board.controller.BoardController;
import com.kjh.board.dao.BoardDAO;
import com.kjh.board.vo.KjhBoardVO;
import com.kjh.board.vo.PageVO;

@Transactional
@Service
public class BoardServiceImpl implements BoardService {
	private static final Logger log = LoggerFactory.getLogger(BoardServiceImpl.class);
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
	public KjhBoardVO board_insert_select(KjhBoardVO kbvo) {
		int result = boardDAO.board_insert(kbvo);
		kbvo = boardDAO.board_select_one(kbvo);
		log.info(kbvo.getB_num());
		log.info(kbvo.getB_subject());
		log.info(kbvo.getB_content());
		log.info(kbvo.getB_reg_date());
		return kbvo;
	}

	@Override
	public String board_insert_with_map(Map map) {
		// 여기서 맵 객체를 분리해서 boardVO에 담아서 보내서 게시판 테이블 인서트 시키고

		// 여기서 또 맵객체를 분리해서 imageVO에 담아서 보내서 이미지 테이블 인서트 시킨다.
		// 그런데 여기 인서트를 시킬려면 글번호를 가지고 와야한다.

		return null;
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

}
