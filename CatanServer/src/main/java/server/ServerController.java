/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import DAO.DAO;
import DAO.User;
import adapter.InterfaceAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.spi.resource.Singleton;
import commandos.ICommand;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import model.Board;
import model.interfaces.IPlayer;
import model.interfaces.IRoad;
import model.interfaces.ISettlement;
import model.interfaces.ITile;
import observer.IObservable;
import observer.IObserver;
import utility.DevCardType;
import utility.PlayerWithMessage;
//import utility.DevCardType.cards.DevCardType;

/**
 * REST Web Service
 *
 * @author Samuel
 */
@Singleton
@Path("catan/")
public class ServerController implements IObserver{

    private HashMap<Integer,Board> models;
    private Gson gson;
    private DAO dao;
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ServerController
     */
    
    public ServerController(/*@PathParam("id") String id*/) {
        
       // models = new HashMap<String,Board>();
        models = new HashMap<Integer,Board>();
        
        try {
                dao = new DAO();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        GsonBuilder gsonbuilder = new GsonBuilder();
        gsonbuilder.registerTypeAdapter(ICommand.class, new InterfaceAdapter<ICommand>());
        gsonbuilder.registerTypeAdapter(ITile.class, new InterfaceAdapter<ITile>());
        gsonbuilder.registerTypeAdapter(IPlayer.class, new InterfaceAdapter<IPlayer>());
        gsonbuilder.registerTypeAdapter(ISettlement.class, new InterfaceAdapter<ISettlement>());
        gsonbuilder.registerTypeAdapter(IRoad.class, new InterfaceAdapter<IRoad>());
        //gsonbuilder.registerTypeAdapter(DevCardType.class, new InterfaceAdapter<DevCardType>());
        gson = gsonbuilder.create();
    }
    
    @GET
    @Path("{id}/login")
    public String login(@QueryParam("pass") String pass,@PathParam("id") int id,@QueryParam("id") int playerId,@Context HttpServletRequest req) {
        HttpSession session = req.getSession();
        User u = dao.getUser(playerId);
        if(u!=null && u.getGamePass().equals(pass) && u.getGameid() == id){
            session.setAttribute("user", u);
            return "true";
        }
        else{
            return "false";
        }
    }

    /**
     * Retrieves representation of an instance of server.ServerController
     * @return an instance of java.lang.String
     */
    @POST
    @Path("{id}/doCommand")
    @Consumes("application/json")
    public void DoCommand(@PathParam("id") int id, String scommand, @Context HttpServletRequest req) {
        HttpSession session = req.getSession();
        User u = (User) session.getAttribute("user");
        if(u != null && u.getGameid() == id){
            ICommand command=gson.fromJson(scommand, ICommand.class);
            Board model = models.get(id);

            if(command.executeServer(model,u.getId())){
                model.addCommand(command);
                dao.insertCommand(id, model.getCommandSize(), scommand);
            }
        }

    }
    
    @POST
    @Path("{id}/sendMessage")
    @Consumes("application/json")
    public void sendMessage(@PathParam("id") int id, String stringwithcolor) {
        
        PlayerWithMessage stringwithcol=gson.fromJson(stringwithcolor, PlayerWithMessage.class);
        Board model = models.get(id);

        
            model.addMessages(stringwithcol);
            
        

    }
    
    @GET
    @Path("{id}/player")
    @Consumes("application/json")
    public String getPlayer(@PathParam("id") int id,@QueryParam("player") int playerId, @Context HttpServletRequest req) {
        HttpSession session = req.getSession();
        User u = (User) session.getAttribute("user");
        if(u != null && u.getGameid() == id){
            if(!models.containsKey(id)){
                if(!dao.checkIfBoardExists(id)){
                    createNewBoard(id);
                }
                else{
                    rebuildBoard(id);
                }
            }
            return gson.toJson(models.get(id).addPlayer(playerId,dao.getUser(playerId).getName()),IPlayer.class);
        }
        else{
            return null;
        }
    }
    /*
    @POST
    @Path("{id}/nextplayer")
    @Consumes("application/json")
    public void goNextPlayer(@PathParam("id") int id) {        
        models.get(id).nextPlayer();
    }
    */
    /*
    @POST
    @Path("{id}/goGameFase")
    @Consumes("application/json")
    public void goGameFase(@PathParam("id") int id, String player) {        
        models.get(id).initiateGameFase(gson.fromJson(player, String.class));
    }
    */
    @GET
    @Path("{id}/getUpdate")
    @Produces("application/json")
    public String getUpdate(@PathParam("id") int id,@QueryParam("begin")int begin, @Context HttpServletRequest req) {
//        HttpSession session = req.getSession();
//        User u = (User) session.getAttribute("user");
//        if(u != null && u.getGameid() == id){
            String JSONlist ="[";
            Board model = models.get(id);
            if(model.getCommands(begin).size()>0){
                for( ICommand cmd  : model.getCommands(begin) ){
                    JSONlist+=(gson.toJson(cmd,ICommand.class))+",";            
                }

                return JSONlist.subSequence(0,JSONlist.length()-1)+"]";        
            }
            else {
                return "[]";
            }
//        }
//        else{
//            return null;
//        }
    }
    
    
    
    @GET
    @Path("{id}/model")
    @Produces("application/json")
    public String getBoard(@PathParam("id") int id, @Context HttpServletRequest req) {
       // HttpSession session = req.getSession();
       // User u = (User) session.getAttribute("user");
       // if(u != null && u.getGameid() == id){
            if(!models.containsKey(id)){
                if(!dao.checkIfBoardExists(id)){
                    createNewBoard(id);
                }
                else{
                    rebuildBoard(id);
                }
            }
            models.get(id).removeObserver(this);
            String answer = gson.toJson(models.get(id));
            models.get(id).addObserver(this);
            return answer;
      //  }
      //  else{
      //      return null;
       // }
    }
    
    @GET
    @Path("{id}/currentPlayer")
    @Produces("application/json")
    public String getCurrentPlayer(@PathParam("id") int id, @Context HttpServletRequest req) {
//        HttpSession session = req.getSession();
//        User u = (User) session.getAttribute("user");
//        if(u != null && u.getGameid() == id){
            return gson.toJson(models.get(id).getCurrentPlayerName(),String.class);
//        }
//        else{
//            return null;
//        }
    }
    
    @GET
    @Path("{id}/tradeOver")
    @Produces("application/json")
    public String isTradeOver(@PathParam("id") int id, @Context HttpServletRequest req) {
        HttpSession session = req.getSession();
        User u = (User) session.getAttribute("user");
        if(u != null && u.getGameid() == id){
            return gson.toJson(models.get(id).isTradeOver());
        }
        else{
            return null;
        }
    }
    
    
    @GET
    @Path("{id}/bank")
    @Produces("application/json")
    public String getBankValues(@PathParam("id") int id, @Context HttpServletRequest req) {
//        HttpSession session = req.getSession();
//        User u = (User) session.getAttribute("user");
        int[] temp=new int[6];
        int[] bankvalues=models.get(id).getBankValues();
        for(int i=0;i<5;i++)
            temp[i]=models.get(id).getBankValues()[i];
        temp[5]=models.get(id).getCardSize();
//        if(u != null && u.getGameid() == id){
            return gson.toJson(temp);
//        }
//        else{
//            return null;
//        }
    }

    @GET
    @Path("{id}/playernames")
    @Produces("application/json")
    public String getPlayerNames(@PathParam("id") int id, @Context HttpServletRequest req) {
//        HttpSession session = req.getSession();
//        User u = (User) session.getAttribute("user");
//        if(u != null && u.getGameid() == id){
            return gson.toJson(models.get(id).getPlayerNames());
//        }
//        else{
//            return null;
//        }
    }
    
    @GET
    @Path("{id}/getGameFase")
    @Produces("application/json")
    public String isGameFase(@PathParam("id") int id, @Context HttpServletRequest req) {
//        HttpSession session = req.getSession();
//        User u = (User) session.getAttribute("user");
//        if(u != null && u.getGameid() == id){
            return gson.toJson(models.get(id).isGameFase());
//        }
//        else{
//            return null;
//        }
    }
    
    @GET
    @Path("{id}/wait")
    @Produces("application/json")
    public String waitForOtherPlayers(@PathParam("id") int id, @Context HttpServletRequest req) {
        HttpSession session = req.getSession();
        User u = (User) session.getAttribute("user");
        if(u != null && u.getGameid() == id){
            return gson.toJson(models.get(id).waitForOtherPlayers());
        }
        else{
            return null;
        }
    }     
    
    @GET
    @Path("{id}/allPlayers")
    @Produces("application/json")
    public String getAllPlayers(@PathParam("id") int id, @Context HttpServletRequest req) {
//        HttpSession session = req.getSession();
//        User u = (User) session.getAttribute("user");
//        if(u != null && u.getGameid() == id){
            return gson.toJson(models.get(id).getAllPlayers());
//        }
//        else{
//            return null;
//        }
    }
     
    
    @GET
    @Path("{id}/playerValues")
    @Produces("application/json")
    public String getPlayerValues(@PathParam("id") int id, @Context HttpServletRequest req) {
        //HttpSession session = req.getSession();
        //User u = (User) session.getAttribute("user");
        //if(u != null && u.getGameid() == id){
            
            return gson.toJson(models.get(id).getPlayerValues());
        //}
        //{
        //    return null;
        //}
    }
    

    @GET
    @Path("{id}/myResources")
    @Produces("application/json")
    public String getMyResources(@PathParam("id") int id,@QueryParam("player")String player, @Context HttpServletRequest req) {
        HttpSession session = req.getSession();
        User u = (User) session.getAttribute("user");
        if(u != null && u.getGameid() == id){
            return gson.toJson(models.get(id).getMyResources(u.getId()));
        }
        else{
            return null;
        }
    }
    
    @GET
    @Path("{id}/getChangedMessages")
    @Produces("application/json")
    public String getChangedMessages(@PathParam("id") int id,@QueryParam("begin")int begin) {
        return gson.toJson(models.get(id).getMessages(begin));
    }
    
    
    private void rebuildBoard(int id){
        ArrayList<String> JSONList = dao.getJsonCommands(id);                
        if(JSONList!=null){

            Type collectionType = new TypeToken<ArrayList<Integer>>(){}.getType();
            ArrayList<Integer> startForm = gson.fromJson(JSONList.get(0),collectionType);
            //System.out.println("startform size: "+startForm.size());
            Board model = new Board(startForm,id);
            List<User> users = dao.getPlayers(id);            

            for(User u : users){
                model.AddUser(u.getId(),u.getName(),u.getColor());
            }            
            for(int i=1;i < JSONList.size();i++){
                ICommand cmd = gson.fromJson(JSONList.get(i),ICommand.class);
                cmd.executeServer(model,model.getCurrentPlayerId());
                model.addCommand(cmd);
            }
            models.put(id, model);
        }
    }
    
    private void createNewBoard(int id){
        Board model = new Board(true,id);
        model.placeField();
        model.addObserver(this);
        //model.setMaxPlayers(1);
        
        
        List<User> users = dao.getPlayers(id);
        
        if(!users.isEmpty()){
            model.setMaxPlayers(users.size());
            for(User u : users){
                model.AddUser(u.getId(),u.getName(), u.getColor());
            }
            model.pickStartingPlayer();
        }
        else{
            model.setMaxPlayers(2);
        }

        //model.pickStartingPlayer();
        


        dao.insertCommand(id,0,gson.toJson(model.writeStartForm()));
        models.put(id, model);
    }
    
    @GET
    @Path("{id}/otherplayernames")
    @Produces("application/json")
    public String getOtherPlayerNames(@PathParam("id") int id,@QueryParam("id")int player, @Context HttpServletRequest req) {
        HttpSession session = req.getSession();
        User u = (User) session.getAttribute("user");
        if(u != null && u.getGameid() == id){
            return gson.toJson(models.get(id).getOtherPlayerNames(player));
        }
        else{
            return null;
        }
    }
    
    @GET
    @Path("{id}/otherplayerids")
    @Produces("application/json")
    public String getOtherPlayerIds(@PathParam("id") int id,@QueryParam("id")int player, @Context HttpServletRequest req) {
        HttpSession session = req.getSession();
        User u = (User) session.getAttribute("user");
        if(u != null && u.getGameid() == id){
            return gson.toJson(models.get(id).getOtherPlayerIds(player));
        }
        else{
            return null;
        }
    }
    
    
//    @GET
//    @Path("{id}/stealResources")
//    @Produces("application/json")
//    public String stealResources(@PathParam("id") String id,@QueryParam("player")String player) {
//        return gson.toJson(models.get(id).stealResources(player));
//    }
//    
    
    /*
    @POST
    @Path("{id}/spectator")
    @Consumes("application/json")
    public void addSpectator(@PathParam("id") String id,String spec) {
        if(!models.containsKey(id)){
            Board model = new Board(true);
            model.placeField();
            models.put(id, model);
        }
        Spectator s=gson.fromJson(spec, Spectator.class);
        models.get(id).addSpectator(s);        
    }
    */

    @Override
    public void update(IObservable o, String arg) {
        if(arg != null && arg.equals("endGame")){
            Board board = (Board) o;
            dao.endGame(board.getId());
        }
    }
}
