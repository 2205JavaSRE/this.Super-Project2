package com.revature.services;

import com.revature.dao.AccountDao;
import com.revature.dao.AccountDaoImpl;
import com.revature.dao.CustomerDao;
import com.revature.dao.CustomerDaoImpl;
import com.revature.dao.TransactionDao;
import com.revature.dao.TransactionDaoImpl;
import com.revature.dao.UserDao;
import com.revature.dao.UserDaoImpl;
import com.revature.models.Account;
import com.revature.models.Customer;
import com.revature.models.User;

public class BankService {
	
	private static AccountDao aDao = new AccountDaoImpl();
	private static CustomerDao cDao = new CustomerDaoImpl();
	private static TransactionDao tDao = new TransactionDaoImpl();
	private static UserDao	uDao = new UserDaoImpl();
	

	public void registerBankAccount(String cusName, String cusLastName, String userName, String password, String type, String status, String label ){
		
		Customer newCustomer = new Customer (-1, cusName, cusLastName);
		
		int cusId = cDao.insertCustomer(newCustomer);
		
		User newUser = new User( -1, cusId, userName, password, false);
		
		int userId = uDao.insertUser(newUser);
		status = "p";
		Account newAccount = new Account(-1, cusId, cusId, 0, type, status, label);
		
		int accountId  = aDao.insertAccount(newAccount);
		
	}
	
	public void jointAccount(int primary, int secondary) {
		aDao.jointAccount(primary, secondary);
		
	}
	
	
	
	
	
	
}
