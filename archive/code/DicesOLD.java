/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import controller.IController;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;

/**
* Dit is een speciale knop met 2 labels op die 2 dobbelstenen voorstellen.
* Wanneer je op deze knop drukt worden er 2 nieuwe randomwaarden weergegeven op het scherm.
* @author Tom
*/
public class DicesOLD extends JButton {
     Random rnd = new Random();
     int dice1;
     int dice2;
     IController controller;
     
     /**
      * Standaard constructor.
      * @param controller De controller die meegegeven wordt is afhankelijk van de fase waarin het spel zich bevindt.
      */
     public DicesOLD(IController controller){
         this.controller=controller;
         addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                generate();
                
            }
         });
         dice1 = 0;//rnd.nextInt(6)+1;
         dice2 = 0;//rnd.nextInt(6)+1;
         setPreferredSize(new Dimension(120,50));
     }
    /**
    * Gooi met de dobbelsteen en geef de resources.
    */
     public void generate(){
         dice1 = rnd.nextInt(6)+1;
         dice2 = rnd.nextInt(6)+1;
         controller.giveResources(dice1+dice2);
         repaint();
     }

     /**
      * Teken de component op het scherm.
      * @param g Graphics component.
      */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString("Dobbelstenen:", 5, 15);
        g.drawRect(5, 25, 20, 20);
        g.drawString(Integer.toString(dice1), 10, 40);
        g.drawRect(30, 25, 20, 20);
        g.drawString(Integer.toString(dice2), 35, 40);
    }
}
