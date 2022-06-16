package com.revature.services;

import java.util.List;
import com.revature.models.Transaction;
import com.revature.dao.TransactionDao;
import com.revature.dao.TransactionDaoImpl;

public class TransactionService {
	
	private TransactionDao tDao = new TransactionDaoImpl();

	public Transaction selectTransactionByID(int id) {
		return tDao.selectTransactionByID(id);
	}
	
	public List<Transaction> selectAllTransactions(){
		return tDao.selectAllTransactions();
	}
	
	public List<Transaction> selectAllTransactionsFromAccountID(int id) {
		return tDao.selectAllTransactionsFromAccountID(id);
	}
	
	public List<Transaction> selectAllTransactionsToAccountID(int id) {
		return tDao.selectAllTransactionsToAccountID(id);
	}
	
	public List<Transaction> selectTransactionsByDate(String date) {
		return tDao.selectTransactionsByDate(date);
	}
	
	public List<Transaction> selectTransactionsByDate(String dateRangeBegin, String dateRangeEnd) {
		return tDao.selectTransactionsByDate(dateRangeBegin, dateRangeEnd);
	}
	
	public boolean insertTransaction(Transaction t) {
		return tDao.insertTransaction(t) > 0;
	}
	
	/**
	 * 
	 * 
	 * @param t
	 * @return the number of rows updated.
	 */
	public int updateTransaction(Transaction t) {
		return tDao.updateTransaction(t);
	}

	public boolean validateTransaction(Transaction transaction) {
		// TODO Auto-generated method stub
		return true;
	}

}
