
import controller.client.JSONClientController;
import gui.JSONTestPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jens
 */
public class JSONTestApp {
    public static void main(String[] args) {
       JSONTestApp d = new JSONTestApp();
    }
    
    public JSONTestApp(){
        JFrame window = new JFrame("Kolonisten van Catan");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JSONClientController cc = new JSONClientController();
        
        
        
        window.setLayout(new BorderLayout());
        window.setContentPane(new JSONTestPanel(cc));
        window.setMinimumSize(new Dimension(800,650));
        
      
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        //window.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
}
