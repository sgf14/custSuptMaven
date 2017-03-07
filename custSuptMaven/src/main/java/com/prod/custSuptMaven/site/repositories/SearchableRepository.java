package com.prod.custSuptMaven.site.repositories;
/*chap 23 full text search function
 *  note this is similar to non-full text search like jpa-search app except for useBooleanMode attribute.
 *  same generic class T carried by calling class- TicketRepository/TicketEntity in this app.  since its generic, project can 
 *  easily be extended later on using this same interface and search structure
 */
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchableRepository<T> {
	Page<SearchResult<T>> search(String query, boolean useBooleanMode,
			Pageable pageable);

}
