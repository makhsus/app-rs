package com.rs.composer;

import org.hibernate.SessionFactory;
import org.zkoss.zk.ui.select.annotation.Listen;

import com.rs.util.DaoRegistry;

public class IndexComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;

	
	@Listen("onCreate = #win ")
	public void connectToDatabase(){
		SessionFactory sessionFactory = DaoRegistry.getSessionFactory();
		System.out.println("sessionFactory-Index: "+sessionFactory);
		zkSession.setAttribute("sessionFactory", sessionFactory);
		setSessionFactory();
	}
}