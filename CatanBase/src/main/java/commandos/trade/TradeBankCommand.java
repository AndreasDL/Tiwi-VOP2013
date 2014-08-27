/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandos.trade;

import commandos.ICommand;
import commandos.build.MakeVillageCommand;
import model.Board;
import model.TradeOffer;
import model.interfaces.IController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import states.OtherPlayerState;

/**
 *
 * @author Andreas De Lille
 */
public class TradeBankCommand implements ICommand{
    private static final Logger log = LoggerFactory.getLogger(MakeVillageCommand.class);
    private int player;
    private TradeOffer trade;
    /**
     * Dit commando zorgt voor een ruilactie tussen een speler en de bank.
     * @param player de speler die ruilt met de bank.
     * @param give  wat hij geeft aan de bank.
     * @param take  wat hij krijgt van de bank.
     * @param count het aantal grondstoffen dat hij krijgt. Hij zou dus 4x de count moeten geven om 1x count te krijgen.
     * 4x give for 1x take
     */
    public TradeBankCommand(int playerName){
        this.player = playerName;       
    }
    public TradeBankCommand(int player,TradeOffer trade){
        this.player = player;
        this.trade=trade;
    }    

    public void executeClient(IController controller , Board board) {
        //executeServer(board);
    }
    public boolean executeServer(Board board,int id) {
        if(id == board.getCurrentPlayerId() && id == player){   
            board.executeBankTrade(trade, player);
            return true;
        }
        else{
            System.out.println("Trade met bank mislukt!");
            return false;
        }
    }

    public void restoreState(IController controller, int prevCommand, boolean myTurn) {
        if(myTurn){
            controller.getBoard().getCommand(prevCommand).restoreState(controller, prevCommand-1, myTurn);
        }
        else{
            controller.setState(new OtherPlayerState(controller, controller.getBoard())); 
            controller.startTimer();
        }
        controller.getBoard().setLastRolledNumber();
    }

    public String uitleg(Board b) {
        return "speler " + b.getPlayer(player).getName() + " ruilt met de bank";
    }
    
}
