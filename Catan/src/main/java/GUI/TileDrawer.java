/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import model.interfaces.ITile;
import utility.Layout;
import utility.TileType;

/**
 * Deze klasse tekent een Gif-prentjes op een panel. 
 * Deze prentjes komen overeen met de verschillende grondstoffen.
 * @author Whatever
 */
public class TileDrawer {
    int m=0;
    private int x=0,y=0;
    private ResourceBundle constanten = ResourceBundle.getBundle("utility.config");
    
    private int scale=15;              //breedte en hoogte van het omliggende vierkant
    private int[] xPoints;              //de x waarde vd hoekpunten vd tegels
    private int[] yPoints;
    final int groottecircel=3;
    
    private final int WIDTH_TILE=4;          //grootte onderste zijde
    private final int WIDTH_BETWEEN_2_TILES=2;    //x afstand onderste zijde tot hoek
    private final int Y_BETWEEN_2_TILES=4;          //y afstand 2 tegels
    private HashMap<TileType,Image> imgs;
    
    //constructor
    /**
     * Standaard constructor. 
     * Leest alle images in vanuit de resources (default package van resources).
     * Haalt de lijst van verschillende grondstoffen op uit de enum TileType.
     * Bij een lege tegel wordt er nog een extra zwarte rand bijgetekend.
     */
    public TileDrawer(){
        
        if (imgs == null){
            imgs = new HashMap<TileType,Image>();

            for (TileType t : TileType.values()){
                Image img;
                try{
                    img = ImageIO.read(getClass().getResource("/tiles/" + t + ".gif"));

                    imgs.put(t, img);
                }catch (Exception e) {
                    System.out.println("fout: " + t);
                }
            }
        }
        calculatePoints();
    }
    
    //getters en setters
    /**
     * Geeft de x-coördinaat terug van de linkerbovenhoek.
     * @return x-coördinaat van de linkerbovenhoek.
     */
    public int getX() {
        return x;
    }
    /**
     * Stelt de x-coördinaat in van de linkerbovenhoek.
     * @param x De waarde van de x-coördinaat van de linkerbovenhoek.
     */
    public void setX(int x) {
        this.x = x;
    }
    /**
     * Geeft de y-coördinaat van de linkerbovenhoek.
     * @return de y-coördinaat van de linkerbovenhoek.
     */
    public int getY() {
        return y;
    }
    /**
     * Zet de y-coördinaat van de linkerbovenhoek.
     * @param y de y-coördinaat van de linkerbovenhoek.
     */
    public void setY(int y) {
        this.y = y;
    }
    /**
     * Stelt de schaal in. 
     * @param scale De schaal is de diameter/10.
     */
    public void setScale(int scale) {
        this.scale = scale;
    }
    /**
     * Geeft de diameter terug van de zeshoek.
     * @return De diameter = schaal * 10.
     */
    public int getDiameter(){
        return 10*scale; //scale is nu diameter
    }
    /**
     * Geeft de tabel terug met alle x-coördinaten van de zeshoek.
     * @return De tabel met alle x-coördinaten van de zeshoek.
     */
    public int[] getxPoints() {
        calculatePoints();
        return xPoints;
    }
    /**
     * Geeft de tabel terug met alle y-coördinaten van de zeshoek.
     * @return De tabel met alle y-coördinaten van de zeshoek.
     */
    public int[] getyPoints() {
        calculatePoints();
        return yPoints;
    }
    
