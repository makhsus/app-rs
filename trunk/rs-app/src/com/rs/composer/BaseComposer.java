package com.rs.composer;

import org.hibernate.SessionFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;

public class BaseComposer extends SelectorComposer<Component> {
	private static final long serialVersionUID = -2082690416380117130L;

	protected SessionFactory sessionFactory;
	protected Session zkSession = Sessions.getCurrent();
	
	public void setSessionFactory(){
		System.out.println("zkSession: "+zkSession);
		System.out.println("attr-rsapp: "+zkSession.getAttribute("rsapp"));
		
		if(zkSession.getAttribute("rsapp") == null){
			Executions.sendRedirect("/login.zul");//next, redirect to login page
		}else{
			sessionFactory= (SessionFactory) zkSession.getAttribute("sessionFactory");
		}	
	}
	
	
}
