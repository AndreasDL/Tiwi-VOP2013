/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Bank;
import utility.Layout;
import utility.TileType;

/**
 * Dit paneel houdt de overige hoeveelheden grondstoffen bij. 
 * Maakt gebruik van Observer pattern, krijgt updates als er waarden veranderd zijn.
 * Maakt eveneens gebruik van een innerclass om waarden mooi te groeperen.
 * @author Jens
 */
public class BankPanel extends JPanel implements Observer{
    private BankInfoPanelReVision infoPanel;    

    /**
     * Plaatst een JLabel bovenaan, aangevuld met een horizontaal rooster waarin het aantal grondstoffen in de bank staan.
     * @param bank om {@link BankPanel} toe te voegen als Observer.
     */
    public BankPanel(Bank bank) {  
        bank.addObserver(this);
        this.infoPanel = new BankInfoPanelReVision(bank);//new BankInfoPanel(bank);
        setLayout(new BorderLayout());
        JLabel tmp = new JLabel("Bank");
        tmp.setFont(Layout.FONT);        
        tmp.setHorizontalAlignment(JLabel.CENTER);
        add(tmp,BorderLayout.NORTH);      
        add(infoPanel,BorderLayout.CENTER);
      //  setBorder(BORDER);
    }
    
    
    /**
     * Wordt gebruikt als Bank aangepast is.
     * Gebruikt <code>valuesChanged</code> om de waarden op het infopaneel te wijzigen.
     * @param o n.v.t
     * @param arg n.v.t
     */
    public void update(Observable o, Object arg) {        
        infoPanel.valuesChanged();
    }

    
    private class BankInfoPanel extends JPanel{
      
        private ResourceBundle constants = ResourceBundle.getBundle("utility.config");    
        private JLabel[] resourceValues = new JLabel[TileType.NUMBER_OF_RESOURCES];
        private Bank bank;

        /**
         * Maakt een horizontaal rooster van grondstoffen met hun aantal eronder.
         * @param bank wordt gebruikt om de juiste startwaarden in te geven.
         */
        public BankInfoPanel(Bank bank) {
           
           this.bank=bank;        
           setLayout(new GridLayout(2,6));       
           
           JLabel tmp;
           tmp = new JLabel(constants.getString("resourceHeader"));
           tmp.setFont(Layout.FONT);
           tmp.setBorder(Layout.BORDER);
           tmp.setBackground(Layout.HEADER_COLOR);
           tmp.setOpaque(true);
           tmp.setHorizontalAlignment(JLabel.CENTER);
           add(tmp);


           TileType[] types = TileType.values();
           for( int i=0; i < TileType.NUMBER_OF_RESOURCES ; i++){
               tmp = new JLabel(constants.getString(types[i].toString()));
               //tmp.setPreferredSize(LABEL_DIM);
               tmp.setFont(Layout.FONT);
               tmp.setBorder(Layout.BORDER);
               tmp.setBackground(Layout.HEADER_COLOR);
               tmp.setHorizontalAlignment(JLabel.CENTER);
               tmp.setOpaque(true);
               add(tmp);
           }
           
           tmp = new JLabel(constants.getString("resourceValueHeader"));
           tmp.setFont(Layout.FONT);
           tmp.setBorder(Layout.BORDER);
           tmp.setOpaque(true);
           tmp.setBackground(Layout.HEADER_COLOR);
           tmp.setHorizontalAlignment(JLabel.CENTER);
           add(tmp);
           for( int i=0; i < TileType.NUMBER_OF_RESOURCES ; i++){
               tmp = new JLabel();
               tmp.setText(Integer.toString(bank.getResource(types[i])));
               tmp.setFont(Layout.FONT);
               tmp.setBorder(Layout.BORDER);
               tmp.setOpaque(true);
               tmp.setBackground(Layout.ITEM_COLOR);
               tmp.setHorizontalAlignment(JLabel.CENTER);
               resourceValues[i]=tmp;
               add(tmp);
           }

        }

        /**
         * De waarden van de grondstoffen worden aangepast als er een update was in de {@link Bank}.
         * @see Bank
         */
        public void valuesChanged(){        
            TileType[] resources = TileType.values();           
            for(int i=0;i<TileType.NUMBER_OF_RESOURCES;i++){
                resourceValues[i].setText(Integer.toString(bank.getResource(resources[i])));
            }
        }
    }
    private class BankInfoPanelReVision extends JPanel{
      
        //private ResourceBundle constants = ResourceBundle.getBundle("utility.config");    
        private JLabel[] resourceValues = new JLabel[TileType.NUMBER_OF_RESOURCES];
        private Bank bank;

        /**
         * Maakt een horizontaal rooster van grondstoffen met hun aantal eronder.
         * @param bank wordt gebruikt om de juiste startwaarden in te geven.
         */
        public BankInfoPanelReVision(Bank bank) {
          
            
           this.bank=bank;        
           setLayout(new GridLayout(1,12));       
           JLabel tmp;
           TileType[] types = TileType.values();
           
           //make labels
           for( int i=0; i < TileType.NUMBER_OF_RESOURCES ; i++){
               //type
               tmp = new JLabel(new ImageIcon(getClass().getResource("/fullCards/" + types[i].toString() + ".jpg")));//new JLabel(constants.getString(types[i].toString()));
               //tmp.setPreferredSize(LABEL_DIM);
               tmp.setText(Integer.toString(bank.getResource(types[i])));
               tmp.setFont(Layout.FONT);
               tmp.setBorder(Layout.BORDER);
               tmp.setBackground(Layout.HEADER_COLOR);
               tmp.setHorizontalAlignment(JLabel.CENTER);
               tmp.setOpaque(true);
               resourceValues[i]=tmp;
               add(tmp);
               
//               //values
//               tmp = new JLabel();
//               tmp.setText(Integer.toString(bank.getResource(types[i])));
//               tmp.setFont(Layout.FONT);
//               tmp.setBorder(Layout.BORDER);
//               tmp.setOpaque(true);
//               tmp.setBackground(Layout.ITEM_COLOR);
//               tmp.setHorizontalAlignment(JLabel.CENTER);
//               resourceValues[i]=tmp;
//               add(tmp);
           }
           

        }

        /**
         * De waarden van de grondstoffen worden aangepast als er een update was in de {@link Bank}.
         * @see Bank
         */
        public void valuesChanged(){        
            TileType[] resources = TileType.values();           
            for(int i=0;i<TileType.NUMBER_OF_RESOURCES;i++){
                resourceValues[i].setText(Integer.toString(bank.getResource(resources[i])));
            }
            
        }

        
    
    
    
}
}
