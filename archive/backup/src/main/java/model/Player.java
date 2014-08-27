/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.interfaces.IPlayer;
import model.cards.IDevCard;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import utility.TileType;

/**
 *Houdt de gegevens bij van een Speler.
 * @author Jens Vandecasteele
 */
public class Player extends Observable implements IPlayer{
    
    private String name;  //naam van de Speler
    private int score;    //aantal zegepunten
    private int[] resourceValues = new int[TileType.NUMBER_OF_RESOURCES]; // RESOURCE(INDEX): GRAAN(0),WOL(1),STEEN(2),ERTS(3),HOUT(4)
    private int numberOfVillagesLeft=5; //resterende dorpen
    private int numberOfCitiesLeft=4;      //resterende steden
    private int numberOfRoadsLeft=15;      //resterende wegen
    private ArrayList<IDevCard> devCards = new ArrayList<IDevCard>(); //lijst met ontwikkelingskaarten
    private Bank bank;
    
    public void setBank(Bank bank) {
        this.bank = bank;
    }
    
    
    public Player(Bank bank){
        this.bank=bank;
        this.name = "Naamloos";
        this.score=0;
    }
    public Player(){
        
        this.name = "Naamloos";
        this.score=0;
    }
    
    /**
     * Maakt een Player aan met opgegeven <code>name</code>.
     * Initialiseert score op 0.
     * @param name naam van de Speler
     */
    public Player(String name,Bank bank){
        this.bank=bank;
        this.name = name;
        this.score = 0;
    }
    
    
    /**
     * Geeft de naam van de Speler terug.
     * @return Spelernaam
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Stelt de naam van de Speler in.
     * @param name nieuwe naam voor de Speler.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Geeft het aantal Zegepunten die de Speler momenteel heeft.
     * @return het aantal Zegepunten
     */
    @Override
    public int getScore() {
        return score;
    }
    
    /**
     * Voegt een grondstofkaart toe van het type {@link TileType} aan de Speler.
     * Verwittigt view en Bank dat waarden aangepast zijn.
     * @param resource voorgedefinieerde grondstof in {@link TileType}
     * @see TileType
     */
    public void addResource(TileType resource){
        if(resource.getValue() >= TileType.NUMBER_OF_RESOURCES){
            throw new RuntimeException("TileType is not a resource for a Player: ");
        }
        if(bank.getResource(resource)-1>=0){
            resourceValues[resource.getValue()]++;
            bank.removeResource(resource);
            setChanged();
            notifyObservers();
        }
        
    }
    
    /**
     * Voegt een <code>aantal</code> grondstofkaarten van dezelfde soort toe.
     * @param resource voorgedefinieerde grondstof in {@link TileType}
     * @param aantal aantal kaarten van die soort die je wil toevoegen
     * @see TileType
     */
    public void addResource(TileType resource,int aantal){
        if(resource.getValue() >= TileType.NUMBER_OF_RESOURCES){
            throw new RuntimeException("TileType is not a resource for a Player");
        }
        if(bank.getResource(resource)-aantal>=0){
            resourceValues[resource.getValue()]+=aantal;
            bank.removeResource(resource,aantal);
            setChanged();
            notifyObservers();
        }
    }
    
    /**
     * Verwijdert een grondstofkaart van het type {@link TileType} van de Speler.
     * Als er geen grondstofkaarten van die soort meer zijn, gebeurt er niets.
     * Verwittigt view en Bank dat waarden aangepast zijn.
     * @param resource voorgedefinieerde grondstof in {@link TileType}
     * @see TileType
     */
    public void removeResource(TileType resource){
        if(resource.getValue() >= TileType.NUMBER_OF_RESOURCES){
            throw new RuntimeException("TileType is not a resource for a Player");
        }
        if( resourceValues[resource.getValue()] - 1 >= 0 ){
            resourceValues[resource.getValue()]--;
            bank.addResource(resource);
            setChanged();
            notifyObservers();
        }
    }
    
     /**
     * Verwijdert een <code>aantal</code> grondstofkaarten van dezelfde soort.
     * Als er niet genoeg grondstofkaarten meer zijn, gebeurt er niets.
     * Gooit een RuntimeException als de grondstof niet voorkomt op informatiepaneel en
     * als het aantal grondstoffen van een bepaalde soort kleiner is dan 0.
     * Verwittigt view dat waarden aangepast zijn.
     * @param resource voorgedefinieerde grondstof in {@link TileType}
     * @param aantal aantal kaarten van die soort die je wil verwijderen
     * @see TileType
     */
    public void removeResource(TileType resource, int aantal){
        if(resource.getValue() >= TileType.NUMBER_OF_RESOURCES){
            throw new RuntimeException("TileType is not a resource for a Player");
        }
        if(resourceValues[resource.getValue()] - aantal < 0){
            throw new RuntimeException("Aantal grondstoffen in Player kleiner dan 0");
        }
        resourceValues[resource.getValue()]-=aantal;
        bank.addResource(resource, aantal);
        
        setChanged();
        notifyObservers();
    }
    
    /**
     * Verwijdert alle grondstofkaarten van de Speler.
     */
    public void resetResources(){
        for(int i=0; i < resourceValues.length ; i++){
            resourceValues[i]=0;
        }
        setChanged();
        notifyObservers();
    }
    
