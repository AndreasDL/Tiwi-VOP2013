package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author Andreas De Lille
 */
public class DAO {
    private ResourceBundle constanten;

    public DAO() throws ClassNotFoundException {
        constanten = ResourceBundle.getBundle("databankconstanten");
        Class.forName("oracle.jdbc.driver.OracleDriver");
    }
    
    public List<User> getUsers() {
        List<User> users = null;

        try {
            Connection conn = openConnection();
            try {
                Statement stmt = conn.createStatement();
                try {
                    ResultSet rs = stmt.executeQuery(constanten.getString("select_users"));
                    users = new ArrayList<User>();
                    while (rs.next()) {
                        users.add(createUser(rs));
                    }
                } finally {
                    stmt.close();
                }
            } finally {
                conn.close();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return users;
    }        
    public boolean addUser(User user) {
        boolean resultaat = false;
        try{
            Connection conn = openConnection();
            try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("insert_user"));
                try{
                    //insert into users (id,name,pass,salt,mail,game,enabled,admin,blocked) values (?,?,?,?,?,?,?,?,?)
                    int i = 1;
                    stmt.setString(i,user.getName());
                    i++;
                    stmt.setString(i,user.getPass());
                    i++;
                    stmt.setString(i,user.getSalt());
                    i++;
                    stmt.setString(i,user.getMail());
                    i++;
                    stmt.setInt(i,user.getGameid());
                    i++;
                    if ( user.isEnabled()){
                        stmt.setInt(i,1);
                    }else{
                        stmt.setInt(i,0);
                    }
                    i++;
                    if (user.isAdmin()){
                        stmt.setInt(i,1);
                    }else{
                        stmt.setInt(i,0);
                    }
                    i++;
                    if(user.isBlocked()){
                        stmt.setInt(i,1);
                    }else{
                        stmt.setInt(i,0);
                    }
                    i++;
                    
                    stmt.executeUpdate();
                    //mislukt als een kolom null is aangezien ze als not null staan ingesteld
                    resultaat = true;
                }finally{
                    stmt.close();
                }
            } finally{
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return resultaat;
    }
    public boolean addFacebookUser(int id, String name) {
        boolean resultaat = false;
        try{
            Connection conn = openConnection();
            try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("insert_facebookuser"));
                try{
                    stmt.setString(1,name);
                    stmt.setInt(2,id);
                    stmt.executeUpdate();
                    //mislukt als een kolom null is aangezien ze als not null staan ingesteld
                    resultaat = true;
                }finally{
                    stmt.close();
                }
            } finally{
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return resultaat;
    }
    public User getFacebookUser(int id){
        User user = null;
        try{
            Connection conn = openConnection();
            try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("get_facebookuser"));
                try{
                    stmt.setInt(1,id);
                    ResultSet rs = stmt.executeQuery();
                    while(rs.next()){
                        user = createUser(rs);
                    }
                }finally{
                    stmt.close();
                }
            } finally{
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return user;
    }
    
    public User getUser(String userName){
        User user = null;
        try{
            Connection conn = openConnection();
            try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("get_user"));
                try{
                    stmt.setString(1,userName);
                    ResultSet rs = stmt.executeQuery();
                    while(rs.next()){
                        user = createUser(rs);
                    }
                }finally{
                    stmt.close();
                }
            } finally{
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return user;
    }
    public User getUserByMail(String userMail){
        User user = null;
        try{
            Connection conn = openConnection();
            try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("get_user_by_mail"));
                try{
                    stmt.setString(1,userMail);
                    ResultSet rs = stmt.executeQuery();
                    while(rs.next()){
                        user = createUser(rs);
                    }
                }finally{
                    stmt.close();
                }
            } finally{
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return user;
    }
    public User getUser(int id){
        User user = null;
        try{
            Connection conn = openConnection();
            try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("get_user_id"));
                try{
                    stmt.setInt(1,id);
                    ResultSet rs = stmt.executeQuery();
                    while(rs.next()){
                        user = createUser(rs);
                    }
                }finally{
                    stmt.close();
                }
            } finally{
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return user;
    }

    public boolean removeUser(User u){
        return removeUser(u.getName());
    }
    public boolean removeUser(String userName){
        boolean resultaat = false;
        try{
           Connection conn = openConnection();
           try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("remove_user"));
               try{
                   stmt.setString(1, userName);
                   stmt.executeUpdate();
                   resultaat = true;
               }finally{
                   stmt.close();
               }
           } finally{
               conn.close();
           }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultaat;     
    }
    public boolean removeUser(int userId){
        boolean resultaat = false;
        try{
           Connection conn = openConnection();
           try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("remove_user_id"));
               try{
                   stmt.setInt(1, userId);
                   stmt.executeUpdate();
                   resultaat = true;
               }finally{
                   stmt.close();
               }
           } finally{
               conn.close();
           }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultaat;     
    }
    
