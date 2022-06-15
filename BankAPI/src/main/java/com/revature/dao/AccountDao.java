package com.revature.dao;

import com.revature.models.Account;
import java.util.List;

public interface AccountDao {
	
	public Account selectAccountByID(int id);
		
	public List<Account> selectAllAccounts();
	
	public List<Account> selectAllAccountsByCustomerID(int id);
	
	public List<Account> selectAllAccountsByType(String type);
	
	public List<Account> selectAllAccountsByStatus(String status);
	
	public int insertAccount(Account a);
	
	public boolean updateAccount(Account a);
	
	public void jointAccount(int primary, int secondary);

}
