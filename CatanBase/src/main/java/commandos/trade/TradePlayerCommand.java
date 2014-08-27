/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandos.trade;

import commandos.ICommand;
import javax.swing.JOptionPane;
import model.Board;
import model.TradeOffer;
import model.interfaces.IController;
import states.OtherPlayerState;
import states.WaitForTradeState;

/**
 *
 * @author Andreas De Lille
 */
public class TradePlayerCommand implements ICommand{
    private int playerId;
    private int otherId;
    private TradeOffer trade;
    
    public TradePlayerCommand(int sellerId,int buyerId,TradeOffer trade){
        this.playerId = sellerId;
        this.otherId = buyerId;
        this.trade = trade;
    }
    /*public TradePlayerCommand(String sellerName,String buyerName){
        this.playerName = sellerName;
        this.otherName = buyerName;
    }*/
    public int getPlayer(){
        return playerId;
    }
    public int getOtherPlayer() {
        return otherId;
    }
    public TradeOffer getTrade() {
        return trade;
    }
    public boolean isMulti(){
        return (otherId==0);
    }
    
    public void executeClient(IController controller , Board board) {
        if(otherId==0 || controller.getOwnId()==otherId){
            controller.askTrade(this);
         }        
    }


    public boolean executeServer(Board board,int id) {
       board.setTradeOver(false);
       return true;
    }
    public void executeTrade(Board board){
        board.executePlayerTrade(trade, playerId, otherId);
    }
    
    public void executeTrade(Board board,int othersId){
        board.executePlayerTrade(trade, playerId, othersId);
    }
    
    @Override
    public String toString() {
        return "Trade";
    }
    public void restoreState(IController controller, int prevCommand, boolean myTurn) {
        if(myTurn){
            controller.getBoard().getCommand(prevCommand).restoreState(controller, prevCommand-1, myTurn);
            controller.setState(new WaitForTradeState(controller.getState()));            
        }
        else{
            controller.setState(new OtherPlayerState(controller, controller.getBoard()));
            executeClient(controller, controller.getBoard());
            controller.startTimer();
            
        }
        controller.getBoard().setLastRolledNumber();
    }

    public String uitleg(Board b) {
        return "speler " + b.getPlayer(playerId).getName() + " wil ruilen met " + b.getPlayer(otherId).getName();
    }
}
