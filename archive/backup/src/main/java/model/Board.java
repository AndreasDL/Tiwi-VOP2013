/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import GUI.SurroundPanel;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import model.interfaces.IPlayer;
import model.interfaces.ISettlement;
import model.interfaces.ITile;
import utility.TileType;

/**
 * Model van het Speelbord.
 * @author Andreas De Lille
 */
public class Board {
    Random rand=new Random(System.currentTimeMillis());
    private ITile[][] tiles;
    private ITile thiefTile;
    private ArrayList<IPlayer> players = new ArrayList<IPlayer>();
    private int currentPlayer=0;
    private boolean initiateBuilding=true;
    final int XDIM = 7;
    final int YDIM =7;
    static int[] NUMBERTILES={4,5,6,7,6,5,4};
    
    private static Point[] outerRecources={new Point(1,1),new Point(2,1),new Point(3,1),new Point(4,1),new Point(5,1),new Point(5,2)
                                          ,new Point(5,3),new Point(4,4),new Point(3,5),new Point(2,4),new Point(1,3),new Point(1,2)};
    
    private static Point[] innerRecources={new Point(2,2),new Point(3,2),new Point(4,2),new Point(4,3),new Point(3,4),new Point(2,3)};
    private static Point pointMiddle=new Point(3,3);
    private static int[] getalfiches={5,2,6,3,8,10,9,12,11,4,8,10,9,4,5,6,3,11}; //A is op plaats 0,B op 1,....
    private SelectTileModel selectModel;
    private ISettlement lastSettlement;
    private boolean placeSettlementPossible=true;
    private boolean placeRoadPossible=true;
    private boolean roadToSettlement=false;
    private boolean placeCity=false;
    private boolean nonSeaRoad=false;
    private boolean nonSeaStartSettlement=false;
    boolean dontShowOptionPanesAgain=false;//toont al dan niet de popupvensters

    
    SurroundPanel surroundPanel;
//    MessagePanel messagePanel; 
    /**
     * Enige constructor, maakt een veld aan met emptyvakjes en zeevakjes
     * @param selectModel ons linkerpaneel tijdens slepen tiles
     */
    public Board(SelectTileModel selectModel){
        tiles = new ITile[XDIM][YDIM];
         for(int i=0;i<XDIM;i++){
            for(int j=0;j<NUMBERTILES[i];j++){
                
                if(i==0||i==XDIM-1||j==0||j==NUMBERTILES[i]-1) {
                    tiles[i][j]=new Tile(TileType.SEA);
                } //waterttype = haven/zee
                else {
                    tiles[i][j]=new Tile(TileType.EMPTY);
                } //grondtype
                tiles[i][j].setNumber(-2);
            }
            
         }
         
         this.selectModel = selectModel;
    } //aanmaken modelveld
   
    /**
     * Voegt een Speler toe aan de lijst van spelers.
     * @param player nieuwe Speler.
     */
    public void addPlayer(IPlayer player){
        players.add(player);
    }
    
    /**
     * Geeft een tegel terug die op positie (x,y) staat.
     * Er wordt gecontroleerd op de invoerwaarden.
     * @param x x-coördinaat van de tegel
     * @param y y-coördinaat van de tegel
     * @return  tegel op positie (x,y)
     */
    public ITile getTile(int x, int y){
        if ( x >= 0 && x <= XDIM && y >=0 && y <= YDIM) {
            return tiles[x][y];
        }
        else {
            return null;
        }
    }
    
    /**
     * Zet de Rover op de tegel met positie (x,y).
     * Controleert als er al een tegel de rover bevat, anders verwijdert hij de rover.
     * Vervolgens wordt hij op de nieuwe positie geplaatst.
     * @param x x-coördinaat van de tegel
     * @param y y-coördinaat van de tegel
     */
    public void setThief(int x , int y){
        if(thiefTile!=null) {
            thiefTile.setThief(false);
        }
        
        thiefTile = tiles[x][y];
        tiles[x][y].setThief(true);
    }
    
