package com.revature.dao;

import com.revature.models.Transaction;
import java.util.List;

public interface TransactionDao {

	public Transaction selectTransactionByID(int id);
	
	public List<Transaction> selectAllTransactions();
	
	public List<Transaction> selectAllTransactionsFromAccountID(int id);
	
	public List<Transaction> selectAllTransactionsToAccountID(int id);
	
	public List<Transaction> selectTransactionsByDate(String date);
	
	public List<Transaction> selectTransactionsByDate(String dateRangeBegin, String dateRangeEnd);
	
	public int insertTransaction(Transaction t);
	
	public int updateTransaction(Transaction t);

}
