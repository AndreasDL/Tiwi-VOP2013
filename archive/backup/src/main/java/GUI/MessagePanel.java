/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import utility.Layout;

/**
 * Dit paneel toont instructie voor het plaatsen van de gebouwen en houdt een done knop bij
 * @author Samuel
 */

public class MessagePanel extends JPanel{
    /**
     * Dit paneel toont instructie voor het plaatsen van de gebouwen en houdt een done knop bij
     */




    SurroundPanel surroundPanel;
    boolean initiateBuilding=true;
    private String s="Versleep de tegels naar het bord, klik op een tegel om hem te verwijderen.";
    private final String txtNumbers="Klik op 1 van de hoektegels om de nummers de plaatsen";
    private final String txTVillages="Plaats de startdorpen en startstraten";
    JLabel area;
    /**
     * standaard constructer
     * @param surroundPanel dient om te kunnen reageren op de knop
     */
    public MessagePanel(final SurroundPanel surroundPanel) {
        this.surroundPanel=surroundPanel;
        setLayout(new FlowLayout(FlowLayout.CENTER));
        area=new JLabel(s);
        area.setFont(Layout.FONT);
        //area.setLineWrap(true);
        //area.setWrapStyleWord(true);
        area.setText(s);
        //area.setPreferredSize(new Dimension(700,50));
        //area.setMinimumSize(new Dimension(700,50));
        //area.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
        //area.setAlignmentY(JTextArea.CENTER_ALIGNMENT);
        area.setBackground(this.getBackground());
        //area.setEditable(false);
        
        //area.setPreferredSize(new Dimension(200, 400));
        
        
        this.add(area,RIGHT_ALIGNMENT); 
 
        this.setVisible(true);
        
  
        
    }
    /**
     * verandert de text die getoond wordt
     */
    public void changeText(){
        //s="plaats twee dorpen met telkens ernaast een straat. een dorp plaats je door op een kruising tussen 3 tegels te klikken, een straat plaats je aan een lijn naast een dorp klik daarna op done om naar de volgende fase te gaan.";
        area.setText(txTVillages);
    }

    void initiateNumberPlacing() {
        area.setText(txtNumbers);
    }
}
