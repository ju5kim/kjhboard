package com.kjh.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kjh.board.dao.CommentDAO;
import com.kjh.board.vo.CommentsVO;

@Transactional
@Service
public class CommentsServiceImpl implements CommentService {

	@Autowired
	CommentDAO commentDAO;

	@Override
	public CommentsVO reply_insert(CommentsVO commentsVO) {
		int result = commentDAO.reply_insert(commentsVO);
		// 여기서 자동으로 c_num값이 셋팅되서 나오나? 아니면 dao에서 받으면된다.
		if (result > 0) {
			commentsVO = commentDAO.reply_select(commentsVO);// c_num으로 셀렉해서 다시 commentsVO셋팅된다.
		}
		return commentsVO;
	}

	@Override
	public List reply_select_all(String b_num) {
		List list=commentDAO.reply_select_all(b_num);
		return list;
	}

	@Override
	public String reply_update(CommentsVO commentsVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String reply_delete(CommentsVO commentsVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommentsVO reply_re_insert(CommentsVO commentsVO) {
		int result = commentDAO.reply_re_insert(commentsVO);
		return commentsVO;
	}

	@Override
	public List reply_re_select_All(CommentsVO commentsVO) {
		List reply_re_list=commentDAO.reply_re_select_All(commentsVO);
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
