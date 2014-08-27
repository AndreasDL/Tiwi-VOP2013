/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import controller.GameController;
import controller.IController;
import controller.PlaceNumbersController;
import controller.PlaceStructuresController;
import controller.PlaceTileController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.JPanel;
import model.Bank;
import model.Board;
import model.Player;
import model.SelectTileModel;

/**
 * Dit paneel wordt gebruikt om de verschillende fasen makkelijk te laden.
 * In dit paneel worden de verschillende panelen gegroepeerd.
 * @author Samuel
 */
public class SurroundPanel extends JPanel{
    CatanBord guiboard;
    IController controller;
    //SelectTilePanel selectPanel;
    JFrame makeBoardDemo;
    Player player;
    Bank bank;
    MessagePanel messagePanel;
 
    //constructors
    /**
     * Standaard constructor.
     */

    public SurroundPanel() {
        messagePanel=new MessagePanel(this);
        setLayout(new BorderLayout());
        //model
        SelectTileModel model = new SelectTileModel();
        
        Board board=new Board(model);
        controller = new PlaceTileController(board,model);
        //selectPanel

        SelectTilePanel selectPanel=new SelectTilePanel(model);
        model.setSelectTilePanel(selectPanel);
        add( selectPanel, BorderLayout.WEST);
        
        //catanbord
        guiboard=new CatanBord(controller);
        selectPanel.setSurroundPanel(this);
        add( guiboard, BorderLayout.CENTER);
        add( messagePanel, BorderLayout.NORTH);
    }
    /**
     * Constructor die al een fame meekrijgt.
     * @param makeBoardDemo Het gebruikte JFrame.
     */
    public SurroundPanel(JFrame makeBoardDemo) {
        this();
        this.makeBoardDemo=makeBoardDemo;
    }

    /**
     * Constructor die alle nodige panelen al meekrijgt voor de placenumberfase
     * @param messagePanel Het linker paneel.
     * @param guiboard  Het paneel dat centraal staat.
     * @param makeBoardDemo Het gebruikte JFrame.
     */
    public SurroundPanel(MessagePanel messagePanel,CatanBord guiboard,JFrame makeBoardDemo){

        this.makeBoardDemo=makeBoardDemo;

        setLayout(new BorderLayout());
        add( messagePanel, BorderLayout.NORTH);
        add( guiboard, BorderLayout.CENTER);
    }

    /**
     * Constructor die alle nodige panelen al meekrijgt.
     * @param eastPanel Het rechterpaneel.
     * @param guiboard  Het paneel dat centraal staat.
     * @param playerPanel Het linker paneel.
     * @param makeBoardDemo Het gebruikte JFrame.
     * @param bankPanel Het paneel dat onderaan staat.
     */
    public SurroundPanel(EastPanel eastPanel,CatanBord guiboard,PlayerPanel playerPanel,JFrame makeBoardDemo,BankPanel bankPanel){        
        this.makeBoardDemo=makeBoardDemo;
        makeBoardDemo.setMinimumSize(new Dimension(700,650));
        setLayout(new BorderLayout());
        add( eastPanel, BorderLayout.EAST);
        add( guiboard, BorderLayout.CENTER);
        add(playerPanel,BorderLayout.WEST);
        add(bankPanel,BorderLayout.SOUTH);
        
        //guiboard.changeController(new GameController((PlaceStructuresController)controller));
        makeBoardDemo.validate();
        
        //guiboard.changeController(new PlaceStructuresController((PlaceNumbersController)controller));
    }
    
    //functions
    /**
     * Zorgt voor de nummerplaatsing.
     */
    public void initiateNumberPlacing(){

        /**
         * Zorgt voor de nummerplaatsing.
         */
        messagePanel.initiateNumberPlacing();

        makeBoardDemo.getContentPane().removeAll();
        makeBoardDemo.setContentPane(new SurroundPanel(messagePanel,guiboard,makeBoardDemo));
        IController newController=new PlaceNumbersController((PlaceTileController)controller);
        
        //((PlaceNumbersController)newController).setLeftPanel(messagePanel);
        ((PlaceNumbersController)newController).setSurroundPanel(this);
        
        guiboard.changeController(newController);
        this.controller=newController;
        makeBoardDemo.validate();
    }
    /**
     * Initialiseren van de eerste gebouwen.
     */
    public void initiateBuildingPlacements() {
        player = new Player();
        bank=new Bank();
        player.setBank(bank);
        IController newController=new PlaceStructuresController((PlaceNumbersController)controller);
        guiboard.changeController(newController);
        makeBoardDemo.validate();
        this.controller=newController;
        controller.addPlayer(player);
        messagePanel.changeText();  
    }
    /**
     * initialiseren van de spel fase.
     */
    public void initiateGameFase() {
        makeBoardDemo.getContentPane().removeAll();
        IController newGameController=new GameController((PlaceStructuresController)controller);
        guiboard.changeController(newGameController);
        PlayerPanel playerPanel = new PlayerPanel();
        player.addObserver(playerPanel);
        BankPanel bankpanel=new BankPanel(bank);
        EastPanel eastPanel = new EastPanel(newGameController);
        eastPanel.addObserver(player);
        makeBoardDemo.setContentPane(new SurroundPanel(eastPanel,guiboard,playerPanel,makeBoardDemo,bankpanel));
        makeBoardDemo.validate();
        makeBoardDemo.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
}
