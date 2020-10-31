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
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);

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
	public String reply_re_insert(CommentsVO commentsVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String reply_re_select(CommentsVO commentsVO) {
		// TODO Auto-generated method stub
		return null;
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
