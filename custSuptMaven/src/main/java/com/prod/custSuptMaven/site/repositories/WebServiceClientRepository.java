package com.prod.custSuptMaven.site.repositories;

import org.springframework.data.repository.CrudRepository;

import com.prod.custSuptMaven.site.entities.WebServiceClient;

public interface WebServiceClientRepository 
	extends CrudRepository<WebServiceClient, Long>{
	
	WebServiceClient getByClientId(String clientId);

}
