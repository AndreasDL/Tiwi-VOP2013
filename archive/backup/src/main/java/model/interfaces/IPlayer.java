/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.interfaces;

import utility.TileType;

/**
 * Deze interface is de interface die gebruikt wordt om een speler voor te stellen.
 * @author Andreas De Lille
 */
public interface IPlayer {
    
    public String getName();
    
    public void setName(String name);
    
    public int getScore();
    
    public int getSettlementsLeft();
    
    public int getRoadsLeft();
    
    public int getCitiesLeft();
    
    public int getResource(TileType resource);   
    
    public void addResource(TileType resource);
    
    public void addResource(TileType resource,int aantal);
    
    public void removeResource(TileType resource);
            
    public void removeResource(TileType resource, int aantal);
    
    public void resetResources();
    
    public void roadBuilt();
    
    public boolean buildRoadPossible();
    
    public void settlementBuilt();
    
    public boolean buildSettlementPossible();
    
    public void CityBuilt();
    
    public boolean buildCityPossible();
    
    public boolean buyDevCard();
    
     public void startRoadBuilt();
    
    public void startSettlementBuilt();

    
}
