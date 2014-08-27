/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package states;

import commandos.ICommand;
import commandos.build.MakeCityCommand;
import commandos.build.MakeRoadCommand;
import commandos.build.MakeVillageCommand;
import java.util.ArrayList;
import model.interfaces.IState;

/**
 *
 * @author Samuel
 */
public class PlaceStructureInGameFaseState extends StandardState{
    
    public PlaceStructureInGameFaseState(IState state) {
        setBoard(state.getBoard());
        setController(state.getController());
        getController().setMessages("building", "");
        
    }

    @Override
    public String toString() {
        return "PlaceStructureInGameFaseState";
    }
    
    
    
    @Override
    public void clickRoad(int x, int y, int index) {        
            getBoard().checkPossibleRoad(x, y, index);
        
    }
    
    /**
     * Geeft de plaatsen waar er mogelijk een weg moet geplaatst worden door aan het model.
     * Gebruikt startmethodes uit model.
     * Er wordt rekening gehouden met de volgorde van constructies plaatsen.
     * @param tellers lijst van {x-coördinaat tegel, y-coördinaat tegel, wegindex op tegel}
     */
    
    @Override
    public ICommand placeRoads(ArrayList<int[]> tellers) {
        ICommand command=null;
        if(getBoard().isBuildRoad()){
            //if(getBoard().setRoads(tellers)){
                command=new MakeRoadCommand(tellers,getBoard().getCurrentPlayerId());
                //getBoard().addCommand(command);
//            }
//            else{
//            //getBoard().setMessages("", "noStreetPossible");
//            }
        }
        getBoard().deselectHighlight();
        return command;
    }
    
    /**
     * Geeft door dat er geklikt werd op een positie van een (mogelijke) weg op tegel (i,j).
     * Model controleert dan als er tegel mag gelegd worden.
     * @param x  x-coördinaat van de tegel
     * @param y  y-coördinaat van de tegel
     * @param index hoek waarop geklikt werd (startend van linkerbovenkant tussen 0 en 5)
     */
    
    @Override
    public void clickSettlement(int x , int y, int index){       
        getBoard().checkPossibleSettlement(x ,y, index);
    }
    
    
    @Override
    public ICommand placeSettlements(ArrayList<int[]> tellers) {
        ICommand command=null;
        if(getBoard().isBuildVillage()){
                command=new MakeVillageCommand(tellers,getBoard().getCurrentPlayerId());
        }
        else if(getBoard().isBuildCity()){
                command=new MakeCityCommand(tellers,getBoard().getCurrentPlayerId());
                System.out.println("citycommand");
        }
        
        getBoard().deselectHighlight();
        return command;
        
    }
    
    @Override
    public void goNextState() {
        getController().setState(new OtherPlayerState(getController(), getBoard()));
    }
}
