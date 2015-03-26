package com.rs.composer.medicine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.rs.bean.MedicineTrnItemBean;
import com.rs.composer.BaseComposer;
import com.rs.dao.MedicineDao;
import com.rs.model.Medicine;
import com.rs.util.CommonUtil;

public class MedicineAddItemComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;

	
	private List<Medicine> medicines;
	private Medicine medicineSelected;
	
	
	@Wire
	private Button btnSearchMedicine, btnSubmit;
	@Wire
	private Textbox tbxNameCodeMedic;
	@Wire
	private Listbox lbxMedicines;
	@Wire
	private Intbox ibxQty;
	@Wire
	private Label lblNameCodeMedic;
	
	@Listen ("onCreate = #winAddMedicines")
	public void winAddMedicinesOnCreate(){
		isLooged();
	}
	
	@Listen("onOK = #tbxNameCodeMedic")
	public void tbxNameCodeMedicOnOk(){
		btnSearchMedicine.focus();
		searchMedicines();
	}
	
	@Listen ("onClick = #btnSearchMedicine")
	public void btnSearchMedicineOnClick(){
		searchMedicines();
	}
	
	@Listen ("onSelect = #lbxMedicines")
	public void lbxMedicinesOnClick(){
		if(medicines!=null){
			int index = lbxMedicines.getSelectedIndex();
			medicineSelected = medicines.get(index);
			
			lblNameCodeMedic.setValue(medicineSelected.getMedicineName()+" ("+medicineSelected.getMedicineCode()+")");
			ibxQty.setDisabled(false);
			btnSubmit.setDisabled(false);
		}
	}
	
	@Listen ("onClick = #btnSubmit")
	public void btnSubmitOnClick(){
		if(medicineSelected==null){
			Messagebox.show("Silahkan pilih Item/Obat");
			return;
		}
		
		if(ibxQty.getValue()==null){
			ibxQty.focus();
			Messagebox.show("Kuantitas harus lebih dari 0");
			return;
		}
		
		if(ibxQty.getValue()<1){
			ibxQty.focus();
			Messagebox.show("Kuantitas harus lebih dari 0");
			return;
		}
		
		Integer quantity = ibxQty.getValue();
		
		MedicineTrnItemBean itemBean = new MedicineTrnItemBean();
		itemBean.setMedicine(medicineSelected);
		itemBean.setQuantity(quantity);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("itemBean", itemBean);
		EventQueues.lookup("onSubmit", EventQueues.DESKTOP, true).publish(new Event("medicalTrnAddItem", null, map));
		
	}
	
	private void searchMedicines(){
		lblNameCodeMedic.setValue("");
		medicineSelected = null;
		ibxQty.setDisabled(true);
		btnSubmit.setDisabled(true);
		
		String key = tbxNameCodeMedic.getValue().trim();
		
		if(key.isEmpty()){
			return;
		}
		
		MedicineDao dao = new MedicineDao();
		dao.setSessionFactory(sessionFactory);
		
		Criterion cr1 = Restrictions.like("medicineName", "%"+key+"%");
		Criterion cr2 = Restrictions.like("medicineCode", "%"+key+"%");
		LogicalExpression logicalExpres = Restrictions.or(cr1, cr2);
		List<Medicine> list = dao.loadBy(Order.asc("medicineName"), logicalExpres);
		medicines = list;
		
		lbxMedicines.getItems().clear();
		lbxMedicines.setModel(new ListModelList<>(list));
		ListitemRenderer<Medicine> renderer = new ListitemRenderer<Medicine>() {
			@Override
			public void render(Listitem li, Medicine obj, int index) throws Exception {
				li.appendChild(new Listcell(obj.getMedicineName()));
				li.appendChild(new Listcell(obj.getMedicineCode()));
				li.appendChild(new Listcell(obj.getAvailableStock().toString()));
				li.appendChild(new Listcell(CommonUtil.formatStringIDR(obj.getPriceSell().intValue())));
				li.appendChild(new Listcell(obj.getMedicineUnit()));
			}
		};
		lbxMedicines.setItemRenderer(renderer);
		
	}
	
}
