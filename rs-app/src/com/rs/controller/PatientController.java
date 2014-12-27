package com.rs.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/patient")
public class PatientController {
	
	@RequestMapping(value={"add"}, method=RequestMethod.GET)
	public String add(HttpServletRequest request, ModelMap model){
		model.addAttribute("title", "Pendaftaran Pasien");
		return "zul/patient/add.zul";
	}
	
	@RequestMapping(value={"", "list"}, method=RequestMethod.GET)
	public String list(HttpServletRequest request, ModelMap model){
		model.addAttribute("title", "List Pasien");
		return "zul/patient/list.zul";
	}
}
