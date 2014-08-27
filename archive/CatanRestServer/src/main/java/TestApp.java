
import controller.client.ClientController;
import gui.TestPanel;
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
public class TestApp {
    public static void main(String[] args) {
       TestApp d = new TestApp();
    }
    
    public TestApp(){
        JFrame window = new JFrame("Kolonisten van Catan");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        ClientController cc = new ClientController();
        
        
        
        window.setLayout(new BorderLayout());
        window.setContentPane(new TestPanel(cc));
        window.setMinimumSize(new Dimension(800,650));
        
      
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        //window.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
    
}
