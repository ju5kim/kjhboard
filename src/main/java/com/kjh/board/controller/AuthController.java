package com.kjh.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sample/*")
public class AuthController {
	@GetMapping("/all")
	public void doAll() {
	}
	@GetMapping("/member")
	public void doMember() {
	}
	
	@GetMapping("/admin")
	public void doAdmin() {
	}
	
}
