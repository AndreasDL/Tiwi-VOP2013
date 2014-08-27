package DAO;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tom
 */
public class Game {
    public static final int LOBBY = 0;
    public static final int PLAYING = 1;
    public static final int DONE = 2;
    
    private int id;
    private int maxPlayers;
    private int status;
    private String name;
    private User Host;
    private List<User> players;//spelers met host
    private String date;//formaat yyyymmdd

    public Game(int id, int maxPlayers, String name, User Host, List<User> players, int status,String date) {
        this.id = id;
        this.maxPlayers = maxPlayers;
        this.status = status;
        this.name = name;
        this.Host = Host;
        this.players = players;
        this.date = date;
    }
    public Game(int id, int maxPlayers, String name, User Host, List<User> players,String date) {
        this.id = id;
        this.maxPlayers = maxPlayers;
        this.name = name;
        this.Host = Host;
        this.players = players;
        this.status=LOBBY;
        this.date = date;
    }
    public Game(int id, int maxPlayers, String name, User Host,String date) {
        this.id = id;
        this.maxPlayers = maxPlayers;
        this.name = name;
        this.Host = Host;
        this.status=LOBBY;
        this.players = new ArrayList<User>();
        players.add(Host);
        this.date = date;
    }
    
    
    public int getMaxPlayers() {
        return maxPlayers;
    }
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public User getHost() {
        return Host;
    }
    public void setHost(User Host) {
        this.Host = Host;
    }

    public List<User> getPlayers() {
        return players;
    }
    public void setPlayers(List<User> players) {
        this.players = players;
    }
    public void AddPlayer(User player){
        players.add(player);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public String getDate(){
        return date;
    }
}
