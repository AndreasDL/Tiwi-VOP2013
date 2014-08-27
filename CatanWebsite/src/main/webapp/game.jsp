<%@page import="java.util.List"%>
<%@page import="DAO.Game"%>
<%@page import="DAO.User"%>
<%@page import="DAO.DAO"%>
<%@page import="org.springframework.web.util.HtmlUtils"%>
<% 
    User user =(User) request.getSession().getAttribute("user");
    if(user == null){
        response.sendRedirect(response.encodeRedirectURL("/index.jsp"));
        return;
    }
    String spectate = request.getParameter("spectate");
    boolean spectator = (spectate != null && Boolean.parseBoolean(spectate));
    
    DAO db = (DAO) getServletContext().getAttribute("data");
    Game game;
    int id;
    if(spectator){
        id = Integer.parseInt(request.getParameter("id"));
        game = db.getGame(id);
    }
    else{
        id=user.getGameid();

        
        game = db.getGame(id);

        if(game == null){
            user.setGameid(0);
            response.sendRedirect(response.encodeRedirectURL("/lobby.jsp"));
            return;
        }

        //List<User> players = db.getPlayers(id);

        //Kijken of de speler niet gekickd is door de host 
        boolean allowed = false;
        for (int i=0; i<game.getPlayers().size();i++){
            if(game.getPlayers().get(i).getId() == user.getId()){
                allowed = true;
            }
        }
        if(!allowed){
            response.sendRedirect(response.encodeRedirectURL("/leavegame.do" ));
            return;
        } 
    }
    //Spel beginnen als host al op start geduwd heeft
    if(game.getStatus() == Game.PLAYING){
        if(!spectator){
            response.sendRedirect(response.encodeRedirectURL("/startgame.do"));
            return;
        }
        else{
            response.sendRedirect(response.encodeRedirectURL("/startgame.do?spectator=true&id=" + id));
            return;
        }
    }
%>
<%  ResourceBundle res = (ResourceBundle) getServletContext().getAttribute("lang");%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% pageContext.setAttribute("active", "home"); %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Kolonisten Van Catan - Spel</title>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
        <link rel="stylesheet" href="http://static.vop.tiwi.be/team02/css/bootstrap.min.css">
        <style type="text/css">
            body{
                background-color: #f5f5f5;
                padding-top: 60px; 
            }
        </style>
        <script type="text/javascript">
            started = false;
            spectator = <%= Boolean.toString(spectator) %>
            CurrentPlayer = <%= user.getId() %>;
            function loadGame(){
                $.getJSON("/ajax/game?id=<%=game.getId()%>", 
                     function(data) {
                        allowed=false;
                        for(i=0;i<data['players'].length;i++){
                            if(data['players'][i]['id'] === CurrentPlayer){
                                allowed =true;
                            }
                        }
                        if(!allowed && !spectator){
                            window.location = "/lobby.jsp";
                        }
                        else if(data["status"] === 1 && !started){
                            started = true;
                            if(!spectator){
                                window.location = "/startgame.do";
                            }
                            else{
                                window.location = "/startgame.do?id=<%= id%>&spectator=true"
                            }
                        }
                        text = "";
                        for(i=0;i<data['players'].length;i++){
                            text+= "<li><span style=\"color: #" + data['players'][i]['rgb'] + "\">" + data['players'][i]['name'] + "</span>";
                            if(CurrentPlayer === data['host']['id'] && CurrentPlayer !== data['players'][i]['id']){
                                text+= "<a class=\"btn btn-mini\" href=\"kickplayer.do?id="+ data['players'][i]['id'] +"\">kick</a>";
                            }
                            text+="</li>"
                        }
                        $("#players").empty();
                        $("#players").prepend(text);
                        $("#maxPlayers").empty();
                        $("#maxPlayers").prepend(data['players'].length + "/" + data['maxPlayers']);
                        if(data['players'].length >=  2){
                            $("#startBtn").removeClass("disabled");
                            $("#startBtn").prop('href',"startgame.do");
                        }
                        else{
                            $("#startBtn").addClass("disabled");
                            $("#startBtn").prop('href',"");
                        }
                        
                     }); 
                setTimeout("loadGame()",5000);
             }
             
             $(document).ready(function() {
                    loadGame();
             });
        </script>
        <script type="text/javascript">
            $(document).ready(function() {
                var canvas = document.getElementById('canvas_picker').getContext('2d');
                var img = new Image();
                img.src = 'http://static.vop.tiwi.be/team02/colormap.gif';
                $(img).load(function(){
                  canvas.drawImage(img,0,0);
                });
            });
        </script>
    </head>
    <body>
        <%@include file="header.jsp" %>
        <div class="container">
            <div class="offset3">
            <h2>Spel</h2>
            <div class="row">
                <div class="span3">                    
                    <h3>Naam</h3>
                    <div id="name"><%= HtmlUtils.htmlEscape(game.getName())%></div>
                    <div>
                        <h3 >Spelers <small id="maxPlayers"><%= game.getPlayers().size() %>/<%= game.getMaxPlayers() %></small></h3>
                        <ul id="players">
                        <% for(int i=0;i<game.getPlayers().size();i++){ %>
                        <li><span style="color: #<%= Integer.toHexString(game.getPlayers().get(i).getColor().getRGB()).substring(2) %>"><%= HtmlUtils.htmlEscape(game.getPlayers().get(i).getName()) %><% if(user.getId() == game.getHost().getId() && user.getId() != game.getPlayers().get(i).getId()){%></span><a class="btn btn-mini" href="kickplayer.do?id=<%= game.getPlayers().get(i).getName() %>">kick</a><%}%></li>
                        <% } %>
                        </ul>
                    </div>
                </div>
                <div class="span3">
                    <% if(user.getId() == game.getHost().getId()){%>
                    <a id="startBtn" <%if(game.getPlayers().size() >=game.getMaxPlayers()){%>href="startgame.do"<%}%> class="btn btn-primary btn-large <%if(game.getPlayers().size() <game.getMaxPlayers()){%>disabled<%}%>">Start Spel</a>
                        <a href="destroygame.do" class="btn btn-danger">Verwijder Spel</a>
                    <%}else{%>
                        <a href="leavegame.do" class="btn btn-danger">Verlaat Spel</a>
                    <%}%>
                </div>
            </div>
            <div class="row">
                <div class="span3">
                    <h3>Kleur</h3>
                    <div class="btn-group">
                        <a href="/choosecolor.do?color=red" class="btn"><div style="background-color: red;width: 20px">&nbsp;</div></a>
                        <a href="/choosecolor.do?color=yellow" class="btn"><div style="background-color: yellow;width: 20px">&nbsp;</div></a>
                        <a href="/choosecolor.do?color=green" class="btn"><div style="background-color: green;width: 20px">&nbsp;</div></a>
                        <a href="/choosecolor.do?color=orange" class="btn"><div style="background-color: orange;width: 20px">&nbsp;</div></a>
                        <a href="/choosecolor.do?color=blue" class="btn"><div style="background-color: blue;width: 20px">&nbsp;</div></a>
                        <a href="/choosecolor.do?color=magenta" class="btn"><div style="background-color: magenta;width: 20px">&nbsp;</div></a>
                    </div>
                </div>
            </div>
            </div>
        </div>
    </body>
</html>