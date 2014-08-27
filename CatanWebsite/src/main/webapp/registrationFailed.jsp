<%-- 
    Document   : registrationFail
    Created on : 14-apr-2013, 2:21:18
    Author     : Andreas De Lille
--%>
<%@page import="java.util.ResourceBundle"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% ResourceBundle res = (ResourceBundle) ResourceBundle.getBundle("nl"); %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%=res.getString("registrationFailTitle")%></title>
        <link rel="stylesheet" href="http://static.vop.tiwi.be/team02/css/bootstrap.min.css">
        <style type="text/css">
            body{
                background-color: #f5f5f5;
                padding-top: 60px; 
            }
            .form-signin {
              max-width: 350px;
              margin: 0 auto 20px;
              padding: 19px 29px 29px;
              background-color: #fff;
              border: 1px solid #e5e5e5;
            }
          </style>
    </head>
    <body>
        <%@include file="header.jsp" %>
        <div class="container">
            <%=res.getString("registrationFailText")%>
            <div>
                <a class="btn btn-primary" href ="/index.jsp"><%= res.getString("registrationFailBtn")%></a>
            </div>
        </div>
    </body>
</html>
