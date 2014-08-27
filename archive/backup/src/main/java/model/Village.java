package model;

import model.interfaces.ISettlement;
import model.interfaces.IPlayer;

/**
 * Deze klasse stelt een settlement voor.
 * @author Andreas De Lille
 */
public class Village implements ISettlement{
    private IPlayer player;

    /**
     * Maakt een dorp aan met als eigenaar <code>player</code>
     * @param player eigenaar van het dorp
     */
    public Village(IPlayer player){
        this.player = player;
    }  
    
    /**
     * Geeft de speler terug die eigenaar is van het dorp.
     * @return speler die eigenaar is
     */
    @Override
    public IPlayer getPlayer() {
        return player;
    }
    public int settlementSize(){
        return 1;
    }

    
}