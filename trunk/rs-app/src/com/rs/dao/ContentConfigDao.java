package com.rs.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.rs.model.ContentConfig;

public class ContentConfigDao extends CommonDao<ContentConfig> {

	public ContentConfigDao() {
		super(ContentConfig.class);
	}
	
	
	public List<ContentConfig> getDataReligion(){
		List<ContentConfig> result = new ArrayList<>();
		Criterion cr1 = Restrictions.eq("category", "RELIGION");
		Criterion cr2 = Restrictions.eq("status", "Y");
		
		try{
			result = loadBy(Order.asc("idContentConfig"), cr1, cr2);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	public List<ContentConfig> getDataEducation(){
		List<ContentConfig> result = new ArrayList<>();
		Criterion cr1 = Restrictions.eq("category", "EDUCATION");
		Criterion cr2 = Restrictions.eq("status", "Y");
		
		try{
			result = loadBy(Order.asc("idContentConfig"), cr1, cr2);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	public List<ContentConfig> getDataWork(){
		List<ContentConfig> result = new ArrayList<>();
		Criterion cr1 = Restrictions.eq("category", "WORK");
		Criterion cr2 = Restrictions.eq("status", "Y");
		
		try{
			result = loadBy(Order.asc("idContentConfig"), cr1, cr2);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	public List<ContentConfig> getDataBloodType(){
		List<ContentConfig> result = new ArrayList<>();
		Criterion cr1 = Restrictions.eq("category", "BLOOD_TYPE");
		Criterion cr2 = Restrictions.eq("status", "Y");
		
		try{
			result = loadBy(Order.asc("idContentConfig"), cr1, cr2);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	public List<ContentConfig> getDataMaritalStatus(){
		List<ContentConfig> result = new ArrayList<>();
		Criterion cr1 = Restrictions.eq("category", "MARITAL_STATUS");
		Criterion cr2 = Restrictions.eq("status", "Y");
		
		try{
			result = loadBy(Order.asc("idContentConfig"), cr1, cr2);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}

}
