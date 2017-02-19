package com.prod.custSuptMaven.site;
/* Class Notes- in-memory ticket db in -v9 got moved out to its own Repository class.  such that this only has Controller (old Servlet)
 * functions. first introduced in chap 14 as the 'C' part of MVC-CSR model.  C in MVC further sudvided into CSR as described in chap 14
 * Controller will pass CSR data to V (view) jsp.  Model being reserved for Database/Repository info.
 */
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.prod.custSuptMaven.config.annotation.WebController;
import com.prod.custSuptMaven.site.entities.Attachment;
import com.prod.custSuptMaven.site.entities.Ticket;
import com.prod.custSuptMaven.site.entities.TicketComment;
import com.prod.custSuptMaven.site.entities.UserPrincipal;
import com.prod.custSuptMaven.site.validation.NotBlank;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@WebController
@RequestMapping("ticket")
public class TicketController
{
    private static final Logger log = LogManager.getLogger();

    @Inject TicketService ticketService;
    //is the default-"" and if "list" is called specifically- compare to TicketServlet in -v9 (pre-Spring) version
    //also note TicketServlet 'action' methodology replaced w/ @RequestMapping methodology
    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    public String list(Map<String, Object> model)
    {
        log.debug("Listing tickets.");
        model.put("tickets", this.ticketService.getAllTickets());

        return "ticket/list";
    }
    
    @RequestMapping(value = "search")
    public String search(Map<String, Object> model) {
    	model.put("searchPerformed", false);
    	model.put("searchForm", new SearchForm());
    	
    	return "ticket/search";
    }
    
    @RequestMapping(value = "search", params = "query")
    public String search(Map<String, Object> model, @Valid SearchForm form,
    					Errors errors, Pageable pageable){
    	if(errors.hasErrors())
    		model.put("searchPerformed", false);
    	else {
    		model.put("searchPerformed", true);
    		model.put("results", this.ticketService.search(
    				form.getQuery(), form.isUseBooleanMode(), pageable
    		));
    	}
    	
    	return "ticket/search";
    	
    }

    @RequestMapping(value = "view/{ticketId}", method = RequestMethod.GET)
    public ModelAndView view(Map<String, Object> model, Pageable page, 
                             @PathVariable("ticketId") long ticketId)
    {
        Ticket ticket = this.ticketService.getTicket(ticketId);
        if(ticket == null)
            return this.getListRedirectModelAndView();
        model.put("ticketId", Long.toString(ticketId));
        model.put("ticket", ticket);
        model.put("comments", this.ticketService.getComments(ticketId, page));
        model.put("commentForm", new CommentForm());
        return new ModelAndView("ticket/view");
    }

    @RequestMapping(
            value = "attachment/{attachmentId}",
            method = RequestMethod.GET
    )
    public View download(@PathVariable("attachmentId") long attachmentId)
    {
        Attachment attachment = this.ticketService.getAttachment(attachmentId);
        if(attachment == null)
        {
            log.info("Requested attachment {} not found.", attachmentId);
            return this.getListRedirectView();
        }

        return new DownloadingView(attachment.getName(),
                attachment.getMimeContentType(), attachment.getContents());
    }
    
    //initiates new ticket- Post method below then uses form to submit
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String create(Map<String, Object> model)
    {
        model.put("ticketForm", new TicketForm());
        return "ticket/add";
    }
    
