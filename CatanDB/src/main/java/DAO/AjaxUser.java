package DAO;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.springframework.web.util.HtmlUtils;

/**
 * Gebruikt voor Ajaxklassen, escaped naam, en geeft email,enabled en wachtwoord niet door
 * @author tom
 */
public class AjaxUser {
    //data fields
    private int id;
    private String name;
    private int gameid;
    private String rgb;
    
    //constructor
    public AjaxUser(int id, String name, int gameid,int rgb){
        this.id =id;
        this.name = HtmlUtils.htmlEscape(name);
        this.gameid = gameid;
        this.rgb = Integer.toHexString(rgb);
        this.rgb = this.rgb.substring(2, this.rgb.length());;
    }
    
    //getters
    public String getName() {
        return name;
    }

    public int getGameid() {
        return gameid;
    }
    
    public int getId() {
        return id;
    }

    public String getRgb() {
        return rgb;
    }
    
    //setters
    public void setName(String name) {
        this.name = HtmlUtils.htmlEscape(name);
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }
    
    public void setId(int id) {
        this.id = id;
    }

}
