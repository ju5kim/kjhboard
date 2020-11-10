package com.kjh.board.accessDenied;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.kjh.board.vo.AuthMember;

import lombok.Getter;

@Getter
public class CustomUsuer extends User {
	

	private static final long serialVersionUID = 1L;
	private AuthMember member;
	
	public CustomUsuer(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}
	public CustomUsuer(AuthMember member) {
		super(member.getUserName(), member.getUserpw(), member.getAuthList().stream().map(auth->new SimpleGrantedAuthority(auth.getAuth())).collect(Collectors.toList()));
		this.member=member;
	}

}
