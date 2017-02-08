package com.prod.custSuptMaven.site.entities;
/*package notes- for chap 21 sql persistence a new /entities package was created that houses all the data items that will be 
 * saved to tables.  more or less once class per table is typical.
 * class notes:  Attachment.java class will save to the corresponding attachments MySQL table.  it uses validation to ensure data entries
 * comply with business rules.
 * 
 */
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.prod.custSuptMaven.site.validation.NotBlank;

import java.io.Serializable;


@Entity
//chap 24 adds similar to ticket adn tickecomment entities
@XmlRootElement(name = "attachment")
@XmlAccessorType(XmlAccessType.NONE)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class Attachment implements Serializable
{
    private static final long serialVersionUID = 1L;

    private long id;
    
    //see below- remvoed by chap 24 w/ intro of ticketcomment attachmetns also
    //private long ticketId;

    @NotBlank(message = "{validate.attachment.name}")
    private String name;

    @NotBlank(message = "{validate.attachment.mimeContentType}")
    private String mimeContentType;

    @Size(min = 1, message = "{validate.attachment.contents}")
    private byte[] contents;

    @Id
    @Column(name = "AttachmentId")
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

    //removed by chap 24 since comments now allows in both tickets and ticket comments.  via db join tables, pg721
//    @Basic
//    public long getTicketId()
//    {
//        return this.ticketId;
//    }
//
//    public void setTicketId(long ticketId)
//    {
//        this.ticketId = ticketId;
//    }

    @Basic
    @Column(name = "AttachmentName")
    @XmlElement
    @JsonProperty
    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Basic
    @XmlElement
    @JsonProperty
    public String getMimeContentType()
    {
        return this.mimeContentType;
    }

    public void setMimeContentType(String mimeContentType)
    {
        this.mimeContentType = mimeContentType;
    }
    
    //chap 24 changes here also, lazy loading
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @XmlElement
    @XmlSchemaType(name = "base64Binary")
    @JsonProperty
    public byte[] getContents()
    {
        return this.contents;
    }

    public void setContents(byte[] contents)
    {
        this.contents = contents;
    }
}
