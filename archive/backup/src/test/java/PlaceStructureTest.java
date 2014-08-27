/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import controller.PlaceStructuresController;
import java.util.ArrayList;
import model.Bank;
import model.Board;
import model.Player;
import model.SelectTileModel;
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
public class PlaceStructureTest {
     Board board;
     Player player;
     Bank bank;
     PlaceStructuresController controller;
    
    
    /**
     * Maakt een deel van Board, 7 tegels met elk een grondstoftype.
     *                    _______ 
     *                   /       \
     *           _______/   WOOL  \_______   
     *          /       \   2,2   /       \
     *         /   WOOD  XX______/  WHEAT  \   
     *         \   3,2   XX      \   2,3   /
     *          \_______/  STONE  \_______/
     *          /       \   3,3   /       \
     *         /  IRON   \_______/   WOOD  \
     *         \   4,2   /       \    3,4  /
     *          \_______/  STONE  \_______/
     *                  \   4,3   /
     *                   \_______/
     * 
     * 
     */
    public PlaceStructureTest() {
     SelectTileModel model = new SelectTileModel();
        board =new Board(model);
        board.setDontShowOptionPanesAgain(true);//optionpanes worden niet meer aan gebruiker bevraagd in test
        
        board.setTile(2, 2, TileType.WOOL);        
        board.setTile(3, 3, TileType.STONE);
        board.setTile(3, 2, TileType.WOOD);
        board.setTile(4, 2, TileType.IRON);
        board.setTile(4, 3, TileType.STONE);
        board.setTile(3, 4, TileType.WOOD);
        board.setTile(2, 3, TileType.WHEAT);
        
        bank = new Bank();
        player = new Player(bank);
        board.addPlayer(player);       
        controller= new PlaceStructuresController(board);
        
         controller.clickSettlement(2 ,2, 5); 
         controller.clickSettlement(3 ,2, 3); 
         controller.clickSettlement(3 ,3, 1);
         
         //als het mogelijk wordt de startdorp geplaatst
         ArrayList<int[]> lijst = new ArrayList<int[]>();         
         lijst.add(new int[]{2,2,5});
         lijst.add(new int[]{3,2,3});
         lijst.add(new int[]{3,3,1});
         controller.placeSettlements(lijst);
         
         //test als startdorp er staat
         assert(board.getTile(2, 2).getSettlement(5)!=null);
         assert(board.getTile(3, 2).getSettlement(3)!=null);
         assert(board.getTile(3, 3).getSettlement(1)!=null);        
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
    
         /*
     *                    _______ 
     *                   /       \
     *           _______/   WOOL  \_______   
     *          /       \   2,2   /       \
     *         /   WOOD  XX_???__/  WHEAT  \   
     *         \   3,2   XX      \   2,3   /
     *          \_______/  STONE  \_______/
     *          /       \   3,3   /       \
     *         /  IRON   \_______/   WOOD  \
     *         \   4,2   /       \    3,4  /
     *          \_______/  STONE  \_______/
     *                  \   4,3   /
     *                   \_______/

*/
     @Test
     public void testRoad1() {        
         controller.clickRoad(2 ,2, 4); 
         controller.clickRoad(3 ,3, 1);   
       
         ArrayList<int[]> lijst = new ArrayList<int[]>();        
         lijst.add(new int[]{2,2,4});
         lijst.add(new int[]{3,3,1});
         controller.placeRoads(lijst);
         
         assert(board.getTile(2, 2).getRoad(4)!=null);
         assert(board.getTile(3, 3).getRoad(1)!=null); 
     }
     
          /*
     *                    _______ 
     *                   /       \
     *           _______/   WOOL  \_______   
     *          /       \   2,2   /       \
     *         /   WOOD  XX______/  WHEAT  \   
     *         \   3,2   XX      \   2,3   /
     *          \_______?  STONE  \_______/
     *          /       \   3,3   /       \
     *         /  IRON   \_______/   WOOD  \
     *         \   4,2   /       \    3,4  /
     *          \_______/  STONE  \_______/
     *                  \   4,3   /
     *                   \_______/

*/
     @Test
     public void testRoad2() {         
         controller.clickRoad(3 ,2, 3); 
         controller.clickRoad(3 ,3, 0);   
       
         ArrayList<int[]> lijst = new ArrayList<int[]>();        
         lijst.add(new int[]{3,2,3});
         lijst.add(new int[]{3,3,0});
         
         controller.placeRoads(lijst);
         
         assert(board.getTile(3, 2).getRoad(3)!=null);
         assert(board.getTile(3, 3).getRoad(0)!=null);
     }
     
     
          /*
     *                    _______ 
     *                   /       \
     *           _______/   WOOL  \_______   
     *          /       ?   2,2   /       \
     *         /   WOOD  XX______/  WHEAT  \   
     *         \   3,2   XX      \   2,3   /
     *          \_______/  STONE  \_______/
     *          /       \   3,3   /       \
     *         /  IRON   \_______/   WOOD  \
     *         \   4,2   /       \    3,4  /
     *          \_______/  STONE  \_______/
     *                  \   4,3   /
     *                   \_______/

*/
      @Test
     public void testRoad3() {         
         controller.clickRoad(3 ,2, 2); 
         controller.clickRoad(2 ,2, 5);   
       
         ArrayList<int[]> lijst = new ArrayList<int[]>();        
         lijst.add(new int[]{3,2,2});
         lijst.add(new int[]{2,2,5});
         controller.placeRoads(lijst);
         
         assert(board.getTile(3, 2).getRoad(2)!=null);
         assert(board.getTile(2, 2).getRoad(5)!=null);
     }
     
 
                 /*
     *                    _______ 
     *                   /       \
     *           _______/   WOOL  \_______   
     *          /       \   2,2   /       \
     *         /   WOOD  XX______/  WHEAT  \   
     *         \   3,2   XX      \   2,3   /
     *          \_______/  STONE  \_______/
     *          /       \   3,3   /       \
     *         /  IRON   \______??   WOOD  \
     *         \   4,2   /      ??    3,4  /
     *          \_______/  STONE  \_______/
     *                  \   4,3   /
     *                   \_______/

*/
     @Test 
     public void test2ndSettlement1() {
         ArrayList<int[]> lijst = new ArrayList<int[]>();         
         lijst.add(new int[]{3,3,4});
         lijst.add(new int[]{3,4,0});
         lijst.add(new int[]{4,3,2});
         controller.placeSettlements(lijst);
         
         assert(board.getTile(3, 3).getSettlement(4)==null);
         assert(board.getTile(3, 4).getSettlement(0)==null);
         assert(board.getTile(4, 3).getSettlement(2)==null);
     }

      
           /*
     *                    _______ 
     *                   /       \
     *           _______/   WOOL  \_______   
     *          /       \   2,2   /       \
     *         /   WOOD  XX_XXX__/  WHEAT  \   
     *         \   3,2   XX      \   2,3   /
     *          \_______/  STONE  \_______/
     *          /       \   3,3   /       \
     *         /  IRON   \______??   WOOD  \
     *         \   4,2   /      ??    3,4  /
     *          \_______/  STONE  \_______/
     *                  \   4,3   /
     *                   \_______/

*/
     @Test 
     public void testResources2ndSettlement() {
         controller.clickRoad(2 ,2, 4); 
         controller.clickRoad(3 ,3, 1);   
       
         ArrayList<int[]> lijst = new ArrayList<int[]>();        
         lijst.add(new int[]{2,2,4});
         lijst.add(new int[]{3,3,1});
         controller.placeRoads(lijst);
         
         assert(board.getTile(2, 2).getRoad(4)!=null);
         assert(board.getTile(3, 3).getRoad(1)!=null); 
         
         //probeer nog een dorp te zetten
         controller.clickSettlement(3 ,3, 4); 
         controller.clickSettlement(3 ,4, 0); 
         controller.clickSettlement(4 ,3, 2);
         
         //als het mogelijk wordt de startdorp geplaatst
          
         
         lijst.clear();
         lijst.add(new int[]{3,3,4});
         lijst.add(new int[]{3,4,0});
         lijst.add(new int[]{4,3,2});
         controller.placeSettlements(lijst);
         
         assert(board.getTile(3, 3).getSettlement(4)!=null);
         assert(board.getTile(3, 4).getSettlement(0)!=null);
         assert(board.getTile(4, 3).getSettlement(2)!=null);
         
         
         //controlleer als speler grondstoffen van 2de nederzetting krijgt
         assert(player.getResource(TileType.STONE)==2);
         assert(player.getResource(TileType.WOOD)==1);
     }
     
     
     
     /*
     *                    _______ 
     *                   /       \
     *           _______/   WOOL  \_______   
     *          /       \   2,2   /       \
     *         /   WOOD  XX_XXX_??  WHEAT  \   
     *         \   3,2   XX     ??   2,3   /
     *          \_______/  STONE  \_______/
     *          /       \   3,3   /       \
     *         /  IRON   \_______/   WOOD  \
     *         \   4,2   /       \    3,4  /
     *          \_______/  STONE  \_______/
     *                  \   4,3   /
     *                   \_______/

*/
     @Test 
     public void test2ndSettlement2() {
         
         controller.clickRoad(2 ,2, 4); 
         controller.clickRoad(3 ,3, 1);   
       
         ArrayList<int[]> lijst = new ArrayList<int[]>();        
         lijst.add(new int[]{2,2,4});
         lijst.add(new int[]{3,3,1});
         controller.placeRoads(lijst);
         
         assert(board.getTile(2, 2).getRoad(4)!=null);
         assert(board.getTile(3, 3).getRoad(1)!=null); 
         
         //probeer nog een dorp te zetten
         controller.clickSettlement(2 ,2, 4); 
         controller.clickSettlement(2 ,3, 0); 
         controller.clickSettlement(3 ,3, 2);
         
         //als het mogelijk wordt het startdorp geplaatst
          
         lijst.clear();         
         lijst.add(new int[]{2,2,4});
         lijst.add(new int[]{2,3,0});
         lijst.add(new int[]{3,3,2});
         controller.placeSettlements(lijst);
         
         assert(board.getTile(2, 2).getSettlement(4)==null);
         assert(board.getTile(2, 3).getSettlement(0)==null);
         assert(board.getTile(3, 3).getSettlement(2)==null);
     }
     
     
     
                /*
     *                    _______ 
     *                   /       \
     *           _______/   WOOL  \_______   
     *          /       \   2,2   /       \
     *         /   WOOD  XX_XXX__/  WHEAT  \   
     *         \   3,2   XX      \   2,3   /
     *          \_______?  STONE  \_______/
     *          /       \   3,3   /       \
     *         /  IRON   \______XX   WOOD  \
     *         \   4,2   /      XX    3,4  /
     *          \_______/  STONE  \_______/
     *                  \   4,3   /
     *                   \_______/

*/
     @Test 
     public void test2ndRoad1() {
         controller.clickRoad(2 ,2, 4); 
         controller.clickRoad(3 ,3, 1);   
       
         ArrayList<int[]> lijst = new ArrayList<int[]>();        
         lijst.add(new int[]{2,2,4});
         lijst.add(new int[]{3,3,1});
         controller.placeRoads(lijst);
         
         assert(board.getTile(2, 2).getRoad(4)!=null);
         assert(board.getTile(3, 3).getRoad(1)!=null); 
         
         //probeer nog een dorp te zetten
         controller.clickSettlement(3 ,3, 4); 
         controller.clickSettlement(3 ,4, 0); 
         controller.clickSettlement(4 ,3, 2);
         
         //als het mogelijk wordt de startdorp geplaatst
          
         
         lijst.clear();
         lijst.add(new int[]{3,3,4});
         lijst.add(new int[]{3,4,0});
         lijst.add(new int[]{4,3,2});
         controller.placeSettlements(lijst);
         
         assert(board.getTile(3, 3).getSettlement(4)!=null);
         assert(board.getTile(3, 4).getSettlement(0)!=null);
         assert(board.getTile(4, 3).getSettlement(2)!=null);
         
         
         //controlleer als speler grondstoffen van 2de startdorp krijgt
         assert(player.getResource(TileType.STONE)==2);
         assert(player.getResource(TileType.WOOD)==1);
         
         controller.clickRoad(3 ,2, 3); 
         controller.clickRoad(3 ,3, 0);   
       
         lijst.clear();
         lijst = new ArrayList<int[]>();        
         lijst.add(new int[]{3,2,3});
         lijst.add(new int[]{3,3,0});
         controller.placeRoads(lijst);
         
         assert(board.getTile(3, 2).getRoad(3)==null);
         assert(board.getTile(3, 3).getRoad(0)==null);
     }

                     /*
     *                    _______ 
     *                   /       \
     *           _______/   WOOL  \_______   
     *          /       ?   2,2   /       \
     *         /   WOOD  XX_XXX__/  WHEAT  \   
     *         \   3,2   XX      \   2,3   /
     *          \_______/  STONE  \_______/
     *          /       \   3,3   /       \
     *         /  IRON   \______XX   WOOD  \
     *         \   4,2   /      XX    3,4  /
     *          \_______/  STONE  \_______/
     *                  \   4,3   /
     *                   \_______/

*/
     @Test 
     public void test2ndRoad2() {
         controller.clickRoad(2 ,2, 4); 
         controller.clickRoad(3 ,3, 1);   
       
         ArrayList<int[]> lijst = new ArrayList<int[]>();        
         lijst.add(new int[]{2,2,4});
         lijst.add(new int[]{3,3,1});
         controller.placeRoads(lijst);
         
         assert(board.getTile(2, 2).getRoad(4)!=null);
         assert(board.getTile(3, 3).getRoad(1)!=null); 
         
         //probeer nog een dorp te zetten
         controller.clickSettlement(3 ,3, 4); 
         controller.clickSettlement(3 ,4, 0); 
         controller.clickSettlement(4 ,3, 2);
         
         //als het mogelijk wordt de startdorp geplaatst
          
         
         lijst.clear();
         lijst.add(new int[]{3,3,4});
         lijst.add(new int[]{3,4,0});
         lijst.add(new int[]{4,3,2});
         controller.placeSettlements(lijst);
         
         assert(board.getTile(3, 3).getSettlement(4)!=null);
         assert(board.getTile(3, 4).getSettlement(0)!=null);
         assert(board.getTile(4, 3).getSettlement(2)!=null);
         
         
         //controlleer als speler grondstoffen van 2de dorp krijgt
         assert(player.getResource(TileType.STONE)==2);
         assert(player.getResource(TileType.WOOD)==1);        
          
       
         controller.clickRoad(3 ,2, 2); 
         controller.clickRoad(2 ,2, 5);   
       
         lijst.clear();   
         lijst.add(new int[]{3,2,2});
         lijst.add(new int[]{2,2,5});
         controller.placeRoads(lijst);
         
         assert(board.getTile(3, 2).getRoad(2)==null);
         assert(board.getTile(2, 2).getRoad(5)==null);
     }


                          /*
     *                    _______ 
     *                   /       \
     *           _______/   WOOL  \_______   
     *          /       \   2,2   /       \
     *         /   WOOD  XX_XXX__/  WHEAT  \   
     *         \   3,2   XX      \   2,3   /
     *          \_______/  STONE  \_______/
     *          /       \   3,3   /       \
     *         /  IRON   \__??__XX   WOOD  \
     *         \   4,2   /      XX    3,4  /
     *          \_______/  STONE  \_______/
     *                  \   4,3   /
     *                   \_______/

*/
     @Test 
     public void test2ndRoad3() {
         controller.clickRoad(2 ,2, 4); 
         controller.clickRoad(3 ,3, 1);   
       
         ArrayList<int[]> lijst = new ArrayList<int[]>();        
         lijst.add(new int[]{2,2,4});
         lijst.add(new int[]{3,3,1});
         controller.placeRoads(lijst);
         
         assert(board.getTile(2, 2).getRoad(4)!=null);
         assert(board.getTile(3, 3).getRoad(1)!=null); 
         
         //probeer nog een dorp te zetten
         controller.clickSettlement(3 ,3, 4); 
         controller.clickSettlement(3 ,4, 0); 
         controller.clickSettlement(4 ,3, 2);
         
         //als het mogelijk wordt de startdorp geplaatst
          
         
         lijst.clear();
         lijst.add(new int[]{3,3,4});
         lijst.add(new int[]{3,4,0});
         lijst.add(new int[]{4,3,2});
         controller.placeSettlements(lijst);
         
         assert(board.getTile(3, 3).getSettlement(4)!=null);
         assert(board.getTile(3, 4).getSettlement(0)!=null);
         assert(board.getTile(4, 3).getSettlement(2)!=null);
         
         
         //controlleer als speler grondstoffen van 2de dorp krijgt
         assert(player.getResource(TileType.STONE)==2);
         assert(player.getResource(TileType.WOOD)==1);        
          
       
         controller.clickRoad(3 ,3, 4); 
         controller.clickRoad(4 ,3, 2);   
       
         lijst.clear();   
         lijst.add(new int[]{3,3,4});
         lijst.add(new int[]{4,3,2});
         
         try{
            controller.placeRoads(lijst);
         }
         catch(NullPointerException npe){
         
         }
         
         assert(board.getTile(3, 3).getRoad(4)!=null);
         assert(board.getTile(4, 3).getRoad(2)!=null);
     }

     
}

