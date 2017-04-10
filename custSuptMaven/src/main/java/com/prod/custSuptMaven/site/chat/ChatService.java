package com.prod.custSuptMaven.site.chat;
/* class notes: chat package introduced chap 10 websockets
 * refactored by chap 14 pg412 Spring MVC-CSR architecture - service interface [business logic] for chat function
 * refactored chap 27 pg 810/11 Spring Authorization to allow user level access to different components
 */
import java.util.List;

import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;

public interface ChatService
{
    /* chap 27 pg 810 similar to TicketService class, PreAuthorize is a Spring security authorization annotation
	 * restricting access to the method via the business logic (Service) class of the related function.
	 * see pg 788 for detailed description of method portions inside parenthesis
	 * Note that to make the '#name' work in PreAuthorize variable the assoc method variable needs to be decorated with @P 
	 * and the matching 'name' ie ...@P("name") String name...
	 * see pg 789 for usage
	 */
	@PreAuthorize("authentication.principal.username.equals(#user) and "
    		+ "hasAuthority('CREATE_CHAT_REQUEST')")
	CreateResult createSession(@P("user") String user);
    
    @PreAuthorize("authentication.principal.username.equals(#user) and " +
    		"hasAuthority('START_CHAT')")
    JoinResult joinSession(long id, @P("user") String user);
    
    @PreAuthorize("authentication.principal.username.equals(#user) and " +
    		"(#user.equals(#session.customerUsername) or " +
    		"#user.equals(#session.representativeUsername)) and " +
    		"hasAuthority('CHAT')")
    ChatMessage leaveSession(@P("session") ChatSession session, 
    						 @P("user") String user,
                             ReasonForLeaving reason);
    
    @PreAuthorize("authentication.principal.username.equals(#message.user) and " + 
    				"(#message.user.equals(#session.customerUsername) or " + 
    				"#message.user.equals(#session.representativeUsername)) and " +
    				"hasAuthority('CHAT')")
    void logMessage(@P("session") ChatSession session, 
    		@P("message") ChatMessage message);
    @PreAuthorize("hasAuthority('VIEW_CHAT_REQUESTS')")
    List<ChatSession> getPendingSessions();

    public static enum ReasonForLeaving
    {
        NORMAL,

        LOGGED_OUT,

        ERROR
    }
}
