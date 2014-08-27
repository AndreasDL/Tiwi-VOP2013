/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import controller.IController;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JButton;

/**
 * Dit is een speciale knop met 2 afbeeldingen op die 2 dobbelstenen voorstellen.
 * Wanneer je op deze knop drukt worden er 2 nieuwe randomwaarden weergegeven op het scherm.
 * @author Andreas De Lille
 */
public class Dices extends JButton {
     Random rnd = new Random();
     int dice1;
     int dice2;
     IController controller;
     Image[] imgs;
     
     /**
      * Standaard constructor.
      * @param controller De controller die meegegeven wordt is afhankelijk van de fase waarin het spel zich bevindt.
      */
     public Dices(IController controller){
         initImgs();
         this.controller=controller;
         addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                generate();
            }
         });
         dice1 = 0;//rnd.nextInt(6)+1;
         dice2 = 0;//rnd.nextInt(6)+1;
         setPreferredSize(new Dimension(120,50));
         setOpaque(false);
         setContentAreaFilled(false);
     }
     
     /**
      * Gooi met de dobbelsteen en geeft de resources.
      */
     public void generate(){
         dice1 = rnd.nextInt(6)+1;
         dice2 = rnd.nextInt(6)+1;
         controller.giveResources(dice1+dice2);
         repaint();
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
}
