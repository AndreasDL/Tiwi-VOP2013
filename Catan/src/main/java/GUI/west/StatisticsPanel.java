/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.west;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import model.interfaces.IController;
import model.interfaces.IPlayer;
import observer.IObservable;
import observer.IObserver;
import utility.Layout;

/**
 *
 * @author Administrator
 */
public class StatisticsPanel extends JPanel implements IObserver,ActionListener{
    private JLabel[] players;
    private JLabel[] numberOfResources;
    private JLabel[] numberVictoryPoints;
    private JLabel[] numberKnights;
    String names[];
    private IController controller;
    private final int[] INIT_VALUE={2,2,0,20};
    private Color originalColor;
    //private JButton state;
    

    public StatisticsPanel(IController controller) {
        setOpaque(false);
        this.controller = controller;
       // this.setPreferredSize(new Dimension(150, 200));
        ArrayList<IPlayer> allPlayers = controller.getPlayers();
        String currentPlayer = controller.getCurrentServerPlayerName();
        int[] playervalues = controller.getPlayerValues();
        setLayout(new GridLayout(0,4));
       // setBackground(Color.white);
        TitledBorder titleBorder = BorderFactory.createTitledBorder("Informatie");
        titleBorder.setTitleColor(Layout.STATPNL_TITLE_FOREGR);
        setBorder(titleBorder);
        JLabel tmp;
        
        tmp = new JLabel("");
        tmp.setForeground(Layout.STATPNL_HEADER_FOREGR);
       // tmp.setOpaque(true);
       // tmp.setBackground(Color.white);
        //add(tmp);
        
        tmp = new JLabel("Spelers");
        tmp.setForeground(Layout.STATPNL_HEADER_FOREGR);
       // tmp.setOpaque(true);
       // tmp.setBackground(Color.white);
        add(tmp);
        tmp = new JLabel("#kaarten");
        tmp.setForeground(Layout.STATPNL_HEADER_FOREGR);
        tmp.setHorizontalAlignment(JLabel.CENTER);
       // tmp.setOpaque(true);
       // tmp.setBackground(Color.white);
        add(tmp);
        
        tmp = new JLabel("#punten");
        tmp.setForeground(Layout.STATPNL_HEADER_FOREGR);
        tmp.setHorizontalAlignment(JLabel.CENTER);
       // tmp.setOpaque(true);
       // tmp.setBackground(Color.white);
        add(tmp);
        
        tmp = new JLabel("#ridders");
        tmp.setForeground(Layout.STATPNL_HEADER_FOREGR);
        tmp.setHorizontalAlignment(JLabel.CENTER);
       // tmp.setOpaque(true);
       // tmp.setBackground(Color.white);
        add(tmp);
        
        
        players = new JLabel[allPlayers.size()];
        numberOfResources = new JLabel[allPlayers.size()];
        
        numberVictoryPoints= new JLabel[allPlayers.size()];
        numberKnights= new JLabel[allPlayers.size()];
        names=new String[allPlayers.size()];
        
        for(int i=0; i<allPlayers.size();i++){
            IPlayer player = allPlayers.get(i);
            
//            tmp = new JLabel("");
//            tmp.setForeground(Layout.STATPNL_VALUE_FOREGR);
//            tmp.setHorizontalAlignment(JLabel.CENTER);
//            knights[i]=tmp;
            //add(tmp);
            
            
            tmp = new JLabel(player.getName());
            
            tmp.setForeground(player.getColor());
       //     tmp.setOpaque(true);
      //      tmp.setBackground(Color.white);
            if(currentPlayer.equals(player.getName())){
                tmp.setBorder(Layout.STATPNL_CURRENT_USER);
            }
            players[i]=tmp;
            names[i]=tmp.getText();
            add(tmp);
            
            
            
            tmp = new JLabel(Integer.toString(playervalues[i]));
            tmp.setForeground(Layout.STATPNL_VALUE_FOREGR);
            tmp.setHorizontalAlignment(JLabel.CENTER);
       //     tmp.setOpaque(true);
       //     tmp.setBackground(Color.white);
            numberOfResources[i]=tmp;
            add(tmp);
            
            tmp = new JLabel(Integer.toString(playervalues[i]));
            tmp.setForeground(Layout.STATPNL_VALUE_FOREGR);
            tmp.setHorizontalAlignment(JLabel.CENTER);
            numberVictoryPoints[i]=tmp;
            add(tmp);
            
            tmp = new JLabel(Integer.toString(0));
            tmp.setToolTipText("");
            //tmp = new JLabel(Integer.toString(playervalues[i]));
            tmp.setForeground(Layout.STATPNL_VALUE_FOREGR);
            tmp.setHorizontalAlignment(JLabel.CENTER);
            numberKnights[i]=tmp;
            originalColor=tmp.getBackground();
            add(tmp);
            
            
            
        }
//        state= new JButton(/*controller.getState().toString()*/);
//        state.addActionListener(this);
//               
//       // state.setForeground(Color.white);
//        add(state);
        
        //tmp = new JLabel("Eigen staat:");
        //add(tmp);
       // lblCurrentState= new JLabel(controller.getState().toString());
        //add(lblCurrentState);
        setPreferredSize(new Dimension(Layout.EASTPNL_DIM.width,Layout.STATPNL_ITEM_HEIGHT*(allPlayers.size()+1)));
        
        
    }