    /*note use of Spring Form, see chap 13, pg 386
     * compare to TicketServlet class/ createTicket method.  Spring makes this simpler.
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public ModelAndView create(Principal principal, @Valid TicketForm form, 
    						Errors errors, Map<String, Object> model) 
    	throws IOException
    {
        if(errors.hasErrors())
        	return new ModelAndView("ticket/add");
        
    	Ticket ticket = new Ticket();
    	ticket.setCustomer((UserPrincipal)principal);
        ticket.setSubject(form.getSubject());
        ticket.setBody(form.getBody());
        
        //and use of Spring MultipartFile for attachment vs downloadAttachment in -v9 project
        //see pg 366 for MultipartFile details for uploading files.  compare to chap 3, utilizing a Servlet (or TicketServlet/processAttachment method)
        for(MultipartFile filePart : form.getAttachments())
        {
            log.debug("Processing attachment for new ticket.");
            Attachment attachment = new Attachment();
            attachment.setName(filePart.getOriginalFilename());
            attachment.setMimeContentType(filePart.getContentType());
            attachment.setContents(filePart.getBytes());
            if((attachment.getName() != null && attachment.getName().length() > 0) ||
                    (attachment.getContents() != null && attachment.getContents().length > 0))
                ticket.addAttachment(attachment);
        }

        try {
        	this.ticketService.save(ticket);
        } catch (ConstraintViolationException e) {
        //redirect to a view after creation
        	model.put("validationErrors", e.getConstraintViolations());
        	return new ModelAndView("ticket/add");
        }
        
        return new ModelAndView(new RedirectView(
        		"/ticket/view/" + ticket.getId(), true, false
        		));
    }
    //comments added in chap 22- spring data JPA
    @RequestMapping(value = "comment/{ticketId}", method = RequestMethod.POST)
    public ModelAndView comment(Principal principal, @Valid CommentForm form,
                                Errors errors, Map<String, Object> model,
                                Pageable page,
                                @PathVariable("ticketId") long ticketId)
                   throws IOException                      
    {
        Ticket ticket = this.ticketService.getTicket(ticketId);
        if(ticket == null)
            return this.getListRedirectModelAndView();

        if(errors.hasErrors())
            return this.view(model, page, ticketId);

        TicketComment comment = new TicketComment();
        comment.setCustomer((UserPrincipal)principal);
        comment.setBody(form.getBody());
        
        //added in chap 24 for ability to add attcmt to comments
        for(MultipartFile filePart : form.getAttachments())
        {
            log.info("Processing attachment for new ticket comment.");
            Attachment attachment = new Attachment();
            attachment.setName(filePart.getOriginalFilename());
            attachment.setMimeContentType(filePart.getContentType());
            attachment.setContents(filePart.getBytes());
            if((attachment.getName() != null && attachment.getName().length() > 0) ||
                    (attachment.getContents() != null && attachment.getContents().length > 0))
                comment.addAttachment(attachment);
        }

        try
        {
            this.ticketService.save(comment, ticketId);
        }
        catch(ConstraintViolationException e)
        {
            model.put("validationErrors", e.getConstraintViolations());
            log.info("Error adding new ticket comment. ", e);
            return this.view(model, page, ticketId);
        }

        return new ModelAndView(new RedirectView(
                "/ticket/view/" + ticketId, true, false
        ));
    }

    private ModelAndView getListRedirectModelAndView()
    {
        return new ModelAndView(this.getListRedirectView());
    }

    private View getListRedirectView()
    {
        return new RedirectView("/ticket/list", true, false);
    }
    
    //spring form class-called/used above in get and post- utilizing MultipartFile for file attachment vs. Ticket.java class in -v9
    public static class TicketForm
    {
    	@NotBlank(message = "{validate.ticket.subject}")
    	private String subject;
    	@NotBlank(message = "{validate.ticket.body}")
    	private String body;
    	@NotNull(message = "{validate.ticket.attachments}")
    	private List<MultipartFile> attachments;

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

        public List<MultipartFile> getAttachments()
        {
            return attachments;
        }

        public void setAttachments(List<MultipartFile> attachments)
        {
            this.attachments = attachments;
        }
    }
    //comment form- chap 22- supporting method
    public static class CommentForm
    {
        @NotBlank(message = "{validate.ticket.comment.body}")
        private String body;
        //attachments added in chap 24
        @NotNull(message = "{validate.ticket.comment.attachments}")
        private List<MultipartFile> attachments;

        public String getBody()
        {
            return body;
        }

        public void setBody(String body)
        {
            this.body = body;
        }
        
        //added by chap 24- atmts on ticket comments
        public List<MultipartFile> getAttachments()
        {
            return attachments;
        }

        public void setAttachments(List<MultipartFile> attachments)
        {
            this.attachments = attachments;
        }
    }
    
    //created search form and supporting methods as part of chap 23 fulltext search function and assoc search.jsp
    public static class SearchForm {
    	@NotBlank(message = "{validate.ticket.search.query}")
    	private String query;
    	private boolean useBooleanMode;
    	
    	public String getQuery() {
    		return query;
    	}
    	public void setQuery(String query) {
    		this.query = query;
    	}
    	public boolean isUseBooleanMode() {
    		return useBooleanMode;
    	}
    	public void setUseBooleanMode(boolean useBooleanMode) {
    		this.useBooleanMode = useBooleanMode;
    	}
    }
}
