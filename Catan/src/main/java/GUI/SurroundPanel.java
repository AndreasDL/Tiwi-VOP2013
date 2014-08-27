/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GUI.top.MessagePanel;
import GUI.bot.SouthPanel;
import GUI.west.PlayerPanel;
import GUI.east.DiceButton;
import GUI.east.EastPanel;
import GUI.west.StatisticsPanel;
import commandos.ICommand;
import controller.GameController;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import model.Bank;
import model.Board;
import model.interfaces.IController;
import model.interfaces.IPlayer;
import observer.IObservable;
import observer.IObserver;
import states.OtherFirstBuildingState;
import states.PlaceStructureState;
import states.SpectatorState;


/**
 * Dit paneel wordt gebruikt om de verschillende fasen makkelijk te laden.
 * In dit paneel worden de verschillende panelen gegroepeerd.
 * @author Samuel
 */
public class SurroundPanel extends JPanel implements IObserver, ComponentListener{
    private CatanBord guiboard;
    private IController controller;
    //SelectTilePanel selectPanel;
    private JFrame makeBoardDemo;
    private IPlayer player;
    private Bank bank;
    private MessagePanel messagePanel;
    private EastPanel eastPanel;
    private int playerId;
    private int gameId;
    private String gamePass;
    private boolean spectator;
    private Image Origimg;
    private Image rescaledimg;
    private JPanel pan;
    private Board board;
    private EndGameFrame ePanel;
    private boolean gamephase=false;

