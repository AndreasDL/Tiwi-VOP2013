
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.east;

import GUI.east.CustomLabel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.FilteredImageSource;
import java.io.IOException;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import model.interfaces.IController;
import model.interfaces.IPlayer;
import observer.IObservable;
import observer.IObserver;
import utility.ImageRecolor;
import utility.Layout;
import utility.Structure;
import utility.TileType;

/**
 *Toont het aantal Structures die de Speler nog kan zetten.
 * Maakt hiervoor gebruik van Observer pattern.
 * @author Jens
 */


public class StructurePanel extends JPanel implements IObserver{
    

    //private ResourceBundle constants = ResourceBundle.getBundle("language_nl.config"); 

    private ResourceBundle constants = ResourceBundle.getBundle("utility.language"); 

    private JLabel[] structureValues = new JLabel[Structure.values().length+1];
    private StructureInfoPanel infoPanel;
    private final int[] INIT_VALUE={2,2,0,20};
    private IController controller;
    private JPanel[] resourceCounts=new JPanel[4];
    private JLabel[] buildings=new JLabel[4];
    private boolean gamefase=false;
    private boolean road=false;
    private String s="a";
    /**
     * Maakt een verticaal rooster aan die toont hoeveel constructies de Speler nog kan doen.
     */
    public StructurePanel(IController controller) {
        setOpaque(false);
        this.controller=controller;
        infoPanel = new StructureInfoPanel();
        setLayout(new BorderLayout());    
        add(infoPanel,BorderLayout.CENTER);
        //setMinimumSize(new Dimension(Layout.EAST_PANEL_DIM.width,Layout.STRUCT_PANEL_MIN_HEIGHT));
        //setMaximumSize(new Dimension(Layout.EAST_PANEL_DIM.width,Layout.STRUCT_PANEL_MAX_HEIGHT));
        setPreferredSize(new Dimension(Layout.EASTPNL_DIM.width,Layout.STRUCTPNL_PREF_HEIGHT));
       
    }

    /**
     * Wordt opgeroepen als het aantal constructies aangepast werd in Player.
     * Zorgt ervoor dat het rooster met gegevens aangepast wordt.
     * @param o n.v.t
     * @param arg Object int[]{aantal wegen over, aantal dorpen over, aantal steden over}
     */
    public void update(Observable o, Object arg) {
        if(arg != null){
            infoPanel.valuesChanged((int[]) arg);
        }
    }
    public void goNextFase(){
        for(int i=0;i<4;i++) {
           // resourceCounts[i].setVisible(true);
            gamefase=true;
        }
    }
    
    public void update(IObservable o, String arg) {



        IPlayer player = (IPlayer) o;
        road=false;
        if(!player.isOtherPlayerState()){
        if(!gamefase){
            if(player.getSettlementsLeft()==0&&player.getRoadsLeft()==0){    //als men in waitgamestatezit alles deselecten
                buildings[0].setEnabled(false);
                buildings[1].setEnabled(false);
            }
            else if(arg!=null){
                System.out.println(arg);
                if(arg.equals("road")){
                    buildings[0].setEnabled(true);
                    buildings[1].setEnabled(false);
                    controller.setMessages("placeRoad", "");
                }
                else if(arg.equals("village")){
                    buildings[0].setEnabled(false);
                    buildings[1].setEnabled(true);
                    controller.setMessages("PlaceVillage", "");
                }
            }
        }
            else{
                for(int i=0;i<4;i++) {
                    buildings[i].setEnabled(false);
                }
            if(player.getRoads()){
                road=true;
                controller.getBoard().highlightRoads(false,true);
                for(int i=1;i<4;i++) {
                    buildings[i].setEnabled(false);
                }
                    buildings[0].setEnabled(true);

            }
                buildings[2].setEnabled(true);
                if(!player.buildCityPossible()||structureValues[1].getText().equals("5")) {
                    buildings[2].setEnabled(false);
                }
                else {
                    //controller.setMessages("placeCity", "");
                }

                if(gamefase&&!player.buildRoadPossible()){
                        //||(!gamefase&&((!buildings[1].isEnabled()&&player.getSettlementsLeft()!=0)||player.getRoadsLeft()==0))) {
                    buildings[0].setEnabled(false);
                    //s="begin";
                }
                else{
                    buildings[0].setEnabled(true);
//                    if(!gamefase) {
//                        controller.setMessages("placeRoad", "");
//
//                    }
                }

                if((gamefase&&!player.buildSettlementPossible())||!controller.getBoard().highlight(1,!gamefase)){
                        //||(!gamefase&&((buildings[0].isEnabled())||player.getSettlementsLeft()==0))) //voor wisselen straat dorp startfase
                
                    buildings[1].setEnabled(false);
                    //s="begin";
                }
                else{
//                    if(!gamefase) {
//                        controller.setMessages("PlaceVillage", "");
//                    }
                    buildings[1].setEnabled(true);
                    controller.getBoard().deselectHighlight();
                }

                if(!player.devCardPossible()||!gamefase) {
                    buildings[3].setEnabled(false);
                }
                else {
                    buildings[3].setEnabled(true);
                }

            
        }
        
        }
        else{
            for(int i=0;i<4;i++) {
                buildings[i].setEnabled(false);
            }
        }

        infoPanel.valuesChanged(player.getStructures());
            
    
    }

    
   
