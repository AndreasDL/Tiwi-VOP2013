/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Observable;
import utility.TileType;

/**
 * Houdt het aantal grondstofkaarten die nog beschikbaar zijn bij.
 * Stuurt updates naar view als er iets gewijzigd is.
 * @author Jens
 */
public class Bank extends Observable{
    private int[] resourceValues = new int[TileType.NUMBER_OF_RESOURCES];
    private final int INIT_VALUE=19;

    /**
     * Standaard constructor.
     * Initialiseert het maximaal aantal grondstofkaarten.
     */
    public Bank() {
        for(int i=0; i < TileType.NUMBER_OF_RESOURCES ; i++){
            resourceValues[i]=INIT_VALUE;
        }
    }    
    
    /**
     * Geeft het aantal grondstoffen van de gevraagde soort terug die de Bank heeft.
     * Gooit een RuntimeException als de grondstofsoort niet voorkomt op informatiepaneel.
     * @param resource voorgedefinieerde grondstof in {@link TileType}
     * @return aantal grondstoffen in Bank van die soort
     */
    public int getResource(TileType resource) {
        if(resource.getValue() >= TileType.NUMBER_OF_RESOURCES){
            throw new RuntimeException("TileType is not a resource for a Bank");
        }
        return resourceValues[resource.getValue()];
    }
    
    /**
     * Voegt 1 grondstofkaart van het type <code>resource</code> toe aan de bank.
     * Verwittigt view van aanpassing.
     * @param resource voorgedefinieerde grondstof uit {@link TileType}
     */
    public void addResource(TileType resource){
        resourceValues[resource.getValue()]++;
        setChanged();
        notifyObservers();
    }    

    /**
     * Voegt <code>aantal</code> grondstofkaart(en) van het type <code>resource</code> toe aan de bank.
     * Verwittigt view van aanpassing.
     * @param resource voorgedefinieerde grondstof uit {@link TileType}
     * @param aantal aantal grondstofkaarten die erbij moeten
     */
    public void addResource(TileType resource,int aantal){        
        resourceValues[resource.getValue()]+=aantal;
        setChanged();
        notifyObservers();
    }

    /**
     * Verwijdert 1 grondstofkaart van het type <code>resource</code> uit de bank.
     * Verwittigt view van aanpassing.
     * @param resource voorgedefinieerde grondstof uit {@link TileType}
     */
    public void removeResource(TileType resource){        
            resourceValues[resource.getValue()]--;
            setChanged();
            notifyObservers();        
    }
    
    /**
     * Verwijdert <code>aantal</code> grondstofkaart(en) van het type <code>resource</code> uit de bank.
     * Verwittigt view van aanpassing.
     * @param resource voorgedefinieerde grondstof uit {@link TileType}
     * @param aantal aantal grondstofkaarten die weg moeten
     */
    public void removeResource(TileType resource, int aantal){       
        resourceValues[resource.getValue()]-=aantal;
        setChanged();
        notifyObservers();
    }
}
