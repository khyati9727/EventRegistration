package com.project.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.project.model.PeopleAttending;

@Entity
@Table(name = "people")
public class PeopleAttendingEntity {

	@Id
	private long phoneNo;
	private String fullName;
	private String gender;
	
	
	
	public PeopleAttending peopleAttending(PeopleAttendingEntity peopleAttendingE) {
		PeopleAttending peopleAttending=new PeopleAttending();
		peopleAttending.setFullName(peopleAttendingE.getFullName());
		peopleAttending.setPhoneNo(peopleAttendingE.getPhoneNo());
		peopleAttending.setGender(peopleAttendingE.getGender());
		return peopleAttending;
	}
	
	
	
	@Override
	public String toString() {
		return "PeopleAttendingEntity [phoneNo=" + phoneNo + ", fullName=" + fullName + ", gender=" + gender + "]";
	}



	public long getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(long phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	
}
