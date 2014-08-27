/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.server;



import com.sun.jersey.spi.resource.Singleton;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import model.Model;

/**
 * REST Web Service
 *
 * @author Jens
 */
@Singleton
@Path("testserver")

public class ServerController {
    

    private Model model;
   // private static int i=0;
  //  private static String txt="leeg";

            
    //org.ow2.bonita.activate-rest-authentication false;

    
    @Context
    private UriInfo context;
    /**
     * Creates a new instance of ServerController
     */

   public ServerController() {

        model = new Model("1", "HelloWorld");     

    }

    /**
     * Retrieves representation of an instance of controller.ServerController
     * @return an instance of java.lang.String
     */
    @GET

    @Produces("text/plain")

    public String getText() {
       return model.getText();
    //    return new Model(name);
       // return txt;

    }

    /**
     * PUT method for updating or creating an instance of ServerController
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */

    @PUT   
    @Consumes("text/plain")

    public void putText( String content) {
        model.setText(content);     

    }
    
    
    
}
