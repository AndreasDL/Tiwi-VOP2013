/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

//import GUI.SurroundPanel;
import GUI.trade.AskTradePanel;
import client.ClientController;
import client.ProxyController;
import commandos.EndTurnCommand;
import commandos.GoGamePhaseCommand;
import commandos.ICommand;
import commandos.cards.AddKnightCommand;
import commandos.cards.AddVictoryPointCommand;
import commandos.cards.BuyDevCommand;
import commandos.cards.GiveResourceCommand;
import commandos.cards.MonopolyStealResourceCommand;
import commandos.cards.RemoveDevCommand;
import commandos.trade.TradeBankCommand;
import commandos.trade.TradePlayerCommand;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.Timer;
import model.Bank;
import model.Board;
import model.Dices;
import model.Spectator;
import model.TradeOffer;
import model.interfaces.IController;
import model.interfaces.IPlayer;
import model.interfaces.IState;
import model.interfaces.ITile;
import observer.IObservable;
import observer.IObserver;
import states.Build2RoadsState;
import states.MonopolyState;
import states.PlaceThiefState;
import states.WaitForTradeState;
import utility.DevCardType;
import utility.PlayerWithMessage;


/**
 * De controller die gebruikt wordt tijdens het spelen van het Spel.
 * Bevat een ClientController die zorgt voor de communicatie met de server.
 * Bevat ook een IState waarnaar de controller delegeert, zodat enkel bepaalde acties op het juiste moment
 * kunnen uitgevoerd worden.
 * @author Jens
 */

public class GameController implements IController , ActionListener, IObservable{
    private ResourceBundle constants = ResourceBundle.getBundle("utility.language");     
    //private boolean gameFase=false;
    private IState state;
    private ProxyController cc;
    private int DELAY = 100; 
    private Timer tmr;
    private ArrayList<IObserver> observers= new ArrayList<IObserver>();
    private Board board;
    public String info="";
    public String changes="";
   
  
    
/**
 * Initialiseert de ClientController en configureert de Timer
 * @param id unieke string die aangeeft met welk model er moet gecommuniceerd worden
 */


    public GameController(int playerId,int id, String gamePass,boolean spectator) {       
        cc=new ClientController(playerId,id,gamePass,spectator);
        //cc= new DummyController();
        tmr = new Timer(DELAY, this);
    }
    
    
    
    /**
     * Geef resources aan de Spelers die een nederzetting hebben aan een tegel met nummer <code>number</code>.
     * Het model controleert als er aan alle voorwaarden voldaan zijn.
     * Er wordt een commando doorgestuurd naar de server als er gedobbeld werd.
     */
    @Override
    public void giveResources() {
        //Dices dices = board.getDices();
        ICommand command =state.giveResources();
        
        if(command!=null) {
            //cc.DoCommand(command);
            notifyObservers("statPanel");
            //doUpdates();
            //syncResources();
        }
            
    }
    
    /**
     * Delegeert naar de huidige staat.
     * @param x x-coördinaat van een tegel
     * @param y y-coördinaat van een tegel
     * @param index index op de tegel
     */
    @Override
    public void clickRoad(int x, int y, int index) {        
            state.clickRoad(x, y, index);
    }
    
    /**
     * Geeft de plaatsen waar er mogelijk een weg moet geplaatst worden door aan de huidige IState.
     * Als het bouwen gelukt is aan clientside, wordt er een commando doorgestuurd naar de server.
     * @param tellers lijst van {x-coördinaat tegel, y-coördinaat tegel, wegindex op tegel}
     */
    @Override
    public void placeRoads(ArrayList<int[]> tellers) {         
            ICommand command =state.placeRoads(tellers);
            if(command!=null) {
                cc.DoCommand(command);
                notifyObservers("statPanel");                
            }
            doUpdates();
            syncResources();
    }
    
    /**
     * Geeft door dat er geklikt werd op een positie van een (mogelijke) weg op tegel (i,j).
     * Model controleert dan als er tegel mag gelegd worden.
     * @param x  x-coördinaat van de tegel
     * @param y  y-coördinaat van de tegel
     * @param index hoek waarop geklikt werd (startend van linkerbovenkant tussen 0 en 5)
     */
    @Override
    public void clickSettlement(int x , int y, int index){       
        state.clickSettlement(x, y, index);
    }

