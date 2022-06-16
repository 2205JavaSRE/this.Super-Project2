package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	private static String URL = System.getenv("db_url");
	private static String USERNAME = System.getenv("db_user");
	private static String PASSWORD = System.getenv("db_password");
	
	private static Connection connection = null;
	
	public static Connection getConnection() {
		
		if(connection != null) {
			return connection; //Make it go faster!
		}
		
		if(URL == null) {
			throw new Error("db_url (e.g. \"jdbc:postgresql://rds.com/database\" )is not set. please set the environment variable in your run configurations.");
		}
		if(USERNAME == null) {
			throw new Error("db_user is not set. please set the environment variable in your run configurations.");
		}
		if(PASSWORD == null) {
			throw new Error("db_password is not set. please set the environment variable in your run configurations.");
		}
		
		try {
			System.out.println("Making Connection...");
			connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
			System.out.println("Connection Made!");
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return connection;
	}
}
