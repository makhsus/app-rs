package com.rs.composer;

import org.zkoss.zk.ui.select.annotation.Listen;

public class HomeComposer extends BaseComposer {
	private static final long serialVersionUID = 1L;

	@Listen ("onCreate = #winHome ")
	public void winHome(){
		setSessionFactory();
	}
	
}
