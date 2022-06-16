package com.revature.services;

import java.util.List;
import com.revature.models.Account;
import com.revature.dao.AccountDao;
import com.revature.dao.AccountDaoImpl;

public class AccountService {
	private AccountDao aDao = new AccountDaoImpl();
	
	public Account selectAccountByID(int id) {
		return aDao.selectAccountByID(id);
	}
	
	public List<Account> selectAllAccounts() {
		return aDao.selectAllAccounts();
	}
	
	public List<Account> selectAllAccountsByCustomerID(int id) {
		return aDao.selectAllAccountsByCustomerID(id);
	}
	
	public List<Account> selectAllAccountsByType(String type) {
		return aDao.selectAllAccountsByType(type);
	}
	
	public List<Account> selectAllAccountsByStatus(String status) {
		return aDao.selectAllAccountsByStatus(status);
	}
	
	public boolean insertAccount(Account a) {
		return aDao.insertAccount(a) > 0;
	}
	
	public int updateAccount(Account a) {
		return aDao.updateAccount(a);
	}

}
