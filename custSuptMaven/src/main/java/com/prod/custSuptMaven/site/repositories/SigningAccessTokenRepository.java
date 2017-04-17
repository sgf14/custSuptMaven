package com.prod.custSuptMaven.site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.custSuptMaven.site.entities.SigningAccessToken;

public interface SigningAccessTokenRepository 
	extends JpaRepository<SigningAccessToken, Long>{
	
	SigningAccessToken getByKey(String key);
	SigningAccessToken getByValue(String value);

}
