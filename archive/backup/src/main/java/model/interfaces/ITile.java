/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.interfaces;

import java.util.ArrayList;
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
    
    public boolean roadPossible(int index);
    
    public boolean settlementPossible(int index); 
    
    public void setSettlement(ISettlement settlement, int index);
    
    public void setStartRoad(IRoad road, int index);

    public boolean startRoadPossible(int i, ISettlement lastSettlement);
    
    public boolean roadToSettlement(int index);

    public boolean CityPossible(int index);
    
}