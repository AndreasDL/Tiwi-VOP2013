/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import utility.Structure;

/**
 * Dit is een aangepast label dat gebruikt wordt om de items die gekocht kunnen worden weer te geven.
 * Elk ShopItem komt overeen met ofwel een dorp,stad of straat.
 * @author tom
 */
public class ShopItem extends JLabel {
    private String name;
    private boolean selected =false;
    private Structure building;
    private BufferedImage icon;
    
    /**
     * Standaard constructor
     * @param name De naam van het item.
     * @param logo Het path van de gebruikte afbeelding die getekend moet worden.
     * @param building Het type van het item (dorp/stad/straat).
     */
    public ShopItem(String name, String logo, Structure building) {
        this.name = name;
        this.building = building;
        this.setPreferredSize(new Dimension(200,50));
        try {
            
             icon = ImageIO.read(getClass().getResource(logo));
        } catch (IOException e) {
            System.out.println(e);
        }       
    }
    /**
     * Geeft het gebouwType terug dat ermee overeenkomt.
     * @return Geeft het gebouwType terug dat ermee overeenkomt.
     */
    public Structure getBuilding(){
        return building;
    }
    /**
     * Duidt aan of het geselecteerd is.
     * @return True indien geselecteerd. False indien niet geselecteerd.
     */
    public boolean isSelection() {
        return selected;
    }
    /**
     * Stel het geselecteerde item in.
     * @param selected Het item dat het oproepen van deze functie geselecteerd is.
     */
    public void setSelection(boolean selected) {
        this.selected = selected;
        repaint();
    }

    /**
     * Uittekenen van de component.
     * @param g Het gebruikte Graphics object.
     */
    @Override
    protected void paintComponent(Graphics g) {

        //super.paintComponent(g);
        if(selected){
            g.drawRect(1, 1, 168, 48);
        }
        g.drawImage(icon, 5, 5, this);
        g.drawString(name, 70, 30);
    }
}
