package com.rs.composer;

import org.hibernate.SessionFactory;
import org.zkoss.zk.ui.select.annotation.Listen;

import com.rs.util.CommonUtil;
import com.rs.util.HibernateUtil;

public class HomeComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;

	@Listen ("onCreate = #winHome ")
	public void winHome(){
		//System.out.println("Home");
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		sessionZk.setAttribute(CommonUtil.SESSION_FACTORY, sessionFactory);
		
		isLooged();
	}
	
}
