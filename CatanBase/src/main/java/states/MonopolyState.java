/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package states;

import model.interfaces.IState;

/**
 *
 * @author Samuel
 */
public class MonopolyState extends StandardState {
    int k;
    int j;
    int n=2;
    boolean monopoly;
    public MonopolyState(IState state,boolean monopoly) {
        setBoard(state.getBoard());
        setController(state.getController());
        getController().notifyObservers("disablebtn");
        if(monopoly)
            getController().setMessages("choosemonopolycard", "");
        else
            getController().setMessages("choosecards", "");
        this.monopoly=monopoly;
      
        
    }

    public void setCardsLeft(int n) {
        this.n = n;
    }

    public int getCardsLeft() {
        return n;
    }
    
    
    
    
    @Override
    public void goNextState() {
        if(monopoly||n==1) {
            getController().setState(new PlaceStructureInGameFaseState(this));
            getController().notifyObservers("btn");
        }
        
        n--;
    }

    @Override
    public String toString() {
        if(monopoly) {
            return "M";
        }
        else {
            return "R";
        }
    }
    
    
    
}
