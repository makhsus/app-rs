package com.rs.composer;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;

import com.rs.util.CommonUtil;

public class BaseComposer extends SelectorComposer<Component> {
	private static final long serialVersionUID = -2082690416380117130L;

	protected Session sessionZk = Sessions.getCurrent();
	
	public void isLooged(){
		System.out.println("BaseComposer, attr-LOGIN_USER: "+sessionZk.getAttribute(CommonUtil.LOGIN_USER));
		if(sessionZk.getAttribute(CommonUtil.LOGIN_USER) == null){
			Executions.sendRedirect("/login");
		}
		return;
	}
	
}
