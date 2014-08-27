<%@page contentType="application/x-java-jnlp-file" %>
<?xml version="1.0" encoding="utf-8"?>  
<jnlp spec="1.0+" codebase="http://stable.team02.vop.tiwi.be/" href="jnlp.jsp?playerId=<%= request.getParameter("playerId")%>&amp;gameId=<%= request.getParameter("gameId")%>&amp;pass=<%= request.getParameter("pass")%>&amp;visitor=<%= request.getParameter("visitor")%>">
   <information>
      <title>CatanSP - Team 02</title>
      <vendor>Team02</vendor>
   </information>
    
   <resources>
      <j2se version="1.6+" href="http://java.sun.com/products/autodl/j2se"/>
      <jar href="LastWork.jar"/>
      <description>Best game ever!</description>
      <icon href="icon.jpg" />
      <icon href="icon.jpg" type="splash"/>
   </resources>

   <application-desc main-class="demos.MakeBoardDemo">  
        <argument><%= request.getParameter("playerId")%></argument>  
        <argument><%= request.getParameter("gameId")%></argument>
        <argument><%= request.getParameter("pass")%></argument>
        <argument><%= request.getParameter("visitor")%></argument>
        <%--<argument><= request.getParameter("maxPlayers") %></argument>--%>
   </application-desc>    
   <security>
        <all-permissions/>
    </security>
</jnlp>