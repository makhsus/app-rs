package com.rs.composer;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

import com.rs.dao.PolyclinicDao;
import com.rs.model.Polyclinic;

public class PolyclinicComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;

	private List<Polyclinic> listPoly;
	private Polyclinic selectedPoly;
	
	
	@Wire
	private Listbox lbxPoly;
	@Wire
	private Grid grdAddEdit;
	@Wire
	private Textbox tbxCode, tbxName;
	@Wire
	private Radiogroup rgIsActive;
	
	
	@Listen ("onCreate = #win")
	public void win(){
		isLooged();
		loadDataPoly();
	}
	
	@Listen ("onClick = #tbnList")
	public void tbnListClick(){
		lbxPoly.setVisible(true);
		grdAddEdit.setVisible(false);
		loadDataPoly();
	}
	
	@Listen ("onClick = #tbnAdd")
	public void tbnAddClick(){
		lbxPoly.setVisible(false);
		lbxPoly.getItems().clear();
		grdAddEdit.setVisible(true);
		tbxCode.setText("");
		tbxName.setText("");
		selectedPoly = null;
	}
	
	@Listen ("onClick = #tbnEdit")
	public void tbnEditClick(){
		if(selectedPoly==null){
			Messagebox.show("Please, select at least one item");
		}else{
			lbxPoly.setVisible(false);
			grdAddEdit.setVisible(true);
			
			Polyclinic obj = selectedPoly;
			
			int indexRg = 0;
			if(!obj.isActive()){
				indexRg = 1;
			}
			
			tbxCode.setText(obj.getPolyclinicCode());
			tbxName.setText(obj.getPolyclinicName());
			rgIsActive.setSelectedIndex(indexRg);
		}
		
	}
	
	@Listen ("onSelect = #lbxPoly")
	public void lbxPolySelect(){
		if(listPoly!=null){
			int index = lbxPoly.getSelectedIndex();
			selectedPoly = listPoly.get(index);
		}
	}
	
	
	@Listen ("onClick = #btnSubmit")
	public void btnSubmitClick(){
		String code = tbxCode.getText().trim();
		String name = tbxName.getText().trim();
		boolean isActive = false;
		if(rgIsActive.getSelectedIndex()==0){
			isActive = true;
		}
		
		String msg = "Save";
		Polyclinic obj = null;
		if(selectedPoly!=null){
			msg = "Update";
			obj = selectedPoly;
		}else{
			obj = new Polyclinic();
		}
		obj.setPolyclinicCode(code);
		obj.setPolyclinicName(name);
		obj.setActive(isActive);
		
		
		PolyclinicDao dao = new PolyclinicDao();
		dao.setSessionFactory(sessionFactory);
		if(dao.saveOrUpdate(obj)) {
			Messagebox.show("Success "+msg);
			lbxPoly.setVisible(true);
			grdAddEdit.setVisible(false);
			loadDataPoly();
		}else{
			Messagebox.show("Failed "+msg);
		}
	}
	
	private void loadDataPoly(){
		selectedPoly = null;
		
		PolyclinicDao dao = new PolyclinicDao();
		dao.setSessionFactory(sessionFactory);
		Criterion cr1 = Restrictions.eq("isActive", true);
		List<Polyclinic> list = dao.loadBy(Order.desc("polyclinicId"), cr1);
		listPoly = list;
		//System.out.println("listPoly: "+list.size());
		
		lbxPoly.getItems().clear();
		lbxPoly.setModel(new ListModelList<>(list));
		ListitemRenderer<Polyclinic> renderer = new ListitemRenderer<Polyclinic>() {
			@Override
			public void render(Listitem item, Polyclinic obj, int index) throws Exception {
				String isActiveStr = "Yes";
				if(!obj.isActive()){
					isActiveStr = "No";
				}
				
				item.appendChild(new Listcell(Integer.toString(index+1)));
				item.appendChild(new Listcell(obj.getPolyclinicName()));
				item.appendChild(new Listcell(isActiveStr));
			}
		};
		lbxPoly.setItemRenderer(renderer);
		
		//int no = 1;
		/*for(Polyclinic obj: list){
			Listitem li = new Listitem();
			lbxPoly.appendChild(li);
			
			String isActiveStr = "Yes";
			if(!obj.isActive()){
				isActiveStr = "No";
			}
			
			Listcell lc = new Listcell(Integer.toString(no));
			li.appendChild(lc);
			lc = new Listcell(obj.getPolyclinicName());
			li.appendChild(lc);
			lc = new Listcell(isActiveStr);
			li.appendChild(lc);
			
			no++;
		}*/
	}
}
