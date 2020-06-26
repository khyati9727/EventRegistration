package com.project.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.project.entity.BookingEntity;

public class Booking {

	private long bookingId;
	private Event event;
	private long phoneNo;
	private String email;
	private String gender;
	private String fullName;
	private LocalDate bookingDate;
	private Integer noOfPeople;
	private String bookingType;
	private List<PeopleAttending> detailsOfPeople=new ArrayList<PeopleAttending>();
	private String idProof;
	private String message;
	
	public BookingEntity booking(Booking booking) {
		BookingEntity bookingE=new BookingEntity();
		bookingE.setBookingId(booking.getBookingId());
		bookingE.setPhoneNo(booking.getPhoneNo());
		bookingE.setEmail(booking.getEmail());
		bookingE.setGender(booking.getGender());
		bookingE.setFullName(booking.getFullName());
		bookingE.setBookingDate(booking.getBookingDate());
		bookingE.setNoOfPeople(booking.getNoOfPeople());
		bookingE.setBookingType(booking.getBookingType());
		bookingE.setIdProof(booking.getIdProof());
		return bookingE;
	}
	
	
	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
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
	public List<PeopleAttending> getDetailsOfPeople() {
		return detailsOfPeople;
	}
	public void setDetailsOfPeople(List<PeopleAttending> detailsOfPeople) {
		this.detailsOfPeople = detailsOfPeople;
	}
	public long getBookingId() {
		return bookingId;
	}
	public void setBookingId(long bookingId) {
		this.bookingId = bookingId;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	
	public LocalDate getBookingDate() {
		return bookingDate;
	}


	public void setBookingDate(LocalDate bookingDate) {
		this.bookingDate = bookingDate;
	}


	public Integer getNoOfPeople() {
		return noOfPeople;
	}
	public void setNoOfPeople(Integer noOfPeople) {
		this.noOfPeople = noOfPeople;
	}
	public String getBookingType() {
		return bookingType;
	}
	public void setBookingType(String bookingType) {
		this.bookingType = bookingType;
	}
	public String getIdProof() {
		return idProof;
	}
	public void setIdProof(String idProof) {
		this.idProof = idProof;
	}
	
}
