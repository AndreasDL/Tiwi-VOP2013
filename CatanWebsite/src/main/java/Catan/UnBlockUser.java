/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Catan;

import DAO.DAO;
import DAO.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Andreas De Lille
 */
public class UnBlockUser extends HttpServlet {

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

        User u = (User) request.getSession().getAttribute("user");
        if (u != null && !u.isAdmin()){
            response.sendRedirect("/index.jsp");
        }else{
            DAO ds = (DAO)getServletContext().getAttribute("data");        
            PrintWriter out = response.getWriter();

            try {
                int id = Integer.parseInt(request.getParameter("id"));

                //uithalen en enabled
                if (ds.getUser(id) != null ){//enkel als de login bestaat
                    if ( ! ds.setBlocked(id,false) ){
                        response.sendRedirect("DBCheck.jsp?errorMsg=unBlockFail");
                    }else{
                        response.sendRedirect("DBCheck.jsp");
                    }
                }else{
                    response.sendRedirect("DBCheck.jsp?errorMsg=unBlockFail");
                }

            } finally {            
                out.close();
            }
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
