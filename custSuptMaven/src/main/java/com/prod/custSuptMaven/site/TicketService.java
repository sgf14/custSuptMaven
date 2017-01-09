package com.prod.custSuptMaven.site;
/* class notes. Orig introduced in chap 14, pg 398.  Service implementation that is extend by appropriate DefaultXx class. 
 * setup as interface so it is extendible to non-Default classes later, as app grows.
 * This is the S in CSR [Controller-Service-Repository] of MVC model. Service = business logic of app
 * Note also there is no @Service annotation on interface- that is on the class that implements it (DefaultTicketService in this case)
 */
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import com.prod.custSuptMaven.site.TicketComment;
import com.prod.custSuptMaven.site.repositories.SearchResult;
import com.prod.custSuptMaven.site.validation.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.List;

@Validated
public interface TicketService
{
    //CRUD ops and later Search ops for UI and fed to Controller (via DefaultXx implementation only in this app)
	@NotNull
    List<Ticket> getAllTickets();
	
	//search method added in chap 23,  used for fulltext style search in this case. see pg 682
	@NotNull
	Page<SearchResult<Ticket>> search(
			@NotBlank(message = "{validate.ticketService.search.query}")
				String query,
			boolean useBooleanMode,
			@NotNull(message = "{validate.ticketService.search.page}")
				Pageable pageable
			);
	
    Ticket getTicket(
            @Min(value = 1L, message = "{validate.ticketService.getTicket.id}")
                long id
    );
    void save(@NotNull(message = "{validate.ticketService.save.ticket}")
              @Valid Ticket ticket);
    void deleteTicket(long id);
    
    @NotNull
    Page<TicketComment> getComments(
            @Min(value = 1L, message = "{validate.ticketService.getComments.id}")
                long ticketId,
            @NotNull(message = "{validate.ticketService.getComments.page}")
                Pageable page
    );
    void save(
            @NotNull(message = "{validate.ticketService.save.comment}")
            @Valid TicketComment comment,
            @Min(value = 1L, message = "{validate.ticketService.saveComment.id}")
                long ticketId
    );
    void deleteComment(long id);
}

