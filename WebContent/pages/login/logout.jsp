<%@page import="java.util.*"%>
<%session.invalidate(); session = null;%>
You have logged out.

<%response.sendRedirect("/SDPUI/pages/login/login.jsf");%>

