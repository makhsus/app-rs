package com.rs.composer;

import java.util.List;

import org.apache.catalina.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.rs.dao.EmployeeDao;
import com.rs.dao.UsersDao;
import com.rs.model.Employee;
import com.rs.model.Users;
import com.rs.util.CommonUtil;
@SuppressWarnings("unused")
public class UsersComposer extends BaseComposer {
	
	private static final long serialVersionUID = 1L;
	private static final Log logger = LogFactory.getLog(UsersComposer.class);
	
	private String patternDate = "DD-MM-YYYY";
	private Users userLogin;
	
	@Wire
	private Window winUsers;
	@Wire
	private Listbox lbxUsers, lbxUsersSelected;
	@Wire
	private Label lblNik;
	@Wire
	private Grid grdSearchNik;
	@Wire
	private Textbox txbSearchNik;
	@Wire
	private Button btnCreateUser;
	
	@Listen ("onCreate = #winUsers")
	public void win(){
		isLooged();
		loadDataUsers();
		lbxUsers.setVisible(true);
		grdSearchNik.setVisible(false);
		lbxUsersSelected.setVisible(false);
		userLogin = (Users)sessionZk.getAttribute(CommonUtil.LOGIN_USER);
	}
	
	@Listen ("onClick = #tbnList")
	public void tbnListClick(){
		isLooged();
		loadDataUsers();
		lbxUsers.setVisible(true);
		grdSearchNik.setVisible(false);
		lbxUsersSelected.setVisible(false);
		userLogin = (Users)sessionZk.getAttribute(CommonUtil.LOGIN_USER);
	}
	
	@Listen ("onClick = #tbnAdd")
	public void tbnAddClick(){
		lbxUsers.setVisible(false);
		grdSearchNik.setVisible(true);
		lbxUsersSelected.setVisible(true);
	}
	
	@Listen ("onClick = #btnSearchNik")
	public void btnSearchNikClick(){
		isLooged();
		searchNikEmployee();
		userLogin = (Users)sessionZk.getAttribute(CommonUtil.LOGIN_USER);
	}
	
	public void loadDataUsers() {
		UsersDao dao = new UsersDao();
		dao.setSessionFactory(sessionFactory);
		Criterion cr1 = Restrictions.eq("status", "Y");
		List<Users> listUsers = dao.loadBy(Order.asc("idUser"), cr1);
		
		lbxUsers.getItems().clear();
		int number = 1;
		
		for(Users obj: listUsers){
			Listitem li = new Listitem();
			lbxUsers.appendChild(li);
			
			String createDate = CommonUtil.dateFormat(obj.getCreateDate(), patternDate);
			String updateDate = "";
			
			if (obj.getLastUpdate() == null) {
				updateDate = "-";
			} else {
				updateDate = CommonUtil.dateFormat(obj.getLastUpdate(), patternDate);
			}
			
			Listcell lc = new Listcell(Integer.toString(number++));
			li.appendChild(lc);
			lc = new Listcell(createDate);
			li.appendChild(lc);
			lc = new Listcell(updateDate);
			li.appendChild(lc);
			lc = new Listcell(obj.getIdEmployee().getNik());
			li.appendChild(lc);
			lc = new Listcell(obj.getUserName());
			li.appendChild(lc);
			lc = new Listcell(obj.getIdRole().getDescription());
			li.appendChild(lc);
		}
	}
	
	public void searchNikEmployee() {
		EmployeeDao dao = new EmployeeDao();
		UsersDao userDao = new UsersDao();
		Employee employee = new Employee();
		dao.setSessionFactory(sessionFactory);
		userDao.setSessionFactory(sessionFactory);
		String nik = txbSearchNik.getText();
		Criterion cr1 = Restrictions.eq("nik", nik);
		List<Employee> listEmployee = dao.loadBy(Order.asc("idEmployee"), cr1);
		
		lbxUsersSelected.getItems().clear();
		
		if (listEmployee.size() > 0) {
			employee = listEmployee.get(0);
			
			Criterion cr2 = Restrictions.eq("idEmployee", employee);
			List<Users> listUser = userDao.loadBy(Order.asc("idUser"), cr2);			
			
			if (listUser.size() == 0) {
				
			Listitem li = new Listitem();
			Button button = new Button();
			button.setId("btnCreateUser");
			button.setLabel("Jadikan User Baru");
			lbxUsersSelected.appendChild(li);
			
			
			Listcell lc = new Listcell(employee.getNik());
			li.appendChild(lc);
			lc = new Listcell(employee.getFullName());
			li.appendChild(lc);
			lc = new Listcell(employee.getPhoneNumber());
			li.appendChild(lc);
			lc = new Listcell("");
			li.appendChild(lc);
			
			} else {
				Messagebox.show("Nik tersebut sudah terdaftar menjadi user");
			}
			
		} else {
			Messagebox.show("Data tidak ditemukan");
		}
		
	}

}
