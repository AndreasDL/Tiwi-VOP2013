/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.google.gson.Gson;
import controller.client.JSONClientController;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import model.Model;
import model.Player;

/**
 *
 * @author Jens
 */
public class JSONTestPanel extends JPanel implements ActionListener{
    private JSONClientController cc;
    private JTextArea txtArea;
    private JButton btnAskServer;
    private JButton btnSendServer;
    

    public JSONTestPanel(JSONClientController cc) {
        this.cc=cc;
        setLayout(new BorderLayout());
        txtArea = new JTextArea();
        
        JPanel pnl = new JPanel();
        btnAskServer = new JButton("getCurrentPlayer");
        btnAskServer.setActionCommand("player");
        btnAskServer.addActionListener(this);
        
        btnSendServer= new JButton("getModel");
        btnSendServer.setActionCommand("model");
        btnSendServer.addActionListener(this);
        
        pnl.add(btnAskServer);
        pnl.add(btnSendServer);
        
        this.add(txtArea);
        this.add(pnl,BorderLayout.SOUTH);          
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( "player".equals(e.getActionCommand())){         
            txtArea.setText(cc.getCurrentPlayer().getName());
            //System.out.println("currentplayer");
        }
        else if( "model".equals(e.getActionCommand())){ 
            Model model = cc.getModel();
            txtArea.setText( model.getText() + "   "  + model.getPlayer(1).getName() +"  "+ model.getPlayer(0).getName());
            //System.out.println("model");
        }
    }
}
