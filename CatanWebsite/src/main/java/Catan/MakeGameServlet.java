/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Catan;

import DAO.DAO;
import DAO.Game;
import DAO.User;
import java.awt.Color;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tom
 */
public class MakeGameServlet extends HttpServlet {

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
        Color[] colors = {Color.RED,Color.YELLOW,Color.GREEN,Color.ORANGE,Color.BLUE,Color.MAGENTA};
        User user = (User) request.getSession().getAttribute("user");
        if (user ==null){
            response.sendRedirect("/index.jsp");
        }
        else{
            DAO db = (DAO) getServletContext().getAttribute("data");
            String name = request.getParameter("Name");
            int maxPlayers = Integer.parseInt(request.getParameter("Players"));
            Calendar now = Calendar.getInstance();
            String date;
            //Date d = new Date(now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH));
            
            if (now.get(Calendar.MONTH) < 10){
                date = "" + now.get(Calendar.YEAR) + "0" + now.get(Calendar.MONTH) + now.get(Calendar.DAY_OF_MONTH);
            }else{
                date = "" + now.get(Calendar.YEAR) + now.get(Calendar.MONTH) + now.get(Calendar.DAY_OF_MONTH);
            }
                    
            Game game = new Game(0,maxPlayers, name, user,date);
            
            int id = db.addGame(game);
            if (id>0){
                db.joinGame(user, id);
                user.setGameid(id);
                int rand = new Random().nextInt(6);
                db.setColor(user.getId(), colors[rand].getRGB());
                user.setColor(colors[rand]);
                response.sendRedirect(response.encodeRedirectURL("/game.jsp"));
            }
            else{
                response.sendRedirect(response.encodeRedirectURL("/lobby.jsp"));
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
