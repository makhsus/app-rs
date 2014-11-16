package com.rs.composer;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.rs.model.Users;
import com.rs.util.CommonUtil;

public class LogInOutComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;

	@Wire
	private Textbox tbxUsername, tbxPassword;
	
	@Listen("onCreate = #winLogin ")
	public void winLogin(){
		System.out.println("WII: "+sessionZk.getAttribute(CommonUtil.LOGIN_USER));
		
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
		
		
		Users user = new Users();
		user.setUserName(username);
		user.setPassword(password);
		sessionZk.setAttribute(CommonUtil.LOGIN_USER, user);
		Executions.sendRedirect("/home");
		
	}
}
