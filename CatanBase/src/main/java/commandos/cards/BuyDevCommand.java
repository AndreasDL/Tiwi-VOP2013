
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandos.cards;

import commandos.ICommand;
import commandos.build.MakeStartVillageCommand;
import java.util.ArrayList;
import model.Board;
import model.interfaces.IController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import states.OtherPlayerState;
import utility.DevCardType;

/**
 *
 * @author Samuel
 */
public class BuyDevCommand implements ICommand{
    private static final Logger log = LoggerFactory.getLogger(MakeStartVillageCommand.class);
    ArrayList<int[]> tellers;
    int player;
    DevCardType card;
    public BuyDevCommand(int player,DevCardType card) {
        log.debug("Entering BuyDevCommand ( player= {} )",player);
        this.player=player;
        //this.card=card;
    }

    
    public void executeClient(IController controller , Board board) {
       if(board.getCurrentPlayerId()==player) {
            board.getPlayer(player).addCard(card);
        }
    }
    

    public boolean executeServer(Board board, int id) {
        if(id == board.getCurrentPlayerId()){
            
            card=board.buyDevCard(player,card);
            return (card!=null);
        }
        return false;
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
        return "speler " + b.getPlayer(player).getName() + " heeft een ontwikkelingskaart gekocht";
    }
    
    
    
}