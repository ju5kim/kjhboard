package com.kjh.board.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.kjh.board.vo.KjhMemberVO;


public interface MemberService {
 public boolean mem_insert(KjhMemberVO kvo,HttpServletRequest request);
 public String mem_select_id(KjhMemberVO kvo);
 public String mem_select_all();
 public int mem_update(KjhMemberVO kjhmemberVO,HttpServletRequest request);
 public boolean mem_delete(KjhMemberVO kjhmemberVO);
 public boolean mem_longin(KjhMemberVO kvo,HttpServletRequest request);
 public KjhMemberVO mem_select_kvo(KjhMemberVO kvo);
}
