/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Board;
import model.interfaces.IPlayer;
import observer.IObservable;
import observer.IObserver;
import utility.Layout;

/**
 *
 * @author Andreas De Lille
 */
public class EndGameFrame extends JFrame implements IObserver{
    private JPanel cPnl;
    private JPanel tPnl;
    private JFrame ouderFrame;
    private ArrayList<JLabel[]> labels;
        
    public EndGameFrame(JFrame ouderFrame){
        super();
        this.ouderFrame = ouderFrame;
        setTitle("game is over");
        setResizable(false);
        setSize(new Dimension(600,600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(false);
        
        addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                close();
            }

            public void windowClosed(WindowEvent e) {
                //close();
            }

            public void windowIconified(WindowEvent e) {
                mini();
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }
        });
    }

    public void update(IObservable o, String arg) {
        if ("endGame".equals(arg)){
            setValues((Board) o);
            this.setVisible(true);    
        }
    }
    
    public void setValues(Board b){
        tPnl = new JPanel();
        JLabel l = new JLabel("Game is Over");
        tPnl.setBackground(Color.BLACK);
        l.setBackground(Color.BLACK);
        l.setForeground(Color.WHITE);
        l.setFont(Layout.HUGEFONT);
        tPnl.add(l);
        add(tPnl,BorderLayout.NORTH);
        
        cPnl = new JPanel();
        cPnl.setLayout(null);//new GridLayout(0,3));
        cPnl.setBackground(Color.BLACK);
        
        //players opvragen
        ArrayList<IPlayer> players = new ArrayList<IPlayer>();
        int[] ids = b.getPlayerIds();
        for (int i = 0 ; i < ids.length ; i++){
            players.add(b.getPlayer(ids[i]));
        }
        
        //players sorteren op score
        IPlayer newValue;
        for (int i = 1; i < players.size(); i++) {
                newValue = players.get(i);
                int j = i;
                while (j > 0 && players.get(j - 1).getScore() > newValue.getScore()) {
                    players.set(j,players.get(j - 1));
                    j--;
                }
                players.set(j,newValue);
        }
        l = new JLabel("Plaats");
        l.setForeground(Color.WHITE);
        l.setFont(Layout.BIGFONT);
        l.setLocation(50,30);
        l.setSize(200,200);
        cPnl.add(l);
        l = new JLabel("Naam");
        l.setForeground(Color.WHITE);
        l.setFont(Layout.BIGFONT);
        l.setLocation(250,30);
        l.setSize(200,200);
        cPnl.add(l);
        l = new JLabel("Score");
        l.setForeground(Color.WHITE);
        l.setFont(Layout.BIGFONT);
        l.setLocation(450,30);
        l.setSize(200,200);
        cPnl.add(l);
        
        
        labels = new ArrayList<JLabel[]>();
        for (int i = 0 ; i < b.getMaxPlayers() ; i++){
            JLabel[] lbl = new JLabel[3];
            lbl[0] = new JLabel(i + "");
            lbl[0].setFont(Layout.BIGFONT);
            lbl[0].setForeground(Color.white);
            lbl[0].setSize(200,200);
            lbl[0].setLocation(50,80+i*50);
            cPnl.add(lbl[0]);
            
            lbl[1] = new JLabel(players.get(i).getName());
            lbl[1].setFont(Layout.BIGFONT);
            lbl[1].setForeground(Color.white);
            lbl[1].setSize(200,200);
            lbl[1].setLocation(250,80+i*50);
            cPnl.add(lbl[1]);
            
            lbl[2] = new JLabel("" + players.get(i).getScore());
            lbl[2].setFont(Layout.BIGFONT);
            lbl[2].setForeground(Color.white);
            lbl[2].setSize(200,200);
            lbl[2].setLocation(450,80+i*50);
            cPnl.add(lbl[2]);
            
            labels.add(lbl);
        }
        
        //eerste in rood
        JLabel[] lbl = labels.get(0);
        for (int i = 0 ; i < lbl.length ; i++){
            lbl[i].setForeground(Color.red);
        }
        
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btn = new JButton("end game");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        p.add(btn);
        
        btn = new JButton("view History");
        btn.addActionListener(new historyOor(b));
        p.add(btn);
        
        add(p,BorderLayout.SOUTH);
        
        cPnl.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(cPnl,BorderLayout.CENTER);
    }
    
    public void close(){
        ouderFrame.dispose();
        dispose();
        System.exit(0);
    }
    public void mini(){
        ouderFrame.setState(JFrame.ICONIFIED);
    }
    private class historyOor implements ActionListener{
        private Board b;

        public historyOor(Board b) {
            this.b = b;
        }

        public void actionPerformed(ActionEvent e) {
            new HistoryFrame(b);
        }
        
    }
}