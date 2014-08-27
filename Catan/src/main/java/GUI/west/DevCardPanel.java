/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.west;

import GUI.bot.SouthPanel;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.interfaces.IController;
import model.interfaces.IPlayer;
import utility.Layout;

/**
 *
 * @author Jens
 */
public class DevCardPanel extends JPanel{

    //private JLabel[] cardValues;
    private JLabel numberValues;
    private IController controller;
    private ToolTipLabel ttl;
    private SouthPanel south;
    
    public DevCardPanel(IController controller,SouthPanel south) {
        this.controller=controller;
        this.south = south;
       setOpaque(false);       
       setLayout(new GridLayout(0,2));
       
       ttl = new ToolTipLabel(controller,south);
       //tmp = new JLabel("DEV");
       //tmp.setFont(Layout.FONT);
       ttl.setBorder(Layout.BORDER);
       ttl.setBackground(Layout.HEADER_COLOR);
       ttl.setOpaque(true);
       ttl.setHorizontalAlignment(JLabel.CENTER);
       //tmp.setPreferredSize(new Dimension(40,66));
       add(ttl);
       
       JLabel tmp;
       tmp = new JLabel("0");
       tmp.setFont(Layout.FONT);
       tmp.setBorder(Layout.BORDER);
       tmp.setOpaque(true);
       tmp.setBackground(Layout.HEADER_COLOR);
       tmp.setHorizontalAlignment(JLabel.CENTER);
       //tmp.setPreferredSize(new Dimension(40,66));
       add(tmp);
       numberValues=tmp;
       
       //types
       //DevCardType[] types = DevCardType.values();
       //cardValues = new JLabel[types.length];
       //for( int i=0; i < types.length ; i++){
           //ImageIcon icon = new ImageIcon(getClass().getResource("/fullCards/" + types[i].toString() + ".jpg"));
           //tmp = new JLabel("tes");//types[i].name());
           
           //tmp.setPreferredSize(new Dimension(icon.getIconWidth(),icon.getIconHeight()));
//           tmp.setFont(Layout.FONT);
//           tmp.setBorder(Layout.BORDER);
//           tmp.setBackground(Layout.ITEM_COLOR);
//           tmp.setHorizontalAlignment(JLabel.CENTER);
//           tmp.setOpaque(true);
//           add(tmp);           
//           
//           
//           tmp = new JLabel("0");
//           tmp.setFont(Layout.FONT);
//           tmp.setBorder(Layout.BORDER);
//           tmp.setOpaque(true);
//           tmp.setBackground(Layout.ITEM_COLOR);
//           tmp.setHorizontalAlignment(JLabel.CENTER);
//           tmp.setName(""+i);
//           tmp.addMouseListener(new MouseAdapter() {
//           
//               @Override
//               public void mouseClicked(MouseEvent e) {
//                   JLabel lbl=(JLabel)(e.getSource());
//                   playCard(Integer.parseInt(lbl.getName()));
//               }
//           });
//           cardValues[i]=tmp;
//           add(tmp);
//           //tmp.setVisible(false);
//    }
       }
//    private void playCard(int n) {
//       if(Integer.parseInt(cardValues[n].getText())>0) {
//            controller.playCard(n);
//        }
//    }

    public void valuesChanged(IPlayer p){
        if(p.getCards().size()!=Integer.parseInt(numberValues.getText())){
            numberValues.setText(""+p.getCards().size());
            numberValues.setForeground(Color.red);
        }
        else {
            numberValues.setForeground(Color.black);
        }
        ttl.valuesChanged(p);
        
//        int[] aantallen=new int[5];
//        for(int i=0;i<DevCardType.NUMBER_OF_RESOURCES;i++) {
//            aantallen[i]=0;
//        }
//        for(int i=0;i<p.getCards().size();i++) {
//            aantallen[p.getCards().get(i).getValue()]++;
//        }
//        for(int i=0;i<aantallen.length;i++){
//            cardValues[i].setText(""+aantallen[i]);
//            if(i==0){
//                cardValues[i].setEnabled(false);
//            }
//            else
//                cardValues[i].setEnabled(true);
//        }
    }
}
