package com.kjh.board.dao;

import com.kjh.board.vo.KjhMemberVO;


public interface MemberDAO {
	 public int mem_insert(KjhMemberVO kvo);
	 public KjhMemberVO mem_select_m_num(KjhMemberVO kvo);
	 public boolean mem_val_id(KjhMemberVO kvo);
	 public String mem_select_all();
	 public int mem_update(KjhMemberVO kvo);
	 public int mem_delete(KjhMemberVO kvo);
	 public KjhMemberVO mem_select_kvo(KjhMemberVO kvo);
	 public String select_salt(KjhMemberVO kvo);
}
