package com.rs.composer;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DaoSupport;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
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
import com.rs.model.MedicalRecords;
import com.rs.model.Users;
import com.rs.util.CommonUtil;
import com.rs.util.EncryptData;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
@SuppressWarnings("unused")
public class UsersComposer extends BaseComposer {
	
	private static final long serialVersionUID = 1L;
	private static final Log logger = LogFactory.getLog(UsersComposer.class);
	
	private String patternDate = "DD-MM-YYYY";
	private Users userLogin;
	
	@Wire
	private Window winUsers, winAddUsers;
	@Wire
	private Listbox lbxUsers, lbxUsersSelected, lbxRoles, lbxSubRoles;
	@Wire
	private Label lblNik, lblIdEmployee;
	@Wire
	private Grid grdSearchNik;
	@Wire
	private Textbox txbSearchNik, tbxUserName, tbxPassword, tbxConfirmPassword;
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
	
	@Listen ("onCreate = #winAddUsers")
	public void winAddUsers(){
		isLooged();
		String nik = (String) winAddUsers.getAttribute("nik");
		lblNik.setValue(nik);
		
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
	
	@Listen ("onClick = #btnAddUser")
	public void btnAddUserClick(){
		isLooged();
		addUser();
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
			lc = new Listcell(obj.getIdSubRole().getDescription());
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
			button.addEventListener("onClick", new NewUser(employee.getIdEmployee()));
			button.setLabel("Jadikan User Baru");
			lbxUsersSelected.appendChild(li);
			
			
			Listcell lc = new Listcell(employee.getNik());
			li.appendChild(lc);
			lc = new Listcell(employee.getFullName());
			li.appendChild(lc);
			lc = new Listcell(employee.getPhoneNumber());
			li.appendChild(lc);
			lc = new Listcell();
			lc.appendChild(button);
			li.appendChild(lc);
			
			} else {
				Messagebox.show("Nik tersebut sudah terdaftar menjadi user");
			}
			
		} else {
			Messagebox.show("Data tidak ditemukan");
		}
		
	}
	
	public void addUser() {
		String nik = lblNik.getValue();
		String userName = tbxUserName.getText();
		String password = tbxPassword.getText();
		String confirmPassword = tbxConfirmPassword.getText();
		System.out.println("nik add user : "+ nik);
		logger.info("nik add user : "+ nik);
		
		UsersDao dao = new UsersDao();
		Users user = new Users();
		dao.setSessionFactory(sessionFactory);
		
			String passwordHash = "";
		try {
			passwordHash = EncryptData.passwordEncrypt(password);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		Criterion cr1 = Restrictions.eq("userName", userName);
		List<Users> listUser = dao.loadBy(Order.asc("idUser"), cr1);
		
		if (listUser.size() > 0) {
			Messagebox.show("userName "+userName+" sudah digunakan");
		} else {
			user.setUserName(userName);
			user.setPassword(passwordHash);
			user.setCreateDate(new Date());
		}
	}
	
	class NewUser implements EventListener<Event> {

		private Long id;
		
		public NewUser(Long id) {
			this.id = id;
		}
		
		@Override
		public void onEvent(Event arg0) throws Exception {
			EmployeeDao dao = new EmployeeDao();
			Employee employee = new Employee();
			dao.setSessionFactory(sessionFactory);
			Criterion cr1 = Restrictions.eq("idEmployee", id);
			List<Employee> listEmployee = dao.loadBy(Order.asc("idEmployee"), cr1);
			
			if (listEmployee.size() > 0) {
				employee = listEmployee.get(0);
				Window windowAdd = (Window) Executions.createComponents("/WEB-INF/zul/user/add_user.zul", null, null);
				windowAdd.setAttribute("nik", employee.getNik());
				windowAdd.doModal();
			} else {
				Messagebox.show("Data tidak ditemukan");
			}
			
		}
		
	}

}
