package com.prod.custSuptMaven;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.prod.custSuptMaven.Ticket;
//import com.prod.custSuptMaven.TicketFunctions;

@WebServlet (
		name = "ticketServlet",
		urlPatterns = {"/tickets"},
		loadOnStartup = 1
		)

@MultipartConfig(
		fileSizeThreshold = 5_242_880, //5MB
		maxFileSize = 20_971_520L, //20MB
		maxRequestSize = 41_943_040L //40MB
		)

public class TicketServlet extends HttpServlet {
	private volatile int TICKET_ID_SEQUENCE = 1;
	
	//temporary hashMap database for use until persistence is setup later in book
	public Map<Integer, Ticket> ticketDatabase = new LinkedHashMap<>();
	
	@Override
	protected void doGet (HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action == null) action = "list";
		
		switch(action) {
		case "create":
			this.showTicketForm(request, response);
			break;
		case "view":
			this.viewTicket(request, response);
			break;
		case "download":
			this.downloadAttachment(request, response);
			break;
		case "list":
		default:
			this.listTickets(response);
			break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
	if (action == null) action = "list";
		
		switch(action) {
		case "create":
			this.createTicket(request, response);
			break;	
		case "download":
		default:
			response.sendRedirect("tickets");;
			break;
		}		
	}
		
	private void showTicketForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/jsp/view/ticketForm.jsp").forward(request, response);
	}
	
	private void viewTicket(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idString = request.getParameter("ticketId");
		Ticket ticket = this.getTicket(idString, response);
		if (ticket == null)
			return;
		
		request.setAttribute("ticketId", idString);
		request.setAttribute("ticket", ticket);
		
		request.getRequestDispatcher("/WEB-INF/jsp/view/viewTicket.jsp").forward(request, response);
	}
	
	
	//see if you can break out the functions below to a supporting class. After I get basic functions working.  see Ticket Functions.java.
	// these items are not called directly by switch statment at top and seem a good candidate for refactoring.
	public Ticket getTicket(String idString, HttpServletResponse response)
            throws ServletException, IOException
    {
        if(idString == null || idString.length() == 0)
        {
            response.sendRedirect("tickets");
            return null;
        }

        try
        {
            Ticket ticket = this.ticketDatabase.get(Integer.parseInt(idString));
            if(ticket == null)
            {
                response.sendRedirect("tickets");
                return null;
            }
            return ticket;
        }
        catch(Exception e)
        {
            response.sendRedirect("tickets");
            return null;
        }
    }

}
