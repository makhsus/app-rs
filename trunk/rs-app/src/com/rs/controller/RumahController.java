package com.rs.controller;

import java.util.List;

import org.hibernate.criterion.Order;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;

import com.rs.dao.RumahDao;
import com.rs.model.Rumah;

public class RumahController extends BaseController {
	private static final long serialVersionUID = 1L;
	
	private List<Rumah> rumahList;
	private Rumah selectedRumah;
	
	
	@Wire
	private Listbox lbxRumah;
	@Wire
	private Textbox tbxOwner, tbxAdress;
	@Wire
	private Grid grdAddEdit;
	
	@Listen("onCreate = #winHome ")
	public void winHomeOnCreate(){
		setSessionFactory();
	}
	
	@SuppressWarnings("rawtypes")
	@Listen("onClick = #mntmLoad")
	public void mntmLoad(){
		lbxRumah.setVisible(true);
		grdAddEdit.setVisible(false);
		
		RumahDao dao = new RumahDao(sessionFactory);
		List<Rumah> list = dao.loadAll(Order.desc("id"));
		if(list!=null){
			if(list.size()>0){
				rumahList = list;
			}
		}
		
		lbxRumah.setModel(new ListModelList<>(list));
		ListitemRenderer renderer = new ListitemRenderer() {
			@Override
			public void render(Listitem item, Object obj, int i) throws Exception {
				Rumah value = (Rumah) obj;
				item.appendChild(new Listcell(value.getOwnerName()));
				item.appendChild(new Listcell(value.getAddress()));
			}			
		};
		lbxRumah.setItemRenderer(renderer);
	}
	
	@Listen("onClick = #mntmAdd")
	public void mntmAdd(){
		lbxRumah.setVisible(false);
		grdAddEdit.setVisible(true);
	}
	
	@Listen("onClick = #mntmEdit")
	public void mntmEdit(){
		lbxRumah.setVisible(false);
		grdAddEdit.setVisible(false);
		
		if(selectedRumah!=null){
			grdAddEdit.setVisible(true);
			System.out.println("ID: "+selectedRumah.getId());
			System.out.println("NAME: "+selectedRumah.getOwnerName());
			tbxOwner.setValue(selectedRumah.getOwnerName());
			tbxAdress.setValue(selectedRumah.getAddress());
		}
	}
	
	@Listen("onClick = #mntmSave")
	public void mntmSave(){
		System.out.println("sessionFactory: "+sessionFactory);
		
		Rumah obj = new Rumah();
		obj.setOwnerName("Makhsus Bilmajdi");
		obj.setAddress("Jl. Ciherang, Perum HBTB Blok C3 No. 47, Kel. Sukatani, Kec. Cimanggis, Depok");
		
		RumahDao dao = new RumahDao(sessionFactory);
		dao.saveOrUpdate(obj);
	}
	
	@Listen("onSelect = #lbxRumah")
	public void lbxRumahSelect(){
		System.out.println("select");
		int index = lbxRumah.getSelectedIndex();
		Rumah rumah = rumahList.get(index);
		selectedRumah = rumah;
	}
	
	@Listen("onClick = #btnSubmit")
	public void gridAddEditBtnSubmit(){
		Rumah rumah = selectedRumah;
		
		if(rumah==null){
			rumah = new Rumah();
		}
		
		rumah.setOwnerName(tbxOwner.getValue());
		rumah.setAddress(tbxAdress.getValue());
		
		System.out.println("sessionFactory: "+sessionFactory);
		RumahDao dao = new RumahDao(sessionFactory);
		dao.saveOrUpdate(rumah);
		
		lbxRumah.setVisible(true);
		grdAddEdit.setVisible(false);
	}
	
	@Listen("onClick = #btnCancel")
	public void gridAddEditBtnCancel(){
		lbxRumah.setVisible(true);
		grdAddEdit.setVisible(false);
	}

}
