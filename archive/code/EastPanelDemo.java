/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package demos;

import GUI.ActionPanel;
import GUI.EastPanel;
import controller.GameController;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import model.Board;
import model.SelectTileModel;

/**
 *
 * @author Jens
 */
public class EastPanelDemo {
    public static void main(String[] args) {
         EastPanelDemo d = new EastPanelDemo();
    }
     
     public EastPanelDemo(){
        JFrame window = new JFrame("TestApp");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(new EastPanel(new GameController(new Board(new SelectTileModel()))));
    //     window.setContentPane(new ActionPanel(new GameController(new Board(new SelectTileModel()))));
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
