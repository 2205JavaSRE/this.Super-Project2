package com.revature.controller;

import java.util.List;
import com.revature.models.Transaction;
import com.revature.services.TransactionService;
import io.javalin.http.Context;

public class TransactionController {
	private TransactionService transactionService = new TransactionService();

	public void getAllTransactions(Context ctx) {
		List<Transaction> transactions = transactionService.selectAllTransactions();
		
		ctx.json(transactions);
	}
}
