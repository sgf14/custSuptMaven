package com.prod.custSuptMaven.site.repositories;
/* class notes Nonce repo interface
 * 
 */
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prod.custSuptMaven.site.entities.Nonce;


public interface NonceRepository extends CrudRepository<Nonce, Long> {
	Nonce getByValueAndTimestamp(String value, long timestamp);
	
	/*pg 846- note use of these annotations- different than previous to chap 28 versions of repos
	 * which were more or less straight implementations.  this deletes nonces after 2 mins.
	 * see assoc nonce service. see chap 22 for Query annotation
	 */
	@Modifying
	@Query("DELETE FROM Nonce n WHERE n.timestamp < :timestamp")
	void deleteWhereTimestampLessThan(long timestamp);

}
