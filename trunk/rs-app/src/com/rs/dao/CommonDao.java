package com.rs.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

public class CommonDao<T> {

	private Class<T> objectClass;
	private SessionFactory sessionFactory;

	public CommonDao(Class<T> objectClass) {
		this.objectClass = objectClass;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public boolean saveOrUpdate(T object) {
		boolean result = false;
		Session session = sessionFactory.openSession();
		try {
			// Session session =
			// HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.saveOrUpdate(object);
			session.getTransaction().commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}
	
	public boolean delete(T object){
		boolean result = false;
		Session session = sessionFactory.openSession();
		try {
			// Session session =
			// HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.delete(object);
			session.getTransaction().commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<T> loadAll(Order order) {
		List<T> list = new ArrayList<>();
		Session session = sessionFactory.openSession();
		try {
			Criteria cr = session.createCriteria(objectClass);
			if (order!=null) {
				cr.addOrder(order);
			}
			list = cr.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Object> loadBy(Order order, Criterion... criterions) {
		List<Object> list = new ArrayList<>();
		Session session = sessionFactory.openSession();
		try {
			Criteria cr = session.createCriteria(objectClass);

			if (order!=null) {
				cr.addOrder(order);
			}
			
			if (criterions!=null) {
				for(Criterion crit : criterions) {
					cr.add(crit);
				}
			}

			list = cr.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}
}
