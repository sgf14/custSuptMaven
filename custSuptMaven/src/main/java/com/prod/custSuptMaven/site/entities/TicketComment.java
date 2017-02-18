package com.prod.custSuptMaven.site.entities;
/* class notes- similar to entities/Ticket.Java this was introduced in chap 24 to eliminate need for orig site/TicketComment class
 * DTO function.  very similar changes- replaced /site/entities/TicketCommentEntity and site/TicketComment, so they fit within a 
 * more normal entity structure but allows Instant data type and array of large attachments 
 * In part introducing lazy loading, so not to affect app performance.  Chap 24 also allows attachmetns to be added to comments
 * and its related DB table changes.
 * 
 */
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
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
import com.prod.custSuptMaven.site.converters.InstantConverter;
import com.prod.custSuptMaven.site.entities.Attachment;
import com.prod.custSuptMaven.site.entities.UserPrincipal;
import com.prod.custSuptMaven.site.validation.NotBlank;

//see ..entities/Ticket for details on annotations
@Entity
@Table(name = "TicketComment")
@XmlRootElement(namespace = "http://example.com/xmlns/support", name = "comment")
@XmlAccessorType(XmlAccessType.NONE)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class TicketComment implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;

    private long ticketId;

    @NotNull(message = "{validate.ticket.comment.customer}")
    private UserPrincipal customer;

    @NotBlank(message = "{validate.ticket.comment.body}")
    private String body;
    
    //note use of Instant here and below- part of chap 24 changes
    private Instant dateCreated;
    
    //note use of ArrayList- part of chap 24 changes- in addition to the new ability to add attachments to both
    //tickets and ticker comments.  w/ db changes
    @Valid
    private List<Attachment> attachments = new ArrayList<>();

    @Id
    @Column(name = "CommentId")
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
    public long getTicketId()
    {
        return this.ticketId;
    }

    public void setTicketId(long ticketId)
    {
        this.ticketId = ticketId;
    }
    
    //eager fetches upfront (increasing memory usage).  lazy doesnt load until actually called
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

    @Lob
    @XmlElement
    @JsonProperty
    public String getBody()
    {
        return this.body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    @Convert(converter = InstantConverter.class)
    @XmlSchemaType(name = "dateTime")
    @XmlElement
    @JsonProperty
    public Instant getDateCreated()
    {
        return this.dateCreated;
    }

    public void setDateCreated(Instant dateCreated)
    {
        this.dateCreated = dateCreated;
    }
    
    //eager method here too, vs ticket version- which is lazy, per chap 24, pg722
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinTable(name = "TicketComment_Attachment",
            joinColumns = { @JoinColumn(name = "CommentId") },
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
