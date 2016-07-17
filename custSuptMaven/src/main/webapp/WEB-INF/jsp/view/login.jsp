<%@ page language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Customer Support</title>
</head>
<body>
	<h2>Login</h2>
	You must login to access the customer support site.<br><br>
	<%
		if (((Boolean)request.getAttribute("loginFailed"))) {
			%>
			<b>The username or password you entered are not correct.  Please
			try again.</b><br><br>
			<%
		}
	%>
	<form method="POST" action="<c:url value="/login" />">
		Username<br>
		<input type="text" name="username" /><br><br>
		Password<br>
		<input type="text" name="password" /><br><br>
		<input type="submit" value="Log In"/>
	</form>
</body>
</html>