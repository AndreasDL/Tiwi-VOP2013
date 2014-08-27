/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GUI.GUITiles.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import model.SelectTileModel;
import utility.TileType;

/**
 *
 * @author Whatever
 */
public class SelectTilePanel extends JPanel implements MouseListener{
    /**
     * Dit paneel is het linkerpaneel tijdens het aanmaken van het bord.
     * De verschillende gifTiles worden weergeven onder mekaar.
     * Hun aantal dat nog gelegd moet worden wordt ook weergegeven.
     */
    private ArrayList<IGUITile> tiles;
    private JButton btnDone;
    private JButton btnReset;    
    private SelectTileModel model;
    
    private SurroundPanel surroundPanel;
    

    public SelectTilePanel(SelectTileModel model){
        /**
        * Standaard constructor. 
        * Heeft een model nodig om de aantallen bij te houden.
        */
        initTiles();//initialseren en aanmaken van tiles
        this.model = model;
        
        setLayout(new BorderLayout());
        setSize(new Dimension(200,1200));
        rescaleAndTranslateTiles();
        
        JPanel pnl = new JPanel();
        pnl.setLayout(new FlowLayout());
            btnDone = new JButton("done");
            //btnDone.setEnabled(false); //even weglaten voor makkelijker testen zonder alles te moeten plaatsen
            btnDone.setText("Done");
        pnl.add(btnDone);
            btnReset = new JButton("reset");
            btnReset.setText("reset");
        pnl.add(btnReset);
        this.add(pnl,BorderLayout.SOUTH);
        
        this.addMouseListener(this);
        this.setVisible(true);
        
        btnDone.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                surroundPanel.initiateNumberPlacing();
            }
        });
    }

    
    
    @Override
    public void paintComponent(Graphics g){
        /**
         * Uittekenen van het panel.
         */
        super.paintComponent(g);
        //herschalen
        rescaleAndTranslateTiles();
        //tegeltjes tekenen met nummers
        for (int i = 0 ; i < tiles.size()-1 ; i++){
            //tiles.get(i).draw(g,model.getCount(i),model.getTile(i).getType().getValue());
        }
        
    }
    public void setDone(){
        /**
         * Wordt door het model opgeroepen indien alle tegel gelegd zijn.
         */
        btnDone.setEnabled(true);
    }
    public void setSurroundPanel(SurroundPanel aThis) {
        /**
         * Stel het gebruikte surroundPanel in.
         */
        this.surroundPanel=aThis;
    }
    
    //private
    private void initTiles(){
        tiles = new ArrayList<IGUITile>();
        
        for (int i = 0 ; i < TileType.values().length-1 ; i++){//sea niet => length -1
            tiles.add(new GUITile());
        }
        
        rescaleAndTranslateTiles();
    }
    private void rescaleAndTranslateTiles(){
        
        int startY = 50;
        int scale = this.getWidth() /40;
        int startX = 0;//(this.getWidth()-tiles.get(0).getDiameter()) / 2;
        
        for (int i = 0 ; i < tiles.size() ; i++){
            tiles.get(i).setScale(scale);
            tiles.get(i).setX(startX);
            tiles.get(i).setY(startY + (tiles.get(i).getDiameter() +10)*i  );
        }
        
        this.setPreferredSize(new Dimension (this.getWidth(),(tiles.get(0).getDiameter() + 10 ) *8 + startY + 50 ));
        //100 => knopruimte
    }

    //events
    public void mouseClicked(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    public void mousePressed(MouseEvent e) {
        //get selected hexagon
        int i = 0;
        while (i < tiles.size() && !tiles.get(i).isPointInTile(e.getX(),e.getY())){
            i++;
        }

        if (i < tiles.size() && model.getCount(i)  >= 0){
            //indien er op een haxegon geklikt was en er nog tegels van zijn
            model.setSelected(i);
        }else{//indien op niets geklikt => tile op null zetten
            model.resetSelected();
        }
        repaint();
    }
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}