/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package demos;

import GUI.SurroundPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author Jens
 */
public class MakeBoardDemo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       MakeBoardDemo d = new MakeBoardDemo();
    }
    
    public MakeBoardDemo(){
        JFrame window = new JFrame("Kolonisten van Catan");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        window.setContentPane(new SurroundPanel(window));
        window.setMinimumSize(new Dimension(800,650));
        
        Image img;
        try{
            img = ImageIO.read(getClass().getResource("/icon.jpg"));
        window.setIconImage(img);
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        //window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
}