    /**
     * Verticaal rooster met constructiegegevens.
     */
    private class StructureInfoPanel extends JPanel{
        private String[] imgUrls = {"/settlements/road.png"
                                    ,"/settlements/settlement.png"
                                    ,"/settlements/city.png"
                                    ,"/devCards/BACK.png"};
        public StructureInfoPanel() {      
           ToolTipManager.sharedInstance().setInitialDelay(0);
           setLayout(new GridLayout(4, 1));           
           setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.BLACK));           
           JPanel structurePanel;
           JPanel infoPanel;
           JPanel pnlResourceCount;
           setOpaque(false);
           for( int i=0; i < imgUrls.length ; i++){
               structurePanel = new JPanel(new BorderLayout());
               structurePanel.setOpaque(false);
               //structurePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
               infoPanel = new JPanel(new GridLayout(1, 2));
               infoPanel.setOpaque(false);
              // infoPanel.setBackground(Color.black);
               infoPanel.setBorder(BorderFactory.createLineBorder(Color.black));               
               
               //Imagelabel van structure
               JLabel lblImage = createImageLabel(i);
               buildings[i]=lblImage;
               
               controller.setMessages("PlaceVillage", "");

               //aantal structures nog mogelijk
               JLabel lblValue = new CustomLabel(INIT_VALUE[i],i);
                       /*= new JLabel(){
                   
                   @Override
                   public JToolTip createToolTip() {
                       ImageTooltip itt = new ImageTooltip();
                       itt.setComponent(this);
                       return itt;
                   }
                   
               };
               lblValue.setText(Integer.toString(INIT_VALUE[i]));
               lblValue.setFont(Layout.FONT);
               lblValue.setOpaque(true);
               lblValue.setBackground(Layout.ITEM_COLOR);
               lblValue.setHorizontalAlignment(JLabel.CENTER);*/
               //lblValue.setToolTipText("<html><img scr=\"file:/fullCards/WOOD.jpg\"> 1x");
               lblValue.setToolTipText("");
               
               structureValues[i]=lblValue;
               structureValues[i].setFont(Layout.BIGFONT);
               
               //add
               infoPanel.add(lblImage,BorderLayout.CENTER);
               infoPanel.add(lblValue,BorderLayout.EAST);


               //Paneel met recept om te bouwen
               pnlResourceCount = createRecipePanel(i);
               pnlResourceCount.setOpaque(false);
               pnlResourceCount.setVisible(false);
               gamefase=false;
               resourceCounts[i]=pnlResourceCount;
               
               //add
               structurePanel.add(infoPanel,BorderLayout.CENTER);
               structurePanel.add(pnlResourceCount,BorderLayout.SOUTH);
               add(structurePanel);
           }
        }
        
        private JLabel createImageLabel(int i){
            Image icon=null;
                try {
                    icon = ImageIO.read(getClass().getResource(imgUrls[i]));
                } catch (IOException ex) {
                    Logger.getLogger(StructurePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
               final Image img =createImage(new FilteredImageSource(icon.getSource(), new ImageRecolor(controller.getOwnColor())));
            JLabel tmp = new JLabel(){                   
                   @Override
                   protected void paintComponent(Graphics g) {
                       super.paintComponent(g);
                       int imgWidth=img.getWidth(null);
                       int imgHeight=img.getHeight(null);
                       int width= this.getWidth();
                       int height = this.getHeight();
                       g.drawImage(img, width/2-imgWidth/2, height/2-imgHeight/2, null);
                   }

                   @Override
                   public void setEnabled(boolean enabled) {
                       super.setEnabled(enabled);
                       if(enabled) {
                           setBackground(Layout.STRUCTPNL_ENABLE_BACKGR);
                       }
                       else {
                           setBackground(Layout.STRUCTPNL_DISABLE_BACKGR);
                       }
                   }
            };
            //tmp.setFont(Layout.BIGFONT);
            tmp.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
            tmp.setBackground(Layout.HEADER_COLOR);
            tmp.setHorizontalAlignment(JLabel.CENTER);
            tmp.setOpaque(true);            
            tmp.setName(""+i);
            tmp.setEnabled(false);
            tmp.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(((JLabel)e.getComponent()).isEnabled()) {
                            doHighlight((JLabel)e.getComponent());
                        }
                    }

            });
            return tmp;
        }
        
        private JLabel createResourceLabel(TileType type){
            JLabel rl = null;
            try {
                rl = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("/fullCards/"+type+".jpg")).getScaledInstance(20, 33, Image.SCALE_DEFAULT)));
            } catch (IOException ex) {
                Logger.getLogger(StructurePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            return rl;
        }
        
        private JPanel createRecipePanel(int i){
            JPanel resourceCount = new JPanel();
            // resourceCount.setLayout(new GridLayout(0,4));
             resourceCount.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
             //JPanel resourceCount2 = new JPanel();
             //resourceCount2.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
            
            if(i == 0){//1 WOOD + 1 STONE
                resourceCount.add(new JLabel("1 x "));                     
                resourceCount.add(createResourceLabel(TileType.WOOD));                     
                resourceCount.add(new JLabel("1 x "));
                resourceCount.add(createResourceLabel(TileType.STONE));
            }
            else if(i==1){//1 WOOD + 1 STONE + 1 WHEAT + 1 WOOL
                resourceCount.add(new JLabel("1 x "));
                resourceCount.add(createResourceLabel(TileType.WOOD));
                resourceCount.add(new JLabel("1 x "));
                resourceCount.add(createResourceLabel(TileType.STONE));
                resourceCount.add(new JLabel("1 x "));
                resourceCount.add(createResourceLabel(TileType.WHEAT));
                resourceCount.add(new JLabel("1 x "));
                resourceCount.add(createResourceLabel(TileType.WOOL));  
            }
            else if(i==2){//2 WHEAT + 3 Iron
                resourceCount.add(new JLabel("2 x "));
                resourceCount.add(createResourceLabel(TileType.WHEAT));
                resourceCount.add(new JLabel("3 x "));
                resourceCount.add(createResourceLabel(TileType.IRON));                    
            }
            else if(i==3){                
                resourceCount.add(new JLabel("1 x "));                     
                resourceCount.add(createResourceLabel(TileType.IRON));                     
                resourceCount.add(new JLabel("1 x "));
                resourceCount.add(createResourceLabel(TileType.WOOL));
                resourceCount.add(new JLabel("1 x "));
                resourceCount.add(createResourceLabel(TileType.WHEAT));
            }
            return resourceCount;
        }
        
        

        private void doHighlight(JLabel label) {
            
            for(int i=0;i<3;i++){
                System.out.println("highlight"+i);
                if(label.getName().equals(""+i)) {
                    if(road&&i==0)
                        controller.getBoard().highlightRoads(false,true);
                    else {
                        controller.getBoard().highlight(i,!gamefase);
                    }
                }
            }
            if(label.getName().equals(""+3)){
                controller.buyDevCard();
            }
       }      
        /**
         * Verandert de waarden die weergegeven worden naar de nieuwe waarden.
         * @param newValues {aantal wegen over, aantal dorpen over, aantal steden over}
         */
        public void valuesChanged(int[] newValues){
            for( int i=0;i<newValues.length;i++){
                structureValues[i].setText(Integer.toString(newValues[i])); 
            }
        }
    }   
}