        /**
     * Constructor die al een fame meekrijgt.
     * @param makeBoardDemo Het gebruikte JFrame.
     */
    public SurroundPanel(JFrame makeBoardDemo,int playerId,int gameId,String gamepass,boolean spectator) {
        
        try {
            Origimg =  ImageIO.read(getClass().getResource("/wall.jpg"));
        } catch (IOException ex) {
            Logger.getLogger(SurroundPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.playerId=playerId;
        this.gameId=gameId;
        this.gamePass = gamepass;
        this.makeBoardDemo=makeBoardDemo;
        this.spectator=spectator;
        setOpaque(false);        
        createGame();
        addComponentListener(this);
        ToolTipManager.sharedInstance().setDismissDelay(5000);
    }
    private void createGame(){
        setLayout(new BorderLayout());
        
        controller = new GameController(playerId,gameId,gamePass,spectator);
       
        if(!spectator){
            player = controller.getPlayerFromServer(playerId);
            if(player != null){
                //controller.setMaxPlayers(maxPlayers);
                createPlayerGUI();
            }
            else{
                System.out.println("Max players reached. Entering Spectator Mode.");
                createSpectator();
            }
        }
        else{
            createSpectator();            
        }
    }
    private void createPlayerGUI(){
            pan=new JPanel();
            pan.setLayout(new BorderLayout());

            waitForOtherPlayers();

            board= controller.getClientBoard(player); 
            
            ePanel = new EndGameFrame(makeBoardDemo);
            board.addObserver(ePanel);
            //board.endGame();
            
            PlaceStructureState state=new PlaceStructureState(board,controller);        
            state.addObserver(this);

            bank=controller.getBank();
            player.setBank(bank);
            //controller.setOtherPlayers();
            
            
            guiboard=new CatanBord(controller);
            board.addObserver(guiboard);
            controller.addObserver(guiboard);

             messagePanel=new MessagePanel(this,controller);
             controller.addObserver(messagePanel);
             board.addObserver(messagePanel);
             //messagePanel.setPlayerName(player.getName());
            //selectPanel.setSurroundPanel(this);



            pan.add( guiboard, BorderLayout.CENTER);
            pan.add(messagePanel, BorderLayout.NORTH);
            pan.setOpaque(false);            

            add( pan, BorderLayout.CENTER);
            //add( messagePanel, BorderLayout.NORTH);
            //board.placeField();
            eastPanel = new EastPanel(controller);

            boolean myTurn = controller.getCurrentServerPlayerName().equals(player.getName());
            if(!myTurn){
                eastPanel.disableDices();
            }
            eastPanel.addObserver(player);
            add( eastPanel, BorderLayout.EAST);
            player.notifyObservers();

            
            if(board.getCommandSize()==0){ //normale start als er geen commands zijn
                if(myTurn){
                        controller.setState(state);
                        board.getCurrentPlayer().notifyObservers("village");
                }
                else{
                    controller.setState(new OtherFirstBuildingState(state));
                    controller.startTimer();
                }
            }
            else{//rebuild state via laatste commandos                    
                controller.setState(state);
                ICommand lastCommand = board.getCommand(board.getCommandSize()-1);
                lastCommand.restoreState(controller,board.getCommandSize()-2,myTurn); 
            }
            if(board.isGameFase()){
                initiateGameFase();                
            }
            
     }
     
     private void createSpectator(){
            //Spectator spec = new Spectator(playerName);
            //controller.addSpectator(spec); 

            waitForOtherPlayers();
           
            
            board= controller.getSpectatorBoard(); 
            
            controller.setState(new SpectatorState(controller, board));           
            
            
            guiboard=new CatanBord(controller);
            board.addObserver(guiboard);
            controller.addObserver(guiboard);            
            
            bank=controller.getBank();
            SouthPanel bankPanel=new SouthPanel(bank,controller);
            add(bankPanel,BorderLayout.SOUTH);
            
            JPanel pnl = new JPanel();
            pnl.setLayout(new GridLayout(0, 1));
            DiceButton dices= new DiceButton(controller);    
            pnl.add(dices);
            StatisticsPanel stats = new StatisticsPanel(controller);
            controller.addObserver(stats);
            pnl.add(stats);
            pnl.add(Box.createVerticalGlue());
            add(pnl,BorderLayout.EAST);
            
            add( guiboard, BorderLayout.CENTER);
            
            
            
            controller.startTimer();
     }

     private void waitForOtherPlayers(){
            WaitPanel waitpanel = new WaitPanel();
            while(controller.waitForOtherPlayers()){            
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SurroundPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            waitpanel.dispose();
     }
    
    
    /**
     * initialiseren van de spel fase.
     */
    public void initiateGameFase() {
        if(!gamephase){
            eastPanel.goNextFase();

            //makeBoardDemo.getContentPane().removeAll();


            
            SouthPanel bankPanel=new SouthPanel(bank,controller);
            
            PlayerPanel playerPanel = new PlayerPanel(controller,bankPanel);
            playerPanel.update(player,null);
            player.addObserver(playerPanel);

            //add( guiboard, BorderLayout.CENTER);
            add(playerPanel,BorderLayout.WEST);
            pan.add(bankPanel,BorderLayout.SOUTH);
            pan.repaint();
            pan.invalidate();

            repaint();
            //makeBoardDemo.setContentPane(new SurroundPanel(eastPanel,guiboard,playerPanel,makeBoardDemo,bankpanel));
            player.notifyObservers();
            makeBoardDemo.validate();
            //makeBoardDemo.setExtendedState(Frame.MAXIMIZED_BOTH);
            gamephase=true;
        }
    }


    public void update(IObservable o, String arg) {
        this.initiateGameFase();
    }

    /*
    @Override
    protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(img,0,0,null);
    }
     */
    @Override
    protected void paintComponent(Graphics g) {      
      if(rescaledimg == null || getWidth()!=rescaledimg.getWidth(null)||getHeight()!=rescaledimg.getHeight(null)){
            g.drawImage(Origimg.getScaledInstance(getWidth(), getHeight(),Image.SCALE_SMOOTH ),0,0,null);
        }
      g.drawImage(rescaledimg, 0, 0, null);
    }
    
    

    public void componentResized(ComponentEvent e) {
        
        rescaledimg = Origimg.getScaledInstance(getWidth(), getHeight(),Image.SCALE_SMOOTH );
    }

    public void componentMoved(ComponentEvent e) {
        
    }

    public void componentShown(ComponentEvent e) {
        
    }

    public void componentHidden(ComponentEvent e) {
        
    }
    
    
}
