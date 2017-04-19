package com.prod.custSuptMaven.site;
/* class notes: chap 28, pg 848- interface to supt signature based tokens
 * this interface is part of implementing a custom token service w/i the OAuth structure.  see pg 847
 * for concepts.  there are several interfaces and classes involved with this custom setup
 * alternatives would include InMemoryTokenStore and JdbcToeknStore described in other classes
 * However, those are for bearer tokens, which the book does nto reccomend.  
 * Instead it offers signature-based tokens (noneces)as a more secure alternative.  but that tkaes more
 * code- as demonstrated in this app.  tokens are exchanged between server and client- so affect both assoc classes.
 */
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import com.prod.custSuptMaven.site.entities.SigningAccessToken;

public interface SigningAccessTokenServices 
	extends AuthorizationServerTokenServices, ResourceServerTokenServices {
	
	SigningAccessToken getAccessToken(String key);

}
