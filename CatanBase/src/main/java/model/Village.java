package model;

import model.interfaces.ISettlement;
import model.interfaces.IPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Deze klasse stelt een settlement voor.
 * @author Andreas De Lille
 */
public class Village implements ISettlement{
    private static final Logger log = LoggerFactory.getLogger(Village.class);
    private IPlayer player;

    /**
     * Maakt een dorp aan met als eigenaar <code>player</code>
     * @param player eigenaar van het dorp
     */
    public Village(IPlayer player){
        log.debug("Entering Village constructor ( player= {} )", player);
        this.player = player;
    }  
    
    /**
     * Geeft de speler terug die eigenaar is van het dorp.
     * @return speler die eigenaar is
     */
    @Override
    public IPlayer getPlayer() {
        log.debug("Returning player {}", player);
        return player;
    }
    public int settlementSize(){
        log.debug("Returning settlementSize {}", 1);
        return 1;
    }

    
}