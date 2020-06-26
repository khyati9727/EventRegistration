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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import com.project.model.Event;

@Entity
@Table(name = "event")
@GenericGenerator(name="eventPkGen",strategy="increment")
public class EventEntity {

	@Id
	@GeneratedValue(generator="eventPkGen")
	private Integer eventId;
	private String eventName;
	private String description;
	private LocalDate dateOfEvent;
	private Integer totalBookings;
	private Integer remainingBooking;
	
	@OneToMany(mappedBy = "event", cascade = CascadeType.MERGE)
	private List<BookingEntity> listOfBookings=new ArrayList<BookingEntity>();
	
	@ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "Event_People", 
             joinColumns = { @JoinColumn(name = "eventId") }, 
             inverseJoinColumns = { @JoinColumn(name = "phoneNo") })
	private List<PeopleAttendingEntity> peopleAttending=new ArrayList<PeopleAttendingEntity>();
	private String eventType;
	
	
	
	public Event event(EventEntity eventE) {
		Event event=new Event();
		event.setEventId(eventE.getEventId());
		event.setEventName(eventE.getEventName());
		event.setDescription(eventE.getDescription());
		event.setDateOfEvent(eventE.getDateOfEvent());
		event.setTotalBookings(eventE.getTotalBookings());
		event.setRemainingBooking(eventE.getRemainingBooking());
		event.setEventType(eventE.getEventType());
		return event;
	}
	
	
	
	@Override
	public String toString() {
		return "EventEntity [eventId=" + eventId + ", eventName=" + eventName + ", description=" + description
				+ ", dateOfEvent=" + dateOfEvent + ", totalBookings=" + totalBookings + ", remainingBooking="
				+ remainingBooking + ", listOfBookings=" + listOfBookings + ", peopleAttending=" + peopleAttending
				+ ", eventType=" + eventType + "]";
	}



	public List<PeopleAttendingEntity> getPeopleAttending() {
		return peopleAttending;
	}
	public void setPeopleAttending(List<PeopleAttendingEntity> peopleAttending) {
		this.peopleAttending = peopleAttending;
	}
	public List<BookingEntity> getListOfBookings() {
		return listOfBookings;
	}
	public void setListOfBookings(List<BookingEntity> listOfBookings) {
		this.listOfBookings = listOfBookings;
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