    /**
     * Plaatst een tegel van het type <code>type</code> op positie (x,y).
     * @param x x-coördinaat van de tegel
     * @param y y-coördinaat van de tegel
     * @param type voorgedefinieerd grondstoftype
     * @see TileType
     */
    public void setTile(int x, int y,TileType type){
        tiles[x][y] = new Tile(type);
    }
    
    /**
     * plaatst het juiste nummer op een tegel gegeven tegel aangeduid door een index
     * berekent de plaats van een tegel in de spiraal uit een gewone rij
     * @param i nummer van de tile waaruit het echte tileObject kan afgeleidt worden
     * @param rowTiles rij van tegels ofwel de buitenste circel tegels ofwel de binnecste circel
     * @param wichnumber duidt de index van het getal aan dat moet geplaatst worden
     * @param n duidt het aantal tegels aan die al een nummer voordien hebben gekregen
     * @return 
     */
    private int placeNumber(int i, Point[] rowTiles,int wichnumber,int n) {
        if(tiles[rowTiles[(i+n)%rowTiles.length].x][rowTiles[(i+n)%rowTiles.length].y].getType()!=TileType.DESERT){ //als tegel desert is geen nummer leggen,anders wel
            tiles[rowTiles[(i+n)%rowTiles.length].x][rowTiles[(i+n)%rowTiles.length].y].setNumber(getalfiches[wichnumber]); //plaats getalfiche
            wichnumber++;
        }
        return wichnumber;
    }
    /**
     * loopt alle tegels af en roept de functie setTile ervan op
     * wordt opgeroepen als er op een tegel geklikt wordt
     * aflopen van de tegels begint bij de aangeklikte tegel als deze een hoekpunt van het veld is
     * @param x xCoordinaat van de tegel waarop geklikt is
     * @param y yCoordinaat van de tegel waarop geklikt is
     */
    public void placeNumbers(int x, int y){
        Point p=new Point(x,y);
        int n=searchBeginPoint(p);
        if(n%2==0){ //testen of het gaat om hoekpunt
            int wichnumber=0; // om houdt bij aan welk getal men zit
            
            for(int i=0;i<outerRecources.length;i++){
                wichnumber=placeNumber(i,outerRecources,wichnumber,n);
            }
            n/=2;
            for(int i=0;i<innerRecources.length;i++){
                wichnumber=placeNumber(i,innerRecources,wichnumber,n);
            }
            
                placeNumber(0,new Point[]{pointMiddle},wichnumber,0);
            goNextFase();
        }
    }
    
    /**
     * zoekt de index van het beginpunt uit de rij van outerRecources 
     * oneven waarde stelt een niet gevonden punt voor: alleen hoekpunten zijn aanklikbaar
     * @param p het Point waarop door de gebruiker geklikt werd
     * @return 
     */
    private int searchBeginPoint(Point p) {
        for(int i=0;i<outerRecources.length;i+=2){//alleen hoekpunten zijn aanduidbaar->+2
            if(p.equals(outerRecources[i])) {
                return i;
            }
        }
        return 1;
    }
    
    /**
     * Geeft alle tegels met nummer <code>number</code> op.
     * @param number dobbelbaar getal
     * @return lijst van tegels met <code>number</code> op
     */
    private ArrayList<ITile> getTilesWithNumber(int number){
        ArrayList<ITile> list = new ArrayList<ITile>();
        
        for (int y = 0 ; y < YDIM ; y++){
            for (int x = 0 ; x < XDIM ; x++){
                if (tiles[x][y] != null && tiles[x][y].getNumber() == number){
                    list.add(tiles[x][y]);
                }
            }
        }
        
        return list;
    }
    
    /**
     * Geeft Spelers die een nederzetting hebben op tegels met nummer <code>number</code> op hun grondstoffen.
     * @param number gedobbelde getal
     */
    public void giveResources(int number){
        ArrayList<ITile> list = getTilesWithNumber(number);
        if(!list.isEmpty()){
            for( ITile tile : list){
                ISettlement[] settlements = tile.getSettlements();         
                    for(ISettlement settlement : settlements){
                        if(settlement != null){
                            for(int i=0;i<settlement.settlementSize();i++) {
                                settlement.getPlayer().addResource(tile.getType());
                            }
                        }  
                    }
            }            
        }
        
    }
    
