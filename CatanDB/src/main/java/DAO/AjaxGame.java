package DAO;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.List;
import org.springframework.web.util.HtmlUtils;

/**
 * Gebruikt voor Ajaxklassen, escaped gamenaam, en gebruikt AjaxUser
 * @author tom
 */
public class AjaxGame {
    private int id;
    private int maxPlayers;
    private int status;
    private String name;
    private AjaxUser host;
    private List<AjaxUser> players;//spelers met host

    public AjaxGame(int id, int maxPlayers, String name, AjaxUser Host, List<AjaxUser> players, int status) {
        this.id = id;
        this.maxPlayers = maxPlayers;
        this.status = status;
        this.name = HtmlUtils.htmlEscapeHex(name);
        this.host = Host;
        this.players = players;
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
        this.name = HtmlUtils.htmlEscapeHex(name);
    }

    public AjaxUser getHost() {
        return host;
    }
    public void setHost(AjaxUser Host) {
        this.host = Host;
    }

    public List<AjaxUser> getPlayers() {
        return players;
    }
    public void setPlayers(List<AjaxUser> players) {
        this.players = players;
    }
    public void AddPlayer(AjaxUser player){
        players.add(player);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
