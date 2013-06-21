<!-- 
 Login bean usage.
 $Id: login.jsp,v 1.2 2001/11/13 20:42:08 ian Exp $
 -->
<jsp:useBean id="pwck" class="darwin.PasswordChecker"/>
<jsp:setProperty name="pwck" property="*"/>
<% if (pwck.isValidPassword()) out.println("You are logged in");
 else out.println("Login name and/or password incorrect.");
 %>
<br>
