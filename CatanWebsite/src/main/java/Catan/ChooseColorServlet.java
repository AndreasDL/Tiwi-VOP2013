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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tom
 */
public class ChooseColorServlet extends HttpServlet {

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
        User user = (User) request.getSession().getAttribute("user");
        String color = request.getParameter("color");
        if (user ==null){
            response.sendRedirect("/index.jsp");
        }
        else if(user.getGameid() == 0){
            response.sendRedirect("/lobby.jsp");
        }
        else{
            DAO db = (DAO) getServletContext().getAttribute("data");
            Game game = db.getGame(user.getGameid());
            if (color.equals("red")){
                boolean free = true;
                for(User player : game.getPlayers()){
                    if(player.getColor().equals(Color.RED)){
                        free = false;
                        break;
                    }
                }
                if(free){
                    db.setColor(user.getId(), Color.RED.getRGB());
                    user.setColor(Color.RED);
                }
            }
            else if (color.equals("yellow")){
                boolean free = true;
                for(User player : game.getPlayers()){
                    if(player.getColor().equals(Color.YELLOW)){
                        free = false;
                        break;
                    }
                }
                if(free){
                    db.setColor(user.getId(), Color.YELLOW.getRGB());
                    user.setColor(Color.YELLOW);
                }
            }
            else if (color.equals("green")){
                boolean free = true;
                for(User player : game.getPlayers()){
                    if(player.getColor().equals(Color.GREEN)){
                        free = false;
                        break;
                    }
                }
                if(free){
                    db.setColor(user.getId(), Color.GREEN.getRGB());
                    user.setColor(Color.GREEN);
                }
            }
            else if (color.equals("orange")){
                boolean free = true;
                for(User player : game.getPlayers()){
                    if(player.getColor().equals(Color.ORANGE)){
                        free = false;
                        break;
                    }
                }
                if(free){
                    db.setColor(user.getId(), Color.ORANGE.getRGB());
                    user.setColor(Color.ORANGE);
                }
            }
            else if (color.equals("blue")){
                boolean free = true;
                for(User player : game.getPlayers()){
                    if(player.getColor().equals(Color.BLUE)){
                        free = false;
                        break;
                    }
                }
                if(free){
                    db.setColor(user.getId(), Color.BLUE.getRGB());
                    user.setColor(Color.BLUE);
                }
            }
            else if (color.equals("magenta")){
                boolean free = true;
                for(User player : game.getPlayers()){
                    if(player.getColor().equals(Color.MAGENTA)){
                        free = false;
                        break;
                    }
                }
                if(free){
                    db.setColor(user.getId(), Color.MAGENTA.getRGB());
                    user.setColor(Color.MAGENTA);
                }
            }
            response.sendRedirect("/game.jsp");
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
