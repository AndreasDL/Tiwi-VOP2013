
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import utility.Layout;
import utility.Structure;

/**
 *Toont het aantal Structures die de Speler nog kan zetten.
 * Maakt hiervoor gebruik van Observer pattern.
 * @author Jens
 */


public class StructurePanel extends JPanel implements Observer{
    
    private ResourceBundle constants = ResourceBundle.getBundle("utility.config"); 
    private JLabel[] structureValues = new JLabel[Structure.values().length];
    private StructureInfoPanel infoPanel;
    private final int[] INIT_VALUE={13,3,4};

    /**
     * Maakt een verticaal rooster aan die toont hoeveel constructies de Speler nog kan doen.
     */
    public StructurePanel() {
        infoPanel = new StructureInfoPanel();
        setLayout(new BorderLayout());
        //JLabel tmp = new JLabel("Constructies over");
        //tmp.setFont(Layout.FONT);        
        //tmp.setHorizontalAlignment(JLabel.CENTER);
        //add(tmp,BorderLayout.NORTH);      
        add(infoPanel,BorderLayout.CENTER);
        setPreferredSize(new Dimension(230,300));
        
    }

    /**
     * Wordt opgeroepen als het aantal constructies aangepast werd in Player.
     * Zorgt ervoor dat het rooster met gegevens aangepast wordt.
     * @param o n.v.t
     * @param arg Object int[]{aantal wegen over, aantal dorpen over, aantal steden over}
     */
    public void update(Observable o, Object arg) {
        if(arg != null){
            infoPanel.valuesChanged((int[]) arg);
        }
    }
   
    /**
     * Verticaal rooster met constructiegegevens.
     */
    private class StructureInfoPanel extends JPanel{
        
        public StructureInfoPanel() {       
           setLayout(new GridLayout(3, 1));
           JLabel tmp;
           Structure[] types = Structure.values();
           setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.BLACK));
           for( int i=0; i < types.length ; i++){
               JPanel structurePanel = new JPanel(new BorderLayout());
               JPanel infoPanel = new JPanel(new GridLayout(1, 2));
               tmp = new JLabel(new ImageIcon(getClass().getResource("/settlements/" + types[i].name().toLowerCase() + ".png")));
               //tmp.setPreferredSize(LABEL_DIM);
               tmp.setFont(Layout.FONT);
               tmp.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
               tmp.setBackground(Layout.HEADER_COLOR);
               tmp.setHorizontalAlignment(JLabel.CENTER);
               tmp.setOpaque(true);
               infoPanel.add(tmp,BorderLayout.CENTER);           

               tmp = new JLabel();
               tmp.setText(Integer.toString(INIT_VALUE[i]));
               tmp.setFont(Layout.FONT);
               tmp.setOpaque(true);
               tmp.setBackground(Layout.ITEM_COLOR);
               tmp.setHorizontalAlignment(JLabel.CENTER);
               structureValues[i]=tmp;
               infoPanel.add(tmp,BorderLayout.EAST);
               JPanel resourceCount = new JPanel();
               resourceCount.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
               if(types[i] == Structure.ROAD){//1 WOOD + 1 STONE
                   resourceCount.add(new JLabel("1 x "));
                    try {
                        resourceCount.add(new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("/fullCards/WOOD.jpg")).getScaledInstance(20, 33, Image.SCALE_DEFAULT))));
                    } catch (IOException ex) {
                        Logger.getLogger(StructurePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   resourceCount.add(new JLabel("1 x "));
                   try {
                        resourceCount.add(new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("/fullCards/STONE.jpg")).getScaledInstance(20, 33, Image.SCALE_DEFAULT))));
                    } catch (IOException ex) {
                        Logger.getLogger(StructurePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
               }
               else if(types[i] == Structure.SETTLEMENT){//1 WOOD + 1 STONE + 1 WHEAT + 1 WHOOL
                   resourceCount.add(new JLabel("1 x "));
                    try {
                        resourceCount.add(new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("/fullCards/WOOD.jpg")).getScaledInstance(20, 33, Image.SCALE_DEFAULT))));
                    } catch (IOException ex) {
                        Logger.getLogger(StructurePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   resourceCount.add(new JLabel("1 x "));
                   try {
                        resourceCount.add(new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("/fullCards/STONE.jpg")).getScaledInstance(20, 33, Image.SCALE_DEFAULT))));
                    } catch (IOException ex) {
                        Logger.getLogger(StructurePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   resourceCount.add(new JLabel("1 x "));
                    try {
                        resourceCount.add(new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("/fullCards/WHEAT.jpg")).getScaledInstance(20, 33, Image.SCALE_DEFAULT))));
                    } catch (IOException ex) {
                        Logger.getLogger(StructurePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   resourceCount.add(new JLabel("1 x "));
                   try {
                        resourceCount.add(new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("/fullCards/WOOL.jpg")).getScaledInstance(20, 33, Image.SCALE_DEFAULT))));
                    } catch (IOException ex) {
                        Logger.getLogger(StructurePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
               }
               else if(types[i] == Structure.CITY){//2 WHEAT + 3 Iron
                   resourceCount.add(new JLabel("2 x "));
                    try {
                        resourceCount.add(new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("/fullCards/WHEAT.jpg")).getScaledInstance(20, 33, Image.SCALE_DEFAULT))));
                    } catch (IOException ex) {
                        Logger.getLogger(StructurePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   resourceCount.add(new JLabel("3 x "));
                   try {
                        resourceCount.add(new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("/fullCards/IRON.jpg")).getScaledInstance(20, 33, Image.SCALE_DEFAULT))));
                    } catch (IOException ex) {
                        Logger.getLogger(StructurePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
               }
               structurePanel.add(infoPanel,BorderLayout.CENTER);
               structurePanel.add(resourceCount,BorderLayout.SOUTH);
               add(structurePanel);
           }
          

    }
        /**
         * Verandert de waarden die weergegeven worden naar de nieuwe waarden.
         * @param newValues {aantal wegen over, aantal dorpen over, aantal steden over}
         */
        public void valuesChanged(int[] newValues){
            for( int i=0;i<newValues.length;i++){
                structureValues[i].setText(Integer.toString(newValues[i])); 
            }
        }

}   
 
}


