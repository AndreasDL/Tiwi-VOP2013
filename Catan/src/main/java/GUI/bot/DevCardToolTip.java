/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.bot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.SwingConstants;
import model.interfaces.IController;
import model.interfaces.IPlayer;
import utility.DevCardType;
import utility.Layout;

/**
 *
 * @author Andreas De Lille
 */
public class DevCardToolTip extends JToolTip {
    private DevCardPnl pnl;
    private IController controller;
    
    public DevCardToolTip(IController controller) {
        super();
        setLayout(new BorderLayout());
        setOpaque(false);
        this.controller = controller;
        pnl = new DevCardPnl(controller);
        add(pnl);
    }
    public void valuesChanged(IPlayer player){
        pnl.valuesChanged(player);
    }
    
    @Override
    public void setTipText(String tipText){
    }
    @Override
    public Dimension getPreferredSize(){
        return pnl.getPreferredSize();
    }
    
    private class DevCardPnl extends JPanel{
        private HashMap<String,JLabel> imgs;
        DevCardType[] types;// = DevCardType.values();
        JLabel[] labels;
        JLabel[] aantallen;
        private IController controller;
        
        public DevCardPnl(IController controller) {
            //setBackground(Color.red);
            setLayout(new GridLayout(0,3));
            setPreferredSize(new Dimension(670,670));
            setOpaque(false);
            this.controller = controller;
            
            //inlezen
            imgs = new HashMap<String,JLabel>();
            types = DevCardType.values();
            labels = new JLabel[DevCardType.NUMBER_OF_CARDS];
            aantallen = new JLabel[DevCardType.NUMBER_OF_CARDS];
            
            for (int i = 0 ; i < DevCardType.NUMBER_OF_CARDS ; i++){
                JPanel p = new JPanel();
                p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
                JLabel lbl = new JLabel(new ImageIcon(getClass().getResource("/devCards/" + types[i] + ".jpg")));
                lbl.setName("" + i);
                lbl.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

                lbl.addMouseListener(new MouseListener() {

                    public void mouseClicked(MouseEvent e) {
                        JLabel lbl=(JLabel)(e.getSource());
                        playCard(Integer.parseInt(lbl.getName()));
                    }
                    public void mousePressed(MouseEvent e) {
                    }
                    public void mouseReleased(MouseEvent e) {
                    }
                    public void mouseEntered(MouseEvent e) {
                    }
                    public void mouseExited(MouseEvent e) {
                    }
                });
                labels[i] = lbl;
                
                imgs.put(types[i].name(),lbl);
                p.add(imgs.get(types[i].name()));
                
                lbl = new JLabel("0",SwingConstants.CENTER);
                lbl.setFont(Layout.NUMBERFONT);
                lbl.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
                aantallen[i] = lbl;
                JPanel pp = new JPanel();
                pp.setLayout(new FlowLayout(FlowLayout.CENTER));
                pp.add(aantallen[i]);
                p.add(pp);
                add(p);
            }
        }
        @Override
        public void paintComponent(Graphics g) {
            //super.paintComponent(g);
            super.paintBorder(g);
        }
        public void valuesChanged(IPlayer p){
//        if(p.getCards().size()!=Integer.parseInt(numberValues.getText())){
//            numberValues.setText(""+p.getCards().size());
//            numberValues.setForeground(Color.red);
//        }
//        else {
//            numberValues.setForeground(Color.black);
//        }
        int[] aantallenInt=new int[DevCardType.NUMBER_OF_CARDS];
        for(int i=0;i<DevCardType.NUMBER_OF_CARDS;i++) {//reset
            aantallenInt[i]=0;
        }
        for(int i=0;i<p.getCards().size();i++) {//instellen
            aantallenInt[p.getCards().get(i).getValue()]++;
        }
        //weergeven op label
        for(int i=0;i<aantallenInt.length;i++){
            aantallen[i].setText(""+aantallenInt[i]);
            
            if(aantallenInt[i]==0){
                labels[i].setEnabled(false);
            }else{
                labels[i].setEnabled(true);
            }
        }
    }
        private void playCard(int n) {
        if(Integer.parseInt(labels[n].getText())>0) {
            controller.playCard(n);
        }
        
    }
     }
}
