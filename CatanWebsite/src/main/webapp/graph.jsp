<%-- 
    Document   : graph
    Created on : 10-mei-2013, 17:26:56
    Author     : Andreas De Lille
--%>
<%@page import="com.google.gson.JsonObject"%>
<%@page import="DAO.DAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% 
    if(request.getSession().getAttribute("user") == null){
        response.sendRedirect(response.encodeRedirectURL("/lobby.jsp"));
    }else if ( ! ((User) request.getSession().getAttribute("user")).isAdmin() ){
        response.sendRedirect("/index.jsp");
    }
    ResourceBundle res = (ResourceBundle) getServletContext().getAttribute("lang");
%>
<% pageContext.setAttribute("active", "Graph");%>

<!DOCTYPE html>

<html>
    <head>
        <link rel="stylesheet" href="http://static.vop.tiwi.be/team02/css/bootstrap.min.css">
        <link rel="shortcut icon" href="/favicon.ico">

        <script src="/RGraph/libraries/RGraph.common.core.js" ></script>
        <script src="/RGraph/libraries/RGraph.line.js" ></script>
        <!--[if lt IE 9]><script src="../excanvas/excanvas.js"></script><![endif]-->

        <title>A basic Line chart</title>
        <script>
            <% DAO ds = (DAO) getServletContext().getAttribute("data");
                String[] labels = ds.getLabels();
                int[] values = ds.getValues();
            %>
        </script>
        <meta name="description" content="A basic Line chart" />
    </head>
    <body>
        <%@include file="header.jsp" %>
        
        
        <div class="container">
            <br>
            <br>
            <br>
            <h1>Aantal spellen actief</h1>
            <canvas id="cvs" width="1000" height="500">[No canvas support]</canvas>
            <script>
                window.onload = function ()
                {
                    var line = new RGraph.Line('cvs', [<% for (int i = 0; i < labels.length; i++) {%><%=values[i]%>,<%}%>])
                    .Set('labels', [<% for (int i = 0; i < values.length; i += values.length / 5) {%>'<%=labels[i]%>',<%}%>]).Draw();
                }
            </script>
<!--
            <table border="2px">
                <tr><td><b>Datum &nbsp;</b></td><td><b>Aantal &nbsp;</b></td></tr>
                < % for (int i = 0; i < labels.length; i++) {%>
                <tr><td>< %=labels[i]%></td><td>< %=values[i]%></td></tr>
                < %} %>
            </table>
-->
        </div>
    </body>
</html>
