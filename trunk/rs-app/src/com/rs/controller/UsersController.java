package com.rs.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/user")
public class UsersController {
	
	@RequestMapping(value={"", "listUser"}, method=RequestMethod.GET)
	public String listUser(HttpServletRequest request, ModelMap model){
		model.addAttribute("title", "Users");
		return "zul/user/list_user.zul";
	}

}
