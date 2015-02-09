package com.rs.composer;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import com.rs.dao.UsersDao;
import com.rs.model.Users;
import com.rs.util.CommonUtil;

public class UsersComposer extends BaseComposer {
	
	private static final long serialVersionUID = 1L;
	private static final Log logger = LogFactory.getLog(UsersComposer.class);
	
	private String patternDate = "DD-MM-YYYY";
	private Users userLogin;
	
	@Wire
	private Window winUsers;
	@Wire
	private Listbox lbxUsers;
	
	@Listen ("onCreate = #winUsers")
	public void win(){
		isLooged();
		loadDataUsers();
		userLogin = (Users)sessionZk.getAttribute(CommonUtil.LOGIN_USER);
	}
	
	public void loadDataUsers() {
		Users users = new Users();
		UsersDao daoUser = new UsersDao();
		
		Criterion cr1 = Restrictions.eq("status", "Y");
		List<Users> listUsers = daoUser.loadBy(Order.desc("idUser"), cr1);
		
		lbxUsers.getItems().clear();
		int number = 1;
		
		for(Users obj: listUsers){
			Listitem li = new Listitem();
			lbxUsers.appendChild(li);
			
			String createDate = CommonUtil.dateFormat(obj.getCreateDate(), patternDate);
			String updateDate = CommonUtil.dateFormat(obj.getLastUpdate(), patternDate);
			
			if ((updateDate.equals("")) || (updateDate == "")) {
				updateDate = "-";
			}
			
			Listcell lc = new Listcell(Integer.toString(number++));
			li.appendChild(lc);
			lc = new Listcell(createDate);
			li.appendChild(lc);
			lc = new Listcell(updateDate);
			li.appendChild(lc);
			lc = new Listcell(obj.getIdEmployee().getNik());
			li.appendChild(lc);
			lc = new Listcell(obj.getUserName());
			li.appendChild(lc);
			lc = new Listcell(obj.getIdRole().getDescription());
			li.appendChild(lc);
		}
	}

}
