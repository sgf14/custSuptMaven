package com.prod.custSuptMaven.config.bootstrap;
/* class notes; introcuded chap 26, pg 769- Spring Security as part of bootstrap refactoring
 * 
 */
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

@Order(2)
public class SecurityBootstrap extends AbstractSecurityWebApplicationInitializer{
	private static final Logger log = LogManager.getLogger();
	
	@Override
	protected boolean enableHttpSessionEventPublisher() {
		log.info("Executing security bootstrap");
		return true;
	}

}
