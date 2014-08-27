<%@page import="java.util.ResourceBundle"%>
<% 
    if(request.getSession().getAttribute("user") != null){
        response.sendRedirect(response.encodeRedirectURL("/lobby.jsp"));
    }
    ResourceBundle res = (ResourceBundle) getServletContext().getAttribute("lang");
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% pageContext.setAttribute("active", "home"); %>
 <!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title><%=res.getString("indexTitle")%> </title>
        <link rel="shortcut icon" href="/favicon.ico">
        
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
        <script type="text/javascript">

        function validate(form) {
                var retvalue = false;
                var bericht = document.getElementById("bericht");
                if(form.Login.value.length == 0 || form.Password.value.length == 0){
                    bericht.innerHTML = '<%=res.getString("errorEmptyField")%>';
                }else{
                    bericht.innerHTML = '';
                    retvalue = true;
                }
                return retvalue;
        }
        </script>
    </head>
    <body>
        <div id="fb-root"></div>
        <script>
          window.fbAsyncInit = function() {
            // init the FB JS SDK
            FB.init({
              appId      : '161941663966577',                    // App ID from the app dashboard
              channelUrl : '/channel.html',                      // Channel file for x-domain comms
              status     : false,                                // Check Facebook Login status
              xfbml      : true                                  // Look for social plugins on the page
            });
            
          };
          
          function login(){
              FB.login(function(response) {
                    if (response.status === 'connected') {
                        // connected
                        window.location = "facebooklogin.do?accessToken=" + response.authResponse.accessToken;
                    } 
              });
          }
          
          // Load the SDK asynchronously
          (function(d, s, id){
             var js, fjs = d.getElementsByTagName(s)[0];
             if (d.getElementById(id)) {return;}
             js = d.createElement(s); js.id = id;
             js.src = "//connect.facebook.net/en_US/all.js";
             fjs.parentNode.insertBefore(js, fjs);
           }(document, 'script', 'facebook-jssdk'));
           
           
        </script>
        <%@include file="header.jsp" %>
        <div class="container">
            <form class="form-signin" action="login.do" method="POST" onSubmit="return validate(this);">
                <h2><%= res.getString("indexHead")%></h2>
                <div id="bericht" style="color: red;"> 
                    <% if(request.getParameter("loginFailed") != null && request.getParameter("loginFailed").equals("true")){ %>
                        <%= res.getString("errorLogin")%>
                    <%}else if (request.getParameter("registrationComplete") != null && request.getParameter("registrationComplete").equals("true")){%>
                        <%= res.getString("indexRegistrationComplete")%>
                    <%}else if (request.getParameter("blocked") != null && request.getParameter("blocked").equals("true") ){ %>
                        <%= res.getString("indexBlocked")%>
                    <% }%>
                </div>
                <table border="0">
                    <tbody>
                        <tr>
                            <td><%=res.getString("indexUser")%></td>
                            <td colspan="2"><input type="text" name="Login" value="" /></td>
                        </tr>
                        <tr>
                            <td><%=res.getString("indexPass")%></td>
                            <td colspan="2"><input type="password" name="Password" value="" /></td>
                        </tr>
                        <tr>
                            <td><a class="btn" href="/register.jsp" style="font-style: normal;"><%=res.getString("indexRegBtn")%></a></td>
                            <td><a class="btn" href="javascript:void(0)" onclick="login()" style="font-style: normal;">Facebook</a></td>
                            <td><input class="btn btn-primary" type="submit" value="<%=res.getString("indexLogInBtn")%>" /></td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>       
    </body>
</html>
