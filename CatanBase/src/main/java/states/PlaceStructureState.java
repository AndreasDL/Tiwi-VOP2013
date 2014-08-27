/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package states;

//import GUI.SurroundPanel;
import commandos.ICommand;
import commandos.build.MakeStartRoadCommand;
import commandos.build.MakeStartVillageCommand;
import java.util.ArrayList;
import model.Board;
import model.interfaces.IController;
import model.interfaces.IState;
import observer.IObservable;
import observer.IObserver;


/**
 *
 * @author Samuel
 */
public class PlaceStructureState implements IState,IObservable {
    int numberVillages=2;
    boolean placeVillageNext=true;
    private Board board;
    private IController controller;
    private ArrayList<IObserver> observers = new ArrayList<IObserver>();
    //SurroundPanel panel;

    public PlaceStructureState(Board board, IController controller) {

        this.board = board;
        this.controller = controller;

        
    }    
   
    public void modifyState(int numberOfCities,boolean placeVillageNext){
        this.numberVillages=numberOfCities;
        this.placeVillageNext=placeVillageNext;        
    }

    @Override
    public void clickRoad(int x, int y, int index) {      
        
        if(!placeVillageNext){
            board.setPlaceRoadPossible(true);
            getBoard().checkPossibleStartRoad(x, y, index);
        }
    }
    
    /**
     * Geeft de plaatsen waar er mogelijk een weg moet geplaatst worden door aan het model.
     * Gebruikt startmethodes uit model.
     * Er wordt rekening gehouden met de volgorde van constructies plaatsen.
     * @param tellers lijst van {x-coördinaat tegel, y-coördinaat tegel, wegindex op tegel}
     */
    @Override
    public ICommand placeRoads(ArrayList<int[]> tellers) { 

        if(getBoard().isBuildRoad()){
            if(!placeVillageNext){
                    ICommand command=new MakeStartRoadCommand(tellers,getBoard().getCurrentPlayerId());
                    
                    controller.doCommand(command);
                    controller.doUpdates();
                        placeVillageNext=true;
                        controller.notifyObservers("statPanel");
                        controller.goNextPlayer();
                        getBoard().deselectHighlight();
                    

            }
        }
        getBoard().deselectHighlight();
        return null;
    }
   

    /**
     * Geeft door dat er geklikt werd op een positie van een (mogelijke) nederzetting op tegel (i,j).
     * Model controleert dan als er tegel mag gelegd worden.
     * Er wordt rekening gehouden met de volgorde van constructies plaatsen.
     * @param x x-coördinaat van de tegel
     * @param y y-coördinaat van de tegel
     * @param index hoek waarop geklikt werd (startend van linkerpunt tussen 0 en 5)
     */
    @Override
    public void clickSettlement(int x, int y, int index) {            
        if(placeVillageNext&&numberVillages>0) {
            board.checkPossibleStartSettlement(x ,y, index);
        }
    }

    /**
     * Geeft de plaatsen waar er mogelijk een nederzetting kan geplaatst worden door aan het model.
     * Gebruikt startmethodes uit model.
     * Er wordt rekening gehouden met de volgorde van constructies plaatsen.
     * @param tellers lijst van {x-coördinaat tegel, y-coördinaat tegel, nederzettingindex op tegel}
     */
    @Override
    public ICommand placeSettlements(ArrayList<int[]> tellers) {
        if(getBoard().isBuildVillage()){
            if(placeVillageNext&&numberVillages>0){

                    ICommand command=new MakeStartVillageCommand(tellers,board.getCurrentPlayerId(),(numberVillages==2));
                    placeVillageNext=false;
                    numberVillages--;
                    getBoard().deselectHighlight();
                    return command;
//                }
//                else{
//                //getBoard().setMessages("", "noVillagePossible");
//                }
            }
        }
        
        getBoard().deselectHighlight();
        return null;
    }

    public Board getBoard() {
        return board;
    }

    public ICommand giveResources() {
        return null;
        
    }


    public void setBoard(Board board) {
        this.board=board;
    }

    @Override
    public void setController(IController controller) {
        this.controller=controller;
    }
    
    @Override
    public IController getController() {
        return controller;
    }
    
    public void goNextState() {
        if(numberVillages == 1){
            controller.setState(new OtherFirstBuildingState(this));
        }
        else{
            controller.setState(new WaitGameState(this));   
        }
        
        
            
    }

    public ICommand placeTile(int i, int j) {
        return null;
    }


    public void addObserver(IObserver obs) {
        observers.add(obs);
    }

    public void notifyObservers(String arg) {
        for(IObserver obs : observers){
            obs.update(this, arg);
        }
    }

    public void notifyObservers() {
        for(IObserver obs : observers){
            obs.update(this, null);
        }
    }
    public boolean waitingGameFase() {
        return false;
    }
    public void notifyYourObservers(){
        notifyObservers();
    }

    @Override
    public String toString() {
        return "StartPlaceStructure";
    }

    
    
    
}
