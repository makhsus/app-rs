package com.rs.composer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
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
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import com.rs.dao.AdmissionDao;
import com.rs.dao.ContentConfigDao;
import com.rs.dao.PatientDao;
import com.rs.model.Admission;
import com.rs.model.ContentConfig;
import com.rs.model.Patient;
import com.rs.model.Users;
import com.rs.util.CommonUtil;
import com.rs.util.JavaScriptUtil;

public class AdmissionComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;

	private List<Patient> patientList;
	private Users loggedUser;
	
	private String patientId = null;
	private Patient selectedPatient;
	
	
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
		Window windowAdd = (Window) Executions.createComponents("/WEB-INF/zul/admission/save_update_patient.zul", null, null);
        windowAdd.doModal();
	}
	
	@Listen("onClick = #btnEditPatient")
	public void clickButtonEditPatient(){
		Window windowAdd = (Window) Executions.createComponents("/WEB-INF/zul/admission/save_update_patient.zul", null, null);
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
	
	@Listen ("onClick = #btnSaveSubmit")
	public void btnSaveSubmitClick(){
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
						lbxPatient.setVisible(false);
						
						setDetailPatientValue(patient);
					}
				}
			}
		});
	}
	
}
