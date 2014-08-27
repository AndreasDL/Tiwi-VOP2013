/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package states;


import commandos.ICommand;
import java.util.ArrayList;
import model.Board;
import model.interfaces.IController;
import model.interfaces.IState;

/**
 *
 * @author Samuel
 */
public class StandardState implements IState{
    private Board board;
    private IController controller;
    public void clickRoad(int x, int y, int index) {
        
    }

    public void clickSettlement(int x, int y, int index) {
        
    }

    public Board getBoard() {
       return board;
    }

    public IController getController() {
        return controller;
    }

    public ICommand giveResources() {
        return null;
    }

    public ICommand placeRoads(ArrayList<int[]> tellers) {
        return null;
    }

    public ICommand placeSettlements(ArrayList<int[]> tellers) {
        return null;
    }

    public ICommand placeTile(int i, int j) {
        return null;
        
    }

    public void setBoard(Board board) {
        this.board=board;
    }

    public void setController(IController controller) {
        this.controller=controller;
    }

    public void goNextState() {
        
    }

    public boolean waitingGameFase() {
        return false;
    }
    
}
