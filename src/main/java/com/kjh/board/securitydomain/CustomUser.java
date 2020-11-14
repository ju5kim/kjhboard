package com.kjh.board.securitydomain;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.kjh.board.vo.KjhMemberVO;

import lombok.Getter;

@Getter
public class CustomUser extends User {
	//기본상속값
	private static final long serialVersionUID = 1L;
	//기본상속값
	public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		
	}
	private KjhMemberVO member;
	
	//내가 만드는 값
	public CustomUser(KjhMemberVO kvo) {
		super(kvo.getM_id(),kvo.getM_pw(),kvo.getAuthList().stream().map(auth->new SimpleGrantedAuthority(auth.getAuth())).collect(Collectors.toList()));
		this.member = kvo;
	}
	

}
