package com.prod.custSuptMaven.site;
/*Class notes, REST services chap 14, pg495.  implements REST services for TicketService class
 * see pg 497-500 for testing this in Firefox, via RESTClient Add-on.
 */
import com.prod.custSuptMaven.config.annotation.RestEndpoint;
import com.prod.custSuptMaven.exception.ResourceNotFoundException;
import com.prod.custSuptMaven.site.entities.Attachment;
import com.prod.custSuptMaven.site.entities.Ticket;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.inject.Inject;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@RestEndpoint
public class TicketRestEndpoint
{
    @Inject TicketService ticketService;

    @RequestMapping(value = "ticket", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> discover()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "OPTIONS,HEAD,GET,POST");
        return new ResponseEntity<>(null, headers, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "ticket/{id}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> discover(@PathVariable("id") long id)
    {
        if(this.ticketService.getTicket(id) == null)
            throw new ResourceNotFoundException();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "OPTIONS,HEAD,GET,DELETE");
        return new ResponseEntity<>(null, headers, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "ticket", method = RequestMethod.GET)
    @ResponseBody @ResponseStatus(HttpStatus.OK)
    public TicketWebServiceList read()
    {
        TicketWebServiceList list = new TicketWebServiceList();
        list.setValue(this.ticketService.getAllTickets());
        return list;
    }

    @RequestMapping(value = "ticket/{id}", method = RequestMethod.GET)
    @ResponseBody @ResponseStatus(HttpStatus.OK)
    public Ticket read(@PathVariable("id") long id)
    {
        Ticket ticket = this.ticketService.getTicket(id);
        if(ticket == null)
            throw new ResourceNotFoundException();
        return ticket;
    }

    @RequestMapping(value = "ticket", method = RequestMethod.POST)
    public ResponseEntity<Ticket> create(@RequestBody TicketForm form)
    {
        Ticket ticket = new Ticket();
        ticket.setCustomer(null); // TODO: How do you secure REST?  changed in chap 24 w/ user principal changes- REST and SOAP
        ticket.setSubject(form.getSubject());
        ticket.setBody(form.getBody());
        ticket.setAttachments(form.getAttachments());

        this.ticketService.create(ticket);

        String uri = ServletUriComponentsBuilder.fromCurrentServletMapping()
                .path("/ticket/{id}").buildAndExpand(ticket.getId()).toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uri);

        return new ResponseEntity<>(ticket, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "ticket/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long id)
    {
        if(this.ticketService.getTicket(id) == null)
            throw new ResourceNotFoundException();
        this.ticketService.deleteTicket(id);
    }

    @XmlRootElement(name = "ticket")
    public static class TicketForm
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
