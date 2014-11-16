package com.rs.composer;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;

import com.rs.model.Users;
import com.rs.util.CommonUtil;
import com.rs.util.DaoRegistry;

public class IndexComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;
	private final static Logger logger = Logger.getLogger(IndexComposer.class);
	
	@Listen("onCreate = #win ")
	public void setSession(){
		logger.info("set-session");
		
		SessionFactory sessionFactory = (SessionFactory) sessionZk.getAttribute("sessionFactory");
		if(sessionFactory==null){
			sessionFactory = DaoRegistry.getSessionFactory();
		}
		sessionZk.setAttribute("sessionFactory", sessionFactory);
		setSessionFactory();
		
		Users user = (Users) sessionZk.getAttribute(CommonUtil.LOGIN_USER);
		if(user!=null){
			Executions.sendRedirect("/home");
		}
		
	}
}
