/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package demos;

import GUI.SurroundPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Jens
 */
public class MakeBoardDemo {
    private static JFrame window;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MakeBoardDemo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MakeBoardDemo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MakeBoardDemo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MakeBoardDemo.class.getName()).log(Level.SEVERE, null, ex);
        }


/*
        try {
            SingleInstanceService si = (SingleInstanceService) ServiceManager.lookup("javax.jnlp.SingleInstanceService");
            si.addSingleInstanceListener(new SingleInstanceListener() {

                public void newActivation(String[] strings) {
                     JOptionPane.showMessageDialog(window, "Catan is all gestart!", "Fout", JOptionPane.ERROR_MESSAGE);
                     
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(MakeBoardDemo.class.getName()).log(Level.SEVERE, null, ex);
        }catch (NoClassDefFoundError e){//niet gestart met jnlp
            Logger.getLogger(MakeBoardDemo.class.getName()).log(Level.INFO, "niet gestart met jnlp");
        }*/

        int playerId;
        int gameId;
        String gamePass;
        boolean spectator=false;
        if(args.length != 0) {
            playerId=Integer.parseInt(args[0]);
            gameId=Integer.parseInt(args[1]);
            gamePass=args[2];
            if(args.length > 3){
                spectator = Boolean.parseBoolean(args[3]);
            }
        }else{

            playerId=Integer.parseInt(JOptionPane.showInputDialog("Geef uw id: "));

            gameId=3;

            gamePass="pass";

        }

        MakeBoardDemo d = new MakeBoardDemo(playerId,gameId,gamePass,spectator);
    }
    
    public MakeBoardDemo(int PlayerId,int gameId ,String gamePass, boolean spectator ){
        window = new JFrame("Kolonisten van Catan");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        window.setContentPane(new SurroundPanel(window,PlayerId,gameId,gamePass,spectator));
        window.setMinimumSize(new Dimension(800,650));
        
        Image img;
        try{
            img = ImageIO.read(getClass().getResource("/icon.jpg"));
        window.setIconImage(img);
        }catch (Exception e) {
        }
        
        //window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        //window.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
}
