/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Jens
 */
public class ConcretePlayer implements IPlayer{

    private String name;
    
    public ConcretePlayer(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return "ConcretePlayer";
    }    
 
    
}
