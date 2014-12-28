package com.rs.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/doctor")
public class DoctorController {
	
	@RequestMapping(value={"", "index"}, method=RequestMethod.GET)
	public String index(HttpServletRequest request, ModelMap model){
		return "zul/mahasiswa/mahasiswa.zul";
	}
	
	@RequestMapping(value={"schedule"}, method=RequestMethod.GET)
	public String jadwalPraktek(HttpServletRequest request, ModelMap model){
		return "zul/doctor/practice_schedule.zul";
	}
}
