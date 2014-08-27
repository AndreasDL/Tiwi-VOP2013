/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package states;

import commandos.ICommand;
import commandos.cards.StealResourceCommand;
import java.util.ArrayList;
import model.interfaces.IState;

/**
 *
 * @author Samuel
 */
class ChoosePlayerState extends StandardState {
    int k;
    int j;
    public ChoosePlayerState(IState state, int i, int j) {
        setBoard(state.getBoard());
        setController(state.getController());
        this.k=i;
        this.j=j;
        getController().setMessages("clickvillage", "");
    }
    @Override
    public ICommand placeSettlements(ArrayList<int[]> tellers) {
        ICommand command=null;
        
        for(int i=0;i<tellers.size();i++){
            if(tellers.get(i)[0]==k&&tellers.get(i)[1]==j) {
                if(getBoard().getTile(tellers.get(i)[0], tellers.get(i)[1]).getSettlement(tellers.get(i)[2])!=null){
                    command=new StealResourceCommand(getBoard().getTile(tellers.get(i)[0], tellers.get(i)[1]).getSettlement(tellers.get(i)[2]).getPlayer().getId(), getBoard().getCurrentPlayerId());
                    getController().notifyObservers("btn");
                    getBoard().getCurrentPlayer().setOtherPlayerState(false);
                    getController().setState(new PlaceStructureInGameFaseState(this));
                    return command;
                }
            }
        }
        return null;
    }
    
}
