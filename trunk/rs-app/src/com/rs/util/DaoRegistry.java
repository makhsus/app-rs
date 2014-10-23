package com.rs.util;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DaoRegistry {
	private static final Logger logger = Logger.getLogger(DaoRegistry.class);
	
	private static ApplicationContext ctx;
	static{
		ctx = new ClassPathXmlApplicationContext("/config/context.xml");
	}

	public DaoRegistry() {
		// TODO Auto-generated constructor stub
		logger.info("Initiazation session factory");
	}
	
	public static SessionFactory getSessionFactory() {
		return (SessionFactory) ctx.getBean("factory", SessionFactory.class);
	}
	
	public static DataSource getDataSource() {
		return (DataSource) ctx.getBean("dataSource", DataSource.class);
	}

}
