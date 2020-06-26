package com.project.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.project.model.Booking;


@Entity
@Table(name = "booking")
@GenericGenerator(name="bookingPkGen",strategy="increment")
public class BookingEntity {
	
	@Id 
	@GeneratedValue(generator="bookingPkGen")
	private long bookingId;
	private long phoneNo;
	private String email;
	private String fullName;
	private String gender;
	
	@ManyToOne(cascade = CascadeType.MERGE ,optional = false)
    @JoinColumn(name = "eventId")
	private EventEntity event;
		
	private LocalDate bookingDate;
	private Integer noOfPeople;
	
	@ManyToMany(cascade = CascadeType.MERGE )
    @JoinTable(name = "Booking_People", 
             joinColumns = { @JoinColumn(name = "bookingId") }, 
             inverseJoinColumns = { @JoinColumn(name = "phoneNo") })
	private List<PeopleAttendingEntity> detailsOfPeople=new ArrayList<PeopleAttendingEntity>();
	private String bookingType;    				 //self,group,corporate,other
	private String idProof;
	

	
	public Booking booking(BookingEntity bookingE) {
		Booking booking=new Booking();
		booking.setBookingId(bookingE.getBookingId());
		booking.setPhoneNo(bookingE.getPhoneNo());
		booking.setGender(bookingE.getGender());
		booking.setEmail(bookingE.getEmail());
		booking.setFullName(bookingE.getFullName());
		booking.setBookingDate(bookingE.getBookingDate());
		booking.setNoOfPeople(bookingE.getNoOfPeople());
		booking.setBookingType(bookingE.getBookingType());
		booking.setIdProof(bookingE.getIdProof());
		return booking;
	}
	
	
	
	@Override
	public String toString() {
		return "BookingEntity [bookingId=" + bookingId + ", phoneNo=" + phoneNo + ", email=" + email + ", fullName="
				+ fullName + ", gender=" + gender + ", event=" + event + ", bookingDate=" + bookingDate
				+ ", noOfPeople=" + noOfPeople + ", detailsOfPeople=" + detailsOfPeople + ", bookingType=" + bookingType
				+ ", idProof=" + idProof + "]";
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
	public List<PeopleAttendingEntity> getDetailsOfPeople() {
		return detailsOfPeople;
	}
	public void setDetailsOfPeople(List<PeopleAttendingEntity> detailsOfPeople) {
		this.detailsOfPeople = detailsOfPeople;
	}
	public long getBookingId() {
		return bookingId;
	}
	public void setBookingId(long bookingId) {
		this.bookingId = bookingId;
	}
	public EventEntity getEvent() {
		return event;
	}
	public void setEvent(EventEntity event) {
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
