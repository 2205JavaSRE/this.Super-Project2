package com.revature.controller;

import java.sql.SQLException;
import java.util.List;

import com.revature.models.Account;
import com.revature.services.BankService;

import io.javalin.http.Context;
import io.javalin.http.HttpCode;

public class BankAppControl {
	
	private BankService bService = new BankService();
	
	public void registerBankAccount(Context ctx) {
		String cusName = ctx.formParam("Customer Name");
		String cusLastName = ctx.formParam("Customer LName");
		String userName = ctx.formParam("username");
		String password = ctx.formParam("password");
		String type = ctx.formParam("Account Type");
		String Label = ctx.formParam("Label");
		
		bService.registerBankAccount(cusName, cusLastName, userName, password,type, type, Label);
		
		ctx.status(201);
		ctx.status(HttpCode.CREATED);
		ctx.result("New Account Registered");
		
		
	}
	
	public void jointAccount(Context ctx) {
		String primary = ctx.formParam("primary");
		String secondary = ctx.formParam("secondary"); 
		int intPrimary = Integer.parseInt(primary);
		int intsecondary = Integer.parseInt(secondary);
		bService.jointAccount(intPrimary, intsecondary);
		ctx.status(HttpCode.CREATED);
		ctx.result("Account Number " + intPrimary + " is joint with " + intsecondary +" number account ");
	}
	
	public void secondaryAccount(Context ctx) {
		String cusId = ctx.formParam("cusId");
		String type = ctx.formParam("Account Type");
		String label = ctx.formParam("Label");
		
		int intcusId = Integer.parseInt(cusId);
		String bankStatus = bService.accountCheck(intcusId);
		if(!bankStatus.equals("p")) {
			bService.secondaryBankAccount(intcusId, type, type, label);
			ctx.result("Second account created successfully");
			
		}else {
			ctx.result("Your account does not approved yet");
		}
	}
	public void selectAllAccountById(Context ctx) {
		
		String cusId = ctx.pathParam("id");
		int intcusId = Integer.parseInt(cusId);
		List<Account> accountList =bService.selectAccountById(intcusId);
		ctx.json(accountList);
	}
	public void withdrawById(Context ctx) throws SQLException {
		String accId = ctx.formParam("Account id");
		String amount = ctx.formParam("amount");
		int intaccId = Integer.parseInt(accId);
		double doubleAmount = Double.valueOf(amount);
		boolean statuss = bService.withdrawById(intaccId, doubleAmount);
		if(statuss) {
			ctx.result("withdrawed");
		}else {
			ctx.result("wrong input");
		}
	}

}
