/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import commandos.ICommand;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import model.Board;

/**
 *
 * @author Andreas De Lille
 */
public class HistoryFrame extends JFrame{
    private Board board;
    private JTextArea txt;
    
    public static void main(String[] args) {
        new HistoryFrame(null);
    }
    public HistoryFrame(Board board) {
        setResizable(false);
        setSize(new Dimension(700,700));
        setLayout(new BorderLayout());
        
        this.board = board;
        
        //buttons
        JButton btn = new JButton("close");
        btn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.RIGHT));
        p.add(btn);
        add(p,BorderLayout.SOUTH);
        
        //text
        txt = new JTextArea();
        txt.setEditable(false);
        JScrollPane sPnl = new JScrollPane(txt);
        sPnl.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sPnl.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(sPnl,BorderLayout.CENTER);
        //tekst instellen
        
        addText();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private void addText(){
        ArrayList<ICommand> cmds = board.getCommands(0);
        String t = "";
        
        for (int i = 0 ; i < cmds.size() ; i++){
            t = t + cmds.get(i).uitleg(board) + "\n";
        }
        
        txt.setText(t);
    }
    
}
