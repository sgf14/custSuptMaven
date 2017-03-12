package com.prod.custSuptMaven.site;
/*part of MVC-CSR- this Controller(C in CSR) replaced SessionListServlet typical of Spring Controllers replacing servlet classes
 *see chap 13, pg 385
 * Note in chap 26- pg 772-74 spring security this whole structure (CSR) was refactored and several Session... classes were deleted to 
 * take advantage of sessionRegistryImpl of Spring Security, that in previous chapters had been built manually as separate classes
 * which the SessionListController class extended.  those classes are now gone.  See github past versions or -v18 book example
 */

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.prod.custSuptMaven.config.annotation.WebController;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebController
@RequestMapping("session")
public class SessionListController
{
    //for chap 26 note this is now SpringSecurity sessionRegistry.  the manually built app class version from earlier chapters is deleted
	@Inject SessionRegistry sessionRegistry;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(Map<String, Object> model)
    {
        //chap 26 these modified for spring security- chap 26, pg 773
    	List<SessionInformation> sessions = new ArrayList<>();
        for(Object principal : this.sessionRegistry.getAllPrincipals())
        	sessions.addAll(this.sessionRegistry.getAllSessions(principal, true));
    	
        //these from before are similar, but now uses sessions attr
    	model.put("timestamp", System.currentTimeMillis());
        model.put("numberOfSessions", sessions.size());
        model.put("sessionList", sessions);

        return "session/list";
    }
}
