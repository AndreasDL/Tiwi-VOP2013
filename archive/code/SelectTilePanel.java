/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import model.SelectTileModel;
import utility.TileType;

/**
 * Dit paneel is het linkerpaneel tijdens het aanmaken van het bord.
 * De verschillende gifTiles worden weergeven onder mekaar.
 * Hun aantal dat nog gelegd moet worden wordt ook weergegeven.
 * @author Andreas De Lille
 */
public class SelectTilePanel extends JPanel implements MouseListener, DragGestureListener, DragSourceListener{
    private ArrayList<TileDrawer> tiles;
    private JButton btnDone; 
    private SelectTileModel model;
    private SurroundPanel surroundPanel;
    
    /**
     * Standaard constructor. 
     * @param model Heeft het model nodig om de aantallen bij te houden.
     */
    public SelectTilePanel(SelectTileModel model){
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
            //btnReset = new JButton("reset");
            //btnReset.setText("reset");
        //pnl.add(btnReset);
        this.add(pnl,BorderLayout.SOUTH);
        
        DragSource dragSource = new DragSource();
        dragSource.addDragSourceListener(this);
        DragGestureRecognizer recognizer = dragSource.createDefaultDragGestureRecognizer(this,DnDConstants.ACTION_COPY_OR_MOVE , this);
        this.addMouseListener(this);
        this.setVisible(true);
        
        btnDone.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                surroundPanel.initiateNumberPlacing();
            }
        });
        repaint();
    }
   
    /**
     * Stel het gebruikte surroundPanel in.
     * @param aThis Het gebruikte surroundPanel
     */
    public void setSurroundPanel(SurroundPanel aThis) {
        this.surroundPanel=aThis;
    }    
    /**
     * Uitekenen van de component.
     * @param g Het gebruikte graphics object.
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        //herschalen
        rescaleAndTranslateTiles();
        //tegeltjes tekenen met nummers
        for (int i = 0 ; i < tiles.size()-1 ; i++){
            if(model.getSelectedTileToDraw()!=null)
                tiles.get(i).draw(g,model.getCount(i),model.getTile(i).getType(),model.getSelectedTileToDraw().equals(model.getTile(i)));
            else
                tiles.get(i).draw(g,model.getCount(i),model.getTile(i).getType(),false);
        }
    } 
    /**
     * Wordt door het model opgeroepen indien alle tegel gelegd zijn.
     * Enabled de done button.
     */
    public void setDone(){
        btnDone.setEnabled(true);
    }
    
    //private
    /**
     * initialiseert de verschillende soorten tegels en hun bijhorende aantallen.
     */
    private void initTiles(){
        tiles = new ArrayList<TileDrawer>();
        
        for (int i = 0 ; i < TileType.values().length-1 ; i++){//sea niet => length -1
            tiles.add(new TileDrawer());
        }
        
        rescaleAndTranslateTiles();
    } 
    /**
     * Herschalen en verschuiven van de tegels.
     */
    private void rescaleAndTranslateTiles(){
        
        int startY = 50;
        int scale = (this.getHeight() - 50 - startY)/(tiles.size()*10);
        int startX = (this.getWidth()-tiles.get(0).getDiameter()) / 2;
        
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
    }
    public void mousePressed(MouseEvent e) {
        /*if(e.getButton() == MouseEvent.BUTTON1){//linkermuisklik
            //get selected hexagon
            int i = 0;
            while (i < tiles.size() && !tiles.get(i).isPointInTile(e.getX(),e.getY())){
                i++;
            }

            if (i < tiles.size() && model.getCount(i)  >= 0){
                //indien er op een hexagon geklikt was en er nog tegels van zijn
                model.setSelected(i);
            }else{//indien op niets geklikt => tile op null zetten
                model.resetSelected();
            }
            repaint();
        }*/
    }
    public void mouseReleased(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
    
    public void dragGestureRecognized(DragGestureEvent arg0) {
        int i = 0;
        while (i < tiles.size() && !tiles.get(i).isPointInTile((int) arg0.getDragOrigin().getX(),(int) arg0.getDragOrigin().getY())){
            i++;
        }
        if (i < tiles.size() && model.getCount(i)  > 0){
                //indien er op een hexagon geklikt was en er nog tegels van zijn
            
            BufferedImage image = null;
            try {
                image = ImageIO.read(new File(getClass().getResource("/" + model.getTile(i).getType() + ".gif").getFile()));
            } catch (IOException ex) {
                Logger.getLogger(SelectTilePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Cursor cursor = toolkit.createCustomCursor(image, new Point(16, 16), "Panel");
            arg0.startDrag(cursor, new TileTransfer(model.getTile(i).getType()));
            model.setSelected(i);
            repaint();
        }
    }

    public void dragEnter(DragSourceDragEvent arg0) {
    }

    public void dragOver(DragSourceDragEvent arg0) {
    }

    public void dropActionChanged(DragSourceDragEvent arg0) {
    }

    public void dragExit(DragSourceEvent arg0) {
    }

    /**
     * Deselecteert de Tile na de dragDrop.
     * @param arg0 
     */
    public void dragDropEnd(DragSourceDropEvent arg0) {
        model.resetSelected();
    }
}

