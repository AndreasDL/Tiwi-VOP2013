/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import controller.IController;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import model.Player;

/**
 *Groepeert een {@link ActionPanel} en een {@link StructurePanel}.
 * @author Jens
 */
public class EastPanel extends JPanel{
    private ActionPanel actionPanel;
    private StructurePanel structPanel;

    /**
     * Maakt een actiepaneel en structurepaneel aan en zet ze samen in een <code>BoxLayout</code>.
     * @param controller de huidige controller
     */
    public EastPanel(IController controller) {
        this.setPreferredSize(new Dimension(250, 200));
        
        this.structPanel = new StructurePanel();
        this.actionPanel = new ActionPanel(controller);
        setLayout(new FlowLayout());
        add(structPanel);
        add(actionPanel);
        
        
    }
    /**
     * Voegt het StructurePanel toe als Observer aan Player.
     * @param player huidige Speler
     */
    public void addObserver(Player player){ 
             player.addObserver(structPanel);
        
    }
    
    
    
}
