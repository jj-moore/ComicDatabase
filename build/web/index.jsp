<%@ include file="/Includes/header.jsp" %>
<c:set var="userID" value="${cookie.userID.value}" scope="session"/>
<c:if test="${userID != null}">
  <c:redirect url="/ComicServlet?action=rebuild"/>
</c:if>
<div id="login">
  <form action="LoginServlet" method="post">
    <table>
      <tr><td>Username:</td><td><input type="text" name="username"></td></tr>
      <tr><td>Password:</td><td><input type="password" name="password" id="password"></td></tr>
      <tr><td></td><td><button type="submit" id="submit">Submit</button></td></tr>
    </table>
  </form>
</div>
<%@ include file="/Includes/footer.html" %>
