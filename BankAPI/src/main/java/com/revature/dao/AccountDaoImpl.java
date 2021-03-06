package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import com.revature.models.Account;
import com.revature.util.ConnectionFactory;

public class AccountDaoImpl implements AccountDao {

	@Override
	public Account selectAccountByID(int id) {
		String sql = "SELECT * FROM \"bankAPI\".\"account\" WHERE account_id=?";
		Connection connection = ConnectionFactory.getConnection();
		Account selectedAccount = null;
		try (PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				selectedAccount = new Account(rs.getInt("account_id"),
						rs.getInt("primary_customer_id"),
						rs.getInt("secondary_customer_id"),
						rs.getDouble("balance"),
						rs.getString("type"),
						rs.getString("status"),
						rs.getString("label"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return selectedAccount;
	}

	@Override
	public List<Account> selectAllAccounts() {
		String sql = "SELECT * FROM \"bankAPI\".\"account\" ORDER BY account_id ASC";
		Connection connection = ConnectionFactory.getConnection();
		List<Account> selectedAccounts = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(sql)){
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				selectedAccounts.add(new Account(rs.getInt("account_id"),
						rs.getInt("primary_customer_id"),
						rs.getInt("secondary_customer_id"),
						rs.getDouble("balance"),
						rs.getString("type"),
						rs.getString("status"),
						rs.getString("label")));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return selectedAccounts;
	}

	@Override
	public List<Account> selectAllAccountsByCustomerID(int id) {
		String sql = "SELECT * FROM \"bankAPI\".\"account\" WHERE primary_customer_id=? OR secondary_customer_id=? ORDER BY account_id ASC";
		Connection connection = ConnectionFactory.getConnection();
		List<Account> selectedAccounts = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setInt(1, id);
			ps.setInt(2, id);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				selectedAccounts.add(new Account(rs.getInt("account_id"),
						rs.getInt("primary_customer_id"),
						rs.getInt("secondary_customer_id"),
						rs.getDouble("balance"),
						rs.getString("type"),
						rs.getString("status"),
						rs.getString("label")));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return selectedAccounts;
	}

	@Override
	public List<Account> selectAllAccountsByType(String type) {
		String sql = "SELECT * FROM \"bankAPI\".\"account\" WHERE type=? ORDER BY account_id ASC";
		Connection connection = ConnectionFactory.getConnection();
		List<Account> selectedAccounts = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setString(1, type);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				selectedAccounts.add(new Account(rs.getInt("account_id"),
						rs.getInt("primary_customer_id"),
						rs.getInt("secondary_customer_id"),
						rs.getDouble("balance"),
						rs.getString("type"),
						rs.getString("status"),
						rs.getString("label")));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return selectedAccounts;
	}

	@Override
	public List<Account> selectAllAccountsByStatus(String status) {
		String sql = "SELECT * FROM \"bankAPI\".\"account\" WHERE status=? ORDER BY account_id ASC";
		Connection connection = ConnectionFactory.getConnection();
		List<Account> selectedAccounts = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setString(1, status);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				selectedAccounts.add(new Account(rs.getInt("account_id"),
						rs.getInt("primary_customer_id"),
						rs.getInt("secondary_customer_id"),
						rs.getDouble("balance"),
						rs.getString("type"),
						rs.getString("status"),
						rs.getString("label")));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return selectedAccounts;
	}

	@Override
	public int insertAccount(Account a) {
		String sql = "INSERT INTO \"bankAPI\".\"account\"  (primary_customer_id, secondary_customer_id, balance, type, status, label) VALUES (?, ?, ?, ?, ?, ?) RETURNING account_id";
		Connection connection = ConnectionFactory.getConnection();
		int accountID = -1; 
        try (PreparedStatement ps = connection.prepareStatement(sql)){
			
        	ps.setInt(1, a.getPrimaryCustomerID());
			ps.setInt(2, a.getSecondaryCustomerID());
			ps.setDouble(3, a.getBalance());
			ps.setString(4, a.getType());
			ps.setString(5, a.getStatus());
			ps.setString(6, a.getLabel());
			ResultSet rs = ps.executeQuery();
			rs.next();
			accountID = rs.getInt("account_id");
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return accountID;
	}

	@Override
	public int updateAccount(Account a) {
		String sql = "UPDATE \"bankAPI\".account "
				+ " SET primary_customer_id = ?, secondary_customer_id = ?,"
				+ " balance = ?, type = ?, status = ?, label = ?"
				+ " WHERE account_id=?";
		Connection connection = ConnectionFactory.getConnection();
		int numUpdated = -1;
        try (PreparedStatement ps = connection.prepareStatement(sql)){
			
        	ps.setInt(1, a.getPrimaryCustomerID());
			ps.setInt(2, a.getSecondaryCustomerID());
			ps.setDouble(3, a.getBalance());
			ps.setString(4, a.getType());
			ps.setString(5, a.getStatus());
			ps.setString(6, a.getLabel());
        	ps.setInt(7, a.getAccountID());
			numUpdated = ps.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return numUpdated;
	}
	public void jointAccount(int primary, int secondary) {
		String sql = "update \"bankAPI\".account set secondary_customer_id = ? where primary_customer_id = ?;";
		Connection connection = ConnectionFactory.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sql)){
			
        	ps.setInt(1, secondary);
			ps.setInt(2, primary);
			ps.execute();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public int secondaryAccount(Account a) {
		String sql = "INSERT INTO \"bankAPI\".\"account\"  (primary_customer_id, secondary_customer_id, balance, type, status, label) VALUES (?, ?, ?, ?, ?, ?) RETURNING account_id";
		Connection connection = ConnectionFactory.getConnection();
		int accountID = -1; 
        try (PreparedStatement ps = connection.prepareStatement(sql)){
			
        	ps.setInt(1, a.getPrimaryCustomerID());
			ps.setInt(2, a.getSecondaryCustomerID());
			ps.setDouble(3, a.getBalance());
			ps.setString(4, a.getType());
			ps.setString(5, a.getStatus());
			ps.setString(6, a.getLabel());
			ResultSet rs = ps.executeQuery();
			rs.next();
			accountID = rs.getInt("account_id");
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return accountID;
	}
	
	public String accountCheck(int cusId) {
		String sql = "SELECT status FROM \"bankAPI\".account WHERE primary_customer_id =? ;";
		Connection connection = ConnectionFactory.getConnection();
		String status = "p";
        try (PreparedStatement ps = connection.prepareStatement(sql)){
			
        	ps.setInt(1, cusId);
        	ResultSet rs = ps.executeQuery();
			rs.next();
			status = rs.getString("status");
		}catch(SQLException e) {
			e.printStackTrace();
		}		
		return status; 
	}
	public boolean withdrawById(int id, double amount) throws SQLException {
		boolean statuss = false;
		String select = "SELECT balance FROM \"bankAPI\".account WHERE account_id =? ;";
		String update = "UPDATE \"bankAPI\".account SET balance = (balance - ?) WHERE account_id =? ;";
		String insert = "INSERT INTO \"bankAPI\".\"transaction\" (from_account_id, to_account_id, \"date\",amount) VALUES ( ?, ?, now(),(-?));";
		Connection connection = ConnectionFactory.getConnection();
		
        	PreparedStatement ps = connection.prepareStatement(select);
        	PreparedStatement ps2 = connection.prepareStatement(update);
        	PreparedStatement ps3 = connection.prepareStatement(insert);
        	//get balance
        	ps.setInt(1, id);
        	ResultSet rs = ps.executeQuery();
			rs.next();
			double balance = rs.getDouble("balance");
			if((balance-amount)>= 0) {
				//update account
	        	ps2.setDouble(1, amount);
	        	ps2.setInt(2, id);
	        	ps2.execute();
	        	//insert transaction
	        	ps3.setInt(1, id);
	        	ps3.setInt(2, id);
	        	ps3.setDouble(3, amount);
	        	ps3.execute();
	        	statuss= true;
				
			}else{
				statuss =false;
			}

			return statuss ;

	}
	public void depositById(int id, double amount){
		String update = "UPDATE \"bankAPI\".account SET balance = (balance + ?) WHERE account_id =? ;";
		String insert = "INSERT INTO \"bankAPI\".\"transaction\" (from_account_id, to_account_id, \"date\",amount) VALUES ( ?, ?, now(),(+?));";
		Connection connection = ConnectionFactory.getConnection();
			try {
	        	PreparedStatement ps2 = connection.prepareStatement(update);
	        	PreparedStatement ps3 = connection.prepareStatement(insert);
	        	//update account
	        	ps2.setDouble(1, amount);
	        	ps2.setInt(2, id);
	        	ps2.execute();
	        	//insert transaction
	        	ps3.setInt(1, id);
	        	ps3.setInt(2, id);
	        	ps3.setDouble(3, amount);
	        	ps3.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


	}
	

}
