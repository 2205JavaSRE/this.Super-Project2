package com.revature.dao;

import com.revature.models.Customer;
import java.util.List;

public interface CustomerDao {
	
	public Customer selectCustomerByID(int id);
		
	public List<Customer> selectAllCustomers();
	
	public int insertCustomer(Customer c);
	
	public boolean updateCustomer(Customer c);

}
