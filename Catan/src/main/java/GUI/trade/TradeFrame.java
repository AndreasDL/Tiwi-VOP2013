/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.trade;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import model.TradeOffer;
import model.interfaces.IController;
import utility.TileType;

/**
 *
 * @author Andreas De Lille
 */
public class TradeFrame extends JFrame {
    private IController controller;
    private ResourceBundle constants = ResourceBundle.getBundle("utility.config");
    private ResourceBundle lang = ResourceBundle.getBundle("utility.language");  
    
    //gui stuff
    private JTabbedPane tabs;
    private BankTradePanel bPnl;
    private PlayerTradePanel pPnl;
    private JButton btnTrade;
    private JButton btnCancel;
    
    public TradeFrame(IController controller){
        super();
        
        this.controller = controller;
        
        this.setSize(new Dimension(600,400));
        //this.setResizable(false);
        this.setTitle(lang.getString("tradeTitle"));
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //scherm hiden zodat het sneller heropent => probs met updaten van spinners
        this.setLayout(new BorderLayout());
        
        tabs = new JTabbedPane();
        
        bPnl = new BankTradePanel();
        tabs.addTab(lang.getString("bankTab"),bPnl);
        pPnl = new PlayerTradePanel();
        tabs.addTab(lang.getString("playerTab"),pPnl);
        this.add(tabs,BorderLayout.CENTER);
        
        //buttons
        JPanel btnPnl = new JPanel();
        btnPnl.setLayout(new FlowLayout());
        btnTrade = new JButton();
        btnTrade.setText(lang.getString("tradeBtn"));
        btnTrade.setEnabled(false);
        
        btnTrade.addActionListener(new TradeActionListener(controller));
        btnPnl.add(btnTrade);
        
        btnCancel = new JButton();
        btnCancel.setText(lang.getString("cancelBtn"));
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        btnPnl.add(btnCancel);
        add(btnPnl,BorderLayout.SOUTH);

                tabs.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (tabs.getSelectedIndex() == 1){
                    btnTrade.setEnabled(true);
                }else{
                    btnTrade.setEnabled(false);
                }
            }
        });
        setLocationRelativeTo(null);
        this.setVisible(true);
    }
    public void reset(){
        bPnl.reset();
        pPnl.reset();
    }
    
    //private classes
    private class BankTradePanel extends JPanel {
        private JLabel lblExplanation;
        private JLabel lblBalanced;
        
        private ArrayList<JSpinner> giveSpinners;
        private ArrayList<JSpinner> takeSpinners;
        
        public BankTradePanel(){
            super();
            setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            setLayout(new BorderLayout(10,10));
            
            //feedback
            JPanel p = new JPanel();
            p.setLayout(new BorderLayout());
            lblExplanation = new JLabel(lang.getString("tradeBankExplanation"));
            p.add(lblExplanation,BorderLayout.NORTH);
            lblBalanced = new JLabel();
            lblBalanced.setOpaque(true);
            p.add(lblBalanced,BorderLayout.SOUTH);
            add(p,BorderLayout.NORTH);
            
            //input forms
            JPanel cPnl = new JPanel();//center panel met gridlayout
            cPnl.setLayout(new GridLayout(0,4,20,10));
            
            //header
            JLabel lbl = new JLabel();
            lbl.setText(lang.getString("tradeGive"));
            cPnl.add(lbl);
            lbl = new JLabel();
            lbl.setText(lang.getString("tradeAmount"));
            cPnl.add(lbl);
            lbl = new JLabel();
            lbl.setText(lang.getString("tradeWant"));
            cPnl.add(lbl);
            lbl = new JLabel();
            lbl.setText(lang.getString("tradeAmount"));
            cPnl.add(lbl);
            
            //resources
            TileType[] types = TileType.values();
            giveSpinners = new ArrayList<JSpinner>();
            takeSpinners = new ArrayList<JSpinner>();
            
            JSpinner spin;
            int[] bankResCount = controller.getBank().getResourceValues();
            int[] playerResCount = controller.getBoard().getCurrentPlayer().getResourceValues(); //de current speler is de enige die kan ruilen
            
            for (int i = 0 ; i < TileType.NUMBER_OF_RESOURCES; i++){
                //i give
                lbl = new JLabel(constants.getString(types[i].toString()));
                cPnl.add(lbl);
                
                spin = new JSpinner(new SpinnerNumberModel( 0,0,playerResCount[i],4));
                spin.addChangeListener(new ChangeListener() {

                    public void stateChanged(ChangeEvent e) {
                        if (isBalanced()){
                            btnTrade.setEnabled(true);
                            lblBalanced.setBackground(Color.green);
                            lblBalanced.setText(lang.getString("tradeBalanced"));
                        }else{
                            btnTrade.setEnabled(false);
                            lblBalanced.setBackground(Color.red);
                            lblBalanced.setText("tradeUnbalanced");
                        }
                    }
                });
                giveSpinners.add(spin);
                cPnl.add(spin);
                
                //i want in return
                lbl = new JLabel(constants.getString(types[i].toString()));
                cPnl.add(lbl);
                
                spin = new JSpinner(new SpinnerNumberModel(0,0,bankResCount[i],1));
                spin.addChangeListener(new ChangeListener() {

                    public void stateChanged(ChangeEvent e) {
                        if (isBalanced()){
                            btnTrade.setEnabled(true);
                            lblBalanced.setBackground(Color.green);
                            lblBalanced.setText(lang.getString("tradeBalanced"));
                        }else{
                            btnTrade.setEnabled(false);
                            lblBalanced.setBackground(Color.red);
                            lblBalanced.setText("tradeUnbalanced");
                        }
                    }
                });
                takeSpinners.add(spin);
                cPnl.add(spin);
            }
            
            add(cPnl,BorderLayout.CENTER);
        }
        private boolean isBalanced(){
            //4 van 1 soort in == 1 uit
            //aantal (per 4 give)
            int aantGive = 0;
            for (int i = 0 ; i < giveSpinners.size(); i++){
                aantGive += (Integer) giveSpinners.get(i).getValue() / 4;
            }
            
            //aantal take
            int aantTake = 0;
            for (int i = 0 ; i < takeSpinners.size();i++){
                aantTake += (Integer) takeSpinners.get(i).getValue();
            }
            
            return aantGive == aantTake;
        }
        public void reset(){
            int[] bankResCount = controller.getBank().getResourceValues();
            int[] playerResCount = controller.getBoard().getCurrentPlayer().getResourceValues();
            
            for (int i = 0 ; i < giveSpinners.size(); i++){
                giveSpinners.get(i).setModel(new SpinnerNumberModel(0,0,playerResCount[i],4));
            }
            
            for (int i = 0 ; i < takeSpinners.size(); i++){                
                takeSpinners.get(i).setModel(new SpinnerNumberModel(0,0,bankResCount[i],1));
            }
        }
        public TradeOffer getTradeOffer(){
            
            if (isBalanced()){    
                TradeOffer tradeoffer = new TradeOffer();
                
                
                //give
                for (int i = 0 ; i < giveSpinners.size(); i++){
                    int value=(Integer) giveSpinners.get(i).getValue();
                    if(value != 0){
                        tradeoffer.addOfferResource(i, value);
                    }
                    
                }
                
                //take
                for (int i = 0; i < takeSpinners.size() ; i++){
                    int value=(Integer) takeSpinners.get(i).getValue();
                    if(value != 0){                    
                        tradeoffer.addRequestResource(i, value);
                    }
                    
                }
                
                return tradeoffer;
            }else{
                return null;
            }
        }
        
    }
    private class PlayerTradePanel extends JPanel {
        private JLabel lblExplanation;
        
        private ArrayList<JSpinner> giveSpinners;
        private ArrayList<JSpinner> takeSpinners;
        
        private JComboBox combo;
        
        public PlayerTradePanel(){
            super();
            setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            setLayout(new BorderLayout(10,10));
            
            //feedback
            JPanel p = new JPanel();
            p.setLayout(new BorderLayout());
            lblExplanation = new JLabel(lang.getString("tradePlayerExplanation"));
            p.add(lblExplanation,BorderLayout.NORTH);
            combo = new JComboBox(controller.getOtherPlayerNames(controller.getOwnId()));
            combo.addItem("Iedereen");
            //combo.removeItem(controller.getOwnName());
            combo.setEditable(false);
            p.add(combo,BorderLayout.SOUTH);
            add(p,BorderLayout.NORTH);
            
            //input forms
            JPanel cPnl = new JPanel();//center panel met gridlayout
            cPnl.setLayout(new GridLayout(0,4,20,10));
            
            //header
            JLabel lbl = new JLabel();
            lbl.setText(lang.getString("tradeGive"));
            cPnl.add(lbl);
            lbl = new JLabel();
            lbl.setText(lang.getString("tradeAmount"));
            cPnl.add(lbl);
            lbl = new JLabel();
            lbl.setText(lang.getString("tradeWant"));
            cPnl.add(lbl);
            lbl = new JLabel();
            lbl.setText(lang.getString("tradeAmount"));
            cPnl.add(lbl);
            
            //resources
            TileType[] types = TileType.values();
            giveSpinners = new ArrayList<JSpinner>();
            takeSpinners = new ArrayList<JSpinner>();
            
            JSpinner spin;
            int[] playerResCount = controller.getBoard().getCurrentPlayer().getResourceValues(); //de current speler is de enige die kan ruilen
            
            for (int i = 0 ; i < TileType.NUMBER_OF_RESOURCES; i++){
                //i give
                lbl = new JLabel(constants.getString(types[i].toString()));
                cPnl.add(lbl);
                
                spin = new JSpinner(new SpinnerNumberModel(0,0,playerResCount[i],1));
                giveSpinners.add(spin);
                cPnl.add(spin);
                
                //i want in return
                lbl = new JLabel(constants.getString(types[i].toString()));
                cPnl.add(lbl);
                
                spin = new JSpinner(new SpinnerNumberModel(0,0,19,1));
                takeSpinners.add(spin);
                cPnl.add(spin);
            }
            
            add(cPnl,BorderLayout.CENTER);
        }
        public void reset(){
            int[] bankResCount = controller.getBank().getResourceValues();
            int[] playerResCount = controller.getBoard().getCurrentPlayer().getResourceValues();
            
            for (int i = 0 ; i < giveSpinners.size(); i++){
                giveSpinners.get(i).setModel(new SpinnerNumberModel(0,0,playerResCount[i],1));
            }
            
            for (int i = 0 ; i < takeSpinners.size(); i++){                
                takeSpinners.get(i).setModel(new SpinnerNumberModel(0,0,19,1));//terug op nul zetten gaat ook
            }
        }
        public TradeOffer getTradeOffer(){                    
                TradeOffer tradeoffer = new TradeOffer();                
                
                //give
                for (int i = 0 ; i < giveSpinners.size(); i++){
                    int value=(Integer) giveSpinners.get(i).getValue();
                    if(value != 0){
                        tradeoffer.addOfferResource(i, value);
                    }
                    
                }
                
                //take
                for (int i = 0; i < takeSpinners.size() ; i++){
                    int value=(Integer) takeSpinners.get(i).getValue();
                    if(value != 0){                    
                        tradeoffer.addRequestResource(i, value);
                    }
                    
                }                
                return tradeoffer;
            
        }
        public int getUser(){
            int selectedUser=combo.getSelectedIndex();
            int[] otherPlayerIds = controller.getOtherPlayerIds(controller.getOwnId());
            if(selectedUser==otherPlayerIds.length){
                return 0;
            }
            else{
                return otherPlayerIds[selectedUser];
            }
        }
    }
    
    private class TradeActionListener implements ActionListener{
        private IController controller;
        
        public TradeActionListener(IController controller){
            this.controller=controller;
        }
        public void actionPerformed(ActionEvent e) {
            if (tabs.getSelectedIndex() == 0){
                controller.tradeWithBank(controller.getOwnId(),bPnl.getTradeOffer());
            }else if (tabs.getSelectedIndex() == 1){
                //controller.tradeWithPlayer(controller.getCurrentServerPlayerName(),pPnl.getUser(),pPnl.getTradeOffer());
                System.out.println(pPnl.getUser());
                controller.tradeWithPlayer(controller.getOwnId(),pPnl.getUser(),pPnl.getTradeOffer());
            }
            
            setVisible(false);
        }
    }
}
