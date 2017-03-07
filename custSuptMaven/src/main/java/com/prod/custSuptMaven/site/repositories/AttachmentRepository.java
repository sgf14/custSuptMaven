package com.prod.custSuptMaven.site.repositories;
/*class notes- JWA chap 21, pg 627.  similar for all persisted data (ie each class in entities package has a MySQL table) 
 * each Repository extends GenericRepository class for CRUD functions 
 * and each DefaultxxRepository extends GenericJpaRepo. to iterate over the ids
 * 
 * chap 22 then later removes the GenericRepository interface and replaces it with CrudRepository used with Spring Jpa Data
 */
import org.springframework.data.repository.CrudRepository;

import com.prod.custSuptMaven.site.entities.Attachment;

public interface AttachmentRepository extends CrudRepository<Attachment, Long>
{
    //iterable removed by chap 24 when attachments broken out to both ticket and ticketComment classes
	//Iterable<Attachment> getByTicketId(long ticketId);
}
