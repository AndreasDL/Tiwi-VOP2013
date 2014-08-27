/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.west;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.interfaces.IPlayer;
import utility.Layout;
import utility.TileType;

/**
 * Groepeert grondstoffen met hun aantal.
 * Maakt gebruik van waarden van huidige Speler.
 * @author Jens
 */
public class ResourcePanel  extends JPanel{   
    private ResourceBundle constants = ResourceBundle.getBundle("utility.config");    
    private JLabel[] resourceValues = new JLabel[TileType.NUMBER_OF_RESOURCES];
    private HashMap<String,Image> imgs;
    //private final Dimension LABEL_DIM = new Dimension(200, 50);
    
    /**
     * Maakt een verticaal rooster aan met behulp van <code>GridLayout</code>.
     * Gebruikt constanten gedefinieerd in {@link Layout}.
     * @see Layout
     */
    public ResourcePanel() {
        setOpaque(false);
        initImgs();
       setLayout(new GridLayout(5,2));
       
       JLabel tmp;
//       tmp = new JLabel(constants.getString("resourceHeader"));
//       tmp.setFont(Layout.FONT);
//       tmp.setBorder(Layout.BORDER);
//       tmp.setBackground(Layout.HEADER_COLOR);
//       tmp.setOpaque(true);
//       tmp.setHorizontalAlignment(JLabel.CENTER);
//       //tmp.setPreferredSize(new Dimension(40,66));
//       add(tmp);
//       
//       tmp = new JLabel(constants.getString("resourceValueHeader"));
//       tmp.setFont(Layout.FONT);
//       tmp.setBorder(Layout.BORDER);
//       tmp.setOpaque(true);
//       tmp.setBackground(Layout.HEADER_COLOR);
//       tmp.setHorizontalAlignment(JLabel.CENTER);
//       //tmp.setPreferredSize(new Dimension(40,66));
//       add(tmp);       
       
       //types
       TileType[] types = TileType.values();
       for( int i=0; i < TileType.NUMBER_OF_RESOURCES ; i++){
           ImageIcon icon = new ImageIcon(getClass().getResource("/fullCards/" + types[i].toString() + ".jpg"));
           tmp = new JLabel(icon);
           
           //tmp.setPreferredSize(new Dimension(icon.getIconWidth(),icon.getIconHeight()));
           tmp.setFont(Layout.FONT);
           tmp.setBorder(Layout.BORDER);
           tmp.setBackground(Layout.ITEM_COLOR);
           tmp.setHorizontalAlignment(JLabel.CENTER);
           tmp.setOpaque(true);
           add(tmp);           
       
           tmp = new JLabel("0");
           tmp.setFont(Layout.BIGFONT);
           tmp.setBorder(Layout.BORDER);
           tmp.setOpaque(true);
           tmp.setBackground(Layout.ITEM_COLOR);
           tmp.setHorizontalAlignment(JLabel.CENTER);
           resourceValues[i]=tmp;
           //resourceValues[i].setFont(null);
           add(tmp);
       }
    }

    /**
     * Wordt opgeroepen door PlayerPanel als de grondstoffen van de Speler veranderd zijn.
     * Waarden in de Labels worden aangepast.
     * @param player huidige Speler
     */
    public void valuesChanged(IPlayer player){
        TileType[] resources = TileType.values();           
        for(int i=0;i<TileType.NUMBER_OF_RESOURCES;i++){
            resourceValues[i].setText(Integer.toString(player.getResource(resources[i])));
        }
        for(int i=0;i<TileType.NUMBER_OF_RESOURCES;i++){
            resourceValues[i].setForeground(Color.black);
            
        }
        for(TileType type:player.getChangedResources()){
            //resourceValues[type.getValue()].setBorder(BorderFactory.createLineBorder(Color.red));
            resourceValues[type.getValue()].setForeground(Color.red);
        }
    }
    /**
     * Initialiseren van de afbeeldingen
     */
    private void initImgs(){
        imgs = new HashMap<String,Image>();

        for (int i = 0 ; i < TileType.NUMBER_OF_RESOURCES ; i++){
            Image img;
            try{
                img = (ImageIO.read(getClass().getResource("/fullCards/" + TileType.values()[i] + ".jpg"))).getScaledInstance(20,35 , Image.SCALE_SMOOTH);
                imgs.put( (TileType.values()[i]).toString(), img);//con);
                
            }catch (Exception e) {
                System.out.println("fout: " + TileType.values()[i]);
            }
        }
  
    }  
}
