package com.rs.composer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import com.rs.dao.AdmissionDao;
import com.rs.dao.ContentConfigDao;
import com.rs.dao.MedicalRecordsDao;
import com.rs.dao.MedicalTransactionDao;
import com.rs.dao.PatientDao;
import com.rs.dao.PolyclinicDao;
import com.rs.dao.PracticeScheduleDao;
import com.rs.dao.UniqueNumberDao;
import com.rs.model.Admission;
import com.rs.model.ContentConfig;
import com.rs.model.Employee;
import com.rs.model.MedicalRecords;
import com.rs.model.MedicalTransaction;
import com.rs.model.Patient;
import com.rs.model.Polyclinic;
import com.rs.model.PracticSchedule;
import com.rs.model.UniqueNumber;
import com.rs.model.Users;
import com.rs.util.CommonUtil;
import com.rs.util.JavaScriptUtil;

public class FrontDeskComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;

	private List<Patient> patientList;
	private List<Polyclinic> polyList;
	private Users loggedUser;
	
	private String patientId = null;
	private Patient selectedPatient;
	private Polyclinic selectedPoly;
	private PracticSchedule selectedSchedule;
	
	
	@Wire
	private Panel pnlRRJ, pnlRRI, pnlIGD, pnlLAB, pnlPatientSearchList;
	@Wire
	private Listbox lbxPatient;
	@Wire
	private Listbox lbxRRJ, lbxRRI, lbxIGD, lbxLAB;
	@Wire
	private Groupbox boxPatient, boxEmpty;
	@Wire
	private Vbox boxPatientDetail;
	@Wire
	private Textbox tbxSearchCardNo, tbxSearchName;
	@Wire
	private Label lblPatientName, lblPatientCardNo, lblPatientGender, lblPatientBirthdate, lblPatientAge, lblPatientReligion, lblPatientMaritalStatus, lblPatientHusbandWifeName, lblPatientOccupation, lblPatientEducation, lblPatientPhone, lblPatientParent, lblPatientAddress, lblEmptyDetail;
	
	
	@Wire
	private Window winSaveUpdatePatient;
	@Wire
	private Listbox lbxReligion, lbxEducation, lbxOccupation;
	@Wire
	private Textbox tbxId, tbxCardNo, tbxName, tbxHusbandWifeName, tbxParentName, tbxAddress, tbxPhone, tbxOtherReligion, tbxOtherEducation, tbxOtherOccupation;
	@Wire
	private Datebox dtbBirthdate;
	@Wire
	private Radiogroup rgGender, rgMaritalStatus, rgHusbandWife;
	@Wire
	private Label lblAge;
	
	
	@Wire
	private Window winSaveUpdateRRJ;
	@Wire
	private Listbox lbxPolyRRJ;
	@Wire
	private Button btnScheduleRRJ;
	@Wire
	private Label lblPatientRRJ, lblDoctorRRJ, lblHoursRRJ;
	
	@Wire
	private Window winDoctorPracticeSchedule;
	@Wire
	private Auxheader axrSchedule;
	@Wire
	private Radiogroup rgSchedule;
	@Wire
	private Rows rwsSchedule;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		
		Execution execution = Executions.getCurrent();
		patientId = execution.getParameter("patientId");
		
	}
	
	@Listen ("onCreate = #win")
	public void onCreateWin(){
		isLooged();
		loggedUser = (Users) sessionZk.getAttribute(CommonUtil.LOGIN_USER);
		
		subscribeToEventQueues("onPatientQueue");
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
	
	
	@Listen ("onCreate = #pnlRRJ")
	public void onCreatePanelRRJ(){
		pnlRRJ.setTitle("Daftar Rawat Jalan - "+CommonUtil.dateFormat(new Date(), "EEEE, dd MMM yyyy", new Locale("id", "ID")));
	}
	
	@Listen ("onCreate = #pnlRRI")
	public void onCreatePanelRRI(){
		pnlRRI.setTitle("Daftar Rawat Inap - "+CommonUtil.dateFormat(new Date(), "EEEE, dd MMM yyyy", new Locale("id", "ID")));
	}
	
	@Listen ("onCreate = #pnlIGD")
	public void onCreatePanelIGD(){
		pnlIGD.setTitle("Daftar Instalasi Gawat Darurat - "+CommonUtil.dateFormat(new Date(), "EEEE, dd MMM yyyy", new Locale("id", "ID")));
	}
	
	@Listen ("onCreate = #pnlLAB")
	public void onCreatePanelLAB(){
		pnlLAB.setTitle("Daftar Laboratorium - "+CommonUtil.dateFormat(new Date(), "EEEE, dd MMM yyyy", new Locale("id", "ID")));
	}
	
	
	private void loadAdmisiToListbox(){
//		lbxRRJ.getItems().clear();
//		
//		AdmissionDao dao = new AdmissionDao();
//		dao.setSessionFactory(sessionFactory);
//		List<Admission> list = dao.loadAll(Order.desc("admId"));
//		
//		lbxAdmissionRRJ.setModel(new ListModelList<>(list));
//		ListitemRenderer<Admission> renderer = new ListitemRenderer<Admission>() {
//			@Override
//			public void render(Listitem listitem, Admission obj, int index) throws Exception {
//				listitem.appendChild(new Listcell(obj.getRegistrationNo()));
//				listitem.appendChild(new Listcell(Integer.toString(obj.getSequenceNo())));
//				listitem.appendChild(new Listcell(obj.getPatient().getName()));
//				listitem.appendChild(new Listcell(obj.getDoctor().getFullName()));
//				listitem.appendChild(new Listcell(obj.getPoly().getPolyclinicCode()));
//				listitem.appendChild(new Listcell(CommonUtil.dateFormat(obj.getAdmDate(), "dd MMM yyyy")));
//			}
//		};
//		lbxAdmissionRRJ.setItemRenderer(renderer);
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
		String titleStr = "Daftar Pasien untuk pencarian ";
		
		if (!cardNo.equalsIgnoreCase("")){
			titleStr += "nomor kartu "+ "\"" +cardNo + "\"";
			
			if (!name.equalsIgnoreCase("")){
				titleStr += " dan ";
			}
		}
		if (!name.equalsIgnoreCase("")){
			titleStr += "nama "+ "\"" +name + "\"";
		}
		
		pnlPatientSearchList.setTitle(titleStr);
		
		
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
			
			Listcell lc = new Listcell(obj.getName());
			li.appendChild(lc);
			lc = new Listcell(obj.getCardNumber());			
			li.appendChild(lc);
			lc = new Listcell(obj.getGenderString());
			li.appendChild(lc);
			lc = new Listcell(obj.getPhone());
			li.appendChild(lc);
			lc = new Listcell(obj.getAddress());
			li.appendChild(lc);
		}
		
		
		patientList = list;
		if( list.size() > 0){
			boxPatient.setVisible(true);
			boxEmpty.setVisible(false);
			
			if (list.size() == 1){
				boxPatientDetail.setVisible(true);
				pnlPatientSearchList.setVisible(false);
				
				setDetailPatientValue(list.get(0));
			}else{
				boxPatientDetail.setVisible(false);
				pnlPatientSearchList.setVisible(true);
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
			
			emptyDetail += " tidak dapat ditemukan";
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
		this.selectedPatient = patient;
		
		String husbandWifeName = "-";
		if (patient.getHusbandName() != null && patient.getWifeName() == null){
			husbandWifeName = patient.getHusbandName();
		}
		else if (patient.getWifeName() != null && patient.getHusbandName() == null){
			husbandWifeName = patient.getWifeName();
		}
		
		lblPatientName.setValue(patient.getName() != null ? patient.getName() : "-");
		lblPatientCardNo.setValue("No. Kartu RS : " + (patient.getCardNumber() != null ? patient.getCardNumber() : "-"));
		lblPatientGender.setValue(patient.getGender() != null ? patient.getGenderString() : "-");
		lblPatientBirthdate.setValue(patient.getBirthdate() != null ? CommonUtil.dateFormat(patient.getBirthdate(), "dd MMM yyyy", new Locale("id", "ID")) : "-");
		lblPatientAge.setValue(patient.getBirthdate() != null ? CommonUtil.getAge(patient.getBirthdate()) : "-");
		lblPatientReligion.setValue(patient.getReligion() != null ? patient.getReligion() : "-");
		lblPatientMaritalStatus.setValue(patient.getMaritalStatus() != null ? patient.getMaritalStatus() : "-");
		lblPatientHusbandWifeName.setValue(husbandWifeName);
		lblPatientOccupation.setValue(patient.getOccupation() != null ? patient.getOccupation() : "-");
		lblPatientEducation.setValue(patient.getLastStudy() != null ? patient.getLastStudy() : "-");
		lblPatientPhone.setValue(patient.getPhone() != null ? patient.getPhone() : "-");
		lblPatientParent.setValue(patient.getParentName() != null ? patient.getParentName() : "-");
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
	
	@Listen("onClick = #btnAddPatient")
	public void clickButtonAddPatient(){
		Window windowAdd = (Window) Executions.createComponents("/WEB-INF/zul/frontdesk/save_update_patient.zul", null, null);
        windowAdd.doModal();
	}
	
	@Listen("onClick = #btnEditPatient")
	public void clickButtonEditPatient(){
		Window windowAdd = (Window) Executions.createComponents("/WEB-INF/zul/frontdesk/save_update_patient.zul", null, null);
		windowAdd.setAttribute("patient", selectedPatient);
        windowAdd.doModal();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onClick = #btnDeletePatient")
	public void clickButtonDeletePatient(){
		Messagebox.show("Nama : "+selectedPatient.getName()+"\n"+
				"No. Kartu : "+selectedPatient.getCardNumber()+"\n\n"+
				"Anda yakin ingin menghapus pasien?", 
				"Hapus Pasien", Messagebox.YES | Messagebox.NO,Messagebox.QUESTION, new EventListener(){
			public void onEvent(Event e){
				if("onYes".equals(e.getName())){
					PatientDao dao = new PatientDao();
					dao.setSessionFactory(sessionFactory);
					if(dao.delete(selectedPatient)){
						Messagebox.show("Pasien berhasil dihapus", "Hapus Pasien", Messagebox.OK, Messagebox.EXCLAMATION);
						
						boxPatient.setVisible(false);
						boxEmpty.setVisible(false);
						btnSearchClick();
					}else{
						Messagebox.show("Gagal menghapus pasien", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
					}
				}
			}
		});
	}
	
	
	@Listen("onCreate = #winSaveUpdatePatient")
	public void onCreateWinSaveUpdatePatient(){
		isLooged();
		loggedUser = (Users) sessionZk.getAttribute(CommonUtil.LOGIN_USER);
		
		loadReligionToListbox();
		loadEducationToListbox();
		loadOccupationToListbox();
		
		Patient patient = (Patient) winSaveUpdatePatient.getAttribute("patient");
		if (patient != null){
			setPatientToView(patient);
		}
	}
	
	private void loadReligionToListbox(){
		ContentConfigDao dao = new ContentConfigDao();
		dao.setSessionFactory(sessionFactory);
		
		List<ContentConfig> list = dao.getDataReligion();
		
		lbxReligion.setModel(new ListModelList<>(list));
		ListitemRenderer<ContentConfig> renderer = new ListitemRenderer<ContentConfig>() {
			@Override
			public void render(Listitem item, ContentConfig obj, int index) throws Exception {
				item.appendChild(new Listcell(obj.getValue1()));
			}
		};
		lbxReligion.setItemRenderer(renderer);
		lbxReligion.setSelectedIndex(0);
	}
	
	private void loadEducationToListbox(){
		ContentConfigDao dao = new ContentConfigDao();
		dao.setSessionFactory(sessionFactory);
		
		List<ContentConfig> list = dao.getDataEducation();
		
		lbxEducation.setModel(new ListModelList<>(list));
		ListitemRenderer<ContentConfig> renderer = new ListitemRenderer<ContentConfig>() {
			@Override
			public void render(Listitem item, ContentConfig obj, int index) throws Exception {
				item.appendChild(new Listcell(obj.getValue1()));
			}
		};
		lbxEducation.setItemRenderer(renderer);
		lbxEducation.setSelectedIndex(0);
	}
	
	private void loadOccupationToListbox(){
		ContentConfigDao dao = new ContentConfigDao();
		dao.setSessionFactory(sessionFactory);
		
		List<ContentConfig> list = dao.getDataWork();
		
		lbxOccupation.setModel(new ListModelList<>(list));
		ListitemRenderer<ContentConfig> renderer = new ListitemRenderer<ContentConfig>() {
			@Override
			public void render(Listitem item, ContentConfig obj, int index) throws Exception {
				item.appendChild(new Listcell(obj.getValue1()));
			}
		};
		lbxOccupation.setItemRenderer(renderer);
		lbxOccupation.setSelectedIndex(0);
	}
	
	@Listen ("onSelect = #lbxReligion")
	public void lbxReligionSelect(){
		if (lbxReligion.getSelectedIndex() == lbxReligion.getListModel().getSize()-1)
			tbxOtherReligion.setVisible(true);
		else
			tbxOtherReligion.setVisible(false);
	}
	
	@Listen ("onSelect = #lbxEducation")
	public void lbxEducationSelect(){
		if (lbxEducation.getSelectedIndex() == lbxEducation.getListModel().getSize()-1)
			tbxOtherEducation.setVisible(true);
		else
			tbxOtherEducation.setVisible(false);
	}
	
	@Listen ("onSelect = #lbxOccupation")
	public void lbxOccupationSelect(){
		if (lbxOccupation.getSelectedIndex() == lbxOccupation.getListModel().getSize()-1)
			tbxOtherOccupation.setVisible(true);
		else
			tbxOtherOccupation.setVisible(false);
	}
	
	@Listen ("onChange = #dtbBirthdate")
	public void dtbBirthdateChange(){
		if (dtbBirthdate.getValue() != null){
			lblAge.setValue(CommonUtil.getAge(dtbBirthdate.getValue()));
		}
	}
	
	@Listen ("onClick = #btnSubmitPatient")
	public void btnSubmitPatientClick(){
		String jqCommand = JavaScriptUtil.shakeWindow("winSaveUpdatePatient");
		
		if (tbxCardNo.getText().equalsIgnoreCase("")){
			Clients.evalJavaScript(jqCommand);
		}
		else if (tbxName.getText().equalsIgnoreCase("")){
			Clients.evalJavaScript(jqCommand);
		}
		else if (dtbBirthdate.getValue() == null){
			Clients.evalJavaScript(jqCommand);
		}
		else if (tbxName.getText().equalsIgnoreCase("")){
			Clients.evalJavaScript(jqCommand);
		}
		else if (tbxOtherReligion.isVisible() && tbxOtherReligion.getText().equalsIgnoreCase("")){
			Clients.evalJavaScript(jqCommand);
		}
		else if (tbxAddress.getText().equalsIgnoreCase("")){
			Clients.evalJavaScript(jqCommand);
		}
		else if (tbxOtherEducation.isVisible() && tbxOtherEducation.getText().equalsIgnoreCase("")){
			Clients.evalJavaScript(jqCommand);
		}
		else if (tbxOtherOccupation.isVisible() && tbxOtherOccupation.getText().equalsIgnoreCase("")){
			Clients.evalJavaScript(jqCommand);
		}
		else{
			Patient patient = new Patient();
			
			if (tbxId.getText() != null && !tbxId.getText().equalsIgnoreCase("")){
				patient.setId(Long.parseLong(tbxId.getText()));
			}
			patient.setCardNumber(tbxCardNo.getText());
			patient.setName(tbxName.getText());
			patient.setGender(rgGender.getSelectedIndex() == 0 ? "M" : "F");
			patient.setMaritalStatus(rgMaritalStatus.getSelectedItem().getLabel());
			patient.setHusbandName( !tbxHusbandWifeName.isDisabled() && rgHusbandWife.getSelectedIndex() == 0 ? tbxHusbandWifeName.getText() : "");
			patient.setWifeName( !tbxHusbandWifeName.isDisabled() && rgHusbandWife.getSelectedIndex() == 1 ? tbxHusbandWifeName.getText() : "");
			patient.setParentName(tbxParentName.getText() != null ? tbxParentName.getText() : "");
			patient.setBirthdate(dtbBirthdate.getValue());
			patient.setReligion(tbxOtherReligion.isVisible() ? tbxOtherReligion.getText() : lbxReligion.getSelectedItem().getLabel());
			patient.setAddress(tbxAddress.getText());
			patient.setLastStudy(tbxOtherEducation.isVisible() ? tbxOtherEducation.getText() : lbxEducation.getSelectedItem().getLabel());
			patient.setOccupation(tbxOtherOccupation.isVisible() ? tbxOtherOccupation.getText() : lbxOccupation.getSelectedItem().getLabel());
			patient.setPhone(tbxPhone.getText());
			
			patient.setCreatedBy(loggedUser);
			patient.setCreatedDate(new Date());
			
			PatientDao dao = new PatientDao();
			dao.setSessionFactory(sessionFactory);
			if(dao.saveOrUpdate(patient)){
				winSaveUpdatePatient.detach();
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("patient", patient);
				EventQueues.lookup("onPatientQueue", EventQueues.DESKTOP, true).publish(new Event("patientDidSave", null, map));
			}else{
				Clients.evalJavaScript(jqCommand);
				Messagebox.show("Gagal menyimpan data", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			}
		}
	}
	
	
	public void setPatientToView(Patient patient){
		
		String id = patient.getId().toString() != null ? patient.getId().toString() : "";
		String cardNo = patient.getCardNumber() != null ? patient.getCardNumber() : "";
		String name = patient.getName() != null ? patient.getName().toUpperCase() : "";
		String gender = patient.getGender() != null ? patient.getGender() : "";
		String maritalStatus = patient.getMaritalStatus() != null ? patient.getMaritalStatus() : "";
		String husbandName = patient.getHusbandName() != null ? patient.getHusbandName() : "";
		String wifeName = patient.getWifeName() != null ? patient.getWifeName() : "";
		String parentName = patient.getParentName() != null ? patient.getParentName() : "";
		Date birthdate = patient.getBirthdate() != null ? patient.getBirthdate() : null;
		String age = birthdate != null ? CommonUtil.getAge(birthdate) : "-";
		String religion = patient.getReligion() != null ? patient.getReligion() : "";
		String address = patient.getAddress() != null ? patient.getAddress() : "";
		String education = patient.getLastStudy() != null ? patient.getLastStudy() : "";
		String occupation = patient.getOccupation() != null ? patient.getOccupation() : "";
		String phone = patient.getPhone() != null ? patient.getPhone() : "";
		
		int rIndex = 0;
		for (int i=0 ; i<lbxReligion.getListModel().getSize(); i++){
			rIndex = i;
			ContentConfig config = (ContentConfig) lbxReligion.getListModel().getElementAt(i); 
			if (religion.equalsIgnoreCase(config.getValue1())){
				break;
			}
		}
		
		int eIndex = 0;
		for (int i=0 ; i<lbxEducation.getListModel().getSize(); i++){
			eIndex = i;
			ContentConfig config = (ContentConfig) lbxEducation.getListModel().getElementAt(i); 
			if (education.equalsIgnoreCase(config.getValue1())){
				break;
			}
		}
		
		int oIndex = 0;
		for (int i=0 ; i<lbxOccupation.getListModel().getSize(); i++){
			oIndex = i;
			ContentConfig config = (ContentConfig) lbxOccupation.getListModel().getElementAt(i); 
			if (occupation.equalsIgnoreCase(config.getValue1())){
				break;
			}
		}
		
		tbxId.setText(id);
		tbxCardNo.setText(cardNo);
		tbxName.setText(name);
		rgGender.setSelectedIndex( gender.equalsIgnoreCase("F") ? 1 : 0 );
		rgMaritalStatus.setSelectedIndex( maritalStatus.equalsIgnoreCase("Menikah") ? 1 : 0 );
		tbxHusbandWifeName.setText(husbandName.equalsIgnoreCase("") ? wifeName : husbandName);
		tbxParentName.setText(parentName);
		dtbBirthdate.setValue(birthdate);
		lblAge.setValue(age);
		lbxReligion.setSelectedIndex(rIndex);
		tbxAddress.setText(address);
		lbxEducation.setSelectedIndex(eIndex);
		lbxOccupation.setSelectedIndex(oIndex);
		tbxPhone.setText(phone);
		
		
	}
	
	@Listen("onClick = #btnRegRRJ")
	public void clickButtonRegisterRRJ(){
		Window windowAdd = (Window) Executions.createComponents("/WEB-INF/zul/frontdesk/save_update_rrj.zul", null, null);
		windowAdd.setAttribute("patient", selectedPatient);
        windowAdd.doModal();
	}
	
	
	@Listen("onCreate = #winSaveUpdateRRJ")
	public void onCreateWinSaveUpdateRRJ(){
		isLooged();
		loggedUser = (Users) sessionZk.getAttribute(CommonUtil.LOGIN_USER);
		
		subscribeToEventQueues("onPracticeScheduleQueue");
		loadPolyToListbox();
		
		selectedPatient = (Patient) winSaveUpdateRRJ.getAttribute("patient");
		if (selectedPatient != null){
			lblPatientRRJ.setValue(selectedPatient.getName());
		}
	}
	
	
	private void loadPolyToListbox(){
		lbxPolyRRJ.getItems().clear();
		
		PolyclinicDao dao = new PolyclinicDao();
		dao.setSessionFactory(sessionFactory);
		Criterion cr1 = Restrictions.eq("isActive", true);
		List<Polyclinic> list = dao.loadBy(Order.asc("polyclinicId"), cr1);
		
		lbxPolyRRJ.setModel(new ListModelList<>(list));
		ListitemRenderer<Polyclinic> renderer = new ListitemRenderer<Polyclinic>() {
			@Override
			public void render(Listitem item, Polyclinic obj, int index) throws Exception {
				item.appendChild(new Listcell(obj.getPolyclinicName()));
			}
		};
		lbxPolyRRJ.setItemRenderer(renderer);
		
		polyList = list;
	}
	
	
	@Listen ("onSelect = #lbxPolyRRJ")
	public void lbxPolyRRJSelect(){
		if(polyList!=null){
			int index = lbxPolyRRJ.getSelectedIndex();
			selectedPoly = polyList.get(index);
		}
	}
	
	@Listen("onClick = #btnScheduleRRJ")
	public void onClickButtonScheduleRRJ(){
		if(selectedPoly==null){
			Messagebox.show("Silahkan pilih poli", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		
		Window windowAdd = (Window) Executions.createComponents("/WEB-INF/zul/frontdesk/doctor_practice_schedule.zul", null, null);
		windowAdd.setAttribute("selectedPoly", selectedPoly);
		windowAdd.doModal();
	}
	
	@Listen ("onClick = #btnSubmitRRJ")
	public void btnSubmitRRJClick(){
		if(selectedPatient==null){
			Messagebox.show("Silahkan pilih pasien", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		if(selectedPoly==null){
			Messagebox.show("Silahkan pilih poli", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		if(selectedSchedule==null){
			Messagebox.show("Silahkan pilih jadwal", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		
		Calendar calNow = Calendar.getInstance(CommonUtil.JAKARTA_TIMEZONE);
		
		UniqueNumber regNo = CommonUtil.generateUniqueNumber(sessionFactory, CommonUtil.CODE_FOR_REG_RAWAT_JALAN);
		UniqueNumberDao unDao = new UniqueNumberDao();
		unDao.setSessionFactory(sessionFactory);
		unDao.saveOrUpdate(regNo);
		
		int seqNo = 1;
		
		MedicalTransactionDao dao = new MedicalTransactionDao();
		dao.setSessionFactory(sessionFactory);
		
		Criterion crCekPatient1 = Restrictions.eq("regDate", calNow.getTime());
		Criterion crCekPatient2 = Restrictions.eq("poly", selectedPoly);
		Criterion crCekPatient3 = Restrictions.eq("doctor", selectedSchedule.getDoctor());
		List<MedicalTransaction> mrList = dao.loadBy(Order.asc("id"), crCekPatient1, crCekPatient2, crCekPatient3);
		if(mrList.size()>0){
			seqNo = mrList.size()+1;
		}
		
		
		
		MedicalTransaction obj = new MedicalTransaction();
		obj.setRegistrationNo(regNo.getUniqueNumber());
		obj.setSequenceNo(seqNo);
		obj.setPatient(selectedPatient);
		obj.setPoly(selectedPoly);
		obj.setDoctor(selectedSchedule.getDoctor());
		obj.setRegDate(calNow.getTime());
		obj.setStatus("RRJ");
		obj.setCreatedBy(loggedUser);
		obj.setCreatedDate(calNow.getTime());
		
		dao = new MedicalTransactionDao();
		dao.setSessionFactory(sessionFactory);
		if(dao.saveOrUpdate(obj)){
			winSaveUpdateRRJ.detach();
		}else{
			Messagebox.show("Save/Update Failed", "Error", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	@Listen ("onCreate = #winDoctorPracticeSchedule")
	public void winSelectScheduleCreate(){
		isLooged();
		
		Polyclinic poly = (Polyclinic) winDoctorPracticeSchedule.getAttribute("selectedPoly");
		axrSchedule.setLabel("Poli "+poly.getPolyclinicName());
		
		Session session = sessionFactory.openSession();
		Criteria cr = session.createCriteria(PracticSchedule.class);
		cr.add(Restrictions.eq("polyclinic", poly));
		cr.setProjection(Projections.distinct(Projections.property("doctor")));
		@SuppressWarnings("unchecked")
		List<Employee> epList = cr.list();
		session.close();
		
		int day = CommonUtil.dayNumberConvertFromJavaCalendar();
		for(Employee ep: epList){
			PracticeScheduleDao dao = new PracticeScheduleDao();
			dao.setSessionFactory(sessionFactory);
			Criterion crPs1 = Restrictions.eq("polyclinic", poly);
			Criterion crPs2 = Restrictions.eq("doctor", ep);
			Criterion crPs3 = Restrictions.like("days", "%"+day+"%");
			List<PracticSchedule> psList = dao.loadBy(Order.asc("startTime"), crPs1, crPs2, crPs3);
			
			int psSize = psList.size();
			for(int i=0;i<psSize;i++){
				PracticSchedule ps = psList.get(i);
				String name = ep.getFullName();
				
				String jam = CommonUtil.dateFormat(ps.getStartTime(), "HH:mm")+"-"+
							 CommonUtil.dateFormat(ps.getEndTime(), "HH:mm");
				
				Radio radio = new Radio();
				radio.setId("radio"+ps.getId());
				radio.setRadiogroup(rgSchedule);
				radio.setValue(ps);
				
				if(psSize>1){
					if(i==0){
						Row row = new Row();
						row.appendChild(radio);
						row.appendChild(new Label(name));
						row.appendChild(new Label(jam));
						rwsSchedule.appendChild(row);
					}else{
						Row row = new Row();
						row.appendChild(radio);
						row.appendChild(new Label());
						row.appendChild(new Label(jam));
						rwsSchedule.appendChild(row);
					}
				}else{
					Row row = new Row();
					row.appendChild(radio);
					row.appendChild(new Label(name));
					row.appendChild(new Label(jam));
					rwsSchedule.appendChild(row);
				}
			}
		}
	}
	
	@Listen ("onCheck = #rgSchedule")
	public void rgScheduleClick(){
		selectedSchedule = rgSchedule.getSelectedItem().getValue();
	}
	
	@Listen ("onClick = #btnSubmitPracticeSchedule")
	public void btnSubmitSelectSchedueClick(){
		if(selectedSchedule==null){
			Messagebox.show("Silahkan pilih jadwal praktek yang tersedia", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("selectedSchedule", selectedSchedule);
		EventQueues.lookup("onPracticeScheduleQueue", EventQueues.DESKTOP, true).publish(new Event("scheduleDidSelected", null, map));
		
		winDoctorPracticeSchedule.detach();
	}
	
//	private void loadPracticeScheduleToListbox(){
//		lbxPolyRRJ.getItems().clear();
//		
//		PolyclinicDao dao = new PolyclinicDao();
//		dao.setSessionFactory(sessionFactory);
//		Criterion cr1 = Restrictions.eq("isActive", true);
//		List<Polyclinic> list = dao.loadBy(Order.asc("polyclinicId"), cr1);
//		
//		lbxPolyRRJ.setModel(new ListModelList<>(list));
//		ListitemRenderer<Polyclinic> renderer = new ListitemRenderer<Polyclinic>() {
//			@Override
//			public void render(Listitem item, Polyclinic obj, int index) throws Exception {
//				item.appendChild(new Listcell(obj.getPolyclinicName()));
//			}
//		};
//		lbxPolyRRJ.setItemRenderer(renderer);
//	}
	
	public void subscribeToEventQueues(final String eventQueueName){
		EventQueues.lookup(eventQueueName, EventQueues.DESKTOP, true).subscribe(new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if(eventQueueName.equals("onPatientQueue")){
					if(event.getName().equals("patientDidSave")){
						@SuppressWarnings("unchecked")
						Map<String, Object> map = (Map<String, Object>) event.getData();
						Patient patient = (Patient) map.get("patient");
						
						boxPatient.setVisible(true);
						boxEmpty.setVisible(false);
						boxPatientDetail.setVisible(true);
						pnlPatientSearchList.setVisible(false);
						
						setDetailPatientValue(patient);
					}
				}
				
				else if(eventQueueName.equals("onPracticeScheduleQueue")){
					if(event.getName().equals("scheduleDidSelected")){
						@SuppressWarnings("unchecked")
						Map<String, Object> map = (Map<String, Object>) event.getData();
						selectedSchedule = (PracticSchedule) map.get("selectedSchedule");
						
						String jam = CommonUtil.dateFormat(selectedSchedule.getStartTime(), "HH:mm")+" - "+
								 CommonUtil.dateFormat(selectedSchedule.getEndTime(), "HH:mm");
						
						lblDoctorRRJ.setValue(selectedSchedule.getDoctor().getFullName());
						lblHoursRRJ.setValue(jam);
					}
				}
			}
		});
	}
	
}
