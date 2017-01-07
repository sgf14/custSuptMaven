package com.prod.custSuptMaven.site.repositories;
/*class notes- the repository package and its related repositories were created in chap 22- springDataJpa- 
 * as noted on pg 656 and earlier example pg 638 all the chap 21 'generic...' classes replaced by CrudRepository interface
 * note however that the attributes are reversed.  instead of <Long, TicketEntity> it is <TicketEntity, Long> as
 * described on pg 638.
 * 
 */
import org.springframework.data.repository.CrudRepository;

import com.prod.custSuptMaven.site.entities.TicketEntity;

public interface TicketRepository extends CrudRepository<TicketEntity, Long>,
	SearchableRepository<TicketEntity>
{
    
}

