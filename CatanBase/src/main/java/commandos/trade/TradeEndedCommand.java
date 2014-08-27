/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandos.trade;

import commandos.ICommand;
import model.Board;
import model.interfaces.IController;
import states.OtherPlayerState;
import states.PlaceStructureInGameFaseState;

/**
 *
 * @author Jens
 */
public class TradeEndedCommand implements ICommand{

    public void executeClient(IController controller, Board board) {
        if(controller.getCurrentServerPlayerName().equals(controller.getBoard().getCurrentPlayerName())){
            controller.getState().goNextState();
        }
        controller.syncResources();
        controller.getBoard().getCurrentPlayer().notifyObservers();
    }

    public boolean executeServer(Board board,int id) {
        return true;
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
        return "";
    }
    
}
