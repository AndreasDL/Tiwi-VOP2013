/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package demos;

import GUI.StructurePanel;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author Jens
 */
public class StructurePanelDemo {
     public static void main(String[] args) {
         StructurePanelDemo d = new StructurePanelDemo();
    }
     
     public StructurePanelDemo(){
        JFrame window = new JFrame("TestApp");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(new StructurePanel());
        window.pack();
        
        Image img;
        try{
            img = ImageIO.read(new File(getClass().getResource("/icon.jpg").getFile()));
        window.setIconImage(img);
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        window.setLocationRelativeTo(null);
        window.setVisible(true);
     }
}
