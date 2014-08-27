/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import model.interfaces.IRoad;
import model.interfaces.ISettlement;
import model.interfaces.ITile;
import utility.TileType;

/**
 * Deze klasse stelt een tegel voor zoals deze wordt bewaard in het model.
 * @author Andreas De Lille
 */
public class Tile implements ITile {
    private int number;
    private TileType type;
    private ISettlement[] settlements = new ISettlement[6];
    private IRoad[] roads = new IRoad[6];
    private boolean hasThief;
    
    /**
     * Standaard constructor.
     * @param type het type van de tegel
     * @param number het nummer dat de tegel heeft.
     */
    public Tile(TileType type, int number){
        this.type=type;
        this.number = number;
        this.hasThief=false;
    }
    /**
     * Standaard constructor.
     * @param type het type van de tegel. (indien het nummer nog niet gekend is.)
     */
    public Tile(TileType type){
        this(type,0);
    }
    /**
     * copyconstructor constructor.
     * @param tile de tegel zoals deze in het model zit.
     */
    public Tile(ITile tile){
        this(tile.getType(),-1);
    }
    
    /**
     * Geeft het nummer terug dat bij de tegel hoort.
     * @return Het nummer dat bij deze tegel hoort.
     */
    @Override
    public int getNumber(){ 
        return number;
    }
    /**
     * Stel het nummer van de tegel in.
     * @param number het nummer dat nu bij de tegel moet staan.
     */
    @Override
    public void setNumber(int number){ 
        this.number = number;
    }

    /**
     * Geeft het type van de tegel terug.
     * @return het type van de tegel.
     */
    @Override
    public TileType getType(){
        return type;
    }
    
    /**
     * Stel het type van de tegel in.
     * @param type Het type van de tegel dat er moet staan nadat deze functie opgeroepen is.
     */
    @Override
    public void setType(TileType type){
        this.type = type;
    }
    
    /**
     * Geeft de nederzetting terug op <code>index</code>.
     * Er wordt gecontroleerd als het een geldige index is. (tussen 0 en 5)
     * @param index plaats op zeshoek (startend van linkerpunt)
     * @return indien er een nederzetting staat <code>ISettlement</code> anders <code>null</code>
     */
    @Override
    public ISettlement getSettlement(int index) {
        if ( index >= 0 && index <= 5) {
            return settlements[index];
        }
        else {
            return null;
        }
    }
    
    /**
     * Controleert als het mogelijk is om een nederzetting te plaatsen op <code>index</code>.
     * Hierbij wordt rekening gehouden als de index tussen 0 en 5 ligt, 
     * als er al geen nederzetting staat en als er geen nederzettingen naast liggen.
     * @param index plaats op zeshoek (startend van linkerpunt)
     * @return true als plaatsen mogelijk is
     */
    @Override
    public boolean settlementPossible(int index){
        return (index >=0 && index <= 5 &&
                settlements[index]==null &&
                settlements[(6+index-1)%6]==null &&
                settlements[(6+index+1)%6]==null 
                );
    }
    
    /**
     * Geeft weer of er een weg kan staan op <code>index</code>
     * Hierbij wordt rekening gehouden als de index tussen 0 en 5 ligt,
     * en de spelregels.
     * @param index de plaats van de weg
     * @return true als het plaatsen mogelijk is.
     */
    public boolean roadToSettlement(int index) {
        return (index >=0 && index <= 5 &&
                (roads[(index+6)%6]!=null ||
                roads[(6+index-1)%6]!=null));
    }

    /**
     * Geeft de weg terug die op <code>index</code> ligt.
     * @param index plaats op zeshoek (startend van linkerpunt)
     * @return true als er een weg ligt, anders <code>null</code>
     */
    @Override
    public IRoad getRoad(int index) {
        if (index >= 0 && index <= 5) {
            return roads[index];
        }
        else {
            return null;
        }
    }
    
    
    /**
     * Controleert als het plaatsen van een weg op <code>index</code> mogelijk is.
     * Er wordt gekeken als de index tussen 0 en 5 ligt, 
     * als er al geen weg ligt en als er ofwel een nederzetting naast ligt, ofwel een weg.
     * @param index plaats op zeshoek (startend van linkerpunt)
     * @return true als plaatsen mogelijk is
     */
    @Override
    public boolean roadPossible(int index){
        return (index >= 0 && index <= 5 &&
                roads[index] == null &&    (
                                             settlements[index]!=null   ||
                                             settlements[(index+1)%6]!=null ||
                                             (roads[(6+index-1)%6]!=null) ||
                                             (roads[(6+index+1)%6]!=null)
                                           )
                );
    }

