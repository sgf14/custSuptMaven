package com.prod.custSuptMaven.site;
/* class notes- Nonce support interface- see chap 28, pg 846
 *  note the interface doesnt even expose the Nonce entity (as you would often see in a service)
 *  since it is not needed in this case.  the Default.. class that implements this is only 
 *  deleting the old nonces every two minutes.
 */
public interface OAuthNonceServices {
	void recordNonceOrFailIfDuplicate(String nonce, long timestamp);

}
