package com.revature.models;

import java.util.Objects;

public class Transaction {
	private int transactionID;
	private int fromAccountID;
	private int toAccountID;
	private String date;
	private double amount;
	private String status;
	public Transaction(int transactionID, int fromAccountID, int toAccountID, String date, double amount,
			String status) {
		super();
		this.transactionID = transactionID;
		this.fromAccountID = fromAccountID;
		this.toAccountID = toAccountID;
		this.date = date;
		this.amount = amount;
		this.status = status;
	}
	public int getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(int transactionID) {
		this.transactionID = transactionID;
	}
	public int getFromAccountID() {
		return fromAccountID;
	}
	public void setFromAccountID(int fromAccountID) {
		this.fromAccountID = fromAccountID;
	}
	public int getToAccountID() {
		return toAccountID;
	}
	public void setToAccountID(int toAccountID) {
		this.toAccountID = toAccountID;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public int hashCode() {
		return Objects.hash(amount, date, fromAccountID, status, toAccountID, transactionID);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		return Double.doubleToLongBits(amount) == Double.doubleToLongBits(other.amount)
				&& Objects.equals(date, other.date) && fromAccountID == other.fromAccountID
				&& Objects.equals(status, other.status) && toAccountID == other.toAccountID
				&& transactionID == other.transactionID;
	}
	@Override
	public String toString() {
		return "Transaction [transactionID=" + transactionID + ", fromAccountID=" + fromAccountID + ", toAccountID="
				+ toAccountID + ", date=" + date + ", amount=" + amount + ", status=" + status + "]";
	}

}
