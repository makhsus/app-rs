package com.rs.composer;

import org.hibernate.SessionFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;

import com.rs.util.CommonUtil;
import com.rs.util.HibernateUtil;

public class BaseComposer extends SelectorComposer<Component> {
	private static final long serialVersionUID = -2082690416380117130L;

	protected SessionFactory sessionFactory = null;
	protected Session sessionZk = Sessions.getCurrent();
	
	public void isLooged(){
		sessionFactory = (SessionFactory) sessionZk.getAttribute(CommonUtil.SESSION_FACTORY);
		if(sessionFactory==null){
			sessionFactory = HibernateUtil.getSessionFactory();
			sessionZk.setAttribute(CommonUtil.SESSION_FACTORY, sessionFactory);
		}
		
		
		//System.out.println("BaseComposer, attr-LOGIN_USER: "+sessionZk.getAttribute(CommonUtil.LOGIN_USER));
		if(sessionZk.getAttribute(CommonUtil.LOGIN_USER) == null){
			Executions.sendRedirect("/login");
		}
		return;
	}
	
}
