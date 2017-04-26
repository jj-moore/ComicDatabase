<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="CSS/main.css">
    <title>Comic Collection</title>
  </head>
  <header>
    <img src="CSS/mvdc-header.jpg" alt="Comics are Cool"> 
    <%
      String userName = "";
      if (session.getAttribute("userName") != null)
        userName = session.getAttribute("userName") + "'s";
    %>
    <h2><%= userName %> Comic Collection</h2>
  </header>
  <body>
