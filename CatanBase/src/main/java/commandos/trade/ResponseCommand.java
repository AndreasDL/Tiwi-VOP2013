/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandos.trade;

import commandos.ICommand;
import javax.swing.JOptionPane;
import model.Board;
import model.interfaces.IController;
import states.OtherPlayerState;
import states.PlaceStructureInGameFaseState;
import states.WaitForTradeState;

/**
 *
 * @author Andreas De Lille
 */
public class ResponseCommand implements ICommand {
    private TradePlayerCommand trade;
    private boolean isAccepted;
    private long reactionTime;
    private boolean isMulti;
    private int playerid;
    
    public ResponseCommand(int playerid,TradePlayerCommand trade,boolean isAccepted,long reactionTime,boolean isMulti){
        this.playerid=playerid;
        this.trade = trade;
        this.isAccepted = isAccepted;
        this.reactionTime=reactionTime;
        this.isMulti=isMulti;
    }

    public void executeClient(IController controller , Board board) {
        if(trade.getPlayer()==board.getCurrentPlayerId()){
            if(isAccepted)
                JOptionPane.showMessageDialog(null,board.getPlayer(playerid).getName()+" heeft het bod geaccepteerd.","Antwoord op ruil",JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(null,board.getPlayer(playerid).getName()+" heeft het bod geweigerd.","Antwoord op ruil",JOptionPane.INFORMATION_MESSAGE);
        
        }
    }
    public boolean isAccepted(){
        return isAccepted;
    }
    public int getPlayerid() {
        return playerid;
    }
    public boolean executeServer(Board board,int id) {
       // if(id == playerid){   
            if( !isMulti ){
                if( isAccepted){
                    trade.executeTrade(board);
                }
                board.setTradeOver(true);
            }
            else{
                board.tradeAnswered(reactionTime, this);
            }
            return true;
        //}
        //else{
        //    return false;
        //}
    }
    public void restoreState(IController controller, int prevCommand, boolean myTurn) {
        boolean isTradeOver=controller.isTradeOver();
        if(myTurn){
            if(isTradeOver){
                controller.setState(new PlaceStructureInGameFaseState(controller.getState()));
                controller.notifyObservers("btn");
            }
            else{
                controller.setState(new WaitForTradeState(controller.getState()));
            }            
        }
        else{
            controller.setState(new OtherPlayerState(controller, controller.getBoard())); 
            controller.startTimer();
            if(!isTradeOver && !controller.getBoard().playerAnswered(controller.getOwnId())){
                trade.executeClient(controller, controller.getBoard());
            }
        }
        controller.getBoard().setLastRolledNumber();
    }
   public TradePlayerCommand getTrade(){
       return trade;
   }
    @Override
    public String toString() {
        return "Response";
    }
    public String uitleg(Board b) {
        if (isMulti && isAccepted){
            return "speler " + b.getPlayer(playerid).getName() + " aanvaard de multi ruil met tijd " + reactionTime;
        }else if(isMulti && !isAccepted){
            return "speler " + b.getPlayer(playerid).getName() + " aanvaard de multi ruil NIET";
        }else if(!isMulti && isAccepted){
            return "speler " + b.getPlayer(playerid).getName() + " aanvaard de ruil";
        }else{
            return "speler " + b.getPlayer(playerid).getName() + " aanvaard de ruil NIET";
        }
    }
    
}
