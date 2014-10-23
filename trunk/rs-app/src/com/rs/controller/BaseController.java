package com.rs.controller;

import org.hibernate.SessionFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;


public class BaseController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 7631965267527060440L;

	protected SessionFactory sessionFactory;
	protected Session zkoosSession = Sessions.getCurrent();
	
	public void setSessionFactory(){
		//System.out.println("zkoosSession: "+zkoosSession);
		System.out.println("attr-rsapp: "+zkoosSession.getAttribute("rsapp"));
		
		if(zkoosSession.getAttribute("rsapp") == null){
			Executions.sendRedirect("/timeout.zul");//next, redirect to login page
		}else{
			sessionFactory= (SessionFactory) zkoosSession.getAttribute("sessionFactory");
		}	
	}
}
