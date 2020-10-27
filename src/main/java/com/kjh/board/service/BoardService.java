package com.kjh.board.service;

import java.util.List;
import java.util.Map;

import com.kjh.board.vo.KjhBoardVO;
import com.kjh.board.vo.PageVO;

public interface BoardService {
	public List<KjhBoardVO> board_select_list();
	public List<KjhBoardVO> board_select_list_page(PageVO pvo);
	public KjhBoardVO board_select_one(KjhBoardVO kbvo);
	public int board_insert(KjhBoardVO kbvo);
	public KjhBoardVO board_insert_select(KjhBoardVO kbvo);
	public String board_insert_with_map(Map map);
	public int board_update(KjhBoardVO kbvo);
	public int board_delete(KjhBoardVO kbvo);
}
