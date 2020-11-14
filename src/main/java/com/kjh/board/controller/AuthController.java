package com.kjh.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kjh.board.security.CustomUserDetailService;

@Controller
@RequestMapping("/sample/*")
public class AuthController {
	@Autowired
	
	CustomUserDetailService custom;
	
	@GetMapping("/all")
	public void doAll(String mid) {
		custom.loadUserByUsername(mid);
		System.out.println("auth 컨트롤러 :::");
	}
	@GetMapping("/member")
	public void doMember() {
		System.out.println("auth 컨트롤러 :::");
	}
	
	@GetMapping("/admin")
	public void doAdmin() {
		System.out.println("auth 컨트롤러 :::");
	}
	
}
