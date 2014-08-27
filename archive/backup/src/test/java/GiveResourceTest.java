/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import model.Bank;
import model.Board;
import model.City;
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
public class GiveResourceTest {
    Board board;
    Player player;
    Bank bank;
    
    /**
     * Maakt een deel van Board, 3 tegels met elk een grondstoftype en een nummer.
     *          _______ 
     *         /       \
     * NR=2   /   WOOL  \_______   
     *        \   2,2   /       \
     *         \_______/  WHEAT  \   NR=4
     *         /       \   2,3   /
     *        /  STONE  \_______/
     * NR=3   \   3,3   /       
     *         \_______/
     * 
     */
    public GiveResourceTest() {
        SelectTileModel model = new SelectTileModel();
        board =new Board(model);
        board.setDontShowOptionPanesAgain(true);//optionpanes worden niet meer aan gebruiker bevraagd in test
        
        board.setTile(2, 2, TileType.WOOL);
        board.getTile(2, 2).setNumber(2);
        board.setTile(3, 3, TileType.STONE);
        board.getTile(3, 3).setNumber(3);
        board.setTile(2, 3, TileType.WHEAT);
        board.getTile(2, 3).setNumber(4);
        
        bank = new Bank();
        player = new Player(bank);
        
        
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
     * Test als een speler grondstoffen krijgt als er een nederzetting staat
     * en het juiste nummer gedobbeld wordt.
     */
    @Test
    public void testGiveResourcesSettlement() {        
        
        //plaats dorp op kruispunt
        board.getTile(2, 2).setSettlement(new Village(player), 4);
        board.getTile(3, 3).setSettlement(new Village(player), 2);
        board.getTile(2, 3).setSettlement(new Village(player), 0);
        
        //geef grondstofkaarten aan tegels met nummer 2
        board.giveResources(2);        
        assert(player.getResource(TileType.WOOL)==1);
        
        //geef grondstofkaarten aan tegels met nummer 3
        board.giveResources(3);        
        assert(player.getResource(TileType.STONE)==1);
        
        //geef grondstofkaarten aan tegels met nummer 4
        board.giveResources(4);        
        assert(player.getResource(TileType.WHEAT)==1);
        
        
    }
    
    /**
     * Test als een speler het juiste aantal grondstoffen krijgt als er een Stad staat
     * en het juiste nummer gedobbeld wordt.
     */
    @Test
    public void testGiveResourcesCity() {
       
        //plaats stad op kruispunt
        board.getTile(2, 2).setSettlement(new City(player), 4);
        board.getTile(3, 3).setSettlement(new City(player), 2);
        board.getTile(2, 3).setSettlement(new City(player), 0);
        
        
        //geef grondstofkaarten aan tegels met nummer 2
        board.giveResources(2);        
        assert(player.getResource(TileType.WOOL)==2);
        
        //geef grondstofkaarten aan tegels met nummer 3
        board.giveResources(3);        
        assert(player.getResource(TileType.STONE)==2);
        
        //geef grondstofkaarten aan tegels met nummer 4
        board.giveResources(4);        
        assert(player.getResource(TileType.WHEAT)==2);
        
        
    }
}
