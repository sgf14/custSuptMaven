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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultTicketService implements TicketService
{
    @Inject TicketRepository ticketRepository;
    @Inject TicketCommentRepository commentRepository;
    @Inject AttachmentRepository attachmentRepository;
    @Inject UserRepository userRepository;

    @Override
    @Transactional
    public List<Ticket> getAllTickets()
    {
    	Iterable<Ticket> i = this.ticketRepository.findAll();
        List<Ticket> l = new ArrayList<>();
        i.forEach(l::add);
        return l;
    }
    
    @Override
    @Transactional
    public Page<SearchResult<Ticket>> search(String query,
    										boolean useBooleanMode,
    										Pageable pageable) {
    	return this.ticketRepository.search(query, useBooleanMode, pageable);
    }

    @Override
    @Transactional
    public Ticket getTicket(long id)
    {
    	Ticket ticket = this.ticketRepository.findOne(id);
        ticket.getNumberOfAttachments();
        return ticket;
    }
    
  //note the earlier chapter convert method (private method) that was here removed entirely based on chap 24 attachment changes
    
    @Override
    @Transactional
    public void save(Ticket ticket)
    {
    	if(ticket.getId() < 1)
            ticket.setDateCreated(Instant.now());

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
    public Page<TicketComment> getComments(long ticketId, Pageable pageable)
    {
    	return this.commentRepository.getByTicketId(ticketId, pageable);
    }

    //as with above private convert method that was here removed entirely based on chap 24 (-v18)changes
    
    @Override
    @Transactional
    public void save(TicketComment comment, long ticketId)
    {
    	if(comment.getId() < 1)
        {
            comment.setTicketId(ticketId);
            comment.setDateCreated(Instant.now());
        }

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
    
