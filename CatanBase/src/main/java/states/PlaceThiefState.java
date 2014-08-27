/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package states;

import commandos.ChangeThiefLocationCommand;
import commandos.ICommand;
import commandos.cards.StealResourceCommand;
import java.util.HashSet;
import model.Board;
import model.interfaces.IState;

/**
 *
 * @author Samuel
 */
public class PlaceThiefState extends StandardState {

    public PlaceThiefState(Board board) {
        setBoard(board);
    }
    public PlaceThiefState(IState state) {
        setBoard(state.getBoard());
        setController(state.getController());
        getController().setMessages("placeThief", "");
    }

    
    
    @Override
    public ICommand placeTile(int i, int j) {
            ICommand command=new ChangeThiefLocationCommand(i, j);
            if(getBoard().canChangeThiefLocationClient(i, j)){
            
            getController().setState(new PlaceStructureInGameFaseState(this));
            HashSet<Integer> players=getBoard().getTile(i, j).containsOtherPlayer(getBoard().getCurrentPlayerId());
            getBoard().getCurrentPlayer().setOtherPlayerState(false);
            if(players.isEmpty()){
                getController().notifyObservers("btn");
            }
            else if(players.size()==1){

                getController().doCommand(command);

                command=new StealResourceCommand(players.iterator().next(),getBoard().getCurrentPlayerId());

                //getController().syncResources(); zit in docommand
                getController().notifyObservers("btn");
            }
            else if(players.size()>1){
                getController().setState(new ChoosePlayerState(this,i,j));
                getBoard().getCurrentPlayer().setOtherPlayerState(true);
                
               
            }
            }
            //(getController()).(new PlaceStructureInGameFaseState(this));
            
            return command;
            
        
        
    }

    @Override
    public String toString() {
        return "PlaceThief";
    }

    
    
}