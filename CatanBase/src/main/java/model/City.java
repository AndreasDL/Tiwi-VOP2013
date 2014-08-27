/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.interfaces.IPlayer;
import model.interfaces.ISettlement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utility.Structure;

/**
 *
 * @author Samuel
 */
public class City implements ISettlement{
    private static final Logger log = LoggerFactory.getLogger(City.class);
    
    private IPlayer player;

    /**
     * Maakt een Stad aan met als eigenaar <code>player</code>
     * @param player eigenaar van de Stad
     */
    public City(IPlayer player){
        log.debug("Entering city constructor ( player= {} )",player);
        
        this.player = player;
    }  
    
    /**
     * Geeft de speler terug die eigenaar is van de stad.
     * @return speler die eigenaar is
     */
    @Override
    public IPlayer getPlayer() {
        log.debug("returning player {}",player);
        return player;
    }
    public int settlementSize(){
        log.debug("returning settlementSize= {}",Structure.CITY.getValue());
        return Structure.CITY.getValue();
    }
    
}
