package com.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.project.dao.CredentialDAO;
import com.project.model.Admin;
import com.project.model.User;


@Service(value = "service")
@Transactional(readOnly = true)
public class CredentialServiceImpl implements CredentialService {

	@Autowired
	CredentialDAO dao;
	
	@Transactional(readOnly = false,propagation=Propagation.REQUIRES_NEW)
	public Admin adminLogin(Admin admin) throws Exception {
		
		Admin adminCheck=new Admin();
		adminCheck.setAdminId(admin.getAdminId());
		adminCheck=dao.adminLogin(adminCheck);
		if(adminCheck==null) {
			throw new Exception("NO SUCH ADMIN EXIST");
		}
		else if(!adminCheck.getPassword().equals(admin.getPassword())) {
			throw new Exception("INCORRECT PASSWORD");
		}
		else {
			adminCheck.setPassword(null);
			return adminCheck;
		}
	}
	
	@Transactional(readOnly = false,propagation=Propagation.REQUIRES_NEW)
	public User userLogin(User user) throws Exception {
		
		User userCheck=new User();
		userCheck.setPhoneNo(user.getPhoneNo());
		userCheck=dao.userLogin(userCheck);
		if(userCheck==null) {
			throw new Exception("NO SUCH USER EXIST");
		}
		else if(!userCheck.getPassword().equals(user.getPassword())) {
			throw new Exception("INCORRECT PASSWORD");
		}else {
			userCheck.setPassword(null);
			return userCheck;
		}
	}
	
	@Transactional(readOnly = false,propagation=Propagation.REQUIRES_NEW)
	public void userSignUp(User user) throws Exception{
		
		if(user.getPhoneNo()==0) {
			throw new Exception(" PHONE NUMBER IS MANDATORY ");
		}
		if(user.getEmail()==null) {
			throw new Exception(" EMAIL IS MANDATORY ");
		}
		User userReturned=dao.userSignUp(user);
		if(userReturned==null) {
			throw new Exception(" USER ACCOUNT ALREADY EXIST ");	
			}
		if(userReturned.getPhoneNo()==-1) {
			throw new Exception(" USER ACCOUNT CREATATOIN FAILED ");	
		}
		
	}
	
	@Transactional(readOnly = false,propagation=Propagation.REQUIRES_NEW)
	public void userChangePassword(User user) throws Exception{
		
		if(user.getMessage()==null) {
			throw new Exception(" NEW PASSWORD WAS NOT ENTERED ");
		}
		if(user.getPassword()==null) {
			throw new Exception(" OLD PASSWORD WAS NOT ENTERED ");
		}
		if(user.getMessage().equals(user.getPassword())) {
			throw new Exception(" NEW PASSWORD AND OLD PASSWORD ARE SAME ");
		}
		
		User userstored=dao.userPasswordChange(user);
		
		if(userstored==null) {
			throw new Exception(" NO SUCH USER EXIST ");
		}
		if(userstored.getPassword()==null) {
			throw new Exception(" INCORRECT OLD PASSWORD ");	
			}
		user.setPassword(null);
	}
	
	@Transactional(readOnly = false,propagation=Propagation.REQUIRES_NEW)
	public void adminChangePassword(Admin admin) throws Exception{
		
		if(admin.getMessage()==null) {
			throw new Exception(" NEW PASSWORD WAS NOT ENTERED ");
		}
		if(admin.getPassword()==null) {
			throw new Exception(" OLD PASSWORD WAS NOT ENTERED ");
		}
		if(admin.getMessage().equals(admin.getPassword())) {
			throw new Exception(" NEW PASSWORD AND OLD PASSWORD ARE SAME ");
		}
		
		Admin adminstored=dao.adminPasswordChange(admin);
		
		if(adminstored==null) {
			throw new Exception(" NO SUCH ADMIN EXIST ");
		}
		if(adminstored.getPassword()==null) {
			throw new Exception(" INCORRECT OLD PASSWORD ");	
			}
		admin.setPassword(null);
		
	}
	
	public User getUserDetails(long phoneNo) throws Exception  {
		
		User user=dao.getUserDetails(phoneNo);
		if(user==null) {
			throw new Exception(" NO SUCH USER EXIST ");
		}
		return user;
	}
	
	@Transactional(readOnly = false,propagation=Propagation.REQUIRES_NEW)
	public void addNewAdmin(Admin admin) throws Exception{
		
		if(admin.getPhoneNo()==0) {
			throw new Exception(" PHONE NUMBER IS MANDATORY ");
		}
		if(admin.getEmail()==null) {
			throw new Exception(" EMAIL IS MANDATORY ");
		}
		Admin adminReturned=dao.addNewAdmin(admin);
		if(adminReturned==null) {
			throw new Exception(" ACCOUNT CREATION FOR ADMIN FAILED ");
		}
	}
	
}
