package com.rs.composer;

import java.util.List;

import org.hibernate.criterion.Order;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.rs.dao.MahasiswaDao;
import com.rs.model.Mahasiswa;

/**
 * @author Makhsus Bilmajdi
 *
 */
public class MahasiswaComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;

	@Wire
	private Listbox lbxRumah;
	@Wire
	private Grid grdAddEdit;
	@Wire
	private Textbox tbxNim, tbxName;
	
	private Mahasiswa selectedMahasiswa;
	
	
	@Listen ("onCreate = #win")
	public void win(){
		isLooged();
		loadDataMahasiswa();
	}
	
	@Listen ("onClick = #tbnAdd")
	public void tbnAddClick(){
		lbxRumah.setVisible(false);
		grdAddEdit.setVisible(true);
		
		lbxRumah.getItems().clear();
		selectedMahasiswa = null;
	}
	
	@Listen ("onClick = #tbnList")
	public void tbnListClick(){
		lbxRumah.setVisible(true);
		grdAddEdit.setVisible(false);
	}
	
	@Listen ("onClick = #btnSubmit")
	public void btnSubmitClick(){
		String nim = tbxNim.getText().trim();
		String name = tbxName.getText().trim();
		
		String msg = "Save";
		Mahasiswa m = new Mahasiswa();
		if(selectedMahasiswa!=null){
			msg = "Update";
			m = selectedMahasiswa;
		}
		m.setNim(nim);
		m.setFullname(name);
		
		MahasiswaDao dao = new MahasiswaDao();
		dao.setSessionFactory(sessionFactory);
		if(dao.saveOrUpdate(m)) {
			Messagebox.show("Success "+msg);
			lbxRumah.setVisible(true);
			grdAddEdit.setVisible(false);
			loadDataMahasiswa();
		}else{
			Messagebox.show("Failed "+msg);
		}
	}
	
	private void loadDataMahasiswa(){
		MahasiswaDao dao = new MahasiswaDao();
		dao.setSessionFactory(sessionFactory);
		//List<Mahasiswa> list = dao.listAll();
		List<Mahasiswa> list = dao.loadAll(Order.desc("id"));
		System.out.println("list: "+list);
		
		lbxRumah.getItems().clear();
		for(Mahasiswa obj: list){
			Listitem li = new Listitem();
			lbxRumah.appendChild(li);
			
			Listcell lc = new Listcell(obj.getNim());
			li.appendChild(lc);
			lc = new Listcell(obj.getFullname());
			li.appendChild(lc);
		}
	}
}
