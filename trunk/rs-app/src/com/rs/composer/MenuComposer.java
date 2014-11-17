package com.rs.composer;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Menu;

import com.rs.model.Users;
import com.rs.util.CommonUtil;

public class MenuComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;

	@Wire
	private Menu mnUserLogin;
	
	@Listen("onCreate = #pnlMenu")
	public void pnlMenu(){
		isLooged();
		
		Users user = (Users) sessionZk.getAttribute(CommonUtil.LOGIN_USER);
		if(user!=null)
			mnUserLogin.setLabel(user.getUserName().toUpperCase());
	}
	
	@Listen("onClick = #mntmLogout")
	public void mntmLogoutClick(){
		sessionZk.setAttribute(CommonUtil.LOGIN_USER, null);
		Executions.sendRedirect("/login");
	}
	
}
