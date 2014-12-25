package com.rs.composer;

import org.hibernate.SessionFactory;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;

import com.rs.model.Users;
import com.rs.util.CommonUtil;
import com.rs.util.HibernateUtil;

public class IndexComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;
	
	@Listen("onCreate = #win ")
	public void setSession(){
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		//System.out.println("sf1: "+sessionFactory);
		sessionZk.setAttribute(CommonUtil.SESSION_FACTORY, sessionFactory);
		//System.out.println("sf2: "+sessionZk.getAttribute(CommonUtil.SESSION_FACTORY));
		
		isLooged();
		Users user = (Users) sessionZk.getAttribute(CommonUtil.LOGIN_USER);
		if(user!=null){
			Executions.sendRedirect("/home");
		}
	}
}
