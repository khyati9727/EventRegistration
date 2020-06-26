package com.project.service;

import com.project.model.Admin;
import com.project.model.User;

public interface CredentialService {

	public Admin adminLogin(Admin admin) throws Exception;
	
	public User userLogin(User user) throws Exception;
	
	public void userSignUp(User user) throws Exception;
	
	public void userChangePassword(User user) throws Exception;
	
	public void adminChangePassword(Admin admin) throws Exception;
	
	public User getUserDetails(long phoneNo) throws Exception ;

	public void addNewAdmin(Admin admin) throws Exception;
	
}
