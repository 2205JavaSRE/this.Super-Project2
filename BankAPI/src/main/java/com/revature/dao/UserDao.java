package com.revature.dao;

import com.revature.models.User;
import java.util.List;

public interface UserDao {

	public User selectUserByID(int id);
	
	public User selectUserByUsername(String username);
	
	public List<User> selectAllUsers();
	
	public int insertUser(User u);
	
	public boolean updateUser(User u);
	
}
