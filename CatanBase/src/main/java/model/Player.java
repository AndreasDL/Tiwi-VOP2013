/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import model.interfaces.IPlayer;
import observer.IObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utility.DevCardType;
import utility.TileType;

/**
 *Houdt de gegevens bij van een Speler.
 * @author Jens Vandecasteele
 */
public class Player  implements IPlayer{
    private static final Logger log = LoggerFactory.getLogger(Player.class);
    
    private String name;  //naam van de Speler
    private int[] resourceValues = new int[TileType.NUMBER_OF_RESOURCES]; // RESOURCE(INDEX): GRAAN(0),WOL(1),STEEN(2),ERTS(3),HOUT(4)
    private int numberOfVillagesLeft=2; //resterende dorpen
    private int numberOfCitiesLeft=0;      //resterende steden
    private int numberOfRoadsLeft=2;      //resterende wegen
    private int numberOfCardsLeft=31;
    //private ArrayList<DevCardType> devCards = new ArrayList<DevCardType>(); //lijst met ontwikkelingskaarten
    private Bank bank;
    private ArrayList<TileType> changedResources=new ArrayList<TileType>();

   
    private ArrayList<IObserver> observers = new ArrayList<IObserver>();
    private Color color;
    private boolean otherPlayerState=false;
    private ArrayList<DevCardType> cards;
    private int victoryPoints=0;
    private boolean roads;
    private int knights=0;
    private boolean knightTrophy=false;
    private int id;
    
    
    public void setBank(Bank bank) {
        this.bank = bank;
    }
    /**
     * voegt een overwinningspunt toe
     * @return spel al dan niet gedaan
     */
    public boolean addVictoryPoint(){
        victoryPoints++;

        return victoryPoints>=10;
    }
    
    public Player(Bank bank){
        this(0,"naamloos");
        this.bank=bank;
        
    }
    public Player(int id,String s){
        this.id = id;
        this.name = s;
        cards=new ArrayList<DevCardType>();
    }
    
    /**
     * Maakt een Player aan met opgegeven <code>name</code>.
     * Initialiseert score op 0.
     * @param name naam van de Speler
     */
    public Player(int id,String name,Bank bank,Color color){
        this(id,name);
        this.bank=bank;
        this.color=color;
    }

