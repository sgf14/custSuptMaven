package com.prod.custSuptMaven.site.entities;
/* class notes- this is the chap 24 version- pg 720- that replaced entities\TicketEntity and site\Ticket (both of which got deleted)
 *  with items you have learned for timestamp and attachments  w/ conversion annotations and a Map of attachments.  ticket.jav used to act as
 *  as DTO to do the date conversion and map the atmt.  see chap 21, pg 624 for orig DTO and pg 698 for revisions and pg 720
 *  this version also uses the one to many annotation and makes extensive changes to DB related to attachments- housing them in a separate
 *  table.  this class kind of mashes TicketEntity and Ticket together w changes described above.  see diff github versions for exact differences
 */

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.prod.custSuptMaven.site.entities.Attachment;
import com.prod.custSuptMaven.site.entities.UserPrincipal;
import com.prod.custSuptMaven.site.validation.NotBlank;
import com.prod.custSuptMaven.site.converters.InstantConverter;

//the entity annotation is established in RootContextConfig class- and used here for the Ticket table specific details
@Entity
@Table(name = "Ticket")
//search- chap 23. this annotation needed for full text search functions- chap 23, pg 679-80. FT search requires a relevance column that does
//not have getters or setters, but exists as index in db table- and in this app val returned and sent to UI for display.
@SqlResultSetMapping(
		name = "searchResultMapping.ticket",
		entities = { @EntityResult(entityClass = Ticket.class) },
		columns = { @ColumnResult(name = "_ft_scoreColumn", type = Double.class)}
		)
//introduced in chap 21 spring-data, pg 624 with some addt detail pgs 647-650,  still dont have a firm grasp on its function
@XmlRootElement(namespace = "http://example.com/xmlns/support", name = "ticket")
//added by chap 24 pg 724 for serializing xml and json of entity(tied to JAXB and Jackson Data Processor)
@XmlAccessorType(XmlAccessType.NONE)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)

public class Ticket implements Serializable {
	//these blocks are same as orig TicketEntity, except for annotations (part of mashing TicketEntity/Ticket together)
	private static final long serialVersionUID = 1L;

    private long id;

    @NotNull(message = "{validate.ticket.customer}")
    private UserPrincipal customer;

    @NotBlank(message = "{validate.ticket.subject}")
    private String subject;

    @NotBlank(message = "{validate.ticket.body}")
    private String body;

    private Instant dateCreated;
    
    //chap 24, pg 720. part of what allows elimination of old Ticket DTO function- see attachment section below also
    @Valid
    private List<Attachment> attachments = new ArrayList<>();
    
    @Id
    @Column(name = "TicketId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement
    @JsonProperty
    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }
    
    //see chap24 pg 720 for changed usage of UserPrincipal
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "UserId")
    @XmlElement
    @JsonProperty
    public UserPrincipal getCustomer()
    {
        return this.customer;
    }

    public void setCustomer(UserPrincipal customer)
    {
        this.customer = customer;
    }

    @Basic
    @XmlElement
    @JsonProperty
    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    @Lob
    @XmlElement
    @JsonProperty
    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }
    
    //part of what allowed elimination of Ticket DTO function as noted above
    @Convert(converter = InstantConverter.class)
    @XmlElement
    @XmlSchemaType(name = "dateTime")
    @JsonProperty
    public Instant getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated)
    {
        this.dateCreated = dateCreated;
    }
    
    //this block and above allows Ticket.java to act like a normal entity even though it is mapping a BLOB, see pg 720 and 722
    // elements somewhat similar to orig site/Ticket.java (the DTO for old TicketEntity class).  note diff
    // between old Map and new ArrayList above.
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinTable(name = "Ticket_Attachment",
            joinColumns = { @JoinColumn(name = "TicketId") },
            inverseJoinColumns = { @JoinColumn(name = "AttachmentId") })
    @OrderColumn(name = "SortKey")
    @XmlElement(name = "attachment")
    @JsonProperty
    public List<Attachment> getAttachments()
    {
        return this.attachments;
    }

    public void setAttachments(List<Attachment> attachments)
    {
        this.attachments = attachments;
    }

    public void addAttachment(Attachment attachment)
    {
        this.attachments.add(attachment);
    }

    @Transient
    public int getNumberOfAttachments()
    {
        return this.attachments.size();
    }

}
