package com.prod.custSuptMaven.site;
//class notes- this is the service interface that is extended.  cahp 14, pg 398 as example.  In this case only once, to TemporaryAuthenticationService.
//note there is no @Service annotation in the interface itself- thats in the implementation
import java.security.Principal;

public interface AuthenticationService {
	Principal authenticate(String username, String password);

}
