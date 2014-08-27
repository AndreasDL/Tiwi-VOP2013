/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import model.Board;
import model.interfaces.IPlayer;
import model.interfaces.ITile;
import utility.TileType;

/**
 * Deze klasse implementeert de interface en zal gebruikt worden om de basis versie 
 * van enkele functies eenmalig vast te leggen.
 * @author Samuel
 */
public class StandardController implements IController{

    private Board board;

    public void removeTile(int i, int j) {
        
    }

    public void placeTile(int i, int j, TileType type) {
    }
    
    public void giveResources(int number) {
        
    }
    /**
     * Geeft de tegel terug die op positie <code>(i,j)</code> ligt.
     * @param i x-coördinaat van de tegel
     * @param j y-coördinaat van de tegel
     * @return  Tile op positie (i,j)
     */
    @Override
    public ITile getTile(int i, int j) {
        return board.getTile(i, j);
    }

    public void clickRoad(int x, int y, int index) {
        
    }

    public void clickSettlement(int x, int y, int index) {
        
    }

    public void placeSettlement(int i, int j, int buildingIndex) {
        
    }

    public void placeSettlements(ArrayList<int[]> tellers) {
        
    }

    public void addPlayer(IPlayer player) {
        
    }

    public void placeRoads(ArrayList<int[]> tellers) {
        
    }
    public void setBoard(Board board) {
        this.board=board;
    }

    public Board getBoard() {
        return board;
    }
    

    
    
}