    /**
     * Verwijdert de grondstoffen die nodig zijn om een weg te bouwen. 
     * De benodigde grondstoffen zijn 1 hout en 1 steen. 
     * Er wordt niet gecontroleerd als er nog genoeg grondstoffen zijn. Dit gebeurt in <code>Board</code>.
     * Het aantal resterende wegen wordt verminderd met 1.
     * Verwittigt de Observers.
     * @see Board
     */
    public void roadBuilt() {        
            removeResource(TileType.WOOD);
            removeResource(TileType.STONE);
            numberOfRoadsLeft--;            
            setChanged();
            notifyObservers(getStructures());        
    }
    
    /**
     * Controleert als er genoeg grondstoffen en constructies zijn om een weg te bouwen.
     * De benodigde grondstoffen zijn 1 hout en 1 steen. 
     * @return true als genoeg grondstoffen en constructies
     */
    public boolean buildRoadPossible(){
        return (resourceValues[TileType.WOOD.getValue()]>=1   && 
                resourceValues[TileType.STONE.getValue()]>=1  &&
                numberOfRoadsLeft>0);
    }
    
    /**
     * Verwijdert de grondstoffen die nodig zijn voor een dorp te bouwen.
     * De benodigde grondstoffen zijn 1 hout, 1 steen, 1 graan en 1 wol.
     * Er wordt niet gecontroleerd als de benodigdheden aanwezig zijn. Dit gebeurt in <code>Board</code>.
     * Het aantal overgebleven dorpen wordt verminderd met 1.
     * Verwittigt de Observers.
     * @see Board
     */
    public void settlementBuilt() {       
            removeResource(TileType.WOOD);
            removeResource(TileType.STONE);
            removeResource(TileType.WHEAT);
            removeResource(TileType.WOOL);
            numberOfVillagesLeft--;            
            setChanged();
            notifyObservers(getStructures());      
    }
    
     /**
     * Controleert als er genoeg grondstoffen en constructies aanwezig zijn om een dorp te bouwen.
     * De benodigde grondstoffen zijn 1 hout, 1 steen, 1 graan en 1 wol.
     * @return true als alles aanwezig is
     */
    public boolean buildSettlementPossible(){
        return (    resourceValues[TileType.WOOD.getValue()]>=1  &&
                    resourceValues[TileType.STONE.getValue()]>=1 &&
                    resourceValues[TileType.WHEAT.getValue()]>=1 &&
                    resourceValues[TileType.WOOL.getValue()]>=1   &&
                    numberOfVillagesLeft>0);
    }
    
    /**
     * Verwijdert de grondstoffen die nodig zijn voor een stad te bouwen als die grondstoffen aanwezig zijn.
     * De benodigde grondstoffen zijn 2 graan en 3 erts.
     * Er wordt niet gecontroleerd als de grondstoffen en constructies aanwezig zijn.
     * Het aantal resterende steden wordt verminderd met 1.
     */
    public void CityBuilt() {        
            removeResource(TileType.WHEAT,2);
            removeResource(TileType.IRON,3);
            numberOfCitiesLeft--;
            numberOfVillagesLeft++;
            setChanged();
            notifyObservers(getStructures());       
    }

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
        setChanged();
        notifyObservers(getStructures());
    }
    
    /**
     * Controleert als het bouwen van een stad mogelijk is.
     * De benodigde grondstoffen zijn 2 graan en 3 erts.
     * @return true als alles aanwezig is
     */
    public boolean buildCityPossible(){
        return (    resourceValues[TileType.WHEAT.getValue()]>=2 &&
                    resourceValues[TileType.IRON.getValue()]>=3  &&
                    numberOfCitiesLeft > 0);
    }

    
    /**
     * Nog niet nodig
     * @return 
     */
    public boolean buyDevCard() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * Geeft het aantal dorpen die de Speler nog kan bouwen.
     * @return resterende dorpen
     */
    public int getSettlementsLeft() {
        return numberOfVillagesLeft;
    }

    /**
     * Geeft het aantal wegen die de Speler nog kan bouwen.
     * @return resterende wegen
     */
    public int getRoadsLeft() {
        return numberOfRoadsLeft;
    }
    
    /**
     * Geeft het aantal steden die de Speler nog kan bouwen.
     * @return resterende steden
     */
    public int getCitiesLeft() {
        return numberOfCitiesLeft;
    }

    /**
     * Geeft het aantal grondstoffen van de gevraagde soort terug die de Speler heeft.
     * Gooit een RuntimeException als de grondstofsoort niet voorkomt op informatiepaneel.
     * @param resource voorgedefinieerde grondstof in {@link TileType}
     * @return aantal grondstoffen van {@link TileType}
     */
    public int getResource(TileType resource) {
        if(resource.getValue() >= TileType.NUMBER_OF_RESOURCES){
            throw new RuntimeException("TileType is not a resource for a Player");
        }
        return resourceValues[resource.getValue()];
        
            
    }
    
    /**
     * Geeft het aantal structures als Object terug.
     * Wordt gebruikt om het aantal structures door te geven aan de Observers.
     * @return Object van int[]{aantal wegen over, aantal dorpen over, aantal steden over}
     */
    public Object getStructures(){
        int[] structures = {numberOfRoadsLeft,numberOfVillagesLeft,numberOfCitiesLeft};
        return (Object) structures;
    }
    
    /**
     * Vermindert het aantal wegen met 1 [STARTFASE]
     */
    public void startRoadBuilt(){
        numberOfRoadsLeft--;
    }
    
    /**
     * Vermindert het aantal dorpen met 1 [STARTFASE]
     */
    public void startSettlementBuilt(){
        numberOfVillagesLeft--;
    }
    
}
