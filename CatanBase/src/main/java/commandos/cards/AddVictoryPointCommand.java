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
import utility.DevCardType;

/**
 *
 * @author Samuel
 */
public class AddVictoryPointCommand implements ICommand{
    int player;
    public AddVictoryPointCommand(int player) {
        this.player=player;
    }
    /**
     * geeft grondstoffen aan de speler die het opvraagt en toont welke tegels gerold zijn
     * @param board 
     */
    public void executeClient(IController controller , Board board) {
        board.getCurrentPlayer().removeCard(DevCardType.VICTORYPOINT.getValue());   
        board.getCurrentPlayer().notifyYourObservers();
    }
    /**
     * geeft grondstoffen aan alle spelers
     * @param board 
     */

    public boolean executeServer(Board board,int id) {
        if(id == board.getCurrentPlayerId() && player == id && board.removeDevCard(player,DevCardType.VICTORYPOINT)){
            board.addVictoryPoint(player);
            return true;
        
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
        return "speler " + b.getPlayer(player).getName() + " heeft een overwinningspunt gekregen";
    }
    
}
