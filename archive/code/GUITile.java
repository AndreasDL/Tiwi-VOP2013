package GUI.GUITiles;


import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.ResourceBundle;
import model.interfaces.IModelTile;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Samuel
 */
public class GUITile implements IGUITile{
    /**
     * Deze klasse tekent de tegels van het Catan bord als zeshoeken. 
     * De types worden voorgesteld door een kleurencode.
     */
    private ResourceBundle constanten = ResourceBundle.getBundle("utility.config");
    int scale=15;
    final int WIDTH_TILE=4;          //grootte onderste zijde
    final int WIDTH_BETWEEN_2_TILES=2;    //x afstand onderste zijde tot hoek
    final int Y_BETWEEN_2_TILES=4;          //y afstand 2 tegels
    final int groottecircel=3;
    final Color[] colors={Color.orange,Color.green,Color.red,Color.lightGray,new Color(156, 93, 82),Color.yellow,Color.blue,Color.white};//de new Color=brown=hout
    //Color colorvakje;
    boolean border;
   
    private int x;
    private int y;
    
    int[] xPoints;              //de x waarde vd hoekpunten vd tegels
    int[] yPoints;
    
    Color color=Color.white;
    private boolean isSeaType; //houd bij of het vak al dan niet op de zee moet liggen
    
    //constructors
    public GUITile() {
        /**
         * Standaard constructor.
         */
        isSeaType = false;
    }
    public GUITile(IGUITile selectedTile) {
        /**
         * Maakt een kopie van de meegegeven tile.
         */
        this();
        this.color=selectedTile.getColor();
        this.border=selectedTile.hasborder();
        this.isSeaType = selectedTile.isSeaType();
    }

    //draw Methods
    @Override
    public void draw(Graphics g,int nummer,int color,boolean selected) {

        /**
         * Teken de tegel.
         * g => graphics.
         * nummer = nummer van de tegel. (Bij het maken van het bord is dit het aantal dat er nog over zijn).
         *      nummer = -1 => Rover staat op deze tegel
         *      nummer = -2 => Geen nummer tekenen
         * (instelbaar in config file)
         * t => TileType (enum). Bepaalt welke prent getekend wordt.
         */

        calculatePoints();
        
        g.setColor(colors[color]);
        g.fillPolygon(xPoints,yPoints,6);
        
        
            g.setColor(Color.black);
            if(false){ //later activeren om roads te kunnen tekenen
                drawStreets(g);
            }
            g.drawPolygon(xPoints,yPoints,6);
            
        g.setColor(Color.black);
            if (nummer!=Integer.parseInt(constanten.getString("noDrawNumber"))) //kijk of je nummer moet tekenen
                g.drawString(""+nummer,GetMiddleX()-scale, GetMiddleY()); //nummer grondstoffen tekenen
            if (nummer==Integer.parseInt(constanten.getString("thiefNumber")))  //kijk of je rover moet tekenen
                g.fillOval((xPoints[3]+x)/2-groottecircel*scale/2,(yPoints[5]+y)/2-groottecircel*scale/2, groottecircel*scale, groottecircel*scale);
            //struikrover tekenen

    }
    public void draw(Graphics g, IModelTile tile) {
        /**
         * Teken de tegel.
         * Haalt de het nummer en het type uit de modelTile.
         * (zie draw(Graphics g, int nummer, TileType t) voor meer info)
         */
        calculatePoints();
        int number;
        int tileType;
        number = tile.getNumber();
        tileType=tile.getType().getValue();
                
        if(tile.hasThief()){
                    number=-1;                   
        }              
        g.setColor(colors[tileType]);
        g.fillPolygon(xPoints,yPoints,6);
        g.setColor(Color.black);
        if(false){ //later activeren om roads te kunnen tekenen
            drawStreets(g);
        }
        
        g.drawPolygon(xPoints,yPoints,6); 
        g.setColor(Color.black);
            if (number!=Integer.parseInt(constanten.getString("noDrawNumber"))) //kijk of je nummer moet tekenen
                g.drawString(""+number,GetMiddleX()-scale, GetMiddleY()); //nummer grondstoffen tekenen
            if (number==Integer.parseInt(constanten.getString("thiefNumber")))  //kijk of je rover moet tekenen
                g.fillOval((xPoints[3]+x)/2-groottecircel*scale/2,(yPoints[5]+y)/2-groottecircel*scale/2, groottecircel*scale, groottecircel*scale);
       ArrayList<Integer> cities=tile.getSettlementsIndices();
       /*for(int i=0;i<cities.size();i++){
           drawCities(g,i);
       }*/
        
    }

