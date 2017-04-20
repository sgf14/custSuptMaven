package com.prod.custSuptMaven.site;
/* class notes. chap 28, pg 844.
 * implements the client side of the web service server/client function
 *  extending the interface the ultimately gets back to the repo and entity, from there to the MySql table
 */
import javax.inject.Inject;

import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.transaction.annotation.Transactional;

import com.prod.custSuptMaven.site.entities.WebServiceClient;
import com.prod.custSuptMaven.site.repositories.WebServiceClientRepository;

public class DefaultWebServiceClientService implements WebServiceClientService {
	@Inject WebServiceClientRepository clientRepository;

	@Override
	@Transactional
	public WebServiceClient loadClientByClientId(String clientId) {
		WebServiceClient client = this.clientRepository.getByClientId(clientId);
		
		if(client == null)
			throw new ClientRegistrationException("Client not found");
		return client;
	}

}
