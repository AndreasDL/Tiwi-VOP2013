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
import utility.TileType;

/**
 *
 * @author Samuel
 */
public class MonopolyStealResourceCommand implements ICommand {
    int stealer;
    int resource;

    public MonopolyStealResourceCommand(int stealer,int i) {
        this.stealer = stealer;
        resource=i;
    }
    

    public void executeClient(IController controller , Board board) {
        
    }

    public boolean executeServer(Board board,int id) {
        if (board.getCurrentPlayerId() == id && id == stealer){
            return board.StealMonopolyResources(stealer,resource);
        }
        return false;
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

    public String uitleg(Board b) {        
        TileType[] types = TileType.values();
        return "speler " + b.getPlayer(stealer).getName() + " steelt grondstof" + types[resource].toString();
    }
    
}

