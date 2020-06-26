package com.project.api;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.Booking;
import com.project.model.Event;
import com.project.model.PeopleAttending;
import com.project.model.User;
import com.project.service.BookingService;

@CrossOrigin
@RestController
@RequestMapping("/booking")
public class BookingAPI {
	
	@Autowired
	BookingService bookingService;
	
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//	Returns list of all the Booking made by a user inside user object type. 
//	If no user exists on corresponding phone no, null object with message  "NO SUCH USER EXIST"	 is returned.
//	If No booking history exists a null object with message "NO BOOKINGS EXIST" is returned.	
//	Return object consists of all bookings made and events for which booking was made and people whose booking was done inside the user object.
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	@RequestMapping(produces =MediaType.APPLICATION_JSON_VALUE,method=RequestMethod.GET,value="/getBookingHistory/{phoneNo}",consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<User> getBookingHistory(@PathVariable long phoneNo) 
	{		
		try {
			User user=bookingService.getBookingHistory(phoneNo);
			user.setMessage(" HISTORY SENT SUCCESSFULLY ");
			return new ResponseEntity<User>(user,HttpStatus.OK);
			}
		catch(Exception e){
			User userReturn=new User();
			userReturn.setMessage(e.getMessage());
			return new ResponseEntity<User>(userReturn,HttpStatus.BAD_REQUEST);
		}		
	}
	
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//		Returns details that are not confidential (like who is attending and who has booked).
//		This method will be used to have details of an event, these details are shown on the user's home Page to know about the event.
//		If an event does not exist null object with message "NO SUCH EVENT EXIST" .
//		Else returns event details in event object with message "EVENT INFORMATION EXTRACTED SUCCESSFULLY"
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	@RequestMapping(produces =MediaType.APPLICATION_JSON_VALUE,method=RequestMethod.GET,value="/getEventInfo/{eventId}",consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Event> getEventInfo(@PathVariable Integer eventId) 
	{		
		try {
			Event event=bookingService.getEventInfo(eventId);
			event.setMessage(" EVENT INFORMATION EXTRACTED SUCCESSFULLY ");
			return new ResponseEntity<Event>(event,HttpStatus.OK);
		}
		catch(Exception e){
			Event eventReturn=new Event();
			eventReturn.setMessage(e.getMessage());
			return new ResponseEntity<Event>(eventReturn,HttpStatus.BAD_REQUEST);
		}	
	}
	
	
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//		This method makes booking for the people and returns details like no of people actually registered (this number may change only if any person was already registered for the same event), booking id and list of people registered.
//		If the person making the booking is not a user his account will be automatically created and the password (Random 6 digit number) will be mailed to him on his registered email ID.
//		Mail and SMS for successful booking will be sent on the person making the booking's email and phone number.
//		If any of the mandatory details are missing null object with message " DETAILS OF PERSON MAKING BOOKINGS ARE MISSING (PHONE NO/EMAIL/NAME) "  or  ” DETAILS OF BOOKING, REQUIRED FOR MAKING BOOKINGS ARE MISSING (EVENTID/BOOKING TYPE/ID PROOF) "  or  " NO DETAILS OF PERSON ATTENDING THE EVENT FOUND " is returned.
//		If no event exists on eventId null object with message "NO SUCH EVENT EXIST" is returned.
//		If the method is unable to find the details of people willing to make booking becoz their bookings are already done for the event  “  NO VALID DETAILS OF PEOPLE WILLING TO ATTEND THE EVENT EXISTS “ + 826*******+  ": IS ALREADY REGISTERED FOR EVENT, SO CAN NOT RE-REGISTER  "  is returned (All the phone numbers already registered are sent in message).
//		If the phone number entered by the user for group details already exists with some other name (example 826******* has name khyati stored in backend and the user enters kushal ) a null object with message  " PHONE NO ALREADY REGISTERED WITH OTHER USER NAME : " + 826******* is returned. The two cases where the person is same are handled internally (also the checking done is case insensitive, so Khyati and khyati are treated same ):	Case 1: when the name in the database is Khyati Gupta and the user enters Khyati only. In this case the booking detail updates with full name.Cade 2: when the name in the database is Khyati and the user enters Khyati Gupta. The database is updated to full name.
//		If the no of bookings required are more than no of seats left the booking fails and a null object with message  " BOOKING FAILED BECAUSE REMAINING SEATS ARE NOT ENOUGH : "+ Number of seats available for event is returned.
//		If the booking type is specified to “self” and the number of people is more than one OR the number of people is one only but the details of user do not match the details of person who is making the booking in such cases a null object with message  " FOR BOOKING TYPE SELF, DETAILS OF USER SHOULD MATCH DETAILS OF PERSON ATTENDING " is returned.
//		If the booking type is other than “self” and the person tries to make his own booking only then the booking type is changed to self by the method.On successful the object with message " BOOKING MADE SUCCESSFULLY " is returned or in case some of the phone numbers were already registered the message returned " BOOKING MADE SUCCESSFULLY, PHONE NO ALREADY REGISTERED WITH OTHER USER NAME : " + 826*******   this message consists of all the phone numbers who were previously registered.
//		After the booking is done the method adds this booking to users booking history and to the list of booking for the event. And the people attending the event are added to the list of people attending event maintained by event.
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	@RequestMapping(produces =MediaType.APPLICATION_JSON_VALUE,method=RequestMethod.POST,value="/bookEvent")
	@ResponseBody
	public ResponseEntity<Booking> bookEvent(@RequestBody Booking booking) 
	{
		try {
			booking=bookingService.bookEvent(booking);
			if(booking.getMessage()==null)
				booking.setMessage(" BOOKING MADE SUCCESSFULLY ");
			else
				booking.setMessage(" BOOKING MADE SUCCESSFULLY, "+booking.getMessage());
			return new ResponseEntity<Booking>(booking,HttpStatus.OK);
		}
		catch (Exception e) {
			Booking bookingReturn=new Booking();
			bookingReturn.setMessage(e.getMessage());
			return new ResponseEntity<Booking>(bookingReturn,HttpStatus.BAD_REQUEST);
		}
	}
	
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//		This method returns true or false in message 
//	    if the details do not exist i.e. object is null and if the entered object is registered with same name message returned is true
//		if the details exist with different name message returned is false
//		Exceptional cases handled if database has name "khyati gupta" and request sent has "khyati" vice versa will all return true as it is the same person	
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	@RequestMapping(produces =MediaType.APPLICATION_JSON_VALUE,method=RequestMethod.POST,value="/duplicateNumberCheck",consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<PeopleAttending> duplicateNumberCheck(@RequestBody PeopleAttending people) 
	{
			try {
				people.setMessage(Boolean.toString(bookingService.duplicateNumberCheck(people)));
				return new ResponseEntity<PeopleAttending>(people,HttpStatus.OK);
			} catch (Exception e) {
				people.setMessage(e.getMessage());
				return new ResponseEntity<PeopleAttending>(people,HttpStatus.BAD_REQUEST);
			}
			
		
	}


//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//		This method is used to provide data for events bookings analysis on admin’s dashboard in the form of bar graphs.
//		Returns Map of lists of bookings made for particular event sorted according to the type of booking i.e. self,group,corporate and other
//		The returned List is of type: 
//		[ “self” : [  {Object of booking of type:self} , {Object of booking of type:self} , {Object of booking of type:self } ] ,
//		“other” : [ {Object of booking of type:other} , {Object of booking of type:other} , {Object of booking of type:other }  ] ,
//		“corporate” : [ {Object of booking of type:corporate} , {Object of booking of type:corporate} , {Object of booking of type:corporate }  ] ,
//		“group” : [ {Object of booking of type:group} , {Object of booking of type:group} , {Object of booking of type:group }  ] ]
//		If the event does not exist one element will be added to the map being returned	[ "NO SUCH EVENT EXIST":null ]
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	@RequestMapping(produces =MediaType.APPLICATION_JSON_VALUE,method=RequestMethod.GET,value="/viewTypeViseBooking/{eventId}")
	@ResponseBody
	public  Map<String,List<Booking>> viewTypeViseBooking(@PathVariable Integer eventId) throws Exception{
		try {
			Map<String,List<Booking>> list=bookingService.viewTypeViseBooking(eventId);
			return list;
		}
		catch (Exception e) {
			Map<String,List<Booking>> x=new HashMap<String, List<Booking>>();
			x.put(e.getMessage(),null);
			return x;
		}		
	}
	
	
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//		This Method Returns the complete booking details of a particular booking 
//		If the booking does not exist null object returned with message "NO SUCH BOOKING EXIST"	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	@RequestMapping(produces =MediaType.APPLICATION_JSON_VALUE,method=RequestMethod.GET,value="/viewBookingDetails/{bookingId}")
	@ResponseBody
	public  ResponseEntity<Booking> viewBookingDetails(@PathVariable long bookingId) throws Exception{
		try {
			Booking booking=bookingService.viewBookingDetails(bookingId);
			booking.setMessage(" DETAILS RETURNED SUCCESSFULLY ");
			return new ResponseEntity<Booking>(booking,HttpStatus.OK);
		}
		catch (Exception e) {
			Booking bookingReturn=new Booking();
			bookingReturn.setMessage(e.getMessage());
			return new ResponseEntity<Booking>(bookingReturn,HttpStatus.BAD_REQUEST);
			
		}
	}
	
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//		This Method returns the List of Events that are open for booking And the event is today or upcoming
//		"NO ON GOING EVENT"	message returned if no upcoming event existed
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	@RequestMapping(produces =MediaType.APPLICATION_JSON_VALUE,method=RequestMethod.GET,value="/getLiveEvents",consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<Event>> getLiveEvents() {
		try {
			List<Event> bookingReturn=bookingService.getLiveEvents();
			return new ResponseEntity<List<Event>>(bookingReturn,HttpStatus.OK);
		}
		catch (Exception e) {
			List<Event> list=new ArrayList<Event>();
			Event eventReturn=new Event();
			eventReturn.setMessage(e.getMessage());
			list.add(eventReturn);
			return new ResponseEntity<List<Event>>(list,HttpStatus.BAD_REQUEST);
			
		}
		
	}
	
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//		This method can be used by admin to add new events on the site.
//		On violation of mandatory fields an null object with message  “IT'S IMPORTANT TO SPECIFY OF EVENT NAME” or " IT'S IMPORTANT TO SPECIFY OF EVENT DATE " or "IT'S IMPORTANT TO SPECIFY NUMBER OF BOOKINGS ALLOWED " or " IT'S IMPORTANT TO SPECIFY OF EVENT TYPE  " is returned.
// 		If event type is not entered correctly "EVENT TYPE CAN BE ONLINE OR LIVE"
//		This method adds new events details and returns a message= "EVENT ADDED SUCCESSFULLY".
//		If the method is unable to set event details returns null object with message message returned  "UNABLE TO ADD EVENT".
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	@RequestMapping(produces =MediaType.APPLICATION_JSON_VALUE,method=RequestMethod.POST,value="/addNewEvent",consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Event> addNewEvent(@RequestBody Event event){
			
		try {
			bookingService.addNewEvent(event);
			event.setMessage(" EVENT ADDED SUCCESSFULLY ");
			return new ResponseEntity<Event>(event,HttpStatus.OK);
		}
		catch(Exception e) {
			Event eventReturn=new Event();
			eventReturn.setMessage(e.getMessage());
			return new ResponseEntity<Event>(eventReturn,HttpStatus.BAD_REQUEST);
		}
	}

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//		If no data to update found an null object with message "NO DATA FOUND FOR UPDATE" is returned.
//		If the eventId is missing an null object with message "NO EVENT ID SENT FOR UPDATE" is returned.
//		If the eventId is incorrect null object with message "NO SUCH EVENT EXIST" is returned.
//		If the server fails to update null object with message "UNABLE TO UPDATE THE DETAILS" is returned.
//		If the details are updated properly the received object with message "EVENT ADDED SUCCESSFULLY" is returned.
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	@RequestMapping(produces =MediaType.APPLICATION_JSON_VALUE,method=RequestMethod.POST,value="/updateEvent",consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Event> updateEvent(@RequestBody Event event) throws Exception {
		try {
			bookingService.updateEvent(event);
			event.setMessage(" EVENT ADDED SUCCESSFULLY ");
			return new ResponseEntity<Event>(event,HttpStatus.OK);
		}
		catch(Exception e) {
			Event eventReturn=new Event();
			eventReturn.setMessage(e.getMessage());
			return new ResponseEntity<Event>(eventReturn,HttpStatus.BAD_REQUEST);
		}
		
	}
	
}