package com.kjh.board.controller;

import org.apache.commons.logging.impl.Log4JLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;

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
