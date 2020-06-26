package com.project.service;

import java.util.List;
import java.util.Map;

import com.project.model.Booking;
import com.project.model.Event;
import com.project.model.PeopleAttending;
import com.project.model.User;

public interface BookingService {
	
	public User getBookingHistory(long phoneNo) throws Exception;
	
	public Event getEventInfo(Integer eventId) throws Exception;
	
	public Booking bookEvent(Booking booking) throws Exception ;
	
	public boolean duplicateNumberCheck(PeopleAttending people) throws Exception;
	
	public  Map<String,List<Booking>> viewTypeViseBooking(Integer eventId) throws Exception;
	
	public  Booking viewBookingDetails(long bookingId) throws Exception;
	
	public List<Event> getLiveEvents() throws Exception;
	
	public Event addNewEvent(Event event) throws Exception;
	
	public void updateEvent(Event event) throws Exception;

}
