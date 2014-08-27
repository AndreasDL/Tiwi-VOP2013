/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.interfaces.IPlayer;
import model.interfaces.ISettlement;
import utility.Structure;

/**
 *
 * @author Samuel
 */
public class City implements ISettlement{

    private IPlayer player;

    /**
     * Maakt een Stad aan met als eigenaar <code>player</code>
     * @param player eigenaar van de Stad
     */
    public City(IPlayer player){
        this.player = player;
    }  
    
    /**
     * Geeft de speler terug die eigenaar is van de stad.
     * @return speler die eigenaar is
     */
    @Override
    public IPlayer getPlayer() {
        return player;
    }
    public int settlementSize(){
        return Structure.CITY.getValue();
    }
    
}
