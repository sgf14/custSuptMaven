package com.prod.custSuptMaven.site.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.prod.custSuptMaven.site.entities.TicketComment;

public interface TicketCommentRepository
        extends CrudRepository<TicketComment, Long>
{
    Page<TicketComment> getByTicketId(long ticketId, Pageable p);
}
