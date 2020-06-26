package com.project.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.project.entity.AdminEntity;
import com.project.entity.UserEntity;
import com.project.model.Admin;
import com.project.model.User;
import com.project.utilities.JavaMailUtil;

@Repository(value ="dao")
public class CredentialDAOImpl implements CredentialDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	public Admin adminLogin(Admin admin)  {
		
		Session session=sessionFactory.getCurrentSession();
		AdminEntity adminE=session.get(AdminEntity.class, admin.getAdminId()); 
		if(adminE == null) {
			return null;
		}
		else {
			admin.setFullName(adminE.getFullName());;
			admin.setPassword(adminE.getPassword());
		}
		return admin;
	}
	
	public User userLogin(User user)  {
		
		Session session=sessionFactory.getCurrentSession();
		UserEntity userE=session.get(UserEntity.class, user.getPhoneNo()); 
		if(userE == null) {
			return null;
		}
		else {
			user.setFullName(userE.getFullName());
			user.setPassword(userE.getPassword());
		}
		return user;
	}
	

	public User userSignUp(User user) throws Exception  {
		
		Session session=sessionFactory.getCurrentSession();
		UserEntity userX=session.get(UserEntity.class,user.getPhoneNo());
		if(userX!=null) {
			return null;
		}		 
		UserEntity userE=new UserEntity();
		userE.setPhoneNo(user.getPhoneNo());
		userE.setFullName(user.getFullName());
		userE.setEmail(user.getEmail());
		if(user.getPassword()==null) {
			long min=100000;
	 		long max=999999999;
	 		long random_int = (long)(Math.random() * (max - min + 1) + min);
	 		userE.setPassword(Long.toString(random_int));
	 		user.setPassword(Long.toString(random_int));
		}else {
			userE.setPassword(user.getPassword());
			user.setPassword(null);
		}
		userE.setGender(user.getGender());
		session.persist(userE);
		session.flush();
		UserEntity userstored=session.get(UserEntity.class,user.getPhoneNo());
		if(userstored==null) {
			user.setPhoneNo(-1);
		}
		JavaMailUtil.sendMailUser(user.getEmail(),user.getPhoneNo(),user.getPassword());
		user.setPassword(null);
		return user;
		
	}
	
	public User userPasswordChange(User user)  {
		
		Session session=sessionFactory.getCurrentSession();
		UserEntity userE=session.get(UserEntity.class,user.getPhoneNo());
		if(userE==null) {
			return null;
		}
		if(!user.getPassword().equals(userE.getPassword())) {
			user.setPassword(null);
			return user;
		}
		userE.setPassword(user.getMessage());
		session.persist(userE);
		session.flush();
		return user;
	}

	public Admin adminPasswordChange(Admin admin)  {
		
		Session session=sessionFactory.getCurrentSession();
		AdminEntity adminE=session.get(AdminEntity.class,admin.getAdminId());
		if(adminE==null) {
			return null;
		}
		if(!admin.getPassword().equals(adminE.getPassword())) {
			admin.setPassword(null);
			return admin;
		}
		adminE.setPassword(admin.getMessage());
		session.persist(adminE);
		session.flush();
		return admin;
	}
	
		public User getUserDetails(long phoneNo)  {
		
		Session session=sessionFactory.getCurrentSession();
		UserEntity userE=session.get(UserEntity.class,phoneNo);
		if(userE==null) {
			return new User();
		}
		User user= new User();
		user.setPhoneNo(userE.getPhoneNo());
		user.setFullName(userE.getFullName());
		user.setEmail(userE.getEmail());
		user.setGender(userE.getGender());
		return user;
	}
	
		public Admin addNewAdmin(Admin admin) throws Exception {
			Session session=sessionFactory.getCurrentSession();
			
			long min=100000;
			long max=999999999;
			long random_int = (long)(Math.random() * (max - min + 1) + min);
										
			AdminEntity adminE=admin.admin(admin);
			adminE.setPassword(Long.toString(random_int));					//Setting Random Number as password and sending this Password to the email of the new Admin       
			session.persist(adminE);
			session.flush();
			AdminEntity adminCheck=session.get(AdminEntity.class,adminE.getAdminId());
			if(adminCheck==null) {
				return null;
			}
			else {
				JavaMailUtil.sendMailAdmin(adminCheck.getEmail(),adminCheck.getAdminId(),Long.toString(random_int));
				return admin;
			}
			
		}
		
		
}
