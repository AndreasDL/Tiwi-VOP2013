<%@page import="org.springframework.web.util.HtmlUtils"%>
<%@page import="DAO.User"%>
<%@page import="java.util.ResourceBundle"%>
<%  ResourceBundle headerRes = (ResourceBundle) getServletContext().getAttribute("lang");%>
<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
    <div class="container">
        <div class="brand"><%= headerRes.getString("menuBalkLinks")%> </div>
        <div class="nav-collapse collapse">
            <ul class="nav">
              <li <%if("home".equals(pageContext.getAttribute("active"))){ %>class="active"<%} %> ><a href="/index.jsp"><%=headerRes.getString("menuHome")%> </a></li>
              <li><a href="http://static.vop.tiwi.be/team02/catan.pdf"><%=headerRes.getString("menuRules")%></a></li>
              <li <%if("register".equals(pageContext.getAttribute("active"))){ %>class="active"<%} %> ><a href="/register.jsp"><%=headerRes.getString("menuRegister")%></a></li>
              <% if ( request.getSession().getAttribute("user") != null && ((User) request.getSession().getAttribute("user")).isAdmin()){%>
                <li <%if("DBCheck".equals(pageContext.getAttribute("active"))){ %>class="active" <%} %> ><a href="/DBCheck.jsp"><%=headerRes.getString("menuDB") %> </a></li>
                <li <%if("Graph".equals(pageContext.getAttribute("active"))) { %> class="active" <%} %> ><a href="/graph.jsp"><%=headerRes.getString("menuGraph") %> </a> </li>
                <% } %>
            </ul>
        </div>
        <div class="pull-right">
            <div class="navbar-link"><%=headerRes.getString("menuText")%> 
                <% 
                if(request.getSession().getAttribute("user") != null){
                    out.print(HtmlUtils.htmlEscape(request.getSession().getAttribute("user").toString()));
                }
                else{
                    out.print(headerRes.getString("menuGuest"));
                }
                %>
            </div>
            <%if(request.getSession().getAttribute("user") != null){%>
                <div class="navbar-link"><a href="/logout.do"><%=headerRes.getString("menuLogOut")%></a></div>
            <%}%>
            <div class="navbar-link">
                <a href='/ChangeLangServlet.do?lang=fr'>FR</a>
            <!--</div><div class="navbar-link">-->
                <a href='/ChangeLangServlet.do?lang=nl'>NL</a>
            </div>
        </div>
    </div>
    </div>
</div>