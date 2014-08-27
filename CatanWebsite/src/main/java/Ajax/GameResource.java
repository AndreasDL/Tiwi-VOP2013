/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ajax;

import DAO.AjaxGame;
import DAO.DAO;
import DAO.User;
import com.google.gson.Gson;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;


/**
 * REST Web Service
 *
 * @author tom
 */
@Path("game")
public class GameResource {

    @Context
    private ServletContext servletContext;
    /**
     * Creates a new instance of GameResource
     */
    public GameResource() {
    }

    /**
     * Retrieves representation of an instance of Ajax.GameResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson(@Context HttpServletRequest req) {
        //HttpSession session = req.getSession();
        //User user = (User) session.getAttribute("user");
        int gameId = Integer.parseInt(req.getParameter("id"));
        DAO db = (DAO) servletContext.getAttribute("data");
        AjaxGame game = db.getAjaxGame(gameId);
        Gson gson = new Gson();
        return gson.toJson(game);
    }

    /**
     * PUT method for updating or creating an instance of GameResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
