/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.interfaces;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import model.Village;
import utility.TileType;

/**
 * Deze interface stelt een tegel voor zoals deze in het model wordt weergegeven.
 * @author Andreas De Lille
 */
public interface ITile {

    public int getNumber();

    public IRoad getRoad(int index);

    public ISettlement getSettlement(int index);

    public TileType getType();

    public boolean hasThief();

    public void setNumber(int number);

    public void setRoad(IRoad road, int index);

    public void setThief(boolean t);

    public void setType(TileType type);
    
    public ISettlement[] getSettlements();
    
    public ArrayList<Integer> getSettlementsIndices();
    
    public boolean roadPossible(int index,int player);
    
    public boolean settlementPossible(int index,int player); 
    
    public void setSettlement(ISettlement settlement, int index);
    
    public void setStartRoad(IRoad road, int index);

    public boolean startRoadPossible(int i, ISettlement lastSettlement,int player);
    
    public boolean roadToSettlement(int index,int player);

    public boolean cityPossible(int index,int naam);
    
    public boolean isRolled();

    public void setRolled(boolean rolled);

    public boolean placeThief();

    public void setRedThief(boolean b);

    public void setTempSettlement(int i);

    public boolean isTempVillage(int k);

    public void deselect();

    public void setTempRoad(int k);

    public boolean isTempRoad(int k);

    public void setTempCity(int k);

    public boolean isTempCity(int k);

    public HashSet<Integer> containsOtherPlayer(int currentPlayerName);

    public boolean freeRoadPossible(int i, int player);

    


}