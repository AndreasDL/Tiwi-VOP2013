/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.interfaces.IPlayer;
import model.interfaces.IRoad;

/**
 * Deze klasse stelt een weg voor.
 * @author Andreas De Lille
 */
public class Road implements IRoad{
    private IPlayer player;

    /**
     * Maakt een weg met als eigenaar <code>player</code>.
     * @param player 
     */
    public Road(IPlayer player){
        this.player = player;
    }
    
    /**
     * Geeft de eigenaar van de weg terug.
     * @return speler die eigenaar is
     */    
    @Override
    public IPlayer getPlayer() {
        return player;
    }    
    
}
