/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.interfaces.IPlayer;
import model.interfaces.IRoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Deze klasse stelt een weg voor.
 * @author Andreas De Lille
 */
public class Road implements IRoad{
    private static final Logger log = LoggerFactory.getLogger(Road.class);
    
    private IPlayer player;

    /**
     * Maakt een weg met als eigenaar <code>player</code>.
     * @param player 
     */
    public Road(IPlayer player){
        log.debug("Entering Road constructor ( player= {} )", player);
        this.player = player;
    }
    
    /**
     * Geeft de eigenaar van de weg terug.
     * @return speler die eigenaar is
     */    
    @Override
    public IPlayer getPlayer() {
        log.debug("Returning player {}", player);
        return player;
    }    
    
}
