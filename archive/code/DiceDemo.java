/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package demos;

import GUI.ActionPanel;
import GUI.BankPanel;
import GUI.CatanBord;
import GUI.EastPanel;
import GUI.PlayerPanel;
import controller.GameController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import model.Bank;
import model.Board;
import model.Road;
import model.Settlement;
//import model.ModelSettlement;
import model.Player;
import model.SelectTileModel;
import utility.TileType;


/**
 *
 * @author Jens
 */
public class DiceDemo {
  public static void main(String[] args) {
      DiceDemo d = new DiceDemo();
    }
       
    public DiceDemo(){
        JFrame window = new JFrame("TestApp");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        
        
        
        //model
        SelectTileModel model = new SelectTileModel();
        Board board=new Board(model);
        board.setTile(1, 1, TileType.WOOL);
        board.setTile(2, 1, TileType.WHEAT);
        board.setTile(3, 1, TileType.WOOD);
        board.setTile(4, 1, TileType.DESERT);
        board.setTile(5, 1, TileType.STONE);
        board.setTile(1, 2, TileType.WHEAT);
        board.setTile(2, 2, TileType.STONE);
        board.setTile(3, 2, TileType.WHEAT);
        board.setTile(4, 2, TileType.IRON);
        board.setTile(5, 2, TileType.WOOL);
        board.setTile(1, 3, TileType.WOOL);
        board.setTile(2, 3, TileType.WOOD);
        board.setTile(3, 3, TileType.WOOL);
        board.setTile(4, 3, TileType.WHEAT);
        board.setTile(5, 3, TileType.WOOL);
        board.setTile(2, 4, TileType.STONE);
        board.setTile(3, 4, TileType.WOOD);
        board.setTile(4, 4, TileType.WHEAT);
        board.setTile(3, 5, TileType.IRON);
        
        board.placeNumbers(1, 1);
        
        
        
        
        GameController controller = new GameController(board);
        
        //catanbord
        CatanBord guiboard=new CatanBord(controller);        
        window.add( guiboard, BorderLayout.CENTER);
        
        
        //actionPanel
        Bank bank = new Bank();
        Player player = new Player(bank);
        EastPanel eastPanel = new EastPanel(controller);
        eastPanel.addObserver(player);
        window.add( eastPanel, BorderLayout.EAST);
        window.setMinimumSize(new Dimension(700,650));
        
        
        //resourcepanel
        
        board.addPlayer(player);
        //board.addPlayer(player);
        PlayerPanel playerPanel = new PlayerPanel();
        player.addObserver(playerPanel);
        window.add(playerPanel ,BorderLayout.WEST);
        
        //BankPanel
        BankPanel bankPanel = new BankPanel(bank);
        window.add(bankPanel,BorderLayout.SOUTH);
        
        //board.setSettlement(1,1, 5);
        board.getTile(1, 3).setSettlement(new Settlement(player), 1);
        board.getTile(0, 2).setSettlement(new Settlement(player), 5);
        board.getTile(1, 2).setSettlement(new Settlement(player), 3);
        board.getTile(2, 4).setSettlement(new Settlement(player), 1);
        board.getTile(2, 3).setSettlement(new Settlement(player), 3);
        board.getTile(1, 3).setStartRoad(new Road(player), 0);
        board.getTile(1, 2).setStartRoad(new Road(player), 3);
        board.getTile(1, 3).setStartRoad(new Road(player), 5);
        board.getTile(2, 3).setStartRoad(new Road(player), 2);
        
        Image img;
        try{
            img = ImageIO.read(new File(getClass().getResource("/icon.jpg").getFile()));
        window.setIconImage(img);
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        //window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true); 
        window.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
}
