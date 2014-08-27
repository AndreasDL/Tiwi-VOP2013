/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.west;

import GUI.bot.SouthPanel;
import GUI.west.ResourcePanel;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import model.interfaces.IController;
import model.interfaces.IPlayer;
import observer.IObservable;
import observer.IObserver;
import utility.Layout;

/**
 *Dit paneel geeft weer welke grondstoffen de speler bezit.
 * Maakt hiervoor gebruik van een {@link ResourcePanel}
 * @author Jens
 */
public class PlayerPanel extends JPanel implements IObserver{ 
    private JLabel lblPlayerName;
   // private final Dimension LABEL_DIM = new Dimension(50, 50);    
    private ResourcePanel resourcePanel;
    private DevCardPanel devCardPanel;
    
    /**
     * Zet de naam van de Speler bovenaan met eronder een rooster van de huidige grondstoffen die de Speler heeft.
     */
    public PlayerPanel(IController controller,SouthPanel s) {
        ToolTipManager.sharedInstance().setInitialDelay(0);
        this.resourcePanel = new ResourcePanel();
        setOpaque(false);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        lblPlayerName = new JLabel(); //hardcoded
        //lblPlayerName.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        lblPlayerName.setFont(Layout.FONT);
        lblPlayerName.setForeground(Layout.PLAYERNAME_FOREGR);
        //lblPlayerName.setPreferredSize(LABEL_DIM);
       // lblPlayerName.setHorizontalAlignment(JLabel.LEFT);
        lblPlayerName.setAlignmentX(Component.CENTER_ALIGNMENT);
       // lblPlayerName.setBorder(Layout.BORDER);
      //  setPreferredSize(new Dimension(200,200));
       // setAlignmentY(CENTER_ALIGNMENT);
       // resourcePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
      //  Border matte = BorderFactory.createMatteBorder(10, 0, 10, 10, Color.GRAY);
       // Border blackline =  BorderFactory.createLineBorder(Color.darkGray,3);
       // setBorder(BorderFactory.createCompoundBorder(blackline,matte));
        
        devCardPanel = new DevCardPanel(controller,s);
        
        add(Box.createVerticalGlue());
        add(lblPlayerName);
        add(resourcePanel);    
        add(devCardPanel);
        add(Box.createVerticalGlue());
      // setBorder(BORDER);
    }
    
    /**
     * Wordt opgeroepen als het aantal grondstoffen in <code>Player</code> aangepast wordt.
     * Roept vervolgens <code>valuesChanged</code> op van {@link ResourcePanel}.
     * @param o Object Player die de huidige Speler is
     * @param arg n.v.t
     */

    public void update(IObservable o, String arg) {
        IPlayer player = (IPlayer) o;
        lblPlayerName.setText(player.getName());
        resourcePanel.valuesChanged(player);
        //if(arg != null && arg == "cards"){
            devCardPanel.valuesChanged(player);
            
        //}
    }
}
