package com.rs.composer.medicalrecord;

import java.util.Calendar;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.East;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.rs.composer.BaseComposer;
import com.rs.dao.MedicalRecordsDao;
import com.rs.dao.MedicalTransactionDao;
import com.rs.model.MedicalRecords;
import com.rs.model.MedicalTransaction;
import com.rs.model.Users;
import com.rs.util.CalendarUtil;
import com.rs.util.CommonUtil;

public class AddEditByNoregComposer extends BaseComposer {
	private static final long serialVersionUID = 774956125207272229L;

	private String noregInserted;
	private MedicalTransaction medicalTrnCurrent;
	private MedicalRecords medicalRecordCurrent;
	
	@Wire
	private Textbox tbxNoReg;
	@Wire
	private East bltEast;
	@Wire
	private Label lblNoreg, lblPasienName, lblRegisterDate, lblPoli, lblDoctor;
	@Wire
	private Textbox tbxAlergi, tbxAnamnesis, tbxDiagnosis, tbxActionPlan, tbxResep;
	
	
	@Listen ("onCreate = #blt")
	public void blt(){
		isLooged();
		
	}
	
	@Listen ("onClick = #btnMrSearch")
	public void btnMrSearchOnClick(){
		String noreg = tbxNoReg.getText().trim();
		if(noreg.isEmpty()){
			return;
		}
		
		noregInserted = noreg;
		medicalTrnCurrent = null;
		medicalRecordCurrent = null;
		
		lblNoreg.setValue("");
		lblPasienName.setValue("");
		lblRegisterDate.setValue("");
		lblPoli.setValue("");
		lblDoctor.setValue("");
		tbxAlergi.setValue("");
		tbxAnamnesis.setValue("");
		tbxDiagnosis.setValue("");
		tbxActionPlan.setValue("");
		tbxResep.setValue("");
		
		MedicalTransactionDao mtDao = new MedicalTransactionDao();
		mtDao.setSessionFactory(sessionFactory);
		
		Criterion crNoreg = Restrictions.eq("registrationNo", noreg);
		List<MedicalTransaction> mtList = mtDao.loadBy(Order.asc("registrationNo"), crNoreg);
		if(mtList.size()<1){
			Messagebox.show("Nomor Registrasi tidak ditemukan");
			
			return;
		}else{
			bltEast.setVisible(true);
			
			MedicalTransaction mt = mtList.get(0);
			medicalTrnCurrent = mt;
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(mt.getRegDate());
			lblNoreg.setValue(mt.getRegistrationNo());
			lblPasienName.setValue(mt.getPatient().getName());
			lblRegisterDate.setValue(CalendarUtil.dateIndonesia_DD_MMM_YYYY(cal));
			lblPoli.setValue(mt.getPoly().getPolyclinicName());
			lblDoctor.setValue(mt.getDoctor().getFullName());
			
			MedicalRecordsDao mrDao = new MedicalRecordsDao();
			mrDao.setSessionFactory(sessionFactory);
			
			List<MedicalRecords> mrList = mrDao.loadBy(Order.asc("medicalRecordsId"), crNoreg);
			if(mrList.size()>0){
				MedicalRecords mr = mrList.get(0);
				medicalRecordCurrent = mr;
				
				tbxAlergi.setValue(mr.getAllergy());
				tbxAnamnesis.setValue(mr.getAnamnesis());
				tbxDiagnosis.setValue(mr.getDiagnosis());
				tbxActionPlan.setValue(mr.getActionPlan());
				tbxResep.setValue(mr.getPrescription());
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
		mr.setRegistrationNo(noregInserted);
		mr.setMedicalTransaction(medicalTrnCurrent);
		mr.setAllergy(alergi);
		mr.setAnamnesis(anamnesis);
		mr.setDiagnosis(diagnosa);
		mr.setActionPlan(actionPlan);
		mr.setActionPlan(actionPlan);
		mr.setPrescription(resep);
		mr.setPatient(medicalTrnCurrent.getPatient());
		
		if(mrDao.saveOrUpdate(mr)){
			Messagebox.show("Rekam medis sukses disimpan");
		}else{
			Messagebox.show("Rekam medis gagal disimpan");
		}
	}
	
}
