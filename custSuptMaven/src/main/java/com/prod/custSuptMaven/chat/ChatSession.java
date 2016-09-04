package com.prod.custSuptMaven.chat;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.Session;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ChatSession {
	private long sessionId;
	private String customerUsername;
	private Session customer;
	private String representativeUsername;
	private Session representative;
	private ChatMessage creationMessage;
	private final List<ChatMessage> chatLog = new ArrayList<>();
	
	//TODO getters/ setters
	
	@JsonIgnore
	public void log(ChatMessage message) {
		//TODO
		
	}

}