    /**
     * Stelt de huidige IState in.
     * @param state de nieuwe staat
     */
    @Override
    public void setState(IState state) {
        this.state = state;
    }
    
     /**
     * Geeft de plaatsen waar er mogelijk een dorp moet geplaatst worden door aan de huidige IState.
     * Als het bouwen gelukt is aan clientside, wordt er een commando doorgestuurd naar de server.
     * @param tellers lijst van {x-coördinaat tegel, y-coördinaat tegel, wegindex op tegel}
     */
    @Override
    public void placeSettlements(ArrayList<int[]> tellers) {
        ICommand command = state.placeSettlements(tellers);
        if(command!=null) {
            cc.DoCommand(command);
            doUpdates();
            syncResources();
            notifyObservers("statPanel");
            notifyObservers("repaint");
        }
    }
    
    /**
     * Niet meer gebruikt.
     * @param i
     * @param j 
     */
    @Override
    public void placeTile(int i, int j) {
        ICommand command =state.placeTile(i, j);
        if(command!=null) {
            cc.DoCommand(command);
            doUpdates();
            syncResources();
            notifyObservers("repaint");
        }
    }
     
    /**
     * Voegt de IPlayer toe aan het ServerModel
     * @param playerId een speler
     */
    @Override
    public IPlayer getPlayerFromServer(int playerId) {
        return cc.getPlayer(playerId);
       // board.addClientPlayer(player);
    }
    
    /**
     * Geeft door dat het aan de volgende Speler is.
     * Er wordt een Timer gestart zodat er periodiek kan gecheckt worden wie aan beurt is.
     * Er wordt ook overgegaan naar de volgende staat.
     */
    @Override
    public void goNextPlayer() {
        state.goNextState();
        tmr.start();
        cc.DoCommand(new EndTurnCommand(getOwnId()));
        notifyObservers("statPanel");               
    }
    
    /**
     * Geeft de naam van de speler die aan beurt is.
     * Dit gebeurt via een aanvraag aan de server.
     * @return naam van speler aan beurt
     */
    @Override
    public String getCurrentServerPlayerName() {
        return cc.getCurrentPlayer();
    }

    /**
     * Controleert wie aan de beurt is.
     * Wordt periodiek uitgevoerd. 
     * Er wordt rekening gehouden in welke fase we ons bevinden.
     * Als we in de wachtfase zitten, wordt er gewacht tot iedereen klaar is.
     * Er wordt naar de volgende IState overgegaan als de client aan beurt is.
     * Updatet incrementeel het ClientModel via commando's verkregen van het ServerModel.
     * Zorgt ook voor een repaint van het spelbord als er iets ontvangen werd.
     * @param e timerevent
     */
    public void actionPerformed(ActionEvent e) {

        if(!state.waitingGameFase()){
            if(cc.getCurrentPlayer().equals(board.getCurrentPlayerName())){
                state.goNextState();
                tmr.stop();
                board.getCurrentPlayer().notifyObservers("village");
            }       
        }
        else if(cc.isGameFase())
        {
            //gameFase=true;
            state.goNextState();
            
            //notifyStuff("all");
        }
        else if(cc.getCurrentPlayer().equals(board.getCurrentPlayerName())){
            state.goNextState();
            
            //notifyStuff("all");
        }
       
        //if(dorepaint)
        if(doUpdates()) {

            updateBank();
            syncResources();
            notifyObservers("repaint");
        }
    }   

       // updateBank();
        //cc.getBankValues();
    //}
    public boolean doUpdates(){
        ArrayList<ICommand> list= cc.getUpdate(board.getCommandSize());       
        for( ICommand cmd : list){            
            cmd.executeClient(this,board);
            board.addCommand(cmd);            
        }        
        return !list.isEmpty();
    }
    
    /**
     * Zorgt ervoor dat de timer kan gestart worden van buitenaf.
     */
    @Override
    public void startTimer() {
        tmr.start();
    }

    /**
     * Geeft de huidige IState terug.
     * @return huidige IState
     */
    @Override
    public IState getState() {
        return state;
    }

    
    public void notifyObservers(String arg) {
        for(IObserver obs : observers){
            obs.update(this, arg);
        }
    }

    @Override
    public void addObserver(IObserver obs) {
        observers.add(obs);
    }

