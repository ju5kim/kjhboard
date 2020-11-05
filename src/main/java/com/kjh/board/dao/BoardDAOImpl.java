package com.kjh.board.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kjh.board.vo.ImageVO;
import com.kjh.board.vo.KjhBoardVO;
import com.kjh.board.vo.PageVO;

@Repository
public class BoardDAOImpl implements BoardDAO {
	private static final Logger log = LoggerFactory.getLogger(BoardDAOImpl.class);
	@Autowired
	SqlSessionTemplate sqlsession;

	@Override
	public List<KjhBoardVO> board_select_list() {
		List<KjhBoardVO> list = sqlsession.selectList("mapper.board.board_select_list");
		return list;
	}

	@Override
	public List<KjhBoardVO> board_select_list_page(PageVO pvo) {
		List<KjhBoardVO> list = sqlsession.selectList("mapper.board.board_select_list_page", pvo);
		return list;
	}

	@Override
	public int board_select_all_count() {
		int result = sqlsession.selectOne("mapper.board.board_select_all_count");
		return result;
	}

	@Override // 글 상세의 경우 글 테이블도 조회하지만 이미지 테이블도 조회해서 해당 글번호와 파일이름을 구해야한다.
	public KjhBoardVO board_select_one(KjhBoardVO kbvo) {
		kbvo = sqlsession.selectOne("mapper.board.board_select_one", kbvo);
		return kbvo;
	}

	// 이미지 테이블을 조회해서 해당 해당글에 대한 정보를 가지고 온다.
	public ImageVO image_select_one(ImageVO imageVO) {
		// 일단 먼저 인서트를 완성하고
		return imageVO;
	}

	@Override
	public int board_insert(KjhBoardVO kbvo) { //
		log.info("DAO bnum ::: " + kbvo.getB_num());
		log.info("DAO bsubject ::: " + kbvo.getB_subject());
		log.info("DAO content ::: " + kbvo.getB_content());
		log.info("DAO m_num ::: " + kbvo.getM_num());
		int result = sqlsession.insert("mapper.board.board_insert", kbvo);
		log.info("dao에서 인서트 완료하고 b_num :::" + kbvo.getB_num());
		return result;// 여기서 sql문으로 글번호를 가지고 올 수 있게 할 수 없을까?
	}

	@Override
	public int image_insert(ImageVO imagevo) {// 글번호와 파일이름을 가지고 이미지를 삽입한다.
		int result = sqlsession.insert("mapper.board.image_insert", imagevo);
		log.info(imagevo.getB_num());
		log.info(imagevo.getImage_num());
		log.info(imagevo.getImage_file_name());
		log.info(imagevo.getReg_date());
		return result;
	}

	@Override
	public int image_update(ImageVO imagevo) {
		int result = sqlsession.update("mapper.board.image_update", imagevo);
		return result;

	}

	@Override
	public int board_update(KjhBoardVO kbvo) {
		int result = sqlsession.update("mapper.board.board_update", kbvo);
		return result;
	}

	@Override
	public int board_delete(KjhBoardVO kbvo) {
		int result = sqlsession.delete("mapper.board.board_delete", kbvo);
		return result;
	}

	@Override
	public List<ImageVO> image_select_list(String b_num) {
		List<ImageVO> list = sqlsession.selectList("mapper.board.select_image", b_num);
		return list;
	}

}
