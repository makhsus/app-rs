package com.rs.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/mahasiswa")
public class MahasiswaController {
	
	@RequestMapping(value={"", "index"}, method=RequestMethod.GET)
	public String index(HttpServletRequest request, ModelMap model){
		model.addAttribute("title", "Rumah - Home");
		return "zul/mahasiswa/mahasiswa.zul";
	}
}
