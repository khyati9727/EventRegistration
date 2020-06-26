package com.project.dao;

import com.project.model.Admin;
import com.project.model.User;

public interface CredentialDAO {

	public Admin adminLogin(Admin admin) ; 
	
	public User userLogin(User user);  
	
	public User userSignUp(User user) throws Exception  ;
		
	public User userPasswordChange(User user);
	
	public Admin adminPasswordChange(Admin admin) ;
	
	public User getUserDetails(long phoneNo) ;
	
	public Admin addNewAdmin(Admin admin) throws Exception;
		
	}
