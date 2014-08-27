/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandos;

import model.Board;
import model.interfaces.IController;
import states.OtherPlayerState;
import states.PlaceStructureState;
import states.RollDiceState;
import states.WaitGameState;

/**
 *
 * @author Jens
 */
public class GoGamePhaseCommand implements ICommand{

    private int playerid;

    public GoGamePhaseCommand(int playerid) {
        this.playerid = playerid;
    }
    
    public void executeClient(IController controller , Board board) {
        
    }

    public boolean executeServer(Board board,int id) {
        //if(id==playerid){
            board.initiateGameFase(playerid);
            return true;
        //}
        //else{
         //   return false;
        //}
    }

    /**
     * Zorgt ervoor dat de speler in de juiste staat terechtkomt bij opnieuw openen van zijn spel.
     * Gaat naar de RollDiceState als de speler aan de beurt is en zorgt ervoor dat elke speler zich
     * in de gamefase bevindt. Anders gaat men in de OtherPlayerState en start men de timer zodat er updates
     * binnenkomen.
     * @param controller de gebruikte controller
     * @param prevCommand   index van het vorige commando
     * @param myTurn true als het aan de speler is
     */
    public void restoreState(IController controller, int prevCommand, boolean myTurn) {
        
        
        if(!controller.getBoard().isGamePhase(controller.getOwnId())){
            PlaceStructureState state = (PlaceStructureState) controller.getState();
            state.modifyState(0, false);
            controller.setState(new WaitGameState(state));
            controller.getBoard().initiateGameFase(controller.getOwnId());
            controller.startTimer();
        }
        else{        
            if(myTurn){
                controller.setState(new RollDiceState(controller.getState()));                
                controller.notifyObservers("dice");
            }
            else{
                controller.setState(new OtherPlayerState(controller, controller.getBoard())); 
                controller.startTimer();
            }
        }
    }

    @Override
    public String toString() {
        return "GoGamePhase";
    }

    public String uitleg(Board b) {
        return "speler " + b.getPlayer(playerid).getName() + " zit nu in het spel";
    }
    
    
}
