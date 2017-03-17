package com.prod.custSuptMaven.site.chat;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionDestroyedEvent;
/* class notes- class added by chap 26 pg 774 to utilize Spring Sec session ApplicationListener- see book notes
 * this was a consequence of removing SessionListener class in app and using Spring Security implementation instead
 * 
 */
import org.springframework.stereotype.Service;

@Service
public class SessionDestroyedListener implements ApplicationListener<SessionDestroyedEvent>{
	private final Set<Consumer<SessionDestroyedEvent>> callbacks = 
			new HashSet<>();
	private final Set<Consumer<SessionDestroyedEvent>> callbacksAddedWhileLocked = 
			new HashSet<>();
	
	@Override
	public void onApplicationEvent(SessionDestroyedEvent event) {
		synchronized(this.callbacks) {
			this.callbacksAddedWhileLocked.clear();
			this.callbacks.forEach(c -> c.accept(event));
			try {
				this.callbacksAddedWhileLocked.forEach(c -> c.accept(event));
			} catch(ConcurrentModificationException ignore) {
				
			}
		}
	}
	
	public void registerOnRemoveCallback(Consumer<SessionDestroyedEvent> callback) {
		this.callbacksAddedWhileLocked.add(callback);
		synchronized(this.callbacks) {
			this.callbacks.add(callback);
		}
	}
	
	public void deregisterOnRemoveCallback(Consumer<SessionDestroyedEvent> callback) {
		synchronized(this.callbacks) {
			this.callbacks.remove(callback);
		}
	}

}
