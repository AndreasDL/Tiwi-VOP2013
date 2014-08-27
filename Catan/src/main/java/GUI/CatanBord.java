package GUI;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import model.interfaces.IController;
import observer.IObservable;
import observer.IObserver;
import utility.ImageRecolor;
import utility.Layout;
import utility.Structure;


/**
 * Deze klasse tekent het spelbord op het scherm.
 * Hij maakt daarbij gebruik van gifTiles.
 * @author Samuel
 */
public class CatanBord extends JPanel implements MouseListener, IObserver{
    private int m=0;    
    private IController controller;
    private PropertiesPanel stats = new PropertiesPanel();    
    private ResourceBundle constants = ResourceBundle.getBundle("utility.config");    
    private int scale=15;//scale is nu de diameter geworden van gifTile
    private int xBetween2Tiles= Integer.parseInt(constants.getString("xBetween2Tiles"));//6;
    private int yBetween2Tiles= Integer.parseInt(constants.getString("yBetween2Tiles"));//4;
    private static int[] NUMBERTILES={4,5,6,7,6,5,4};
    
    
    private int beginX=600;
    private int beginY=0;
    private TileDrawer[][] tiles=new TileDrawer[7][7];
    private int[] xBeginRow={0,-xBetween2Tiles*scale,-2*xBetween2Tiles*scale,-3*xBetween2Tiles*scale,-3*xBetween2Tiles*scale,-3*xBetween2Tiles*scale,-3*xBetween2Tiles*scale};
    //geeft de beginpositie van de x waarde van de volgende rij
    private int[] yBeginRow={0,yBetween2Tiles*scale,2*yBetween2Tiles*scale,3*yBetween2Tiles*scale,5*yBetween2Tiles*scale,7*yBetween2Tiles*scale,9*yBetween2Tiles*scale};
    private final int lengthfield=7;
    private BufferedImage icon;
    private Image settlementImg=null;
    private Image roadImg=null;
    private Image cityImg=null;
    
