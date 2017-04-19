package com.prod.custSuptMaven.site;
/* class notes: chap 28, pg 848-49.  Class for signature based tokens 
 * this does the heavy lifting for tokens via the supporting custom and stock Oauth interfaces.
 *   class supplies all the business logic (ie- a service) to create and retrieve signature based access tokens
 *  
 * Its a fairly complex class and is part of the custom method as an alternative to bearer tokens- 
 * as described by chap 28, pg 847.  Book sees bearer tokens as a less secure method.
 * signature based tokens are passed between server and client- so affect both types of web service security classes.
 * 
 * there are several classes and interfaces associated with these signature based tokens- as demonstrated
 * by this app.
 */

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.transaction.annotation.Transactional;

import com.prod.custSuptMaven.site.entities.SigningAccessToken;
import com.prod.custSuptMaven.site.repositories.SigningAccessTokenRepository;

public class DefaultAccessTokenServices implements SigningAccessTokenServices {
	
	AuthenticationKeyGenerator authenticationKeyGenerator = 
			new DefaultAuthenticationKeyGenerator();
	
	@Inject SigningAccessTokenRepository repository;

	@Override
	@Transactional
	public OAuth2AccessToken createAccessToken(OAuth2Authentication auth)
			throws AuthenticationException {
		String key = this.authenticationKeyGenerator.extractKey(auth);
        SigningAccessToken token = this.repository.getByKey(key);
        if(token != null)
        {
            if(token.isExpired())
            {
                this.repository.delete(token);
                this.repository.flush();
            }
            else
            {
                token.setAuthentication(auth); // in case authorities changed
                this.repository.save(token);
                return token;
            }
        }

        token = new SigningAccessToken(
                key,
                UUID.randomUUID().toString(),
                new Date(System.currentTimeMillis() + 86_400_000L), // one day
                auth.getAuthorizationRequest().getScope(),
                auth
        );

        this.repository.save(token);

        return token;
	}

	@Override
	@Transactional
	public OAuth2AccessToken getAccessToken(OAuth2Authentication auth) {		
		return this.repository.getByKey(
				this.authenticationKeyGenerator.extractKey(auth)
		);
	}

	@Override
	@Transactional
	public SigningAccessToken getAccessToken(String key) {
		return this.repository.getByKey(key);
	}
	
	@Override
	@Transactional
	public OAuth2AccessToken readAccessToken(String tokenValue) {
		return this.repository.getByValue(tokenValue);
	}
	
	@Override
	@Transactional
	public OAuth2Authentication loadAuthentication(String tokenValue)
			throws AuthenticationException {
		SigningAccessToken token = this.repository.getByValue(tokenValue);
        if(token == null)
            throw new InvalidTokenException("Invalid token " + tokenValue + ".");

        if(token.isExpired())
        {
            this.repository.delete(token);
            throw new InvalidTokenException("Expired token " + tokenValue + ".");
        }

        return token.getAuthentication();
	}	

	@Override	
	public OAuth2AccessToken refreshAccessToken(String refreshToken,
			AuthorizationRequest request) throws AuthenticationException {
		
		throw new UnsupportedOperationException();
	}

}
