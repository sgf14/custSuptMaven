package com.prod.custSuptMaven.site.entities;
/*class notes: entity assoc w/ repo for individual ticket comments
 * introduced in chap 22, pg 658- since it also has an instant() property this entity serves as a DTO similar to TicketEntity class
 * 
 */
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "TicketComment")
public class TicketCommentEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    private long id;

    private long ticketId;

    private long userId;

    private String body;

    private Timestamp dateCreated;

    @Id
    @Column(name = "CommentId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId()
    {
        return this.id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    @Basic
    public long getTicketId()
    {
        return this.ticketId;
    }

    public void setTicketId(long ticketId)
    {
        this.ticketId = ticketId;
    }

    @Basic
    public long getUserId()
    {
        return this.userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    @Basic
    public String getBody()
    {
        return this.body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    @Basic
    public Timestamp getDateCreated()
    {
        return this.dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated)
    {
        this.dateCreated = dateCreated;
    }
}
