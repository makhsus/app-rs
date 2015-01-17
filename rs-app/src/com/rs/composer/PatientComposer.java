package com.rs.composer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.rs.dao.PatientDao;
import com.rs.model.Patient;
import com.rs.model.Polyclinic;

public class PatientComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;

	@Wire
	private Window win;
	@Wire
	private Listbox lbxList;
	@Wire
	private Grid grdAddEdit;
	@Wire
	private Grid grdSearch;
	@Wire
	private Textbox tbxSearchCardNo, tbxSearchName;
	@Wire
	private Textbox tbxCardNo, tbxName, tbxHusbandWifeName, tbxParentName, tbxReligion, tbxAddress, tbxOccupation, tbxPhone;
	@Wire
	private Radiogroup rgGender, rgHusbandWife, rgLastStudy;
	@Wire
	private Datebox dtbBirthdate;
	@Wire
	private Label lblAge;
	
	
	private Patient selectedPatient;
	
	
	@Listen ("onCreate = #win")
	public void win(){
		isLooged();
		loadDataPatient();
	}
	
	@Listen ("onClick = #tbnList")
	public void tbnListClick(){
		lbxList.setVisible(true);
		grdSearch.setVisible(true);
		grdAddEdit.setVisible(false);
		win.setTitle("Pasien Rumah Sakit Siti Aminah");
	}
	
	@Listen ("onClick = #tbnAdd")
	public void tbnAddClick(){
		lbxList.setVisible(false);
		grdSearch.setVisible(false);
		grdAddEdit.setVisible(true);
		win.setTitle("Pendaftaran Pasien");
	}
	
	@Listen ("onClick = #btnSubmit")
	public void btnSubmitClick(){
		String cardNo = tbxCardNo.getText().trim();
		String name = tbxName.getText().trim();
		String gender = "";
		if(rgGender.getSelectedIndex() == 0)
			gender = "M";
		else
			gender = "F";
		String husbandName = "";
		String wifeName = "";
		String parentName="";
		if(rgHusbandWife.getSelectedIndex() == 0)
			husbandName = tbxHusbandWifeName.getText().trim();
		else
			wifeName = tbxHusbandWifeName.getText().trim();
		Date birthdate = dtbBirthdate.getValue();
		String religion = tbxReligion.getText().trim();
		String address = tbxAddress.getText().trim();
		String lastStudy = rgLastStudy.getSelectedItem().getLabel().trim();
		String occupation = tbxOccupation.getText().trim();
		String phoneNumber = tbxPhone.getText().trim();
		
		String msg = "Save";
		Patient patient = new Patient();
		if(selectedPatient!=null){
			msg = "Update";
			patient = selectedPatient;
		}
		
		patient.setCardNumber(cardNo);
		patient.setName(name);
		patient.setGender(gender);
		patient.setHusbandName(husbandName);
		patient.setWifeName(wifeName);
		patient.setParentName(parentName);
		patient.setBirthdate(birthdate);
		patient.setReligion(religion);
		patient.setAddress(address);
		patient.setLastStudy(lastStudy);
		patient.setOccupation(occupation);
		patient.setPhone(phoneNumber);
		if(selectedPatient!=null){
			patient.setUpdatedDate(new Date());
		}else{
			patient.setCreatedDate(new Date());
		}
		
		PatientDao dao = new PatientDao();
		dao.setSessionFactory(sessionFactory);
		if(dao.saveOrUpdate(patient)) {
			Messagebox.show("Success "+msg);
			tbnListClick();
			loadDataPatient();
		}else{
			Messagebox.show("Failed "+msg);
		}
	}
	
	private void loadDataPatient(){
		PatientDao dao = new PatientDao();
		dao.setSessionFactory(sessionFactory);
		List<Patient> list = dao.loadAll(Order.asc("name"));//dao.listAll();
		//System.out.println("list: "+list);
		
		lbxList.getItems().clear();
		for(Patient obj: list){
			Listitem li = new Listitem();
			lbxList.appendChild(li);
			
			Listcell lc = new Listcell(obj.getId().toString());
			li.appendChild(lc);
			lc = new Listcell(obj.getName());
			li.appendChild(lc);
			lc = new Listcell(obj.getCardNumber());			
			li.appendChild(lc);
			lc = new Listcell(obj.getGender());
			li.appendChild(lc);
			lc = new Listcell(obj.getPhone());
			li.appendChild(lc);
		}
	}
	
	private void loadDataPatient(String cardNo, String name){
		
		
		PatientDao dao = new PatientDao();
		dao.setSessionFactory(sessionFactory);
		
		
		
		Criterion criterionCard = Restrictions.eq("cardNumber", cardNo);
		Criterion criterionName = Restrictions.eq("name", name);
		
		
		List<Patient> list = new ArrayList<>();
		if (cardNo.equalsIgnoreCase("") && name.equalsIgnoreCase("")){
			list = dao.loadAll(Order.asc("name"));
		}
		else if (!cardNo.equalsIgnoreCase("") && name.equalsIgnoreCase("")){
			list = dao.loadBy(Order.asc("name"), criterionCard);
		}
		else if (cardNo.equalsIgnoreCase("") && !name.equalsIgnoreCase("")){
			list = dao.loadBy(Order.asc("name"), criterionName);
		}
		else{
			list = dao.loadBy(Order.asc("name"), new Criterion[]{criterionCard, criterionName});
		}
		
		
		lbxList.getItems().clear();
		for(Patient obj: list){
			Listitem li = new Listitem();
			lbxList.appendChild(li);
			
			Listcell lc = new Listcell(obj.getId().toString());
			li.appendChild(lc);
			lc = new Listcell(obj.getName());
			li.appendChild(lc);
			lc = new Listcell(obj.getCardNumber());			
			li.appendChild(lc);
			lc = new Listcell(obj.getGender());
			li.appendChild(lc);
			lc = new Listcell(obj.getPhone());
			li.appendChild(lc);
		}
	}
	
	
	@Listen ("onClick = #btnSearch")
	public void btnSearchClick(){
		String cardNo = "";
		String name = "";
		
		cardNo = tbxSearchCardNo.getText().toString();
		name = tbxSearchName.getText().toString();
		
		if (cardNo.equalsIgnoreCase("") && name.equalsIgnoreCase("")){
			loadDataPatient();
		}else{
			loadDataPatient(cardNo, name);
		}
		
		
				
	}
}
