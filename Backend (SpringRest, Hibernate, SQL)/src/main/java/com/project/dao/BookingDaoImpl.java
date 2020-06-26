package com.project.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.project.entity.BookingEntity;
import com.project.entity.EventEntity;
import com.project.entity.PeopleAttendingEntity;
import com.project.entity.UserEntity;
import com.project.model.Booking;
import com.project.model.Event;
import com.project.model.PeopleAttending;
import com.project.model.User;
import com.project.utilities.JavaMailUtil;
import com.project.utilities.MobileMessage;

@Repository(value ="bookingDao")
public class BookingDaoImpl implements BookingDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	public User getBookingHistory(long phoneNo){		
		
		Session session=sessionFactory.getCurrentSession();
		UserEntity userE=session.get(UserEntity.class,phoneNo);
		
		if(userE==null) {
			return null;
		}
		User user=new User();
		
		if(userE.getBookings().isEmpty()) {
			user.setPhoneNo(-1);
			return user;
		}
		List<Booking> bookinglList=new ArrayList<Booking>();
		
		for(BookingEntity bookingE:userE.getBookings()) {
			Booking booking=bookingE.booking(bookingE);
			
			//booking event 
			EventEntity eventE=bookingE.getEvent();
			Event event=eventE.event(eventE);
			booking.setEvent(event);
			
			//people attending
			List<PeopleAttending> listPeople=new ArrayList<PeopleAttending>();
			
			for(PeopleAttendingEntity peopleE:bookingE.getDetailsOfPeople()) {
				PeopleAttending people=peopleE.peopleAttending(peopleE);
				listPeople.add(people);
			}
			booking.setDetailsOfPeople(listPeople);
			bookinglList.add(booking);
		}
		user.setBooking(bookinglList);
		user.setPhoneNo(userE.getPhoneNo());
		return user;
	}
	
	
	public Event getEventInfo(Integer eventId){					//Returns details that are not confidential like who is attending and who has booked are not returned
		
		Session session=sessionFactory.getCurrentSession();		// these details will be used to show event details on home page
		EventEntity eventE=session.get(EventEntity.class,eventId);
		
		if(eventE==null) {
			return null;
		}
		
		Event event=eventE.event(eventE);
		return event;
	}
	
		
	public	Booking bookEvent(Booking booking) throws Exception {
		
		Session session=sessionFactory.getCurrentSession();
		EventEntity eventE=session.get(EventEntity.class,booking.getEvent().getEventId());
		
		if(eventE==null) {											   //if event doesn't exist booking fails
			return null;
		}
		
		LocalDate date=LocalDate.now();									//setting booking date as the system date now
		booking.setBookingDate(date);
		
		BookingEntity bookingE=booking.booking(booking);
		bookingE.setEvent(eventE); 										//eventEntity saved in booking
		
		if(booking.getMessage()==null) 
		{																//When the person making booking wants his booking also to be done, front-end sends "1" else it sends "0"
																		//This step is done to add the details of person making booking in the list of people attending 
			booking.getDetailsOfPeople().remove(0);		
	
		}
		booking.setMessage(null);
		
		List<PeopleAttendingEntity> listE=new ArrayList<PeopleAttendingEntity>();
		List<PeopleAttendingEntity> listExisting=eventE.getPeopleAttending();
		List<PeopleAttending> plist=new ArrayList<PeopleAttending>();
		
		for(PeopleAttending people:booking.getDetailsOfPeople()) {
			PeopleAttendingEntity peopleE=session.get(PeopleAttendingEntity.class,people.getPhoneNo());
			
			if(peopleE==null) {											//if no database available for person new person added in database
				peopleE=people.peopleAttending(people);
				session.persist(peopleE);
				session.flush();
				plist.add(people);
				listE.add(peopleE);
			}else {
				String entity=peopleE.getFullName().toLowerCase();
				String model=people.getFullName().toLowerCase();
				
			 if(listExisting.contains(peopleE)) {					//if the person is already registered for event adding a message for a reason of not re-register 
				 if(booking.getMessage()==null) {
					booking.setMessage(peopleE.getPhoneNo()+": IS ALREADY REGISTERED FOR EVENT, SO CAN NOT RE-REGISTER ");
				}
				else {
					booking.setMessage(booking.getMessage()+peopleE.getPhoneNo()+": IS ALREADY REGISTERED FOR EVENT, SO CAN NOT RE-REGISTER ");
				}
			}
			else if(model.contains(entity) && !entity.equals(model)) {
				peopleE.setFullName(people.getFullName());						//this case handles the situation when the person details in database might not be complete, example database name has fullName:"khyati" and request sent has fullName:"khyati gupta"
				listE.add(peopleE);
				plist.add(people);
			}
			else if(entity.contains(model) && !entity.equals(model)) {
				people.setFullName(peopleE.getFullName());						//this case handles the situation when the person details in database might be complete, example database name has fullName:"khyati gupta" and request sent has fullName:"khyati"
				listE.add(peopleE);
				plist.add(people);
			}
			
			else if(!entity.equals(model)) {									//if database exist for number but for different person returns error as no two user can have same phoneNo 
				Booking book=new Booking();									
				book.setBookingId(-2);
				book.setMessage(people.getFullName());
				return book;
			}
			
			else {
				listE.add(peopleE);
				plist.add(people);
			}
			 }
		
		}
		
		booking.setDetailsOfPeople(plist);
		bookingE.setDetailsOfPeople(listE);								//List of people attending after all verfications

		booking.setNoOfPeople(listE.size());
		bookingE.setNoOfPeople(booking.getNoOfPeople());				// resetting no of people according to actual no of booking being made
		
		if(listE.isEmpty()) {											 //if no details of people entered booking fails as no one to be registered
			booking.setBookingId(-1);
			return booking;
		}
		
		if(eventE.getRemainingBooking()<booking.getNoOfPeople()) {		//if seats not available booking fails
			booking.setBookingId(-3);
			booking.setMessage(eventE.getRemainingBooking().toString());
			return booking;
		}
		
		if(!booking.getBookingType().equals("self")) {					//If the person is making booking of his own but he doesn't select the booking type self it will be done by this method
			if(booking.getNoOfPeople()==1 && booking.getPhoneNo()==booking.getDetailsOfPeople().get(0).getPhoneNo() && (booking.getFullName().toLowerCase().contains(booking.getDetailsOfPeople().get(0).getFullName().toLowerCase()) || booking.getDetailsOfPeople().get(0).getFullName().toLowerCase().contains(booking.getFullName().toLowerCase()))) {
				booking.setBookingType("self");
				bookingE.setBookingType("self");
			}
		}
		else {															//verfication of booking type for self
			
			if(booking.getNoOfPeople()!=1 || booking.getNoOfPeople()==1 && booking.getPhoneNo()!=booking.getDetailsOfPeople().get(0).getPhoneNo() || !(booking.getFullName().toLowerCase().contains(booking.getDetailsOfPeople().get(0).getFullName().toLowerCase()) || booking.getDetailsOfPeople().get(0).getFullName().toLowerCase().contains(booking.getFullName().toLowerCase())))
			{
				Booking book=new Booking();
				book.setBookingId(-4);
				return book;
			}	
			if( booking.getNoOfPeople()==1) {
				
			}
		}
		 
		session.persist(bookingE);
		session.flush();
		booking.setBookingId(bookingE.getBookingId());					// return new bookingId for ticket generation
		eventE.setRemainingBooking(eventE.getRemainingBooking()-booking.getNoOfPeople());    //updating no of seats left in event database
		
		booking.setEvent(eventE.event(eventE));
		
		JavaMailUtil.sendMail(booking.getEmail(), booking);				//Email for successful booking sent
		MobileMessage.message(booking.getPhoneNo(), eventE.getEventName(),booking.getEmail());		
		
		UserEntity userE=session.get(UserEntity.class,booking.getPhoneNo());
		
		Integer min=100000;
		Integer max=999999;
		Integer rand_int= (int)(Math.random() * (max - min + 1) + min);
		
		if(userE==null) {												//if no user exist in database create new user ID for the persone making booking 
			userE=new UserEntity();
			userE.setPhoneNo(booking.getPhoneNo());
			userE.setEmail(booking.getEmail());
			userE.setFullName(booking.getFullName());
			userE.setPhoneNo(booking.getPhoneNo());
			userE.setGender(booking.getGender());
			userE.setPassword(Integer.toString(rand_int));				//password is also sent to the users email, it is a random number that is generated in front end  
			session.persist(userE);
			session.flush();
			JavaMailUtil.sendMailUser(userE.getEmail(),userE.getPhoneNo(),userE.getPassword());			//Mail of new Account user creation
		}	
		
		List<BookingEntity> listbookE=userE.getBookings();				//Adding booking details to user booking history
		listbookE.add(bookingE);
		userE.setBookings(listbookE);
		session.persist(userE);
		
		List<BookingEntity> listeventE=eventE.getListOfBookings();		      //Adding booking to Event booking record 
		listeventE.add(bookingE);
		eventE.setListOfBookings(listeventE);
		List<PeopleAttendingEntity> listpeopleE=eventE.getPeopleAttending();  //Adding people attending to the record of Event list of people attending
		listpeopleE.addAll(bookingE.getDetailsOfPeople());
		eventE.setPeopleAttending(listpeopleE);
		
		session.persist(eventE);
		session.flush();
		
		return booking;
	}
	
	
	public PeopleAttending duplicateNumberCheck(PeopleAttending people) {
		
		Session session=sessionFactory.getCurrentSession();
		PeopleAttendingEntity peopleE=session.get(PeopleAttendingEntity.class,people.getPhoneNo());
		
		if(peopleE==null) {
			return null;
		}
		people.setFullName(peopleE.getFullName());
		
		return people;
	}
	
	
	public Map<String,List<Booking>> viewTypeViseBooking(Integer eventId){
		
		Map<String,List<Booking>> returnList=new HashMap<String, List<Booking>>();
		Session session=sessionFactory.getCurrentSession();
		
		EventEntity eventE=session.get(EventEntity.class,eventId);
		if(eventE==null) {
			return null;
		}
			
		CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = builder.createQuery(Object[].class);
        Root<BookingEntity> root = criteriaQuery.from(BookingEntity.class);
        
        criteriaQuery.multiselect(root.get("bookingId"),root.get("fullName"),root.get("bookingDate"));
        criteriaQuery.where(builder.and(builder.equal(root.get("event"),eventE)),(builder.equal(root.get("bookingType"),"self")));
        List<Object[]> list = session.createQuery(criteriaQuery).list();
        
        
        if(!list.isEmpty())
        {
        	List<Booking> bookList=new ArrayList<Booking>();
        
        	for(Object[] x:list) {
        		Booking b=new Booking();
        		b.setBookingId((long)x[0]);
        		b.setFullName((String)x[1]);
        		b.setBookingDate((LocalDate)x[2]);
        		bookList.add(b);
        		}
        
        	returnList.put("self",bookList);
        }
       
        else {
        	returnList.put("self",new ArrayList<Booking>());
        }
        
        
        criteriaQuery.multiselect(root.get("bookingId"),root.get("fullName"),root.get("bookingDate"));
        criteriaQuery.where(builder.and(builder.equal(root.get("event"),eventE)),(builder.equal(root.get("bookingType"),"group")));
        list = session.createQuery(criteriaQuery).list();
       
        if(!list.isEmpty())
        {
            List<Booking> bookList=new ArrayList<Booking>();
        	
            for(Object[] x:list) {
            	Booking b=new Booking();
            	b.setBookingId((long)x[0]);
            	b.setFullName((String)x[1]);
            	b.setBookingDate((LocalDate)x[2]);
            	bookList.add(b);
        	  }
        	  returnList.put("group",bookList);
        	}
        else{
            	returnList.put("group",new ArrayList<Booking>());
            }
              
        criteriaQuery.multiselect(root.get("bookingId"),root.get("fullName"),root.get("bookingDate"));
        criteriaQuery.where(builder.and(builder.equal(root.get("event"),eventE)),(builder.equal(root.get("bookingType"),"corporate")));
        list = session.createQuery(criteriaQuery).list();
        
        if(!list.isEmpty()) 
        {
           List<Booking> bookList=new ArrayList<Booking>();
           
           for(Object[] x:list) {
        	   Booking b=new Booking();
        	   b.setBookingId((long)x[0]);
        	   b.setFullName((String)x[1]);
        	   b.setBookingDate((LocalDate)x[2]);
        	   bookList.add(b);
        }
           returnList.put("corporate",bookList);
        }
        else {
        	returnList.put("corporate",new ArrayList<Booking>());
        }
       
        criteriaQuery.multiselect(root.get("bookingId"),root.get("fullName"),root.get("bookingDate"));
        criteriaQuery.where(builder.and(builder.equal(root.get("event"),eventE)),(builder.equal(root.get("bookingType"),"other")));
        list = session.createQuery(criteriaQuery).list();
       
        if(!list.isEmpty()) 
        {
            List<Booking> bookList=new ArrayList<Booking>();
      
            for(Object[] x:list) {
            	Booking b=new Booking();
            	b.setBookingId((long)x[0]);
            	b.setFullName((String)x[1]);
            	b.setBookingDate((LocalDate)x[2]);
            	bookList.add(b);
        }
            returnList.put("other",bookList);
        }
        else {
        	returnList.put("other",new ArrayList<Booking>());
        }
   
		return returnList;
	}
	
	
	public  Booking viewBookingDetails(long bookingId) {
		
		Session session=sessionFactory.getCurrentSession();
		BookingEntity bookingE=session.get(BookingEntity.class,bookingId);
		
		if(bookingE==null) {
			return null;
		}
		
		Booking booking=bookingE.booking(bookingE);
		List<PeopleAttending> list=new ArrayList<PeopleAttending>();
		PeopleAttending people=new PeopleAttending();
		
		for(PeopleAttendingEntity pe:bookingE.getDetailsOfPeople()) {
			people=pe.peopleAttending(pe);
			list.add(people);
		}
	
		booking.setDetailsOfPeople(list);
		Event event=bookingE.getEvent().event(bookingE.getEvent());
		booking.setEvent(event);
		return booking;
	}
	
	
	public List<Event> getLiveEvents(){
		
		Session session=sessionFactory.getCurrentSession();
		LocalDate now = LocalDate.now();  
		EventEntity eventE;
	    Event event;
	    List<Event> listReturn=new ArrayList<Event>();
		CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Integer> criteriaQuery= builder.createQuery(Integer.class);
        Root<EventEntity> root = criteriaQuery.from(EventEntity.class);
        criteriaQuery.select(root.get("eventId"));
        List<Predicate> restrictions = new ArrayList<Predicate>();
        restrictions.add(builder.greaterThanOrEqualTo(root.get("dateOfEvent").as(LocalDate.class), now));
        criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
        List<Integer> list= session.createQuery(criteriaQuery).getResultList();
      
       if(list.isEmpty()) {
    	   return null;
       }
        for(Integer x:list){
        	eventE=session.get(EventEntity.class,x);
        	event=eventE.event(eventE);
        	listReturn.add(event);
        }
        
		return listReturn;
	}
	
	
	public Event addNewEvent(Event event) {
		
		event.setRemainingBooking(event.getTotalBookings());
		EventEntity eventE=event.event(event);
		Session session=sessionFactory.getCurrentSession();
		session.persist(eventE);
		session.flush();
		event.setEventId(eventE.getEventId());
		EventEntity eventCheck=session.get(EventEntity.class,eventE.getEventId());
		if(eventCheck==null) {
			return null;
		}
		return event;
	}
	
	
	public Event updateEvent(Event event) {
		
		Session session=sessionFactory.getCurrentSession();
		EventEntity eventE=session.get(EventEntity.class,event.getEventId());
		if(eventE==null) {
			return null;
		}
		eventE.setEventId(event.getEventId());
		eventE.setEventName(event.getEventName());
		eventE.setDescription(event.getDescription());
		eventE.setDateOfEvent(event.getDateOfEvent());
		eventE.setTotalBookings(event.getTotalBookings());
		eventE.setRemainingBooking(event.getRemainingBooking());
		eventE.setEventType(event.getEventType());
		session.persist(eventE);
		session.flush();
	
		EventEntity eventCheck=session.get(EventEntity.class,event.getEventId());
		if(eventCheck!=eventE) {
		event.setEventId(-1); 	
		return event;
		}
		return event;
	}
}
