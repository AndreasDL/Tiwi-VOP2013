/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.interfaces;


import commandos.ICommand;
import commandos.trade.TradePlayerCommand;
import java.awt.Color;
import model.Board;
import java.util.ArrayList;
import model.Bank;
import model.Dices;
import model.Spectator;
import model.TradeOffer;
import observer.IObservable;
import utility.PlayerWithMessage;

/**
 * Deze interace legt de functies van een controller vast.
 * @author Samuel
 */
public interface IController  extends IObservable{
    
    public void placeTile(int i,int j);//klik op tile in CatanBord
    
   // public void placeTile(int i,int j, TileType type);
    
    public void giveResources();
    
    public ITile getTile(int i, int j);
    
    public void clickRoad(int x , int y, int index);
    
    public void clickSettlement(int x , int y, int index);

    public void placeSettlements(ArrayList<int[]> tellers);
    
   public IPlayer getPlayerFromServer(int playerId);
   
   public void placeRoads(ArrayList<int[]> tellers);
   
   public void setBoard(Board board);
   
   public IState getState();
   
   public void setState(IState gameState); 
   
    public Board getBoard();

    public Board getClientBoard(IPlayer player);

    public void goNextPlayer();

    public String getCurrentServerPlayerName();
    
    public void startTimer();    
    
    public Bank getBank();

//    public void enableButtons(String s);
    
//    public void notifyStuff(String s);
    
    public String[] getPlayerNames();
    
    public String[] getOtherPlayerNames(int id);
    
    public int[] getPlayerIds();
    
    public int[] getOtherPlayerIds(int id);    

    public void goGameFase(int player);
    
    public String getOwnName();
    
    public Dices getDices();
    
    public void updateBank();

    public boolean waitForOtherPlayers();

    public ArrayList<IPlayer> getPlayers();
    
    public int[] getPlayerValues();

    public String getInfo();

    public String getChanges();
    
    public void setMessages(String info,String changes);
    
    public Color getOwnColor();
    
    public void tradeWithBank(int playerId,TradeOffer trade);
    
    public void tradeWithPlayer(int playerId,int otherId,TradeOffer trade);

    public void addSpectator(Spectator spec);

    public Board getSpectatorBoard();

    public void buyDevCard();

    public void playCard(int n);

    public void doCommand(ICommand command);

    public void syncResources();
    
    public ArrayList<ICommand> getUpdates();
    
    public int[] getMyResources();
    
    public void klikResource(int i);
    
    public boolean doUpdates();



    public PlayerWithMessage getChangedMessages(int numberMessageShowed);

    public void sendMessage(String s);

    public void askTrade(TradePlayerCommand aThis);


    public int getOwnId();
    
    public boolean isTradeOver();

}
