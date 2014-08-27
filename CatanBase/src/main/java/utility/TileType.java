/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

/**
 * Deze enum houdt de verschillende types van tegels bij.
 * @author Andreas De Lille
 */
public enum TileType {
    WHEAT(0,"graan"),
    WOOL(1,"wol"),   
    STONE(2,"steen"),
    IRON(3,"ijzer"),
    WOOD(4,"hout"),
    DESERT(5,"woestijn"),
    SEA(6,"zee"),
    EMPTY(7,"leeg");
    public static int NUMBER_OF_RESOURCES=5;
    private final int value;
    private final String translation;
    
    TileType(int value,String name){
        this.value=value;
        this.translation=name;
    }
    
    public int getValue(){
        return value;
    }
    public String getTranslation(){
        return translation;
    }
}
