<%@page contentType='text/html'%>
<%-- The taglib directive specifies that we're using the JSTL 1.1 core taglib.
  -- If you're using 1.0, change to "http://java.sun.com/jstl/core_rt" --%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
  <c:when test='${param.name == null}'>
    <form>
    <i>Please enter your name: </i>
    <input name="name"/>
    <input type="Submit"/>
    </form>
  </c:when>
  <c:otherwise>
    Hello ${param.name}!
  </c:otherwise>
</c:choose>
