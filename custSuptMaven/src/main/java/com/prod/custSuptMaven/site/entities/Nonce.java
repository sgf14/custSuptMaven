package com.prod.custSuptMaven.site.entities;
/* class notes- POJO for chap 28 Oauth Nonce function.  see pg 840 and 845
 * Nonces in OAuth function as both a signature item for the token and ensuring 
 * a different signature on each request.
 * 
 * the Nonce will include a entity, repository and service, but does not expose the service
 * since this is not needed.  it essentially utilizes a Oauth1 function into Oauth2 as described by 
 * chap 28, pg 840 as a security implementation within Oauth2 framework.
 * 
 * you will note part of the services will delete the nonce table entries every 2 minutes
 * since in a highly utilized app this table could become very large and the only purpose
 * of a nonce is to ensure security and prevent replay attacks.  Older Nonces do not need to be kept.
 * 
 */

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "OAuthNonce")
public class Nonce implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String value;
	private long timestamp;
	
	//empty constructor.  see java beginner book, chap 4, pg 127
	public Nonce() {}
	
	//standard constructor
	public Nonce(String value, long timestamp) {
		this.value = value;
		this.timestamp = timestamp;
	}
	
	//getters and setters- w/ annotated ties to related tables- when class is invoked
	@Id
	@Column(name = "OAuthNonceId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Column(name = "NonceTimestamp")
	public long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	

}
