package com.project.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.project.model.Admin;

@Entity
@Table(name ="admin")
@GenericGenerator(name="adminPkGen",strategy="increment")
public class AdminEntity {
	
	
	@Id
	@GeneratedValue(generator="adminPkGen")
	private long adminId;
	private String fullName;
	private long phoneNo;
	private String email;
	private String password;
	
	public Admin admin(AdminEntity adminE) {
		Admin admin=new Admin();
		admin.setAdminId(adminE.getAdminId());
		admin.setEmail(adminE.getEmail());
		admin.setFullName(adminE.getFullName());
		admin.setPassword(adminE.getPassword());
		admin.setPhoneNo(adminE.getPhoneNo());
		return admin;
	}
	
	@Override
	public String toString() {
		return "AdminEntity [adminId=" + adminId + ", fullName=" + fullName + ", phoneNo=" + phoneNo + ", email="
				+ email + ", password=" + password + "]";
	}
	
	public long getAdminId() {
		return adminId;
	}
	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}
	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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