    /**
     * vraagt de geplaatste tile van het selectmodel op en plaatst deze op het board op de plaats waarop geklikt werd
     * @param x xlocatie van de geklikte tile
     * @param y xlocatie van de geklikte tile
     */
    public void placeTile(int x, int y) {
        ITile tileToPlace=selectModel.getSelectedTile();
        if (tileToPlace != null){//een selected ?
            if (tiles[x][y].getType() == TileType.EMPTY){//vrij
                tiles[x][y] = new Tile(tileToPlace);
                tiles[x][y].setNumber(-2); //-2 zorgt dat geen nummer getekend wordt
            }
            else {
                selectModel.returnTile(tileToPlace);
            }
        }
        selectModel.resetSelected();
    }
    public void returnTile(int x, int y) {
        
        TileType type=tiles[x][y].getType();
        if (type != TileType.EMPTY&&type !=TileType.SEA){//vrij
            tiles[x][y] = new Tile(TileType.EMPTY);
            tiles[x][y].setNumber(-2); //-2 zorgt dat geen nummer getekend wordt
            selectModel.returnTile(type);
        }
    }
    
    /**
     * 
     * @param x xlocatie van de geklikte tile
     * @param y xlocatie van de geklikte tile
     */
    public void placeTile(int x, int y, TileType type) {
        if (type != null){//een selected ?
            if (tiles[x][y].getType() == TileType.EMPTY){//vrij
                tiles[x][y] = new Tile(type);
                tiles[x][y].setNumber(-2); //-2 zorgt dat geen nummer getekend wordt
                selectModel.takeTile(type);
            }
        }
    }
    
    /**
     * Plaatst de Rover op de woestijntegel.
     */
    public void setThiefInDesert() {
        for (int y = 0 ; y < YDIM ; y++){
            for (int x = 0 ; x < XDIM ; x++){
                if (tiles[x][y] != null && tiles[x][y].getType()==TileType.DESERT){
                    tiles[x][y].setThief(true);
                }
            }
        }
        
    }    
    /**
     * Controleert als het plaatsen van een nederzetting in de startfase mogelijk is.
     * Houdt bij als het niet mogelijk is.
     * @param x x-coördinaat van de tegel
     * @param y y-coördinaat van de tegel
     * @param index nederzettingindex (index tussen 0 en 5, startend van uiterst linkse punt)
     */
    public void checkPossibleStartSettlement(int x, int y, int index) {
        if(!getTile(x, y).settlementPossible(index)){
            placeSettlementPossible=false;
        }
        if(!checkSea(x, y)) {
            nonSeaStartSettlement=true;
        }
    }
    private boolean checkSea(int x,int y){
        return getTile(x, y).getType().equals(TileType.SEA);
            
    }
    /**
     * Controleert als het plaatsen van een nederzetting in de speelfase mogelijk is.
     * Houdt bij als het niet mogelijk is.
     * @param x x-coördinaat van de tegel
     * @param y y-coördinaat van de tegel
     * @param index nederzettingindex (index tussen 0 en 5, startend van uiterst linkse punt)
     */
     public void checkPossibleSettlement(int x, int y, int index) {
        if(players.get(currentPlayer).buildCityPossible()&&(getTile(x, y).CityPossible(index))) {
            {
                placeCity=true;
            }
         }
        else if(players.get(currentPlayer).buildSettlementPossible() ){
            
            if(!getTile(x, y).settlementPossible(index)){
                placeSettlementPossible=false;
            }
            if(getTile(x, y).roadToSettlement(index)){
                roadToSettlement=true;
            }
        }
    }
     /**
     * Plaatst in aangrenzende tegels (meegekregen uit view) de weg op de juiste index.[SPEELFASE]
     * Controleert als plaatsen van nederzetting mogelijk is.
     * Past de laatst geplaatste nederzetting aan met de nieuwe indien nodig.
     * Zorgt ervoor dat de verbruikte grondstoffen verwijderd worden.
     * @param tellers lijst van {x-coördinaat tegel, y-coördinaat tegel, nederzettingindex op tegel}
     * @return true als plaatsen van nederzetting gelukt is
     */
     public void setSettlements(ArrayList<int[]> tellers) {
        if(placeCity){
            if(askConfirmation("Plaatsen stad","Weet je zeker dat je hier een stad wilt plaatsen?")){
                for(int i=0;i<tellers.size();i++){
                     getTile(tellers.get(i)[0], tellers.get(i)[1]).setSettlement(new City(players.get(currentPlayer)), tellers.get(i)[2]);
                 }
                players.get(currentPlayer).CityBuilt();  
                placeCity=false;
            }
        }
        else if(placeSettlementPossible&&roadToSettlement){
            if(askConfirmation("Plaatsen dorp","Weet je zeker dat je hier een dorp wilt plaatsen?")){
                for(int i=0;i<tellers.size();i++){
                    getTile(tellers.get(i)[0], tellers.get(i)[1]).setSettlement(new Village(players.get(currentPlayer)), tellers.get(i)[2]);
                }
                players.get(currentPlayer).settlementBuilt();  
            }
        }
        placeSettlementPossible=true;
        roadToSettlement=false;   
    }

