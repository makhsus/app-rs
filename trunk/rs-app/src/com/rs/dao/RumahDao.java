package com.rs.dao;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

import com.rs.model.Rumah;

public class RumahDao {

	private Session session;
	
	public RumahDao(SessionFactory sessionFactory) {
		System.out.println("sessFactory: "+sessionFactory);
		this.session = sessionFactory.getCurrentSession();
	}
	
	public boolean saveOrUpdate(Rumah obj){
		boolean result = false;
		
		try {
			session.beginTransaction();
			session.saveOrUpdate(obj);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
}
