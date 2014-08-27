/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.FilteredImageSource;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import utility.ImageRecolor;

/**
 *
 * @author Jens
 */
public class ImageRecolored extends JPanel{

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image src = null;  
        try {
            src = ImageIO.read(getClass().getResource("/settlements/settlement.png"));
        } catch (IOException ex) {
            Logger.getLogger(ImageRecolored.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Image img =createImage(new FilteredImageSource(src.getSource(), new ImageRecolor(Color.yellow)));
        g.drawImage(img, 0, 0, null);
    }
    
    
    
}
