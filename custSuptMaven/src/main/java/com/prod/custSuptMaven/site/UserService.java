package com.prod.custSuptMaven.site;
/*class notes- this is the service interface that is extended.  chap 14, pg 398 as example.  In this case only once, to TemporaryAuthenticationService.
note there is no @Service annotation in the interface itself- thats in the implementation.

also modified later to include validation annotations- see chap 16
and Save function as part of persistence adds- chap 21 for saveUser function (allows users to change password)

chapter 27 refactored this to add authorization functions and take advantage to use Spring UserDetailsService.
It changed file name from AuthenticationService to UserService, along with its default implementation (DefaultUserService.java).
class is just extending a different Spring interface.
*/

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.prod.custSuptMaven.site.entities.UserPrincipal;

@Validated
public interface UserService extends UserDetailsService {
	@Override
	UserPrincipal loadUserByUsername(String username);
	
	void saveUser(
			@NotNull(message = "{validate.authenticate.saveUser}") @Valid
				UserPrincipal principal,
				String newPassword
			);

}
