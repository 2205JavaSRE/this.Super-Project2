package com.revature.controller;

import io.javalin.http.Context;

/**
 * I made this a separate file so that we can update the version without making merge conflicts.
 */
public class VersionController {

	public static void getVersion(Context ctx) {
		ctx.status(200); //Status Ok.
		ctx.result("Version 1.0.0 of the BankAPI by Team: this.super()");
	}

}
