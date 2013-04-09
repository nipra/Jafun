<html>
<title>Counter JavaBean Demo</title>
<!-- $Id: counter.jsp,v 1.2 2001/11/13 19:23:58 ian Exp $ -->
<body>
<jsp:useBean class="darwin.Counter" scope="session" id="myCount"/>
<h1>Counter JavaBean Demo</h1>
The counter is now <jsp:getProperty name="myCount" property="count"/>.
<% myCount.incr(); %>
</body>
</html>
