package com.prod.custSuptMaven.site;
/* class notes- jwa chap 21, pg 627.  for each persistence group (each MySQL table in essence) all interfaces (ie TicketRepository class) 
 * extend GenericRepository and all implementations (this DefaultTicketRepository class) extend GenericJpaRepository.  
 * for Ticket group specifically no custom methods are needed so its empty.  (GenericJPA does all the CRUD work necessary)
 * 
 */
import org.springframework.stereotype.Repository;

import com.prod.custSuptMaven.site.entities.TicketEntity;

@Repository
public class DefaultTicketRepository
//extends applies to abstract classes- see pg 610 to 618 for detailed review as it applies to persistence
        extends GenericJpaRepository<Long, TicketEntity>
//and implements applies to interfaces
        implements TicketRepository
{
	//you are in essence doing all the basic functions in the abstract class or interface, so you are not repeating the same
	//code in every table class.  only those functions that are not common will have methods within the DefaultxxRepository class itself
}