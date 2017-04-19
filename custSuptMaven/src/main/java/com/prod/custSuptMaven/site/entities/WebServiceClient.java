package com.prod.custSuptMaven.site.entities;
/* class notes: chap 28, pg 843- Oauth web services security. It defines the clients that are permitted to access your web services
 * this is the class that establishes a client entity (vs the server entity) that allows clients 
 * to access the authorized URIs.  
 * 
 * some primary alternatives to this method are built in 1) InMemoryClientDetailsService [cant be used in a clustered env]
 *  and 2) JdbcClientDetailsService- but this will be a custom implementation since we are already using JPA, Spring Data and 
 *  Spring Framework Transactions.  see pg 842.
 * 
 */
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    
    @Override
	public String getClientId() {		
		return this.clientId;
	}
    
    public void setClientId(String clientId) {
    	this.clientId = clientId;
    }
    
    @Override
	public String getClientSecret() {
		return this.clientSecret;
	}
    
    public void setClientSecret(String clientSecret) {
    	this.clientSecret = clientSecret;
    }
    
    @Override
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "WebServiceClient_Scope", joinColumns = {
            @JoinColumn(name = "WebServiceClientId",
                    referencedColumnName = "WebServiceClientId")
    })
    @Column(name = "Scope")
	public Set<String> getScope() {
		return this.scope;
	}
    
    public void setScope(Set<String> scope) {
    	this.scope = scope;
    }
    
    @Override
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "WebServiceClient_Grant", joinColumns = {
            @JoinColumn(name = "WebServiceClientId",
                    referencedColumnName = "WebServiceClientId")
    })
    @Column(name = "GrantName")
	public Set<String> getAuthorizedGrantTypes() {
		return this.authorizedGrantTypes;
	}
    
    public void setAuthorizedGrantTypes(Set<String> authorizedGrantTypes) {
    	this.authorizedGrantTypes = authorizedGrantTypes;
    }
    
    @Override
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "WebServiceClient_RedirectUri", joinColumns = {
            @JoinColumn(name = "WebServiceClientId",
                    referencedColumnName = "WebServiceClientId")
    })
    @Column(name = "Uri")
	public Set<String> getRegisteredRedirectUri() {
		return this.registeredRedirectUri;
	}
    
    public void setRegisteredRedirectUri(Set<String> registeredRedirectUri) {
    	this.registeredRedirectUri = registeredRedirectUri;
    }
    
    //other customized items (no var above- so no getters/setters)
    private static final Set<String> RESOURCE_IDS = new HashSet<>();
    private static final Set<GrantedAuthority> AUTHORITIES = new HashSet<>();
    static {
        RESOURCE_IDS.add("SUPPORT");
        AUTHORITIES.add(new SimpleGrantedAuthority("OAUTH_CLIENT"));
    }
    
    @Override
    @Transient
	public Set<String> getResourceIds() {
		return RESOURCE_IDS;
	}
    
    @Override
    @Transient
	public Collection<GrantedAuthority> getAuthorities() {		
		return AUTHORITIES;
	}

    //other carry in methods for ClientDetails interface that need implementation
	@Override
	@Transient
	public Integer getAccessTokenValiditySeconds() {		
		return 3600;
	}
	
	@Override
	public Integer getRefreshTokenValiditySeconds() {
		return -1;
	}	
	
	@Override
	@Transient
	public Map<String, Object> getAdditionalInformation() {		
		return null;
	}	

	@Override
	@Transient
	public boolean isSecretRequired() {		
		return true;
	}
	
	@Override
	@Transient
	public boolean isScoped() {		
		return true;
	}

}
