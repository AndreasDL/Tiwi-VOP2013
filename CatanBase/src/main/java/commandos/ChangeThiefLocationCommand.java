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

/**
 *
 * @author Samuel
 */
public class ChangeThiefLocationCommand implements ICommand{
    private static final Logger log = LoggerFactory.getLogger(ChangeThiefLocationCommand.class);
    
    int x;
    int y;

    public ChangeThiefLocationCommand(int x, int y) {
        log.debug("Entering ChangeThiefLocationCommand constructor ( x= {} y= {} )",x,y);
        this.x = x;
        this.y = y;
        log.debug("Leaving ChangThiefLocationCommand constructor");
    }
    /**
     * veranderd de locatie van de struikrover
     * @param board 
     */
    public void executeClient(IController controller ,Board board) {
        board.changeThiefLocationClient(x, y);
        log.info("Thief move to location x = {} y = {}", x , y);
        log.debug("ExcuteClient done");
    }
    /**
     * test op de server of de struikrover verplaatst kan worden en zo ja verplaatst hem naar de juiste plaats
     * @param board 
     */

    public boolean executeServer(Board board, int playerId) {
        
        return(board.changeThiefLocation(x, y));
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
        return "ChangeThiefLocation";
    }

    public String uitleg(Board b) {
        return "Rover verzet naar x: " + x + " y: " + y ;
    }
    
}
