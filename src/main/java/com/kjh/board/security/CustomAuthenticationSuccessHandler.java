package com.kjh.board.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		System.out.println("커스텀 인증 핸들러 작동되었음 ::::");
		List<String> roleNameList = new ArrayList<String>();
		
		authentication.getAuthorities().forEach(authority -> {roleNameList.add(authority.getAuthority());});
		for(String role : roleNameList) {
			System.out.println("사용자가 가지고 있는 role :::: "+role);
		}
		if(roleNameList.contains("ROLE_ADMIN")) { //관리자 라면 관리자 화면으로
			response.sendRedirect("/sample/admin");
			return;
		}
		if(roleNameList.contains("ROLE_MEMBER")) {// 일반 회원이ㅣ라면 일반 회원화면으로
			response.sendRedirect("/sample/member");
			return;
		}
		System.out.println("커스텀 인증 핸들러 작동 종료되었음");
		response.sendRedirect("/"); //아니면 초기화면으로 
	}

}
