package com.kjh.board.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.kjh.board.vo.ImageVO;
import com.kjh.board.vo.KjhBoardVO;
import com.kjh.board.vo.PageVO;

public interface BoardService {
	public List<KjhBoardVO> board_select_list();
	public List<ImageVO> select_image(String b_num); 
	public List<KjhBoardVO> board_select_list_page(PageVO pvo);
	public KjhBoardVO kbvo_setting(MultipartHttpServletRequest multipartRequest);
	public List<ImageVO> imagevo_setting(MultipartHttpServletRequest multipartRequest,String b_num) throws IllegalStateException, IOException;
	public List<ImageVO> image_insert(List<ImageVO> list,MultipartHttpServletRequest multipartHttpServletRequest);
	public KjhBoardVO board_insert_select_file(KjhBoardVO kbvo, List list);
	public KjhBoardVO board_select_one(KjhBoardVO kbvo);
	public int board_insert(KjhBoardVO kbvo);
	public KjhBoardVO board_insert_select(KjhBoardVO kbvo);
	public String board_insert_with_map(Map map);
	public int board_update(KjhBoardVO kbvo);
	public int board_delete(KjhBoardVO kbvo);
}
