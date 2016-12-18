package com.prod.custSuptMaven.site.entities;
/* class notes- see AttachmentEntity class notes except that this acts as a DTO due since you cant persist entities w/ instant() properties.
 * see chap 21 pg 624. note difference in naming from attachment and UserPrincipal w/ entity at the end
 * .  this entity ties java code to the equivalent MySQL table via implementation
 * in DefaultTicketRepository (which in turn extends BaseRepo and HBaseJpaRepo respectively)
 *  this class is a modified POJO that allows JPA to be implemented in order to be able to save data to tables- see getters and setters below.
 *  Hibernate Validation from chap 16 also used- see annotations below
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

//the entity annotation is established in RootContextConfig class- and used here for the Ticket table specific details
@Entity
@Table(name = "Ticket")
public class TicketEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    private long id;

    private long userId;

    private String subject;

    private String body;
    //note Instant is not supported in MySQL as of Java8- so site/Ticket.java DTO class used for java.Instant and then converted to Timestamp,  see pg 624
    private Timestamp dateCreated;

    @Id
    @Column(name = "TicketId")
    //ties to auto_increment method in MySQL for unique id
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
    public long getUserId()
    {
        return this.userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    @Basic
    public String getSubject()
    {
        return this.subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
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