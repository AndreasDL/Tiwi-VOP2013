/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.util.ArrayList;
import model.Bank;
import model.Board;
import model.Player;
import model.Road;
import model.Village;
import model.interfaces.IPlayer;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utility.TileType;

/**
 *
 * @author Jens
 */

public class DistanceRuleTest {
    
     private Board board;
     private IPlayer player;    

    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    /**
     * Maakt een deel van Board, 7 tegels met elk een grondstoftype.
     * 
     *           _______
     *          /       \
     *         /  WHEAT  \_______ 
     *         \   2,1   /       \
     *          \_______/   WOOL  \_______   
     *          /       X   2,2   /       \
     *         /   WOOD  XX_XXX__/  WHEAT  \   
     *         \   3,2   XX      \   2,3   /
     *          \_______X  STONE  \_______/
     *          /       \   3,3   /
     *         /  IRON   \_______/ 
     *         \   4,2   /
     *          \_______/ 
     *                   
     */
    @Before
    public void setUp() {        
        board =new Board(true,1);
        //board.setDontShowOptionPanesAgain(true);//optionpanes worden niet meer aan gebruiker bevraagd in test
        board.setTile(2, 1, TileType.WHEAT);        
        board.setTile(2, 2, TileType.WOOL);        
        board.setTile(3, 3, TileType.STONE);
        board.setTile(3, 2, TileType.WOOD);
        board.setTile(4, 2, TileType.IRON);                
        board.setTile(2, 3, TileType.WHEAT);

        player = board.addPlayer(1,"test");;
        
               
        
        Village settlement =new Village(player);
        board.getTile(2, 2).setSettlement(settlement, 5);
        board.getTile(3, 2).setSettlement(settlement, 3);
        board.getTile(3, 3).setSettlement(settlement, 1);
         
         Road road = new Road(player);
         board.getTile(2 ,2).setRoad(road, 4);
         board.getTile(3 ,3).setRoad(road, 1);
         
         road = new Road(player);
         board.getTile(3 ,2).setRoad(road, 3);
         board.getTile(3 ,3).setRoad(road, 0);         
         
         road = new Road(player);
         board.getTile(3 ,2).setRoad(road, 2);
         board.getTile(2 ,2).setRoad(road, 5);
         
         player.addResource(TileType.WHEAT);
         player.addResource(TileType.STONE);
         player.addResource(TileType.WOOD);
         player.addResource(TileType.WOOL);
    }
    
    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    
    
     /**
     * Test als het mogelijk is om op de gemarkeerde plek met vraagtekens een dorp te plaatsen
     * 
     *           _______
     *          /       \
     *         /  WHEAT  \_______ 
     *         \   2,1   /       \
     *          \_______/   WOOL  \_______   
     *          /       X   2,2   /       \
     *         /   WOOD  XX_XXX_??  WHEAT  \   
     *         \   3,2   XX     ??   2,3   /
     *          \_______X  STONE  \_______/
     *          /       \   3,3   /
     *         /  IRON   \_______/ 
     *         \   4,2   /
     *          \_______/ 
     *                   
     *                  
     * 
     * 
     */
     @Test
     public void testSettlement1() {
         board.checkPossibleSettlement(2, 2, 4);
         board.checkPossibleSettlement(2, 3, 0);
         board.checkPossibleSettlement(3, 3, 2);
         
         //als het mogelijk wordt het startdorp geplaatst
          
         ArrayList<int[]> lijst = new ArrayList<int[]>();                 
         lijst.add(new int[]{2,2,4});
         lijst.add(new int[]{2,3,0});
         lijst.add(new int[]{3,3,2});
         board.setSettlements(lijst);
         
         assertNull(board.getTile(2, 2).getSettlement(4));
         assertNull(board.getTile(2, 3).getSettlement(0));
         assertNull(board.getTile(3, 3).getSettlement(2));
     }
     
     
          /**
     * Maakt een deel van Board, 7 tegels met elk een grondstoftype.
     * 
     *           _______
     *          /       \
     *         /  WHEAT  \_______ 
     *         \   2,1   /       \
     *          \_______/   WOOL  \_______   
     *          /       X   2,2   /       \
     *         /   WOOD  XX_XXX__/  WHEAT  \   
     *         \   3,2   XX      \   2,3   /
     *          \_______X  STONE  \_______/
     *          /      XX   3,3   /
     *         /  IRON   \_______/ 
     *         \   4,2   /
     *          \_______/ 
     *                   
     *                  
     * 
     * 
     */
     @Test
     public void testSettlement2() {   
         board.checkPossibleSettlement(3, 2, 4);
         board.checkPossibleSettlement(4, 2, 2);
         board.checkPossibleSettlement(3, 3, 0);
         
         //als het mogelijk wordt het startdorp geplaatst          
         ArrayList<int[]> lijst = new ArrayList<int[]>();                 
         lijst.add(new int[]{3,2,4});
         lijst.add(new int[]{4,2,2});
         lijst.add(new int[]{3,3,0});
         board.setSettlements(lijst);
         
         assertNull(board.getTile(3, 2).getSettlement(4));
         assertNull(board.getTile(4, 2).getSettlement(2));
         assertNull(board.getTile(3, 3).getSettlement(0));
     }

          /**
     * Maakt een deel van Board, 7 tegels met elk een grondstoftype.
     * 
     *           _______
     *          /       \
     *         /  WHEAT  \_______ 
     *         \   2,1   /       \
     *          \______XX   WOOL  \_______   
     *          /       X   2,2   /       \
     *         /   WOOD  XX_XXX__/  WHEAT  \   
     *         \   3,2   XX      \   2,3   /
     *          \_______X  STONE  \_______/
     *          /       \   3,3   /
     *         /  IRON   \_______/ 
     *         \   4,2   /
     *          \_______/ 
     *                   
     *                  
     * 
     * 
     */
     @Test
     public void testSettlement3() {
         board.checkPossibleSettlement(2, 1, 4);
         board.checkPossibleSettlement(2, 2, 0);
         board.checkPossibleSettlement(3, 2, 2);
         //als het mogelijk wordt het startdorp geplaatst
          
         ArrayList<int[]> lijst = new ArrayList<int[]>();                 
         lijst.add(new int[]{2,1,4});
         lijst.add(new int[]{2,2,0});
         lijst.add(new int[]{3,2,2});
         board.setSettlements(lijst);
         
         assertNull(board.getTile(2, 1).getSettlement(4));
         assertNull(board.getTile(2, 2).getSettlement(0));
         assertNull(board.getTile(3, 2).getSettlement(2));
     }

     
}
