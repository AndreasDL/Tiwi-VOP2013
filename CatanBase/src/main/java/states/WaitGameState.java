/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package states;

import java.util.ArrayList;
import observer.IObservable;
import observer.IObserver;

/**
 *
 * @author Samuel
 */
public class WaitGameState extends StandardState implements IObservable{
    private ArrayList<IObserver> observers=new ArrayList<IObserver>();
    private PlaceStructureState state;
    
    public WaitGameState(PlaceStructureState state) {
        setController(state.getController());
        setBoard(state.getBoard());
        this.state=state;
        getController().setMessages("otherPlayer", "");
    }

    @Override
    public String toString() {
        return "WaitGameState";
    }
    
    
    @Override
    public void goNextState() {       
        getController().updateBank();
        getController().goGameFase(getBoard().getCurrentPlayerId());
        getController().setState(new OtherPlayerState(getController(), getBoard()));
        state.notifyYourObservers();
        getBoard().initiateGameFase(getBoard().getCurrentPlayerId());
        getController().startTimer();        
    }
    @Override
    public boolean waitingGameFase() {
        return true;
    }

    public void addObserver(IObserver obs) {
        observers.add(obs);
    }

    public void notifyObservers(String arg) {
        for(IObserver observer:observers){
            observer.update(this, arg);
        }
    }

    public void notifyObservers() {
        for(IObserver observer:observers){
            observer.update(this, null);
        }
    }
}
