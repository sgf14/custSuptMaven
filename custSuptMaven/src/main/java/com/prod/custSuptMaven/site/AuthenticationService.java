package com.prod.custSuptMaven.site;
/*class notes- this is the service interface that is extended.  chap 14, pg 398 as example.  In this case only once, to TemporaryAuthenticationService.
note there is no @Service annotation in the interface itself- thats in the implementation.

also modified later to include validation annotations- see chap 16
and Save function as part of persistence adds- chap 21 for saveUser function (allows users to change password)
*/

import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.prod.custSuptMaven.site.entities.UserPrincipal;
import com.prod.custSuptMaven.site.validation.NotBlank;

@Validated
public interface AuthenticationService {
	UserPrincipal authenticate(
			@NotBlank(message = "{validate.authenticate.username}")
			String username, 
			@NotBlank(message = "{validate.authenticate.password}")
			String password
		);
	
	void saveUser(
			@NotNull(message = "{validate.authenticate.saveUser}") @Valid
				UserPrincipal principal,
				String newPassword
			);

}
