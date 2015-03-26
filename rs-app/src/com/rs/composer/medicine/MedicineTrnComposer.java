package com.rs.composer.medicine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.rs.bean.MedicineTrnItemBean;
import com.rs.composer.BaseComposer;
import com.rs.dao.MedicalTransactionDao;
import com.rs.model.MedicalTransaction;
import com.rs.model.Medicine;
import com.rs.model.MedicineTransaction;
import com.rs.model.Users;
import com.rs.util.CommonUtil;

public class MedicineTrnComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;
	
	private Users userLogged;
	private MedicalTransaction medicalTrnSelected;
	private MedicineTransaction medicineTrnSelected;
	private List<MedicineTransaction> medicineTrnList = new ArrayList<>();
	
	
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
		userLogged = (Users) sessionZk.getAttribute(CommonUtil.LOGIN_USER);
		
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
		windowAdd.setAttribute("medicalTrnSelected", medicalTrnSelected);
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
						MedicalTransaction mt = (MedicalTransaction) map.get("medicalTrn");
						MedicineTrnItemBean itemBean = (MedicineTrnItemBean) map.get("itemBean");
						
						medicalTrnSelected = mt;
						Medicine medicine = itemBean.getMedicine();
						Integer qty = itemBean.getQuantity();
						
						boolean isMedicineExist = false;
						boolean isQtySame = false;
						for(MedicineTransaction mTrn: medicineTrnList){
							if(medicine.getMedicineCode().equalsIgnoreCase(mTrn.getMedicineCode())){
								isMedicineExist = true;
								if(qty==mTrn.getQuantity()){
									isQtySame = true;
								}
							}
						}
						
						if(isMedicineExist){
							if(isQtySame){
								return;
							}else{
								updateQuantityItem(medicine, qty);
							}
						}else{
							addItem(mt, medicine, qty);
						}

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
	
	private void medicineItemsListbox(){
		lbxItems.getItems().clear();
		lbxItems.setModel(new ListModelList<>(medicineTrnList));
		ListitemRenderer<MedicineTransaction> renderer = new ListitemRenderer<MedicineTransaction>() {
			@Override
			public void render(Listitem li, MedicineTransaction obj, int index) throws Exception {
				Listcell lc = new Listcell();
				Toolbarbutton tbnEditQty = new Toolbarbutton("Edit Qty");
				lc.appendChild(tbnEditQty);
				Label lbl = new Label("|");
				lc.appendChild(lbl);
				Toolbarbutton tbnDelete = new Toolbarbutton("Hapus");
				lc.appendChild(tbnDelete);
				
				li.appendChild(new Listcell(Integer.toString(index+1)));
				li.appendChild(new Listcell(obj.getMedicineName()));
				li.appendChild(new Listcell(obj.getMedicineCode()));
				li.appendChild(new Listcell(CommonUtil.formatStringIDR(obj.getMedicinePrice().intValue())));
				li.appendChild(new Listcell(obj.getMedicineUnit()));
				li.appendChild(new Listcell(Integer.toString(obj.getQuantity())));
				li.appendChild(new Listcell(CommonUtil.formatStringIDR(obj.getTotalPrice().intValue())));
				li.appendChild(lc);
			}
		};
		lbxItems.setItemRenderer(renderer);
	}
	
	private void addItem(MedicalTransaction medicalTransaction, Medicine medicine, Integer quantity){
		Integer totalPrice = medicine.getPriceSell().intValue() * quantity;
		
		MedicineTransaction obj = new MedicineTransaction();
		obj.setMedicalTrn(medicalTransaction);
		obj.setMedicine(medicine);
		obj.setMedicineName(medicine.getMedicineName());
		obj.setMedicineCode(medicine.getMedicineCode());
		obj.setMedicinePrice(medicine.getPriceSell());
		obj.setQuantity(quantity);
		obj.setTotalPrice(new BigDecimal(totalPrice));
		obj.setPatient(medicalTransaction.getPatient());
		obj.setCreatedBy(userLogged);
		obj.setCreatedDate(Calendar.getInstance().getTime());
		medicineTrnList.add(obj);
		
		medicineItemsListbox();
	}
	
	private void updateQuantityItem(Medicine medicine, Integer quantity){
		int index = -1;
		for(int i=0;i<medicineTrnList.size();i++){
			MedicineTransaction mTrn = medicineTrnList.get(i);
			if(medicine.getMedicineCode().equalsIgnoreCase(mTrn.getMedicineCode())){
				index = i;
			}
		}
		
		if(index > -1){
			Integer totalPrice = medicine.getPriceSell().intValue() * quantity;
			
			MedicineTransaction mTrn = medicineTrnList.get(index);
			mTrn.setQuantity(quantity);
			mTrn.setTotalPrice(new BigDecimal(totalPrice));
			
			medicineTrnList.remove(index);
			medicineTrnList.add(index, mTrn);
			
			medicineItemsListbox();
		}
	}
}
