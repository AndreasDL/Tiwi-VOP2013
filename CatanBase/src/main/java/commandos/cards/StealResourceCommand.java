/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandos.cards;

import commandos.ICommand;
import model.Board;
import model.interfaces.IController;
import states.OtherPlayerState;
import states.PlaceStructureInGameFaseState;

/**
 *
 * @author Samuel
 */
public class StealResourceCommand implements ICommand {
    int playerToStealFrom;
    int stealer;

    public StealResourceCommand(int playerToStealFrom, int stealer) {
        this.playerToStealFrom = playerToStealFrom;
        this.stealer = stealer;
    }
    

    public void executeClient(IController controller , Board board) {
        
    }

    public boolean executeServer(Board board,int id) {
        if(id == board.getCurrentPlayerId() && id == stealer){   
            board.StealResources(playerToStealFrom, stealer);
            return true;
        }else{
            return false;
        }
        
    }

    public void restoreState(IController controller, int prevCommand, boolean myTurn) {
        if(myTurn){
            controller.setState(new PlaceStructureInGameFaseState(controller.getState()));
            controller.notifyObservers("btn");
        }
        else{
            controller.setState(new OtherPlayerState(controller, controller.getBoard())); 
            controller.startTimer();
        }
        controller.getBoard().setLastRolledNumber();
    }

    @Override
    public String toString() {
        return "StealResource";
    }

    public String uitleg(Board b) {
        return "speler: " + b.getPlayer(stealer).getName() + " steelt van " + b.getPlayer(playerToStealFrom).getName() ;
    }
    
    
}
