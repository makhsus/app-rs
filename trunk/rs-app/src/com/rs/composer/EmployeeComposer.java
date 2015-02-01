package com.rs.composer;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import com.rs.dao.EmployeeDao;
import com.rs.dao.OccupationDao;
import com.rs.dao.UsersDao;
import com.rs.model.Employee;
import com.rs.model.Occupation;
import com.rs.model.Users;
import com.rs.util.CommonUtil;

public class EmployeeComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;
	
	private String patternDate = "DD-MM-YYYY";
	
	@Wire
	private Window win;
	@Wire
	private Listbox lbxEmployee;
	@Wire
	private Grid grdAdd;
	
	@Listen ("onCreate = #win")
	public void win(){
		isLooged();
		loadDataEmployee();
		grdAdd.setVisible(false);
	}
	
	@Listen ("onClick = #tbnList")
	public void tbnListClick(){
		lbxEmployee.setVisible(true);
		grdAdd.setVisible(false);
		win.setTitle("List Data Karyawan");
	}
	
	@Listen ("onClick = #tbnAdd")
	public void tbnAddClick(){
		lbxEmployee.setVisible(false);
		grdAdd.setVisible(true);
		win.setTitle("Tambah Karyawan Baru");
	}
	
	
	private void loadDataEmployee() {
		
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
					
					String isActive = "NON";
					
					if (obj.getStatus().equals("Y")) {
						isActive = "OK";
					}
					
					String registerDate = CommonUtil.dateFormat(obj.getRegisterDate(), patternDate);
					
					Listcell lc = new Listcell(Integer.toString(number++));
					li.appendChild(lc);
					lc = new Listcell(registerDate);
					li.appendChild(lc);
					lc = new Listcell(obj.getNik());
					li.appendChild(lc);
					lc = new Listcell(obj.getFullName());
					li.appendChild(lc);
					lc = new Listcell(obj.getPhoneNumber());
					li.appendChild(lc);
					lc = new Listcell(obj.getGender());
					li.appendChild(lc);
					lc = new Listcell(obj.getOccupationId()!=null ? obj.getOccupationId().getOccupationName():"");
					li.appendChild(lc);
					lc = new Listcell(obj.getIdentityNumber());
					li.appendChild(lc);
					lc = new Listcell(isActive);
					li.appendChild(lc);
					
				}
		
	}

}
