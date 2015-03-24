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
import com.rs.dao.MedicineDao;
import com.rs.model.Medicine;
import com.rs.util.CommonUtil;

public class MedicineComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;

	@Wire
	private Listbox lbxObat;
	
	
	@Listen ("onCreate = #win")
	public void win(){
		isLooged();
		loadDataObat();
	}
	
	private void loadDataObat(){
		MedicineDao dao = new MedicineDao();
		dao.setSessionFactory(sessionFactory);
		List<Medicine> list = dao.loadAll(Order.asc("medicineName"));
		
		lbxObat.getItems().clear();
		int no = 1;
		for(Medicine obj: list){
			Listitem li = new Listitem();
			lbxObat.appendChild(li);
			
			Listcell lc = new Listcell(Integer.toString(no));
			li.appendChild(lc);
			lc = new Listcell(obj.getMedicineName()!=null ? obj.getMedicineName().toUpperCase():"");
			li.appendChild(lc);
			lc = new Listcell(obj.getMedicineCode()!=null ? obj.getMedicineCode():"");
			li.appendChild(lc);
			lc = new Listcell(obj.getSupplier()!=null ? obj.getSupplier().getSupplierName():"");
			li.appendChild(lc);
			lc = new Listcell(obj.getMedicineUnit()!=null ? obj.getMedicineUnit():"");
			li.appendChild(lc);
			lc = new Listcell(obj.getPriceBuy()!=null ? CommonUtil.formatStringIDR(obj.getPriceBuy().intValue()):"");
			li.appendChild(lc);
			lc = new Listcell(obj.getPriceSell()!=null ? CommonUtil.formatStringIDR(obj.getPriceSell().intValue()):"");
			li.appendChild(lc);
			
			
			lc = new Listcell();
			Toolbarbutton tbn = new Toolbarbutton("Edit");
			lc.appendChild(tbn);
			li.appendChild(lc);
			
			no++;
		}
		
	}
}
