package com.rs.composer.medicine;

import java.util.List;

import org.hibernate.criterion.Order;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Toolbarbutton;

import com.rs.composer.BaseComposer;
import com.rs.dao.SupplierDao;
import com.rs.model.Supplier;

public class MedicineSupplierComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;
	
	@Wire
	private Listbox lbxSupplier;
	
	
	@Listen ("onCreate = #win")
	public void win(){
		isLooged();
		loadDataSupplier();
	}
	
	
	private void loadDataSupplier(){
		SupplierDao dao = new SupplierDao();
		dao.setSessionFactory(sessionFactory);
		List<Supplier> list = dao.loadAll(Order.asc("supplierName"));
		//System.out.println("list: "+list.size());
		
		lbxSupplier.getItems().clear();
		int no = 1;
		for(Supplier obj: list){
			Listitem li = new Listitem();
			lbxSupplier.appendChild(li);
			
			Listcell lc = new Listcell(Integer.toString(no));
			li.appendChild(lc);
			lc = new Listcell(obj.getSupplierName()!=null ? obj.getSupplierName():"");
			li.appendChild(lc);
			lc = new Listcell(obj.getSupplierCode()!=null ? obj.getSupplierCode():"");
			li.appendChild(lc);
			lc = new Listcell(obj.getPhoneSupplier()!=null ? obj.getPhoneSupplier():"");
			li.appendChild(lc);
			lc = new Listcell(obj.getAddress()!=null ? obj.getAddress():"");
			li.appendChild(lc);
			lc = new Listcell(obj.isActive() ? "Yes":"No");
			li.appendChild(lc);
			
			lc = new Listcell();
			Toolbarbutton tbn = new Toolbarbutton("Edit");
			lc.appendChild(tbn);
			li.appendChild(lc);
			
			no++;
		}
		
	}
	

}
