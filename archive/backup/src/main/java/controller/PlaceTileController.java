/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import GUI.CatanBord;
import model.Board;
import model.SelectTileModel;
import model.interfaces.ITile;
import utility.TileType;

/**
 * Deze controller wordt gebruikt tijdens het leggen van de tegels.
 * @author Samuel
 */
public class PlaceTileController extends StandardController{

    /**
     * Deze controller wordt gebruikt tijdens het plaatsen van de tegels.
     */
    SelectTileModel tileModel;
    

    /**
     * Standaard constructor. 
     * @param board => modelbord
     * @param model => selectTile model
     */
    public PlaceTileController(Board board, SelectTileModel model) {
        setBoard(board);

        this.tileModel=model;
    }
    
    /**
     * niet gebruikt bij het plaatsen van de tegels.
     * @param x x-coördinaat volgens het nummeringssysteem (zie documentatie /scans/nummering).
     * @param y y-coördinaat volgens het nummeringssysteem
     */
    @Override
    public void removeTile(int x, int y) {
        getBoard().returnTile(x, y); 
    }
    
    /**
     * plaatst een tegel op het bord.
     * @param x x-coördinaat volgens het nummeringssysteem (zie documentatie /scans/nummering).
     * @param y y-coördinaat volgens het nummeringssysteem
     * @param tileType het type van de gelegde tegel.
     */
    @Override
    public void placeTile(int i, int j, TileType tileType) {
        /**
         * Plaatst een tegel op het bord.
         */
        getBoard().placeTile(i, j, tileType);        
    }

    public SelectTileModel getselectTileModel() {
        return tileModel;

    }   
}