    /**
     * Controleert als het plaatsen van een weg in de startfase mogelijk is.
     * Houdt bij als het niet mogelijk is.
     * Geeft door waar de laatste nederzetting geplaatst werd.
     * @param x x-coördinaat van de tegel
     * @param y y-coördinaat van de tegel
     * @param index wegindex (index tussen 0 en 5, startend van linkerbovenkant)
     */
    public void checkPossibleStartRoad(int x, int y, int index) {
        if(!getTile(x, y).startRoadPossible((index),lastSettlement)){
            placeRoadPossible=false;
        }
        if(!getTile(x, y).getType().equals(TileType.SEA)){
            nonSeaRoad=true;
        }
    }
    /**
     * Controleert als het plaatsen van een weg in de startfase mogelijk is.
     * Houdt bij als het niet mogelijk is.
     * Geeft door waar de laatste nederzetting geplaatst werd.
     * @param x x-coördinaat van de tegel
     * @param y y-coördinaat van de tegel
     * @param index wegindex (index tussen 0 en 5, startend van linkerbovenkant)
     */
    public void checkPossibleRoad(int x, int y, int index) {
        if(getTile(x, y).roadPossible(index)&&players.get(currentPlayer).buildRoadPossible()){
            placeRoadPossible=true;
        }
        if(!getTile(x, y).getType().equals(TileType.SEA)){
            nonSeaRoad=true;
        }
        
    }
    
    /**
     * Plaatst een weg op de tegels waarvan de indices zijn meegegeven in tellers
     * Controleert als het wel degelijk mogelijk is om één te plaatsen.
     * Indien gelukt, wordt er een nieuwe weg aangemaakt met als eigenaar de huidige Speler.
     * @param tellers houdt de tegels en de positie van de nieuwe road binnen de tile bij waar road moeten gemaakt worden
     */
    public void setRoads(ArrayList<int[]> tellers) {
        
        if(placeRoadPossible&&nonSeaRoad){
            if(askConfirmation("Plaatsen straat","Weet je zeker dat je hier een straat wilt plaatsen?")){
                Road road=new Road(players.get(currentPlayer));
                for(int i=0;i<tellers.size();i++){
                    getTile(tellers.get(i)[0], tellers.get(i)[1]).setRoad(road, tellers.get(i)[2]);
                }
                players.get(currentPlayer).roadBuilt();
            }
        }
        placeRoadPossible=false;
        nonSeaRoad=false;
    }
    