    @Override
    public void notifyObservers() {
        for(IObserver obs : observers){
            obs.update(this, null);
        }
    }
    /*
    @Override
    public void notifyStuff(String s){
        notifyObservers(s);
        
    }
    */
    
    /**
     * Vraagt alle namen van de Spelers op die meedoen aan het spel.
     * @return alle spelernamen
     */
    @Override
    public String[] getPlayerNames() {
        return cc.getPlayerNames();
    }

    @Override
    public int[] getPlayerIds() {
        return cc.getPlayerIds();
    }
    
    @Override
    public String[] getOtherPlayerNames(int id) {
        return cc.getOtherPlayerNames(id);
    }

    @Override
    public int[] getOtherPlayerIds(int id) {
        return cc.getOtherPlayerIds(id);
    }
    
       

    /**
     * Geeft door aan het ServerModel om over te gaan naar de Gamefase.
     */

    public void goGameFase(int player){
        cc.DoCommand(new GoGamePhaseCommand(player));
        //board.getCurrentPlayer().goGameFase();

    }
    

    /**
     * Stelt het Board in van de controller.
     * @param board het modelbord
     */
    public void setBoard(Board board) {       
       this.board=board;
    }

    /**
     * Haalt het modelbord van de Server en vormt het om tot een clientversie.
     * @param player de speler
     * @return clientversie van Board
     */
    public Board getClientBoard(IPlayer player) {
        this.board= cc.getBoard();
        board.setIsServer(false);        
        board.setCurrentPlayer(player);        
        //System.out.println(board.getNumberOfPlayers());
        return board;
    }
    
    public Board getSpectatorBoard(){
        this.board= cc.getBoard();
        board.setIsServer(false);
        board.setSpectatorMode(true);
        return board;
    }
    
    /**
     * Geeft het Board van de client terug
     * @return clientboard
     */
    public Board getBoard(){
        return board;
    }

    /**
     * Geeft de bank terug die gebruikt wordt in het ClientModel.
     * @return bank
     */
    public Bank getBank() {
        return board.getBank();
    }

    
    public ITile getTile(int i, int j) {
        return board.getTile(i, j);
    }

    /**
     * Geeft de naam terug van de client.
     * @return eigen naam
     */
    @Override
    public String getOwnName() {
        return board.getCurrentPlayerName();
    }
    
    @Override
    public int getOwnId() {
        return board.getCurrentPlayerId();
    }

    /**
     * Vraagt de dobbelstenen op van het Board.
     * @return dobbelstenen
     */
    public Dices getDices() {
        return board.getDices();
    }

    /**
     * Stelt de waarden van de ClientBank in met die van de ServerBank.
     */
    public void updateBank() {
        int[] values=cc.getBankValues();
        int[] bankValues=new int[5];
        for(int i=0;i<5;i++)
            bankValues[i]=values[i];
        board.setBankValues(cc.getBankValues());
        if(board.isGameFase())  //voor de gamefase moet dit nog niet aangepast worden
            board.getCurrentPlayer().setDevCards(values[5]);
    }

    /**
     * Vraagt aan de Server hoeveel spelers er meedoen aan het spel.
     * @return aantal spelers
     */
    public boolean waitForOtherPlayers() {
        return cc.waitForOtherPlayers();
    }

    /**
     * Geeft alle spelers terug.
     * @return alle spelers
     */
    public ArrayList<IPlayer> getPlayers() {
        return board.getAllPlayers();
    }

    /**
     * Geeft het totaal aantal kaarten dat een elke speler heeft terug.
     * @return totaal kaarten van elke speler
     */
    public int[] getPlayerValues() {
        int[] playervalues=cc.getPlayerValues();
        for(int i=0; i <board.getMaxPlayers();i++){
            board.getPlayerFromPosition(i).setScore(playervalues[i+board.getMaxPlayers()]);
            if((playervalues[i+board.getMaxPlayers()])>=10){
                board.endGame();
            }
            
        }
        return playervalues;
    }
    public void setMessages(String info,String changes){
        if(info.equals("")) {
            this.info="";
        }
        else {
            this.info=constants.getString(info);
        }
        if(changes.equals("")) {
            this.changes="";
        }
        else {
            this.changes=constants.getString(changes);
        }
        notifyObservers("");
    }
    public String getInfo() {
        return info;
    }
    public String getChanges() {
        return changes;
    }
    public Color getOwnColor(){
        return board.getOwnColor();
    }
    public void tradeWithBank(int playerName, TradeOffer trade) {
        TradeBankCommand tbc = new TradeBankCommand(playerName,trade);
        cc.DoCommand(tbc);
        syncResources();
    }

