package com.rs.composer.medicine;

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
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.rs.bean.MedicineTrnItemBean;
import com.rs.composer.BaseComposer;
import com.rs.dao.MedicalTransactionDao;
import com.rs.model.MedicalTransaction;;

public class MedicineTrnComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;
	
	private MedicalTransaction medicalTrnSelected;
	
	
	@Wire
	private Textbox tbxSearchNoreg;
	@Wire
	private Label lblNoreg, lblNamaPasien, lblPoly, lblDokter;
	@Wire
	private Listbox lbxItems;
	@Wire
	private Toolbarbutton tbnAddItem;
	
	
	@Listen ("onCreate = #win")
	public void winCreate(){
		isLooged();
		
		subscribeToEventQueues("onSubmit");
	}
	
	@Listen("onOK = #tbxSearchNoreg")
	public void tbxSearchNoregOnOk(){
		searchMedicalTrn();
	}
	
	@Listen ("onClick = #btnSearchNoreg")
	public void btnSearchNoregOnClick(){
		searchMedicalTrn();
	}
	
	@Listen ("onClick = #tbnClear")
	public void tbnClearOnClick(){
		medicalTrnSelected = null;
		lblNoreg.setValue("");
		lblNamaPasien.setValue("");
		tbxSearchNoreg.setText("");
		lblPoly.setValue("");
		lblDokter.setValue("");
		tbnAddItem.setDisabled(true);
		lbxItems.setDisabled(true);
	}
	
	@Listen ("onClick = #tbnAddItem")
	public void tbnAddItemOnClick(){
		if(medicalTrnSelected==null){
			Messagebox.show("Silahkan cari nomor registrasi", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		
		Window windowAdd = (Window) Executions.createComponents("/WEB-INF/zul/medicine/add_medicines.zul", null, null);
		//windowAdd.setAttribute("resultSearcMt", medicalTrnList);
		windowAdd.doModal();
		
	}
	
	
	
	
	public void subscribeToEventQueues(final String eventQueueName){
		EventQueues.lookup(eventQueueName, EventQueues.DESKTOP, true).subscribe(new EventListener<Event>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onEvent(Event event) throws Exception {
				if(eventQueueName.equals("onSubmit")){
					if(event.getName().equals("medicalTrnByNoreg")){
						Map<String, Object> map = (Map<String, Object>) event.getData();
						MedicalTransaction mt = (MedicalTransaction) map.get("medicalTrn");
						medicalTrnSelected = mt;
						
						lblNoreg.setValue(mt.getRegistrationNo());
						lblNamaPasien.setValue(mt.getPatient().getName());
						lblPoly.setValue(mt.getPoly().getPolyclinicName());
						lblDokter.setValue(mt.getDoctor().getFullName());
						tbxSearchNoreg.setText("");
						tbnAddItem.setDisabled(false);
						lbxItems.setDisabled(false);
					}else if(event.getName().equals("medicalTrnAddItem")){
						Map<String, Object> map = (Map<String, Object>) event.getData();
						MedicineTrnItemBean itemBean = (MedicineTrnItemBean) map.get("itemBean");
						System.out.println("Medicine: "+itemBean.getMedicine().getMedicineName());
						System.out.println("Quantity: "+itemBean.getQuantity());
						
						//tambahkan_ke_listbox
						
					}
				}
			}
		});
	}
	
	
	public void searchMedicalTrn(){
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
			windowAdd.setAttribute("resultSearcMt", medicalTrnList);
			windowAdd.doModal();
		}
		
	}
	

}
