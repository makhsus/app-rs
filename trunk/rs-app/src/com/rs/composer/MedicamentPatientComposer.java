package com.rs.composer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.rs.dao.MedicalRecordsDao;
import com.rs.model.MedicalRecords;

public class MedicamentPatientComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;
	
	private List<MedicalRecords> mrSearchList;
	private MedicalRecords mrPatientSelected;
	private MedicalRecords mrSearchSelected;
	
	@Wire
	private Window winSearchPatient;
	@Wire
	private Textbox tbxSearchAdmisi;
	@Wire
	private Label lblNoreg, lblNamaPasien;
	@Wire
	private Listbox lbxMedicalRecord, lbxItems;
	@Wire
	private Toolbarbutton tbnAddItem;
	
	
	@Listen ("onCreate = #win")
	public void winCreate(){
		isLooged();
		
		subscribeToEventQueues("onSubmit");
	}
	
	@Listen("onOK = #tbxSearchAdmisi")
	public void tbxSearchAdmisiOnOk(){
		searchAdmisi();
	}
	
	@Listen ("onClick = #tbnClear")
	public void tbnClearOnClick(){
		mrPatientSelected = null;
		lblNoreg.setValue("");
		lblNamaPasien.setValue("");
		tbxSearchAdmisi.setText("");
		tbnAddItem.setDisabled(true);
		lbxItems.setDisabled(true);
	}
	
	@Listen ("onClick = #tbnAddItem")
	public void tbnAddItemOnClick(){
		if(mrPatientSelected==null){
			Messagebox.show("Silahkan cari nomor registrasi", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		
		
		
	}
	
	
	
	
	
	@SuppressWarnings("unchecked")
	@Listen ("onCreate = #winSearchPatient")
	public void winSearchPatientOnCreate(){
		List<MedicalRecords> mrList = (List<MedicalRecords>) winSearchPatient.getAttribute("resultSearcMr");
		this.mrSearchList = mrList;
		
		lbxMedicalRecord.getItems().clear();
		lbxMedicalRecord.setModel(new ListModelList<>(mrList));
		ListitemRenderer<MedicalRecords> renderer = new ListitemRenderer<MedicalRecords>() {
			@Override
			public void render(Listitem li, MedicalRecords obj, int index) throws Exception {
				li.appendChild(new Listcell(obj.getRegistrationNo()));
				li.appendChild(new Listcell(obj.getPatient()!=null ? obj.getPatient().getName():""));
				li.appendChild(new Listcell(obj.getPoly().getPolyclinicName()));
				li.appendChild(new Listcell(obj.getDoctor().getFullName()));
			}
		};
		lbxMedicalRecord.setItemRenderer(renderer);
		
	}
	
	@Listen ("onClick = #btnClose")
	public void btnCloseOnClick(){
		winSearchPatient.detach();
	}
	
	@Listen ("onSelect = #lbxMedicalRecord")
	public void lbxMedicalRecordOnSelect(){
		if(mrSearchList!=null){
			int index = lbxMedicalRecord.getSelectedIndex();
			mrSearchSelected = mrSearchList.get(index);
		}
	}
	
	@Listen ("onClick = #btnSubmit")
	public void btnSubmitOnClick(){
		if(mrSearchSelected==null){
			Messagebox.show("Silahkan pilih satu nomor registrasi yang tersedia", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("medicalRecord", mrSearchSelected);
		EventQueues.lookup("onSubmit", EventQueues.DESKTOP, true).publish(new Event("mrForMedicamentPatient", null, map));
		
	}
	
	
	public void subscribeToEventQueues(final String eventQueueName){
		EventQueues.lookup(eventQueueName, EventQueues.DESKTOP, true).subscribe(new EventListener<Event>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onEvent(Event event) throws Exception {
				if(eventQueueName.equals("onSubmit")){
					if(event.getName().equals("mrForMedicamentPatient")){
						Map<String, Object> map = (Map<String, Object>) event.getData();
						MedicalRecords mr = (MedicalRecords) map.get("medicalRecord");
						mrPatientSelected = mr;
						
						lblNoreg.setValue(mr.getRegistrationNo());
						lblNamaPasien.setValue(mr.getPatient().getName());
						tbxSearchAdmisi.setText("");
						tbnAddItem.setDisabled(false);
						lbxItems.setDisabled(false);
					}
				}
			}
		});
	}
	
	
	public void searchAdmisi(){
		String key = tbxSearchAdmisi.getText().trim();
		
		if(key.equalsIgnoreCase("")){
			return;
		}
		
		MedicalRecordsDao dao = new MedicalRecordsDao();
		dao.setSessionFactory(sessionFactory);
		
		Criterion cr1 = Restrictions.like("registrationNo", "%"+key+"%");
		List<MedicalRecords> mrList = dao.loadBy(Order.asc("registrationNo"), cr1);
		
		if(mrList.size()<1){
			Messagebox.show("Nomor Registrasi tidak ditemukan", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			tbnAddItem.setDisabled(true);
			lbxItems.setDisabled(true);
			return;
		}else{
			Window windowAdd = (Window) Executions.createComponents("/WEB-INF/zul/medicament/result_search_patient.zul", null, null);
			windowAdd.setAttribute("resultSearcMr", mrList);
			windowAdd.doModal();
		}
		
	}
	

}
