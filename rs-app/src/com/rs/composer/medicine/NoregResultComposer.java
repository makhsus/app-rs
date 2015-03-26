package com.rs.composer.medicine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.rs.composer.BaseComposer;
import com.rs.model.MedicalTransaction;

public class NoregResultComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;
	
	private List<MedicalTransaction> medicalTrnList = new ArrayList<>();
	private MedicalTransaction medicalTrnSelected;
	
	
	@Wire
	private Window winSearchNoreg;
	@Wire
	private Listbox lbxMedicalTrn;
	
	@SuppressWarnings("unchecked")
	@Listen ("onCreate = #winSearchNoreg")
	public void winSearchPatientOnCreate(){
		isLooged();
		
		medicalTrnList = (List<MedicalTransaction>) winSearchNoreg.getAttribute("resultSearcMt");
		
		lbxMedicalTrn.getItems().clear();
		lbxMedicalTrn.setModel(new ListModelList<>(medicalTrnList));
		ListitemRenderer<MedicalTransaction> renderer = new ListitemRenderer<MedicalTransaction>() {
			@Override
			public void render(Listitem li, MedicalTransaction obj, int index) throws Exception {
				li.appendChild(new Listcell(obj.getRegistrationNo()));
				li.appendChild(new Listcell(obj.getPatient()!=null ? obj.getPatient().getName():""));
				li.appendChild(new Listcell(obj.getPoly().getPolyclinicName()));
				li.appendChild(new Listcell(obj.getDoctor().getFullName()));
			}
		};
		lbxMedicalTrn.setItemRenderer(renderer);
		
	}
	
	@Listen ("onClick = #btnClose")
	public void btnCloseOnClick(){
		winSearchNoreg.detach();
	}
	
	@Listen ("onSelect = #lbxMedicalTrn")
	public void lbxMedicalRecordOnSelect(){
		if(medicalTrnList!=null){
			int index = lbxMedicalTrn.getSelectedIndex();
			medicalTrnSelected = medicalTrnList.get(index);
		}
	}
	
	@Listen ("onClick = #btnSubmit")
	public void btnSubmitOnClick(){
		if(medicalTrnSelected==null){
			Messagebox.show("Silahkan pilih satu nomor registrasi yang tersedia", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("medicalTrn", medicalTrnSelected);
		EventQueues.lookup("onSubmit", EventQueues.DESKTOP, true).publish(new Event("medicalTrnByNoreg", null, map));
	}

}