    //tekenen
    /**
    * Uittekenen van het object.
    * @param g het gebruikte Graphics object.
    * @param number Het nummer dat moet weergegeven worden.
    *       -1 => rover.
    *       -2 => geen nummer tekenen.
    * @param t Het TileType dat overeen komt met de tegel die getekend moet worden.
    * @param selected Is de tegel al dan niet geslecteerd.
    *       true => extra kader rondtekenen.
    *       false => enkel de tegel zelf tekenen.
    */
    public void draw(Graphics g, int number, TileType t,boolean b){
        calculatePoints();
        g.drawImage(imgs.get(t).getScaledInstance( (2*WIDTH_BETWEEN_2_TILES + WIDTH_TILE)*scale ,8*scale,100 ),x,y,null);
        
        if ( number!=Integer.parseInt(constanten.getString("noDrawNumber")) && number != Integer.parseInt(constanten.getString("thiefNumber")) ){ //kijk of je nummer moet tekenen
            if(b) {
                g.setColor(Color.blue);
            }
            else {
                g.setColor(Color.WHITE);
            }
            g.fillOval(x + ((2*WIDTH_BETWEEN_2_TILES + WIDTH_TILE)*scale)/2-(3*scale)/2 ,y+(5*scale)/2, 3*scale , 3*scale);
            
            if(number==6||number==8) {
                g.setColor(Color.RED);
            }
            else if(b) {
                g.setColor(Color.white);
            }
            else {
                g.setColor(Color.BLACK);
            }
            Font origFont = g.getFont();
            Font font = Layout.NUMBERFONT;
            font = font.deriveFont(font.getStyle(),2*scale);
            g.setFont(font);
            FontMetrics fontMetrics = g.getFontMetrics();
            g.drawString(Integer.toString(number),x+(2*WIDTH_BETWEEN_2_TILES + WIDTH_TILE)*scale/2-fontMetrics.stringWidth(Integer.toString(number))/2,y+4*scale+4*scale/7);
            g.setColor(Color.BLACK);
            g.setFont(origFont);
        }
        
        
        
    }
    /**
     * Uittekenen van het object.
     * @param g het gebruikte Graphics object.
     * @param tile De tile zoals deze opgeslagen zit in het model.
     */
    public void draw(Graphics g, ITile tile,boolean b){

        draw(g,tile.getNumber(),tile.getType(),b);
        if(tile.getType()==TileType.EMPTY){
            g.drawPolygon(xPoints,yPoints,6);
        }
        if(tile.hasThief()){
//            if(tile.placeThief())         
//                    g.setColor(Color.red);   //klant wil dat de struikrover zwart blijft
             g.fillOval(x + ((2*WIDTH_BETWEEN_2_TILES + WIDTH_TILE)*scale)/2-(3*scale)/2 ,y+(5*scale)/2, 3*scale , 3*scale);
             //g.setColor(Color.black);
        }
        /*if(b){
//            g.setColor(Color.blue);
//            g.drawPolygon(xPoints, yPoints, 6);
            tile.setRolled(false);
//            g.setColor(Color.black);
        }*/
    }

