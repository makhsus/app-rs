package com.rs.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.classic.Session;

import com.rs.model.Patient;
import com.rs.util.HibernateUtil;

public class PatientDao {

	public boolean saveOrUpdate(Patient obj){
		boolean result = false;
		System.out.println("obj: "+obj.getName());
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.saveOrUpdate(obj);
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Patient> listAll(){
		List<Patient> list = new ArrayList<>();
		
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();
			Criteria cr = session.createCriteria(Patient.class);
			list = cr.list();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	
}
