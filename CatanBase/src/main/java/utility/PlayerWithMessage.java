/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author Samuel
 */
public class PlayerWithMessage {
    ArrayList<String> messages=new ArrayList<String>();
    ArrayList<Integer> players=new ArrayList<Integer>();

    public PlayerWithMessage() {
    }
    public void addMessage(String message,int player){
        messages.add(message);
        players.add(player);
    }

    public String getMessage(int i) {
        return messages.get(i);
    }

    public int getPlayer(int i) {
        return players.get(i);
    }
    public int size(){
        return messages.size();
    }
}
