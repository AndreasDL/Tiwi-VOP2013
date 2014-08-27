
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import model.IPlayer;
import model.InterfaceAdapter;
import model.Model;
import model.Player;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jens
 */
public class ModelTest {
   
    
    public static void main(String[] args) {
        IPlayer player = new Player("Team02") ;
        Gson gson = new GsonBuilder().registerTypeAdapter(IPlayer.class, new InterfaceAdapter<IPlayer>()).create();
        
        String JSONPlayer = gson.toJson(player, IPlayer.class);
        
        System.out.println(JSONPlayer);
        
        //System.out.println(player.getClass().getName());
        
        
        IPlayer playerAfter;
        
     /*   if(JSONPlayer.contains("\"type\":\"model.Player\"")){
            playerAfter = gson.fromJson(JSONPlayer, Player.class);
        }
        else
            playerAfter=null;
        
        */
        
        playerAfter = gson.fromJson(JSONPlayer, IPlayer.class);
        
        System.out.println(playerAfter.getName());
        
        
        Model model = new Model("2", "random");
        
        String JSONModel = gson.toJson(model);
        
        System.out.println(JSONModel);
        
        Model ModelAfter = gson.fromJson(JSONModel, Model.class);
        
        System.out.println(model.getCurrentPlayer().getName());
        System.out.println(ModelAfter.getCurrentPlayer().getName());
        System.out.println(ModelAfter.getText());
        
        
        String playersAfter = gson.toJson(model.getPlayers());
        System.out.println(playersAfter);
        
        
        
        String JSONlist ="[";
        for( IPlayer p  : model.getPlayers() ){
            JSONlist+=(gson.toJson(p,IPlayer.class))+",";
            
        }
        JSONlist=JSONlist.subSequence(0,JSONlist.length()-1)+"]";
        
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(JSONlist).getAsJsonArray();
       // System.out.println(array.toString());
        for( int i= 0; i<array.size();i++){
            System.out.println(gson.fromJson(array.get(i),IPlayer.class));
            
        }
        
      //  System.out.println(JSONlist);
        
        
        
    }
}
