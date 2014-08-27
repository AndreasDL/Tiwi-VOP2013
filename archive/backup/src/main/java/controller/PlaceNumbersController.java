/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import GUI.MessagePanel;
import GUI.SurroundPanel;
import model.Board;
import model.SelectTileModel;
import model.interfaces.ITile;

/**
 * De controller die gebruikt wordt tijdens het plaatsen van de nummers.
 * @author Samuel
 */
public class PlaceNumbersController extends StandardController{
    /**
         * Standaard constructor.
         * board => het gebruikte board
         * model => het gebruikte modelBoard.
         */
    public PlaceNumbersController(Board board, SelectTileModel model) {
        setBoard(board);
        getBoard().setThiefInDesert();
    }
    /**
     * Constructor.
     * @param controller gebruikte controller.
     */
    public PlaceNumbersController(PlaceTileController controller) {
        this(controller.getBoard(),controller.getselectTileModel());
    }
    /**
         * Plaats een tegel op het veld.
         * Ongebruikt tijdens het spel
         */
    @Override
    public void removeTile(int i, int j) {
        
        getBoard().placeNumbers(i, j);
    }


    
    /**
     * geeft surroundPanel door aan modelboard
     * @param aThis 
     */
    public void setSurroundPanel(SurroundPanel aThis) {
        getBoard().setSurroundPanel(aThis);
    }
}