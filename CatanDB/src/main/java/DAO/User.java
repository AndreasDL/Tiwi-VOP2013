package DAO;

import java.awt.Color;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Andreas De Lille
 */
public class User {
    //data fields
    private String name;
    private String pass;
    private String salt;
    private String mail;
    private int gameid;
    private boolean enabled;
    private boolean admin;
    private boolean blocked;
    private int id;
    private Color color;
    private String gamePass;
    
    //constructor
    public User(int id,String name,String pass,String salt,String mail, int gameid,int rgb, boolean enabled,boolean admin,boolean blocked){
        this.id = id;
        this.name = name;
        this.pass = pass;
        this.salt = salt;
        this.mail = mail;
        this.gameid = gameid;
        this.color= new Color(rgb);
        this.enabled = enabled;
        this.admin = admin;
        this.blocked = blocked;
    }
    
    //constructor
    public User(int id,String name,String pass,String salt,String mail, int gameid,int rgb, boolean enabled,boolean admin,boolean blocked,String gamePass){
        this.id = id;
        this.name = name;
        this.pass = pass;
        this.salt = salt;
        this.mail = mail;
        this.gameid = gameid;
        this.color= new Color(rgb);
        this.enabled = enabled;
        this.admin = admin;
        this.blocked = blocked;
        this.gamePass = gamePass;
    }
    
    //getters
    public String getName(){
        return name;
    }
    public String getMail(){
        return mail;
    }
    public String getPass(){//hashed value
        return pass;
    }
    public String getSalt(){
        return salt;
    }
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    //other
    public boolean checkPass(String pass){
        return this.pass.equals(pass);
    }
    @Override
    public String toString() {
        return name; //To change body of generated methods, choose Tools | Templates.
    }
    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public boolean isAdmin(){
        return admin;
    }
    public boolean isBlocked(){
        return blocked;
    }
    public void setBlocked(boolean blocked){
        this.blocked = blocked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGamePass() {
        return gamePass;
    }

    public void setGamePass(String gamePass) {
        this.gamePass = gamePass;
    }
    
}
