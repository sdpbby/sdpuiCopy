<html>
    <body>
    	<%
    		
			if (request.isUserInRole("ECCUser") && !request.isUserInRole("Monitor") && !request.isUserInRole("OpsUser") && !request.isUserInRole("OpsAdmin")) {
    			response.sendRedirect("/SDPUI/pages/eccui/eccui.jsf");
    		} else {
    			response.sendRedirect("/SDPUI/pages/home/home.jsf");
    		}
    	%>
    </body>
</html>