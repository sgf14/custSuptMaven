<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Customer Support</title>
</head>
<body>
	<h2>Create a Ticket</h2>
	<form method="POST" action="tickets" enctype="multipart/form-data">
		<input type="hidden" name="action" value="create">
<!-- 	name field removed from ticket creation when chap 5 session added- uses logged in user name -->
<!-- 		Your Name<br> -->
<!-- 		<input type="text" name="customerName"><br><br> -->
		Subject<br>
		<input type="text" name="subject"><br><br>
		Body<br>
		<textarea name="body" rows="5" cols="30"></textarea><br><br>
		<b>Attachments</b><br>
		<input type="file" name="file1"><br><br>
		<input type="submit" value="Submit">
	</form>
</body>
</html>