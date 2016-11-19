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
*/
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.prod.custSuptMaven.config.annotation.WebController;
import com.prod.custSuptMaven.site.validation.NotBlank;
import com.prod.custSuptMaven.site.entities.UserPrincipal;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;

import java.security.Principal;
import java.util.Map;

//this authentication group more heavily commented than others to document CSR pattern (although Authentication doesnt have its own R- see ticket..) 
//Interface logic
@WebController
public class AuthenticationController
{
    private static final Logger log = LogManager.getLogger();

    @Inject AuthenticationService authenticationService;
    /*Spring request mapping annotation- pg 356 intro.  within the class annot will typically 
    correspond to a method- as in this case- will map to a URL portion via ViewResolver in config package 
    when url is called this method is then executed.
    */ 
    @RequestMapping("logout")
    public View logout(HttpServletRequest request, HttpSession session)
    {
        if(log.isDebugEnabled() && request.getUserPrincipal() != null)
            log.debug("User {} logged out.", request.getUserPrincipal().getName());
        session.invalidate();
        //redirect to proper jsp view
        return new RedirectView("/login", true, false);
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public ModelAndView login(Map<String, Object> model, HttpSession session)
    {
        if(UserPrincipal.getPrincipal(session) != null)
            return this.getTicketRedirect();

        model.put("loginFailed", false);
        model.put("loginForm", new LoginForm());

        return new ModelAndView("login");
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ModelAndView login(Map<String, Object> model, HttpSession session,
                              HttpServletRequest request, LoginForm form, Errors errors)
    {
        if(UserPrincipal.getPrincipal(session) != null)
            return this.getTicketRedirect();

        if(errors.hasErrors())
        {
            form.setPassword(null);
            return new ModelAndView("login");
        }

        Principal principal;
        try
        {
            principal = this.authenticationService.authenticate(
                    form.getUsername(), form.getPassword()
            );
        }
        catch (ConstraintViolationException e)
        {
            form.setPassword(null);
            model.put("validationErrors", e.getConstraintViolations());
            return new ModelAndView("login");
        }
        
        if(principal == null)
        {
            form.setPassword(null);
            model.put("loginFailed", true);
            model.put("loginForm", form);
            return new ModelAndView("login");
        }

        UserPrincipal.setPrincipal(session, principal);
        request.changeSessionId();
        return this.getTicketRedirect();
    }

    private ModelAndView getTicketRedirect()
    {
        return new ModelAndView(new RedirectView("/ticket/list", true, false));
    }

    public static class LoginForm
    {
    	@NotBlank(message = "{validate.authenticate.username}")
    	private String username;
    	@NotBlank(message = "{validate.authenticate.password}")
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
