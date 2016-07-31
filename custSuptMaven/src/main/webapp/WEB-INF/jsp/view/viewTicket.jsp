<%@ page language="java" %>
<!-- note that for Ticket and Attachment classes below, base.jspf will take care of the import so it doesnt have to be 
duplicated in each jsp file-->

<!-- jsp expression language variables used -->
<%--@elvariable id="ticketId" type="java.lang.String" --%>
<%--@elvariable id="ticket" type="com.prod.custSuptMaven.Ticket" --%>

<!-- jsp scriptlet -->
<!-- the base.jspf imports the Ticket class, project and files error below started showing up in chap 6 changes.   -->
<!-- project cleans, compiles and runs properly.  not sure yet how to clear the errors. -->

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Customer Support</title>
</head>
<body>
	<a href="<c:url value="/login?logout" />">Logout</a>
	<h2>Ticket #${ ticketId }: <c:out value="${ ticket.subject }" /></h2>
	<i> Customer Name - <c:out value="${ ticket.customerName }" /></i><br><br>
	<c:out value="${ ticket.body }" /><br><br>
	<c:if test="${ ticket.numberOfAttachments > 0 }">
		Attachments:
		<c:forEach items="${ ticket.attachments }" var="attachment" varStatus="status">
			<c:if test="${ !status.first }">, </c:if>
			<a href="<c:url value="/tickets">
				<c:param name="action" value="download" />
				<c:param name="ticketId" value="${ ticketId }" />
				<c:param name="attachment" value="${ attachment.name }" /> 
			</c:url>"><c:out value="${ attachment.name }" /></a>
		</c:forEach>
		<br><br>
	</c:if>
	
	<a href="<c:url value="/tickets" />">Return to list of tickets</a>
</body>
</html>