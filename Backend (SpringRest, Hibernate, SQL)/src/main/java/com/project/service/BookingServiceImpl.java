package com.project.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.project.dao.BookingDAO;
import com.project.model.Booking;
import com.project.model.Event;
import com.project.model.PeopleAttending;
import com.project.model.User;

@Service(value = "bookingService")
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService{


	@Autowired
	BookingDAO bookingDao;
	
	
	public User getBookingHistory(long phoneNo) throws Exception  {
		
		User user=bookingDao.getBookingHistory(phoneNo);
		if(user==null) {
			throw new Exception(" NO SUCH USER EXIST ");
		}
		if(user.getPhoneNo()==-1) {
			throw new Exception(" NO BOOKINGS EXIST ");
		}
		return user;
	}
	
	public Event getEventInfo(Integer eventId) throws Exception{
		
		Event event=bookingDao.getEventInfo(eventId);
		if(event==null) {
			throw new Exception(" NO SUCH EVENT EXIST ");
		}
		return event;
	}
	

	@Transactional(readOnly = false,propagation=Propagation.REQUIRES_NEW)
	public Booking bookEvent(Booking booking) throws Exception {
		booking.setBookingType(booking.getBookingType().toLowerCase()); 			//This is to maintain the case of type in back-end
		if(booking.getPhoneNo()==0 || booking.getEmail()==null || booking.getFullName()==null) {
			throw new Exception(" DETAILS OF PERSON MAKING BOOKINGS ARE MISSING (PHONE NO/EMAIL/NAME)");
		}
		if(booking.getEvent()==null || booking.getBookingType()==null || booking.getIdProof()==null) {
			throw new Exception(" DETAILS OF BOOKING, REQUIRED FOR MAKING BOOKINGS ARE MISSING (EVENTID/BOOKING TYPE/ID PROOF)");
		}
		if(booking.getDetailsOfPeople().isEmpty() && booking.getMessage()!=null) {
			throw new Exception(" NO DETAILS OF PERSON ATTENDING THE EVENT FOUND ");
		}
		
		booking=bookingDao.bookEvent(booking);
		
		if(booking==null) {
			throw new Exception(" NO SUCH EVENT EXIST ");
		}
		if(booking.getBookingId()==-1) {
			throw new Exception(" NO VALID DETAILS OF PEOPLE WILLING TO ATTEND THE EVENT EXISTS , "+booking.getMessage());
		}
		if(booking.getBookingId()==-2) {
			throw new Exception(" PHONE NO ALREADY REGISTERED WITH OTHER USER NAME : "+booking.getMessage());
		}
		if(booking.getBookingId()==-3) {
			throw new Exception(" BOOKING FAILED BECAUSE REMAINING SEATS ARE NOT ENOUGH : "+booking.getMessage());
		}
		if(booking.getBookingId()==-4) {
			throw new Exception(" FOR BOOKING TYPE SELF, DETAILS OF USER SHOULD MATCH DETAILS OF PERSON ATTENDING ");
		}
		return booking;
	}
	
	public boolean duplicateNumberCheck(PeopleAttending people) throws Exception{
		
		if(people.getPhoneNo()==0) {
			throw new Exception(" PHONE NUMBER IS MANDATORY ");
		}
		if(people.getFullName()==null) {
			throw new Exception(" FULL NAME IS MANDATORY ");
		}
		
		PeopleAttending peopleCheck=new PeopleAttending();
		peopleCheck.setPhoneNo(people.getPhoneNo());
		
		peopleCheck=bookingDao.duplicateNumberCheck(peopleCheck);
		
		if(peopleCheck==null) {
			return true; 
		}
		
		String entity=peopleCheck.getFullName().toLowerCase();
		String model=people.getFullName().toLowerCase();
		
		if(entity.equalsIgnoreCase(model)) {
			return true;
		}
		
		if(entity.contains(model))						//when the name in database is "khyati gupta" valid entries for this case will be "khyati" and "khyati gupta"
		{
			return true;
		}
		
		else if(model.contains(entity)){				//when the name saved in database is "khyati" and the request sent is for "khyati gupta"
			return true;
		}
		
		return false;
	
	}
	
	public  Map<String,List<Booking>> viewTypeViseBooking(Integer eventId) throws Exception{
		
		Map<String,List<Booking>> map=bookingDao.viewTypeViseBooking(eventId);	
		if(map==null) {
			throw new Exception(" NO SUCH EVENT EXIST ");
		}
		return map; 
	}
	
	public  Booking viewBookingDetails(long bookingId) throws Exception {
		
		Booking b= bookingDao.viewBookingDetails(bookingId);
		if(b==null) {
			throw new Exception(" NO SUCH BOOKING EXIST "); 
		}
		return b;
	}
	
	public List<Event> getLiveEvents() throws Exception{
		List<Event> list= bookingDao.getLiveEvents();
		if(list==null) {
			throw new Exception(" NO ON GOING EVENT ");
		}
		return list;
	}
	
	@Transactional(readOnly = false,propagation=Propagation.REQUIRES_NEW)
	public Event addNewEvent(Event event) throws Exception {
		if(event.getEventName()==null) {
			throw new Exception(" EVENT NAME IS MANDATORY ");
		}
		if(event.getDateOfEvent()==null) {
			throw new Exception(" EVENT DATE IS MANDATORY ");
		}
		if(event.getTotalBookings()==null) {
			throw new Exception(" NUMBER OF BOOKINGS ALLOWED IS MANDATORY ");
		}
		if(event.getEventType()==null) {
			throw  new Exception(" EVENT TYPE IS MANDATORY ");
		}
		
		event.setEventType(event.getEventType().toLowerCase());
		
		if(!(event.getEventType().equals("online") || event.getEventType().equals("live")) ) {
			throw  new Exception(" EVENT TYPE CAN BE ONLINE OR LIVE ");
		}
		
		Event eventReturn=bookingDao.addNewEvent(event);
		
		if(eventReturn==null) {
			throw new Exception(" UNABLE TO ADD EVENT ");
		}
		return eventReturn;
	}
	
	@Transactional(readOnly = false,propagation=Propagation.REQUIRES_NEW)
	public void updateEvent(Event event) throws Exception {
		if(!(event.getEventName()!=null || event.getDescription()!=null ||event.getEventType()!=null || event.getDateOfEvent()!=null)) {
			throw new Exception(" NO DATA FOR UPDATE "); 
		}
		if(event.getEventId()==null) {
			throw new Exception(" NO EVENT ID SENT FOR UPDATE "); 
		}
		event=bookingDao.updateEvent(event);
		
		if(event==null) {
			throw new Exception(" NO SUCH EVENT EXIST ");
		}
		if(event.getEventId()==-1) {
			throw new Exception(" UNABLE TO UPDATE THE DETAILS ");
		}
	}
}
