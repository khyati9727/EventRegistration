package com.project.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;



public class WebIntializer implements WebApplicationInitializer {

	public AnnotationConfigWebApplicationContext webApplicationContext = null;
	public void onStartup(ServletContext servletContext) throws ServletException {
		
		webApplicationContext= new AnnotationConfigWebApplicationContext();
		webApplicationContext.register(DispatcherConfig.class);
		webApplicationContext.setServletContext(servletContext);
		ServletRegistration.Dynamic myCustomDispatcherServlet=servletContext.addServlet("MyDispatcherServlet",new DispatcherServlet(webApplicationContext));
		myCustomDispatcherServlet.setLoadOnStartup(1);
		myCustomDispatcherServlet.addMapping("/");
	}


}
