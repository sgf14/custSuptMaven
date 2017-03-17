package com.prod.custSuptMaven.site;
/*class notes: this is the spring version of aurthenticationServlet.  uses @Controller annotation and is evaluated
this AuthenticationController replaced LoginServlet, per chap 13, pg385.  and is typicpal of Controllers replacing Servlets
by config package ServletContextConfiguration class- as with all the other controllers below.
w/i MVC framework the C is further subdivided into CSR, Controller-Service-Repository per chap 14, pg 390.
w/i CSR Controllers handle interface logic, Services provide business logic and Repositories provide database/persistence logic.
the benefit of this is new interfaces- desktop, mobile tablet, etc- all reside in one class w/ purely business logic handled by Services.
this is an optional architecture designed to support more complex apps, and Spring does not enforce its usage, but it is an option
to manage complexity by breaking it into smaller pieces.
the in memory user db in LoginServlet got moved to its own Repository class

Chap 26 Spring Security- note this class was heavily refactored- and simplified- to implement the spring security code.  
See pg 774-75.  see github chap 21 ver for prior versions
*/
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.prod.custSuptMaven.config.annotation.WebController;
import com.prod.custSuptMaven.site.entities.UserPrincipal;

import java.util.Map;

//this authentication group more heavily commented than others to document CSR pattern (although Authentication doesnt have its own R- see ticket..) 
//Interface logic
@WebController
public class AuthenticationController
{
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public ModelAndView login(Map<String, Object> model)
    {
        if(SecurityContextHolder.getContext().getAuthentication() instanceof
        		UserPrincipal)
        	return new ModelAndView(new RedirectView("/ticket/list", true, false));
        
        model.put("loginForm", new LoginForm());
        return new ModelAndView("login");
    }
    
    public static class LoginForm
    {
    	private String username;
    	private String password;

        public String getUsername()
        {
            return username;
        }

        public void setUsername(String username)
        {
            this.username = username;
        }

        public String getPassword()
        {
            return password;
        }

        public void setPassword(String password)
        {
            this.password = password;
        }
    }
}
