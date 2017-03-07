package com.prod.custSuptMaven.site;
// class supports log4j logging,chapter 11.  requires some changes to Configurator.java also.  see pg 319 along w/ 
// src/main/resources/log4j2.xml file and pg 313.
// from customer-support-v9 import plus some file modifications as noted 
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.ThreadContext;
import com.prod.custSuptMaven.site.entities.UserPrincipal;

public class LoggingFilter implements Filter {
	@Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException
    {
		String id = UUID.randomUUID().toString();
        ThreadContext.put("id", id);
        Principal principal = UserPrincipal.getPrincipal(
                ((HttpServletRequest)request).getSession(false)
        );
        if(principal != null)
            ThreadContext.put("username", principal.getName());

        try
        {
            ((HttpServletResponse)response).setHeader("X-Wrox-Request-Id", id);
            chain.doFilter(request, response);
        }
        finally
        {
            ThreadContext.clearAll();
        }

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
