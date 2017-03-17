package com.prod.custSuptMaven.config.bootstrap;
/* class notes= Added as part of chap 26 Spring security to enable logging and fish tag to setup properly
 * chap 26 also instantiated bootstrap package w/ @Order annotation
 */
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;

import com.prod.custSuptMaven.site.PostSecurityLoggingFilter;

@SuppressWarnings("unused")
@Order(3)
public class LoggingBootstrap implements WebApplicationInitializer {
	private static final Logger log = LogManager.getLogger();
	
	@Override
	public void onStartup(ServletContext container) throws ServletException {
		log.info("Executing logging bootstrap");
		
		FilterRegistration.Dynamic registration = container.addFilter(
				"postSecurityLoggingFilter", new PostSecurityLoggingFilter()
		);
		registration.addMappingForUrlPatterns(null, false, "/*");
	}

}
