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
import model.SelectTileModel;
import model.Village;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utility.TileType;

/**
 *
 * @author Administrator
 */
public class BuildSeaTest {
    
    Board board;
    Player player;
    Bank bank;
    GameController controller;
    PlaceStructuresController pcontroller;
    
    /**
     * Maakt een stukje Board aan met 2 SeaTiles en 1 gewone.
     * Op het kruispunt van de tegels wordt er een dorp geplaatst.
     *          _______ 
     *         /       \
     *        /   SEA   \_______   
     *        \   4,0   /       \
     *         \_______/  STONE  \
     *         /       \   4,1   /
     *        /   SEA   \_______/
     *        \   5,0   /       
     *         \_______/
     * 
     */
    public BuildSeaTest() {
        SelectTileModel model = new SelectTileModel();
        board =new Board(model);
        board.setDontShowOptionPanesAgain(true);//optionpanes worden niet meer aan gebruiker bevraagd in test
        
        board.setTile(4, 0, TileType.SEA);        
        board.getTile(4, 0).setNumber(-2);
        board.setTile(4, 1, TileType.STONE);
        board.getTile(4, 1).setNumber(5);
        board.setTile(5, 0, TileType.SEA);
        board.getTile(5, 0).setNumber(-2);   
        
        
        
        board.getTile(4, 0).setSettlement(new Village(player), 4);
        assert(board.getTile(4, 0).getSettlement(4)!=null);
        
        board.getTile(4, 1).setSettlement(new Village(player), 0);
        assert(board.getTile(4, 1).getSettlement(0)!=null);
        
        board.getTile(5, 0).setSettlement(new Village(player), 2);
        assert(board.getTile(5, 0).getSettlement(2)!=null);
        
        bank = new Bank();
        player = new Player(bank);
        
        board.addPlayer(player);       
        
        controller = new GameController(board);
        
        pcontroller= new PlaceStructuresController(board);
        
        
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
     * Test als er een weg gebouwd wordt indien je op een zeetegel klikt en voldoende
     * resources hebt.
     */
     @Test
     public void testIfRoadBuilt() {
         //voeg grondstoffen toe zodat er zeker een weg gemaakt kan worden
         player.addResource(TileType.WOOD);
         player.addResource(TileType.STONE);
         
         //doe alsof je een weg probeert te plaatsen
         //hier worden ze gecontrolleerd
         controller.clickRoad(4 , 0, 4);
         controller.clickRoad(5 , 0, 1);
         
         //hier worden ze gezet als het mogelijk is
         ArrayList<int[]> lijst = new ArrayList<int[]>();         
         lijst.add(new int[]{4,0,4});
         lijst.add(new int[]{5,0,1});
         controller.placeRoads(lijst);
         
         //test als er een weg gezet is
         assert(board.getTile(4, 0).getRoad(4)==null);
         assert(board.getTile(5, 0).getRoad(1)==null);
         
         //test als grondstoffen veranderd zijn
         assert(player.getResource(TileType.WOOD)==1);
         assert(player.getResource(TileType.STONE)==1);
         
         
         
     }
     
     /**
      * Test als het mogelijk is een startdorp te plaatsen op een zeetegel
      */
     @Test
     public void testIfStartSettlementBuilt() {
         
         //doe alsof je een startdorp wil zetten
         //controleert als het mogelijk is om een te zetten
         pcontroller.clickSettlement(4 , 0, 5); 
         pcontroller.clickSettlement(5 , 0, 1); 
         
         //als het mogelijk wordt de startdorp geplaatst
         ArrayList<int[]> lijst = new ArrayList<int[]>();         
         lijst.add(new int[]{4,0,5});
         lijst.add(new int[]{5,0,1});
         pcontroller.placeSettlements(lijst);
         
         //test als startdorp er staat
         assert(board.getTile(4, 0).getSettlement(5)==null);
         assert(board.getTile(5, 0).getSettlement(1)==null);
         
     }
     
}