    //getters & setters
    @Override
    public void setBorder(boolean border) {
        /**
         * Stelt in of de rand moet getekend worden.
         */
        this.border=border;
    }
    public boolean hasborder() {
        /**
         * Toont of de tile wel of geen rand tekent.
         */
        return border;
    }
    @Override
    public int getX() {
        /**
         * Geeft de x-coördinaat van de linkerbovenhoek.
         */
        return x;
    }
    @Override
    public void setX(int x) {
        /**
         * Zet de x-coördinaat van de linkerbovenhoek.
         */
        this.x = x;
        calculatePoints();
    }
    @Override
    public int getY() {
        /**
         * Geeft de y-coördinaat van de linkerbovenhoek.
         */
        return y;
    }
    @Override
    public void setY(int y) {
        /**
         * Zet de y-coördinaat van de linkerbovenhoek.
         */
        this.y = y;
        calculatePoints();
    }
    
    @Override
    public void setScale(int scale) {
        /**
         * Stelt de schaal in. De schaal is de diameter/10.
         */
        this.scale=scale;
        calculatePoints();
    }
    @Override
    public void setColor(Color c) {
        /**
         * Stelt de kleur in die gebruikt wordt om de tile op te vullen.
         */
        color=c;
    }
    @Override
    public Color getColor() {
        /**
         * Geeft de ingestelde kleur terug.
         */
        return color;
    }
    public int getDiameter(){
        /**
         * Geeft de diameter van de tile terug in pixels.
         */
        return xPoints[3]-xPoints[0];
    }
    public int getHeight(){
        /**
         * Geeft de hoogte van de tile terug in pixels.
         */
        return yPoints[4]-yPoints[1];
    }
    public boolean isSeaType(){
        /**
         * Geeft terug of de tegel een zeeType is.
         * Als dit true is dan mag de tegel op de plaats van een zee gelegd worden.
         * Dit is het geval bij de zee zelf en havens.
         */
        return isSeaType;
    } 
    protected void setSeaType(boolean isSeaType){
        /**
         * Stelt het zeeType in. 
         * Indien het zeeType op true staat kan men deze tegel op water plaatsen.
         * Dit is zo bij haven-tegels en zee-tegels.
         */
        this.isSeaType = isSeaType;
    }
    public int[] getxPoints() {
        /**
         * Geeft de tabel terug met alle x-coördinaten van de zeshoek.
         */
        return xPoints;
    }
    public int[] getyPoints() {
        /**
         * Geeft de tabel terug met alle y-coördinaten van de zeshoek.
         */
        return yPoints;
    }

