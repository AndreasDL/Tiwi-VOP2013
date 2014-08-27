/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Catan;


import Catan.database.Hasher;
import DAO.DAO;
import DAO.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tom
 */
public class Register extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        DAO db = (DAO) getServletContext().getAttribute("data");
        Hasher hash = (Hasher) getServletContext().getAttribute("hash");
        
        String name = request.getParameter("Login");
        String pass = request.getParameter("Password");
        String pass2 = request.getParameter("Password2");
        String mail = request.getParameter("Email");
        
        if (pass.equals(pass2) //wachtwoorden gelijk
                && name.length() > 0 && pass.length() > 5 && pass2.length() >5 && mail.length() > 0 //alles ingevuld
                && db.getUserByMail(mail) == null//mail niet in databank
                && db.getUser(name) == null //login niet in databank
                && mail.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
                ){
            
            request.getSession().setAttribute("name", name);
            
            
            String salt = hash.makeSalt();
            
            //email disabled tijdelijk omdat server offline is
            //db.addUser(new User(0,name,Hasher.hash(pass,salt),salt,mail,0,0,false,false,false));//indatabank steken unabled
            //response.sendRedirect("/EmailServlet.do?name=" + name + "&mail=" + mail);//pass niet doorgeven in link want zit in databank
            
            //direct enabled in db steken => mailserver kapot
            db.addUser(new User(0,name,Hasher.hash(pass,salt),salt,mail,0,0,true,false,false));
            response.sendRedirect("/index.jsp");
        }else{
            String errorMsg = "errorDefault";
            if (!pass.equals(pass2)){
                errorMsg = "errorPasswordMisMatch";
            }else if (!(name.length() > 0 && pass.length() > 0 && pass2.length() >0 && mail.length() > 0 )){
                errorMsg = "errorEmptyField";
            }else if (db.getUserByMail(mail) != null){
                errorMsg = "errorMail";
            }else if (db.getUser(name) != null){
                errorMsg = "errorName";
            }else if (!mail.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
                errorMsg = "errorInvalidMail";
            }else if (pass2.length() <= 5){
                errorMsg = "errorShortPass";
            }
            response.sendRedirect(response.encodeRedirectURL("/register.jsp?errorMsg=" + errorMsg));
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
