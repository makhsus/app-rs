package com.rs.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/medicine")
public class MedicineController {

	@RequestMapping(value={"", "index"}, method=RequestMethod.GET)
	public String index(HttpServletRequest request, ModelMap model){
		model.addAttribute("title", "Obat");
		return "zul/medicine/master.zul";
	}
	
	@RequestMapping(value={"supplier"}, method=RequestMethod.GET)
	public String supplier(HttpServletRequest request, ModelMap model){
		model.addAttribute("title", "Obat");
		return "zul/medicine/supplier.zul";
	}
	
	@RequestMapping(value={"trn"}, method=RequestMethod.GET)
	public String patient(HttpServletRequest request, ModelMap model){
		model.addAttribute("title", "Pemberian Obat Pasien");
		return "zul/medicine/transactions.zul";
	}
}
