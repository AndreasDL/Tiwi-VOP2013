/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.spi.resource.Singleton;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import model.IPlayer;
import model.InterfaceAdapter;
import model.Model;

/**
 * REST Web Service
 *
 * @author Jens
 */
@Singleton
@Path("JSONtestserver")
public class JSONServerController {

    private Model model;
    private Gson gson;
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of JSONServerController
     */
    public JSONServerController() {
        model = new Model("1", "HelloWorld");
        gson = new GsonBuilder().registerTypeAdapter(IPlayer.class, new InterfaceAdapter<IPlayer>()).create();
    }

    /**
     * Retrieves representation of an instance of controller.JSONServerController
     * @return an instance of java.lang.String
     */
    @GET
    @Path("currentPlayer")
    @Produces("application/json")
    public String getCurrentPlayer() {
        return gson.toJson(model.getCurrentPlayer(),IPlayer.class);
        //return "lol";
    }
    
    @GET
    @Path("model")
    @Produces("application/json")
    public String getModel() {
        return gson.toJson(model);
       // return "model";
    }
    
    @GET
    @Path("players")
    @Produces("application/json")
    public String getPlayers() {
        return gson.toJson(model.getPlayers());
       // return "model";
    }

    /**
     * PUT method for updating or creating an instance of JSONServerController
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
