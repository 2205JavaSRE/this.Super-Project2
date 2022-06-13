package com.revature.models;

import java.util.Objects;

public class User {
	private int userID;
	private int customerID;
	private String username;
	private String password;
	private boolean isManager;
	public User(int userID, int customerID, String username, String password, boolean isManager) {
		super();
		this.userID = userID;
		this.customerID = customerID;
		this.username = username;
		this.password = password;
		this.isManager = isManager;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public int getCustomerID() {
		return customerID;
	}
	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isManager() {
		return isManager;
	}
	public void setManager(boolean isManager) {
		this.isManager = isManager;
	}
	@Override
	public int hashCode() {
		return Objects.hash(customerID, isManager, password, userID, username);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return customerID == other.customerID && isManager == other.isManager
				&& Objects.equals(password, other.password) && userID == other.userID
				&& Objects.equals(username, other.username);
	}
	@Override
	public String toString() {
		return "User [userID=" + userID + ", customerID=" + customerID + ", username=" + username + ", password="
				+ password + ", isManager=" + isManager + "]";
	}
}
