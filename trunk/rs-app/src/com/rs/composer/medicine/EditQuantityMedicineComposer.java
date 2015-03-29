package com.rs.composer.medicine;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import com.rs.bean.MedicineTrnItemBean;
import com.rs.composer.BaseComposer;
import com.rs.model.MedicalTransaction;
import com.rs.model.MedicineTransaction;

public class EditQuantityMedicineComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;
	
	private MedicalTransaction  medicalTrx;
	private MedicineTransaction medicineTrx;
	
	@Wire
	private Window winEditQty;
	@Wire
	private Label lblNameMedic, lblCodeMedic;
	@Wire
	private Intbox ibxQty;
	

	@Listen ("onCreate = #winEditQty")
	public void winEditQtyOnCreate(){
		isLooged();
		
		MedicalTransaction medicalTrx = (MedicalTransaction) winEditQty.getAttribute("medicalTrn");
		this.medicalTrx = medicalTrx;
		
		MedicineTransaction mt = (MedicineTransaction) winEditQty.getAttribute("medicineTrn");
		if(mt!=null){
			medicineTrx = mt;
			lblNameMedic.setValue(mt.getMedicineName());
			lblCodeMedic.setValue(mt.getMedicineCode());
			ibxQty.setValue(mt.getQuantity());
		}
	}
	
	@Listen ("onClick = #btnSubmit")
	public void btnSubmitOnClick(){
		if(medicineTrx!=null && ibxQty!=null){
			if(ibxQty.getValue()>0){
				Integer qtyUpdate = ibxQty.getValue();
				//medicineTrx.setQuantity(ibxQty.getValue());
				
				MedicineTrnItemBean itemBean = new MedicineTrnItemBean();
				itemBean.setMedicine(medicineTrx.getMedicine());
				itemBean.setQuantity(qtyUpdate);
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("itemBean", itemBean);
				map.put("medicalTrn", medicalTrx);
				EventQueues.lookup("onSubmit", EventQueues.DESKTOP, true).publish(new Event("medicalTrnUpdateQty", null, map));
			}
		}
	}
	
}
