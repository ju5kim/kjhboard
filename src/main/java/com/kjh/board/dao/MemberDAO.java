package com.kjh.board.dao;

import org.springframework.stereotype.Repository;

import com.kjh.board.vo.KjhMemberVO;


public interface MemberDAO {
	 public int mem_insert(KjhMemberVO kvo);
	 public String mem_select_m_num(KjhMemberVO kvo);
	 public boolean mem_val_id(KjhMemberVO kjhmemberVO);
	 public String mem_select_all();
	 public int mem_update(KjhMemberVO kjhmemberVO);
	 public int mem_delete(KjhMemberVO kjhmemberVO);
	 public KjhMemberVO mem_select_kvo(KjhMemberVO kvo);
}
