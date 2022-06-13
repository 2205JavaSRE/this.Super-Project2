package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import com.revature.models.User;
import com.revature.util.ConnectionFactory;

public class UserDaoImpl implements UserDao {

	@Override
	public User selectUserByID(int id) {
		String sql = "SELECT * FROM \"BankAPI\".\"user\" WHERE user_id=?";
		Connection connection = ConnectionFactory.getConnection();
		User selectedUser = null;
		try (PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				selectedUser = new User(rs.getInt("user_id"),
						rs.getInt("customer_id"),
						rs.getString("username"),
						rs.getString("password"),
						rs.getBoolean("is_manager"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return selectedUser;
	}

	@Override
	public User selectUserByUsername(String username) {
		String sql = "SELECT * FROM \"BankAPI\".\"user\" WHERE username=?";
		Connection connection = ConnectionFactory.getConnection();
		User selectedUser = null;
		try (PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setString(1, username);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				selectedUser = new User(rs.getInt("user_id"),
						rs.getInt("customer_id"),
						rs.getString("username"),
						rs.getString("password"),
						rs.getBoolean("is_manager"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return selectedUser;
	}

	@Override
	public List<User> selectAllUsers() {
		String sql = "SELECT * FROM \"BankAPI\".\"user\" ORDER BY user_id ASC";
		Connection connection = ConnectionFactory.getConnection();
		List<User> selectedUsers = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(sql)){
		
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				selectedUsers.add(new User(rs.getInt("user_id"),
						rs.getInt("customer_id"),
						rs.getString("username"),
						rs.getString("password"),
						rs.getBoolean("is_manager")));
			}

		}catch(SQLException e){
			e.printStackTrace();
			selectedUsers = null;
		}
		
		return selectedUsers;
	}


	@Override
	public int insertUser(User u) {
		String sql = "INSERT INTO \"BankAPI\".\"user\" (user_id, customer_id, username, password, is_manager) VALUES (DEFAULT, ?, ?, ?, ?) RETURNING user_id";
		Connection connection = ConnectionFactory.getConnection();
		int userID = -1;
        try (PreparedStatement ps = connection.prepareStatement(sql)){
			
			ps.setInt(1, u.getCustomerID());
			ps.setString(2, u.getUsername());
			ps.setString(3, u.getPassword());
			ps.setBoolean(4, u.isManager());
			ResultSet rs = ps.executeQuery();
			rs.next();
			userID = rs.getInt("user_id");
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return userID;
	}

	@Override
	public boolean updateUser(User u) {
		String sql = "UPDATE \"BankAPI\".\"user\" (customer_id, username, password, is_manager) = (?, ?, ?, ?) WHERE user_id=?";
		Connection connection = ConnectionFactory.getConnection();
		boolean executed = false;
        try (PreparedStatement ps = connection.prepareStatement(sql)){
			
			ps.setInt(1, u.getCustomerID());
			ps.setString(2, u.getUsername());
			ps.setString(3, u.getPassword());
			ps.setBoolean(4, u.isManager());
        	ps.setInt(5, u.getUserID());
			executed = ps.execute();
			
        }catch(SQLException e) {
			e.printStackTrace();
		}
        return executed;
	}

}
