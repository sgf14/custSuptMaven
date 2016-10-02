package com.prod.custSuptMaven;
//class added by chap 8 Filters to clean up duplicated login check code in JSPs- pg 254 
import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

public class AuthenticationFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest)request).getSession(false);
		if(session != null && session.getAttribute("username") == null)
			((HttpServletResponse)response).sendRedirect("login");
		else
			chain.doFilter(request, response);
	}
	
	@Override
	public void init(FilterConfig config) throws ServletException{
		
	}
	
	@Override
	public void destroy() {
		
	}

}
