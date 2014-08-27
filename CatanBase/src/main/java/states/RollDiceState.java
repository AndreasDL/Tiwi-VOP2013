/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package states;

import commandos.ICommand;
import commandos.RollDiceCommand;
import model.Dices;
import model.interfaces.IState;

/**
 *
 * @author Samuel
 */
public class RollDiceState extends StandardState {
    public RollDiceState(IState state) {
        setBoard(state.getBoard());
        setController(state.getController());
        getController().setMessages("rollDice", "");
    }
    @Override
    public ICommand giveResources() {
        ICommand command=new RollDiceCommand();
        getController().doCommand(command);
        getController().doUpdates();
        getController().notifyObservers("repaint");
        getController().syncResources();
        
        Dices dices= getBoard().getDices();
        if(dices.getDice1()+dices.getDice2()==7){//dice1+dice2==7){
            getController().setState(new PlaceThiefState(this));
            //getBoard().placeThiefRed();
            getBoard().clearLastRolledNumber();
            getController().setMessages("placeThief", "");
        }
        else {
            getBoard().giveResources(dices.getDice1()+dices.getDice2());
            getController().notifyObservers("btn");
            getController().setState(new PlaceStructureInGameFaseState(this));
            getBoard().getCurrentPlayer().setOtherPlayerState(false);
        }     
        return command;
    }
    @Override
    public void goNextState() {
        getController().setState(new OtherPlayerState(getController(), getBoard()));
    }
    @Override
    public String toString() {
        return "RollDiceState";
    }
}
