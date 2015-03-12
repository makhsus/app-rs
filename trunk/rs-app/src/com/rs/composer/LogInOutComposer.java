package com.rs.composer;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.rs.dao.UsersDao;
import com.rs.model.Users;
import com.rs.util.CommonUtil;
import com.rs.util.EncryptData;
import com.rs.util.HibernateUtil;

public class LogInOutComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;
	
	private static final Log logger = LogFactory.getLog(LogInOutComposer.class);
	
	private static final int USER_ROLE_ADMIN = 1;
	private static final int USER_ROLE_RECEPTIONIST = 2;
	private String patternDate = "DD-MM-YYYY";
	private Users userLogin;

	@Wire
	private Textbox tbxUsername, tbxPassword;
	
	@Listen("onCreate = #winLogin ")
	public void winLogin(){
		sessionFactory = (SessionFactory) sessionZk.getAttribute(CommonUtil.SESSION_FACTORY);
		if(sessionFactory==null){
			sessionFactory = HibernateUtil.getSessionFactory();
			sessionZk.setAttribute(CommonUtil.SESSION_FACTORY, sessionFactory);
		}
		
		System.out.println("Session: "+sessionZk.getAttribute(CommonUtil.LOGIN_USER));
		
		if(sessionZk.getAttribute(CommonUtil.LOGIN_USER) != null){
			Executions.sendRedirect("/home");
			return;
		}
	}
	
	@Listen("onCreate = #winLogout ")
	public void winLogout(){
		Messagebox.show("Waktu sesi Login telah habis, silahkan Login kembali", "Error", Messagebox.OK, Messagebox.ERROR);
		Executions.sendRedirect("/login");
	}
	
	@Listen("onClick = #btnLogin")
	public void btnLogin() {
		String username = tbxUsername.getText().trim();
		String password = tbxPassword.getText().trim();
		
		if(username.equalsIgnoreCase("") || password.equalsIgnoreCase("")){
			Messagebox.show("Please enter Username & Password", "Error", Messagebox.OK, Messagebox.ERROR);
			return;
		}
		
		String passwordEncryption = "";
		
		try {
			passwordEncryption = EncryptData.passwordEncrypt(password);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		Date dateNow = new Date();
		
		System.out.println("user : " + username);
		System.out.println("date login : " + dateNow);
		logger.info("user : " + username);
		logger.info("date login : " + dateNow);
		System.out.println("pass : " + passwordEncryption);
		
		
		
		UsersDao dao = new UsersDao();
		dao.setSessionFactory(sessionFactory);
		Criterion crLogon1 = Restrictions.eq("userName", username);
		Criterion crLogon2 = Restrictions.eq("password", passwordEncryption);
		List<Users> listUsers = dao.loadBy(Order.asc("idUser"), crLogon1, crLogon2);
		//List<Users> listUsers = dao.loadAll(null);
		System.out.println("listUser: "+listUsers.size());
		
		if(listUsers.size()<1){
			Messagebox.show("Sorry, We can't find your account!");
			return;
		}else{
			Users user = listUsers.get(0);
			sessionZk.setAttribute(CommonUtil.LOGIN_USER, user);
			
			user.setLastLogin(dateNow);
			
			try {
				dao.saveOrUpdate(user);
				System.out.println("save last login user success");
				logger.info("save last login user success");
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println("save last login user failed because "+e.getMessage());
				logger.info("save last login user failed because "+e.getMessage());
			}
			
			
			int roleId = user.getIdSubRole().getIdRole().getIdRole().intValue();
			
			switch (roleId) {
			case USER_ROLE_ADMIN:
				Executions.sendRedirect("/home");
				break;
				
			case USER_ROLE_RECEPTIONIST:
				Executions.sendRedirect("/frontdesk/home");
				break;

			default:
				break;
			}
			
		}
		
	}
	
	public void sessionLogOut() {
		
	}
}
