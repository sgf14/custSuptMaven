<%@ page language="java" import="java.util.Map" %>
<!-- an attempt to clear error (even though already in base.jspf) didnt work -->
<%-- <%@ page import="com.prod.custSuptMaven.Ticket" %> --%>

<!-- the base.jspf imports the Ticket class, project and files error below started showing up in chap 6 changes.   -->
<!-- project cleans, compiles and runs properly.  not sure yet how to clear the errors. -->
<%
	@SuppressWarnings("unchecked")
	Map<Integer, Ticket> ticketDatabase = 
	(Map<Integer, Ticket>)request.getAttribute("ticketDatabase");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Customer Support</title>
</head>
<body>
	<a href="<c:url value="/login?logout" />">Logout</a>
	<h2>Tickets</h2>
	<a href="<c:url value="/tickets">
		<c:param name="action" value="create" />
		</c:url>">Create Ticket
	</a><br><br>
		<%
			if(ticketDatabase.size() == 0) {
				%><i>There are no tickets in the system</i><%
			} else {
				for (int id : ticketDatabase.keySet()) {
					String idString = Integer.toString(id);
					Ticket ticket = ticketDatabase.get(id);
					%>Ticket #<%= idString %>: <a href="<c:url value="/tickets">
						<c:param name="action" value="view" />
						<c:param name="ticketId" value="<%= idString %>" />
						</c:url>"><%= ticket.getSubject() %></a> (customer: 
							<%= ticket.getCustomerName() %>)<br><%
				}
			}
		%>

</body>
</html>