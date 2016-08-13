package com.prod.custSuptMaven;
//class added by chap 8 Filters to clean up duplicated login check code in java web servlets- pg 254 
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Configurator implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		
		FilterRegistration.Dynamic registration = context.addFilter(
				"authenticationFilter", new AuthenticationFilter());
		registration.setAsyncSupported(true);
		//if more @WebServlet pages are added just add their URL patterns to the list below
		//only affects TicketServlet and SessionListener servlets at this time- see pg 255
		registration.addMappingForUrlPatterns(null, false, "/sessions", "/tickets");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		
	}

}
