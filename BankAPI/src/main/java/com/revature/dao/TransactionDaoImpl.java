package com.revature.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

import com.revature.models.Transaction;
import com.revature.util.ConnectionFactory;

public class TransactionDaoImpl implements TransactionDao {

	@Override
	public Transaction selectTransactionByID(int id) {
		String sql = "SELECT * FROM \"bankAPI\".transaction WHERE transaction_id=?";
		Connection connection = ConnectionFactory.getConnection();
		Transaction selectedTransaction = null;

		try(PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				selectedTransaction = new Transaction(rs.getInt("transaction_id"),
						rs.getInt("from_account_id"),
						rs.getInt("to_account_id"),
						rs.getDate("date").toString(),
						rs.getDouble("amount"),
						rs.getString("status"));
			}
		} catch(SQLException e) {
		e.printStackTrace();
		}
		return selectedTransaction;
	}

	@Override
	public List<Transaction> selectAllTransactions() {
		String sql = "SELECT * FROM \"bankAPI\".transaction ORDER BY transaction_id ASC";
		Connection connection = ConnectionFactory.getConnection();
		List<Transaction> selectedTransactions = new ArrayList<>();

		try(PreparedStatement ps = connection.prepareStatement(sql)){

			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				selectedTransactions.add(new Transaction(rs.getInt("transaction_id"),
						rs.getInt("from_account_id"),
						rs.getInt("to_account_id"),
						rs.getDate("date").toString(),
						rs.getDouble("amount"),
						rs.getString("status")));
			}
		} catch(SQLException e) {
		e.printStackTrace();
		}
		return selectedTransactions;
	}

	@Override
	public List<Transaction> selectAllTransactionsFromAccountID(int id) {
		String sql = "SELECT * FROM \"bankAPI\".transaction WHERE from_account_id=? ORDER BY transaction_id ASC";
		Connection connection = ConnectionFactory.getConnection();
		List<Transaction> selectedTransactions = new ArrayList<>();

		try(PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				selectedTransactions.add(new Transaction(rs.getInt("transaction_id"),
						rs.getInt("from_account_id"),
						rs.getInt("to_account_id"),
						rs.getDate("date").toString(),
						rs.getDouble("amount"),
						rs.getString("status")));
			}
		} catch(SQLException e) {
		e.printStackTrace();
		}
		return selectedTransactions;
	}

	@Override
	public List<Transaction> selectAllTransactionsToAccountID(int id) {
		String sql = "SELECT * FROM \"bankAPI\".transaction WHERE to_account_id=? ORDER BY transaction_id ASC";
		Connection connection = ConnectionFactory.getConnection();
		List<Transaction> selectedTransactions = new ArrayList<>();

		try(PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				selectedTransactions.add(new Transaction(rs.getInt("transaction_id"),
						rs.getInt("from_account_id"),
						rs.getInt("to_account_id"),
						rs.getDate("date").toString(),
						rs.getDouble("amount"),
						rs.getString("status")));
			}
		} catch(SQLException e) {
		e.printStackTrace();
		}
		return selectedTransactions;
	}

	@Override
	public List<Transaction> selectTransactionsByDate(String date) {
		String sql = "SELECT * FROM \"bankAPI\".transaction WHERE date=? ORDER BY transaction_id ASC";
		Connection connection = ConnectionFactory.getConnection();
		List<Transaction> selectedTransactions = new ArrayList<>();

		try(PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setDate(1, new Date(new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime()));

			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				selectedTransactions.add(new Transaction(rs.getInt("transaction_id"),
						rs.getInt("from_account_id"),
						rs.getInt("to_account_id"),
						rs.getDate("date").toString(),
						rs.getDouble("amount"),
						rs.getString("status")));
			}
		} catch(SQLException | java.text.ParseException e) {
		e.printStackTrace();
		}
		return selectedTransactions;
	}

	@Override
	public List<Transaction> selectTransactionsByDate(String dateRangeBegin, String dateRangeEnd) {
		String sql = "SELECT * FROM \"bankAPI\".transaction WHERE date>=? AND date<=? ORDER BY transaction_id ASC";
		Connection connection = ConnectionFactory.getConnection();
		List<Transaction> selectedTransactions = new ArrayList<>();

		try(PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setDate(1, new Date(new SimpleDateFormat("yyyy-MM-dd").parse(dateRangeBegin).getTime()));
			ps.setDate(2, new Date(new SimpleDateFormat("yyyy-MM-dd").parse(dateRangeEnd).getTime()));


			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				selectedTransactions.add(new Transaction(rs.getInt("transaction_id"),
						rs.getInt("from_account_id"),
						rs.getInt("to_account_id"),
						rs.getDate("date").toString(),
						rs.getDouble("amount"),
						rs.getString("status")));
			}
		} catch(SQLException | java.text.ParseException e) {
		e.printStackTrace();
		}
		return selectedTransactions;
	}

	@Override
	public int insertTransaction(Transaction t) {
		String sql = "INSERT INTO \"bankAPI\".transaction (transaction_id, from_account_id, to_account_id, date, amount, status) VALUES (DEFAULT, ?, ?, ?, ?, ?) RETURNING transaction_id";
		Connection connection = ConnectionFactory.getConnection();
		int transactionID = -1;
		Date now = new Date(System.currentTimeMillis());
        try (PreparedStatement ps = connection.prepareStatement(sql)){
			
        	ps.setInt(1, t.getFromAccountID());
			ps.setInt(2, t.getToAccountID());
			ps.setDate(3, now);
			ps.setDouble(4, t.getAmount());
			ps.setString(5, t.getStatus());
			ResultSet rs = ps.executeQuery();
			rs.next();
			transactionID = rs.getInt("transaction_id");
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return transactionID;
	}

	@Override
	public int updateTransaction(Transaction t) {
		String sql = "UPDATE \"bankAPI\".transaction "
				+ " SET from_account_id = ?, to_account_id = ?, date = ?, amount = ?, status = ? "
				+ " WHERE transaction_id = ?";
		Connection connection = ConnectionFactory.getConnection();
		int rowsUpdated = -1;
		Date now = new Date(System.currentTimeMillis());
        try (PreparedStatement ps = connection.prepareStatement(sql)){
			
        	ps.setInt(1, t.getFromAccountID());
			ps.setInt(2, t.getToAccountID());
			ps.setDate(3, now);
			ps.setDouble(4, t.getAmount());
			ps.setString(5, t.getStatus());
        	ps.setInt(6, t.getTransactionID());
        	rowsUpdated = ps.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return rowsUpdated;
	}

}
