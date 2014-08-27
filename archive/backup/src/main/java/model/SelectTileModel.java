/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import GUI.SelectTilePanel;
import java.util.ArrayList;
import model.interfaces.ITile;
import utility.TileType;

/**
 * Deze klasse is het achterliggende model dat gebruikt wordt voor het SelectTilepanel.
 * Dit model is noodzakelijk tijdens de aanmaakfase van het spelbord.
 * @author Andreas De Lille
 */
public class SelectTileModel {
    SelectTilePanel selectpanel;
    
    private int selectedTile;
    private ArrayList<ITile> tiles;
    public static final int WOOD = 0;
    public static final int IRON = 1;
    public static final int WOOL = 2;
    public static final int STONE = 3;
    public static final int WHEAT = 4;
    public static final int DESERT = 5;
    
    //constructor
    /**
     * Standaard constructor.
     * Je moet wel nog een selectpanel instellen dat gebruikt wordt.
     */
    public SelectTileModel(){
        tiles = new ArrayList<ITile>();
        initTiles();
        selectedTile = -1;
        
    }
    
    //getters en setters
    /**
     * Geeft het aantal van een bepaald type terug.
     * @param i Het nummer van het type
     *      De nummers zijn beschikbaar via static constanten.
     * @return Geeft het aantal velden weer dat nog moet gelegd worden van een bepaald type.
     */
    public int getCount(int i){
        return tiles.get(i).getNumber();
    }
    /**
     * Geeft de tegel terug van het gegeven type.
     * @param i Het nummer van het type
     *      De nummers zijn beschikbaar via static constanten.
     * @return Geeft de tegel terug van het gegeven type.
     */
    public ITile getTile(int i) {
        return tiles.get(i);
    }
    /**
     * Geeft de geselecteerde tegel terug.
     * Doet hierbij vanzelf het aantal overige van de soort naar beneden.
     * @return Geeft de geselecteerde tegel terug.
     *      Indien er geen tegel geselecteerd is wordt er null terug gegeven.
     */
    public ITile getSelectedTile(){
        //indien null => niets
        //anders de soort die ermee overeen komt.
        
        //aantal naar beneden doen        
        if (selectedTile > -1 && tiles.get(selectedTile).getNumber() > 0 ){
            tiles.get(selectedTile).setNumber(tiles.get(selectedTile).getNumber()-1);        
        }else if (selectedTile > -1 && tiles.get(selectedTile).getNumber() == 0) {
            selectedTile = -1;
        }

        if (selectedTile != -1){
            return tiles.get(selectedTile);
        }else{
            return null;
        }
    }
    /**
     * Geeft de geslecteerde tegel terug
     * @return 
     */
    public ITile getSelectedTileToDraw(){
        if (selectedTile != -1){
            return tiles.get(selectedTile);
        }else{
            return null;
        }   
        
    }
    /**
     * Verwijdert 1 tegel van het type tileType uit het selectiepaneel
     * @param tileType 
     */
    public void takeTile(TileType tileType) {
        for(int i=0;i<tiles.size();i++){
            if(tiles.get(i).getType() == tileType){
                tiles.get(i).setNumber(tiles.get(i).getNumber() -1);
            }
        }


    }
    /**
     * Zet 1 tegel van het type tileType terug in het selectiepaneel
     * @param tileType 
     */
    public void returnTile(TileType tileType) {
        for(int i=0;i<tiles.size();i++){
            if(tiles.get(i).getType() == tileType){
                tiles.get(i).setNumber(tiles.get(i).getNumber() +1);
            }
        }
        selectpanel.repaint();
    }
    /**
     * Indien een tegel niet getekend kan worden, moet men deze teruggeven zodat de aantallen blijven kloppen.
     * @param tile De tegel die overeen komt met de soort waarvan het aantal terug omhoog moet.
     */
    public void returnTile(ITile tile){
        //aantal naar boven doen        
        if (selectedTile > -2 && tiles.get(selectedTile).getNumber() >= 0 ){
            tiles.get(selectedTile).setNumber(tiles.get(selectedTile).getNumber()+1);        
        }
        selectpanel.repaint();
        
    }
    
    /**
     * Stel de geselecteerde tegel in.
     * @param i Het nummer van het type
     *      De nummers zijn beschikbaar via static constanten.
     */
    public void setSelected(int i){
        selectedTile = i;
    }
    /**
     * Reset de geselecteerde tegel.
     */
    public void resetSelected(){
        selectedTile = -1;
        selectpanel.repaint();
        int sum=0;
        for (int i = 0 ; i < tiles.size() ; i++){
            sum+= tiles.get(i).getNumber();
        }
        if(sum == 0)
            selectpanel.setDone();
        
    }
    /**
     * Ga terug naar de begin waarde.
     * Herstel de aantallen van alle type tegels naar de oorspronkelijke toestand.
     */
    public void resetTiles(){
        initTiles();
    }
    /**
     * Stel het selectpanel in dat gebruikt wordt.
     * @param selectPanel Het selectpanel dat gebruikt wordt.
     */
    public void setSelectTilePanel(SelectTilePanel selectPanel) {
        this.selectpanel=selectPanel;
        selectpanel.repaint();
    }
    
    //private
    private void initTiles(){
        tiles.add(new Tile(TileType.WOOD,4));
        tiles.add(new Tile(TileType.IRON,3));
        tiles.add(new Tile(TileType.WOOL,4));
        tiles.add(new Tile(TileType.STONE,3));
        tiles.add(new Tile(TileType.WHEAT,4));
        tiles.add(new Tile(TileType.DESERT,1));

        selectedTile = -1;

    }

}
