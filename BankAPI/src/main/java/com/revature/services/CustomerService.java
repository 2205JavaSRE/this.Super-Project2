package com.revature.services;

import java.util.List;
import com.revature.models.Customer;
import com.revature.dao.CustomerDao;
import com.revature.dao.CustomerDaoImpl;

public class CustomerService {

	private CustomerDao cDao = new CustomerDaoImpl();
	
	public Customer selectCustomerByID(int id) {
		return cDao.selectCustomerByID(id);
	}
	
	public List<Customer> selectAllCustomers(){
		return cDao.selectAllCustomers();
	}
	
	public boolean insertCustomer(Customer c) {
		return cDao.insertCustomer(c) > 0;
	}
	
	public boolean updateCustomer(Customer c) {
		return cDao.updateCustomer(c);
	}

}
