package Catan.database;

import DAO.DAO;
import java.util.ResourceBundle;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * Web application lifecycle listener.
 */

public class Init implements ServletContextListener {
    
    public static final String DATA = "data";
    public static final String DATABANK = "databank";
    
    /**
     * ### Method from ServletContextListener ###
     * 
     * Called when a Web application is first ready to process requests
     * (i.e. on Web server startup and when a context is added or reloaded).
     * 
     * For example, here might be database connections established
     * and added to the servlet context attributes.
     */
    @Override
    public void contextInitialized(ServletContextEvent evt) {
       
        ServletContext ctx = evt.getServletContext();
        String databankString = ctx.getInitParameter(DATABANK);
        try {
            //new OracleDriver();
            DAO database = new DAO();
            ctx.setAttribute(DATA,database);
            
            //properties voor talen
            ResourceBundle res = ResourceBundle.getBundle("nl");
            ctx.setAttribute("lang", res);
            
            //hasher
            Hasher hash = new Hasher();
            ctx.setAttribute("hash",hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        
    }

    /**
     * ### Method from ServletContextListener ###
     * 
     * Called when a Web application is about to be shut down
     * (i.e. on Web server shutdown or when a context is removed or reloaded).
     * Request handling will be stopped before this method is called.
     * 
     * For example, the database connections can be closed here.
     */
    @Override
    public void contextDestroyed(ServletContextEvent evt) {
        // TODO add your code here e.g.:
        /*
                Connection con = (Connection) e.getServletContext().getAttribute("con");
                try { con.close(); } catch (SQLException ignored) { } // close connection
        */
    }
}