    /**
     * Plaatst de meegegeven weg op <code>index</code>.
     * @param road de weg die je wil plaatsen
     * @param index plaats op zeshoek (startend van linkerpunt)
     */
    public void setRoad(IRoad road, int index) {
            roads[index] = road;

    }

    /**
     * Geeft aan als de tegel de Rover bevat
     * @param t true als hij Rover bevat
     */
    @Override
    public void setThief(boolean t) {
        this.hasThief = t;
    }
    
    /**
     * Geeft terug als de tegel de Rover bevat
     * @return true als de tegel de Rover bevat
     */
    @Override
    public boolean hasThief() {
        return this.hasThief;
    }
    
    /**
     * Geeft een tabel terug met nederzettingen.
     * @return nederzettingen in tabel
     */
    public ISettlement[] getSettlements(){       
        return settlements;
    }
    
    /**
     * Geeft de indexen terug die een nederzetting bevatten.
     * @return indexen waar een een nederzetting staat
     */
    public ArrayList<Integer> getSettlementsIndices(){
        ArrayList<Integer>indices=new ArrayList<Integer>();
       for(int i=0;i<settlements.length;i++){
           if(settlements[i]!=null) {
               indices.add(i);
           }
       }
        return indices;
        
    }
    
    /**
     * Geeft de indexen terug die een weg bevatten.
     * @return indexen met een weg op
     */
    public ArrayList<Integer> getRoadIndices(){
        ArrayList<Integer>indices=new ArrayList<Integer>();
       for(int i=0;i<roads.length;i++){
           if(roads[i]!=null) {
               indices.add(i);
           }
       }
        return indices;        
    }
  
    /**
     * Controleert als er een weg kan geplaatst worden. [STARTFASE]
     * Houdt rekening met index tussen 0 en 5, als er al geen weg ligt en
     * als de nederzetting ernaast net geplaatst is.
     * @param index plaats op zeshoek (startend van linkerpunt)
     * @param lastSettlement
     * @return true als weg mogelijk is
     */
    public boolean startRoadPossible(int index, ISettlement lastSettlement){
        return (index >= 0 && index <= 5 &&
                roads[index] == null &&
                ( (settlements[index%6]!=null&&settlements[index%6].equals(lastSettlement)) || 
                  (settlements[(index+1)%6]!=null&&settlements[(index+1)%6].equals(lastSettlement)) ) );
    }
    
    /**
     * Plaatst de meegegeven nederzetting op <code>index</code>.[STARTFASE]
     * @param settlement de te plaatsen nederzetting
     * @param index plaats op zeshoek (startend van linkerpunt)
     */
    public void setSettlement(ISettlement settlement, int index) {
        //if(settlementPossible(index)) {
            settlements[index] = settlement;
        //}
    }
    
    /**
     * Plaatst een de meegegeven weg op  <code>index</code>.
     * Controleert als plaatsen wel mogelijk is.
     * @param road de te plaatsen weg
     * @param index plaats op zeshoek (startend van linkerpunt)
     */
    public void setStartRoad(IRoad road, int index) {
        if(startRoadPossible(index)){
            roads[index] = road;            
        }
    }
    
    /**
     * Controleert als het plaatsen van een weg mogelijk is. [STARTFASE]
     * Houdt rekening met index tussen 0 en 5 , als er al een weg ligt
     * en als er een nederzetting naast ligt.
     * @param index plaats op zeshoek (startend van linkerpunt)
     * @return 
     */
    public boolean startRoadPossible(int index) {
        return (index >= 0 && index <= 5 &&
                roads[index] == null &&
                ( settlements[index%6]!=null || settlements[(index+1)%6]!=null ) );

    }
    /**
     * Controleert als het plaatsen van een stad mogelijk is.
     * Houdt rekening met index tussen 0 en 5 en als er al een nederzetting ligt.
     * vervangt de nederzetting door de stad
     * @param index plaats op zeshoek (startend van linkerpunt)
     * @return 
     */
    public boolean CityPossible(int index) {
         return (index >= 0 && index <= 5 &&
                 settlements[index] != null&&
                 settlements[index] instanceof Village);
    }
}
