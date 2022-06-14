package com.revature.controller;

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

}
