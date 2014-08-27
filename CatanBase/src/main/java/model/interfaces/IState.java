/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.interfaces;

import commandos.ICommand;
import java.util.ArrayList;
import model.Board;

/**
 *
 * @author Samuel
 */
public interface IState {

    public void clickRoad(int x, int y, int index);

    /**
     * Geeft door dat er geklikt werd op een positie van een (mogelijke) weg op tegel (i,j).
     * Model controleert dan als er tegel mag gelegd worden.
     * @param x  x-coördinaat van de tegel
     * @param y  y-coördinaat van de tegel
     * @param index hoek waarop geklikt werd (startend van linkerbovenkant tussen 0 en 5)
     */
    public void clickSettlement(int x, int y, int index);

    public Board getBoard();

    public IController getController();

    public ICommand giveResources();

    /**
     * Geeft de plaatsen waar er mogelijk een weg moet geplaatst worden door aan het model.
     * Gebruikt startmethodes uit model.
     * Er wordt rekening gehouden met de volgorde van constructies plaatsen.
     * @param tellers lijst van {x-coördinaat tegel, y-coördinaat tegel, wegindex op tegel}
     */
    public ICommand placeRoads(ArrayList<int[]> tellers);

    public ICommand placeSettlements(ArrayList<int[]> tellers);

    public ICommand placeTile(int i, int j);

    public void setBoard(Board board);

    public void setController(IController controller);
    
    public void goNextState();
    
    public boolean waitingGameFase();
    
}
