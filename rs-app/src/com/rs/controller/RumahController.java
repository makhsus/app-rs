package com.rs.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rs.model.Rumah;

@Controller
@RequestMapping("/rumah")
public class RumahController {
	
	@RequestMapping(value={"/", "", "index"}, method=RequestMethod.GET)
	public String index(HttpServletRequest request, ModelMap model){
		
		Rumah rumah = new Rumah();
		rumah.setOwnerName("Makhsus");
		rumah.setAddress("Bla bla bla");
		
		model.addAttribute("title", "Rumah - Home");
		return "zul/rumah/rumah.zul";
	}
}
