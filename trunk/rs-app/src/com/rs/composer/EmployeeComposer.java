package com.rs.composer;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.rs.dao.EmployeeDao;
import com.rs.model.Employee;

public class EmployeeComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;
	
	private List<Employee> listEmployee;
	private Employee selectedEmployee;
	
	@Wire
	private Listbox lbxEmployee;
	
	@Listen ("onCreate = #win")
	public void win(){
		isLooged();
		loadDataEmployee();
	}
	
	
	private void loadDataEmployee() {
		selectedEmployee = null;
		
		EmployeeDao dao = new EmployeeDao();
		dao.setSessionFactory(sessionFactory);
		Criterion cr1 = Restrictions.eq("status", "Y");
		List<Employee> list = dao.loadBy(Order.desc("idEmployee"), cr1);
		listEmployee = list;
		
		lbxEmployee.getItems().clear();
		lbxEmployee.setModel(new ListModelList<>(list));
		ListitemRenderer<Employee> renderer = new ListitemRenderer<Employee>() {
			@Override
			public void render(Listitem item, Employee obj, int index) throws Exception {
				
				String isActive = "";
				
				if (obj.getStatus().equals("Y")) {
					isActive = "Aktif";
				} else if (obj.getStatus().equals("N")) {
					isActive = "Non Aktif";
				} else {
					isActive = "Root";
				}
				
				item.appendChild(new Listcell(Integer.toString(index+1)));
				item.appendChild(new Listcell(obj.getFullName()));
				item.appendChild(new Listcell(obj.getPhoneNumber()));
				item.appendChild(new Listcell(isActive));
			}
		};
		lbxEmployee.setItemRenderer(renderer);
		
	}

}
