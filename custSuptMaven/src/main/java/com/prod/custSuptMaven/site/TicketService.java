package com.prod.custSuptMaven.site;
/* class notes. Orig introduced in chap 14, pg 398.  Service implementation that is extend by appropriate DefaultXx class. 
 * setup as interface so it is extendible to non-Default classes later, as app grows.
 * This is the S in CSR [Controller-Service-Repository] of MVC model. Service = business logic of app
 * Note also there is no @Service annotation on interface- that is on the class that implements it (DefaultTicketService in this case)
 */
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import com.prod.custSuptMaven.site.entities.Attachment;
import com.prod.custSuptMaven.site.entities.Ticket;
import com.prod.custSuptMaven.site.entities.TicketComment;
import com.prod.custSuptMaven.site.repositories.SearchResult;
import com.prod.custSuptMaven.site.validation.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.function.Consumer;

@Validated
public interface TicketService
{
    //CRUD ops and later Search ops for UI and fed to Controller (via DefaultXx implementation only in this app)
	@NotNull
	//this annotation introduced by chap 27 Spring security authorization, pg 809, to allow user level authorizations
	// similar PreAuthorize on remaining service methods below also
	@PreAuthorize("hasAuthority('VIEW_TICKETS')")
    List<Ticket> getAllTickets(TicketExpand... expand);
	
	//search method added in chap 23,  used for fulltext style search in this case. see pg 682
	@NotNull
	@PreAuthorize("hasAuthority('VIEW_TICKETS')")
	Page<SearchResult<Ticket>> search(
			@NotBlank(message = "{validate.ticketService.search.query}")
				String query,
			boolean useBooleanMode,
			@NotNull(message = "{validate.ticketService.search.page}")
				Pageable pageable,
				TicketExpand...expand
			);
	
	
	@PreAuthorize("hasAuthority('VIEW_TICKETS')")
    Ticket getTicket(
            @Min(value = 1L, message = "{validate.ticketService.getTicket.id}")
                long id,
            TicketExpand... expand
    );
	
	//chap 27 no requires normal save method to be split into a create and update set- since different user functions
	//are allowed by different users- see pg 809.  ie users can edit their own tickets, but not others.  admins can do either
	
    //pre chap 27
	//void save(@NotNull(message = "{validate.ticketService.save.ticket}")
    //          @Valid Ticket ticket);
	
	// chap 27 changes to save method into create and update
	@PreAuthorize("#ticket.id == 0 and hasAuthority('CREATE_TICKET')")
	void create(@NotNull(message = "{validate.ticketService.save.ticket}")
				@Valid @P("ticket") Ticket ticket 
	);
	
	@PreAuthorize("(authentication.principal.equals(#ticket.customer) and "
			+ "hasAuthority('EDIT_OWN_TICKET')) or hasAuthority('EDIT_ANY_TICKET')")
	void update(@NotNull(message = "{validate.ticketService.save.ticket}")
				@Valid @P("ticket") Ticket ticket 
	);
	
	@PreAuthorize("hasAuthority('DELETE_TICKET')")
    void deleteTicket(long id);
    
    @NotNull
    @PreAuthorize("hasAuthority('VIEW_COMMENTS')")
    Page<TicketComment> getComments(
            @Min(value = 1L, message = "{validate.ticketService.getComments.id}")
                long ticketId,
            @NotNull(message = "{validate.ticketService.getComments.page}")
                Pageable page,
            CommentExpand... expand 
    );
    //pre chap 27 version- similar to above now has create/update methods instead of save
//    void save(
//            @NotNull(message = "{validate.ticketService.save.comment}")
//            @Valid TicketComment comment,
//            @Min(value = 1L, message = "{validate.ticketService.saveComment.id}")
//                long ticketId
//    );
    
    @PreAuthorize("#comment.id == 0 and hasAuthority('CREATE_COMMENT')")
    void create(
    		@NotNull(message = "{validate.ticketService.save.comment}")
    		@Valid @P("comment") TicketComment comment,
    		@Min(value = 1L, message = "{validate.ticketService.saveComment.id}")
    			long ticketId
    );
    
    @PreAuthorize("(authentication.principal.equals(#comment.customer) and " +
    		"hasAuthority('EDIT_OWN_COMMENT')) or " +
    		"hasAuthority('EDIT_ANY_TICKET')")
    void update(@NotNull(message = "{validate.ticketService.save.comment}")
    			@Valid @P("comment") TicketComment comment);
    
    @PreAuthorize("hasAuthority('DELETE_COMMENT')")
    void deleteComment(long id);
    
    //added by chap 24 after attachment re factored into sepr tables
    @PreAuthorize("hasAuthority('VIEW_ATTACHMENT')")
    Attachment getAttachment(long id);
    
    //added by chap 28- oauth- and interfaces used above
    @FunctionalInterface
    public static interface TicketExpand extends Consumer<Ticket> { }

    @FunctionalInterface
    public static interface CommentExpand extends Consumer<TicketComment> { }
}

