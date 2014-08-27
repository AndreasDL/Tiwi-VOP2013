package Catan;


import DAO.DAO;
import java.io.*;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.*;
import javax.servlet.http.*;


/**
 * Deze servlet verstuurd een bevestgingsmail naar de gebruiker.
 * De gebruiker moet dan zijn email adres bevestigen via een link die hij toegestuurd krijgt.
 * @author Andreas De Lille
 */
public class EmailServlet extends HttpServlet {

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
        
        PrintWriter out = response.getWriter();
        DAO db = (DAO) getServletContext().getAttribute("data");
        String name = "";
        
        try {
            name = (String) request.getSession().getAttribute("name");
            //String pass = (String) request.getSession().getAttribute("pass");
            String mail = (String) request.getSession().getAttribute("mail");
            
            
            Properties props = new Properties();
            props.setProperty("mail.host", "smtp.hogent.be");
            props.setProperty("mail.smtp.port", "25");
            props.setProperty("mail.smtp.auth", "false");
            Session mailSession = Session.getDefaultInstance(props);//,auth);
            MimeMessage msg = new MimeMessage(mailSession);

            msg.setText("goeiedag " + name + "\n\nKlik op de onderstaande link om uw registratie van Catan te bevestigen\n\n" 
                    + "http://" + request.getServerName() + "/MakeUserActive.do?name="+name
                    );
            msg.setSubject("Bevestig uw registratie");
            msg.setFrom(new InternetAddress("catanteam02@gmail.com"));
            msg.addRecipient(Message.RecipientType.TO,new InternetAddress(mail));

            Transport.send(msg);
            response.sendRedirect("/mailSend.jsp");
        }catch (Exception e){
            //fuck
            //user uit databank gooien
            db.removeUser(name);
            response.sendRedirect("/mailFailed.jsp");
        } finally {            
            out.close();
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
    private class SMTPAuthenticator extends Authenticator {

        private PasswordAuthentication authentication;

        public SMTPAuthenticator(String login, String password) {
            authentication = new PasswordAuthentication(login, password);
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return authentication;
        }
    }
}
