package com.project.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.project.model.User;

@Entity
@Table(name="user")
public class UserEntity {
	
	@Id
	private long  phoneNo;
	private String fullName;
	private String password;
	private String email;
	private String gender;
	
	@OneToMany(cascade=CascadeType.MERGE)
    @JoinTable(name="User_Booking",
    joinColumns=@JoinColumn(name="phoneNo", referencedColumnName="phoneNo"),
    inverseJoinColumns=@JoinColumn(name="bookingId", referencedColumnName="bookingId",unique=true))
	private List<BookingEntity> bookings=new ArrayList<BookingEntity>();
	
	
	public User user(UserEntity userE) {
		User user=new User();
		user.setEmail(userE.getEmail());
		user.setPhoneNo(userE.getPhoneNo());
		user.setFullName(userE.getFullName());
		user.setPassword(userE.getPassword());
		user.setGender(userE.getGender());
		return user;
	}
	
	@Override
	public String toString() {
		return "UserEntity [phoneNo=" + phoneNo + ", fullName=" + fullName + ", password=" + password + ", email="
				+ email + ", gender=" + gender + ", bookings=" + bookings + "]";
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
	public List<BookingEntity> getBookings() {
		return bookings;
	}
	public void setBookings(List<BookingEntity> bookings) {
		this.bookings = bookings;
	}
}
