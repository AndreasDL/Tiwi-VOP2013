/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import model.Village;
import model.interfaces.IRoad;
import model.interfaces.ISettlement;
import model.interfaces.ITile;

/**
 * Deze klasse wordt gebruikt om de eigenschappen van de elementen weer te geven.
 * @author tom
 */
public class PropertiesPanel extends JDialog implements FocusListener{
    private ITile tile;
    private IRoad road;
    private ISettlement settlement;
    private ResourceBundle constants = ResourceBundle.getBundle("utility.config");
    
    /**
     * Standaard constructor.
     */
    public PropertiesPanel(){
        addFocusListener(this);
        setAlwaysOnTop(true);
        setResizable(false);
        setUndecorated(true);
    }

    /**
     * Stel de straat in waarvan de eigenschappen moet weergegeven worden.
     * @param road De straat die ingesteld wordt.
     */
    public void setRoad(IRoad road) {
        this.road = road;
        tile= null;
        settlement = null;
        setSize(200, 50);
    }
    /**
     * Stel het settlement in waarvan de eigenschappen moeten weergeven.
     * @param settlement Het settlement waarvan de eigenschappen moeten weergegeven worden.
     */
    public void setSettlement(ISettlement settlement) {
        this.settlement = settlement;
        tile = null;
        road = null;
        setSize(200, 70);
    }
    /**
     * Stel de tegel in waarvan info moet gegeven worden.
     * @param tile De tegel waarvan de eigenschappen moeten weergegeven worden.
     */
    public void setTile(ITile tile) {
        /**
         * 
         */
        this.tile = tile;
        settlement = null;
        road = null;
        setSize(150, 50);
    }

    /**
     * Uitekekenen van de component.
     * @param g het graphics object.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(tile !=null){
           g.setFont(g.getFont().deriveFont(Font.BOLD));
           g.drawString("Tegel", 20, 20);
           g.setFont(g.getFont().deriveFont(Font.PLAIN));
           g.drawString("Type: " + constants.getString(tile.getType().name()), 5, 40);
        }
        else if(settlement != null){
           g.setFont(g.getFont().deriveFont(Font.BOLD));
           g.drawString("Nederzetting", 20, 20);
           g.setFont(g.getFont().deriveFont(Font.PLAIN));
           if (settlement instanceof Village){
                g.drawString("Type: dorp", 5, 40);
           }else{
               g.drawString("Type: city", 5, 40);
           }
           g.drawString("Speler: " + settlement.getPlayer().getName(), 5, 60);
        }
        else if(road != null){
           g.setFont(g.getFont().deriveFont(Font.BOLD));
           g.drawString("Straat", 20, 20);
           g.setFont(g.getFont().deriveFont(Font.PLAIN));
           g.drawString("Speler: " + road.getPlayer().getName(), 5, 40);
        }
        g.drawRect(0, 0, getWidth() -1, getHeight() -1);
    }
    
    //events
    public void focusGained(FocusEvent arg0) {
        
    }
    public void focusLost(FocusEvent arg0) {
        this.setVisible(false);
    }
}
