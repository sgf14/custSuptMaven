package com.prod.custSuptMaven.site;
/* Class notes- chap 17 SOAP (XML) version in conjunction with REST (JSON) server to server comm.
 * 
 */
import com.prod.custSuptMaven.site.entities.Attachment;
import com.prod.custSuptMaven.site.entities.Ticket;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.Namespace;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.server.endpoint.annotation.XPathParam;

import javax.inject.Inject;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Endpoint
public class TicketSoapEndpoint
{
    private static final String NAMESPACE = "http://example.com/xmlns/support";

    @Inject TicketService ticketService;

    @PayloadRoot(namespace = NAMESPACE, localPart = "ticketsRequest")
    @ResponsePayload
    public TicketWebServiceList read()
    {
        TicketWebServiceList list = new TicketWebServiceList();
        list.setValue(this.ticketService.getAllTickets());
        return list;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "ticketRequest")
    @Namespace(uri = NAMESPACE, prefix = "s")
    @ResponsePayload
    public Ticket read(@XPathParam("/s:ticketRequest/id") long id)
    {
        return this.ticketService.getTicket(id);
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "createTicket")
    @ResponsePayload
    public Ticket create(@RequestPayload CreateTicket form)
    {
        Ticket ticket = new Ticket();
        ticket.setCustomer(null); // TODO: How do you secure SOAP? changed in chap 24 w/ user principal changes- REST and SOAP
        ticket.setSubject(form.getSubject());
        ticket.setBody(form.getBody());
        if(form.getAttachments() != null)
            ticket.setAttachments(form.getAttachments());

        this.ticketService.save(ticket);

        return ticket;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "deleteTicket")
    @Namespace(uri = NAMESPACE, prefix = "s")
    public void delete(@XPathParam("/s:deleteTicket/id") long id)
    {
        this.ticketService.deleteTicket(id);
    }

    @XmlRootElement(namespace = NAMESPACE, name = "createTicket")
    public static class CreateTicket
    {
        private String subject;
        private String body;
        private List<Attachment> attachments;

        public String getSubject()
        {
            return subject;
        }

        public void setSubject(String subject)
        {
            this.subject = subject;
        }

        public String getBody()
        {
            return body;
        }

        public void setBody(String body)
        {
            this.body = body;
        }

        @XmlElement(name = "attachment")
        public List<Attachment> getAttachments()
        {
            return attachments;
        }

        public void setAttachments(List<Attachment> attachments)
        {
            this.attachments = attachments;
        }
    }
}
