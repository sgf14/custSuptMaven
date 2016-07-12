package com.prod.custSuptMaven;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

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
	private Map<Integer, Ticket> ticketDatabase = new LinkedHashMap<>();
	
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

}
