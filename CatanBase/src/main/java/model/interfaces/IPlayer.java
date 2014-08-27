/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.interfaces;


import java.awt.Color;
import java.util.ArrayList;
import model.Bank;
import observer.IObservable;
import utility.DevCardType;
import utility.TileType;

/**
 * Deze interface is de interface die gebruikt wordt om een speler voor te stellen.
 * @author Andreas De Lille
 */
public interface IPlayer extends IObservable{
    
    
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
    
    public void resetChangedResources();
    
    public void roadBuilt();
    
    public boolean buildRoadPossible();
    
    public void settlementBuilt();
    
    public boolean buildSettlementPossible();
    
    public void CityBuilt();
    
    public boolean buildCityPossible();
    
    public boolean buyDevCard();
    
     public void startRoadBuilt();
    
    public void startSettlementBuilt();

    public void notifyYourObservers();
    
    public int[] getStructures();

    public void setBank(Bank bank);

    public ArrayList<TileType> getChangedResources();
    
    public void setColor(Color color);
    
    public Color getColor();
    
    public int getTotalResources();
    
    public void goGameFase();

    public void setOtherPlayerState(boolean b);

    public boolean isOtherPlayerState();
    
    public int[] getResourceValues();
    
    public void setResourceValues(int[] resourceValues);

    public boolean addCard(DevCardType card);

    public boolean devCardPossible();

    public boolean addVictoryPoint();

    public void setRoads(boolean b);
    
    public boolean getRoads();

    public ArrayList<DevCardType> getCards();

    public boolean removeCard(int n);

    public int removeRandomResource();

    public void addKnight();

    public void removeKnightTrophy();

    public int getNumberKnights();

    public void giveKnightTrophy();

    
    public boolean tradePossible(int[] request);

    public int removeResources(int i);
    
    public boolean hasKnightTrophy();

    public int getId();
    public void setId(int id);
    
    public boolean isGamePhase();

    public void setDevCards(int i);

    public void setScore(int i);
}
