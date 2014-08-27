/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Ajax;

import DAO.AjaxGame;
import DAO.DAO;
import com.google.gson.Gson;
import java.util.List;
import javax.servlet.ServletContext;
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
@Path("lobby")
public class LobbyResource {
    
    
    @Context
    private ServletContext servletContext;

    /**
     * Creates a new instance of LobbyResource
     */
    public LobbyResource() {
        
    }

    /**
     * Retrieves representation of an instance of Ajax.LobbyResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        DAO db = (DAO) servletContext.getAttribute("data");
        List<AjaxGame> games = db.getAjaxGames();
        Gson gson = new Gson();
        return gson.toJson(games);
    }

    /**
     * PUT method for updating or creating an instance of LobbyResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
