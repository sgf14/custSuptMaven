package com.prod.custSuptMaven.site;
/* class notes- chap 28, pg 846.  Oauth Nonce service to support signature based tokens.
 * the whole purpose of this class is simply to delete old Nonces w/ signature older than 2 minutes
 * since they arent needed longer than that and in a larger app the assoc 
 * MySQL table would get unwieldy (and for no purpose)- its only to avoid replay attacks as described on pg 840.
 * 
 */
import javax.inject.Inject;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.transaction.annotation.Transactional;

import com.prod.custSuptMaven.site.entities.Nonce;
import com.prod.custSuptMaven.site.repositories.NonceRepository;

public class DefaultOAuthNonceServices implements OAuthNonceServices {
	
	@Inject NonceRepository nonceRepository;

	@Override
	@Transactional
	public void recordNonceOrFailIfDuplicate(String nonce, long timestamp) {
		if(this.nonceRepository.getByValueAndTimestamp(nonce, timestamp) != null)
			throw new InvalidTokenException("Duplicate nonce value [" + nonce + 
					"," + timestamp + "]");
		
		this.nonceRepository.save(new Nonce(nonce, timestamp));
		
	}
	
	//delete nonces older than 2 min (120_000)
	@Transactional
	@Scheduled(fixedDelay = 60_000L)
	public void deleteOldNonces() {
		this.nonceRepository.deleteWhereTimestampLessThan(
				(System.currentTimeMillis() - 120_000L) / 1_000L
		);
	}

}
