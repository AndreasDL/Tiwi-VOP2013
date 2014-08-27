/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandos.cards;

import commandos.ICommand;
import model.Board;
import model.interfaces.IController;
import states.MonopolyState;
import states.OtherPlayerState;
import states.PlaceStructureInGameFaseState;
import utility.TileType;

/**
 *
 * @author Samuel
 */
public class GiveResourceCommand implements ICommand{
    private int stealer;
    private int resource;
    private int cardsLeft;

    public GiveResourceCommand(int stealer,int i,int cardsLeft) {
        this.stealer = stealer;
        this.resource=i;
        this.cardsLeft=cardsLeft;
        
    }
    

    public void executeClient(IController controller , Board board) {
        
    }

    public boolean executeServer(Board board, int id) {
        if(stealer == id && board.getBank().getResource(TileType.values()[resource])>0){
            return board.GiveResources(stealer,resource);
        }
        return false;
    }

    public void restoreState(IController controller, int prevCommand, boolean myTurn) {
        if(myTurn){
            if(cardsLeft==0){
                controller.setState(new PlaceStructureInGameFaseState(controller.getState()));
                controller.notifyObservers("btn");
            }
            else{
                MonopolyState state = new MonopolyState(controller.getState(),false);
                state.setCardsLeft(1);
                controller.setState(state);
            }
        }
        else{
            controller.setState(new OtherPlayerState(controller, controller.getBoard())); 
            controller.startTimer();
        }
        controller.getBoard().setLastRolledNumber();
    }

    @Override
    public String toString() {
        return "GiveResource";
    }

    public String uitleg(Board b) {
        TileType[] types = TileType.values();
        return "speler " + b.getPlayer(stealer).getName() + " heeft " + types[resource].toString() + " gestolen";
    }
    
}
