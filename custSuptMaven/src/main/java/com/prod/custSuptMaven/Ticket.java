package com.prod.custSuptMaven;
//Ticket starts chap 3, pg 67
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Ticket{
	//instantiate vars
    private String customerName;
    private String subject;
    private String body;
    private Map<String, Attachment> attachments = new LinkedHashMap<>();
    private OffsetDateTime dateCreated;
    
    //getters and setters
    public String getCustomerName(){
        return customerName;
    }

    public void setCustomerName(String customerName){
        this.customerName = customerName;
    }

    public String getSubject(){
        return subject;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }

    public String getBody(){
        return body;
    }

    public void setBody(String body){
        this.body = body;
    }
    
    //note this is a little different since its a hashmap object
    public Attachment getAttachment(String name){
        return this.attachments.get(name);
    }

    public Collection<Attachment> getAttachments(){
        return this.attachments.values();
    }

    public void addAttachment(Attachment attachment){
        this.attachments.put(attachment.getName(), attachment);
    }

    public int getNumberOfAttachments(){
        return this.attachments.size();
    }
    
    public OffsetDateTime getDateCreated(){
        return dateCreated;
    }

    public void setDateCreated(OffsetDateTime dateCreated){
        this.dateCreated = dateCreated;
    }
}