    //other
    /**
     * Berekent de x waarde die hoort bij een gegeven y waarde als dit punt op de lijn ligt.
     * @param x1 Begin x-coördinaat van de lijn.
     * @param x2 Eind x-coördinaat van de lijn.
     * @param y1 Begin y-coördinaat van de lijn.
     * @param y2 Eind y-coördinaat van de lijn.
     * @param y De y-coördinaat van de lijn.
     * @return De x-coördinaat die hoort bij de y-coördinaat zodat deze op de lijn ligt.
     */
    public int calculateXOnLine(int x1,int x2,int y1,int y2,int y){
        return (y-y1)*(x2-x1)/(y2-y1)+x1;
        
    }
    /**
     * Kijkt of een gegeven punt in de tile ligt.
     * @param Mousex De x-coördinaat van het punt.
     * @param Mousey De y-coördinaat van het punt.
     * @return 
     */
    public boolean isPointInTile(int Mousex,int Mousey){
        if(Mousey>=yPoints[1]&&Mousey<=yPoints[0]){
            int x1=calculateXOnLine(xPoints[0],xPoints[1],yPoints[0],yPoints[1],Mousey); //y-y1=(y2-y1)/(x2-x1)*(x-x1) omgevormd
            int x2=calculateXOnLine(xPoints[3],xPoints[2],yPoints[3],yPoints[2],Mousey);
            return Mousex>x1&&Mousex<x2;    
        }
        else if((Mousey>=yPoints[3]&&Mousey<=yPoints[4])){
            int x1=calculateXOnLine(xPoints[0],xPoints[5],yPoints[0],yPoints[5],Mousey);
            int x2=calculateXOnLine(xPoints[4],xPoints[3],yPoints[4],yPoints[3],Mousey);
            return Mousex>x1&&Mousex<x2;
        }
        return false;
    }
    /**
     * Kijkt of een gegeven punt op een straat ligt.
     * @param x De x-coördinaat van het punt.
     * @param y De y-coördinaat van het punt.
     * @return Geeft terug of het gegeven punt op de straat ligt.
     */
    public boolean isPointOnRoad(int x, int y) {
        for(int i=0;i<6;i++){
            int y1=calculateXOnLine(yPoints[i],yPoints[(i+1)%6],xPoints[i],xPoints[(i+1)%6],x);
            if(liesBetween(xPoints[i],xPoints[(i+1)%6],x)){

            if(y>y1-scale&&y<y1+scale){
                return true;
            }


            }
        }
        return false;
    }
    /**
     * Kijkt op welke straat dit punt ligt.
     * @param x De x-coördinaat van het punt.
     * @param y De y-coördinaat van het punt.
     * @return De index van de straat waar het punt op ligt.
     *      -1 => het punt ligt niet op een straat.
     *      0,1,2,3,4,5 => de index die hoort bij de geselecteerde straat.
     */
    public int getPointOnRoad(int x, int y) {
        for(int i=0;i<6;i++){
            int y1=calculateXOnLine(yPoints[i],yPoints[(i+1)%6],xPoints[i],xPoints[(i+1)%6],x);
            if(liesBetween(xPoints[i],xPoints[(i+1)%6],x)){
                if(y>y1-scale&&y<y1+scale){
                    return i;
                }
            }
        }
        return -1;
    }
    /**
     * Kijkt of een gegeven punt op een hoekpunt van de tile ligt.
     * De hoekpunten komen overeen met locaties waar dorpen en/of steden gebouwd kunnen worden.
     * @param x De x-coördinaat van het punt.
     * @param y De y-coördinaat van het punt.
     * @return Geeft aan of een gegeven punt op een hoekpunt van de tile ligt.
     */
    public boolean isPointOnBuilding(int x, int y) {
        calculatePoints();
        for(int i=0;i<6;i++){
            if(liesBetween(xPoints[i]-scale,xPoints[i]+scale,x)){
                if(liesBetween(yPoints[i]-scale, yPoints[i]+scale,y)){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Kijkt op welk settlement dit punt ligt.
     * @param x De x-coördinaat van het punt.
     * @param y De y-coördinaat van het punt.
     * @return De index van het settlement waar het punt op ligt.
     *      -1 => het punt ligt niet op een settlement.
     *      0,1,2,3,4,5 => de index die hoort bij het geselecteerde settlement.
     */
    public int getPointOnBuilding(int x, int y) {
        calculatePoints();
        for(int i=0;i<6;i++){
            if(liesBetween(xPoints[i]-scale,xPoints[i]+scale,x)){
                if(liesBetween(yPoints[i]-scale, yPoints[i]+scale,y)){
                    return i;
                }
            }
        }
        return -1;
    }
    
    //private
    /**
     *berekent hoekpunten van de zeshoek en slaat ze op in twee rijen.
     */
    private void calculatePoints() {
        xPoints=new int[]{0,WIDTH_BETWEEN_2_TILES,WIDTH_BETWEEN_2_TILES+WIDTH_TILE,2*WIDTH_BETWEEN_2_TILES+WIDTH_TILE,WIDTH_BETWEEN_2_TILES+WIDTH_TILE,WIDTH_BETWEEN_2_TILES};
        yPoints=new int[]{Y_BETWEEN_2_TILES,0,0,Y_BETWEEN_2_TILES,2*Y_BETWEEN_2_TILES,2*Y_BETWEEN_2_TILES}; 
        for(int i=0;i<xPoints.length;i++){
            //xPoints[i]+=X_BETWEEN_2_TILES;
            xPoints[i]*=scale;
            xPoints[i]+=x;
            //yPoints[i]+=Y_BETWEEN_2_TILES;
            yPoints[i]*=scale;
            yPoints[i]+=y;
        }
        
        x = xPoints[0];
        y = yPoints[1];
    }
    /**
     * kijkt of waarde x tussen x1 en x2 ligt
     * @param x1 eerste x waarde.
     * @param x2 tweede x waarde.
     * @param x waarde die gecontroleerd moet worden.
     * @return of x wel of niet tussen x1 en x2 ligt.
     */
    private boolean liesBetween(int x1, int x2,int x) {
        return (x>x1&&x<x2)||(x<x1&&x>x2);
    }
}