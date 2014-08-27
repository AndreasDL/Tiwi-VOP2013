/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import controller.client.ClientController;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Jens
 */
public class TestPanel extends JPanel implements ActionListener{
    
    private ClientController cc;
    private JTextArea txtArea;
    private JButton btnAskServer;
    private JButton btnSendServer;

    public TestPanel(ClientController cc) {
        this.cc=cc;
        setLayout(new BorderLayout());
        txtArea = new JTextArea();
        
        JPanel pnl = new JPanel();
        btnAskServer = new JButton("Ask Server");
        btnAskServer.setActionCommand("ask");
        btnAskServer.addActionListener(this);
        
        btnSendServer= new JButton("Send Server");
        btnSendServer.setActionCommand("send");
        btnSendServer.addActionListener(this);
        
        pnl.add(btnAskServer);
        pnl.add(btnSendServer);
        
        this.add(txtArea);
        this.add(pnl,BorderLayout.SOUTH);          
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if( "ask".equals(e.getActionCommand())){
            txtArea.setText(cc.getText());
            System.out.println("ask");
        }
        else if( "send".equals(e.getActionCommand())){
            cc.putText(txtArea.getText());
            System.out.println("send");
        }
    }
    
    
    
}
