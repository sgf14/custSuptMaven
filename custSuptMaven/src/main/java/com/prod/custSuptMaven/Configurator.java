package com.prod.custSuptMaven;
//class added by chap 8 Filters to clean up duplicated login check code in java web servlets- pg 254 
//when new pages created in app you will want to add them the URL list below so they are login protected
//see chapter 10 (customer-support-v8 project) pg 289 as an example with the websockets chat functionality.
// the urls are housed in the different XxServlet.java classes for the different pages via the @WebServer annotation.
import java.util.EnumSet;

import javax.servlet.DispatcherType;
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
                "loggingFilter", new LoggingFilter()
        );
        registration.addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.INCLUDE,
                        DispatcherType.FORWARD, DispatcherType.ERROR),
                false, "/*"
        );

        registration = context.addFilter(
                "authenticationFilter", new AuthenticationFilter()
        );
		//if more @WebServlet pages are added just add their URL patterns to the list below
		//only affects TicketServlet and SessionListener servlets at this time- see pg 255
		registration.addMappingForUrlPatterns(null, false, "/sessions", "/tickets", "/chat");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		
	}

}
