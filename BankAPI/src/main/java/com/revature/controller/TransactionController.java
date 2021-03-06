package com.revature.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.revature.models.Account;
import com.revature.models.Transaction;
import com.revature.models.User;
import com.revature.services.AccountService;
import com.revature.services.TransactionService;
import io.javalin.http.Context;

public class TransactionController {
	private TransactionService transactionService = new TransactionService();
	private UserController userController = new UserController();
	private AccountService accountService = new AccountService();

	public void getAllTransactions(Context ctx) {
		List<Transaction> transactions = transactionService.selectAllTransactions();
		
		ctx.json(transactions);
	}

	public void viewTransfers(Context ctx) {
		if(userController.isValidCredentials(ctx)) {
			User me = userController.getUser(ctx);
			
			assert me != null;
			
			List<Account> myAccounts 
				= accountService.selectAllAccountsByCustomerID(me.getCustomerID());
			
			List<Transaction> transToMe = new ArrayList<Transaction>();
			for(Account a: myAccounts) {
				transToMe.addAll(transactionService
						.selectAllTransactionsToAccountID(a.getAccountID())
								);
			}
			
			List<Transaction> transFromMe = new ArrayList<Transaction>();
			for(Account a: myAccounts) {
				transFromMe.addAll(transactionService
						.selectAllTransactionsFromAccountID(a.getAccountID())
								);
			}
			
			//Remove duplicate Transactions.
			Set<Transaction> allMyTransactions = new HashSet<Transaction>();
			
			allMyTransactions.addAll(transToMe);
			allMyTransactions.addAll(transFromMe);
			
			ctx.json(allMyTransactions);
			
		} else {
			ctx.result("Failed Authentication. Please Login.");
		}
		
		return;
	}

	public void newTransaction(Context ctx) {
		int fromAccountId;
		int toAccountId;
		double amount;
		
		try {
			fromAccountId = Integer.parseInt(ctx.formParam("from_account_id"));
			toAccountId = Integer.parseInt(ctx.formParam("to_account_id"));
			amount = Double.parseDouble(ctx.formParam("amount"));
		} catch (Exception e) {
			ctx.result("Invalid form params.");
			return;
		}
		
		Date now = new Date(System.currentTimeMillis());
		
		
		Account fromAccount = accountService.selectAccountByID(fromAccountId);
		Account toAccount = accountService.selectAccountByID(toAccountId);
		if(fromAccount == null || toAccount == null) {
			ctx.result("One or more of the accounts listed in the transaction does not exist.");
			return;
		}
		
		//make sure that the FROM account is MY account
		if(fromAccount.getPrimaryCustomerID() != userController.getUser(ctx).getCustomerID()
			&& fromAccount.getSecondaryCustomerID() != userController.getUser(ctx).getCustomerID()) {
			ctx.result("You cannot transfer money from an account that you don't own!");
			return;
		}
		//make sure that the FROM account is an approved account
		if(!fromAccount.getStatus().equals("a")) {
			ctx.result("You cannot send money from account_id: " 
					+ fromAccountId + ". It is not an approved account.");
			return;
		}
		
		//make sure the transaction isn't negative!
		if(amount < 0) {
			ctx.result("You cannot send negative money!");
			return;
		}
		
		
		
		// we don't need to check that the TO account is approved, because it's possible that
		// someone might make a new account and set a starting balance by transferring funds.
		// hence there could exist a transfer to a non-approved account.
		
		// I can't know the transactionId before it exists, hence -1.
		// "p" is short for "pending"
		Transaction transaction = new Transaction( -1, fromAccountId, 
				toAccountId, now.toString(), amount, "p");
		
		boolean success = transactionService.insertTransaction(transaction);
		
		if(!success) {
			ctx.result("Failed to insert transaction");
			return;
		}
		ctx.result("Transaction Inserted successfully.");
		return;
	}

	public void approveOrDenyTransfer(Context ctx) {
		// get/validate user credentials
		User me = userController.getUser(ctx);
		if(!userController.isValidCredentials(ctx)) {
			ctx.result("Failed Authentication. Please Login.");
			return;
		}
		
		// get/validate user parameters: transaction_id and status
		int transactionId = -2;
		String status;
		
		try {
			transactionId = Integer.parseInt(ctx.formParam("transaction_id"));
			status = ctx.formParam("status");
			
			if(status == null) {
				throw new Exception("status form param not set.");
			}
		} catch (Exception e) {
			ctx.result("Invalid form params. Please input transaction_id and status");
			return;
		}
		
		Transaction transaction = transactionService.selectTransactionByID(transactionId);
		if(transaction == null) {
			ctx.result("Could not find transaction with ID: " + transactionId);
			return;
		}
		
		// Double check that the transaction to_account_id is one of our accounts
		Account toAccount = accountService.selectAccountByID(transaction.getToAccountID());
		if(toAccount.getPrimaryCustomerID() != me.getCustomerID()) {
			ctx.result("You cannot approve/deny transactions to accounts you do not own!");
			return;
		}
		
		// Update the status. (set status to Approved/Denied)
		// Make sure a valid status is set.
		try {
			transaction.setStatus(status);
		} catch (Exception e) {
			ctx.result("Could not set status. " + e.getMessage());
			return;
		}
		
		if(status.equals("a")) {
			//If we are approving a transaction, make sure that everything is in order.
			boolean validated = transactionService.validateTransaction(transaction);
			if(!validated) {
				ctx.result("This transaction cannot be approved becase it is not valid."
						+ " check if there is enough money in the from account,"
						+ " check that both accounts are approved.");
				return;
			}
		}
		
		int rowsUpdated = transactionService.updateTransaction(transaction);
		
		assert rowsUpdated <= 1;
		if(rowsUpdated != 1) {
			ctx.result("Could not update transaction.");
			return;
		}
		
		ctx.result("Transaction updated!");
		
		return;
	}
}
