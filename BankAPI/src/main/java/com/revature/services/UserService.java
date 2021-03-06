package com.revature.services;

import java.util.List;
import com.revature.models.User;
import com.revature.dao.UserDao;
import com.revature.dao.UserDaoImpl;

public class UserService {
	
	private UserDao uDao = new UserDaoImpl();
	
	//Login
	public boolean login(String username, String password) {
		User user = getUserByUsername(username);
		if(user == null) {
			return false;
		}
		return getUserByUsername(username).getPassword().equals(password);
	}
	
	//User services
	public User getUserByID(int id) {
		return uDao.selectUserByID(id);
	}

	public User getUserByUsername(String username) {
		return uDao.selectUserByUsername(username);
	}

	public List<User> getAllUsers() {
		return uDao.selectAllUsers();
	}

	public boolean createUser(User jsonUser) {
		return uDao.insertUser(jsonUser) > 0;
	}
	
	public boolean updateUser(User jsonUser) {
		return uDao.updateUser(jsonUser);
	}
}
