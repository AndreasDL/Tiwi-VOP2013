/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandos;

import model.Board;
import model.interfaces.IController;
import states.OtherFirstBuildingState;
import states.OtherPlayerState;
import states.PlaceStructureState;
import states.RollDiceState;
import states.WaitGameState;

/**
 *
 * @author Jens
 */
public class EndTurnCommand implements ICommand{
    
    private int player;

    public EndTurnCommand(int player) {
        this.player = player;
    }   
    

    public void executeClient(IController controller , Board board) {
        
    }


    public boolean executeServer(Board board,int id) {
        if(board.getCurrentPlayerId() == id && id == player){
            board.nextPlayer(player);
            return true;
        }else{
            return false;
        }
    }
    @Override
    public String toString() {
        
        return "EndTurn";
    }

    public void restoreState(IController controller, int prevCommand,boolean myTurn) {
        if(controller.getBoard().getCommand(prevCommand).toString().equals("StartRoad")){
            PlaceStructureState state = (PlaceStructureState) controller.getState();
            int villagesLeft = controller.getBoard().getPlayer(controller.getOwnId()).getSettlementsLeft();
            if(myTurn){
                state.modifyState(villagesLeft, true);
                if(villagesLeft==0){
                    controller.setState(new WaitGameState(state));
                    controller.startTimer();
                }
            }
            else{
                if(villagesLeft==0){  //2 huizen gezet
                    state.modifyState(villagesLeft, false);
                    controller.setState(new WaitGameState(state));
                    controller.startTimer();
                }
                else{   //0 of 1 huis gezet
                    state.modifyState(villagesLeft, true);
                    controller.setState(new OtherFirstBuildingState(state));
                }
                controller.startTimer();
            }
            controller.getBoard().deselectHighlight();
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
            controller.getBoard().deselectHighlight();
        }
        
    }

    public String uitleg(Board b) {
        return "speler: " + b.getPlayer(player).getName() + " heeft zijn beurt beëindigd";
    }
    
    
}
