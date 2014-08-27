/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import model.Board;
import model.interfaces.IPlayer;

/**
 * Deze controller wordt gebruikt tijdens het plaatsen van de eerste 2 gebouwen en straten.
 * Er wordt rekening gehouden met de volgorde van plaatsen van constructies.
 * @author Jens
 */
public class PlaceStructuresController extends StandardController{   
    int numberCities=2;
    boolean placeCityNext=true;
    

    /**
     * Standaard constructor.
     * @param modelBoard gebruikte model
     */
    public PlaceStructuresController(Board modelBoard) {        
        setBoard(modelBoard);
    }

    /**
     * Geeft door dat er geklikt werd op een positie van een (mogelijke) weg op tegel (i,j).
     * Model controleert dan als er tegel mag gelegd worden.
     * Houdt rekening met de startregels.
     * @param x  x-coördinaat van de tegel
     * @param y  y-coördinaat van de tegel
     * @param index hoek waarop geklikt werd (startend van linkerbovenkant tussen 0 en 5)
     */
    @Override
    public void clickRoad(int x, int y, int index) {        
        if(!placeCityNext){
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
    public void placeRoads(ArrayList<int[]> tellers) {        
        if(!placeCityNext){
            if(getBoard().setStartRoads(tellers)){
                placeCityNext=true;
                if(numberCities==0){//alle dorpen en steden zijn dan al geplaatst
                    getBoard().goGameFase();
                    
                }
            }
        }
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
        if(placeCityNext&&numberCities>0) {
            getBoard().checkPossibleStartSettlement(x ,y, index);
        }
    }

    /**
     * Geeft de plaatsen waar er mogelijk een nederzetting kan geplaatst worden door aan het model.
     * Gebruikt startmethodes uit model.
     * Er wordt rekening gehouden met de volgorde van constructies plaatsen.
     * @param tellers lijst van {x-coördinaat tegel, y-coördinaat tegel, nederzettingindex op tegel}
     */
    @Override
    public void placeSettlements(ArrayList<int[]> tellers) {
        if(placeCityNext&&numberCities>0){
            if(getBoard().setStartSettlements(tellers)){
                placeCityNext=false;
                numberCities--;
                
            }
        }
    }

    /**
     * Geeft het huidige model terug.
     * @return huidige model
     */
    public Board getModelBoard() {
        return getBoard();
    }
    
    /**
     * geeft door aan modelboard die dan player aan zichzelf toevoegd
     * @param player 
     */
    @Override
    public void addPlayer(IPlayer player){
        getBoard().addPlayer(player);
    }
    
    
}