    public boolean setEnabled(User u){
        return setEnabled(u.getId(),true);
    }
    public boolean setEnabled(String userName){
        return setEnabled(getUser(userName).getId(),true);
    }
    public boolean setEnabled(User u,boolean enabled){
        return setEnabled(u.getId(),enabled);
    }
    public boolean setEnabled(int userId,boolean enabled){
        //let op in het user object zelf wordt niets aangepast.
        //User u = getUser(userId);
        //u.setEnabled(enabled);
        //removeUser(userId);
        //return addUser(u);
        
        boolean resultaat = false;
        try{
            Connection conn = openConnection();
            try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("enable_user"));
                try{
                    if(enabled){
                        stmt.setInt(1, 1);
                    }
                    else{
                        stmt.setInt(1, 0);
                    }
                    stmt.setInt(2,userId);
                    stmt.executeQuery();
                    
                    resultaat = true;
                }finally{
                    stmt.close();
                }
            } finally{
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultaat;
    }
    
    public boolean setBlocked(String userName,boolean blocked){
        User u = getUser(userName);
        return setBlocked(u.getId(), blocked);
    }
    public boolean setBlocked(int userId,boolean blocked){
//        User u = getUser(userId);
//        u.setBlocked(blocked);
//        removeUser(userId);
//        return addUser(u);
        
        boolean resultaat = false;
        try{
            Connection conn = openConnection();
            try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("block_user"));
                try{
                    if(blocked){
                        stmt.setInt(1, 1);
                    }
                    else{
                        stmt.setInt(1, 0);
                    }
                    stmt.setInt(2,userId);
                    stmt.executeQuery();
                    
                    resultaat = true;
                }finally{
                    stmt.close();
                }
            } finally{
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultaat;
    }
    
    public boolean setGamePass(int userId,String gamePass){
        //let op in het user object zelf wordt niets aangepast.
        //User u = getUser(userId);
        //u.setEnabled(enabled);
        //removeUser(userId);
        //return addUser(u);
        
        boolean resultaat = false;
        try{
            Connection conn = openConnection();
            try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("set_gamepass"));
                try{
                    stmt.setString(1, gamePass);
                    stmt.setInt(2,userId);
                    stmt.executeQuery();
                    
                    resultaat = true;
                }finally{
                    stmt.close();
                }
            } finally{
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultaat;
    }
    
        public boolean setColor(int userId,int rgb){
        //let op in het user object zelf wordt niets aangepast.
        //User u = getUser(userId);
        //u.setEnabled(enabled);
        //removeUser(userId);
        //return addUser(u);
        
        boolean resultaat = false;
        try{
            Connection conn = openConnection();
            try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("set_color"));
                try{
                    stmt.setInt(1, rgb);
                    stmt.setInt(2,userId);
                    stmt.executeQuery();
                    
                    resultaat = true;
                }finally{
                    stmt.close();
                }
            } finally{
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultaat;
    }
        
