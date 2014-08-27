/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import model.Board;
import model.interfaces.ITile;

/**
 * De controller die gebruikt wordt tijdens het spelen van het Spel.
 * @author Jens
 */
public class GameController extends StandardController{

    public GameController( Board modelBoard) {       
        setBoard(modelBoard);
    } 
/**
 * constructor die al een controller meekrijgt. Het model wordt opgehaald uit de controller.
 * @param controller de controller die het gebruikte model bevat.
 */
    public GameController(PlaceStructuresController controller) {
        this(controller.getBoard());
        getBoard().initiateGameFase();
    }   
    
    /**
     * Geef resources aan de Spelers die een nederzetting hebben aan een tegel met nummer <code>number</code>.
     * Het model controleert als er aan alle voorwaarden voldaan zijn.
     * @param number het gedobbelde getal
     */
    @Override
    public void giveResources(int number) {        
        getBoard().giveResources(number);
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
    public void placeRoads(ArrayList<int[]> tellers) {         
            getBoard().setRoads(tellers);
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
    public void placeSettlements(ArrayList<int[]> tellers) {
        getBoard().setSettlements(tellers);
        
    }
}
