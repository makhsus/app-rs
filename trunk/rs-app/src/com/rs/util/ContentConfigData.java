package com.rs.util;

import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.rs.dao.ContentConfigDao;
import com.rs.model.ContentConfig;

public class ContentConfigData {
	private static final Logger logger = Logger.getLogger(ContentConfigData.class);
	
	ContentConfigDao contentConfig = new ContentConfigDao();
	
	public List<ContentConfig> getDataReligion(SessionFactory sessionFactory) {
		contentConfig.setSessionFactory(sessionFactory);
		Criterion cr1 = Restrictions.eq("category", "RELIGION");
		Criterion cr2 = Restrictions.eq("status", "Y");
		List<ContentConfig> listReligion = contentConfig.loadBy(Order.asc("idContentConfig"), cr1, cr2);
		return listReligion;
	}
	
	public List<ContentConfig> getDataBloodType(SessionFactory sessionFactory) {
		contentConfig.setSessionFactory(sessionFactory);
		Criterion cr1 = Restrictions.eq("category", "BLOOD_TYPE");
		Criterion cr2 = Restrictions.eq("status", "Y");
		List<ContentConfig> listBloodtype = contentConfig.loadBy(Order.asc("idContentConfig"), cr1, cr2);
		return listBloodtype;
	}
	
	public List<ContentConfig> getDataWork(SessionFactory sessionFactory) {
		contentConfig.setSessionFactory(sessionFactory);
		Criterion cr1 = Restrictions.eq("category", "WORK");
		Criterion cr2 = Restrictions.eq("status", "Y");
		List<ContentConfig> listBloodtype = contentConfig.loadBy(Order.asc("idContentConfig"), cr1, cr2);
		return listBloodtype;
	}
	
	public List<ContentConfig> getDataEducation(SessionFactory sessionFactory) {
		contentConfig.setSessionFactory(sessionFactory);
		Criterion cr1 = Restrictions.eq("category", "EDUCATION");
		Criterion cr2 = Restrictions.eq("status", "Y");
		List<ContentConfig> listBloodtype = contentConfig.loadBy(Order.asc("idContentConfig"), cr1, cr2);
		return listBloodtype;
	}
	
	public List<ContentConfig> getDataMaritalStatus(SessionFactory sessionFactory) {
		contentConfig.setSessionFactory(sessionFactory);
		Criterion cr1 = Restrictions.eq("category", "MARITAL_STATUS");
		Criterion cr2 = Restrictions.eq("status", "Y");
		List<ContentConfig> listMaritalStatus = contentConfig.loadBy(Order.asc("idContentConfig"), cr1, cr2);
		return listMaritalStatus;
	}

}
