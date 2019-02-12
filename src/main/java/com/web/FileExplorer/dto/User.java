package com.web.FileExplorer.dto;

import java.util.ArrayList;

public class User {

	private String name;
	private String password;
	private final ArrayList<String> roles;

	/* These are additional security variables */
	private boolean encryptSave;
	private boolean postLinks;
	private boolean encryptLinks;

	/**
	 * Given a username and password, initializes the User object
	 * 
	 * @param username Desired username
	 * @param password Desired password
	 */
	public User(String username, String password, String userRoles) {
		name = username;
		this.password = password;
		roles = new ArrayList<String>();
		if (userRoles != null) {
			roles.add(userRoles);
		}
	}

	/**
	 * Sets encryptSave equal to save
	 * 
	 * @param save
	 */
	public void setEncryptSave(boolean save) {
		encryptSave = save;
	}

	/**
	 * @return value of encryptSave
	 */
	public boolean getEncryptSave() {
		return encryptSave;
	}

	/**
	 * Sets the value of postLinks equal to post
	 * 
	 * @param post
	 */
	public void setPostLinks(boolean post) {
		postLinks = post;
	}

	/**
	 * @return value of postLinks
	 */
	public boolean getPostLinks() {
		return postLinks;
	}

	/**
	 * Sets encryptLinks equal to save
	 * 
	 * @param encrypt
	 */
	public void setEncryptLinks(boolean encrypt) {
		encryptLinks = encrypt;
	}

	/**
	 * @return value of encryptLinks
	 */
	public boolean getEncryptLinks() {
		return encryptLinks;
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

	/**
	 * Adds the given role to the user
	 * 
	 * @param role A single string that is the desired role
	 */
	public void addRole(String role) {
		if (role != null) {
			roles.add(role);
		}
	}

	/**
	 * 
	 * @return An ArrayList containing all of the roles for this user
	 */
	public String[] getRoles() {
		return (String[]) roles.toArray();
	}
}
