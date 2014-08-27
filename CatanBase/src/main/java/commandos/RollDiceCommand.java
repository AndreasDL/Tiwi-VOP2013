/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandos;

import model.Board;
import model.interfaces.IController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import states.OtherPlayerState;
import states.PlaceStructureInGameFaseState;
import states.PlaceThiefState;

/**
 *
 * @author Samuel
 */
public class RollDiceCommand implements ICommand{
    private static final Logger log = LoggerFactory.getLogger(RollDiceCommand.class);

    private int dices[]=new int[2];
    public RollDiceCommand() {
        log.debug("Entering RollDiceCommand constructor ");

        //this.controller=controller;ut 
        log.debug("Leaving RollDiceCommand constructor");
    }
    /**
     * geeft grondstoffen aan de speler die het opvraagt en toont welke tegels gerold zijn
     * @param board 
     */
    public void executeClient(IController controller , Board board) {
        //board.giveResources(dice1+dice2);
        
        log.info("Gave Resources");
        board.setDices(dices[0],dices[1]);
        board.giveResources(dices[0]+dices[1]);
        
        log.debug("executeClient done");
        
        
    }
    /**
     * geeft grondstoffen aan alle spelers
     * @param board 
     */

    public boolean executeServer(Board board,int id) {
        if(board.getCurrentPlayerId() == id){
            dices=board.giveResources();
            //System.out.println(dices[0]+"   "+dices[1]);
            log.info("gave resources on server");
            log.debug("executeServer done");
            if(dices!=null)
                return true;

        }
        return false;
    }

    public void restoreState(IController controller, int prevCommand, boolean myTurn) {
        if(myTurn){
            if((dices[0]+dices[1])==7){
                controller.setState(new PlaceThiefState(controller.getState()));
            }
            else{
                controller.setState(new PlaceStructureInGameFaseState(controller.getState()));
                controller.notifyObservers("btn");
            }            
        }
        else{
            controller.setState(new OtherPlayerState(controller, controller.getBoard())); 
            controller.startTimer();
        }
        controller.getBoard().setLastRolledNumber();
    }

    @Override
    public String toString() {
        return "RollDice";
    }

    public String uitleg(Board b) {
        return "Er werd een " + dices[0] + " en een " + dices[1] + " gegooid";
    }
    
    
    
}
