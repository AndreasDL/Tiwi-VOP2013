/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.interfaces.IPlayer;
import utility.Layout;

/**
 *Dit paneel geeft weer welke grondstoffen de speler bezit.
 * Maakt hiervoor gebruik van een {@link ResourcePanel}
 * @author Jens
 */
public class PlayerPanel extends JPanel implements Observer{ 
    private JLabel lblPlayerName;
   // private final Dimension LABEL_DIM = new Dimension(50, 50);    
    private ResourcePanel resourcePanel;
    
    /**
     * Zet de naam van de Speler bovenaan met eronder een rooster van de huidige grondstoffen die de Speler heeft.
     */
    public PlayerPanel() {
        this.resourcePanel = new ResourcePanel();
        setLayout(new BorderLayout());
        lblPlayerName = new JLabel("naamloos"); //hardcoded
        lblPlayerName.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        lblPlayerName.setFont(Layout.FONT);
        //lblPlayerName.setPreferredSize(LABEL_DIM);
        lblPlayerName.setHorizontalAlignment(JLabel.CENTER);
        add(lblPlayerName,BorderLayout.NORTH);
        add(resourcePanel);
      // setBorder(BORDER);
    }
    
    /**
     * Wordt opgeroepen als het aantal grondstoffen in <code>Player</code> aangepast wordt.
     * Roept vervolgens <code>valuesChanged</code> op van {@link ResourcePanel}.
     * @param o Object Player die de huidige Speler is
     * @param arg n.v.t
     */
    @Override
    public void update(Observable o, Object arg) {        
        IPlayer player = (IPlayer) o;
        lblPlayerName.setText(player.getName());
        resourcePanel.valuesChanged(player);
    }
}