    public void update(IObservable o, String arg) {
        String currentPlayer = controller.getCurrentServerPlayerName();
        int[] playervalues = controller.getPlayerValues();
        for(int i=0; i <players.length;i++){
            if(currentPlayer.equals(players[i].getText())){
                players[i].setBorder(Layout.STATPNL_CURRENT_USER);
            }
            else {
                players[i].setBorder(null);
            }
            numberOfResources[i].setText(Integer.toString(playervalues[i]));
            numberVictoryPoints[i].setText(Integer.toString(playervalues[i+numberOfResources.length]));
            
            numberKnights[i].setText(Integer.toString(playervalues[i+2*numberOfResources.length]));
            
            players[i].setText(names[i]);
            players[i].setToolTipText("");
//            players[i].setBackground(originalColor);
//            numberOfResources[i].setBackground(originalColor);
//            numberVictoryPoints[i].setBackground(originalColor);
//            numberKnights[i].setBackground(originalColor);
            
        }
            if(playervalues[playervalues.length-1]!=-1) {
                //numberKnights[playervalues[playervalues.length-1]].setToolTipText("Grootste Riddermacht");
                //numberOfResources[playervalues[playervalues.length-1]].setToolTipText("Grootste Riddermacht");
                players[playervalues[playervalues.length-1]].setToolTipText("Grootste Riddermacht");
                players[playervalues[playervalues.length-1]].setText(names[playervalues[playervalues.length-1]]+"(R)");
//                numberKnights[playervalues[playervalues.length-1]].setBackground(new Color(156, 93, 82));
//                numberOfResources[playervalues[playervalues.length-1]].setBackground(new Color(156, 93, 82));
//                players[playervalues[playervalues.length-1]].setBackground(new Color(156, 93, 82));
//                numberVictoryPoints[playervalues[playervalues.length-1]].setBackground(new Color(156, 93, 82));
                //knights[playervalues[playervalues.length-1]].setText("K");//.setIcon(new ImageIcon("/settlements/road.png"));
            }
        
//        if(controller.getState()!=null)
//            state.setText(controller.getState().toString());
//        //lblCurrentState.setText(controller.getState().toString());
//    }
//
//    public void actionPerformed(ActionEvent e) {
//         if(controller.getState()!=null)
//            state.setText(controller.getState().toString());
//    }
    
   
    
   
   
    
    } 

    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
