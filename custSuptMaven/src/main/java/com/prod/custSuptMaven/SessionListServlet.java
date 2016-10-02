//added in chap 5, http sessions to display list of sessions.  see pg 136
package com.prod.custSuptMaven;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(
		name = "sessionListServlet",
		urlPatterns = "/sessions"
)
public class SessionListServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		//login check replaced by chap 8 filter- pg 254
//		if(request.getSession().getAttribute("username") == null){
//			response.sendRedirect("login");
//			return;
//		}
		
		request.setAttribute("numberOfSessions", SessionRegistry.getNumberOfSessions());
		request.setAttribute("sessionList", SessionRegistry.getAllSessions());
		request.setAttribute("timestamp", System.currentTimeMillis());
		request.getRequestDispatcher("/WEB-INF/jsp/view/sessions.jsp").forward(request, response);
	}

}
