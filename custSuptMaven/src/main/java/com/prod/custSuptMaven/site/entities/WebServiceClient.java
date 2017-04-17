package com.prod.custSuptMaven.site.entities;
/* class notes.  chap 28 pg 843 Oauth web service security.
 * this is the client vs server side of the equation.  POJO for client and tie to DB tables
 * 
 */
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

@Entity
public class WebServiceClient implements ClientDetails, Serializable {
	
	private static final long serialVersionUID = 1L;
	private long id;
    private String clientId;
    private String clientSecret;
    private Set<String> scope;
    private Set<String> authorizedGrantTypes;
    private Set<String> registeredRedirectUri;
    
    @Id
    @Column(name = "WebServiceClientId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
    	return this.id;
    }
    
    public void setId(long id) {
    	this.id = id;
    }
    
    // TODO - left off here 04/15/17. more getters and setters
    
    

    //carry in methods that need implementation
	@Override
	public Integer getAccessTokenValiditySeconds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getAdditionalInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getAuthorizedGrantTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClientId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClientSecret() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getRefreshTokenValiditySeconds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getRegisteredRedirectUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getResourceIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getScope() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isScoped() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSecretRequired() {
		// TODO Auto-generated method stub
		return false;
	}

}
