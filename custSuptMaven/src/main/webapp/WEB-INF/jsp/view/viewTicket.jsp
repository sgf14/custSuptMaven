<!-- note that for Ticket and Attachment classes below, base.jspf will take care of the import so it doesnt have to be 
duplicated in each jsp file-->
<%
	String ticketId = (String)request.getAttribute("ticketId");
	Ticket ticket = (Ticket)request.getAttribute("ticket"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Customer Support</title>
</head>
<body>
	<h2>Ticket #<%= ticketId %>: <%= ticket.getSubject() %></h2>
	<i> Customer Name - <%= ticket.getCustomerName() %></i><br><br>
	<%
		if(ticket.getNumberOfAttachments() > 0) {
			%>Attachments: <%
			int i = 0;
			for (Attachment a : ticket.getAttachments()) {
				if(i++ > 0)
					out.print(", ");
				%><a href= 
					"<c:url value="/tickets">
					<c:param name="action" value="download" />
					<c:param name="ticketId" value="<%= ticketId %>" />
					<c:param name="attachment" value="<%= a.getName() %>" />
					</c:url>"><%= a.getName() %>
				</a><br><br><%
			}
		}
	%>
	<a href="<c:url value="/tickets" />">Return to list of tickets</a>
</body>
</html>