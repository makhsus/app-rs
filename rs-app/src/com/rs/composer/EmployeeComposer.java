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
import com.rs.dao.OccupationDao;
import com.rs.dao.UsersDao;
import com.rs.model.Employee;
import com.rs.model.Occupation;
import com.rs.model.Users;

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
		
		OccupationDao occupationDao = new OccupationDao();
		Occupation occupation = new Occupation();
		EmployeeDao dao = new EmployeeDao();
		UsersDao userDao = new UsersDao();
		Users users = new Users();
		dao.setSessionFactory(sessionFactory);
		Criterion cr1 = Restrictions.eq("status", "Y");
		List<Employee> listEmployee = dao.loadBy(Order.desc("idEmployee"), cr1);
				
				lbxEmployee.getItems().clear();
				int number = 1;
				
				for(Employee obj: listEmployee){
					Listitem li = new Listitem();
					lbxEmployee.appendChild(li);
					
					userDao.setSessionFactory(sessionFactory);
					Criterion cr2 = Restrictions.eq("idEmployee", obj);
					List<Users> listUser = userDao.loadBy(Order.desc("idUser"), cr2);
					
					String isActive = "AKTIF";
					String statusUser = "-";
					
					if (listUser.size() > 0) {
						users = listUser.get(0);
						statusUser = "AKTIF";
						System.out.println("users = "+users.getIdEmployee().getIdEmployee());
					}
					
					
					
					Listcell lc = new Listcell(Integer.toString(number++));
					li.appendChild(lc);
					lc = new Listcell(obj.getNik());
					li.appendChild(lc);
					lc = new Listcell(obj.getFullName());
					li.appendChild(lc);
					lc = new Listcell(obj.getPhoneNumber());
					li.appendChild(lc);
					lc = new Listcell(obj.getGender());
					li.appendChild(lc);
					lc = new Listcell(obj.getOccupationId().getOccupationName());
					li.appendChild(lc);
					lc = new Listcell(isActive);
					li.appendChild(lc);
					lc = new Listcell(statusUser);
					li.appendChild(lc);
					
				}
		
	}

}
