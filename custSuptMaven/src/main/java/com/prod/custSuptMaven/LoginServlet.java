package com.prod.custSuptMaven;
//LoginServlet starts chapter 5- http sessions- pg 129
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
//suppresses SerializedUID warning on public class- comment out or see TicketServlet for alternatives
@SuppressWarnings("serial")
@WebServlet (
		name = "loginServlet",
		urlPatterns = "/login"
)

public class LoginServlet extends HttpServlet {
	private static final Map<String, String> userDatabase = new Hashtable<>();
	
	static {
		userDatabase.put("Nicholas", "password");
		userDatabase.put("Sarah", "drowssap");
		userDatabase.put("Mike", "wordpass");
		userDatabase.put("John", "green");
		userDatabase.put("Scott", "blue");
	}
	
	@Override
	protected void doGet (HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		HttpSession session = request.getSession();
		//logout function, see JWA pg 132
		if (request.getParameter("logout") != null) {
			session.invalidate();
			response.sendRedirect("login");
			return;
		//else login and send to /tickets- note the initial backslash not added, similar to "WEB-INF/..." below
		} else if (session.getAttribute("username") != null) {
			response.sendRedirect("tickets");
			return;
		}
		
		request.setAttribute("loginFailed", false);
		request.getRequestDispatcher("WEB-INF/jsp/view/login.jsp").forward(request, response);
	}
	
	@Override
	protected void doPost (HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute("username") != null) {
			response.sendRedirect("tickets");
			return;
		}
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		//login validation matches db. format is: if (not/fail) else (pass)
		if (username == null 
				|| password == null 
				|| !LoginServlet.userDatabase.containsKey(username)
				|| !password.equals(LoginServlet.userDatabase.get(username))
				) {
			request.setAttribute("loginFailed", true);
			request.getRequestDispatcher("WEB-INF/jsp/view/login.jsp").forward(request, response);
		} else {
			//assigns logged in user to session
			session.setAttribute("username", username);
			//session fixation attack mitigation -see JWA pg 131 and 113.
			request.changeSessionId();
			//send user to main page
			response.sendRedirect("tickets");
		}
	}
}
