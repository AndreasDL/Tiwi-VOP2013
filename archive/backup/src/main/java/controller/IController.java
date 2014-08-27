/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import model.interfaces.IPlayer;
import model.interfaces.ITile;
import utility.TileType;

/**
 * Deze interace legt de functies van een controller vast.
 * @author Samuel
 */
public interface IController {
    public void removeTile(int i,int j);//klik op tile in CatanBord
    public void placeTile(int i,int j, TileType type);
    
    public void giveResources(int number);
    public ITile getTile(int i, int j);
    public void clickRoad(int x , int y, int index);
    public void clickSettlement(int x , int y, int index);

    public void placeSettlement(int i, int j, int buildingIndex);

    public void placeSettlements(ArrayList<int[]> tellers);
    
   public void addPlayer(IPlayer player);
   public void placeRoads(ArrayList<int[]> tellers);

    


    

    
}
