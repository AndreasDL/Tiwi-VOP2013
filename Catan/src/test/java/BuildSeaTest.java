/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import model.Bank;
import model.Board;
import model.Village;
import model.interfaces.IPlayer;
import org.junit.*;
import utility.TileType;
import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
public class BuildSeaTest {
    
    private Board board;
    private IPlayer player;   

    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    /**
     * Maakt een stukje Board aan met 2 SeaTiles en 1 gewone.
     * Op het kruispunt van de tegels wordt er een dorp geplaatst.
     *          _______ 
     *         /       \
     *        /   SEA   \_______   
     *        \   4,0   /       \
     *         \______XX  STONE  \
     *         /      XX   4,1   /
     *        /   SEA   \_______/
     *        \   5,0   /       
     *         \_______/
     * 
     */    
    @Before
    public void setUp() {
        
        //maak ServerBoard
        board =new Board(true,1);
        
        //optionpanes worden niet meer aan gebruiker bevraagd in test
        //board.setDontShowOptionPanesAgain(true);
        
        
        //stelt de tegels in
        board.setTile(4, 0, TileType.SEA);
        board.setTile(4, 1, TileType.STONE);
        board.setTile(5, 0, TileType.SEA); 
        
        
        player = board.addPlayer(1,"test");;
        board.setMaxPlayers(1);
        board.pickStartingPlayer();
        
        
        //zet 1 dorp
        board.getTile(4, 0).setSettlement(new Village(player), 4);
        board.getTile(4, 1).setSettlement(new Village(player), 0);
        board.getTile(5, 0).setSettlement(new Village(player), 2); 

               
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
     *  
     *          _______ 
     *         /       \
     *        /   SEA   \_______   
     *        \   4,0   /       \
     *         \_????_XX  STONE  \
     *         /      XX   4,1   /
     *        /   SEA   \_______/
     *        \   5,0   /       
     *         \_______/
     * 
     */     
     @Test
     public void testIfRoadBuilt() {
         
         //voeg grondstoffen toe zodat er zeker een weg gemaakt kan worden
         player.addResource(TileType.WOOD);
         player.addResource(TileType.STONE);
           
         board.checkPossibleRoad(4, 0, 4);
         board.checkPossibleRoad(5, 0, 1);
         
         //hier worden ze gezet als het mogelijk is
         ArrayList<int[]> lijst = new ArrayList<int[]>();         
         lijst.add(new int[]{4,0,4});
         lijst.add(new int[]{5,0,1});
         board.setRoads(lijst);
         
         //test als er een weg gezet is, moet null zijn want is zee
         assertNull(board.getTile(4, 0).getRoad(4));
         assertNull(board.getTile(5, 0).getRoad(1));
         
         //test als grondstoffen veranderd zijn
         assertTrue(player.getResource(TileType.WOOD)==1);
         assertTrue(player.getResource(TileType.STONE)==1);
         
         
         
     }
     
     /**
      * Test als het mogelijk is een startdorp te plaatsen op een zeetegel
      */
     @Test
     public void testIfStartSettlementBuilt() {
         

         //controleert als het mogelijk is om een te zetten
         board.checkPossibleStartSettlement(4, 0, 5);
         board.checkPossibleStartSettlement(5, 0, 1);
         
         //als het mogelijk wordt de startdorp geplaatst
         ArrayList<int[]> lijst = new ArrayList<int[]>();         
         lijst.add(new int[]{4,0,5});
         lijst.add(new int[]{5,0,1});
         board.setStartSettlements(lijst);
         
         //test als startdorp er staat
         assertNull(board.getTile(4, 0).getSettlement(5));
         assertNull(board.getTile(5, 0).getSettlement(1));
         
     }
     
}