    /**
    * Standaard constructor.
    * Indien je dit gebruikt moet je later manueel de controller nog instellen.
    */
    public CatanBord() {
      //  setOpaque(true);
        setMinimumSize(new Dimension(500, 500));
   //     setBorder(Layout.BORDER);
        initTiles();
        setOpaque(false);
        addMouseListener(this);
        try {
            settlementImg = ImageIO.read(getClass().getResource("/settlements/settlement.png"));
        } catch (IOException ex) {
            Logger.getLogger(CatanBord.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            cityImg = ImageIO.read(getClass().getResource("/settlements/city.png"));
          
        } catch (IOException ex) {
            Logger.getLogger(CatanBord.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            roadImg  = ImageIO.read(getClass().getResource("/settlements/road.png"));
        } catch (IOException ex) {
            Logger.getLogger(CatanBord.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        
        //        try {
        //            icon = ImageIO.read(getClass().getResource("/wall2.jpg"));
        //        } catch (IOException ex) {
        //            Logger.getLogger(CatanBord.class.getName()).log(Level.SEVERE, null, ex);
        //        }
              //  setBackground(Color.black);
        
     //setBackground(Color.WHITE);   
     //setOpaque(true);
    }
    /**
     * Constructor die de controller al direct meekrijgt.
     * @param controller De gebruikte controller. (Type is afhankelijk van de fase waarin het spel zich bevindt.)
     */
    public CatanBord(IController controller) {
        this();
        this.controller=controller;
    }
    
    public void setController(IController controller) {
        this.controller = controller;
    }
    /**
     * 
     * Uittekenen van het panel.
     * @param g 
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        //g2.setRenderingHint(RenderingHints.KEY_RENDERING,  RenderingHints.VALUE_RENDER_QUALITY);

     //    g.drawImage(icon.getScaledInstance(getWidth(), getHeight(),Image.SCALE_SMOOTH), 0, 0, null);
        
        if(this.getWidth()/(8*xBetween2Tiles)<this.getHeight()/(15*yBetween2Tiles)) {
            scale=(int) (this.getWidth()/(8*xBetween2Tiles))+1; //8 ipv7(som vd stralen) zodat bij afronding altijd heel het veld zichtbaar is
        }        
        else {
            scale=(int) (this.getHeight()/(1.0*15*yBetween2Tiles)+0.5); //15 ipv14(som vd stralen) zodat bij afronding altijd heel het veld zichtbaar is
        }       
        beginX=(int) (this.getWidth()/2-xBetween2Tiles*scale/1.3);
        beginY=(int)1.5*scale;   //zodat de taakbalk ook gebruikt wordt
        drawFields(g);
        drawRoads(g);
        drawSettlements(g);
        
       
    }
    /**
     * Stel de gebruikte controller in. 
     * @param controller De gebruikte controller. (type is afhankelijk van de fase waarin het spel zich bevindt.)
     */
    public void changeController(IController controller) {
        this.controller=controller;
    }
    
    //eventhandelstuff
    /**
     * Kijkt op welke plaats er is rechtsgeklikt, en toont het bijpassende MessagePanel
     * als wordt linkergeklinkt roept het de functie removetile aan die de tile verwijdert zodat deze op een andere plaats kan gezet worden
     * @param e 
     */
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
                    else if(controller.getTile(i, j).getRoad(tiles[i][j].getPointOnRoad(e.getX(), e.getY())) != null){ //kijk of op straat
                        stats.setRoad(controller.getTile(i, j).getRoad(tiles[i][j].getPointOnRoad(e.getX(), e.getY())));
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
    
    }
    /**
     * Kijkt waar er geklikt werd en geeft dit door aan het modelboard die dan indien mogelijk een gebouw maakt
     * @param e duidt aan waar geklikt werd
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        
        if(e.getButton() == MouseEvent.BUTTON1){//linkermuisklik

            ArrayList<int[]> tellers=new ArrayList<int[]>();
            boolean setSettlement=true;
            for(int i=0;i<lengthfield;i++){
                for(int j=0;j<NUMBERTILES[i];j++){
                    int buildingIndex=tiles[i][j].getPointOnBuilding(e.getX(), e.getY());
                    if(buildingIndex!=-1){

                        controller.clickSettlement(i, j, buildingIndex);
                        tellers.add(new int[]{i,j,buildingIndex});
                        //controller.placeSettlement(i, j, buildingIndex);
                    }
                    else if(tiles[i][j].isPointOnRoad(e.getX(), e.getY())){ //kijk of op straat
                        setSettlement=false;
                        int roadIndex=tiles[i][j].getPointOnRoad(e.getX(), e.getY());
                        controller.clickRoad(i, j, roadIndex);
                        tellers.add(new int[]{i,j,roadIndex});
                        
                        
                    }
                    
                     else if(tiles[i][j].isPointInTile(e.getX(), e.getY())){ // kijken of op hexagon
                                controller.placeTile(i, j);//tegel leggen of nummers leggen bij klikken tegel
                    }



                }


            }
            if(!tellers.isEmpty()){
                if(setSettlement) {
                    controller.placeSettlements(tellers);
                }
                else{
                    controller.placeRoads(tellers);
                }
                //repaint();

            }

            
            
            
        }
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        
    }
    @Override
    public void mouseExited(MouseEvent e) {
        
    }

   /**
    * kijkt waar er geklikt wordt en roept de overeenkomstige functie van de controller op
    * @param g :het Graphics object
    */
    private void drawFields(Graphics g) { //tekent alle zeshoeken en getalfiche erin
        calculateTile(); //berekent posities vd tile
        ArrayList<Point> tiledrawerWithRed=new ArrayList<Point>();
        for(int i=0;i<lengthfield;i++){
            for(int j=0;j<NUMBERTILES[i];j++){
                
                tiles[i][j].setX(beginX+xBeginRow[i]+xBetween2Tiles*scale*j);
                tiles[i][j].setY(beginY+yBeginRow[i]+yBetween2Tiles*scale*j);
                tiles[i][j].setScale(scale);
                if(controller.getTile(i, j).isRolled()){
                    tiledrawerWithRed.add(new Point(i, j));
                }
                else {
                    tiles[i][j].draw(g,controller.getTile(i, j),false);
                }
            }
        }
        for(Point point:tiledrawerWithRed){
            tiles[point.x][point.y].draw(g,controller.getTile(point.x, point.y),true);
        }
        
    }
    /**
     * berekent de hoekpunten van de zeshoek en slaat ze op in tabellen
     */
    private void calculateTile() {
        xBeginRow=new int[]{0,-xBetween2Tiles*scale,-2*xBetween2Tiles*scale,-3*xBetween2Tiles*scale,-3*xBetween2Tiles*scale,-3*xBetween2Tiles*scale,-3*xBetween2Tiles*scale};
        yBeginRow=new int[]{0,yBetween2Tiles*scale,2*yBetween2Tiles*scale,3*yBetween2Tiles*scale,5*yBetween2Tiles*scale,7*yBetween2Tiles*scale,9*yBetween2Tiles*scale};
    }
    /**
     * Zet zeetegels en lege tegels met border errond.
     */
    private void initTiles() { 
        for(int i=0;i<lengthfield;i++){
            for(int j=0;j<NUMBERTILES[i];j++){
                tiles[i][j]=new TileDrawer();
                tiles[i][j].setX(beginX+xBeginRow[i]+xBetween2Tiles*scale*j);
                tiles[i][j].setY(beginY+yBeginRow[i]+yBetween2Tiles*scale*j);                      
            }
        }
    }
    /**
     * tekent de dorpen
     * @param g 
     */
    private void drawSettlements(Graphics g) {       
        if(controller!=null){
            for(int i=0;i<lengthfield;i++){
                for(int j=0;j<NUMBERTILES[i];j++){
                    if(controller.getTile(i, j).getType().getValue()<6){//Don't do anything for seatiles and empty tiles
                        for(int k=0;k<6;k++){
                            if(controller.getTile(i, j).getSettlement(k) != null){
                                if (controller.getTile(i, j).isTempCity(k)){ //teken mogelijke plaatsen cities
                                    g.setColor(Layout.HIGHLIGHT_COLOR_CITY);
                                    g.fillOval( tiles[i][j].getxPoints()[k]-scale*2, tiles[i][j].getyPoints()[k]-scale*2,scale*3,scale*3);
                                    g.setColor(Color.black);                                
                                }
                                else if(controller.getTile(i, j).getSettlement(k).settlementSize() == Structure.SETTLEMENT.getValue()){
                                    Image img =createImage(new FilteredImageSource(settlementImg.getSource(), new ImageRecolor(controller.getTile(i, j).getSettlement(k).getPlayer().getColor())));
                                    g.drawImage(img.getScaledInstance(scale*4, scale*4,Image.SCALE_SMOOTH), tiles[i][j].getxPoints()[k]-scale*2, tiles[i][j].getyPoints()[k]-scale*2,null);
                               
                                         
                                   /* g.setColor(controller.getTile(i, j).getSettlement(k).getPlayer().getColor());    
                                    g.fillOval( tiles[i][j].getxPoints()[k]-scale*3/2, tiles[i][j].getyPoints()[k]-scale*3/2,scale*3,scale*3);
                                    g.setColor(Color.black);
                                   */ 
                                    
//                            
                                }
                                else if(controller.getTile(i, j).getSettlement(k).settlementSize() == Structure.CITY.getValue()){
                                    
                                   // g.drawImage(cityImg, tiles[i][j].getxPoints()[k]-scale*2, tiles[i][j].getyPoints()[k]-scale*2, null);
                                    Image img =createImage(new FilteredImageSource(cityImg.getSource(), new ImageRecolor(controller.getTile(i, j).getSettlement(k).getPlayer().getColor())));
                                    g.drawImage(img.getScaledInstance(scale*4, scale*4,Image.SCALE_SMOOTH), tiles[i][j].getxPoints()[k]-scale*2, tiles[i][j].getyPoints()[k]-scale*2, null);
                                }
                                
                            }
                            else if(controller.getTile(i, j).isTempVillage(k)){
                                    g.setColor(Layout.HIGHLIGHT_COLOR_VILLAGE);    
                                    g.fillOval( tiles[i][j].getxPoints()[k]-scale*2/2, tiles[i][j].getyPoints()[k]-scale*2/2,scale*2,scale*2);
                                    g.setColor(Color.black);
                            }
                        }
                    }
                }
            }
        }
    }
    /**
     * vraagt aan controller waar er wegen liggen en tekent deze
     * @param g 
     */
    private void drawRoads(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
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
                                Image img =createImage(new FilteredImageSource(roadImg.getSource(), new ImageRecolor(controller.getTile(i, j).getRoad(k).getPlayer().getColor())));
                                g2.drawImage(img.getScaledInstance(scale*4, scale*4,Image.SCALE_SMOOTH), transform, null);
                                //g2.drawImage(roadImg, transform, null);
                            }
                            else if(controller.getTile(i, j).isTempRoad(k)){
                                    g.setColor(Layout.HIGHLIGHT_COLOR_ROAD);    
                                    //g.fillOval( (tiles[i][j].getxPoints()[k]+tiles[i][j].getxPoints()[(k+1)%6])/2-scale*2/2,
                                    //            (tiles[i][j].getyPoints()[k]+tiles[i][j].getyPoints()[(k+1)%6])/2-scale*2/2,
                                    g2.setStroke(new BasicStroke(10));
                                    //            scale*2,scale*2);
                                    g.drawLine(tiles[i][j].getxPoints()[k], tiles[i][j].getyPoints()[k], 
                                               tiles[i][j].getxPoints()[(k+1)%6], tiles[i][j].getyPoints()[(k+1)%6]);
                                    
                                    g.setColor(Color.black);
                           }
                        }
                    }
                }
            }
        }
    }

    public void update(IObservable o, String arg) {
        
        if(arg == null || arg.equals("repaint") || arg.equals("all")){
            invalidate();
            repaint();
        }
        
        
    }

    
    
}