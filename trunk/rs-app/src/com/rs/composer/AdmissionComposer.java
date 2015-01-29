package com.rs.composer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

import com.rs.dao.AdmissionDao;
import com.rs.dao.PatientDao;
import com.rs.model.Admission;
import com.rs.model.Patient;
import com.rs.model.Polyclinic;
import com.rs.model.PracticSchedule;
import com.rs.model.Users;
import com.rs.util.CommonUtil;

public class AdmissionComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;

	private List<Patient> patientList;
	private List<Polyclinic> polyList;
	private List<String> admCategoryList;
	private List<Admission> admissionList;
	private Users loggedUser;
	private Polyclinic polySelected;
	private PracticSchedule scheduleSelectedFromHome;
	private PracticSchedule scheduleSelectedFromSelection;
	private Admission admissionSelected;
	private Admission admissionAddEdit;
	
	private String patternJam = "HH:mm";
	private String recordStatusNew = "NEW";
	
	private String patientId = null;
	
	
	@Wire
	private Panel pnlAdmissionRRJ, pnlAdmissionRRI, pnlAdmissionIGD, pnlAdmissionLAB;
	
	@Wire
	private Listbox lbxPatient;
	
	@Wire
	private Listbox lbxAdmissionRRJ, lbxAdmissionRRI, lbxAdmissionIGD, lbxAdmissionLAB;
	
	@Wire
	private Groupbox boxPatient, boxEmpty;
	
	@Wire
	private Vbox boxPatientDetail;
	
	@Wire
	private Textbox tbxSearchCardNo, tbxSearchName;
	
	@Wire
	private Label lblPatientName, lblPatientCardNo, lblPatientGender, lblPatientBirthdate, lblPatientAge, lblPatientReligion, lblPatientOccupation, lblPatientAddress, lblEmptyDetail;
	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		
		Execution execution = Executions.getCurrent();
		patientId = execution.getParameter("patientId");
		
	}
	
	@Listen ("onCreate = #win")
	public void winCreate(){
		isLooged();
		loggedUser = (Users) sessionZk.getAttribute(CommonUtil.LOGIN_USER);
		
		
		boxPatient.setVisible(false);
		boxEmpty.setVisible(false);
		
		loadAdmisiToListbox();
		
		if (patientId != null && !patientId.equalsIgnoreCase("")){
			
			PatientDao dao = new PatientDao();
			dao.setSessionFactory(sessionFactory);
			
			Criterion criterion = Restrictions.eq("id", Long.parseLong(patientId));
			Patient patient;
			try{
				patient = dao.loadBy(Order.asc("name"), criterion).get(0);
			}catch(Exception e){
				e.printStackTrace();
				patient = null;
			}
			
			if (patient != null ){
				//tbnAddClick();
				//patientSelected = patient;
				//bdxPasien.setValue(patientSelected.getName()+" - "+patientSelected.getCardNumber());
			}
			
		}
	}
	
	
	@Listen ("onCreate = #pnlAdmissionRRJ")
	public void onCreatePanelAdmissionRRJ(){
		pnlAdmissionRRJ.setTitle("Daftar Rawat Jalan - "+CommonUtil.dateFormat(new Date(), "EEEE, dd MMM yyyy", new Locale("id", "ID")));
	}
	
	@Listen ("onCreate = #pnlAdmissionRRI")
	public void onCreatePanelAdmissionRRI(){
		pnlAdmissionRRI.setTitle("Daftar Rawat Inap - "+CommonUtil.dateFormat(new Date(), "EEEE, dd MMM yyyy", new Locale("id", "ID")));
	}
	
	@Listen ("onCreate = #pnlAdmissionIGD")
	public void onCreatePanelAdmissionIGD(){
		pnlAdmissionIGD.setTitle("Daftar Instalasi Gawat Darurat - "+CommonUtil.dateFormat(new Date(), "EEEE, dd MMM yyyy", new Locale("id", "ID")));
	}
	
	@Listen ("onCreate = #pnlAdmissionLAB")
	public void onCreatePanelAdmissionLAB(){
		pnlAdmissionLAB.setTitle("Daftar Laboratorium - "+CommonUtil.dateFormat(new Date(), "EEEE, dd MMM yyyy", new Locale("id", "ID")));
	}
	
	
	private void loadAdmisiToListbox(){
		lbxAdmissionRRJ.getItems().clear();
		
		AdmissionDao dao = new AdmissionDao();
		dao.setSessionFactory(sessionFactory);
		List<Admission> list = dao.loadAll(Order.desc("admId"));
		admissionList = list;
		
		lbxAdmissionRRJ.setModel(new ListModelList<>(list));
		ListitemRenderer<Admission> renderer = new ListitemRenderer<Admission>() {
			@Override
			public void render(Listitem listitem, Admission obj, int index) throws Exception {
				listitem.appendChild(new Listcell(obj.getRegistrationNo()));
				listitem.appendChild(new Listcell(Integer.toString(obj.getSequenceNo())));
				listitem.appendChild(new Listcell(obj.getPatient().getName()));
				listitem.appendChild(new Listcell(obj.getDoctor().getFullName()));
				listitem.appendChild(new Listcell(obj.getPoly().getPolyclinicCode()));
				listitem.appendChild(new Listcell(CommonUtil.dateFormat(obj.getAdmDate(), "dd MMM yyyy")));
			}
		};
		lbxAdmissionRRJ.setItemRenderer(renderer);
	}
	

	@Listen ("onClick = #btnSearch")
	public void btnSearchClick(){
		String cardNo = "";
		String name = "";
		
		cardNo = tbxSearchCardNo.getText().toString();
		name = tbxSearchName.getText().toString();
		
		if (!cardNo.equalsIgnoreCase("") || !name.equalsIgnoreCase("")){
			loadDataPatient(cardNo, name);
			
		}
	}
	
	
	private void loadDataPatient(String cardNo, String name){
		PatientDao dao = new PatientDao();
		dao.setSessionFactory(sessionFactory);
		
		
		
		Criterion criterionCard = Restrictions.like("cardNumber", cardNo+"%");
		Criterion criterionName = Restrictions.like("name", name+"%");
		
		
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
		
		
		
		
		lbxPatient.getItems().clear();
		for(Patient obj: list){
			Listitem li = new Listitem();
			lbxPatient.appendChild(li);
			
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
		
		
		patientList = list;
		if( list.size() > 0){
			boxPatient.setVisible(true);
			boxEmpty.setVisible(false);
			
			if (list.size() == 1){
				boxPatientDetail.setVisible(true);
				lbxPatient.setVisible(false);
				
				setDetailPatientValue(list.get(0));
			}else{
				boxPatientDetail.setVisible(false);
				lbxPatient.setVisible(true);
			}
		}else{
			boxPatient.setVisible(false);
			boxEmpty.setVisible(true);
			
			String emptyDetail = "Pasien dengan ";
			
			
			
			if (!cardNo.equalsIgnoreCase("")){
				emptyDetail += "nomor kartu "+ cardNo;
				
				if (!name.equalsIgnoreCase("")){
					emptyDetail += " dengan ";
				}
			}
			if (!name.equalsIgnoreCase("")){
				emptyDetail += "nama "+ name;
			}
			
			emptyDetail += " tidak dapat ditemukan.";
			lblEmptyDetail.setValue(emptyDetail);
		}
	}
	
	
	@Listen ("onSelect = #lbxPatient")
	public void lbxPatientSelect(){
		if(patientList!=null){
			int index = lbxPatient.getSelectedIndex();
			
			boxPatientDetail.setVisible(true);
			setDetailPatientValue(patientList.get(index));
		}
	}
	
	
	public void setDetailPatientValue(Patient patient){
		lblPatientName.setValue(patient.getName() != null ? patient.getName() : "-");
		lblPatientCardNo.setValue("No. Kartu RS : " + (patient.getCardNumber() != null ? patient.getCardNumber() : "-"));
		lblPatientGender.setValue(patient.getGender() != null ? patient.getGenderString() : "-");
		lblPatientBirthdate.setValue(patient.getBirthdate() != null ? CommonUtil.dateFormat(patient.getBirthdate(), "dd MMM yyyy", new Locale("id", "ID")) : "-");
		lblPatientAge.setValue(patient.getBirthdate() != null ? CommonUtil.getAge(patient.getBirthdate()) : "-");
		lblPatientReligion.setValue(patient.getReligion() != null ? patient.getReligion() : "-");
		lblPatientOccupation.setValue(patient.getOccupation() != null ? patient.getOccupation() : "-");
		lblPatientAddress.setValue(patient.getAddress() != null ? patient.getAddress() : "-");
	}
	
	// in your controller
	@Listen("onOK = #tbxSearchCardNo")
	public void onQueryCardNo(Event event) {
		btnSearchClick();
	}
	
	
	// in your controller
	@Listen("onOK = #tbxSearchName")
	public void onQueryName(Event event) {
		btnSearchClick();
	}
	
}
