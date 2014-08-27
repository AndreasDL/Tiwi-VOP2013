
package commandos.cards;

import commandos.ICommand;
import model.Board;
import model.interfaces.IController;
import states.OtherPlayerState;
import states.PlaceThiefState;
import utility.DevCardType;

public class AddKnightCommand implements ICommand{
    int player;
    public AddKnightCommand(int player) {
        this.player=player;
    }
    /**
     * geeft grondstoffen aan de speler die het opvraagt en toont welke tegels gerold zijn
     * @param board 
     */
    public void executeClient(IController controller , Board board) {
        board.getCurrentPlayer().removeCard(DevCardType.KNIGHT.getValue());   
        board.getCurrentPlayer().notifyYourObservers();
    }
    /**
     * geeft grondstoffen aan alle spelers
     * @param board 
     */


    public boolean executeServer(Board board, int id) {
        if(id == board.getCurrentPlayerId() && board.removeDevCard(player,DevCardType.KNIGHT)){

            board.addKnight(player);
            return true;
        }
        
        return false;
    }

    public void restoreState(IController controller, int prevCommand, boolean myTurn) {
        if(myTurn){
            controller.setState(new PlaceThiefState(controller.getState()));
        }
        else{
            controller.setState(new OtherPlayerState(controller, controller.getBoard())); 
            controller.startTimer();
        }
        controller.getBoard().setLastRolledNumber();
    }

    public String uitleg(Board b) {
        return "speler " + b.getPlayer(player).getName() + " heeft een ridder gekregen";
    }
    
}

