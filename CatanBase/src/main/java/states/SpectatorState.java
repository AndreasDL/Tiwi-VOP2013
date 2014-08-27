/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package states;

import model.Board;
import model.interfaces.IController;

/**
 *
 * @author Jens
 */
public class SpectatorState extends StandardState{

    public SpectatorState(IController controller, Board board) {
        setController(controller);
        setBoard(board);    
    }
    
    @Override
    public String toString() {
        return "Spectator";
    }
    
}
