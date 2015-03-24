package com.rs.composer.medicine;

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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.rs.composer.BaseComposer;
import com.rs.dao.MedicalTransactionDao;
import com.rs.model.MedicalTransaction;;

public class MedicineTrnComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;
	
	private List<MedicalTransaction> medicalTrnList;
	private MedicalTransaction mrPatientSelected;
	private MedicalTransaction mrSearchSelected;
	
	@Wire
	private Window winSearchPatient;
	@Wire
	private Textbox tbxSearchNoreg;
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
	
	@Listen("onOK = #tbxSearchNoreg")
	public void tbxSearchNoregOnOk(){
		searchAdmisi();
	}
	
	@Listen ("onClick = #btnSearchNoreg")
	public void btnSearchNoregOnClick(){
		searchAdmisi();
	}
	
	@Listen ("onClick = #tbnClear")
	public void tbnClearOnClick(){
		mrPatientSelected = null;
		lblNoreg.setValue("");
		lblNamaPasien.setValue("");
		tbxSearchNoreg.setText("");
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
		/*List<MedicalRecords> mrList = (List<MedicalRecords>) winSearchPatient.getAttribute("resultSearcMr");
		this.mrSearchList = mrList;
		
		lbxMedicalRecord.getItems().clear();
		lbxMedicalRecord.setModel(new ListModelList<>(mrList));*/
		/*ListitemRenderer<MedicalRecords> renderer = new ListitemRenderer<MedicalRecords>() {
			@Override
			public void render(Listitem li, MedicalRecords obj, int index) throws Exception {
				li.appendChild(new Listcell(obj.getRegistrationNo()));
				li.appendChild(new Listcell(obj.getPatient()!=null ? obj.getPatient().getName():""));
				li.appendChild(new Listcell(obj.getPoly().getPolyclinicName()));
				li.appendChild(new Listcell(obj.getDoctor().getFullName()));
			}
		};
		lbxMedicalRecord.setItemRenderer(renderer);*/
		
	}
	
	@Listen ("onClick = #btnClose")
	public void btnCloseOnClick(){
		winSearchPatient.detach();
	}
	
	@Listen ("onSelect = #lbxMedicalRecord")
	public void lbxMedicalRecordOnSelect(){
		/*if(mrSearchList!=null){
			int index = lbxMedicalRecord.getSelectedIndex();
			mrSearchSelected = mrSearchList.get(index);
		}*/
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
						//MedicalRecords mr = (MedicalRecords) map.get("medicalRecord");
						//mrPatientSelected = mr;
						
						//lblNoreg.setValue(mr.getRegistrationNo());
						//lblNamaPasien.setValue(mr.getPatient().getName());
						//tbxSearchNoreg.setText("");
						//tbnAddItem.setDisabled(false);
						//lbxItems.setDisabled(false);
					}
				}
			}
		});
	}
	
	
	public void searchAdmisi(){
		String key = tbxSearchNoreg.getText().trim();
		
		if(key.equalsIgnoreCase("")){
			return;
		}
		
		MedicalTransactionDao dao = new MedicalTransactionDao();
		dao.setSessionFactory(sessionFactory);
		
		Criterion cr1 = Restrictions.like("registrationNo", "%"+key+"%");
		List<MedicalTransaction> medicalTrnList = dao.loadBy(Order.asc("registrationNo"), cr1);
		
		if(medicalTrnList.size()<1){
			Messagebox.show("Nomor Registrasi tidak ditemukan", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			tbnAddItem.setDisabled(true);
			lbxItems.setDisabled(true);
			return;
		}else{
			Window windowAdd = (Window) Executions.createComponents("/WEB-INF/zul/medicine/noreg_result_list.zul", null, null);
			windowAdd.setAttribute("resultSearcMr", medicalTrnList);
			windowAdd.doModal();
		}
		
	}
	

}
