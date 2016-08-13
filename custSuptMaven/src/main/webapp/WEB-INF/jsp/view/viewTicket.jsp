<!-- jsp expression language variables used -->
<%--@elvariable id="ticketId" type="java.lang.String" --%>
<%--@elvariable id="ticket" type="com.prod.custSuptMaven.Ticket" --%>

<template:basic htmlTitle="${ticket.subject}" bodyTitle="Ticket # ${ticketId}: ${ticket.subject}">
	<i> Customer Name - <c:out value="${ticket.customerName}" /><br>
	Created <wrox:formatDate value="${ticket.dateCreated}" type="both"
		timeStyle="long" dateStyle="full" /></i><br>
	<c:out value="${ticket.body}" /><br><br>
	<c:if test="${ticket.numberOfAttachments > 0}">
		Attachments:
		<c:forEach items="${ticket.attachments}" var="attachment" varStatus="status">
			<c:if test="${!status.first}">, </c:if>
			<a href="<c:url value="/tickets">
				<c:param name="action" value="download" />
				<c:param name="ticketId" value="${ticketId}" />
				<c:param name="attachment" value="${attachment.name}" /> 
			</c:url>"><c:out value="${attachment.name}" /></a>
		</c:forEach>
		<br><br>
	</c:if>
</template:basic>