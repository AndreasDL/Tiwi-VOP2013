/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import controller.GameController;
import controller.PlaceStructuresController;
import java.util.ArrayList;
import model.Bank;
import model.Board;
import model.Player;
import model.Road;
import model.SelectTileModel;
import model.Village;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import utility.TileType;

/**
 *
 * @author Jens
 */
public class DistanceRuleTest {
    
     Board board;
     Player player;
     Bank bank;
     GameController controller;
    
    
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
     *                  
     * 
     * 
     */
    public DistanceRuleTest() {
     SelectTileModel model = new SelectTileModel();
        board =new Board(model);
        board.setDontShowOptionPanesAgain(true);//optionpanes worden niet meer aan gebruiker bevraagd in test
        board.setTile(2, 1, TileType.WHEAT);        
        board.setTile(2, 2, TileType.WOOL);        
        board.setTile(3, 3, TileType.STONE);
        board.setTile(3, 2, TileType.WOOD);
        board.setTile(4, 2, TileType.IRON);                
        board.setTile(2, 3, TileType.WHEAT);
        
        bank = new Bank();
        player = new Player(bank);
        board.addPlayer(player);       
        controller= new GameController(board);
        
         Village settlement =new Village(player);
        board.getTile(2, 2).setSettlement(settlement, 5);
        board.getTile(3, 2).setSettlement(settlement, 3);
        board.getTile(3, 3).setSettlement(settlement, 1);
        
        
         assert(board.getTile(2, 2).getSettlement(5)!=null);
         assert(board.getTile(3, 2).getSettlement(3)!=null);
         assert(board.getTile(3, 3).getSettlement(1)!=null);   
         
         
         board.getTile(2 ,2).setRoad(new Road(player), 4);
         board.getTile(3 ,3).setRoad(new Road(player), 1);
         
         assert(board.getTile(2 ,2).getRoad( 4)!=null);
         assert(board.getTile(3 ,3).getRoad( 1)!=null);
         
         
         board.getTile(3 ,2).setRoad(new Road(player), 3);
         board.getTile(3 ,3).setRoad(new Road(player), 0);
         
         assert(board.getTile(3 ,2).getRoad( 3)!=null);
         assert(board.getTile(3 ,3).getRoad( 0)!=null);
         
         
         board.getTile(3 ,2).setRoad(new Road(player), 2);
         board.getTile(2 ,2).setRoad(new Road(player), 5);
         
         assert(board.getTile(3 ,2).getRoad( 2)!=null);
         assert(board.getTile(2 ,2).getRoad( 5)!=null);
         
         
         controller.clickRoad(3 ,2, 2); 
         controller.clickRoad(2 ,2, 5);
         
         player.addResource(TileType.WHEAT);
         player.addResource(TileType.STONE);
         player.addResource(TileType.WOOD);
         player.addResource(TileType.WOOL);
         
         
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    
    
     /**
     * Maakt een deel van Board, 7 tegels met elk een grondstoftype.
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
         controller.clickSettlement(2 ,2, 4); 
         controller.clickSettlement(2 ,3, 0); 
         controller.clickSettlement(3 ,3, 2);
         
         //als het mogelijk wordt het startdorp geplaatst
          
         ArrayList<int[]> lijst = new ArrayList<int[]>();                 
         lijst.add(new int[]{2,2,4});
         lijst.add(new int[]{2,3,0});
         lijst.add(new int[]{3,3,2});
         controller.placeSettlements(lijst);
         
         assert(board.getTile(2, 2).getSettlement(4)==null);
         assert(board.getTile(2, 3).getSettlement(0)==null);
         assert(board.getTile(3, 3).getSettlement(2)==null);
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
         controller.clickSettlement(3 ,2, 4); 
         controller.clickSettlement(4 ,2, 2); 
         controller.clickSettlement(3 ,3, 0);
         
         //als het mogelijk wordt het startdorp geplaatst
          
         ArrayList<int[]> lijst = new ArrayList<int[]>();                 
         lijst.add(new int[]{3,2,4});
         lijst.add(new int[]{4,2,2});
         lijst.add(new int[]{3,3,0});
         controller.placeSettlements(lijst);
         
         assert(board.getTile(3, 2).getSettlement(4)==null);
         assert(board.getTile(4, 2).getSettlement(2)==null);
         assert(board.getTile(3, 3).getSettlement(0)==null);
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
         controller.clickSettlement(2 ,1, 4); 
         controller.clickSettlement(2 ,2, 0); 
         controller.clickSettlement(3 ,2, 2);
         
         //als het mogelijk wordt het startdorp geplaatst
          
         ArrayList<int[]> lijst = new ArrayList<int[]>();                 
         lijst.add(new int[]{2,1,4});
         lijst.add(new int[]{2,2,0});
         lijst.add(new int[]{3,2,2});
         controller.placeSettlements(lijst);
         
         assert(board.getTile(2, 1).getSettlement(4)==null);
         assert(board.getTile(2, 2).getSettlement(0)==null);
         assert(board.getTile(3, 2).getSettlement(2)==null);
     }

     
}
