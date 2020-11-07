package com.kjh.board.dao;

import com.kjh.board.vo.KjhMemberVO;

interface NaverLoginDAO {
	public int naver_insert(KjhMemberVO kvo);
	public KjhMemberVO naver_login(KjhMemberVO kvo);
	public String naver_id_db_chek(KjhMemberVO kvo);
}
