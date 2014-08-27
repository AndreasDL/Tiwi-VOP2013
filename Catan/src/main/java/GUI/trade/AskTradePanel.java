/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.trade;

import commandos.trade.ResponseCommand;
import commandos.trade.TradePlayerCommand;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import model.Board;
import model.interfaces.IController;

/**
 *
 * @author Andreas De Lille
 */
public class AskTradePanel extends JFrame{
    private JButton btnAccept;
    private JButton btnDecline;
    private JTextArea txtOffer;
    private Timer tmr;
    private long startTime=0;
    
    public AskTradePanel(TradePlayerCommand trade,Board board,IController controller){
        super();
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        this.setSize(new Dimension(400,400));
        this.setTitle("Voorstel tot ruilen");
        this.startTime= System.currentTimeMillis();
        
        
        
        JPanel pnl = new JPanel();
        pnl.setLayout(new BorderLayout());
        
        JPanel btn = new JPanel();
        btn.setLayout(new FlowLayout());
        btnAccept = new JButton();
        btnAccept.setText("accept");
        btnAccept.addActionListener(new AcceptOor(controller,trade,this));
        btn.add(btnAccept);
        
        btnDecline = new JButton();
        btnDecline.setText("decline");
        btnDecline.addActionListener(new DeclineOor(controller,trade,this));
        btn.add(btnDecline);
        pnl.add(btn,BorderLayout.SOUTH);
        
        txtOffer = new JTextArea();
        String txt = board.getPlayer(trade.getPlayer()).getName();
            txt+= " zou graag met je ruilen,\nHij stelt het volgende voor.\n";
            txt+= "hij geeft:\n";
            txt+=trade.getTrade().getOfferString();
            txt+= "\ndaarvoor wil hij:\n";
            txt+=trade.getTrade().getRequestString();
            
            if(board.getCurrentPlayer().tradePossible(trade.getTrade().getRequest())){
                txt+="\n\nGaat u hiermee akkoord?";
                btnAccept.setEnabled(true);
            }else{
                txt+="Maar u heeft helaas niet genoeg grondstoffen.";
                btnAccept.setEnabled(false);
            }
        txtOffer.setText(txt);
        txtOffer.setEditable(false);
        pnl.add(txtOffer,BorderLayout.CENTER);
        
        tmr = new Timer(60000,new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tmr.stop();
                btnDecline.doClick();
            }
        });
        
        tmr.start();
        setLocationRelativeTo(null);
        this.setContentPane(pnl);
        setVisible(true);
    }
    
    private class AcceptOor implements ActionListener{
        private IController controller;
        private TradePlayerCommand trade;
        private JFrame frame;
        
        public AcceptOor(IController controller,TradePlayerCommand trade,JFrame frame){
            this.controller = controller;
            this.trade = trade;
            this.frame = frame;
        }
        public void actionPerformed(ActionEvent e) {
            long reactionTime = System.currentTimeMillis()-startTime;
            controller.doCommand(new ResponseCommand(controller.getOwnId(),trade, true,reactionTime,trade.isMulti()));
            tmr.stop();
            frame.dispose();
        }
    }
    private class DeclineOor implements ActionListener{
        private IController controller;
        private TradePlayerCommand trade;
        private JFrame frame;
        
        public DeclineOor(IController controller,TradePlayerCommand trade,JFrame frame){
            this.controller = controller;
            this.trade = trade;
            this.frame = frame;
        }
        public void actionPerformed(ActionEvent e) {
            long reactionTime = System.currentTimeMillis()-startTime;
            controller.doCommand(new ResponseCommand(controller.getOwnId(),trade, false,reactionTime,trade.isMulti()));
            tmr.stop();
            frame.dispose();
        }
    }

}
