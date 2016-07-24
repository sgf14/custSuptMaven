//added in chap 5- sessions as part of group to track and display HTTP sessions.  see JWA pg 133
package com.prod.custSuptMaven;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;

@WebListener
public class SessionListener implements HttpSessionListener, HttpSessionIdListener{
	@Override
	public void sessionCreated(HttpSessionEvent e){
		//can comment out to disable console logging below
		System.out.println(this.date() + ": Session " + e.getSession().getId()
				+ " created.");
		//for web page "/sessions' webpage view
		SessionRegistry.addSession(e.getSession());
	}
	
	@Override
	public void sessionDestroyed(HttpSessionEvent e){
		System.out.println(this.date() + ": Session " + e.getSession().getId()
				+ " destroyed");
		SessionRegistry.removeSession(e.getSession());
	}
	
	@Override
	public void sessionIdChanged(HttpSessionEvent e, String oldSessionId){
		System.out.println(this.date() + ": Session ID " + oldSessionId  
				+ " changed to " + e.getSession().getId());
		SessionRegistry.updateSessionId(e.getSession(), oldSessionId);
	}
	
	private SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
	
	private String date(){
		return this.formatter.format(new Date());
	}

}
