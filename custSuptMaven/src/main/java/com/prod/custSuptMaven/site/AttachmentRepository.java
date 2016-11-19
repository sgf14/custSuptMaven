package com.prod.custSuptMaven.site;
/*class notes- JWA chap 21, pg 627.  similar for all persisted data (ie each class in entities package has a MySQL table) 
 * each Repository extends GenericRepository class for CRUD functions 
 * and each DefaultxxRepository extends GenericJpaRepo. to iterate over the ids
 */
import com.prod.custSuptMaven.site.GenericRepository;
import com.prod.custSuptMaven.site.entities.Attachment;

public interface AttachmentRepository extends GenericRepository<Long, Attachment>
{
    Iterable<Attachment> getByTicketId(long ticketId);
}
