package com.revature.controller;

import java.util.List;
import com.revature.models.Account;
import com.revature.models.User;
import com.revature.services.AccountService;
import io.javalin.http.Context;

public class AccountController {
	private AccountService accountService = new AccountService();
	private UserController userController = new UserController();

	public void viewCustomerAccounts(Context ctx) {
		int customerId = -1;
		try {
			customerId = Integer.parseInt(ctx.pathParam("customer_id"));
		} catch (Exception e) {
			ctx.result("Invalid customer_id in path parameter, please input an integer.");
			return;
		}
		
		// I can view the accounts from customerId IF:
		//   1) I am an employee
		//   2) I am the customer with customer_id
		if(!userController.isValidCredentials(ctx)) {
			ctx.result("Please login to view accounts.");
			return;
		}
		
		User me = userController.getUser(ctx);
		
		if(!me.isManager() && me.getCustomerID() != customerId) {
			ctx.result("You must be logged in as the customer associated "
					+ "with the customer_id, or logged in as a "
					+ "employee/manager to view these accounts.");
			return;
		}
		
		
		List<Account> accounts = accountService.selectAllAccountsByCustomerID(customerId);
		ctx.json(accounts);
		return;
	}

	public void acceptOrRejectAccount(Context ctx) {
		if(!userController.isValidCredentials(ctx) 
				|| !userController.getUser(ctx).isManager()) {
			ctx.result("Please login as an Employee to approve/reject accounts.");
			return;
		}
		
		int accountId;
		try {
			accountId = Integer.parseInt(ctx.formParam("account_id"));
		} catch (Exception e) {
			ctx.result("Please input an Integer for account_id in form params.");
			return;
		}
		
		Account account = accountService.selectAccountByID(accountId);
		
		if(account == null) {
			ctx.result("Could not find account with account id: " + accountId);
			return;
		}
		
		String status = ctx.formParam("status");
		try {
			account.setStatus(status);
		} catch (Exception e) {
			ctx.result("Invalid status. " + e.getMessage());
			return;
		}
		
		int numUpdated = accountService.updateAccount(account);
		if(numUpdated != 1) {
			ctx.result("Could not update account.");
			return;
		}
		
		ctx.result("Account status updated!");
		return;
	}

	public void getPendingAccounts(Context ctx) {
		if(!userController.isValidCredentials(ctx) 
				|| !userController.getUser(ctx).isManager()) {
			ctx.result("Please login as an Employee to approve/reject accounts.");
			return;
		}
		
		List<Account> pendingAccounts = accountService.selectAllAccountsByStatus("p");
		
		ctx.json(pendingAccounts);
		return ;
	}
	
	
}
