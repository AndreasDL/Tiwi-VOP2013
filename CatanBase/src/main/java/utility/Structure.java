/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

/**
 * Deze enum houdt de verschillende types van structures bij.
 * @author tom
 */
public enum Structure {
    ROAD(0),
    SETTLEMENT(1),   
    CITY(2);
    
    private final int value;
    
    Structure(int value){
        this.value=value;
    }
    
    public int getValue(){
        return value;
    }
}
