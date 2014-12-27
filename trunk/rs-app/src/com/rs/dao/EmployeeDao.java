package com.rs.dao;

import com.rs.model.Employee;

public class EmployeeDao extends CommonDao<Employee> {

	public EmployeeDao() {
		super(Employee.class);
	}

}
