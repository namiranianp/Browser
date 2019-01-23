package com.web.FileExplorer.dto;

public class User {

	private String name;
	private String password;

	/**
	 * Given a username and password, initializes the User object
	 * 
	 * @param username Desired username
	 * @param password Desired password
	 */
	public User(String username, String password) {
		name = username;
		this.password = password;
	}

	/**
	 * Changes the name of the current user
	 * 
	 * @param newName Desired new name
	 */
	public void setName(String newName) {
		name = newName;
	}

	/**
	 * 
	 * @return Username of the user
	 */
	public String getName() {
		return name;
	}

	/**
	 * Changes the password of the user
	 * 
	 * @param newPassword Desired new password
	 */
	public void setPassword(String newPassword) {
		password = newPassword;
	}

	/**
	 * 
	 * @return This user's password
	 */
	public String getPassword() {
		return password;
	}
}
