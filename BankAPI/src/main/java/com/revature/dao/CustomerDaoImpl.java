package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import com.revature.models.Customer;
import com.revature.util.ConnectionFactory;

public class CustomerDaoImpl implements CustomerDao {

	@Override
	public Customer selectCustomerByID(int id) {
		String sql = "SELECT * FROM \"BankAPI\".\"customer\" WHERE customer_id=?";
		Connection connection = ConnectionFactory.getConnection();
		Customer selectedCustomer = null;
		try (PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				selectedCustomer = new Customer(rs.getInt("customer_id"),
						rs.getString("first_name"),
						rs.getString("last_name"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return selectedCustomer;
	}

	@Override
	public List<Customer> selectAllCustomers() {
		String sql = "SELECT * FROM \"BankAPI\".\"customer\" ORDER BY customer_id ASC";
		Connection connection = ConnectionFactory.getConnection();
		List<Customer> selectedCustomers = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(sql)){
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				selectedCustomers.add(new Customer(rs.getInt("customer_id"),
						rs.getString("first_name"),
						rs.getString("last_name")));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return selectedCustomers;
	}
	
	@Override
	public int insertCustomer(Customer c) {
		String sql = "INSERT INTO \"BankAPI\".\"customer\" (customer_id, first_name, last_name) VALUES (DEFAULT, ?, ?) RETURNING customer_id";
		Connection connection = ConnectionFactory.getConnection();
		int customerID = -1;
        try (PreparedStatement ps = connection.prepareStatement(sql)){
			
        	ps.setString(1, c.getFirstName());
			ps.setString(2, c.getLastName());
			ResultSet rs = ps.executeQuery();
			rs.next();
			customerID = rs.getInt("customer_id");
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return customerID;
	}

	@Override
	public boolean updateCustomer(Customer c) {
		String sql = "UPDATE \"BankAPI\".\"customer\" (first_name, last_name) = (?, ?) WHERE customer_id=?";
		Connection connection = ConnectionFactory.getConnection();
		boolean executed = false;
        try (PreparedStatement ps = connection.prepareStatement(sql)){
			
        	ps.setString(1, c.getFirstName());
			ps.setString(2, c.getLastName());
			ps.setInt(3, c.getCustomerID());
			executed = ps.execute();
			
        }catch(SQLException e) {
			e.printStackTrace();
		}
        return executed;
	}

}
