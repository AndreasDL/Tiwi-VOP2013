/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import utility.Layout;

/**
 *
 * @author Jens
 */
public class WaitPanel extends JFrame implements ActionListener{

    private JLabel lblwait;
    private Timer tmr;
    private int numberOfDots=0;
    private String TEXT = "Wachten op andere spelers";
    private String[] dots = {"",".","..","..."};
    
    public WaitPanel(){
        super("Wachten...");
        lblwait=new JLabel(TEXT);
        lblwait.setPreferredSize(Layout.WAITPNL_DIM);
        add(lblwait);
        tmr = new Timer(1000, this);        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        pack();
        tmr.start();
        
        Image img;
        try{
            img = ImageIO.read(getClass().getResource("/icon.jpg"));
        setIconImage(img);
        }catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public void actionPerformed(ActionEvent e) {        
        numberOfDots= (numberOfDots+1)%4;
        lblwait.setText(TEXT+dots[numberOfDots]);
    }
    
    
    
}
