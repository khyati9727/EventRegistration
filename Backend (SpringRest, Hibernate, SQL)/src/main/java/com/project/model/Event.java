package com.project.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.project.entity.EventEntity;



public class Event {
	
	private Integer eventId;
	private String eventName;
	private String description;
	private LocalDate dateOfEvent;
	private Integer totalBookings;
	private Integer remainingBooking;
	private String eventType;
	private List<Booking> listOfBookings=new ArrayList<Booking>();
	private List<PeopleAttending> peopleAttending=new ArrayList<PeopleAttending>();
	private String message;
	
	
	public EventEntity event(Event event) {
		EventEntity eventE=new EventEntity();
		eventE.setEventId(event.getEventId());
		eventE.setEventName(event.getEventName());
		eventE.setDescription(event.getDescription());
		eventE.setDateOfEvent(event.getDateOfEvent());
		eventE.setTotalBookings(event.getTotalBookings());
		eventE.setRemainingBooking(event.getRemainingBooking());
		eventE.setEventType(event.getEventType());
		return eventE;
	}
	
	
	public List<Booking> getListOfBookings() {
		return listOfBookings;
	}


	public void setListOfBookings(List<Booking> listOfBookings) {
		this.listOfBookings = listOfBookings;
	}


	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public List<PeopleAttending> getPeopleAttending() {
		return peopleAttending;
	}
	public void setPeopleAttending(List<PeopleAttending> peopleAttending) {
		this.peopleAttending = peopleAttending;
	}
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public LocalDate getDateOfEvent() {
		return dateOfEvent;
	}


	public void setDateOfEvent(LocalDate dateOfEvent) {
		this.dateOfEvent = dateOfEvent;
	}


	public Integer getTotalBookings() {
		return totalBookings;
	}
	public void setTotalBookings(Integer totalBookings) {
		this.totalBookings = totalBookings;
	}
	public Integer getRemainingBooking() {
		return remainingBooking;
	}
	public void setRemainingBooking(Integer remainingBooking) {
		this.remainingBooking = remainingBooking;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	
	
}
