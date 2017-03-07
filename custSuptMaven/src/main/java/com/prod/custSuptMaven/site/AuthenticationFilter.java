package com.prod.custSuptMaven.site;
//class mostly left intact upon spring conversion, but some differences see customer-support-v8 for comparison.  this 
//incorporates Principle class and methods
//class added by chap 8 Filters to clean up duplicated login check code in JSPs- pg 254 
import com.prod.custSuptMaven.site.entities.UserPrincipal;
import java.io.IOException;
import java.security.Principal;

import javax.servlet.*;
import javax.servlet.http.*;

public class AuthenticationFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest)request).getSession(false);
		final Principal principal = UserPrincipal.getPrincipal(session);
        if(principal == null)
        {
            ((HttpServletResponse)response).sendRedirect(
                    ((HttpServletRequest) request).getContextPath() + "/login"
            );
        }
        else
        {
            chain.doFilter(
                    new HttpServletRequestWrapper((HttpServletRequest)request)
                    {
                        @Override
                        public Principal getUserPrincipal() {
                            return principal;
                        }
                    },
                    response
            );
        }
	}
	
	@Override
	public void init(FilterConfig config) throws ServletException{
		
	}
	
	@Override
	public void destroy() {
		
	}

}
