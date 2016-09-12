package com.prod.custSuptMaven;
// class supports log4j logging,chapter 11.  requires some changes to Configurator.java also.  see pg 319 along w/ 
// src/main/resources/log4j2.xml file and pg 313.
// from customer-support-v9 import plus some file modifications as noted 
import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.ThreadContext;

public class LoggingFilter implements Filter {
	@Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException
    {
        boolean clear = false;
        if(!ThreadContext.containsKey("id")) {
            clear = true;
            ThreadContext.put("id", UUID.randomUUID().toString());
            HttpSession session = ((HttpServletRequest)request).getSession(false);
            if(session != null)
                ThreadContext.put("username",
                        (String)session.getAttribute("username"));
        }

        try {
            chain.doFilter(request, response);
        } finally {
            if(clear)
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
