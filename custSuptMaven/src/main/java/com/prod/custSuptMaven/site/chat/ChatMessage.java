package com.prod.custSuptMaven.site.chat;
//basic POJO to instantiate variables used with the chat classes
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ChatMessage {
	private Instant timestamp;
    private Type type;
    private String user;
    private String content;

    public Instant getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp)
    {
        this.timestamp = timestamp;
    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    @Override
    @SuppressWarnings("CloneDoesntDeclareCloneNotSupportedException")
    protected ChatMessage clone() {
        try {
            return (ChatMessage)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Impossible clone not supported.", e);
        }
    }
    
    public static enum Type
    {
        STARTED, JOINED, ERROR, LEFT, TEXT
    }
    
    static abstract class MixInForLogWrite
    {
        @JsonIgnore public abstract String getLocalizedContent();
        @JsonIgnore public abstract void setLocalizedContent(String l);
    }

    static abstract class MixInForWebSocket
    {
        @JsonIgnore public abstract String getContentCode();
        @JsonIgnore public abstract void setContentCode(String c);
        @JsonIgnore public abstract Object[] getContentArguments();
        @JsonIgnore public abstract void setContentArguments(Object... c);
    }

}
