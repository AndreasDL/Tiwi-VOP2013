/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package states;

import model.Board;
import model.interfaces.IController;

/**
 *
 * @author Samuel
 */
public class OtherPlayerState extends StandardState{

    

    public OtherPlayerState(IController controller, Board board) {
        setController(controller);
        setBoard(board);
        getController().setMessages("otherPlayer", "");
        getBoard().getCurrentPlayer().setOtherPlayerState(true);        
    }

    @Override
    public String toString() {
        return "OtherPlayerState";
    }
    
    
    @Override
    public void goNextState() {
        getController().setState(new RollDiceState(this));
        getController().notifyObservers("dice");
    }
    
    
    
}
