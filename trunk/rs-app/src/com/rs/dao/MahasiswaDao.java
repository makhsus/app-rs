package com.rs.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

import com.rs.model.Mahasiswa;

public class MahasiswaDao {

	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public boolean saveOrUpdate(Mahasiswa obj){
		boolean result = false;
		System.out.println("obj: "+obj.getFullname());
		Session session = sessionFactory.openSession();
		try {
			//Session session = HibernateUtil.getSessionFactory().openSession();	
			session.beginTransaction();
			session.saveOrUpdate(obj);
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
	public List<Mahasiswa> listAll(){
		List<Mahasiswa> list = new ArrayList<>();
		Session session = sessionFactory.openSession();
		try {
			//Session session = HibernateUtil.getSessionFactory().openSession();
			Criteria cr = session.createCriteria(Mahasiswa.class);
			list = cr.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		return list;
	}
	
	
}
