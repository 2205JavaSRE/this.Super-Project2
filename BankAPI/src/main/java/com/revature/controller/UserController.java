package com.revature.controller;

import java.util.List;
import com.revature.models.User;
import com.revature.services.UserService;
import io.javalin.http.Context;

public class UserController {
	
	private UserService userService = new UserService();
	
	public boolean login(Context ctx) {
		if(userService.login(ctx.formParam("username"), ctx.formParam("password"))) {
			User selectedUser = userService.getUserByUsername(ctx.formParam("username"));
			ctx.cookieStore("access", true);
			ctx.cookieStore("userID", selectedUser.getUserID());
			ctx.cookieStore("manager", selectedUser.isManager());
			ctx.result("You are logged in");
			ctx.status(201);
			return true;
		}else {
			ctx.result("those credentials are invalid");
			ctx.status(401);
			return false;
		}
	}
	
}