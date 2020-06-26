package com.project.model;

import com.project.entity.PeopleAttendingEntity;

public class PeopleAttending {

	private long phoneNo;
	private String fullName;
	private String gender;
	private String message;
	
	
	public PeopleAttendingEntity peopleAttending(PeopleAttending peopleAttending) {
		PeopleAttendingEntity peopleAttendingE=new PeopleAttendingEntity();
		peopleAttendingE.setFullName(peopleAttending.getFullName());
		peopleAttendingE.setPhoneNo(peopleAttending.getPhoneNo());
		peopleAttendingE.setGender(peopleAttending.getGender());
		return peopleAttendingE;
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
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
}
