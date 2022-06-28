package com.revature.controller;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

import com.revature.models.*;
import com.revature.util.ConnectionFactory;

import io.javalin.Javalin;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.DiskSpaceMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;

public class RequestMapper {
	
	private UserController userController = new UserController();
	private CustomerController customerController = new CustomerController();
	private TransactionController transactionController = new TransactionController();
	private BankAppControl bController = new BankAppControl();
	private AccountController accountController = new AccountController();


	public void configureRoutes(Javalin app, PrometheusMeterRegistry registry) {
		new ClassLoaderMetrics().bindTo(registry);
		new JvmMemoryMetrics().bindTo(registry);
		new JvmGcMetrics().bindTo(registry);
		new JvmThreadMetrics().bindTo(registry);
		new UptimeMetrics().bindTo(registry);
		new ProcessorMetrics().bindTo(registry);
		new DiskSpaceMetrics(new File(System.getProperty("user.dir"))).bindTo(registry);
		
		AtomicInteger activeUsers = registry.gauge("numberGauge", new AtomicInteger(0));
		//Counters for request_submit
		Counter counter = Counter.builder("total_login").description("track number").tag("purpose", "request_login").register(registry);
		//Counters for request for reimbursement
		Counter counter1 = Counter.builder("total_login_success").description("track number").tag("purpose", "login_success").register(registry);
		//Counter counter2 = Counter.builder("total_login_success").description("track number").tag("purpose", "login_success").register(registry);
		//Counter counter1 = Counter.builder("http_request_total").
		CollectorRegistry registry2 = new CollectorRegistry();
		Gauge gauge= Gauge.build().name("Gauge_test").help("size").register(registry2);
//		io.micrometer.core.instrument.Gauge.builder("is_rds_connected",
//				new ValidRdsConnectionGauge(), obj -> obj.value()).register(registry);
		io.micrometer.core.instrument.Gauge.builder("is_rds_connected",() -> {
			try {
				int timeoutInSeconds = 3;
				java.sql.Connection conn = ConnectionFactory.getConnection();
				if(conn != null && conn.isValid(timeoutInSeconds)) {
					return 1.0;
				}
			} catch (java.sql.SQLException e) {
				return 0;
			}
			return 0;
		}).register(registry);


		
		
		app.get("/", ctx -> {
			ctx.html("<h1>Please Login to continue</h1>\n"
					+ "<form method=\"post\" action=\"/login\">\n"
					+ "  <label for=\"username\">Username:</label><br>\n"
					+ "  <input type=\"text\" id=\"username\" name=\"username\"><br>\n"
					+ "  <label for=\"password\">Password:</label><br>\n"
					+ "  <input type=\"password\" id=\"password\" name=\"password\">\n"
					+ "  <button>Submit</button>\n"
					+ "</form>");
		});
		
		app.get("/metrics", ctx ->{
			ctx.result(registry.scrape());
		});
		
		app.post("/login", ctx -> {
			counter.increment(1);
			if(userController.login(ctx)) {
				activeUsers.incrementAndGet();	
				counter1.increment(1);
			}	
		});
		
		app.get("/login", ctx -> {
			if(ctx.cookieStore("access") != null && ctx.cookieStore("access").equals(true)) {
				ctx.result("You are logged in");
				ctx.status(201);
			}
			else {
				ctx.result("those credentials are invalid");
				ctx.status(401);
			}
		});

		app.get("/logout", ctx -> {
			ctx.clearCookieStore();
			activeUsers.decrementAndGet();
			ctx.result("You are logged out");
			ctx.status(201);
		});
		
		//TODO: User Story 7: As the system, I reject invalid transactions.
		// TODO: Reject: A withdrawal that would result in a negative balance.
		// TODO: Reject: A deposit or withdrawal of negative money.
		// TODO: only allow transfers between approved accounts.
		
		//User story 8: As an employee, I can approve or reject an account registration by a user.
		//view pending accounts
		app.get("/pendingAccounts", ctx -> accountController.getPendingAccounts(ctx));
		//accept/reject account.
		app.patch("/account", ctx -> accountController.acceptOrRejectAccount(ctx));
		
		//User story 9: As an employee, I can view a customer's bank accounts.
		app.get("/accounts/{customer_id}", ctx -> accountController.viewCustomerAccounts(ctx));
		
		//9.2 (extra) as an employee, I can view the list of customers.
		app.get("/customers", ctx -> customerController.getAllCustomers(ctx));
		
		//9.3 (extra) as an employee, I can find a customer by their username.
		app.get("/customer/{username}", ctx -> customerController.getCustomerByUsername(ctx));
		
		//User story 10: As a customer, I can post a money transfer to another account.
		app.post("/transfers", ctx -> transactionController.newTransaction(ctx));
		
		//User story 11: As a customer, I can accept a money transfer from another account.
		//View money Transfers
		app.get("/transfers", ctx -> transactionController.viewTransfers(ctx));
		//Approve/deny money transfers
		app.patch("/transfers", ctx -> transactionController.approveOrDenyTransfer(ctx));
		
		// User story 12: An employee, I can view a log of all transactions.
		app.get("/transactions", ctx -> {
			if(ctx.cookieStore("access") != null && ctx.cookieStore("access").equals(true)
					&& ctx.cookieStore("manager") != null 
					&& ctx.cookieStore("manager").equals(true)) {
				transactionController.getAllTransactions(ctx);
				ctx.status(201);
			}
			else {
				ctx.result("those credentials are invalid");
				ctx.status(401);
			}
		});
		
		app.post("/register", ctx ->{
			bController.registerBankAccount(ctx);
			
		});
		
		app.post("/joint", ctx ->{
			
			if(ctx.cookieStore("access") != null && ctx.cookieStore("access").equals(true)) {
				bController.jointAccount(ctx);
				ctx.status(201);
			}
			else {
				ctx.result("those credentials are invalid");
				ctx.status(401);
			}
		});
		
		app.post("/secondaccount", ctx ->{
			if(ctx.cookieStore("access") != null && ctx.cookieStore("access").equals(true)) {
				bController.secondaryAccount(ctx);
				ctx.status(201);
			}
			else {
				ctx.result("those credentials are invalid");
				ctx.status(401);
			}
		});
		
		app.get("/accounts/{id}", ctx ->{
			if(ctx.cookieStore("access") != null && ctx.cookieStore("access").equals(true)) {
				bController.selectAllAccountById(ctx);
				ctx.status(201);
			}
			else {
				ctx.result("those credentials are invalid");
				ctx.status(401);
			}
			
		});
		
		app.post("/withdraw", ctx ->{
			if(ctx.cookieStore("access") != null && ctx.cookieStore("access").equals(true)) {
				bController.withdrawById(ctx);
				ctx.status(201);
			}
			else {
				ctx.result("those credentials are invalid");
				ctx.status(401);
			}
			
		});
		
		app.post("/deposit", ctx ->{
			if(ctx.cookieStore("access") != null && ctx.cookieStore("access").equals(true)) {
				bController.depositById(ctx);
				ctx.status(201);
			}
			else {
				ctx.result("those credentials are invalid");
				ctx.status(401);
			}			
			
		});
		
		app.get("/version", ctx ->{
			
			VersionController.getVersion(ctx);
			
		});
		
	
	}
}