package GUI;


import GUI.GUITiles.*;
import controller.IController;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author Samuel
 */
public class CatanBord extends JPanel implements MouseListener{
    /**
     * Deze klasse tekent het spelbord op het scherm.
     * Hij maakt daarbij gebruik van GUITiles.
     */
    IController controller;
    SelectTilePanel selectPanel;
    StatsWindow stats = new StatsWindow();
    private ResourceBundle constants = ResourceBundle.getBundle("utility.config");
    
    int scale=15;
    int xBetween2Tiles= Integer.parseInt(constants.getString("xBetween2Tiles"));//6;
    int yBetween2Tiles= Integer.parseInt(constants.getString("yBetween2Tiles"));//4;
    static int[] NUMBERTILES={4,5,6,7,6,5,4};
    
    int beginX=600;
    int beginY=0;
    GUITile[][] tiles=new GUITile[7][7];
    int[] xBeginRow={0,-xBetween2Tiles*scale,-2*xBetween2Tiles*scale,-3*xBetween2Tiles*scale,-3*xBetween2Tiles*scale,-3*xBetween2Tiles*scale,-3*xBetween2Tiles*scale};
    //geeft de beginpositie van de x waarde van de volgende rij
    int[] yBeginRow={0,yBetween2Tiles*scale,2*yBetween2Tiles*scale,3*yBetween2Tiles*scale,5*yBetween2Tiles*scale,7*yBetween2Tiles*scale,9*yBetween2Tiles*scale};
    private final int lengthfield=7;

    public CatanBord() {
        /**
         * Standaard constructor.
         * Indien je dit gebruikt moet je later manueel de controller nog instellen.
         */
        setMinimumSize(new Dimension(500, 500));
        initTiles();
        addMouseListener(this);
    }
    public CatanBord(IController controller) {
        /**
         * Constructor die de controller al direct meekrijgt.
         */
        this();
        this.controller=controller;
    }
    @Override
    protected void paintComponent(Graphics g) {
        /**
         * Uittekenen van het panel.
         */
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        if(this.getWidth()/(8*xBetween2Tiles)<this.getHeight()/(15*yBetween2Tiles)) {
            scale=(int) (this.getWidth()/(8*xBetween2Tiles)); //8 ipv7(som vd stralen) zodat bij afronding altijd heel het veld zichtbaar is
        }        
        else {
            scale=(int) (this.getHeight()/(15*yBetween2Tiles)); //15 ipv14(som vd stralen) zodat bij afronding altijd heel het veld zichtbaar is
        }       
        beginX=(int) (this.getWidth()/2-xBetween2Tiles*scale/1.3);
        beginY=3*scale;   //zodat de taakbalk ook gebruikt wordt
        drawTiles(g);
        drawRoads(g);
        drawSettlements(g);
    }
    public void changeController(IController placeNumbersController) {
        /**
         * Stel de gebruikte controller in. 
         * De controller is afhankelijk van de fase waarin het spel zich bevind.
         * (Aanmaken bord != spel spelen.)
         */
        this.controller=placeNumbersController;
        repaint();
    }
    
