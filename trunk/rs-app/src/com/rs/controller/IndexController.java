package com.rs.controller;

import java.net.MalformedURLException;

import org.hibernate.SessionFactory;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;

import com.rs.util.DaoRegistry;

public class IndexController extends BaseController {
	private static final long serialVersionUID = 1L;

	@Listen("onCreate = #win ")
	public void connectToDb(){
		SessionFactory sessionFactory = DaoRegistry.getSessionFactory();
		System.out.println("sessionFactory-Index: "+sessionFactory);
		zkoosSession.setAttribute("sessionFactory", sessionFactory);
		zkoosSession.setAttribute("rsapp", "next-value-change-to-login-object");
		
		try {
			Executions.encodeToURL("index.zul");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
