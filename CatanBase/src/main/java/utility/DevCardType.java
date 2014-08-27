/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

/**
 *
 * @author Jens
 */
public enum DevCardType {
    KNIGHT(0),
    ROAD(1),
    VICTORYPOINT(2),
    RESOURCES(3),   
    MONOPOLY(4);
   
    public static int NUMBER_OF_CARDS=5;
    private final int value;
    
    DevCardType(int value){
        this.value=value;
    }
    
    public int getValue(){
        return value;
    }
    
    
            
}
