/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import commandos.ICommand;
import java.util.ArrayList;
import model.Board;
import model.interfaces.IPlayer;
import utility.PlayerWithMessage;

/**
 *
 * @author Jens
 */
public class DummyController implements ProxyController{

    private Board model;
    
    public DummyController() {
        model = new Board(true,1); 
        model.placeField();
        model.setMaxPlayers(1);
        System.out.println("USING DUMMYCONTROLLER");
    } 
    
    

    @Override
    public void DoCommand(ICommand command) {        
        if(command.executeServer(model,model.getCurrentPlayerId())){
            model.addCommand(command);            
        }
    }        
   
    
    @Override
    public ArrayList<ICommand> getUpdate(int begin) {
        return model.getCommands(begin);        
    }
    
    @Override
     public Board getBoard() {
         return model;
     }
    
   
    @Override
     public IPlayer getPlayer(int player) {
         return model.addPlayer(player,"test");
     }
     
    @Override
    public String getCurrentPlayer() {
        return model.getCurrentPlayerName();
    }
     
    @Override
    public int[] getBankValues(){
        return model.getBankValues();
    }
    
    @Override
    public String[] getPlayerNames() {
        return model.getPlayerNames();
    }
    
    @Override
    public String[] getOtherPlayerNames(int id) {
        return model.getOtherPlayerNames(id);
    }
    
    @Override
    public int[] getPlayerIds() {
        return model.getPlayerIds();
    }
    
    @Override
    public int[] getOtherPlayerIds(int id) {
        return model.getOtherPlayerIds(id);
    }
    
    @Override
    public boolean isGameFase() {
        return model.isGameFase();
    }
    
    @Override
    public boolean waitForOtherPlayers() {
        return model.waitForOtherPlayers();
    }
    
    @Override
    public ArrayList<IPlayer> getAllPlayers() {
        return model.getAllPlayers();
    }
       
    @Override
    public int[] getPlayerValues(){
        return model.getPlayerValues();
    }
         
    
    @Override
    public int[] getMyResources(int player){
        return model.getMyResources(player);
    }

    @Override
    public void close() {
        
    }

    public PlayerWithMessage getChangedMessages(int begin) {
        return null;
    }

    public void sendMessage(PlayerWithMessage sc) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isTradeOver() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
   
    
    
}
