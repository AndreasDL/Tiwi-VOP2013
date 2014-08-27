/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package demos;

import GUI.ImageRecolored;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author Jens
 */
public class ImageTester {
    
       public static void main(String[] args) {
       ImageTester d = new ImageTester();
       Color c=Color.red;
           System.out.println(c.getRGB());
           int red = c.getRGB();
           c=Color.blue;
           System.out.println(c.getRGB());
           Color newColor = new Color(red);
    }
    
    public ImageTester(){
        JFrame window = new JFrame("Kolonisten van Catan");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        
        window.setMinimumSize(new Dimension(800,650));
        
       
        window.add(new ImageRecolored());
        //window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        //window.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
}
