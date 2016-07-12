package com.prod.custSuptMaven;

//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import com.prod.custSuptMaven.Ticket;
//import com.prod.custSuptMaven.TicketServlet;

public class TicketFunctions {
	//ant attempt to breakout supporting functions to TicketServlet.  not working yet.  imports/ties not right yet.
	//commented out for now.
	/*
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
*/
}
