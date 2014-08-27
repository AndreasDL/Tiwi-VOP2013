/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.east;

import GUI.trade.TradeFrame;
import GUI.west.StatisticsPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import model.interfaces.IController;
import model.interfaces.IPlayer;
import observer.IObservable;
import observer.IObserver;
import utility.Layout;

/**
 *Groepeert een {@link ActionPanel} en een {@link StructurePanel}.
 * @author Jens
 */
public class EastPanel extends JPanel implements ActionListener,IObserver{

    private StructurePanel structPanel;
    private IController controller;
    private DiceButton dices;
    private JButton btnEndTurn;
    private JButton btnTrade;
    /**
     * Maakt een actiepaneel en structurepaneel aan en zet ze samen in een <code>BoxLayout</code>.
     * @param controller de huidige controller
     */
    public EastPanel(IController controller) {
        setOpaque(false);
        this.setPreferredSize(Layout.EASTPNL_DIM);
        this.controller=controller;        
        this.structPanel = new StructurePanel(controller);
        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(structPanel);
        dices= new DiceButton(controller);        
        add(dices);
        
        btnEndTurn = new JButton("Beëindig beurt");
        btnEndTurn.setEnabled(false);
        btnEndTurn.addActionListener(this);
        add(btnEndTurn);
        
        btnTrade = new JButton("Ruilen");
        btnTrade.setEnabled(false);
        btnTrade.addActionListener(new OpenFrameActionListener(controller));
        add(btnTrade);
        
        controller.addObserver(this);
        StatisticsPanel statPanel = new StatisticsPanel(controller);
        controller.addObserver(statPanel);
        add(statPanel);
        dices.setVisible(false);
        btnEndTurn.setVisible(false);
        btnTrade.setVisible(false);//verbergen
        
        
    }
    /**
     * Voegt het StructurePanel toe als Observer aan Player.
     * @param player huidige Speler
     */
    public void addObserver(IPlayer player){ 
        player.addObserver(structPanel);   
    }
    public void actionPerformed(ActionEvent e) {
        controller.goNextPlayer();
        btnEndTurn.setEnabled(false);
        btnTrade.setEnabled(false);
    }
    public void update(IObservable o, String arg) {
        if(arg.equals("dice")){
            dices.setEnabled(true);
             dices.showText(0,0);
            //btnEndTurn.setEnabled(true);
        }else if(arg.equals("btn")) {
            btnEndTurn.setEnabled(true);
            btnTrade.setEnabled(true);
            //dices.setText("dobbel");
        }else if(arg.equals("disablebtn")){
            btnEndTurn.setEnabled(false);
        }else if("trade".equals(arg)) {
        }

    }
    public void disableDices(){
        dices.setEnabled(false);
    }
    public void goNextFase() {
        dices.setVisible(true);
        btnEndTurn.setVisible(true);
        btnTrade.setVisible(true);
        structPanel.goNextFase();
    }

    /**
     * ActionListener van Trade knop
     */
    private class OpenFrameActionListener implements ActionListener{
        private IController controller;
        private TradeFrame tradeFrame;

        public OpenFrameActionListener(IController controller){
            this.controller = controller;
        }
        
        public void actionPerformed(ActionEvent e) {
            if (tradeFrame == null){
                tradeFrame = new TradeFrame(controller);
            }else{
                tradeFrame.reset();
                tradeFrame.setVisible(true);
            }
        }
        
    
    }
}
