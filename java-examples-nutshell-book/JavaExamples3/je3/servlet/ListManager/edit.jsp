<%-- ---------------------------------------------------------------------
  - edit.jsp: Edit subscription preferences, unsubcribe, or log out
  -
  - Inputs: this page expects a user to be logged in and ${sessionScope.user}
  - (the "user" attribute of HttpSession) to be the User object for that user.
  - It expects ${applicationScope.listname} to contain the name of the mailing
  - list.
  - 
  - Outputs: This page displays 3 forms which POST to "edit.action",
  - "logout.action" and "unsubscribe.action" respectively.  The edit form
  - includes HTML checkboxes, and defines a parameter "html" if the user
  - indicates that she prefers HTML-formatted e-mail.  It defines the
  - parameter "digest" if the user prefers to receive message digests.  n
  - The logout and unsubscribe forms define no parameters.
  ---------------------------------------------------------------------- --%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %><%--custom tags directory--%>
<html>
<head><title>ListManager: Delivery Preferences</title><head>
<body><div align="center">
  <h1>List Manager: ${applicationScope.listname}</h1>
  <tags:box bgcolor="#ccf" border="3" margin="75" padding="20">
    <!-- This table puts the edit form on the left and stacks the -->
    <!-- unsubscribe and logout buttons on the right. -->
    <table><tr><td>
      <!-- Custom tag creates a titled box for the edit form -->
      <tags:box
	    bgcolor="#fff" border="1" margin="25" padding="20"
	    title="Preferences for ${sessionScope.user.emailAddress}">
	<!-- The edit form -->
	<form action="edit.action" method="post">
	  Send messages in HTML format:
	  <%-- Note the use of a ${} EL expression inside these tags to --%>
	  <%-- set the default state of the checkboxes --%>
	  <input type="checkbox" name="html"
		 ${sessionScope.user.prefersHTML?"checked":""}>
	  <p>
	  Send digests:
	  <input type="checkbox" name="digest"
	  ${sessionScope.user.prefersDigests?"checked":""}>
	  <p>
	  <input type="submit" value="Save Preferences">
	</form>
      </tags:box>
    </td><td>
      <!-- The unsubscribe form -->
      <form action="unsubscribe.action" method="post">
	<input type="submit"
	value="Unsubscribe ${sessionScope.user.emailAddress}">
      </form>
      <p>
      <!-- The logout form -->
      <form action="logout.action" method="post">
	<input type="submit" value="Logout ${sessionScope.user.emailAddress}">
      </form>
    </td></tr></table>
  </tags:box>
</div></body></html>
