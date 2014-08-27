/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

/**
 * Jersey REST client generated for REST resource:ServerController
 * [testserver]<br>
 *  USAGE:
 * <pre>
 *        ClientController client = new ClientController();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Jens
 */
public class ClientController {
    private WebResource webResource;
    private Client client;

    private static final String BASE_URI = "http://testing.team02.vop.tiwi.be/game/webresources";



    public ClientController() {
        com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
        client = Client.create(config);
        webResource = client.resource(BASE_URI).path("testserver");
    }

   
    public String getText() throws UniformInterfaceException {
        WebResource resource = webResource;

       // System.out.println("gettextclient");

        return resource.accept(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

   
    public void putText(Object requestEntity) throws UniformInterfaceException {
        webResource.type(javax.ws.rs.core.MediaType.TEXT_PLAIN).put(requestEntity);

       // System.out.println("puttextclient");

    }

   
    public void close() {
        client.destroy();
    }
   
    
}
