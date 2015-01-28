package com.rs.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admission")
public class AdmissionController {
	
	@RequestMapping(value={"add"}, method=RequestMethod.GET)
	public String add(HttpServletRequest request, ModelMap model){
		model.addAttribute("title", "Daftar Admisi");
		return "zul/admission/add.zul";
	}
	
	@RequestMapping(value={"", "list"}, method=RequestMethod.GET)
	public String list(HttpServletRequest request, ModelMap model){
		model.addAttribute("title", "List Admission");
		return "zul/admission/list.zul";
	}
}
