/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.east;

import model.interfaces.IController;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import model.Dices;
import observer.IObservable;
import observer.IObserver;
import utility.Layout;

/**
 * Dit is een speciale knop met 2 afbeeldingen op die 2 dobbelstenen voorstellen.
 * Wanneer je op deze knop drukt worden er 2 nieuwe randomwaarden weergegeven op het scherm.
 * @author Andreas De Lille
 */
public class DiceButton extends JButton implements IObserver{     
     private int dice1;
     private int dice2;
     private IController controller;
     private Image[] imgs;
     
     /**
      * Standaard constructor.
      * @param controller De controller die meegegeven wordt is afhankelijk van de fase waarin het spel zich bevindt.
      */
     public DiceButton(IController controller){
         initImgs();
         this.controller=controller;
         controller.getDices().addObserver(this);
         addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                generate();
            }
         });
         dice1 = 0;//rnd.nextInt(6)+1;
         dice2 = 0;//rnd.nextInt(6)+1;
         setPreferredSize(Layout.DICES_DIM);
         setOpaque(false);
         setContentAreaFilled(false);
         setForeground(Layout.DICES_FOREGR);
         setEnabled(false);
     }
     
     /**
      * Gooi met de dobbelsteen en geeft de resources.
      */
     public void generate(){
         controller.giveResources();
         setEnabled(false);
     }
     /**
      * Uittekenen van de component
      * @param g het graphics object.
      */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (dice1 > 0 && dice2 > 0 && dice1 <= 6 && dice2 <= 6){//ze beginnen op 1
            setText("");
            g.drawImage(imgs[dice1-1].getScaledInstance((getWidth()-40) / 2, getHeight()-20, 50),10,10,null);
            g.drawImage(imgs[dice2-1].getScaledInstance((getWidth()-40) / 2, getHeight()-20, 50),getWidth()/2+10,10,null);
        }else{
            setText("dobbel");
        }
    }
    
    /**
     * Laadt de afbeeldingen die gebruikt worden in de klasse.
     */
    private void initImgs(){
        imgs = new Image[6];
        
        for (int i = 0 ; i < 6 ; i++){
            try{
                int h = i+1;
                imgs[i] = ImageIO.read(getClass().getResource("/dices/" + h + ".png"));
            }catch (Exception e){
                System.out.println("probleem laden dobbelsteen " + i);
            }
        }
    }
    public void showText(int dice1,int dice2) {
        this.dice1=dice1;
        this.dice2=dice2;
        System.out.println("ShowText in Dices");
    }
    public void update(IObservable o, String arg) {
        Dices dices = (Dices) o;
        dice1=dices.getDice1();
        dice2=dices.getDice2();
        repaint();
        System.out.println("DicesUpdated");
    }
    
    @Override
    public void setEnabled(boolean b){
        super.setEnabled(b);
        if (b){
            setBorder(Layout.ACTIVE_BORDER);
            //setBackground(Layout.ACTIVE_BACK_COLOR);
        }else{
            setBorder(Layout.NOT_ACTIVE_BORDER);
            //setBackground(Layout.NOT_ACTIVE_BACK_COLOR);
        }
    }
}
