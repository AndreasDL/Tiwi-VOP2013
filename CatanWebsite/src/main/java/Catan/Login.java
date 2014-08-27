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
import javax.servlet.http.HttpSession;

/**
 *
 * @author tom
 */
public class Login extends HttpServlet {

    /**
     * TODO: controleren of wachtwoord en login kloppen (met databank)
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
        
        String login = request.getParameter("Login");
        String passwd = request.getParameter("Password");
        
        DAO db = (DAO) getServletContext().getAttribute("data");
        
        User u = db.getUser(login);
        if (u != null && u.getPass().equals(Hasher.hash(passwd,u.getSalt())) && u.isEnabled() && ! u.isBlocked()){
        //if (u != null && u.getPass().equals(passwd) && u.isEnabled()){
            HttpSession session = request.getSession();
            session.setAttribute("user", u);
            response.sendRedirect("/lobby.jsp");
        }else if (u != null && u.isBlocked()){
            response.sendRedirect("/index.jsp?blocked=true");
        }else{
            response.sendRedirect(response.encodeRedirectURL("/index.jsp?loginFailed=true"));
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