    /**
     * Plaatst in aangrenzende tegels (meegekregen uit view) de weg op de juiste index.
     * Controleert als het mogelijk is om de weg te plaatsen.
     * Zorgt ervoor dat de constructieaantallen aangepast worden.
     * @param tellers lijst van {x-coördinaat tegel, y-coördinaat tegel, wegindex op tegel}
     * @return true als plaatsen van weg gelukt is
     */
    public boolean setStartRoads(ArrayList<int[]> tellers) {
        
        if(placeRoadPossible&&nonSeaRoad ){
            if(askConfirmation("Plaatsen startstraat","Weet je zeker dat je hier een startstraat wilt plaatsen?")){
                Road road=new Road(players.get(currentPlayer));
                for(int i=0;i<tellers.size();i++){
                    getTile(tellers.get(i)[0], tellers.get(i)[1]).setStartRoad(road, tellers.get(i)[2]);
                }
                players.get(currentPlayer).startRoadBuilt();
                nonSeaRoad=false;
                return true;
            
            }
        
    
        }
        placeRoadPossible=true;
        nonSeaRoad=false;
        return false;
    }
    /**
     * Plaatst in aangrenzende tegels (meegekregen uit view) de weg op de juiste index [STARTFASE].
     * Controleert als plaatsen van nederzetting mogelijk is.
     * Past de laatst geplaatste nederzetting aan met de nieuwe indien nodig.
     * Zorgt ervoor dat de constructieaantallen aangepast worden.
     * @param tellers lijst van {x-coördinaat tegel, y-coördinaat tegel, nederzettingindex op tegel}
     * @return true als plaatsen van nederzetting gelukt is
     */
    public boolean setStartSettlements(ArrayList<int[]> tellers) {
        if(placeSettlementPossible&&nonSeaStartSettlement){
            if(askConfirmation("Plaatsen startdorp","Weet je zeker dat je hier een startdorp wilt plaatsen?")){
                ISettlement settlement =new Village(players.get(currentPlayer));
                for(int i=0;i<tellers.size();i++){

                    getTile(tellers.get(i)[0], tellers.get(i)[1]).setSettlement((settlement), tellers.get(i)[2]);
                    if(     lastSettlement!=null&&
                            getTile(tellers.get(i)[0], tellers.get(i)[1]).getType()!=TileType.DESERT&&
                            getTile(tellers.get(i)[0], tellers.get(i)[1]).getType()!=TileType.SEA){
                        players.get(currentPlayer).addResource(getTile(tellers.get(i)[0], tellers.get(i)[1]).getType());
                    }
                }
                lastSettlement=settlement;
                players.get(currentPlayer).startSettlementBuilt();
                nonSeaStartSettlement=false;
                return true;
            }
            
        }
        placeSettlementPossible=true;
        nonSeaStartSettlement=false;
        return false;
    }
    /**
     * begint da gamefase
     */
    public void initiateGameFase() {
        placeRoadPossible=false;
        placeSettlementPossible=true;  
    }
    /**
     * als intiatebuilding true is gaat naar placebuildingfase anders naar gamefase
     */
    public void goNextFase(){
        if(initiateBuilding) {
                    surroundPanel.initiateBuildingPlacements();
                }
                else {
                    surroundPanel.initiateGameFase();
                }
                initiateBuilding=false;
    }

    public void setSurroundPanel(SurroundPanel surroundPanel) {
        this.surroundPanel = surroundPanel;
    }
    private boolean askConfirmation(String titel,String vraag){
        JCheckBox checkbox = new JCheckBox("Toon dit bericht niet meer");  
        String message = vraag;  
        Object[] params = {message, checkbox};
       if(dontShowOptionPanesAgain){
                return true;
            }
       else{
                boolean showOptionPane=JOptionPane.showOptionDialog(surroundPanel, params, titel, JOptionPane.YES_NO_OPTION,
                                            JOptionPane.PLAIN_MESSAGE,null, new String[]{"Ja", "Nee"},"default")==JOptionPane.YES_OPTION;
                dontShowOptionPanesAgain = checkbox.isSelected();
                return showOptionPane;
       } 
    }
    public void setDontShowOptionPanesAgain(boolean dontShowOptionPanesAgain) {
        this.dontShowOptionPanesAgain = dontShowOptionPanesAgain;
    }
}