    public int addGame(Game game) {
        int id = -1;
        try{
            Connection conn = openConnection();
            try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("insert_game"),new String[]{"id"});
                try{
                    stmt.setString(1,game.getName());
                    stmt.setInt(2,game.getHost().getId());
                    stmt.setInt(3, game.getMaxPlayers());
                    stmt.setString(4,game.getDate());
                    
                    stmt.executeUpdate();
                    ResultSet rs = stmt.getGeneratedKeys();
                    while(rs.next()){
                        id =rs.getInt(1);
                    }
                }finally{
                    stmt.close();
                }
            } finally{
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return id;
    }
    public Game getGame(int id){
        Game game = null;
        try{
            Connection conn = openConnection();
            try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("get_game"));
                try{
                    stmt.setInt(1,id);
                    ResultSet rs = stmt.executeQuery();
                    if(rs.next()){
                        game = createGame(rs);
                    }
                }finally{
                    stmt.close();
                }
            } finally{
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return game;
    }
    public List<Game> getGames(){
        ArrayList<Game> game = new ArrayList<Game>();
        try{
            Connection conn = openConnection();
            try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("get_games"));
                try{
                    //stmt.setInt(1,id);
                    ResultSet rs = stmt.executeQuery();
                    while(rs.next()){
                        
                        game.add(createGame(rs));
                    }
                }finally{
                    stmt.close();
                }
            } finally{
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return game;
    }
    /**
     * active = lobby or playing
     * @return 
     */
    public List<Game> getActiveGames(){
        ArrayList<Game> game = new ArrayList<Game>();
        try{
            Connection conn = openConnection();
            try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("get_activegames"));
                try{
                    //stmt.setInt(1,id);
                    ResultSet rs = stmt.executeQuery();
                    while(rs.next()){
                        
                        game.add(createGame(rs));
                    }
                }finally{
                    stmt.close();
                }
            } finally{
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return game;
    }
    
