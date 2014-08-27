/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import observer.IObservable;
import observer.IObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utility.TileType;

/**
 * Houdt het aantal grondstofkaarten die nog beschikbaar zijn bij.
 * Stuurt updates naar view als er iets gewijzigd is.
 * @author Jens
 */
public class Bank implements IObservable{
    private static final Logger log = LoggerFactory.getLogger(Bank.class);
    
    private int[] resourceValues = new int[TileType.NUMBER_OF_RESOURCES];
    private final int INIT_VALUE=19;
    private ArrayList<IObserver> observers = new ArrayList<IObserver>();
    private ArrayList<TileType> changedResources=new ArrayList<TileType>();

    /**
     * Standaard constructor.
     * Initialiseert het maximaal aantal grondstofkaarten.
     */
    public Bank(){
        log.debug("Entering Bank() constructor\nLeaving Bank() constructor");
    }
    
    public Bank(int value) {
        log.debug("Entering Bank constructor ( value= {} )", value);
        for(int i=0; i < TileType.NUMBER_OF_RESOURCES ; i++){
            resourceValues[i]=INIT_VALUE;
            log.trace("ResourceValue {} = {}", resourceValues[i], INIT_VALUE);
        }
        log.debug("Leaving Bank constructor");
    }    
    
    /**
     * Geeft het aantal grondstoffen van de gevraagde soort terug die de Bank heeft.
     * Gooit een RuntimeException als de grondstofsoort niet voorkomt op informatiepaneel.
     * @param resource voorgedefinieerde grondstof in {@link TileType}
     * @return aantal grondstoffen in Bank van die soort
     */
    public int getResource(TileType resource) {
        log.debug("getResource ( resource= {} )" , resource);
        if(resource.getValue() >= TileType.NUMBER_OF_RESOURCES){
            log.error("TileType is not a resource for a Bank");
            throw new RuntimeException("TileType is not a resource for a Bank");
        }
        log.debug("returning from getResource = {}", resourceValues[resource.getValue()]);
        return resourceValues[resource.getValue()];
    }
    
    /**
     * Voegt 1 grondstofkaart van het type <code>resource</code> toe aan de bank.
     * Verwittigt view van aanpassing.
     * @param resource voorgedefinieerde grondstof uit {@link TileType}
     */
    public void addResource(TileType resource){
        log.debug("Entering addResource ( resource= {} )", resource);
        resourceValues[resource.getValue()]++;
        changedResources.add(resource);
        //notifyObservers();
        log.info("Resource {} added to bank", resource);
        log.debug("Leaving addResource");
    }

    /**
     * Voegt <code>aantal</code> grondstofkaart(en) van het type <code>resource</code> toe aan de bank.
     * Verwittigt view van aanpassing.
     * @param resource voorgedefinieerde grondstof uit {@link TileType}
     * @param aantal aantal grondstofkaarten die erbij moeten
     */
    public void addResource(TileType resource,int aantal){
        log.debug("Entering addResource ( resource= {} aantal= {} )", resource , aantal);
        
        resourceValues[resource.getValue()]+=aantal;
        changedResources.add(resource);
        //notifyObservers();
        
        log.info("Resource {} {} times added to bank", resource, aantal);
        log.debug("Leaving addResource");
    }
    

    /**
     * Verwijdert 1 grondstofkaart van het type <code>resource</code> uit de bank.
     * Verwittigt view van aanpassing.
     * @param resource voorgedefinieerde grondstof uit {@link TileType}
     */
    public void removeResource(TileType resource){
        log.debug("Entering removeResource ( resource= {} )", resource);
        resourceValues[resource.getValue()]--;
        changedResources.add(resource);
        //notifyObservers();    
        
        log.info("Resource {} removed from bank", resource);
        log.debug("Leaving removeResource");
    }
    
    /**
     * Verwijdert <code>aantal</code> grondstofkaart(en) van het type <code>resource</code> uit de bank.
     * Verwittigt view van aanpassing.
     * @param resource voorgedefinieerde grondstof uit {@link TileType}
     * @param aantal aantal grondstofkaarten die weg moeten
     */
    public void removeResource(TileType resource, int aantal){ 
        log.debug("Entering removeResource ( resource= {} aantal= {} )", resource , aantal);
        
        resourceValues[resource.getValue()]-=aantal;
        changedResources.add(resource);
        //notifyObservers();
        
        log.info("Resource {} {} times removed from bank", resource, aantal);
        log.debug("Leaving removeResource");
    }

    public void setResourceValues(int[] resourceValues) {
        log.debug("Entering setResourceValues");
        this.resourceValues = resourceValues;
        notifyObservers();
        
        log.info("resource values changed");
        log.debug("leaving setResourceValues");
    }

    public int[] getResourceValues() {
        log.debug("returning ResourceValues");
        return resourceValues;
    }

    public void addObserver(IObserver obs) {
        log.debug("adding observer");
        observers.add(obs);
    }

    public void notifyObservers(String arg) {
        log.debug("Entering notify observers ( arg= {} )", arg);
        
        for(IObserver obs : observers){
            obs.update(this, arg);
            log.trace("observer {} updated with {}", obs.toString(), arg);
        }
        
        log.debug("Leaving notify observers");
    }

    public void notifyObservers() {
        log.debug("Entering notifyObservers");
        
        for(IObserver obs : observers){
            obs.update(this, null);
            log.trace("observer {} updated", obs.toString());
        }    
        
        log.debug("leaving notifyObservers");
    }

    void notifyYourObservers() {
        log.debug("Entering notifyYourObservers");
        
         notifyObservers();
        changedResources.clear();
        log.debug("Leaving notifyYourObservers");
    }
    
    
    
}
