<%-- ------------------------------------------------------------------------
  - login.jsp: Displays a login form for the ListManager web application. 
  - 
  - Inputs: The page expects the name of the mailing list in
  - ${applicationScope.listname} (the "listname" attribute of ServletContext).
  - it also looks for an optional error or status message in
  - ${requestScope.loginMessage} (the "loginMessage" attribute of the
  - ServletRequest
  - 
  - Outputs: When submitted, the form POSTs to "login.action", and defines
  - parameters named "email" and "password".  If the user submits with the
  - Subscribe button it defines a "subscribe" parameter.  Otherwise, if the
  - user submits with the Login button, it defines a "login" parameter.
  ----------------------------------------------------------------------  --%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %><%-- custom tag directory --%>
<html>
<head><title>ListManager: Login</title></head>
<body>
<div align="center">
<%-- Get list title from ServletContext attribute --%>
<h1>List Manager: ${applicationScope.listname}</h1>

<%-- Use the custom "box" tag to highlight the login form --%>
<tags:box title="Login" bgcolor="#ccf" border="3" margin="70" padding="20">
<%-- Get login message from request attribute and highlight with a tag. --%>
<tags:attention>${requestScope.loginMessage}</tags:attention>
<%-- The rest of the file is standard HTML --%>
<p>
Please enter an e-mail address and password to subscribe to this
mailing list or to log in and change your delivery preferences.
<form action='login.action' method="post">
  <table>         
    <tr>        <%-- First row: email address --%>
      <td align='right'>Email-address:</td>
      <td><input name='email'></td>
    </tr><tr>   <%-- Second row: password --%>
      <td align='right'>Password:</td>
      <td><input type='password' name='password'></td>
    </tr><tr>   <%-- Third row: buttons --%>
      <td align='center' colspan=2>
	<input type=submit name="subscribe" value='Subscribe'>
	<input type=submit name="login" value='Login'>
      </td>
    </tr>
  </table>
</form>
<%-- Demonstrate <jsp:include> to include servlet output in a page --%>
This page has been accessed
<jsp:include page="/Counter">
<jsp:param name="counter" value="login.jsp"/>
</jsp:include> times.
</tags:box>
</div>
</body>
</html>
