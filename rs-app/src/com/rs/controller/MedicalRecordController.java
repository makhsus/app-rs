package com.rs.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/medicalrecord")
public class MedicalRecordController {

	@RequestMapping(value={"addeditbynoreg"}, method=RequestMethod.GET)
	public String addeditbynoreg(HttpServletRequest request, ModelMap model){
		return "zul/medicalrecord/add_edit_by_noreg.zul";
	}
	
	@RequestMapping(value={"addeditbypatient"}, method=RequestMethod.GET)
	public String addeditbypatient(HttpServletRequest request, ModelMap model){
		return "zul/medicalrecord/add_edit_by_patient.zul";
	}
	
}
