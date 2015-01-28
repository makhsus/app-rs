package com.rs.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Url1MappingController {

	@RequestMapping(value={"login"}, method=RequestMethod.GET)
	public String login(){
		return "zul/user/login.zul";
	}

	@RequestMapping(value={"home", "home/"}, method=RequestMethod.GET)
	public String home(){
		return "zul/home.zul";
	}
	
	@RequestMapping(value={"occupation"}, method=RequestMethod.GET)
	public String occupation(){
		return "zul/xxxx.zul";
	}
}
