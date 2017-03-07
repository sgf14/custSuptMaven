package com.prod.custSuptMaven.site;
/* class supports log4j logging,chapter 11.  requires some changes to Configurator.java also.  see pg 319 along w/ 
 * src/main/resources/log4j2.xml file and pg 313.
 * from customer-support-v9 import plus some file modifications as noted 
 * 
 * chap 26, pg 767 implementing Spring Security requires LoggingFilter class to be split into a Pre and Post classes
 * because the fish tagging requires the use of a UUID- which is not available until SpringSec filter chain executes
 */
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class PostSecurityLoggingFilter implements Filter {
	@Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException
    {
		SecurityContext context = SecurityContextHolder.getContext();
		if(context != null && context.getAuthentication() != null)
			ThreadContext.put("username", context.getAuthentication().getName());
		
		chain.doFilter(request, response);

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {

    }

    @Override
    public void destroy()
    {

    }

}
