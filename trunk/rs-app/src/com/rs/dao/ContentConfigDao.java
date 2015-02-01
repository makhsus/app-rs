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
	
	
	public List<ContentConfig> getReligions(){
		List<ContentConfig> result = new ArrayList<>();
		
		Criterion criterion = Restrictions.eq("category", "RELIGION");
		try{
			result = loadBy(Order.asc("id"), criterion);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	public List<ContentConfig> getEducations(){
		List<ContentConfig> result = new ArrayList<>();
		
		Criterion criterion = Restrictions.eq("category", "EDUCATION");
		try{
			result = loadBy(Order.asc("id"), criterion);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	public List<ContentConfig> getOccupations(){
		List<ContentConfig> result = new ArrayList<>();
		
		Criterion criterion = Restrictions.eq("category", "WORK");
		try{
			result = loadBy(Order.asc("id"), criterion);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}

}
