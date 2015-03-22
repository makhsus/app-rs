package com.rs.composer.medicalrecord;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.East;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

import com.rs.composer.BaseComposer;
import com.rs.dao.MedicalRecordsDao;
import com.rs.dao.PatientDao;
import com.rs.model.MedicalRecords;
import com.rs.model.Patient;
import com.rs.model.Users;
import com.rs.util.CalendarUtil;
import com.rs.util.CommonUtil;

public class AddEditByPatientComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;
	
	private Patient patientCurrent;
	private MedicalRecords medicalRecordCurrent;
	
	
	@Wire
	private Textbox tbxPasienName, tbxCardNo;
	@Wire
	private Listbox lbxSearchResult;
	@Wire
	private Textbox tbxAlergi, tbxAnamnesis, tbxDiagnosis, tbxActionPlan, tbxResep;
	@Wire
	private East bltEast;
	
	@Listen ("onCreate = #blt")
	public void blt(){
		isLooged();
		
	}
	
	@Listen ("onClick = #btnSearch")
	public void btnSearchOnClick(){
		String pasienName = tbxPasienName.getValue().trim().equalsIgnoreCase("") ? "5454h3jk24h":tbxPasienName.getValue().trim();
		String cardNo = tbxCardNo.getValue().trim().equalsIgnoreCase("") ? "fsfsd979":tbxCardNo.getValue().trim();
		
		lbxSearchResult.getItems().clear();
		medicalRecordCurrent = null;
		tbxAlergi.setValue("");
		tbxAnamnesis.setValue("");
		tbxDiagnosis.setValue("");
		tbxActionPlan.setValue("");
		tbxResep.setValue("");
		
		PatientDao patientDao = new PatientDao();
		patientDao.setSessionFactory(sessionFactory);
		
		Criterion or1 = Restrictions.like("name", "%"+pasienName+"%");
		Criterion or2 = Restrictions.like("cardNumber", "%"+cardNo+"%");
		LogicalExpression le = Restrictions.or(or1, or2);
		List<Patient> patientList = patientDao.loadBy(Order.asc("id"), le);
		
		if(patientList.size()<1){
			Messagebox.show("Pasien tidak ditemukan");
			return;
		}else{
			for(Patient p: patientList){
				Listitem li = new Listitem();
				lbxSearchResult.appendChild(li);
				
				Listcell lc = new Listcell(p.getName());
				li.appendChild(lc);
				
				lc = new Listcell(p.getCardNumber());
				li.appendChild(lc);
				
				lc = new Listcell();
				Toolbarbutton tbn = new Toolbarbutton("Rekam Medis");
				tbn.addEventListener("onClick", new AddEditMedicalRecord(p));
				lc.appendChild(tbn);
				li.appendChild(lc);
				
			}
		}
	}
	
	@Listen ("onClick = #btnSaveMr")
	public void btnSaveMrOnClick(){
		String alergi = tbxAlergi.getValue().trim();
		String anamnesis = tbxAnamnesis.getValue().trim();
		String diagnosa = tbxDiagnosis.getValue().trim();
		String actionPlan = tbxActionPlan.getValue().trim();
		String resep = tbxResep.getValue().trim();
		
		Users user = (Users) sessionZk.getAttribute(CommonUtil.LOGIN_USER);
		
		MedicalRecordsDao mrDao = new MedicalRecordsDao();
		mrDao.setSessionFactory(sessionFactory);
		
		MedicalRecords mr = new MedicalRecords();
		if(medicalRecordCurrent!=null){
			mr = medicalRecordCurrent;	
		}else{
			mr.setCreatedBy(user);
			mr.setCreatedDate(CalendarUtil.dateNow());
		}
		mr.setAllergy(alergi);
		mr.setAnamnesis(anamnesis);
		mr.setDiagnosis(diagnosa);
		mr.setActionPlan(actionPlan);
		mr.setActionPlan(actionPlan);
		mr.setPrescription(resep);
		mr.setPatient(patientCurrent);
		
		if(mrDao.saveOrUpdate(mr)){
			Messagebox.show("Rekam medis sukses disimpan");
		}else{
			Messagebox.show("Rekam medis gagal disimpan");
		}
	}
	
	
	class AddEditMedicalRecord implements EventListener<Event> {

		private Patient p;
		
		public AddEditMedicalRecord(Patient p) {
			this.p = p;
		}
		
		@Override
		public void onEvent(Event event) throws Exception {
			patientCurrent = p;
			
			MedicalRecordsDao mrDao = new MedicalRecordsDao();
			mrDao.setSessionFactory(sessionFactory);
			
			Criterion crPatient = Restrictions.eq("patient", p);
			List<MedicalRecords> mrList = mrDao.loadBy(Order.asc("medicalRecordsId"), crPatient);
			System.out.println("size: "+mrList.size());
			if(mrList.size()>0){
				MedicalRecords mr = mrList.get(0);
				medicalRecordCurrent = mr;
				
				bltEast.setVisible(true);
				tbxAlergi.setValue(mr.getAllergy());
				tbxAnamnesis.setValue(mr.getAnamnesis());
				tbxDiagnosis.setValue(mr.getDiagnosis());
				tbxActionPlan.setValue(mr.getActionPlan());
				tbxResep.setValue(mr.getPrescription());
			}	
		}
		
	}

}
