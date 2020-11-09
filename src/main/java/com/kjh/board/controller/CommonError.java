package com.kjh.board.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommonError {
	
	@RequestMapping("/accessError")
	public String accessDenied(Authentication authentication,Model model) {
		
		model.addAttribute("msg","Accessdenied 입니다.");
		return "/sample/accessError";
	}
	@RequestMapping("/customLogin")
	public String loginInput(String error,String logout,Model model) {
		
		if(error != null) {
			model.addAttribute("error", "로그인 에러 다시 체크하세요");
		}
		if(logout != null) {
			model.addAttribute("logout", "로그아웃 되었습니다.");
		}
		return "/sample/customLogin";
	}
	
}
