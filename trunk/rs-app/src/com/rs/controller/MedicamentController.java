package com.rs.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/medicament")
public class MedicamentController {

	@RequestMapping(value={"", "index"}, method=RequestMethod.GET)
	public String index(HttpServletRequest request, ModelMap model){
		model.addAttribute("title", "Obat");
		return "zul/medicament/master.zul";
	}
	
	@RequestMapping(value={"supplier"}, method=RequestMethod.GET)
	public String supplier(HttpServletRequest request, ModelMap model){
		model.addAttribute("title", "Obat");
		return "zul/medicament/supplier.zul";
	}
	
	@RequestMapping(value={"patient"}, method=RequestMethod.GET)
	public String patient(HttpServletRequest request, ModelMap model){
		model.addAttribute("title", "Pemberian Obat Pasien");
		return "zul/medicament/m_patient.zul";
	}
}
