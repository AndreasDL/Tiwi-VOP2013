/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import adapter.InterfaceAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import commandos.ICommand;
import java.lang.reflect.Type;
import java.util.ArrayList;
import model.Board;
import model.interfaces.IPlayer;
import model.interfaces.IRoad;
import model.interfaces.ISettlement;
import model.interfaces.ITile;
import utility.PlayerWithMessage;


/**
 * Jersey REST client generated for REST resource:ServerController [catan]<br>
 *  USAGE:
 * <pre>
 *        ClientController client = new ClientController();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Samuel
 */
public class ClientController implements ProxyController {
    private WebResource webResource;
    private Client client;
    private Gson gson;



    private static final String BASE_URI = /*"http://localhost:8084/webresources";//*/"http://stable.team02.vop.tiwi.be/game/webresources";




    public ClientController(int playerId, int gameId,String pass,boolean spectator) {
        ApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();  
        config.getProperties().put(ApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, true);
        client = ApacheHttpClient.create(config);        
        webResource = client.resource(BASE_URI).path("catan/"+gameId);
        GsonBuilder gsonbuilder = new GsonBuilder();
        gsonbuilder.registerTypeAdapter(ICommand.class, new InterfaceAdapter<ICommand>());
        gsonbuilder.registerTypeAdapter(ITile.class, new InterfaceAdapter<ITile>());
        gsonbuilder.registerTypeAdapter(IPlayer.class, new InterfaceAdapter<IPlayer>());
        gsonbuilder.registerTypeAdapter(ISettlement.class, new InterfaceAdapter<ISettlement>());
        gsonbuilder.registerTypeAdapter(IRoad.class, new InterfaceAdapter<IRoad>());
        //gsonbuilder.registerTypeAdapter(DevCardType.class, new InterfaceAdapter<DevCardType>());
        gson = gsonbuilder.create();
        if(!spectator){
            String response = webResource.path("login").queryParam("pass", pass).queryParam("id", Integer.toString(playerId)).get(String.class);
            if(! response.equals("true")){
                throw new IllegalAccessError();
            }
        }
    }

    @Override
    public void DoCommand(ICommand command) throws UniformInterfaceException {
        
        webResource.path("doCommand").type(javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE).post(gson.toJson(command,ICommand.class));
    }

    @Override
    public void close() {
        client.destroy();
    }

    @Override
    public ArrayList<ICommand> getUpdate(int begin) {
        WebResource resource = webResource;
        
        JsonParser parser = new JsonParser();
        String response = resource.path("getUpdate").queryParam("begin", ""+begin+"").accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        JsonArray array = parser.parse(response).getAsJsonArray();  
        ArrayList<ICommand> cmds = new ArrayList<ICommand>();
        for( int i= 0; i<array.size();i++){
            cmds.add(gson.fromJson(array.get(i),ICommand.class));            
        }
        return cmds;        
    }
    @Override
    public Board getBoard() {
        WebResource resource = webResource;
        String response = resource.path("model").accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        return gson.fromJson(response, Board.class);        
    }
    
    

    @Override
    public IPlayer getPlayer(int playerId) {
        WebResource resource = webResource;
        String response =resource.path("player").queryParam("player", Integer.toString(playerId)).type(javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE).get(String.class);
        return gson.fromJson(response, IPlayer.class);
    }
    /*
    public void goNextPlayer() {        
        webResource.path("nextplayer").type(javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE).post();
    }
    */
    /*
    public void goGameFase(String player) {        
        webResource.path("goGameFase").type(javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE).post(gson.toJson(player));
    }
    */
    @Override
     public String getCurrentPlayer() {
        WebResource resource = webResource;
        String response = resource.path("currentPlayer").accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        return gson.fromJson(response,String.class);        
    }
    
    @Override
     public boolean isTradeOver() {
        WebResource resource = webResource;
        String response = resource.path("tradeOver").accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        return gson.fromJson(response,Boolean.class);        
    }
     
    @Override
    public int[] getBankValues(){
        WebResource resource = webResource;
        String response = resource.path("bank").accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        Type collectionType = new TypeToken<int[]>(){}.getType();
        return gson.fromJson(response, collectionType);             
    }
    
    @Override
    public String[] getPlayerNames() {
        WebResource resource = webResource;
        String response = resource.path("playernames").accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        Type collectionType = new TypeToken<String[]>(){}.getType();
        return gson.fromJson(response, collectionType);      
    }
    
    @Override
    public String[] getOtherPlayerNames(int id) {
        WebResource resource = webResource;
        String response = resource.path("otherplayernames").queryParam("id", Integer.toString(id)).accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        Type collectionType = new TypeToken<String[]>(){}.getType();
        return gson.fromJson(response, collectionType);      
    }
    
    @Override
    public int[] getPlayerIds() {
        WebResource resource = webResource;
        String response = resource.path("playerids").accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        Type collectionType = new TypeToken<int[]>(){}.getType();
        return gson.fromJson(response, collectionType);      
    }
    
    @Override
    public int[] getOtherPlayerIds(int id) {
        WebResource resource = webResource;
        String response = resource.path("otherplayerids").queryParam("id", Integer.toString(id)).accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        Type collectionType = new TypeToken<int[]>(){}.getType();
        return gson.fromJson(response, collectionType);      
    }
    
    @Override
    public boolean isGameFase() {
        WebResource resource = webResource;
        String response = resource.path("getGameFase").accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        return gson.fromJson(response,Boolean.class);      
    }
    
    @Override
    public boolean waitForOtherPlayers() {
        WebResource resource = webResource;
        String response = resource.path("wait").accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        return gson.fromJson(response,Boolean.class);
                
    }
    
    @Override
    public ArrayList<IPlayer> getAllPlayers() {
        WebResource resource = webResource;
        String response = resource.path("allPlayers").accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        Type collectionType = new TypeToken<ArrayList<IPlayer>>(){}.getType();
        return gson.fromJson(response, collectionType);      
    }
    
    @Override
    public int[] getPlayerValues(){
        WebResource resource = webResource;
        String response = resource.path("playerValues").accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        Type collectionType = new TypeToken<int[]>(){}.getType();
        return gson.fromJson(response, collectionType);             
    }
    
    @Override
    public int[] getMyResources(int player){
        WebResource resource = webResource;
        String response = resource.path("myResources").queryParam("player",Integer.toString(player)).accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        Type collectionType = new TypeToken<int[]>(){}.getType();
        return gson.fromJson(response, collectionType);  
    }
    
    @Override
    public PlayerWithMessage getChangedMessages(int begin) {
        WebResource resource = webResource;
        String response = resource.path("getChangedMessages").queryParam("begin", ""+begin+"").accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
        return gson.fromJson(response, PlayerWithMessage.class);        
    }
    
    
    
//    public int[] stealResources(String playerWhoSteals,String playerToStealFrom){
//        WebResource resource = webResource;
//        String response = resource.path("stealResources").queryParam("player",playerWhoSteals).accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
//        Type collectionType = new TypeToken<int[]>(){}.getType();
//        return gson.fromJson(response, collectionType);  
//    }
    
    
    /*
    public void addSpectator(Spectator spec) {
        //System.out.println("opgeroepen");
        webResource.path("spectator").type(javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE).post(gson.toJson(spec,Spectator.class));
    }*/
    
    public void sendMessage(PlayerWithMessage sc) {
        webResource.path("sendMessage").type(javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE).post(gson.toJson(sc,PlayerWithMessage.class));
    }
    
}
