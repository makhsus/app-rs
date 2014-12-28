package com.rs.composer;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Window;

import com.rs.dao.AdmisiDao;
import com.rs.dao.PatientDao;
import com.rs.dao.PolyclinicDao;
import com.rs.dao.PracticeScheduleDao;
import com.rs.model.Admisi;
import com.rs.model.Employee;
import com.rs.model.Patient;
import com.rs.model.Polyclinic;
import com.rs.model.PracticSchedule;
import com.rs.model.Users;
import com.rs.util.CommonUtil;

public class AdmisiComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;

	private List<Patient> patientList;
	private List<Polyclinic> polyList;
	private Users loggedUser;
	private Patient patientSelected;
	private Polyclinic polySelected;
	private PracticSchedule scheduleSelectedFromHome;
	private PracticSchedule scheduleSelectedFromSelection;
	
	
	@Wire
	private Window winSelectSchedule;
	@Wire
	private Grid grdAddEdit;
	@Wire
	private Listbox lbxList, lbxPasien, lbxPoly;
	@Wire 
	private Bandbox bdxPasien;
	@Wire 
	private Radiogroup rgSchedule;
	@Wire
	private Auxheader axrSchedule;
	@Wire
	private Rows rwsSchedule;
	@Wire
	private Label lblDokter, lblJam;
	
	
	@Listen ("onCreate = #win")
	public void win(){
		isLooged();
		loggedUser = (Users) sessionZk.getAttribute(CommonUtil.LOGIN_USER);
		
		
		subscribeToEventQueues("onSubmit");
		loadDataPasienToListbox();
		loadPolyToListbox();
		loadAdmisiToListbox();
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
		
		Window windowAdd = (Window) Executions.createComponents("/WEB-INF/zul/admisi/pilih_jadwal.zul", null, null);
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
		
		Admisi obj = new Admisi();
		obj.setPatient(patientSelected);
		obj.setPoly(polySelected);
		obj.setDoctor(scheduleSelectedFromHome.getDoctor());
		obj.setAdmisiDate(calNow.getTime());
		obj.setAdmisiStartTime(scheduleSelectedFromHome.getStartTime());
		obj.setAdmisiEndTime(scheduleSelectedFromHome.getEndTime());
		obj.setCreatedBy(loggedUser);
		obj.setCreatedDate(calNow.getTime());
		
		AdmisiDao dao = new AdmisiDao();
		dao.setSessionFactory(sessionFactory);
		if(dao.saveOrUpdate(obj)){
			loadAdmisiToListbox();
		}else{
			Messagebox.show("Save/Update Failed", "Error", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	@Listen ("onClick = #btnClear")
	public void btnClearClick(){
		patientSelected = null;
		polySelected = null;
		scheduleSelectedFromHome = null;
		
		bdxPasien.setValue("");
		lbxPoly.setSelectedIndex(-1);
		lblDokter.setValue("");
		lblJam.setValue("");
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Listen ("onCreate = #winSelectSchedule")
	public void winSelectSchedule(){
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
				
				String patternJam = "HH:mm";
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
	
	
	public void subscribeToEventQueues(final String eventQueueName){
		EventQueues.lookup(eventQueueName, EventQueues.DESKTOP, true).subscribe(new EventListener<Event>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onEvent(Event event) throws Exception {
				if(eventQueueName.equals("onSubmit")){
					if(event.getName().equals("selectSchedule")){
						Map<String, Object> map = (Map<String, Object>) event.getData();
						scheduleSelectedFromHome = (PracticSchedule) map.get("scheduleSelected");
						
						String patternJam = "HH:mm";
						String jam = CommonUtil.dateFormat(scheduleSelectedFromHome.getStartTime(), patternJam)+"-"+
								 CommonUtil.dateFormat(scheduleSelectedFromHome.getEndTime(), patternJam);
						
						lblDokter.setValue(scheduleSelectedFromHome.getDoctor().getFullName());
						lblJam.setValue(jam);
					}
				}
			}
		});
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
		lbxList.getItems().clear();
		
		AdmisiDao dao = new AdmisiDao();
		dao.setSessionFactory(sessionFactory);
		List<Admisi> list = dao.loadAll(Order.desc("admisiId"));
		
		lbxList.setModel(new ListModelList<>(list));
		ListitemRenderer<Admisi> renderer = new ListitemRenderer<Admisi>() {
			@Override
			public void render(Listitem listitem, Admisi obj, int index) throws Exception {
				String patternJam = "HH:mm";
				String jam = CommonUtil.dateFormat(obj.getAdmisiStartTime(), patternJam)+"-"+
							 CommonUtil.dateFormat(obj.getAdmisiEndTime(), patternJam);
				
				listitem.appendChild(new Listcell(obj.getPatient().getName()));
				listitem.appendChild(new Listcell(obj.getDoctor().getFullName()));
				listitem.appendChild(new Listcell(obj.getPoly().getPolyclinicCode()));
				listitem.appendChild(new Listcell(CommonUtil.dateFormat(obj.getAdmisiDate(), "dd MMM yyyy")));
				listitem.appendChild(new Listcell(jam));
			}
		};
		lbxList.setItemRenderer(renderer);
	}
}
