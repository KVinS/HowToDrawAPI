package com.websystique.springmvc.dao;

import java.util.List;

import com.websystique.springmvc.model.Employee;

public interface EmployeeDao {

	
	List<Employee> findAllEmployees();
	
	void deleteEmployeeBySsn(String ssn);
}
