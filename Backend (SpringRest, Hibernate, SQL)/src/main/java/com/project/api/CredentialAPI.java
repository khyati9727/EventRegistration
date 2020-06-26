package com.project.api;

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

import com.project.model.Admin;
import com.project.model.User;
import com.project.service.CredentialService;

@CrossOrigin
@RestController
@RequestMapping("/credential")
public class CredentialAPI {
	
	@Autowired
	CredentialService service;

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//		this method returns an object with fullName(Admin's name), message= "LOGGED IN SUCCESSFULLY" if the data entered was correctly
//		If no admin with the id exist returns a null object with message= "NO SUCH ADMIN EXIST"
//		if password entered is incorrect returns null object with message= "ENTERED INCORRECT PASSWORD"
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	@RequestMapping(produces =MediaType.APPLICATION_JSON_VALUE,method=RequestMethod.POST,value="/adminLogin",consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Admin> adminLogin(@RequestBody Admin admin) 
	{
			try{
				admin=service.adminLogin(admin);
				admin.setMessage(" LOGGED IN SUCCESSFULLY ");
				return new ResponseEntity<Admin>(admin,HttpStatus.OK);
			}
			catch(Exception e) {
				Admin adminReturn=new Admin();
				adminReturn.setMessage(e.getMessage());
				return new ResponseEntity<Admin>(adminReturn,HttpStatus.BAD_REQUEST);
			}
			
		}
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//		this method returns an object with fullName(User's name), message= "LOGGED IN SUCCESSFULLY" if the data entered was correctly
//		If no user with the phoneNo exist returns a null object with message= "NO SUCH USER EXIST"
//		if password entered is incorrect returns null object with message= "ENTERED INCORRECT PASSWORD"
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	@RequestMapping(produces =MediaType.APPLICATION_JSON_VALUE,method=RequestMethod.POST,value="/userLogin",consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<User> userLogin(@RequestBody User user) 
	{		
		try{
			user=service.userLogin(user);
			user.setMessage(" LOGGED IN SUCCESSFULLY ");
			return new ResponseEntity<User>(user,HttpStatus.OK);
		}
		catch(Exception e) {
			User userReturn=new User();
			userReturn.setMessage(e.getMessage());
			return new ResponseEntity<User>(userReturn,HttpStatus.BAD_REQUEST);
		}
		}
	
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//		This method is used to create a new account for users.
//		When the Account is created a welcome mail is sent to the user’s email if the password is unknown to the user(this case occurs when the user doesn’t create an account and directly makes a booking in this case his account is created automatically ) the password is sent in the mail.
//		If the details already exist an null object with the message " USER PROFILE ALREADY EXIST " is returned.
//		If mandatory fields are violated an null object with the message " EMAIL IS MANDATORY FIELD " or       " PHONE NUMBER IS MANDATORY FIELD " is returned.
//		returns null object if unable to store and add the exception thrown by compiler to message, else add message as "UNABLE TO CREATE PROFILE FOR USER"
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------		
	
	@RequestMapping(produces =MediaType.APPLICATION_JSON_VALUE,method=RequestMethod.POST,value="/userSignUp",consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<User> userSignUp(@RequestBody User user) 
	{		
		try {
			service.userSignUp(user);
			user.setMessage(" USER ACCOUNT CREATED SUCCESSFULLY ");
			return new ResponseEntity<User>(user,HttpStatus.OK);
		}
		catch (Exception e) {
			User userReturn=new User();
			userReturn.setMessage(e.getMessage());
			return new ResponseEntity<User>(userReturn,HttpStatus.BAD_REQUEST);
		}
			
		}
	
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//		Uses password sent in the message field to set a new password.
//		If the passwords are entered in correctly an null object with message  "INCORRECT OLD PASSWORD" or " NEW PASSWORD AND OLD PASSWORD ARE SAME " is returned.
//		If the adminId does not exist  an null object with message " NO SUCH USER EXIST " is returned.
//		If the mandatory fields are missing an null object with a message " NEW PASSWORD WAS NOT ENTERED " or " OLD PASSWORD WAS NOT ENTERED " is returned.
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	@RequestMapping(produces =MediaType.APPLICATION_JSON_VALUE,method=RequestMethod.POST,value="/userPasswordChange",consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<User> userPasswordChange(@RequestBody User user) 
	{		
		try {
			service.userChangePassword(user);
			user.setMessage(" PASSWORD UPDATED SUCCESSFULLY ");
			return new ResponseEntity<User>(user,HttpStatus.OK);
		}
		catch (Exception e) {
			User userReturn=new User();
			userReturn.setMessage(e.getMessage());
			return new ResponseEntity<User>(userReturn,HttpStatus.BAD_REQUEST);
		}
			
		}
	
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//		Uses password sent in the message field to set a new password.
//		If the passwords are entered in correctly an null object with message  "INCORRECT OLD PASSWORD" or " NEW PASSWORD AND OLD PASSWORD ARE SAME " is returned.
//		If the adminId does not exist  an null object with message " NO SUCH ADMIN EXIST " is returned.
//		If the mandatory fields are missing an null object with a message " NEW PASSWORD WAS NOT ENTERED " or " OLD PASSWORD WAS NOT ENTERED " is returned.
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	@RequestMapping(produces =MediaType.APPLICATION_JSON_VALUE,method=RequestMethod.POST,value="/adminPasswordChange",consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Admin> adminPasswordChange(@RequestBody Admin admin) 
	{		
		try {
			service.adminChangePassword(admin);
		}
		catch (Exception e) {
			Admin adminReturn=new Admin();
			adminReturn.setMessage(e.getMessage());
			return new ResponseEntity<Admin>(adminReturn,HttpStatus.BAD_REQUEST);
		}
			admin.setMessage(" PASSWORD UPDATED SUCCESSFULLY ");
			return new ResponseEntity<Admin>(admin,HttpStatus.OK);
		}
	
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
//		Returns the User details(password not included because of privacy concerns) 	
//		if user does not exist returns null object of user with message= "NO SUCH USER EXIST"	
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	@RequestMapping(produces =MediaType.APPLICATION_JSON_VALUE,method=RequestMethod.GET,value="/getUserDetails/{phoneNo}",consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<User> getUserDetails(@PathVariable long phoneNo) 
	{	
		try {
			User user=service.getUserDetails(phoneNo);
			return new ResponseEntity<User>(user,HttpStatus.OK);
			}
		catch(Exception e){
			User userReturn=new User();
			userReturn.setMessage(e.getMessage());
			return new ResponseEntity<User>(userReturn,HttpStatus.BAD_REQUEST);
		}		
	}
	
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//		Sends A mail of account creation to the admin's email provided while creating account,mail has the adminId and Password 
//		this method will be accessible by other Admin's only they will be able to add new admin 			
//		On violating Mandatory fields message sent back "PHONE NUMBER IS MANDATORY FIELD","EMAIL IS MANDATORY FIELD"
//		If unable to create account message sent "UNABLE TO CREATE A PROFILE FOR ADMIN"	
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	@RequestMapping(produces =MediaType.APPLICATION_JSON_VALUE,method=RequestMethod.POST,value="/addNewAdmin",consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Admin> addNewAdmin(@RequestBody Admin admin) {
		try {
			service.addNewAdmin(admin);
			admin.setMessage(" ADMIN ACCOUNT CREATED SUCCESSFULLY ");
			return new ResponseEntity<Admin>(admin,HttpStatus.OK);
		}
		catch(Exception e) {
			Admin adminReturn=new Admin();
			adminReturn.setMessage(e.getMessage());
			return new ResponseEntity<Admin>(adminReturn,HttpStatus.BAD_REQUEST);
		
		}
	}
	
	
}
