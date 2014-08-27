/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.bot;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.Border;
import model.Bank;
import model.interfaces.IController;
import model.interfaces.IPlayer;
import observer.IObservable;
import observer.IObserver;
import utility.DevCardType;
import utility.Layout;
import utility.TileType;

/**
 * Dit paneel houdt de overige hoeveelheden grondstoffen bij. Maakt gebruik van
 * Observer pattern, krijgt updates als er waarden veranderd zijn. Maakt
 * eveneens gebruik van een innerclass om waarden mooi te groeperen.
 *
 * @author Jens
 */
public class SouthPanel extends JPanel implements IObserver, MouseListener {

    private BankInfoPanelReVision infoPanel;
    private IController controller;
    private JPanel compoundpnl;
    private DevCardPnlBot devPanel;
    private boolean isBank;
    private boolean isHidden;
    private JLabel title;

    /**
     * Plaatst een JLabel bovenaan, aangevuld met een horizontaal rooster waarin
     * het aantal grondstoffen in de bank staan.
     *
     * @param bank om {@link BankPanel} toe te voegen als Observer.
     */
    public SouthPanel(Bank bank, IController controller) {
        this.controller = controller;
        setOpaque(false);
        bank.addObserver(this);
        this.infoPanel = new BankInfoPanelReVision(bank);//new BankInfoPanel(bank);
        this.devPanel = new DevCardPnlBot(controller,this);
        //setLayout(new BorderLayout());

        compoundpnl = new JPanel();
        compoundpnl.setLayout(new BorderLayout());

        compoundpnl.setOpaque(false);
        //Border matte = BorderFactory.createMatteBorder(0, 0, 0, 0, Color.GRAY);
        //TitledBorder title = BorderFactory.createTitledBorder(matte,"Bank");
        //title.setTitleJustification(TitledBorder.CENTER);
        //title.setTitleColor(Color.white);        
        title = new JLabel("Bank", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.addMouseListener(this);

        Border blackline = BorderFactory.createMatteBorder(3, 3, 0, 3, Color.darkGray);
        // Border compound = BorderFactory.createCompoundBorder(title,matte);
        compoundpnl.setBorder(blackline);//BorderFactory.createCompoundBorder(blackline,title));
        //setLayout(new BorderLayout());
        //JLabel tmp = new JLabel("Bank");        
        //tmp.setFont(Layout.FONT);        
        //tmp.setHorizontalAlignment(JLabel.CENTER);
        //pnl.add(tmp,BorderLayout.NORTH);        

        //showhide = new JButton("Show/hide");
        //showhide.addActionListener(this);
        compoundpnl.add(title, BorderLayout.NORTH);
        compoundpnl.add(infoPanel, BorderLayout.CENTER);
        add(Box.createHorizontalGlue());
        add(compoundpnl);
        add(Box.createHorizontalGlue());
        //  setBorder(BORDER);
        
        isBank = true;
        isHidden = false;
    }

    /**
     * Wordt gebruikt als Bank aangepast is. Gebruikt
     * <code>valuesChanged</code> om de waarden op het infopaneel te wijzigen.
     *
     * @param o n.v.t
     * @param arg n.v.t
     */
    public void update(IObservable o, String arg) {
        infoPanel.valuesChanged();
    }
    public void updateBank(IPlayer p){
        devPanel.valuesChanged(p);
    }

    public void mouseClicked(MouseEvent e) {
        if (isHidden){
            isHidden = false;
            showBank();
        }else{
            isHidden = true;
            infoPanel.setVisible(false);
            devPanel.setVisible(false);
            title.setText("Bank");
        }
        this.validate();
    }

    public void mousePressed(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
    
    public void showHideDev(){
        if (isHidden || isBank){
            isBank = false;
            isHidden = false;
            compoundpnl.add(devPanel,BorderLayout.CENTER);
            devPanel.setVisible(true);
            infoPanel.setVisible(false);
            title.setText("Ontwikkelingskaarten");
        }else{
            showBank();
        }
        this.validate();
    }
    public void showBank(){
        isBank = true;
        isHidden = false;
        compoundpnl.add(infoPanel,BorderLayout.CENTER);
        devPanel.setVisible(false);
        infoPanel.setVisible(true);
        title.setText("Bank");
        this.validate();
    }

    private class BankInfoPanelReVision extends JPanel {

        //private ResourceBundle constants = ResourceBundle.getBundle("utility.config");    
        private JLabel[] resourceValues = new JLabel[TileType.NUMBER_OF_RESOURCES];
        private Bank bank;

        /**
         * Maakt een horizontaal rooster van grondstoffen met hun aantal
         * eronder.
         *
         * @param bank wordt gebruikt om de juiste startwaarden in te geven.
         */
        public BankInfoPanelReVision(Bank bank) {
            setOpaque(false);

            this.bank = bank;
            setLayout(new GridLayout(1, 10));
            JLabel tmp;
            TileType[] types = TileType.values();

            //make labels
            for (int i = 0; i < TileType.NUMBER_OF_RESOURCES; i++) {
                //type
                tmp = new JLabel(new ImageIcon(getClass().getResource("/fullCards/" + types[i].toString() + ".jpg")));//new JLabel(constants.getString(types[i].toString()));
                //tmp.setPreferredSize(LABEL_DIM);
                //tmp.setText(Integer.toString(bank.getResource(types[i])));
                tmp.setFont(Layout.FONT);
                tmp.setBorder(Layout.BORDER);
                tmp.setOpaque(false);
                tmp.setBackground(Layout.HEADER_COLOR);
                tmp.setHorizontalAlignment(JLabel.CENTER);
                tmp.setName("" + i);
                tmp.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JLabel lab = (JLabel) e.getSource();
                        klikresource(lab.getName());
                    }
                });

                //              resourceValues[i]=tmp;
                add(tmp);

                //values
                tmp = new JLabel();
                tmp.setText(Integer.toString(bank.getResource(types[i])));
                tmp.setFont(Layout.FONT);
                tmp.setBorder(Layout.BORDER);
                tmp.setOpaque(false);
                // tmp.setBackground(Layout.ITEM_COLOR);
                tmp.setForeground(Layout.BANK_LBL_FOREGR);
                tmp.setHorizontalAlignment(JLabel.CENTER);
                resourceValues[i] = tmp;
                add(tmp);
            }


        }
        private void klikresource(String name) {
            controller.klikResource(Integer.parseInt(name));
        }
        /**
         * De waarden van de grondstoffen worden aangepast als er een update was
         * in de {@link Bank}.
         *
         * @see Bank
         */
        public void valuesChanged() {
            TileType[] resources = TileType.values();
            for (int i = 0; i < TileType.NUMBER_OF_RESOURCES; i++) {
                resourceValues[i].setText(Integer.toString(bank.getResource(resources[i])));

            }
//            for(int i=0;i<TileType.NUMBER_OF_RESOURCES;i++){
//            resourceValues[i].setBorder(BorderFactory.createLineBorder(Color.black));
//            
//            }
//            for(TileType type:player.getChangedResources()){
//                resourceValues[type.getValue()].setBorder(BorderFactory.createLineBorder(Color.red));
//            }


        }
    }
    private class DevCardPnlBot extends JPanel {
        private HashMap<String, JLabel> imgs;
        DevCardType[] types;// = DevCardType.values();
        JLabel[] labels;
        JLabel[] aantallen;
        private IController controller;
        private SouthPanel south;

        public DevCardPnlBot(IController controller,SouthPanel south) {
            this.south = south;
            //setBackground(Color.red);
            setLayout(new GridLayout(0, 10));
            //setPreferredSize(new Dimension(, 100));
            setOpaque(false);
            this.controller = controller;

            //inlezen
            imgs = new HashMap<String, JLabel>();
            types = DevCardType.values();
            labels = new JLabel[DevCardType.NUMBER_OF_CARDS];
            aantallen = new JLabel[DevCardType.NUMBER_OF_CARDS];
            String[] tooltxt = {"Bij het spelen van deze kaart moet je de struikrover verzetten en van één van de getroffen spelers een grondstoffenkaart trekken.",
                "Bij het spelen van deze kaart mag je direct twee straten bouwen.",
                "1 overwinningspunt",
                "Bij het spelen van deze kaart neem je direct twee grondstoffenkaarten naar keuze van de bank.",
                "Bij het spelen van deze kaart kies je een grondstof.Alle spelers geven je van deze grondstof alle kaarten die ze bezitten."   
            };
                    

            for (int i = 0; i < DevCardType.NUMBER_OF_CARDS; i++) {
                //JPanel p = new JPanel();
                //p.setOpaque(false);
                //p.setLayout(new BorderLayout());//new BoxLayout(p, BoxLayout.Y_AXIS));
                JLabel lbl = new JLabel(new ImageIcon(getClass().getResource("/devCards/" + types[i] + ".jpg")));
                lbl.setName("" + i);
                lbl.setBorder(Layout.BORDER);//BorderFactory.createEmptyBorder(2, 2, 2, 2));

                lbl.addMouseListener(new mouseOor(south));
                lbl.setToolTipText(tooltxt[i]);
                labels[i] = lbl;

                imgs.put(types[i].name(), lbl);
                //p.
                add(imgs.get(types[i].name()));

                lbl = new JLabel("0", SwingConstants.CENTER);
                lbl.setFont(Layout.FONT);//NUMBERFONT);
                lbl.setForeground(Color.WHITE);
                lbl.setBorder(Layout.BORDER);//BorderFactory.createEmptyBorder(2,2,2,2));
                
                aantallen[i] = lbl;
                //JPanel pp = new JPanel();
                //pp.setLayout(new FlowLayout(FlowLayout.CENTER));
                //pp.setOpaque(false);
                //pp.add(aantallen[i],BorderLayout.EAST);
                //p.add(pp);
                add(aantallen[i]);//p);
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            //super.paintComponent(g);
            super.paintBorder(g);
        }
        public void valuesChanged(IPlayer p) {
//        if(p.getCards().size()!=Integer.parseInt(numberValues.getText())){
//            numberValues.setText(""+p.getCards().size());
//            numberValues.setForeground(Color.red);
//        }
//        else {
//            numberValues.setForeground(Color.black);
//        }
            int[] aantallenInt = new int[DevCardType.NUMBER_OF_CARDS];
            for (int i = 0; i < DevCardType.NUMBER_OF_CARDS; i++) {//reset
                aantallenInt[i] = 0;
            }
            for (int i = 0; i < p.getCards().size(); i++) {//instellen
                aantallenInt[p.getCards().get(i).getValue()]++;
            }
            //weergeven op label
            for (int i = 0; i < aantallenInt.length; i++) {
                aantallen[i].setText("" + aantallenInt[i]);

                if (aantallenInt[i] == 0||p.isOtherPlayerState()) {
                    labels[i].setEnabled(false);
                } else {
                    labels[i].setEnabled(true);
                }
            }
        }
        private void playCard(int n) {
            if (Integer.parseInt(aantallen[n].getText()) > 0) {
                controller.playCard(n);
            }

        }
        private class mouseOor implements MouseListener{
            private SouthPanel south;

            public mouseOor(SouthPanel south) {
                this.south = south;
            }
            public void mouseClicked(MouseEvent e) {
                        JLabel lbl = (JLabel) (e.getSource());
                        playCard(Integer.parseInt(lbl.getName()));
                        //panel terug wegdoen
                        south.showBank();
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        
        }
        
        
    }
}
