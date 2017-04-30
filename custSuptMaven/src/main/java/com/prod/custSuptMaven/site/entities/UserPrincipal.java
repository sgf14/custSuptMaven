package com.prod.custSuptMaven.site.entities;
/* class notes: class established in chap 16 and 21 (JPA Hibernate db) and heavily refactored in chap 26 Spring Authentication
 * then updated again in a fairly substantial was in chap 27- spring authorization.
 * 1st iteration used Principal convention to tie user list to Db table, 2nd then Spring uses this 
 * for user login and 3rd validating which user has access to what resource
 * 
 * UserPrincipal scope:  as described in chap 27, pg 805 you do not nescessarily want to combine both the persistence function
 * of Principal with the security context- as is the case below- however for the sake of simplicity they are combined under
 * one class in the book project.  separating them out allows them to be more modular, thus matching more closely  a separation 
 * of concerns model, but also adds to the coding complexity.
 */
import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
//import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UserPrincipal_Username", columnNames = "Username")
})
@XmlAccessorType(XmlAccessType.NONE)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)

// chap 26 implements spring Authentication, but chap 27 changes that to UserDetails and CredentialsContainer- see pg 805
public class UserPrincipal implements UserDetails, CredentialsContainer, Cloneable
{
    private static final long serialVersionUID = 1L;
    
    //var removed by chap 26 implementing Authentication vs Principal
    //private static final String SESSION_ATTRIBUTE_KEY = "com.prod.custSuptMaven.user.principal";

    private long id;
    private String username;
    private byte[] hashedPassword;    
    //var added by chap 26 spring security ,pg 770
    //private boolean authenticated;
    private Set<UserAuthority> authorities = new HashSet<>();
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    @Id
    @Column(name = "UserId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement
    @JsonProperty
    public long getId()
    {
        return this.id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    @Basic
    @XmlElement
    @JsonProperty
    public String getUsername()
    {
        return this.username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "HashedPassword")
    public byte[] getHashedPassword()
    {
        return this.hashedPassword;
    }

    public void setHashedPassword(byte[] password)
    {
        this.hashedPassword = password;
    }
    
    //chap 27 changed the following section commented code blocks extensively- immediately below is the new authentication AND authorization code.
    // this group overrided methods in the newly added Spring UserDetails and CredentialsContainer interfaces 
    @Transient
    @Override
    public String getPassword() {
    	return this.getHashedPassword() == null ? null :
    		new String(this.getHashedPassword(), StandardCharsets.UTF_8);
    }
    
    @Override
    public void eraseCredentials() {
    	this.hashedPassword = null;
    }
    
    @Override
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "UserPrincipal_Authority", joinColumns = {
    		@JoinColumn(name = "UserId", referencedColumnName = "UserId")
    })
    public Set<UserAuthority> getAuthorities() {
    	return this.authorities;
    }
    
    public void setAuthorities(Set<UserAuthority> authorities) {
    	this.authorities = authorities;
    }
    
    @Override
    @XmlElement
    @JsonProperty
    public boolean isAccountNonExpired() {
    	return this.accountNonExpired;
    }
    
    public void setAccountNonExpired(boolean accountNonExpired) {
    	this.accountNonExpired = accountNonExpired;
    }
    
    @Override
    @XmlElement
    @JsonProperty
    public boolean isAccountNonLocked() {
    	return this.accountNonLocked;
    }
    
    public void setAccountNonLocked(boolean accountNonLocked) {
    	this.accountNonLocked = accountNonLocked;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
    	return this.credentialsNonExpired;
    }
    
    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
    	this.credentialsNonExpired = credentialsNonExpired;
    }
    
    @Override
    @XmlElement
    @JsonProperty
    public boolean isEnabled() {
    	return this.enabled;
    }
    
    public void setEnabled(boolean enabled) {
    	this.enabled = enabled;
    }
    
    /* OLD AUTHENTICATION ONLY VERSION- next several code blocks added chap 26 spring security, pg 770 implements extended Authentication class methods within this class.  
    //similar to any other interface Note @override annotation. chap 26 version is boilerplate method impl, but is customized in chap 27.
    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	return Arrays.asList();
    }
    
    @Override
    @Transient
    public String getName() {
    	return this.username;
    }
    
    @Override
    @Transient
    public Object getPrincipal() {
    	return this.username;
    }
    
    @Override
    @Transient
    public Object getDetails() {
    	return this.username;
    }
    
    @Override
    @Transient
    public Object getCredentials() {
    	return this.password;
    }
    
    @Override
    @Transient
    public boolean isAuthenticated() {
    	return this.authenticated;
    }
    
    @Override
    public void setAuthenticated(boolean authenticated) {
    	this.authenticated = authenticated;
    }
    //end of chap 26 method adds
	*/
    @Override
    public int hashCode()
    {
        return this.username.hashCode();
    }
    
    @Override
    public boolean equals(Object other) {
    	return other instanceof UserPrincipal &&
    			((UserPrincipal)other).id == this.id;
    }
    
    // OLD - chap 26 version of equals() method
    /*
    @Override
    public boolean equals(Object other)
    {
        return other instanceof UserPrincipal &&
                ((UserPrincipal)other).username.equals(this.username);
    }
	*/
    
    @Override
    @SuppressWarnings("CloneDoesntDeclareCloneNotSupportedException")
    protected UserPrincipal clone()
    {
        try {
            return (UserPrincipal)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); // not possible
        }
    }

    @Override
    public String toString()
    {
        return this.username;
    }
    // these methods removed when Part IV chap 26 Spring security implemented
    /*
    public static Principal getPrincipal(HttpSession session)
    {
        return session == null ? null :
                (Principal)session.getAttribute(SESSION_ATTRIBUTE_KEY);
    }

    public static void setPrincipal(HttpSession session, Principal principal)
    {
        session.setAttribute(SESSION_ATTRIBUTE_KEY, principal);
    }
    */
}
