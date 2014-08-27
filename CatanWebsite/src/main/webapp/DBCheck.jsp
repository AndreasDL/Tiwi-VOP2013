<%-- 
    Document   : DBCheck
    Created on : 15-mrt-2013, 11:24:12
    Author     : Andreas De Lille
--%>

<%@page import="DAO.DAO"%>
<%@page import="DAO.User"%>
<%@page import="java.util.List"%>
<% 
    if(request.getSession().getAttribute("user") == null){
        response.sendRedirect(response.encodeRedirectURL("/lobby.jsp"));
    }else if ( ! ((User) request.getSession().getAttribute("user")).isAdmin() ){
        response.sendRedirect("/index.jsp");
    }
    ResourceBundle res = (ResourceBundle) getServletContext().getAttribute("lang");
%>
<% pageContext.setAttribute("active", "DBCheck"); %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>AdminPage</title>
        
        <link rel="stylesheet" href="http://static.vop.tiwi.be/team02/css/bootstrap.min.css">
        <style type="text/css">
            body{
                background-color: #f5f5f5;
                padding-top: 60px; 
            }

          </style>
    </head>
    <body>
        <%@include file="header.jsp" %>
        
        <div class="container">
        <h2>Database Control Center</h2>
        <div id="bericht" style="color: red;"> 
            <% if(request.getParameter("errorMsg") != null){ %>
                <%= res.getString(request.getParameter("errorMsg"))%>
            <% }else{%>
                <%= "" %>
            <%}%>
        </div>
        <table border='1' width='100%'>
            <tr>
                <td colspan='7'><b>Lijst van Users<b></td>
            </tr>
            <tr>
                <td><b>name</b></td>
                <td><b>mail</b></td>
                <td><b>game</b></td>
                <td><b>enabled</b></td>
                <td><b>admin</b></td>
                <td><b>blocked</b></td>
                <td><b>Block/unblock</b></td>
                
            </tr>
            <% DAO ds = (DAO) getServletContext().getAttribute("data"); %>
            <% for(User u : ds.getUsers()){ %>
            <tr>
                <td><%= u.getName() %></td>
                <td><%= u.getMail() %></td>
                <td><%= u.getGameid() %></td>
                <td><%= u.isEnabled() %></td>
                <td><%= u.isAdmin() %></td>
                <td><%= u.isBlocked() %></td>
                <td><a class="btn" href='<% if (!u.isBlocked()){%><%= "/BlockUser?id=" + u.getId()%><% }else{%><%= "/UnBlockUser?id=" + u.getId()%><%}%>'>
                    <% if (u.isBlocked()){%>
                        <%= "unBlock" %>
                    <%} else {%>
                        <%= "Block" %>
                    <%}%>
                    </a></td>
            </tr>
            <% }%>
        </table>
        </div>
    </body>
</html>
