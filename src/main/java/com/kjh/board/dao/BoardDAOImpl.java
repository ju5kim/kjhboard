package com.kjh.board.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kjh.board.vo.KjhBoardVO;
import com.kjh.board.vo.PageVO;


@Repository
public class BoardDAOImpl implements BoardDAO{
	
	@Autowired
	SqlSessionTemplate sqlsession;
	
	@Override
	public List<KjhBoardVO> board_select_list() {
		List<KjhBoardVO> list=sqlsession.selectList("mapper.board.board_select_list");
		return list;
	}
	
	@Override
	public List<KjhBoardVO> board_select_list_page(PageVO pvo) {
		List<KjhBoardVO> list=sqlsession.selectList("mapper.board.board_select_list_page",pvo);
		return list;
	}
	@Override
	public int board_select_all_count() {
		int result =sqlsession.selectOne("mapper.board.board_select_all_count");
		return result;
	}

	@Override
	public KjhBoardVO board_select_one(KjhBoardVO kbvo) {
		kbvo=sqlsession.selectOne("mapper.board.board_select_one", kbvo);
		return kbvo;
	}

	@Override
	public int board_insert(KjhBoardVO kbvo) {
		System.out.println("DAO bnum ::: "+kbvo.getB_num());
		System.out.println("DAO bsubject ::: "+kbvo.getB_subject());
		System.out.println("DAO content ::: "+kbvo.getB_content());
		System.out.println("DAO m_num ::: "+kbvo.getM_num());
		int result=sqlsession.insert("mapper.board.board_insert",kbvo);
		
		return result;
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
