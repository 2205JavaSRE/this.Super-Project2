package com.revature.models;

import java.util.Objects;

public class Account {

	private int accountID;
	private int primaryCustomerID;
	private int secondaryCustomerID;
	private double balance;
	private String type;
	private String status;
	private String label;
	
	public Account(int accountID, int primaryCustomerID, int secondaryCustomerID, double balance, String type,
			String status, String label) {
		super();
		this.accountID = accountID;
		this.primaryCustomerID = primaryCustomerID;
		this.secondaryCustomerID = secondaryCustomerID;
		this.balance = balance;
		this.type = type;
		setStatus(status);
		this.label = label;
	}

	public int getAccountID() {
		return accountID;
	}

	public void setAccountID(int accountID) {
		this.accountID = accountID;
	}

	public int getPrimaryCustomerID() {
		return primaryCustomerID;
	}

	public void setPrimaryCustomerID(int primaryCustomerID) {
		this.primaryCustomerID = primaryCustomerID;
	}

	public int getSecondaryCustomerID() {
		return secondaryCustomerID;
	}

	public void setSecondaryCustomerID(int secondaryCustomerID) {
		this.secondaryCustomerID = secondaryCustomerID;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) throws IllegalArgumentException {
		switch(status) {
		case "a": // Approved
		case "d": // Denied
		case "p": // Pending
			this.status = status;
			return;
		default:
			throw new IllegalArgumentException("Invalid transaction status: " + status);
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountID, balance, label, primaryCustomerID, secondaryCustomerID, status, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		return accountID == other.accountID
				&& Double.doubleToLongBits(balance) == Double.doubleToLongBits(other.balance)
				&& Objects.equals(label, other.label) && primaryCustomerID == other.primaryCustomerID
				&& secondaryCustomerID == other.secondaryCustomerID && Objects.equals(status, other.status)
				&& Objects.equals(type, other.type);
	}

	@Override
	public String toString() {
		return "Account [label=" + label + ", accountID=" + accountID + ", primaryCustomerID=" + primaryCustomerID
				+ ", secondaryCustomerID=" + secondaryCustomerID + ", balance=" + balance + ", type=" + type
				+ ", status=" + status + "]";
	}
	
}