    public void tradeWithPlayer(int playerId, int otherID, TradeOffer trade) {
        TradePlayerCommand tpc = new TradePlayerCommand(playerId, otherID,trade);
        notifyObservers("disablebtn");
  //      board.addCommand(tpc);
  //      tpc.executeClient(board);

        state= new WaitForTradeState(state);
        board.addCommand(tpc);

        cc.DoCommand(tpc);
        //updateBank();
    }


        
    

    
    public void addSpectator(Spectator spec) {
        //cc.addSpectator(spec);
    }
    public void buyDevCard(){
        
        ICommand command=new BuyDevCommand(board.getCurrentPlayerId(),null);
        cc.DoCommand(command);
        doUpdates();
        
        
    }
    public void playCard(int n){
        if(n==DevCardType.KNIGHT.getValue()){
            
            ICommand command=new AddKnightCommand(getOwnId());
            cc.DoCommand(command);
            //if(doUpdates()){    //zorgen dat je pas van staat veranderd wnr goedgekeurd door server
                setState(new PlaceThiefState(state));
                setMessages("placeThief", "");
            //}
        }
        else if(n==DevCardType.ROAD.getValue()){
            
            ICommand command=new RemoveDevCommand(board.getCurrentPlayerId(), DevCardType.ROAD);
            cc.DoCommand(command);
            //if(doUpdates()){
            setState(new Build2RoadsState(state));
            //}
        }
        else if(n==DevCardType.VICTORYPOINT.getValue()){
            ICommand command=new AddVictoryPointCommand(getOwnId());
            cc.DoCommand(command);
            //command=new RemoveDevCommand(board.getCurrentPlayerId(), DevCardType.VICTORYPOINT);
            //cc.DoCommand(command);
            doUpdates();
            //command.executeServer(board);
        }
        else if(n==DevCardType.RESOURCES.getValue()){
            
            ICommand command=new RemoveDevCommand(board.getCurrentPlayerId(), DevCardType.RESOURCES);
            cc.DoCommand(command);
            if(doUpdates()){
                setState(new MonopolyState(state,false));
            }
        }
        else if(n==DevCardType.MONOPOLY.getValue()){
            
            ICommand command=new RemoveDevCommand(board.getCurrentPlayerId(), DevCardType.MONOPOLY);
            cc.DoCommand(command);
            if(doUpdates()){
                setState(new MonopolyState(state,true));
            }
        }
        
        
    }


    public void doCommand(ICommand command) {
        cc.DoCommand(command);
        doUpdates();
    }

    public void syncResources() {
        if(board.isGameFase()){
                board.setMyResources(cc.getMyResources(getOwnId()));
        }
    }
    public ArrayList<ICommand> getUpdates(){
        return cc.getUpdate(board.getCommandSize());
    }

    
    public int[] getMyResources(){
        return cc.getMyResources(getOwnId());
    }
    
    public void klikResource(int i){
        if(state.toString().equals("M")){
            ICommand command=new MonopolyStealResourceCommand(getBoard().getCurrentPlayerId(),i);
            doCommand(command);
            board.setMyResources(cc.getMyResources(getOwnId()));
            state.goNextState();
        }
        else if(state.toString().equals("R")){
            MonopolyState Mstate = (MonopolyState) state;
            ICommand command=new GiveResourceCommand(getBoard().getCurrentPlayerId(),i,Mstate.getCardsLeft()-1);
            doCommand(command);
            board.setMyResources(cc.getMyResources(getOwnId()));
            state.goNextState();
        }
        

        
    }
    

    public void askTrade(TradePlayerCommand cmd) {
        AskTradePanel pnl = new AskTradePanel(cmd, board, this);
    }

    public PlayerWithMessage getChangedMessages(int numberMessageShowed) {
        return cc.getChangedMessages(numberMessageShowed);
        
    }

    public void sendMessage(String s) {
        PlayerWithMessage sc=new PlayerWithMessage();
        sc.addMessage(s, getOwnId());
        cc.sendMessage(sc);
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isTradeOver(){
        return cc.isTradeOver();
    }
    
   
    
    
}
