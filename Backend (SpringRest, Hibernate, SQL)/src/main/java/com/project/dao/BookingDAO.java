package com.project.dao;

import java.util.List;
import java.util.Map;

import com.project.model.Booking;
import com.project.model.Event;
import com.project.model.PeopleAttending;
import com.project.model.User;


public interface BookingDAO {
	
	public User getBookingHistory(long phoneNo);
	
	public Event getEventInfo(Integer eventId);
		
	public	Booking bookEvent(Booking booking) throws Exception; 	
	
	public PeopleAttending duplicateNumberCheck(PeopleAttending people);
	
	public Map<String,List<Booking>> viewTypeViseBooking(Integer eventId);
	
	public  Booking viewBookingDetails(long bookingId);		
	
	public List<Event> getLiveEvents();
    	
	public Event addNewEvent(Event event);
	
	public Event updateEvent(Event event);
	
}
