package com.kjh.board.dao;

import java.util.List;

import com.kjh.board.vo.CommentsVO;

public interface CommentDAO {
	
	public int reply_insert(CommentsVO commentsVO);
	// 댓글 조회
	public CommentsVO reply_select(CommentsVO commentsVO);
	// 댓글 전체조회
	public List reply_select_all(String b_num);
	// 댓글 수정
	public int reply_update(CommentsVO commentsVO);

	// 댓글 삭제
	public String reply_delete(CommentsVO commentsVO);

	// 대댓글 달기
	public int reply_re_insert(CommentsVO commentsVO);

	// 대댓글 조회
	public List reply_re_select_All(CommentsVO commentsVO);

	// 대댓글 수정
	public String reply_re_update(CommentsVO commentsVO);

	// 대댓글 삭제
	public String reply_re_delete(CommentsVO commentsVO);
}
