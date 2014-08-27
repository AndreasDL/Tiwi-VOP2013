/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package states;

import commandos.ICommand;
import commandos.build.MakeFreeRoadCommand;
import java.util.ArrayList;
import model.interfaces.IState;

/**
 *
 * @author Samuel
 */
public class Build2RoadsState extends StandardState{
    
    
    public Build2RoadsState(IState state) {
        setBoard(state.getBoard());
        setController(state.getController());
        getController().setMessages("build2roads", "");
        getBoard().getCurrentPlayer().setRoads(true);
        getController().notifyObservers("disablebtn");
        getBoard().setNumberFreeRoadsPlaced(0);
        
    }
    @Override
    public ICommand placeRoads(ArrayList<int[]> tellers) {
        ICommand command=null;
        //if(getBoard().isBuildRoad()){
            //getBoard().setRoadsWithoutTesting(tellers,getBoard().getCurrentPlayerName());
            command=new MakeFreeRoadCommand(tellers,getBoard().getCurrentPlayerId());
            getController().doCommand(command);
            getController().doUpdates();
            //getBoard().addCommand(command);
            //getBoard().getCurrentPlayer().startRoadBuilt();
        //}
        getBoard().deselectHighlight();
        if(command!=null){
            if(getBoard().getNumberFreeRoadsPlaced()>=2){
                getController().setState(new PlaceStructureInGameFaseState(this));
                getBoard().getCurrentPlayer().setRoads(false);
                getController().notifyObservers("btn");
                getBoard().setNumberFreeRoadsPlaced(0);
            }
            
        }
        
        
        return command;
    }
     

    @Override
    public String toString() {
        return "PlaceStructureInGameFaseState";
    }
}
