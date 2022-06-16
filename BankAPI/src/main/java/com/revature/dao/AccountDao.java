package com.revature.dao;

import com.revature.models.Account;

import java.sql.SQLException;
import java.util.List;

public interface AccountDao {
	
	public Account selectAccountByID(int id);
		
	public List<Account> selectAllAccounts();
	
	public List<Account> selectAllAccountsByCustomerID(int id);
	
	public List<Account> selectAllAccountsByType(String type);
	
	public List<Account> selectAllAccountsByStatus(String status);
	
	public int insertAccount(Account a);
	
	public void jointAccount(int primary, int secondary);
	
	public int secondaryAccount(Account a);
	
	public String accountCheck(int cusId); 
	
	public boolean withdrawById(int id, double amount) throws SQLException;
	
	public void depositById(int id, double amount);
	
	public int updateAccount(Account a);

}
