/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import controller.IController;
import javax.swing.JPanel;

/**
* Deze klasse Stelt het actiepaneel voor. 
* Dit paneel geeft een overzicht van alle mogelijk actie die de speler kan uitvoeren.
* Dit is momenteel : Shop (kopen van dorp/straat/stad)
* De mogelijk om een beurt te spelen en dus de dobbelsteen te rollen.
* @author tom
*/
public class ActionPanel extends JPanel{
    private IController controller;
    
    /**
     * De standaard constructor. 
     * @param controller controller die moet meegegeven worden. (Soort is afhankelijk van de fase waarin het spel zich bevindt.)
     */
    public ActionPanel(IController controller){
       add(new Dices(controller));
    }
}
