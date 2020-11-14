package com.kjh.board.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.kjh.board.dao.MemberDAO;
import com.kjh.board.securitydomain.CustomUser;
import com.kjh.board.vo.KjhMemberVO;

public class CustomUserDetailService implements UserDetailsService{
	@Autowired
	MemberDAO memberdao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("CustomUserDetailService 실행 :::");
		KjhMemberVO kvo = new KjhMemberVO();
		kvo.setM_id(username);
		KjhMemberVO kv = memberdao.mem_select_m_num(kvo);
		System.out.println(kv);
		System.out.println("username:::"+username);
	
		return new CustomUser(kv) ;
	}

}
