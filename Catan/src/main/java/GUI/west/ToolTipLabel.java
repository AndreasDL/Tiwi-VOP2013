/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.west;

import GUI.bot.DevCardToolTip;
import GUI.bot.SouthPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JToolTip;
import javax.swing.ToolTipManager;
import model.interfaces.IController;
import model.interfaces.IPlayer;

/**
 *
 * @author Andreas De Lille
 */
public class ToolTipLabel extends JLabel implements MouseListener {
    //private DevCardToolTip tool;
    private IController controller;
    private SouthPanel south;
    
    public ToolTipLabel(IController controller,SouthPanel south){
        try {
            //super();
            setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/devCards/back.jpg"))));
        } catch (IOException ex) {
            Logger.getLogger(ToolTipLabel.class.getName()).log(Level.SEVERE, null, ex);
        }
        setToolTipText("");
        this.controller = controller;
        //tool = new DevCardToolTip(controller);
        //tool.setComponent(this);
        ToolTipManager.sharedInstance().setDismissDelay(5000);
        this.south = south;
        
        this.addMouseListener(this);
    }
        /*
    @Override
    public JToolTip createToolTip() {
        //return tool;
    }*/
    public void valuesChanged(IPlayer p){
        south.updateBank(p);
    }

    public void mouseClicked(MouseEvent e) {
        south.showHideDev();
    }
    public void mousePressed(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
}
