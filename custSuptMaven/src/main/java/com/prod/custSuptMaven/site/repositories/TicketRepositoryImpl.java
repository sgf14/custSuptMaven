package com.prod.custSuptMaven.site.repositories;
/*class notes- chap 23, pg 681.  implementation of full text search criteria
 * w/ custom SQL script.  looks for matches in both ticket and ticketComment.
 * returns total count, matches themselves w/ relevance score, in a pageable format
 * as directed by .jsp ui script
 * 
 */
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.prod.custSuptMaven.site.entities.TicketEntity;

public class TicketRepositoryImpl implements SearchableRepository<TicketEntity> {
	//using TicketEntity as base for entityManager in this case
	@PersistenceContext EntityManager entityManager;
	
	@Override
	public Page<SearchResult<TicketEntity>> search(String query, 
												boolean useBooleanMode, 
												Pageable pageable) {
		//vars for use in methods- custom SQL script via TicketEntity SqlResultSetMapping annotation
		//note use of compact if/then/else since there are only two choices
		/* MySql boolean mode.  in FULLTEXT search multi element text string entry here are some of the common functions:
		 * 1) + = AND 
		 * 2) - = NOT 
		 * 3) [no symbol] = implied OR
		 * 4) * = wildcard. at end of text.  in manuals may be referred to as Truncation character
		 * 5) it inlcude STOPWORDS function.  see Mysql manual (google search on 'mysql in boolean mode')
		 * 
		 * there is a UI checkbox for this.  it really isnt described in JWA book, above is based on web search.
		 * note there isnt a description of wildcard characters, and entry of partial text strings
		 * dont return any results.  if has to be the full word separated by space characters
		 * this is not the way a typical google search works, where partial strings do produce results
		 * a little more research needed on this.  it may be that FULLTEXT searches in MySQL dont really
		 * work this way, that it is intended to bring back matching full words.
		 */
		String mode = useBooleanMode ? "IN BOOLEAN MODE" : "IN NATURAL LANGUAGE MODE";
		String matchTicket = "MATCH(t.subject, t.body) AGAINST(?1 " + mode + ")";
		String matchComment = "MATCH(c.body) AGAINST(?1 " + mode + ")";
		
		//total count of matches- in either table (using outer join)
		long total = ((Number)this.entityManager.createNativeQuery(
				"SELECT COUNT(DISTINCT t.TicketId) FROM Ticket t " +
						"LEFT OUTER JOIN TicketComment c ON c.TicketId = "+
						"t.TicketId WHERE " + matchTicket + " OR " + matchComment
		).setParameter(1, query).getSingleResult()).longValue();
		
		//list the results in pageable format
		@SuppressWarnings("unchecked")
		List<Object[]> results = this.entityManager.createNativeQuery(
				"SELECT DISTINCT t.*, (" + matchTicket + " + " + matchComment +
						") AS _ft_scoreColumn " +
						"FROM Ticket t LEFT OUTER JOIN TicketComment c " +
						"ON c.TicketId = t.TicketId "+
						"WHERE " + matchTicket + " OR " + matchComment + " " +
						"ORDER BY _ft_scoreColumn DESC, TicketId DESC",
				"searchResultMapping.ticket"
		).setParameter(1, query)
			.setFirstResult(pageable.getOffset())
			.setMaxResults(pageable.getPageSize())
			.getResultList();
		
		//Object var results cast to Array list via lambda expression.  see pg 682.  result 1st, relevance 2nd
		//(zero based array = 0,1)
		List<SearchResult<TicketEntity>> list = new ArrayList<>();
		results.forEach(o -> list.add(
				new SearchResult<>((TicketEntity)o[0], (Double)o[1])
		));
		
		return new PageImpl<>(list, pageable, total);
		
	}

}
