package com.kjh.board.vo;

public class KjhCommentVO {
	
	private String c_num; //댓글번호 기본키
	private String c_comment;//댓글내용
	private String c_reg_date;//작성일자
	private String c_update_date;//업데이트일자
	private String c_target_num; //상위댓글번호
	private String b_num; // 댓글들의 글번호 외래키
	private String m_num; // 작성자 회원번호 외래키
}
