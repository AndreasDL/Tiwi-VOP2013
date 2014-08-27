/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package demos;

import GUI.PlayerPanel;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import model.Bank;
import model.Player;
import model.interfaces.IPlayer;

/**
 *
 * @author Jens
 */
public class PlayerPanelDemo {
    public static void main(String[] args) {
        PlayerPanelDemo p = new PlayerPanelDemo();
    }
    
    public PlayerPanelDemo() {
        JFrame window = new JFrame("TestApp");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        IPlayer player=new Player("Nameless", new Bank());
        window.setContentPane(new PlayerPanel());
        window.pack();
        window.setLocationRelativeTo(null);
        
        Image img;
        try{
            img = ImageIO.read(new File(getClass().getResource("/icon.jpg").getFile()));
        window.setIconImage(img);
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        window.setVisible(true);
    }
}
