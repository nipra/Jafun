<%@page language='java' contentType='text/html'%>
<% // Begin scriptlet: this is Java code 
if (request.getParameter("name") == null) {  // If no name, request one
// Note that the scriptlet ends in the middle of a block.
%>
<%-- Now we switch to HTML tags and java expressions --%>
<form>
<i>Please enter your name: </i><input name="name">
<input type="Submit">
</form>
<%
// Back to Java code
} else {  // Otherwise, if there was a name parameter, greet the user
%>
Hello <%= request.getParameter("name") %>
<% } // end the Java if/else statement %>