    public Color getColor() {
        return color;
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
        return victoryPoints;
    
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
            changedResources.add(resource);
            log.info("Resource {} added to Player", resource);            
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
        if(aantal > 0 && bank.getResource(resource)-aantal>=0){
            resourceValues[resource.getValue()]+=aantal;
            bank.removeResource(resource,aantal);
            changedResources.add(resource);
            log.info("Resource {} {} times added to Player {}", resource,aantal,name);
            bank.notifyObservers();
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
            changedResources.add(resource);
            log.info("Resource {} removed from Player", resource);
//            setChanged();
//            notifyObservers();
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
        if(aantal > 0){
            resourceValues[resource.getValue()]-=aantal;
            bank.addResource(resource, aantal);
            changedResources.add(resource);
            log.info("Resource {} {} times removed from Player {}", resource,aantal,name);
            bank.notifyObservers();
            notifyObservers(); 
        }
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
            bank.notifyObservers();
            notifyObservers(); 
            
            log.info("Road built");
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
            bank.notifyObservers();
            notifyObservers();  
            System.out.println("Village built "+name);
            log.info("Village built");
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
            bank.notifyObservers();
            notifyObservers();  
            System.out.println("City built"+name);
            log.info("City built");
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
        //return true;
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
    public int[] getStructures(){
        int[] structures = {numberOfRoadsLeft,numberOfVillagesLeft,numberOfCitiesLeft,numberOfCardsLeft};
        return structures;
    }
    
    /**
     * Vermindert het aantal wegen met 1 [STARTFASE]
     */
    public void startRoadBuilt(){
        numberOfRoadsLeft--;
        notifyObservers();
        System.out.println("startroadbuilt "+name);
    }
    public void goGameFase(){
        if(numberOfRoadsLeft==0){
            numberOfRoadsLeft=13;
            numberOfVillagesLeft=3;
            numberOfCitiesLeft=4;
        }
        notifyObservers();
    }
    
    public boolean isGamePhase(){
        return !(numberOfRoadsLeft==0);
    }
    
    /**
     * Vermindert het aantal dorpen met 1 [STARTFASE]
     */
    public void startSettlementBuilt(){
        numberOfVillagesLeft--;
        notifyObservers();
        System.out.println("startsettlementbuilt "+name);
    }

    public void notifyYourObservers() {
        bank.notifyYourObservers();
        notifyObservers();
        
    }

    public ArrayList<TileType> getChangedResources() {
        return changedResources;
        
    }

    public void addObserver(IObserver obs) {
        observers.add(obs);
    }

    public void notifyObservers(String arg) {
        for(IObserver obs : observers){
            obs.update(this, arg);
        }
    }

    public void notifyObservers() {
        for(IObserver obs : observers){
            obs.update(this, null);
        }
    }

    public void resetChangedResources() {
        
        changedResources.clear();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getTotalResources() {
        int totaal =0;
        for( int i : resourceValues) {
            totaal+=i;
        }
        log.info("Total number of resources = {}", totaal);
        return totaal;
    }
    public void setOtherPlayerState(boolean b){
        
        this.otherPlayerState=b;
        notifyObservers();
    }
    public boolean isOtherPlayerState(){
        return otherPlayerState;
    }

    public int[] getResourceValues() {
        return resourceValues;
    }
    
    public void setResourceValues(int[] resourceValues) {
        resetChangedResources();
        for(int i=0;i<resourceValues.length;i++){
            if(resourceValues[i]!=this.resourceValues[i])
                changedResources.add(TileType.values()[i]);
        }
        this.resourceValues=resourceValues;
        notifyObservers();
    }

    public boolean addCard(DevCardType card){
        if(resourceValues[TileType.WOOL.getValue()]>0&&resourceValues[TileType.WHEAT.getValue()]>0&&resourceValues[TileType.IRON.getValue()]>0&&numberOfCardsLeft>0){
            removeResource(TileType.WOOL);
            removeResource(TileType.WHEAT);
            removeResource(TileType.IRON);
            cards.add(card);
            numberOfCardsLeft--;
            notifyObservers();
            for(int i=0;i<cards.size();i++) {

            }
            return true;
        }
        return false;

    }
    public boolean devCardPossible(){
        return  resourceValues[TileType.WHEAT.getValue()]>=1&&
                resourceValues[TileType.IRON.getValue()]>=1&&
                resourceValues[TileType.WOOL.getValue()]>=1;
        
    }

    
    public void setRoads(boolean b){
        this.roads=b;
        notifyObservers();
    }

    public boolean getRoads() {
        return roads;
    }
    

    public int removeRandomResource(){
        int totaal=getTotalResources();
        Random rand=new Random(System.currentTimeMillis());
        int n=rand.nextInt(totaal);
        totaal=0;
        for(int i=0;i<resourceValues.length;i++){
            totaal+=resourceValues[i];
            if(n<totaal){
                TileType resource =TileType.values()[i];
                removeResource(resource);
                //bank.addResource(resource);
                //changedResources.add(resource);
                return i;
            }
                
        }
       
        return 0;
        
    }
    public void addKnight(){
        knights++;
    }

    public void removeKnightTrophy(){
        knightTrophy=false;
        victoryPoints-=2;
    }

    public int getNumberKnights(){
        return knights;
    }

    public boolean hasKnightTrophy() {
        return knightTrophy;
    }

    public void giveKnightTrophy(){
        knightTrophy=true;
        //victoryPoints+=2; wordt nu in board gedaan

    }
    public boolean tradePossible(int[] request) {
        int i=0;
        while(i < TileType.NUMBER_OF_RESOURCES &&  request[i]<=resourceValues[i]){
            i++;
        }
        return (i==TileType.NUMBER_OF_RESOURCES);

    }

    public int removeResources(int i) {
        int j=resourceValues[i];
        removeResource(TileType.values()[i], j);
        return j;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean removeCard(int n) {
        for(int i=0;i<cards.size();i++){
            if(cards.get(i).getValue()==n){
                cards.remove(i);
                return true;
            }
        }
        return false;
    }
  
     public ArrayList<DevCardType> getCards() {
        return cards;
    }

    public void setDevCards(int i) {
        numberOfCardsLeft=i;
        notifyObservers();
    }

    public void setScore(int i) {
        victoryPoints=i;
    }
     
     
    
}
