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
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Grid;
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
import org.zkoss.zul.Window;

import com.rs.dao.AdmissionDao;
import com.rs.dao.MedicalRecordsDao;
import com.rs.dao.PatientDao;
import com.rs.dao.PolyclinicDao;
import com.rs.dao.PracticeScheduleDao;
import com.rs.dao.UniqueNumberDao;
import com.rs.model.Admission;
import com.rs.model.Employee;
import com.rs.model.MedicalRecords;
import com.rs.model.Patient;
import com.rs.model.Polyclinic;
import com.rs.model.PracticSchedule;
import com.rs.model.UniqueNumber;
import com.rs.model.Users;
import com.rs.util.CommonUtil;

public class AdmissionComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;

	private List<Patient> patientList;
	private List<Polyclinic> polyList;
	private List<String> admCategoryList;
	private List<Admission> admissionList;
	private Users loggedUser;
	private Patient patientSelected;
	private Polyclinic polySelected;
	private PracticSchedule scheduleSelectedFromHome;
	private PracticSchedule scheduleSelectedFromSelection;
	private Admission admissionSelected;
	private Admission admissionAddEdit;
	
	private String patternJam = "HH:mm";
	private String recordStatusNew = "NEW";
	
	private String patientId = null;
	
	
	@Wire
	private Window winSelectSchedule;
	@Wire
	private Grid grdAddEdit;
	@Wire
	private Listbox lbxPasien, lbxPoly, lbxAdmission;
	@Wire 
	private Bandbox bdxPasien;
	@Wire 
	private Radiogroup rgSchedule;
	@Wire
	private Auxheader axrSchedule;
	@Wire
	private Rows rwsSchedule;
	@Wire
	private Label lblDokter, lblJam, lblRegNo, lblNamaPasien, lblAlergi, lblAnamnesi, lblDiagnosa, lblPlan, lblResepObat;
	@Wire
	private Textbox tbxAlergi, tbxAnamnesi, tbxDiagnosa, tbxPlan, tbxResepObat;
	
	@Wire
	private Panel pnlAdmissionRRJ, pnlAdmissionRRI, pnlAdmissionIGD, pnlAdmissionLAB;
	
	@Wire
	private Listbox lbxAdmissionRRJ, lbxAdmissionRRI, lbxAdmissionIGD, lbxAdmissionLAB;
	
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
		
		
		subscribeToEventQueues("onSubmit");
		loadDataPasienToListbox();
		loadPolyToListbox();
		loadAdmisiToListbox();
		loadAdmisiCategoryToListbox();
		
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
				tbnAddClick();
				patientSelected = patient;
				bdxPasien.setValue(patientSelected.getName()+" - "+patientSelected.getCardNumber());
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
	
	@Listen ("onClick = #tbnList")
	public void tbnListClick(){
		grdAddEdit.setVisible(false);
	}
	
	@Listen ("onClick = #tbnAdd")
	public void tbnAddClick(){
		grdAddEdit.setVisible(true);
	}
	
	@Listen ("onSelect = #lbxPasien")
	public void lbxPasienSelect(){
		if(patientList!=null){
			int index = lbxPasien.getSelectedIndex();
			patientSelected = patientList.get(index);
			bdxPasien.setValue(patientSelected.getName()+" - "+patientSelected.getCardNumber());
		}
	}
	
	@Listen ("onSelect = #lbxPoly")
	public void lbxPolySelect(){
		if(polyList!=null){
			int index = lbxPoly.getSelectedIndex();
			polySelected = polyList.get(index);
		}
	}
	
	@Listen ("onClick = #btnPilihJadwal")
	public void btnPilihJadwalClick(){
		if(polySelected==null){
			Messagebox.show("Silahkan pilih poli", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		
		Window windowAdd = (Window) Executions.createComponents("/WEB-INF/zul/admisi/select_schedule.zul", null, null);
		windowAdd.setAttribute("polySelected", polySelected);
		windowAdd.doModal();
	}
	
	@Listen ("onClick = #btnSubmitAdmisi")
	public void btnSubmitAdmisiClick(){
		if(patientSelected==null){
			Messagebox.show("Silahkan pilih pasien", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		if(polySelected==null){
			Messagebox.show("Silahkan pilih poli", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		if(scheduleSelectedFromHome==null){
			Messagebox.show("Silahkan pilih jadwal", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		
		Calendar calNow = Calendar.getInstance(CommonUtil.JAKARTA_TIMEZONE);
		
		UniqueNumber regNo = CommonUtil.generateUniqueNumber(sessionFactory, CommonUtil.CODE_FOR_REG_RAWAT_JALAN);
		UniqueNumberDao unDao = new UniqueNumberDao();
		unDao.setSessionFactory(sessionFactory);
		unDao.saveOrUpdate(regNo);
		
		String jam = CommonUtil.dateFormat(scheduleSelectedFromHome.getStartTime(), patternJam)+" - "+
				 CommonUtil.dateFormat(scheduleSelectedFromHome.getEndTime(), patternJam);
		
		int seqNo = 1;
		
		AdmissionDao dao = new AdmissionDao();
		dao.setSessionFactory(sessionFactory);
		
		Criterion crCekPatient1 = Restrictions.eq("admDate", calNow.getTime());
		Criterion crCekPatient2 = Restrictions.eq("poly", polySelected);
		Criterion crCekPatient3 = Restrictions.eq("doctor", scheduleSelectedFromHome.getDoctor());
		List<Admission> mrList = dao.loadBy(Order.asc("admId"), crCekPatient1, crCekPatient2, crCekPatient3);
		if(mrList.size()>0){
			seqNo = mrList.size()+1;
		}
		
		
		Admission obj = new Admission();
		obj.setRegistrationNo(regNo.getUniqueNumber());
		obj.setSequenceNo(seqNo);
		obj.setPatient(patientSelected);
		obj.setPoly(polySelected);
		obj.setDoctor(scheduleSelectedFromHome.getDoctor());
		obj.setAdmDate(calNow.getTime());
		obj.setCreatedBy(loggedUser);
		obj.setCreatedDate(calNow.getTime());
		
		dao = new AdmissionDao();
		dao.setSessionFactory(sessionFactory);
		if(dao.saveOrUpdate(obj)){
			clearTambahAdmisi();
			loadAdmisiToListbox();
		}else{
			Messagebox.show("Save/Update Failed", "Error", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	@Listen ("onClick = #btnClear")
	public void btnClearClick(){
		clearTambahAdmisi();
	}
	
	
	@Listen ("onClick = #tbnMedicalRecord")
	public void tbnMedicalRecordClick(){
//		if(admissionSelected==null){
//			Messagebox.show("Silahkan pilih admisi", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
//			return;
//		}
//		
//		String actionFor = "Add";
//		if(admissionSelected.getRecordStatus().equalsIgnoreCase(recordStatusNew)){
//			actionFor = "Add";
//		}
//		
//		Window windowAdd = (Window) Executions.createComponents("/WEB-INF/zul/admisi/medical_record.zul", null, null);
//		windowAdd.setAttribute("actionFor", actionFor);
//		windowAdd.setAttribute("admissionSelected", admissionSelected);
//		windowAdd.doModal();
	}
	
	@Listen ("onClick = #tbnStatus")
	public void tbnStatusClick(){
//		if(medicalRecordSelected==null){
//			Messagebox.show("Silahkan pilih admisi", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
//			return;
//		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Listen ("onCreate = #winSelectSchedule")
	public void winSelectScheduleCreate(){
		isLooged();
		
		Polyclinic poly = (Polyclinic) winSelectSchedule.getAttribute("polySelected");
		axrSchedule.setLabel("Poli "+poly.getPolyclinicName());
		
		Session session = sessionFactory.openSession();
		Criteria cr = session.createCriteria(PracticSchedule.class);
		cr.add(Restrictions.eq("polyclinic", poly));
		cr.setProjection(Projections.distinct(Projections.property("doctor")));
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
				
				String jam = CommonUtil.dateFormat(ps.getStartTime(), patternJam)+"-"+
							 CommonUtil.dateFormat(ps.getEndTime(), patternJam);
				
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
		scheduleSelectedFromSelection = rgSchedule.getSelectedItem().getValue();
	}
	
	@Listen ("onClick = #btnClose")
	public void btnCloseClick(){
		winSelectSchedule.detach();
	}
	
	@Listen ("onClick = #btnSubmitSelectSchedue")
	public void btnSubmitSelectSchedueClick(){
		if(scheduleSelectedFromSelection==null){
			Messagebox.show("Silahkan pilih jadwal praktek yang tersedia", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("scheduleSelected", scheduleSelectedFromSelection);
		EventQueues.lookup("onSubmit", EventQueues.DESKTOP, true).publish(new Event("selectSchedule", null, map));
	}
	
	
	@Listen ("onCreate = #winMedicalRecord")
	public void winMedicalRecordCreate(){
		isLooged();
		
//		String actionFor = (String) winMedicalRecord.getAttribute("actionFor");
//		admissionAddEdit = (Admission) winMedicalRecord.getAttribute("admissionAddEdit");
//		
//		String alergi = medicalRecordAddEdit.getAllergy();
//		String anamnesi = medicalRecordAddEdit.getAnamnesis();
//		String diagnosa = medicalRecordAddEdit.getDiagnosis();
//		String plan = medicalRecordAddEdit.getActionPlan();
//		String precription = medicalRecordAddEdit.getPrescription();
//		
//		if(actionFor.equalsIgnoreCase("Add")){
//			tbxAlergi.setVisible(true);
//			tbxAnamnesi.setVisible(true);
//			tbxDiagnosa.setVisible(true);
//			tbxPlan.setVisible(true);
//			tbxResepObat.setVisible(true);
//			lblAlergi.setVisible(false);
//			lblAnamnesi.setVisible(false);
//			lblDiagnosa.setVisible(false);
//			lblPlan.setVisible(false);
//			lblResepObat.setVisible(false);
//			tbxAlergi.setValue(alergi);
//			tbxAnamnesi.setValue(anamnesi);
//			tbxDiagnosa.setValue(diagnosa);
//			tbxPlan.setValue(plan);
//			tbxResepObat.setValue(precription);
//		}
//		
//		
//		lblRegNo.setValue(medicalRecordAddEdit.getRegistrationNo());
//		lblNamaPasien.setValue(medicalRecordAddEdit.getPatient().getName());
		
	}
	
	
	public void subscribeToEventQueues(final String eventQueueName){
		EventQueues.lookup(eventQueueName, EventQueues.DESKTOP, true).subscribe(new EventListener<Event>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onEvent(Event event) throws Exception {
				if(eventQueueName.equals("onSubmit")){
					if(event.getName().equals("selectSchedule")){
						Map<String, Object> map = (Map<String, Object>) event.getData();
						scheduleSelectedFromHome = (PracticSchedule) map.get("scheduleSelected");
						
						String jam = CommonUtil.dateFormat(scheduleSelectedFromHome.getStartTime(), patternJam)+" - "+
								 CommonUtil.dateFormat(scheduleSelectedFromHome.getEndTime(), patternJam);
						
						lblDokter.setValue(scheduleSelectedFromHome.getDoctor().getFullName());
						lblJam.setValue(jam);
					}
				}
			}
		});
	}
	
	private void loadAdmCategoryToListbox(){
		polySelected = null;
		lbxPoly.getItems().clear();
		
		PolyclinicDao dao = new PolyclinicDao();
		dao.setSessionFactory(sessionFactory);
		Criterion cr1 = Restrictions.eq("isActive", true);
		List<Polyclinic> list = dao.loadBy(Order.asc("polyclinicId"), cr1);
		polyList = list;
		
		lbxPoly.setModel(new ListModelList<>(list));
		ListitemRenderer<Polyclinic> renderer = new ListitemRenderer<Polyclinic>() {
			@Override
			public void render(Listitem item, Polyclinic obj, int index) throws Exception {
				item.appendChild(new Listcell(obj.getPolyclinicName()));
			}
		};
		lbxPoly.setItemRenderer(renderer);
	}
	
	private void loadDataPasienToListbox(){
		patientSelected = null;
		
		PatientDao dao = new PatientDao();
		dao.setSessionFactory(sessionFactory);
		List<Patient> list = dao.loadAll(Order.asc("name"));
		patientList = list;
		
		lbxPasien.setModel(new ListModelList<>(list));
		ListitemRenderer<Patient> renderer = new ListitemRenderer<Patient>() {
			@Override
			public void render(Listitem item, Patient obj, int index) throws Exception {
				item.appendChild(new Listcell(obj.getName()));
				item.appendChild(new Listcell(obj.getCardNumber()));
			}
		};
		lbxPasien.setItemRenderer(renderer);
	}
	
	private void loadPolyToListbox(){
		polySelected = null;
		lbxPoly.getItems().clear();
		
		PolyclinicDao dao = new PolyclinicDao();
		dao.setSessionFactory(sessionFactory);
		Criterion cr1 = Restrictions.eq("isActive", true);
		List<Polyclinic> list = dao.loadBy(Order.asc("polyclinicId"), cr1);
		polyList = list;
		
		lbxPoly.setModel(new ListModelList<>(list));
		ListitemRenderer<Polyclinic> renderer = new ListitemRenderer<Polyclinic>() {
			@Override
			public void render(Listitem item, Polyclinic obj, int index) throws Exception {
				item.appendChild(new Listcell(obj.getPolyclinicName()));
			}
		};
		lbxPoly.setItemRenderer(renderer);
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
	
	private void loadAdmisiCategoryToListbox(){
		lbxAdmission.getItems().clear();
		
		admCategoryList = new ArrayList<>();
		admCategoryList.add("Rawat Jalan");
		admCategoryList.add("Rawat Inap");
		admCategoryList.add("Instalasi Gawat Darurat");
		admCategoryList.add("Laboratorium");
		
		lbxAdmission.setModel(new ListModelList<>(admCategoryList));
		ListitemRenderer<String> renderer = new ListitemRenderer<String>() {
			@Override
			public void render(Listitem item, String obj, int index) throws Exception {
				item.appendChild(new Listcell(obj));
			}
		};
		lbxAdmission.setItemRenderer(renderer);
	}
	
	private void clearTambahAdmisi(){
		patientSelected = null;
		polySelected = null;
		scheduleSelectedFromHome = null;
		
		bdxPasien.setValue("");
		lbxPoly.setSelectedIndex(-1);
		lblDokter.setValue("");
		lblJam.setValue("");
	}
}
