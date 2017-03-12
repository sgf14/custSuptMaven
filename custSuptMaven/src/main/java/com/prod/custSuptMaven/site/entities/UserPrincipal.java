package com.prod.custSuptMaven.site.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Collection;

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

public class UserPrincipal implements Authentication, Cloneable
{
    private static final long serialVersionUID = 1L;
    
    //var removed by chap 26 implementing Authentication vs Principal
    //private static final String SESSION_ATTRIBUTE_KEY = "com.prod.custSuptMaven.user.principal";

    private long id;

    private String username;

    private byte[] password;
    
    //var added by chap 26 spring security ,pg 770
    private boolean authenticated;

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

    @Basic
    @Column(name = "HashedPassword")
    public byte[] getPassword()
    {
        return this.password;
    }

    public void setPassword(byte[] password)
    {
        this.password = password;
    }
    
    //next several code blocks added chap 26 spring security, pg 770 implements extended Authentication class methods within this class.  
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

    @Override
    public int hashCode()
    {
        return this.username.hashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        return other instanceof UserPrincipal &&
                ((UserPrincipal)other).username.equals(this.username);
    }

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
