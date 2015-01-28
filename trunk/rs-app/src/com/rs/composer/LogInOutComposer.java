package com.rs.composer;

import java.util.List;

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
import com.rs.util.HibernateUtil;

public class LogInOutComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;
	
	private static final int USER_ROLE_ADMIN = 1;
	private static final int USER_ROLE_RECEPTIONIST = 2;

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
	
	@Listen("onClick = #btnLogin")
	public void btnLogin(){
		String username = tbxUsername.getText().trim();
		String password = tbxPassword.getText().trim();
		
		if(username.equalsIgnoreCase("") || password.equalsIgnoreCase("")){
			Messagebox.show("Please enter Username & Password", "Error", Messagebox.OK, Messagebox.ERROR);
			return;
		}
		
		UsersDao dao = new UsersDao();
		dao.setSessionFactory(sessionFactory);
		Criterion crLogon1 = Restrictions.eq("userName", username);
		Criterion crLogon2 = Restrictions.eq("password", password);
		List<Users> listUsers = dao.loadBy(Order.asc("idUser"), crLogon1, crLogon2);
		//List<Users> listUsers = dao.loadAll(null);
		System.out.println("listUser: "+listUsers.size());
		
		if(listUsers.size()<1){
			Messagebox.show("Sorry, We can't find your account!");
			return;
		}else{
			Users user = listUsers.get(0);
			sessionZk.setAttribute(CommonUtil.LOGIN_USER, user);
			
			int roleId = user.getIdRole().getIdRole().intValue();
			
			switch (roleId) {
			case USER_ROLE_ADMIN:
				Executions.sendRedirect("/home");
				break;
				
			case USER_ROLE_RECEPTIONIST:
				Executions.sendRedirect("/admission");
				break;

			default:
				break;
			}
			
		}
		
	}
}