    //eventhandelstuff
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON3){//rechtermuisklik
            boolean showDialog = false; //only show when something is there
            for(int i=0;i<lengthfield;i++){
                for(int j=0;j<NUMBERTILES[i];j++){
                    if(controller.getTile(i, j).getSettlement(tiles[i][j].getPointOnBuilding(e.getX(), e.getY())) != null){//kijken of op building en building bestaat
                        stats.setSettlement(controller.getTile(i, j).getSettlement(tiles[i][j].getPointOnBuilding(e.getX(), e.getY())));
                        showDialog=true;
                    }
                    else if(controller.getTile(i, j).getRoad(tiles[i][j].getPointOnStreet(e.getX(), e.getY())) != null){ //kijk of op straat
                        int temp = tiles[i][j].getPointOnStreet(e.getX(), e.getY());
                        stats.setRoad(controller.getTile(i, j).getRoad(tiles[i][j].getPointOnStreet(e.getX(), e.getY())));
                        showDialog=true;
                    }
                    else if(tiles[i][j].isPointInTile(e.getX(), e.getY())){ // kijken of op hexagon
                        stats.setTile(controller.getTile(i, j));
                        showDialog=true;
                    }
                }
            }
            if(showDialog){
                stats.setLocation(e.getLocationOnScreen());
                stats.setVisible(true);
                stats.requestFocus();
            }
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
        ArrayList<int[]> tellers=new ArrayList<int[]>();
        
        for(int i=0;i<lengthfield;i++){
            for(int j=0;j<NUMBERTILES[i];j++){
                int buildingIndex=tiles[i][j].getPointOnBuilding(e.getX(), e.getY());
                if(buildingIndex!=-1){
                    System.out.println(i+"        "+j+"           "+buildingIndex);
                    controller.clickSettlement(i, j, buildingIndex);
                    tellers.add(new int[]{i,j,buildingIndex});
                    //controller.placeSettlement(i, j, buildingIndex);
                }
                else if(tiles[i][j].isPointOnStreet(e.getX(), e.getY())){ //kijk of op straat
                    
                }
                else if(tiles[i][j].isPointInTile(e.getX(), e.getY())){ // kijken of op hexagon
                            //controller.placeNumbers(i, j);//nummers leggen
                            controller.placeTile(i,j);//tegel leggen
                }
                    
                
                
            }
            
       
        }
        if(!tellers.isEmpty()){
                controller.placeSettlements(tellers);
            
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        for(int i=0;i<lengthfield;i++){
            for(int j=0;j<NUMBERTILES[i];j++){
                int buildingIndex=tiles[i][j].getPointOnBuilding(e.getX(), e.getY());
                if(buildingIndex!=-1){
                    controller.placeSettlement(i, j, buildingIndex);
                }
            }
       
        }
        repaint();
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        
    }
    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    //private stuffs
    private void placeTile(int x, int y) {
        controller.placeTile(x,y);
    }    
    private void drawSettlements(Graphics g) {
        Image settlementImg = null;
        try {
           Image img  = ImageIO.read(new File(getClass().getResource("/settlement.png").getFile()));
           settlementImg = img.getScaledInstance(scale*4, scale*4,Image.SCALE_SMOOTH);
        } catch (IOException ex) {
            //Logger.getLogger(CatanBord.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(controller!=null){
            for(int i=0;i<lengthfield;i++){
                for(int j=0;j<NUMBERTILES[i];j++){
                    if(controller.getTile(i, j).getType().getValue()<6){//Don't do anything for seatiles and empty tiles
                        for(int k=0;k<6;k++){
                            if(controller.getTile(i, j).getSettlement(k) != null){
                                g.drawImage(settlementImg, tiles[i][j].getxPoints()[k]-scale*2, tiles[i][j].getyPoints()[k]-scale*2, this);
                            }
                        }
                    }
                }
            }
        }
    }
    private void drawRoads(Graphics g) {
        Image roadImg = null;
        Graphics2D g2 = (Graphics2D) g;
        try {
           Image tmp  = ImageIO.read(new File(getClass().getResource("/road.png").getFile()));
           
           roadImg = tmp.getScaledInstance(scale*4, scale*4,Image.SCALE_SMOOTH);
           
        } catch (IOException ex) {
            //Logger.getLogger(CatanBord.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(controller!=null){
            for(int i=0;i<lengthfield;i++){
                for(int j=0;j<NUMBERTILES[i];j++){
                    if(controller.getTile(i, j).getType().getValue()<6){//Don't do anything for seatiles and empty tiles
                        for(int k=0;k<6;k++){
                            if(controller.getTile(i, j).getRoad(k) != null){
                                int x = (tiles[i][j].getxPoints()[k]+tiles[i][j].getxPoints()[(k+1)%6])/2 - scale*2;
                                int y = (tiles[i][j].getyPoints()[k]+tiles[i][j].getyPoints()[(k+1)%6])/2 - scale*2;
                                AffineTransform transform=new AffineTransform();
                                transform.translate(x, y);
                                transform.rotate(tiles[i][j].getxPoints()[k]-tiles[i][j].getxPoints()[(k+1)%6], tiles[i][j].getyPoints()[k]-tiles[i][j].getyPoints()[(k+1)%6],scale*2,scale*2);
                                g2.drawImage(roadImg, transform, this);
                            }
                        }
                    }
                }
            }
        }
    }
    private void drawTiles(Graphics g) { //tekent alle zeshoeken en getalfiche erin
        calculateTile(); //berekent posities vd tile
        for(int i=0;i<lengthfield;i++){
            for(int j=0;j<NUMBERTILES[i];j++){
                tiles[i][j].setX(beginX+xBeginRow[i]+xBetween2Tiles*scale*j);
                tiles[i][j].setY(beginY+yBeginRow[i]+yBetween2Tiles*scale*j);
                tiles[i][j].setScale(scale);             
                tiles[i][j].draw(g,controller.getTile(i, j));
                
            }
        }
    }
    private void calculateTile() {
        xBeginRow=new int[]{0,-xBetween2Tiles*scale,-2*xBetween2Tiles*scale,-3*xBetween2Tiles*scale,-3*xBetween2Tiles*scale,-3*xBetween2Tiles*scale,-3*xBetween2Tiles*scale};
        yBeginRow=new int[]{0,yBetween2Tiles*scale,2*yBetween2Tiles*scale,3*yBetween2Tiles*scale,5*yBetween2Tiles*scale,7*yBetween2Tiles*scale,9*yBetween2Tiles*scale};
    }
    private void initTiles() { //zet zeetegels en lege tegels met border errond
        for(int i=0;i<lengthfield;i++){
            for(int j=0;j<NUMBERTILES[i];j++){
                tiles[i][j]=new GUITile();
                tiles[i][j].setX(beginX+xBeginRow[i]+xBetween2Tiles*scale*j);
                tiles[i][j].setY(beginY+yBeginRow[i]+yBetween2Tiles*scale*j);                      
            }
        }
    }    
}
    
    

    
    
    

