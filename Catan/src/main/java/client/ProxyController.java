/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import commandos.ICommand;
import java.util.ArrayList;
import model.Board;
import model.interfaces.IPlayer;
import utility.PlayerWithMessage;

/**
 *
 * @author Jens
 */
public interface ProxyController {

    public void DoCommand(ICommand command);

    public void close();

    public ArrayList<IPlayer> getAllPlayers();

    public int[] getBankValues();

    public Board getBoard();
    
    public String getCurrentPlayer();

    public int[] getMyResources(int player);    

    public IPlayer getPlayer(int playerId);

    public String[] getPlayerNames();
    
    public String[] getOtherPlayerNames(int id);
    
    public int[] getPlayerIds();
    
    public int[] getOtherPlayerIds(int id);

    public int[] getPlayerValues();

    public ArrayList<ICommand> getUpdate(int begin);

    public boolean isGameFase();

    public boolean waitForOtherPlayers();
    
    public PlayerWithMessage getChangedMessages(int begin);

    public void sendMessage(PlayerWithMessage sc);
    
    public boolean isTradeOver();
    
}
