package com.prod.custSuptMaven.site;
/*class notes: chap 14, pg 393.  significant modifications in Persistence chap 21, pg 630.  includes both ticket and newly added ticketComment
 * Below (spring) MVC model, the C is subdivided to CSR on the server side. the ..Service classes contains the business logic 
 * handed to the controller and Services get their data from the R- Repository layer.
 * the Default implementation extends the ..Service class Interface.  the base service interface instantiates the methods- and any commonly used
 * business rules and the DefaultxxService implements those in the default case.  If non-default cases are needed a new class can be created
 * and extends the same interface in order to make the app more easily extendible.
 * 
 * the Persistence changes use the @Inject annotations to access the respective Repositories
 * 
 * Significant changes to this service class in chap 22 to take advantage of springDataJpa- as described in pg 632/656
 * 
 * chap 23- added new search method for fulltext search function
 * 
 * chap 24- with refactoring of attachments significant changes made to this class.  see git hub version changes.  not described in detail
 * in book itself, since subjects as a whole were already covered. compar book version of customer-support-v17 vs chap 24 xx-v18
 */
import com.prod.custSuptMaven.site.entities.Attachment;
import com.prod.custSuptMaven.site.entities.Ticket;
import com.prod.custSuptMaven.site.entities.TicketComment;
import com.prod.custSuptMaven.site.repositories.AttachmentRepository;
import com.prod.custSuptMaven.site.repositories.SearchResult;
import com.prod.custSuptMaven.site.repositories.TicketCommentRepository;
import com.prod.custSuptMaven.site.repositories.TicketRepository;
import com.prod.custSuptMaven.site.repositories.UserRepository;

// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultTicketService implements TicketService
{
	// private static final Logger log = LogManager.getLogger();
	
	@Inject TicketRepository ticketRepository;
    @Inject TicketCommentRepository commentRepository;
    @Inject AttachmentRepository attachmentRepository;
    @Inject UserRepository userRepository;

    @Override
    @Transactional
    public List<Ticket> getAllTickets(TicketExpand...expands )
    {
    	Iterable<Ticket> iterable = this.ticketRepository.findAll();    	
        List<Ticket> list = new ArrayList<>();
        iterable.forEach(t -> {
        	for(TicketExpand expand : expands)
        		expand.accept(t);
        	list.add(t);
        });
        return list;
    }
    
    @Override
    @Transactional
    public Page<SearchResult<Ticket>> search(String query,
    										boolean useBooleanMode,
    										Pageable pageable,
    										TicketExpand... expands) {
    	Page<SearchResult<Ticket>> results = this.ticketRepository
    			.search(query, useBooleanMode, pageable);
    	for(TicketExpand expand : expands)
    		results.getContent().forEach(r -> expand.accept(r.getEntity()));
    	return results;
    }

    @Override
    @Transactional
    public Ticket getTicket(long id, TicketExpand... expands)
    {
    	Ticket ticket = this.ticketRepository.findOne(id);
        ticket.getNumberOfAttachments();
        for(TicketExpand expand : expands)
        	expand.accept(ticket);
        return ticket;
    }
    
  //note the earlier chapter convert method (private method) that was here removed entirely based on chap 24 attachment changes
    //this original [pre chap 27] method got changed to a create and update- see TicketService interface
//    @Override
//    @Transactional
//    public void save(Ticket ticket)
//    {
//    	if(ticket.getId() < 1)
//            ticket.setDateCreated(Instant.now());
//
//        this.ticketRepository.save(ticket);
//    }
    
    @Override
    @Transactional
    public void create(Ticket ticket) {
    	ticket.setDateCreated(Instant.now());    	
    	this.ticketRepository.save(ticket);
    }
    
    @Override
    @Transactional
    public void update(Ticket ticket) {
    	this.ticketRepository.save(ticket);
    }
    
    //the delete method was added in Persistence changes- note that TicketService interface needed to add this new method as well
    //(you cant override something that isnt there.)
    @Override
    @Transactional
    public void deleteTicket(long id) {
    	this.ticketRepository.delete(id);
    }
    
    //ticketComment blocks
    @Override
    @Transactional
    public Page<TicketComment> getComments(long ticketId, Pageable pageable,
    										CommentExpand... expands)
    {
    	Page<TicketComment> results = this.commentRepository
    			.getByTicketId(ticketId, pageable);
    	for(CommentExpand expand : expands)
    		results.getContent().forEach(expand);
    	return results;
    }

    //as with above private convert method that was here removed entirely based on chap 24 (-v18)changes
    //save as above- chap 27 spring security authorization replaced save with create and update
//    @Override
//    @Transactional
//    public void save(TicketComment comment, long ticketId)
//    {
//    	log.info("Entered ticket comment {} save.", ticketId);
//    	if(comment.getId() < 1)
//        {
//            comment.setTicketId(ticketId);
//            comment.setDateCreated(Instant.now());
//        }
//
//        this.commentRepository.save(comment);
//    }
    
    @Override
    @Transactional
    public void create(TicketComment comment, long ticketId) {
    	comment.setTicketId(ticketId);
    	comment.setDateCreated(Instant.now());
    	this.commentRepository.save(comment);
    }
    
    @Override
    @Transactional
    public void update(TicketComment comment){
    	this.commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(long id)
    {
        this.commentRepository.delete(id);
    }
    
    @Override
    @Transactional
    public Attachment getAttachment(long id)
    {
        Attachment attachment = this.attachmentRepository.findOne(id);
        if(attachment != null)
            attachment.getContents();
        return attachment;
    }
    
 }    
    
