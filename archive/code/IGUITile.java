/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.GUITiles;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Whatever
 */
public interface IGUITile {

    int getX();
    void setX(int x);
    int getY();
    void setY(int y);

    void setBorder(boolean border);
    public boolean hasborder();

    void setColor(Color c);
    Color getColor();

    void setScale(int scale);

    
    void draw(Graphics g,int nummer,int color,boolean selected);  
    //public void draw(Graphics g);
    
    public int calculateXOnLine(int x1,int x2,int y1,int y2,int y);
    public boolean isPointInTile(int Mousex,int Mousey);
    public boolean isPointOnStreet(int x, int y);
    public boolean isPointOnBuilding(int x, int y);
    
    public int getDiameter();
    public boolean isSeaType();//geeft terug of een tegel al den niet op een zeeveldmoet liggen
}