    /**
     * 
     * @param gameid
     * @return List of players for the gameid
     */
    public List<User> getPlayers(int gameId){
        List<User> players = new ArrayList<User>();
        try{
           Connection conn = openConnection();
           try{
               PreparedStatement stmt = conn.prepareStatement(constanten.getString("get_players"));
               try{
                   stmt.setInt(1, gameId);
                   ResultSet rs = stmt.executeQuery();
                   while (rs.next()) {
                        players.add(createUser(rs));
                    }
               }finally{
                   stmt.close();
               }
           } finally{
               conn.close();
           }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return players;
    }
    public boolean joinGame(User user,int gameId){
        boolean resultaat = false;
        try{
           Connection conn = openConnection();
           try{
               PreparedStatement stmt = conn.prepareStatement(constanten.getString("join_game"));
               try{
                   stmt.setInt(1, gameId);
                   stmt.setInt(2, user.getId());
                   stmt.executeUpdate();
                   resultaat = true;
               }finally{
                   stmt.close();
               }
           } finally{
               conn.close();
           }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultaat;
    }
    public boolean leaveGame(User user){
        boolean resultaat = false;
        try{
           Connection conn = openConnection();
           try{
               PreparedStatement stmt = conn.prepareStatement(constanten.getString("leave_game"));
               try{
                   stmt.setInt(1, user.getId());
                   stmt.executeUpdate();
                   resultaat = true;
               }finally{
                   stmt.close();
               }
           } finally{
               conn.close();
           }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultaat;
    }
    
    public boolean startGame(int gameid){
        boolean resultaat = false;
        try{
           Connection conn = openConnection();
           try{
               PreparedStatement stmt = conn.prepareStatement(constanten.getString("start_game"));
               try{
                   stmt.setInt(1, gameid);
                   stmt.executeUpdate();
                   resultaat = true;
               }finally{
                   stmt.close();
               }
           } finally{
               conn.close();
           }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultaat;
    }
    public boolean destroyGame(int gameid){
        boolean resultaat = false;
        try{
           Connection conn = openConnection();
           try{
               PreparedStatement stmt = conn.prepareStatement(constanten.getString("destroy_game"));
               try{
                   stmt.setInt(1, gameid);
                   stmt.executeUpdate();
                   resultaat = true;
               }finally{
                   stmt.close();
               }
           } finally{
               conn.close();
           }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultaat;
    }
    public boolean endGame(int gameid){
        boolean resultaat = false;
        try{
           Connection conn = openConnection();
           try{
               PreparedStatement stmt = conn.prepareStatement(constanten.getString("end_game"));
               try{
                   stmt.setInt(1, gameid);
                   stmt.executeUpdate();
                   resultaat = true;
               }finally{
                   stmt.close();
               }
           } finally{
               conn.close();
           }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultaat;
    }
    
    public List<AjaxUser> getAjaxPlayers(int gameId){
        List<AjaxUser> players = new ArrayList<AjaxUser>();
        try{
           Connection conn = openConnection();
           try{
               PreparedStatement stmt = conn.prepareStatement(constanten.getString("get_players"));
               try{
                   stmt.setInt(1, gameId);
                   ResultSet rs = stmt.executeQuery();
                   while (rs.next()) {
                        players.add(createAjaxUser(rs));
                    }
               }finally{
                   stmt.close();
               }
           } finally{
               conn.close();
           }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return players;
    }
    public AjaxGame getAjaxGame(int id){
        AjaxGame game = null;
        try{
            Connection conn = openConnection();
            try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("get_game"));
                try{
                    stmt.setInt(1,id);
                    ResultSet rs = stmt.executeQuery();
                    if(rs.next()){
                        game = createAjaxGame(rs);
                    }
                }finally{
                    stmt.close();
                }
            } finally{
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return game;
    }
    public List<AjaxGame> getAjaxGames(){
        ArrayList<AjaxGame> game = new ArrayList<AjaxGame>();
        try{
            Connection conn = openConnection();
            try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("get_activegames"));
                try{
                    //stmt.setInt(1,id);
                    ResultSet rs = stmt.executeQuery();
                    while(rs.next()){
                        
                        game.add(createAjaxGame(rs));
                    }
                }finally{
                    stmt.close();
                }
            } finally{
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return game;
    }
    public AjaxUser getAjaxUser(int id){
        AjaxUser user = null;
        try{
            Connection conn = openConnection();
            try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("get_user_id"));
                try{
                    stmt.setInt(1,id);
                    ResultSet rs = stmt.executeQuery();
                    while(rs.next()){
                        user = createAjaxUser(rs);
                    }
                }finally{
                    stmt.close();
                }
            } finally{
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return user;
    }
    
    //private
    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection(constanten.getString("connectiestring"), constanten.getString("username"), constanten.getString("password"));
    }
    private User createUser(ResultSet rs) throws SQLException{
        return new User(rs.getInt("id"),rs.getString("name"),rs.getString("pass"),rs.getString("salt"),rs.getString("mail"),rs.getInt("game"),rs.getInt("color"),rs.getBoolean("enabled"),rs.getBoolean("administrator"),rs.getBoolean("blocked"),rs.getString("gamepass"));
    }
    private Game createGame(ResultSet rs) throws SQLException {
       User host = getUser(rs.getInt("host"));
       List<User> players = getPlayers(rs.getInt("id"));
       return new Game(rs.getInt("id"),rs.getInt("maxplayers"), rs.getString("name"),host,players,rs.getInt("status"),rs.getString("datum"));
    }
    private AjaxUser createAjaxUser(ResultSet rs) throws SQLException{
        return new AjaxUser(rs.getInt("id"),rs.getString("name"),rs.getInt("game"),rs.getInt("color"));
    }
    private AjaxGame createAjaxGame(ResultSet rs) throws SQLException {
       AjaxUser host = getAjaxUser(rs.getInt("host"));
       List<AjaxUser> players = getAjaxPlayers(rs.getInt("id"));
       return new AjaxGame(rs.getInt("id"),rs.getInt("maxplayers"), rs.getString("name"),host,players,rs.getInt("status"));
    }
    private NumberPair createNumberPair(ResultSet rs) throws SQLException{
        NumberPair p = new NumberPair(rs.getString("datum"),rs.getInt("aantal"));
        return p;
    }
    
    public boolean insertCommand(int id ,int nr,String jsonCommand) {
        boolean resultaat = false;
        try{
            Connection conn = openConnection();
            try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("insert_command"));
                try{
                    stmt.setInt(1,id);
                    stmt.setInt(2,nr);
                    stmt.setString(3, jsonCommand);
                    stmt.executeUpdate();
                    resultaat = true;
                }finally{
                    stmt.close();
                }
            } finally{
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return resultaat;
    }
    public boolean checkIfBoardExists(int id){
        boolean result = false;
        try{
            Connection conn = openConnection();
            try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("check_if_board_exists"));
                try{
                    stmt.setInt(1,id);
                    ResultSet rs = stmt.executeQuery();
                    int number=-1;
                    while(rs.next()){
                        number = rs.getInt(1);
                    }
                    if(number>0){
                        result=true;
                    }
                }finally{
                    stmt.close();
                }
            } finally{
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
    public ArrayList<String> getJsonCommands(int id){
        ArrayList<String> JSONList = null;
        try{
            Connection conn = openConnection();
            try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("get_commands"));
                try{
                    JSONList = new ArrayList<String>();
                    stmt.setInt(1,id);
                    ResultSet rs = stmt.executeQuery();
                    while(rs.next()){
                        JSONList.add(rs.getString(1));
                    }
                }finally{
                    stmt.close();
                }
            } finally{
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return JSONList;
    }
    public int getActiveGameCount(){
        int aantal = -1;
        try{
            Connection conn = openConnection();
            try{
                PreparedStatement stmt = conn.prepareStatement(constanten.getString("get_active_games"));
                try{
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    aantal = rs.getInt(1);
                }finally{
                    stmt.close();
                }
            } finally{
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return aantal;
    }
    
    public String[] getLabels(){
        ArrayList<String> lijst = null;
        try {
            Connection conn = openConnection();
            try {
                Statement stmt = conn.createStatement();
                try {
                                        
                    ResultSet rs = stmt.executeQuery(constanten.getString("get_labels"));
                    lijst = new ArrayList<String>();
                    while (rs.next()) {
                        lijst.add(rs.getString(1));
                    }
                } finally {
                    stmt.close();
                }
            } finally {
                conn.close();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
                
        String[] retVal = new String[lijst.size()];
        for (int i = 0 ; i < lijst.size() ; i++){
            retVal[i] = new String(lijst.get(i).substring(6, 8) + "/" + lijst.get(i).substring(4,6) + "/" + lijst.get(i).substring(0, 4));
        }
        return retVal;
        
    }
    public int[] getValues(){
        ArrayList<Integer> lijst = null;
        try {
            Connection conn = openConnection();
            try {
                Statement stmt = conn.createStatement();
                try {
                                        
                    ResultSet rs = stmt.executeQuery(constanten.getString("get_values"));
                    lijst = new ArrayList<Integer>();
                    while (rs.next()) {
                        lijst.add(rs.getInt(1));
                    }
                } finally {
                    stmt.close();
                }
            } finally {
                conn.close();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
                
        int[] retVal = new int[lijst.size()];
        for (int i = 0 ; i < lijst.size() ; i++){
            retVal[i] = lijst.get(i);
        }
        return retVal;
    }
    
    public List<NumberPair> getCount(){
        ArrayList<NumberPair> lijst = null;
        
        try {
            Connection conn = openConnection();
            try {
                Statement stmt = conn.createStatement();
                try {
                                        
                    ResultSet rs = stmt.executeQuery(constanten.getString("get_numbers"));
                    lijst = new ArrayList<NumberPair>();
                    while (rs.next()) {
                        lijst.add(createNumberPair(rs));
                    }
                } finally {
                    stmt.close();
                }
            } finally {
                conn.close();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
                
        return lijst;
    }
}