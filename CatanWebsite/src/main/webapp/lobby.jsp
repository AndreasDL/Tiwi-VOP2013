<%@page import="DAO.Game"%>
<%@page import="java.util.List"%>
<%@page import="DAO.User"%>
<%@page import="DAO.DAO"%>
<%@page import="org.springframework.web.util.HtmlUtils"%>
<% 
    ResourceBundle res = (ResourceBundle) getServletContext().getAttribute("lang");
    User user =(User) request.getSession().getAttribute("user");
    if(user == null){
        response.sendRedirect(response.encodeRedirectURL("/index.jsp"));
        return;
    }
    if(user.getGameid() !=0){
        response.sendRedirect(response.encodeRedirectURL("/game.jsp"));
        return;   
    }
    DAO db = (DAO) getServletContext().getAttribute("data");
    List<Game> games = db.getActiveGames();
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% pageContext.setAttribute("active", "home"); %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%=res.getString("lobbyTitle")%></title>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
        <link rel="stylesheet" href="http://static.vop.tiwi.be/team02/css/bootstrap.min.css">
        
        <style type="text/css">
            body{
                background-color: #f5f5f5;
                padding-top: 60px; 
            }
        </style>
        <script type="text/javascript">
            function newGame(){
                if($('#newgame').is(':visible')) {
                    $('#newgame').fadeOut('slow');
                }
                else{
                    $('#newgame').fadeIn('slow');
                }
            }
            
        </script>
        <script type="text/javascript">
            function loadGames(){
                $.getJSON("/ajax/lobby", 
                     function(data) {
                        table = $("#games");
                        text="";
                        for(i=0;i<data.length;i++){
                            text+="<tr><td>" + data[i]['name'] + "</td>";
                            text+="<td>";
                            for(j=0;j<data[i]['players'].length;j++){
                                text+= data[i]['players'][j]['name'] + "<br/>";
                            }
                            text+="</td>";
                            text+="<td>" + data[i]['players'].length + "/" + data[i]['maxPlayers'] + "</td>";
                            if(data[i]['status'] === 0){
                                text+="<td><a class=\"btn\" href=\"/joingame.do?id="+ data[i]['id'] +"\"><%=res.getString("lobbyBtnPlay")%></a></td>"; 
                            }
                            else{
                                 text+="<td><div class=\"btn disabled\" ><%=res.getString("lobbyBtnPlay")%></a></td>";
                            }
                            text+="<td><a class=\"btn\" href=\"/game.jsp?id="+ data[i]['id'] +"&spectate=true\"><%=res.getString("lobbyBtnSpectate")%></a></td></tr>";
                        }
                        table.empty();
                        table.prepend(text);
                     }); 
                setTimeout("loadGames()",5000);
             }
             
             $(document).ready(function() {
                    loadGames();
             });
        </script>
    </head>
    <body>
        <%@include file="header.jsp" %>
        <div class="container">
        <div class="span6 offset3">
        <h2>Lobby</h2>
        
        <table class="table" border="0">
            <thead>
                <tr style="border-bottom: black solid">
                    <th style="border-bottom: 1px dotted;"><%=res.getString("lobbyName")%></th>
                    <th style="border-bottom: 1px dotted;"><%=res.getString("lobbyHost")%></th>
                    <th style="border-bottom: 1px dotted;"><%=res.getString("lobbyPlayers")%></th>
                    <th style="border-bottom: 1px dotted;"><%=res.getString("lobbyPlay")%></th>
                    <th style="border-bottom: 1px dotted;"><%=res.getString("lobbySpectate")%></th>
                </tr>
            </thead>
            <tbody id="games">
                <% 
                    for(Game game : games){
                        %>
                        <tr>
                            <td>
                                <%= HtmlUtils.htmlEscape(game.getName()) %>
                            </td>
                            <td>
                                <% for(int i=0;i<game.getPlayers().size();i++){%><%= HtmlUtils.htmlEscape(game.getPlayers().get(i).getName()) %><br/><%}%>
                            </td>
                            <td>
                                <%=game.getPlayers().size() %>/<%=game.getMaxPlayers()%>
                            </td>
                            <td>
                                <% if(game.getStatus() == 0){ %>
                                <a class="btn" href="/joingame.do?id=<%=game.getId()%>"><%=res.getString("lobbyBtnPlay")%></a>
                                <%}else{ %>
                                <div class="btn disabled"><%=res.getString("lobbyBtnPlay")%></div>
                                <% } %>
                            </td>
                            <td>
                                <a class="btn" href="/game.jsp?id=<%=game.getId()%>?spectate=true"><%=res.getString("lobbyBtnSpectate")%></a>
                            </td>
                        </tr>
                        <%
                    }
                %>
                <%--<tr>
                    <td>Spel1</td>
                    <td>TestSpeler1</td>
                    <td>2/3</td>
                    <td><a class="btn" href="/joingame.do?id=1"><%=res.getString("lobbyBtnPlay")%></a></td>
                </tr>
                <tr>
                    <td>SpelNaam2</td>
                    <td>TestSpeler2</td>
                    <td>1/3</td>
                    <td><a class="btn" href="/joingame.do?id=2"><%=res.getString("lobbyBtnPlay")%></a></td>
                </tr>
                <tr>
                    <td>Spel3</td>
                    <td>TestSpeler3</td>
                    <td>2/4</td>
                    <td><a class="btn" href="/joingame.do?id=3"><%=res.getString("lobbyBtnPlay")%></a></td>
                </tr>--%>
            </tbody>
        </table>
        <div class="btn btn-large btn-info" onclick="newGame();"><%=res.getString("lobbyBtnPlay")%></div>
        <div id="newgame" style="display: none;">
            <form action="makegame.do" method="POST">
                <table>
                    <tr>
                        <td><%=res.getString("lobbyGameName")%></td>
                        <td><input type="text" name="Name" value="" /></td>
                    </tr>
                    <tr>
                        <td><%=res.getString("lobbyMaxPlayers")%></td>
                        <td><div align="center"><label class="radio inline"><input type="radio" name="Players" value="2" />2</label> 
                            <label class="radio inline"><input type="radio" name="Players" value="3" />3</label>  
                            <label class="radio inline"><input type="radio" name="Players" value="4" checked="true"/>4</label></div></td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td><button type="submit" class="btn"><%=res.getString("lobbyMakeGameBtn")%></button></td>
                    </tr>
                </table>
            </form>
        </div>
        </div>
        </div>
    </body>
</html>
