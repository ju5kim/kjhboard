package com.kjh.board.service;

import com.kjh.board.vo.KjhBoardVO;
import com.kjh.board.vo.KjhMemberVO;

interface NaverLoinService {

public int naver_insert(KjhMemberVO kvo);
public KjhMemberVO naver_login(KjhMemberVO kvo);
public String naver_id_db_chek(KjhMemberVO kvo);
}
