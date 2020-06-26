package com.project.model;

import com.project.entity.AdminEntity;

public class Admin {
	
	private long adminId;
	private String fullName;
	private long phoneNo;
	private String email;
	private String password;
	private String message;
	
	public AdminEntity admin(Admin admin) {
		AdminEntity adminE=new AdminEntity();
		adminE.setAdminId(admin.getAdminId());
		adminE.setEmail(admin.getEmail());
		adminE.setFullName(admin.getFullName());
		adminE.setPassword(admin.getPassword());
		adminE.setPhoneNo(admin.getPhoneNo());
		return adminE;
	}
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public long getAdminId() {
		return adminId;
	}
	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}
	public long getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(long phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	

}
