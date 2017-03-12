package com.prod.custSuptMaven.site;

import com.prod.custSuptMaven.site.entities.UserPrincipal;
import com.prod.custSuptMaven.site.repositories.UserRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
public class DefaultAuthenticationService implements AuthenticationService
{
    private static final Logger log = LogManager.getLogger();
    private static final SecureRandom RANDOM;
    private static final int HASHING_ROUNDS = 10;
    
    static
    {
        try
        {
            RANDOM = SecureRandom.getInstanceStrong();
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new IllegalStateException(e);
        }
    }

    @Inject UserRepository userRepository;

    @Override
    @Transactional
    public UserPrincipal authenticate(Authentication authentication)
    {
        /*new chap 26 pg 770-71 codeblocks for Spring security.  note use of boilerplate method implementation within UserPrincipal class
    	//which were part of spring security extension.
    	 * 
    	 */
    	UsernamePasswordAuthenticationToken credentials = 
    			(UsernamePasswordAuthenticationToken)authentication;
    	String username = credentials.getPrincipal().toString();
    	String password = credentials.getCredentials().toString();
    	credentials.eraseCredentials();
    	
    	UserPrincipal principal = this.userRepository.getByUsername(username);
        if(principal == null)
        {
            log.warn("Authentication failed for non-existent user {}.", username);
            return null;
        }
		
    	
    	//chap 26, pg 770-71 Spring security implemented methods
    	
        if(!BCrypt.checkpw(
                password,
                new String(principal.getPassword(), StandardCharsets.UTF_8)
        ))
        {
            log.warn("Authentication failed for user {}.", username);
            return null;
        }
        
        //chap 26 add
        principal.setAuthenticated(true);
        log.info("User {} successfully authenticated.", username);

        return principal;
    }
    
    //added by chap 26
    @Override
    public boolean supports(Class<?> c) {
    	return c == UsernamePasswordAuthenticationToken.class;
    }
    
    //chap 26 leaves previous saveUser method unchanged
    @Override
    @Transactional
    public void saveUser(UserPrincipal principal, String newPassword)
    {
        //see chap 21, pg 629 for usage of password encryption via BCrypt w/ salting and hashing rounds for security
    	if(newPassword != null && newPassword.length() > 0)
        {
            String salt = BCrypt.gensalt(HASHING_ROUNDS, RANDOM);
            principal.setPassword(BCrypt.hashpw(newPassword, salt).getBytes());
        }
    	//like other DefaultServices this section got changed from add()/update() to save() to match CrudRepo interface
        this.userRepository.save(principal);
    }
}

