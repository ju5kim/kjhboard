package com.kjh.board.dao;

import java.util.List;

import com.kjh.board.vo.KjhBoardVO;
import com.kjh.board.vo.PageVO;

public interface BoardDAO {
	public List<KjhBoardVO> board_select_list();
	public KjhBoardVO board_select_one(KjhBoardVO kbvo);
	public int board_select_all_count();
	public List<KjhBoardVO> board_select_list_page(PageVO pvo);
	public int board_insert(KjhBoardVO kbvo);
	public int board_update(KjhBoardVO kbvo);
	public int board_delete(KjhBoardVO kbvo);
	
}
