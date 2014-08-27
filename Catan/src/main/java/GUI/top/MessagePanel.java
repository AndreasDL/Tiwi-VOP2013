/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.top;

import GUI.SurroundPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import model.Board;
import model.interfaces.IController;
import observer.IObservable;
import observer.IObserver;
import utility.PlayerWithMessage;

/**
 * Dit paneel toont instructie voor het plaatsen van de gebouwen en houdt een done knop bij
 * @author Samuel
 */

public class MessagePanel extends JPanel implements ActionListener,IObserver{
    /**
     * Dit paneel toont instructie voor het plaatsen van de gebouwen en houdt een done knop bij
     */
    String vorigText="";



    private ResourceBundle constants = ResourceBundle.getBundle("utility.language");     
    //private SurroundPanel surroundPanel;
    boolean initiateBuilding=true;
    private String s1="Plaats de startdorpen en startstraten";
    private String s2="Plaats de startdorpen en startstraten";
    
    private JTextArea area;
    private JTextPane display;
    private JButton btnsend=new JButton("zend");
    private JPanel pan=new JPanel();
    //private JTextArea lblChanges;
    private IController controller;
    //private String playerName;
    
    private int numberMessageShowed=0;
    
    private Timer tmr;
    private int DELAY = 2000; 
    /**
     * standaard constructer
     * @param surroundPanel dient om te kunnen reageren op de knop
     */
    
    
    public MessagePanel(final SurroundPanel surroundPanel, IController controller) {
        
        tmr = new Timer(DELAY, this);
        tmr.start();
        this.controller=controller;
        //middlePanel.setBorder ( new TitledBorder ( new EtchedBorder (), "Display Area" ) );

        // create the middle panel components
        setLayout(new BorderLayout() );
        display = new JTextPane();// 3,5 );
        display.setEditable ( false ); // set textArea non-editable
        //display.setPreferredSize(new Dimension(500, 50));
        JScrollPane scroll = new JScrollPane ( display );
        scroll.setPreferredSize(new Dimension(500, 50));
        scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

        //Add Textarea in to middle panel
        //setPreferredSize(new Dimension(500, 50));
        add ( scroll,BorderLayout.NORTH );
        
        pan.setLayout(new BorderLayout() );
        add(pan,BorderLayout.SOUTH);
        
        area = new JTextArea ( 1,5 );
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        //area.setEditable(false);
        area.setMaximumSize(new Dimension(100,100));
        
        int condition = JComponent.WHEN_FOCUSED;
        InputMap iMap = area.getInputMap(condition);
        ActionMap aMap = area.getActionMap();

        String enter = "enter";
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), enter);
        aMap.put(enter, new AbstractAction() {

           @Override
           public void actionPerformed(ActionEvent arg0) {
              sendmessages();
           }
        });
        
        
        JScrollPane scrollsend = new JScrollPane ( area );
        scrollsend.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollsend.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pan.add(scrollsend,BorderLayout.CENTER);
        pan.add(btnsend,BorderLayout.EAST);
        
        
        btnsend.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                sendmessages();
                
            }

            
        });

        display.setEditable(false);
        display.setMaximumSize(new Dimension(100,100));

 
        this.setVisible(true);
        
  
        
    }
    private void sendmessages() {
        String s=area.getText();
        area.setText("");
        controller.sendMessage(s);
        //controller.playCard(Integer.parseInt(area.getText()));    //easytesting
            }
    /**
     * verandert de text die getoond wordt
     */
    public void changeText(){
        //s="plaats twee dorpen met telkens ernaast een straat. een dorp plaats je door op een kruising tussen 3 tegels te klikken, een straat plaats je aan een lijn naast een dorp klik daarna op done om naar de volgende fase te gaan.";
//        area.setText(txTVillages);
    }

    void initiateNumberPlacing() {
//        area.setText(txtNumbers);
    }

    public void actionPerformed(ActionEvent e) {
        showChat();
    }

    public void setPlayerName(String playerName) {
        //this.playerName = playerName;
    }

    public void update(IObservable o, String arg) {

        if(o instanceof Board){
            
            //lblInfo.setText(((Board)o).getInfo());
            //System.out.println(((Board)o).getInfo());
                String tmp="";
                if(((Board)o).getChanges().equals("message")) {
                    if(!s1.equals(((Board)o).getInfo())){
                        tmp=((Board)o).getInfo();
                        if(!tmp.equals("")) {
                            writeMessage(((IController)o).getInfo());
                        }
                    
                            
                }
//                else if(!((Board)o).getInfo().equals("")){
//                    if(((Board)o).getChanges().equals("")){
//                    lblChanges.append(constants.getString(((Board)o).getInfo()));
//                    s=constants.getString(((Board)o).getInfo());
//                }
                else {
                    if(!s1.equals(constants.getString(((Board)o).getInfo()))){
                        tmp=constants.getString(((Board)o).getInfo());
                        if(!tmp.equals("")) {
                            writeMessage(constants.getString(((Board)o).getInfo()));
                        }
                        
                    }
//                    }
                    writeMessage(((IController)o).getInfo());
                    s1=constants.getString(((Board)o).getInfo());
            //}
        }
                if(!tmp.equals("")) {
                        s1=tmp;
                    }
        }
        }
        else 
            if(o instanceof IController){
                if(!s1.equals((((IController)o).getInfo()))){
                    
                    String tmp=(((IController)o).getInfo());
                    if(!tmp.equals("")){
                        s1=(((IController)o).getInfo());
                        writeMessage(((IController)o).getInfo());
                    }
                }
            //System.out.println(((IController)o).getInfo());
                if(!s2.equals(((IController)o).getChanges())){
                    
                    String tmp=((IController)o).getChanges();
                    if(!tmp.equals("")){
                        s2=constants.getString(((IController)o).getChanges());
                        writeMessage(((IController)o).getChanges());
                    }
                }
            
        }
    }

    private void writeMessage(int player,Color c,String message) {
        String s;
        if(player==-1) {
            s="server";
        }
        else {
            s=controller.getBoard().getPlayer(player).getName();
        }
        
            StyledDocument doc = display.getStyledDocument();
            Style style = display.addStyle("style", null);
            StyleConstants.setForeground(style, c);
            try { doc.insertString(doc.getLength(), 
                                   "\n["+new Timestamp(new java.util.Date().getTime()).getHours()+":"+
                                        new Timestamp(new java.util.Date().getTime()).getMinutes()+"] "+s+":\t"+message,
                                   style); }
            catch (BadLocationException ex){} 
    }
    private void writeMessage(String text) {
        writeMessage(-1,Color.black,text);
        
    }

    private void showChat() {
        PlayerWithMessage messages=controller.getChangedMessages(numberMessageShowed);
                numberMessageShowed+=messages.size();
                for(int i=0;i<messages.size();i++){
                    
                    
                    writeMessage(messages.getPlayer(i),controller.getBoard().getPlayer(messages.getPlayer(i)).getColor(),messages.getMessage(i));
                    //display.append(toTimeStamp(messages.getPlayer(i))+messages.getMessage(i)); 
                }
                display.setCaretPosition(display.getDocument().getLength());
    }
}
