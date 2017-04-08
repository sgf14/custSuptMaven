package com.prod.custSuptMaven.site.entities;
/* class notes- added by chap 27- Spring Authorization.  see pg 806.  implemented by UserPrincipal
 *  as part of Spring Authorization methodology.  purpose is a POJO that implements spring GrantedAuthority interface
 */
import javax.persistence.Embeddable;

import org.springframework.security.core.GrantedAuthority;

@Embeddable
public class UserAuthority implements GrantedAuthority {

	private static final long serialVersionUID = 1L;
	private String authority;
	
	public UserAuthority() {
		
	}
	
	public UserAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public String getAuthority() {
		return this.authority;
	}
	
	public void setAuthority(String authority) {
		this.authority = authority;
	}

}
