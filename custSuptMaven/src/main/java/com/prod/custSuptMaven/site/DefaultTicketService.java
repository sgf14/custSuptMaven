package com.prod.custSuptMaven.site;
/*class notes: chap 14, pg 393.  significant modifications in Persistence chap 21, pg 630
 * Below (spring) MVC model, the C is subdivided to CSR on the server side. the ..Service classes contain the business
 * logic handed to the controller and Services get their data from the R- Repository layer.
 * the Default implementation extends the ..Service class Interface.  the base service instantiates the methods- and any commonly used
 * business rules and the DefaultxxService implements those in the default case.  If non-default cases are needed a new class can be created
 * and extends the same interface in order to make the app more easily extendable.
 * 
 * the Persistence changes use the @Inject annotations to access the respective Repositories
 * 
 * Significant changes to this service class in chap 22 to take advantage of springDataJpa- as described in pg 632/656
 */
import com.prod.custSuptMaven.site.entities.Attachment;
import com.prod.custSuptMaven.site.entities.TicketEntity;
import com.prod.custSuptMaven.site.entities.TicketCommentEntity;
import com.prod.custSuptMaven.site.entities.UserPrincipal;
import com.prod.custSuptMaven.site.repositories.AttachmentRepository;
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
    	List<Ticket> list = new ArrayList<>();
        this.ticketRepository.findAll().forEach(e -> list.add(this.convert(e)));
        return list;
    }

    @Override
    @Transactional
    public Ticket getTicket(long id)
    {
    	TicketEntity entity = this.ticketRepository.findOne(id);
        return entity == null ? null : this.convert(entity);
    }
    
  //the convert method was added as part of persistence changes
    private Ticket convert(TicketEntity entity)
    {
        Ticket ticket = new Ticket();
        ticket.setId(entity.getId());
        ticket.setCustomerName(
                this.userRepository.findOne(entity.getUserId()).getUsername()
        );
        ticket.setSubject(entity.getSubject());
        ticket.setBody(entity.getBody());
        ticket.setDateCreated(Instant.ofEpochMilli(
                entity.getDateCreated().getTime()
        ));
        this.attachmentRepository.getByTicketId(entity.getId())
                .forEach(ticket::addAttachment);
        return ticket;
    }

    @Override
    @Transactional
    public void save(Ticket ticket)
    {
    	TicketEntity entity = new TicketEntity();
        entity.setId(ticket.getId());
        entity.setUserId(this.userRepository.getByUsername(
                ticket.getCustomerName()
        ).getId());
        entity.setSubject(ticket.getSubject());
        entity.setBody(ticket.getBody());

        if(ticket.getId() < 1)
        {
            ticket.setDateCreated(Instant.now());
            entity.setDateCreated(new Timestamp(
                    ticket.getDateCreated().toEpochMilli()
            ));
            this.ticketRepository.save(entity);
            ticket.setId(entity.getId());
            for(Attachment attachment : ticket.getAttachments())
            {
                attachment.setTicketId(entity.getId());
                this.attachmentRepository.save(attachment);
            }
        }
        else
            this.ticketRepository.save(entity);
    }
    
    //the delete method was added in Persistence changes- note that TicketService interface needed to add this new method as well
    //(you cant override something that isnt there.)
    @Override
    @Transactional
    public void deleteTicket(long id) {
    	this.ticketRepository.delete(id);
    }
    
    @Override
    @Transactional
    public Page<TicketComment> getComments(long ticketId, Pageable page)
    {
        List<TicketComment> comments = new ArrayList<>();
        Page<TicketCommentEntity> entities =
                this.commentRepository.getByTicketId(ticketId, page);
        entities.forEach(e -> comments.add(this.convert(e)));

        return new PageImpl<>(comments, page, entities.getTotalElements());
    }

    private TicketComment convert(TicketCommentEntity entity)
    {
        TicketComment comment = new TicketComment();
        comment.setId(entity.getId());
        comment.setCustomerName(
                this.userRepository.findOne(entity.getUserId()).getUsername()
        );
        comment.setBody(entity.getBody());
        comment.setDateCreated(Instant.ofEpochMilli(
                entity.getDateCreated().getTime()
        ));

        return comment;
    }

    @Override
    @Transactional
    public void save(TicketComment comment, long ticketId)
    {
        TicketCommentEntity entity = new TicketCommentEntity();
        entity.setId(comment.getId());
        entity.setTicketId(ticketId);
        entity.setUserId(this.userRepository.getByUsername(
                comment.getCustomerName()
        ).getId());
        entity.setBody(comment.getBody());

        if(comment.getId() < 1)
        {
            comment.setDateCreated(Instant.now());
            entity.setDateCreated(new Timestamp(
                    comment.getDateCreated().toEpochMilli()
            ));
            this.commentRepository.save(entity);
            comment.setId(entity.getId());
        }
        else
            this.commentRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteComment(long id)
    {
        this.commentRepository.delete(id);
    }
    
 }    
    
