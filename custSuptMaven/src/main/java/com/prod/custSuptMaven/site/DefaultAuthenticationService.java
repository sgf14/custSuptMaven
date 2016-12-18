package com.prod.custSuptMaven.site;

import com.prod.custSuptMaven.site.entities.UserPrincipal;
import com.prod.custSuptMaven.site.repositories.UserRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
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
//injecting this is causing a problem
    @Inject UserRepository userRepository;

    @Override
    @Transactional
    public UserPrincipal authenticate(String username, String password)
    {
        UserPrincipal principal = this.userRepository.getByUsername(username);
        if(principal == null)
        {
            log.warn("Authentication failed for non-existent user {}.", username);
            return null;
        }

        if(!BCrypt.checkpw(
                password,
                new String(principal.getPassword(), StandardCharsets.UTF_8)
        ))
        {
            log.warn("Authentication failed for user {}.", username);
            return null;
        }

        log.debug("User {} successfully authenticated.", username);

        return principal;
    }

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

