package com.rs.composer;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;

import com.rs.model.Users;
import com.rs.util.CommonUtil;

public class MenuComposer extends BaseComposer {
	
	private static final long serialVersionUID = 1L;
	private static final int USER_ROLE_ADMIN = 1;
	private static final int USER_ROLE_RECEPTIONIST = 2;

	@Wire
	private Menu mnDataUsers, mnApotek, mnUserLogin;
	
	@Wire
	private Menuitem mntmHome, mntmPoly, mntmJp, mntmPatient, mntmFrontDesk, mntmSample;
	
	
	@Listen("onCreate = #pnlMenu")
	public void pnlMenu(){
		isLooged();
		
		Users user = (Users) sessionZk.getAttribute(CommonUtil.LOGIN_USER);
		if(user!=null)
			setMenuByRole(user);
	}
	
	@Listen("onClick = #mntmLogout")
	public void mntmLogoutClick(){
		sessionZk.setAttribute(CommonUtil.LOGIN_USER, null);
		Executions.sendRedirect("/login");
	}
	
	
	public void setMenuByRole(Users user){
		mnUserLogin.setLabel(CommonUtil.neatString(user.getIdEmployee().getFullName().trim()));
		
		int roleId = user.getIdRole().getIdRole().intValue();
		
		switch (roleId) {
		case USER_ROLE_ADMIN:
			
			break;
			
		case USER_ROLE_RECEPTIONIST:
			mntmHome.setVisible(false);
			mnDataUsers.setVisible(false);
			mntmPoly.setVisible(false);
			mntmJp.setVisible(true);
			mntmPatient.setVisible(true);
			mntmFrontDesk.setVisible(true);
			mnApotek.setVisible(false);
			mntmSample.setVisible(false);
			break;

		default:
			break;
		}
		
		
	}
	
}
