package com.rs.composer;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;

import com.rs.dao.UsersDao;
import com.rs.model.Users;
import com.rs.util.CommonUtil;

public class MenuComposer extends BaseComposer {
	
	private static final long serialVersionUID = 1L;
	private static final int USER_ROLE_ADMIN = 1;
	private static final int USER_ROLE_RECEPTIONIST = 2;
	private Users userLogin;
	private static final Log logger = LogFactory.getLog(LogInOutComposer.class);

	@Wire
	private Menu mnDataUsers, mnApotek, mnUserLogin;
	
	@Wire
	private Menuitem mntmHome, mntmPoly, mntmJp, mntmPatient, mntmFrontDesk, mntmSample;
	
	
	@Listen("onCreate = #pnlMenu")
	public void pnlMenu(){
		isLooged();
		userLogin = (Users) sessionZk.getAttribute(CommonUtil.LOGIN_USER);
		if(userLogin!=null)
			setMenuByRole(userLogin);
	}
	
	@Listen("onClick = #mntmLogout")
	public void mntmLogoutClick(){
		UsersDao dao = new UsersDao();
		dao.setSessionFactory(sessionFactory);
		Users user = new Users();
		Date dateNow = new Date();
		userLogin = (Users) sessionZk.getAttribute(CommonUtil.LOGIN_USER);
		
		System.out.println("value : " + userLogin.getUserName());
		
		Criterion cr1 = Restrictions.eq("userName", userLogin.getUserName());
		List<Users> listUsers = dao.loadBy(Order.asc("idUser"), cr1);
		if (listUsers.size() > 0) {
			user = listUsers.get(0);
			user.setLastLogout(dateNow);
			
			try {
				dao.saveOrUpdate(user);
				System.out.println("save last logout user success");
				logger.info("save last logout user success");
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println("save last logout user failed because "+e.getMessage());
				logger.info("save last logout user failed because "+e.getMessage());
			}
			
		}
		
		System.out.println("user : " + userLogin.getUserName());
		System.out.println("date login : " + dateNow);
		logger.info("user : " + userLogin.getUserName());
		logger.info("date login : " + dateNow);
		
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
