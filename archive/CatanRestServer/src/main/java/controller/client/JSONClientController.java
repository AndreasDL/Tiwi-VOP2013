/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import model.IPlayer;
import model.InterfaceAdapter;
import model.Model;

/**
 * Jersey REST client generated for REST resource:JSONServerController
 * [JSONtestserver]<br>
 *  USAGE:
 * <pre>
 *        JSONClientController client = new JSONClientController();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Jens
 */
public class JSONClientController {
    private WebResource webResource;
    private Client client;
    private static final String BASE_URI = "http://localhost:8084/CatanRestServer/webresources";
    private Gson gson;

    public JSONClientController() {
        com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
        client = Client.create(config);
        webResource = client.resource(BASE_URI).path("JSONtestserver");
        gson = new GsonBuilder().registerTypeAdapter(IPlayer.class, new InterfaceAdapter()).create();
    }


    public void putJson(Object requestEntity) throws UniformInterfaceException {
        webResource.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(requestEntity);
    }

    
    public Model getModel() throws UniformInterfaceException {
        WebResource resource = webResource;
        return gson.fromJson( resource.path("model").accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class) , Model.class);
    }


    public IPlayer getCurrentPlayer() throws UniformInterfaceException {
        WebResource resource = webResource;
        return gson.fromJson( resource.path("currentPlayer").accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class) ,IPlayer.class );
       
        
    }
    
    /*public ArrayList<IPlayer> getCurrentPlayer() throws UniformInterfaceException {
        WebResource resource = webResource;
        return gson.fromJson( resource.path("currentPlayer").accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class) ,IPlayer.class );
       
        
    }*/
    


    public void close() {
        client.destroy();
    }

    
}
