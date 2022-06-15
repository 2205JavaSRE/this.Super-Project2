package com.revature.controller;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

import com.revature.models.*;

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

public class RequestMapper {
	
	private UserController userController = new UserController();
	private TransactionController transactionController = new TransactionController();
	private BankAppControl bController = new BankAppControl();

	public void configureRoutes(Javalin app, PrometheusMeterRegistry registry) {
		new ClassLoaderMetrics().bindTo(registry);
		new JvmMemoryMetrics().bindTo(registry);
		new JvmGcMetrics().bindTo(registry);
		new JvmThreadMetrics().bindTo(registry);
		new UptimeMetrics().bindTo(registry);
		new ProcessorMetrics().bindTo(registry);
		new DiskSpaceMetrics(new File(System.getProperty("user.dir"))).bindTo(registry);
		
		AtomicInteger activeUsers = registry.gauge("numberGauge", new AtomicInteger(0));

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
			if(userController.login(ctx))
				activeUsers.incrementAndGet();			
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
		
		
		// User story 12
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
			
			bController.jointAccount(ctx);
		});
		
		app.post("/secondaccount", ctx ->{
			
			bController.secondaryAccount(ctx);
		});
		
	
	}
}