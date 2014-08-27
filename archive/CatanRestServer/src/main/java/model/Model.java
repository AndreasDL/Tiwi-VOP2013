package model;


import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jens
 */

public class Model {
    private String id;
    private String text ;
    private ArrayList<IPlayer> players = new ArrayList<IPlayer>();
    private int currentPlayer=0;
    

    public Model(String id,String text) {
        this.text = text;
        this.id=id;
        players.add(new ConcretePlayer("Samuel"));
        players.add(new Player("Jens"));
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    public IPlayer getCurrentPlayer(){
        return players.get(currentPlayer);
    }
    
     public IPlayer getPlayer(int index){
        return players.get(index);
    }
     
     public ArrayList<IPlayer> getPlayers(){
         return players;
     }
    
    
    
}
