<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<% if (session != null) {session.invalidate();} %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login Failed</title>
</head>
<body>
	<span>Login Failed.</span><a href='<%= request.getContextPath() + "/index.jsp" %>'>Try again.</a>
</body>
</html>