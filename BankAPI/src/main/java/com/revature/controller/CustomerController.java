package com.revature.controller;

import java.util.List;
import com.revature.models.Customer;
import com.revature.models.User;
import com.revature.services.CustomerService;
import com.revature.services.UserService;

import io.javalin.http.Context;

public class CustomerController {
	
	private CustomerService customerService = new CustomerService();
	private UserController userController = new UserController();
	private UserService userService = new UserService();

	public void getAllCustomers(Context ctx) {
		if(!userController.isValidCredentials(ctx)) {
			ctx.result("Please login as an employee to view the customers.");
			return;
		}
		
		User me = userController.getUser(ctx);
		
		if(!me.isManager()) {
			ctx.result("You must be an employee to view the customers.");
			return;
		}
		
		List<Customer> customers = customerService.selectAllCustomers();
		ctx.json(customers);
		return;
	}

	public void getCustomerByUsername(Context ctx) {
		String username = ctx.pathParam("username");
		if(username == null) {
			ctx.result("You must put a username in the path to perform the search.");
			return;
		}
		
		if(!userController.isValidCredentials(ctx)) {
			ctx.result("Please login as an employee to view this customer.");
			return;
		}
		
		User me = userController.getUser(ctx);
		
		if(!me.isManager() && !me.getUsername().equals(username)) {
			ctx.result("You must be an employee to view this customer"
					+ ", or logged in as the customer with the corresponding username.");
			return;
		}
		User user = userService.getUserByUsername(username);
		if(user == null) {
			ctx.result("There is no user with username: " + username);
			return;
		}
		int customerId = user.getCustomerID();
		Customer customer = customerService.selectCustomerByID(customerId);
		
		ctx.json(customer);
		return;
	}
	
}
