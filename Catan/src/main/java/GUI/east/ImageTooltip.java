/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.east;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolTip;
import utility.TileType;

/**
 *
 * @author Jens
 */
public class ImageTooltip extends JToolTip{

    private JPanel resourceCount;
    
    public ImageTooltip(int i) {
        super();
        setLayout(new BorderLayout());
        resourceCount = createRecipePanel(i);
        add(resourceCount);
        
    }

    @Override
    public Dimension getPreferredSize() {
        return  resourceCount.getPreferredSize();//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTipText(String tipText) {
        
    }
    
     private JLabel createResourceLabel(TileType type){
            JLabel rl = null;
            try {
                rl = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("/fullCards/"+type+".jpg")).getScaledInstance(20, 33, Image.SCALE_DEFAULT)));
            } catch (IOException ex) {
                Logger.getLogger(StructurePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            return rl;
    }
    
     private JPanel createRecipePanel(int i){
            JPanel resourceCount = new JPanel();
            // resourceCount.setLayout(new GridLayout(0,4));
             resourceCount.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
             //JPanel resourceCount2 = new JPanel();
             //resourceCount2.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
            
            if(i == 0){//1 WOOD + 1 STONE
                resourceCount.add(new JLabel("1 x "));                     
                resourceCount.add(createResourceLabel(TileType.WOOD));                     
                resourceCount.add(new JLabel("1 x "));
                resourceCount.add(createResourceLabel(TileType.STONE));
            }
            else if(i==1){//1 WOOD + 1 STONE + 1 WHEAT + 1 WOOL
                resourceCount.add(new JLabel("1 x "));
                resourceCount.add(createResourceLabel(TileType.WOOD));
                resourceCount.add(new JLabel("1 x "));
                resourceCount.add(createResourceLabel(TileType.STONE));
                resourceCount.add(new JLabel("1 x "));
                resourceCount.add(createResourceLabel(TileType.WHEAT));
                resourceCount.add(new JLabel("1 x "));
                resourceCount.add(createResourceLabel(TileType.WOOL));  
            }
            else if(i==2){//2 WHEAT + 3 Iron
                resourceCount.add(new JLabel("2 x "));
                resourceCount.add(createResourceLabel(TileType.WHEAT));
                resourceCount.add(new JLabel("3 x "));
                resourceCount.add(createResourceLabel(TileType.IRON));                    
            }
            else if(i==3){                
                resourceCount.add(new JLabel("1 x "));                     
                resourceCount.add(createResourceLabel(TileType.IRON));                     
                resourceCount.add(new JLabel("1 x "));
                resourceCount.add(createResourceLabel(TileType.WOOL));
                resourceCount.add(new JLabel("1 x "));
                resourceCount.add(createResourceLabel(TileType.WHEAT));
            }
            return resourceCount;
        }
}
