/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * In deze klassen zitten een aantal constanten die gebruikt worden voor de layout te bepalen.
 * @author Jens
 */
public class Layout {
    
    //Default
    public final static Font      FONT = new Font("Verdana",Font.PLAIN,20);
    public final static Border    BORDER = BorderFactory.createLineBorder(Color.BLACK);
    public final static Color     HEADER_COLOR = Color.white;//LIGHT_GRAY;
    public final static Color     ITEM_COLOR = Color.white;
    public final static Font      NUMBERFONT = new Font("Calibri",Font.BOLD,12);  
    public final static Font      BIGFONT = new Font("Verdana",Font.PLAIN,30);
    public final static Font      HUGEFONT = new Font("Verdana",Font.BOLD,50);
    //EastPanel
    public final static Dimension EASTPNL_DIM = new Dimension(260, 200);
    
    //SructurePanel
    public final static int       STRUCTPNL_PREF_HEIGHT = 250;    
    public final static Color     STRUCTPNL_DISABLE_BACKGR = Color.gray;
    public final static Color     STRUCTPNL_ENABLE_BACKGR = Color.white;
    
    //WaitPanel
    public final static Dimension WAITPNL_DIM= new Dimension(200,100);
    
    //BankPanel
    public final static Color     BANK_LBL_FOREGR=Color.white;
    
    //CatanBord
    public final static Color     HIGHLIGHT_COLOR_CITY = Color.cyan;//new Color(156,32,16);
    public final static Color     HIGHLIGHT_COLOR_VILLAGE = Color.cyan;//new Color(156,32,16);
    public final static Color     HIGHLIGHT_COLOR_ROAD = Color.cyan;
    
    //Dices
    public final static Dimension DICES_DIM= new Dimension(120,50);
    public final static Color     DICES_FOREGR = Color.WHITE;
    public final static Border    ACTIVE_BORDER = BorderFactory.createLineBorder(Color.GREEN,3);
    public final static Border    NOT_ACTIVE_BORDER = BorderFactory.createLineBorder(Color.RED,3);
    //public final static Border    NOT_ACTIVE_BORDER = BorderFactory.createEmptyBorder(10,10,10,10);
    public final static Color     ACTIVE_BACK_COLOR = Color.GREEN;
    public final static Color     NOT_ACTIVE_BACK_COLOR = Color.RED;
    public final static Font      DICE_FONT = new Font("Calibri",Font.BOLD,12);  
    
    //PlayerPanel
    public final static Color     PLAYERNAME_FOREGR = Color.white;
    
    //StatisticsPanel
    public final static int       STATPNL_ITEM_HEIGHT = 30;
    public final static Color     STATPNL_TITLE_FOREGR = Color.white;
    public final static Color     STATPNL_HEADER_FOREGR=Color.white;
    public final static Color     STATPNL_VALUE_FOREGR=Color.white;
    public final static Border     STATPNL_CURRENT_USER=BorderFactory.createLineBorder(Color.white);

    
}
