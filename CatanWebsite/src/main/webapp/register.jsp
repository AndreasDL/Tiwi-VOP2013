<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% pageContext.setAttribute("active", "register"); %>
<%  ResourceBundle res = (ResourceBundle) getServletContext().getAttribute("lang");%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta content="/favicon.ico" itemprop="image">
        <title><%=res.getString("registerTitle")%></title>
        
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
        function validateMail(mailadres){
            re=/^[_A-Za-z0-9-]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$/
            if(!re.exec(mailadres))    {
                return false;
            }else{
                return true;
            }
        }

        function validate(form) {
                var retvalue = false;
                var bericht = document.getElementById("bericht");
                if(form.Login.value.length == 0 || form.Password.value.length == 0 || form.Email.value.length == 0 || form.Password2.value.length == 0){
                    //bericht.style.color = '#ff0000';
                    bericht.innerText = '<%=res.getString("errorEmptyField")%>';
                }else if(!validateMail(form.Email.value)){
                    //bericht.style.color = '#ff0000';
                    bericht.innerText = '<%=res.getString("errorInvalidEmail")%>';
                }else if(form.Password.value != form.Password2.value){
                    //bericht.style.color = '#ff0000';
                    bericht.innerText = '<%=res.getString("errorPasswordsMisMatch") %>';
                }else{
                    bericht.innerText= '';
                    retvalue = true;
                }
                return retvalue;
        }
        </script>
    </head>
    <body>
        <%@include file="header.jsp" %>
        <div align="container">
            <form class="form-signin" action="register.do" method="POST" onSubmit="return validate(this)">
                <h2><%= res.getString("registerHead")%> </h2>
                <table border="0">
                    <div id="bericht" style="color: #ff0000">
                    <% String text="";
                    if (request.getParameter("errorMsg") != null){
                        text = res.getString(request.getParameter("errorMsg"));
                    }%>
                    <%= text %>
                    </div>
                    <tbody>
                        <tr>
                            <td><%= res.getString("registerName")%> </td>
                            <td><input type="text" name="Login" value="" /></td>
                        </tr>
                        <tr>
                            <td><%= res.getString("registerPass")%></td>
                            <td><input type="Password" name="Password" value="" /></td>
                        </tr>
                        <tr>
                            <td><%= res.getString("registerPass2")%></td>
                            <td><input type="Password" name="Password2" value="" /></td>
                        </tr>
                        
                        <tr>
                            <td><%= res.getString("registerEmail") %></td>
                            <td><input type="email" name="Email" value="" /></td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input class="btn btn-primary" type="submit" value="<%= res.getString("registerBtn") %>" /></td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </body>
</html>