    //other
    public int calculateXOnLine(int x1,int x2,int y1,int y2,int y){
        /**
         * geeft x-waarde van overeenkomstige y-waarde op een lijn gegeven door 2 punten (gaat ook van y->y)
         */
        return (y-y1)*(x2-x1)/(y2-y1)+x1;
    }
    public boolean isPointInTile(int Mousex,int Mousey){    
        /**
         * Kijkt of een gegeven punt in de tile ligt.
         */
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
    public boolean isPointOnStreet(int x, int y) {  
        /**
         * Kijkt of het gegeven punt op een straat ligt.
         */
        for(int i=0;i<6;i++){
            int y1=calculateXOnLine(yPoints[i],yPoints[(i+1)%6],xPoints[i],xPoints[(i+1)%6],x);
            if(liesBetween(xPoints[i],xPoints[(i+1)%6],x)){
            if(y>y1-scale&&y<y1+scale){
                //System.out.println("ligt op lijn tussen"+ i+"  op tile "+eerstewaarde+"  "+tweedewaarde  );
                return true;
            }
            }
        }
        return false;
    }
    public int getPointOnStreet(int x, int y) {  
        /**
         * Kijkt op welke straat dit punt ligt.
         * -1 => niet op een straat
         * 0,1,2,3,4,5 => de index die hoort bij de aangeklikte straat.
         */
        for(int i=0;i<6;i++){
            int y1=calculateXOnLine(yPoints[i],yPoints[(i+1)%6],xPoints[i],xPoints[(i+1)%6],x);
            if(liesBetween(xPoints[i],xPoints[(i+1)%6],x)){
                if(y>y1-scale&&y<y1+scale){
                    //System.out.println("ligt op lijn tussen"+ i+"  op tile "+eerstewaarde+"  "+tweedewaarde  );
                    return i;
                }
            }
        }
        return -1;
    }
    public boolean isPointOnBuilding(int x, int y) { 
        /**
         * Kijkt of een gegeven punt op een hoekpunt van de tile ligt.
         * De hoekpunten komen overeen met locaties waar dorpen en/of steden gebouwd kunnen worden.
         */
        calculatePoints();
        for(int i=0;i<6;i++){
            if(liesBetween(xPoints[i]-scale,xPoints[i]+scale,x)){
                if(liesBetween(yPoints[i]-scale, yPoints[i]+scale,y)){
                    //System.out.println("ligt op Building  "+ i+"  op tile "+eerstewaarde+"  "+tweedewaarde  );
                    return true;
                }
            }
        }
        return false;
    }
    public int getPointOnBuilding(int x, int y) { //kijkt of het opgegeven punt op een hoekpunt lig
        /**
         * Kijkt op welk hoekpunt/dorp/stad dit punt ligt.
         * -1 => niet op een hoekpunt/dorp/stad
         * 0,1,2,3,4,5 => de index die hoort bij het aangeklikt hoekpunt.
         */
        calculatePoints();
        for(int i=0;i<6;i++){
            if(liesBetween(xPoints[i]-scale,xPoints[i]+scale,x)){
                if(liesBetween(yPoints[i]-scale, yPoints[i]+scale,y)){
                    //System.out.println("ligt op Building  "+ i+"  op tile "+eerstewaarde+"  "+tweedewaarde  );
                    return i;
                }
            }
        }
        return -1;
    }
   
    //private
    private boolean liesBetween(int x1, int x2,int x) {
        return (x>x1&&x<x2)||(x<x1&&x>x2);
    }
    private int GetMiddleX(){
        return (xPoints[3]+xPoints[0])/2;
    }
    private int GetMiddleY(){
        return (yPoints[5]+yPoints[1])/2;
    } 
    private void calculatePoints() {
        //berekent hoekpunt dmv schaal en linkerbovenhoek omvattende rechthoek
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
    }
    private void drawCities(Graphics g, int i) {
        g.fillRect(xPoints[i], yPoints[i], scale, scale);
    }
    private void drawStreet(Graphics g,int locatie){
        int y1=yPoints[locatie];
                    int y2=yPoints[(locatie+1)%6];
                    int x1=xPoints[locatie];
                    int x2=xPoints[(locatie+1)%6];
                    int[] xvalues;
                    int[] yvalues;
                    if(y1==y2){ //alleen y waarde moet veranderen, horizontale lijn
                        int scaleRoad=(int) (scale/2);
                        xvalues=new int[]{x1,x1,x2,x2};
                        yvalues=new int[]{y1-scaleRoad,y1+scaleRoad,y2+scaleRoad,y2-scaleRoad};
                    }
                    else{
                        int scaleRoad=scale/4;
                        int ydraw1=y1-scaleRoad;
                        int ydraw2=y1+scaleRoad;
                        int ydraw3=y2+scaleRoad;
                        int ydraw4=y2-scaleRoad;
                        int x1_1=(ydraw1-y1)*-1*(y2-y1)/(x2-x1)+x1;
                        int x2_2=(ydraw2-y1)*-1*(y2-y1)/(x2-x1)+x1;
                        int x3_3=(ydraw3-y2)*-1*(y1-y2)/(x1-x2)+x2;
                        int x4_4=(ydraw4-y2)*-1*(y1-y2)/(x1-x2)+x2;
                        xvalues=new int[]{x1_1,x2_2,x3_3,x4_4};
                        yvalues=new int[]{ydraw1,ydraw2,ydraw3,ydraw4};
                    }
                    g.fillPolygon(xvalues,yvalues,4);
    }
    private void drawStreets(Graphics g){ //berekend 4 punten en tekent rechthoek
        for(int i=0;i<6;i++){
                    drawStreet(g,i);
        }
    }
}
