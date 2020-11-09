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
}
