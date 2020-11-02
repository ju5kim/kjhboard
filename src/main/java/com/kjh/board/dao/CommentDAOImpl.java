package com.kjh.board.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kjh.board.controller.BoardController;
import com.kjh.board.vo.CommentsVO;

@Repository
public class CommentDAOImpl implements CommentDAO {
	private static final Logger log = LoggerFactory.getLogger(CommentDAOImpl.class);

	@Autowired
	SqlSessionTemplate sqlsessionT;

	@Override
	public int reply_insert(CommentsVO commentsVO) {
		int result = sqlsessionT.insert("mapper.comments.reply_insert", commentsVO);
		return result;
	}

	@Override
	public CommentsVO reply_select(CommentsVO commentsVO) {
		commentsVO = sqlsessionT.selectOne("mapper.comments.reply_select", commentsVO);
		return commentsVO;
	}

	@Override
	public List reply_select_all(String b_num) {
		List list = sqlsessionT.selectList("mapper.comments.reply_select_all", b_num);
		return list;
	}

	@Override
	public int reply_update(CommentsVO commentsVO) {
		int result = sqlsessionT.update("mapper.comments.reply_update", commentsVO);
		return result;
	}

	@Override
	public String reply_delete(CommentsVO commentsVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int reply_re_insert(CommentsVO commentsVO) {
		String b_num = commentsVO.getB_num();// 최상위 글 번호
		String c_c_num = commentsVO.getC_c_num(); // 대 댓글 번호//평범하게 입력시 대 댓글은 null
		String m_num = commentsVO.getM_num();//
		String c_content = commentsVO.getC_content();//
		String c_num =commentsVO.getC_num();
		log.info("commentsVO.c_num ::: " + c_num);
		log.info("commentsVO.c_c_num ::: " + c_c_num);
		log.info("commentsVO.m_num ::: " + m_num);
		log.info("commentsVO.c_content ::: " + c_content);
		log.info("commentsVO.b_num ::: " + b_num);
		//아마 내생각에 vo에 cnum 값이 있는데 어게 충돌이 일어나는게 아닐까 하는 생각이 든다.
		
		int result = sqlsessionT.insert("mapper.comments.reply_re_insert", commentsVO);
		log.info("실행 완료");
		 b_num = commentsVO.getB_num();// 최상위 글 번호
		 c_c_num = commentsVO.getC_c_num(); // 대 댓글 번호//평범하게 입력시 대 댓글은 null
		m_num = commentsVO.getM_num();//
		 c_content = commentsVO.getC_content();//
		 c_num =commentsVO.getC_num();
		log.info("commentsVO.c_num ::: " + c_num);
		log.info("commentsVO.c_c_num ::: " + c_c_num);
		log.info("commentsVO.m_num ::: " + m_num);
		log.info("commentsVO.c_content ::: " + c_content);
		log.info("commentsVO.b_num ::: " + b_num);
		return result;
	}

	@Override
	public List reply_re_select_All(CommentsVO commentsVO) {
		List reply_re_list=sqlsessionT.selectList("mapper.comments.reply_re_select_All", commentsVO);
		return reply_re_list;
	}

	@Override
	public String reply_re_update(CommentsVO commentsVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String reply_re_delete(CommentsVO commentsVO) {
		// TODO Auto-generated method stub
		return null;
	}

}
