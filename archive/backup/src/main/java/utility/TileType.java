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
    WHEAT(0),
    WOOL(1),   
    STONE(2),
    IRON(3),
    WOOD(4),
    DESERT(5),
    SEA(6),
    EMPTY(7);
    public static int NUMBER_OF_RESOURCES=5;
    private final int value;
    
    TileType(int value){
        this.value=value;
    }
    
    public int getValue(){
        return value;
    }
}
