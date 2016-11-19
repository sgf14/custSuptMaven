package com.prod.custSuptMaven.site;
/* class notes- chap 21, pg 627 persistence- this is the default implementation of attachment entity. see AttachmentRepository class notes.
 * 
 */
import org.springframework.stereotype.Repository;
import com.prod.custSuptMaven.site.entities.Attachment;

@Repository
public class DefaultAttachmentRepository
        extends GenericJpaRepository<Long, Attachment>
        implements AttachmentRepository
{
    @Override
    public Iterable<Attachment> getByTicketId(long ticketId)
    {
        return this.entityManager.createQuery(
                "SELECT a FROM Attachment a WHERE a.ticketId = :id ORDER BY a.id",
                Attachment.class
        ).setParameter("id", ticketId).getResultList();
    }
}
