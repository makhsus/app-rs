package com.rs.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Order;

import com.rs.model.Rumah;

public class RumahDao {
	
	private Session session;
	
	public RumahDao(SessionFactory sessionFactory) {
		this.session = sessionFactory.openSession();
	}
	
	public boolean saveOrUpdate(Rumah obj){
		try {
			session.beginTransaction();
			session.saveOrUpdate(obj);
			session.getTransaction().commit();
			session.close();
			return true;
		} catch (Exception e) {
			//create log
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Rumah> loadAll(Order order){
		List<Rumah> list = new ArrayList<>();
		try {
			Criteria cr = session.createCriteria(Rumah.class);
			cr.addOrder(order);
			list = cr.list();
			
		} catch (Exception e) {
			// create log
			e.printStackTrace();
		}
		return list;
	}

}
