package com.prod.custSuptMaven.site;
/* class notes: chap 28, pg 844.  
 * this interface extends oAuth ClientDetailsService to clarify that the service
 * always returns WebServiceClient entities
 */

import org.springframework.security.oauth2.provider.ClientDetailsService;
import com.prod.custSuptMaven.site.entities.WebServiceClient;

public interface WebServiceClientService extends ClientDetailsService {
	@Override
	WebServiceClient loadClientByClientId(String clientId);
	
	
}
