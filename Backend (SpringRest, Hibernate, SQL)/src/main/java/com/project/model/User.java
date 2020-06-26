package com.project.model;


import java.util.ArrayList;
import java.util.List;

import com.project.entity.UserEntity;

public class User {
	
	private long phoneNo;
	private String fullName;
	private String password;
	private String email;
	private String gender;
	private List<Booking> booking=new ArrayList<Booking>();
	private String message;
	

	public UserEntity user(User user) {
		UserEntity userE=new UserEntity();
		userE.setEmail(user.getEmail());
		userE.setPhoneNo(user.getPhoneNo());
		userE.setFullName(user.getFullName());
		userE.setPassword(user.getPassword());
		userE.setGender(user.getGender());
		return userE;
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
	public long getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(long phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public List<Booking> getBooking() {
		return booking;
	}
	public void setBooking(List<Booking> booking) {
		this.booking = booking;
	}
	
	
	
}
