/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import commandos.ICommand;
import commandos.trade.ResponseCommand;
import commandos.trade.TradeEndedCommand;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import model.interfaces.IPlayer;
import model.interfaces.ISettlement;
import model.interfaces.ITile;
import observer.IObservable;
import observer.IObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utility.DevCardType;
import utility.PlayerWithMessage;
import utility.TileType;

/**
 * Model van het Speelbord.
 *
 * @author Andreas De Lille
 */
public class Board implements IObservable {

    private static final Logger log = LoggerFactory.getLogger(Board.class);
    private int id;
    //private ResourceBundle constants = ResourceBundle.getBundle("utility.language"); 
    private Random rand = new Random(System.currentTimeMillis());
    private ITile[][] tiles;
    private ArrayList<IPlayer> players = new ArrayList<IPlayer>();
    private int currentPlayer = 0;
    private final int XDIM = 7;
    private final int YDIM = 7;
    static int[] NUMBERTILES = {4, 5, 6, 7, 6, 5, 4};
    private static Point[] outerRecources = {new Point(1, 1), new Point(2, 1), new Point(3, 1), new Point(4, 1), new Point(5, 1), new Point(5, 2), new Point(5, 3), new Point(4, 4), new Point(3, 5), new Point(2, 4), new Point(1, 3), new Point(1, 2)};
    private static Point[] innerRecources = {new Point(2, 2), new Point(3, 2), new Point(4, 2), new Point(4, 3), new Point(3, 4), new Point(2, 3)};
    private static Point pointMiddle = new Point(3, 3);
    private static int[] getalfiches = {5, 2, 6, 3, 8, 10, 9, 12, 11, 4, 8, 10, 9, 4, 5, 6, 3, 11}; //A is op plaats 0,B op 1,....
    private ISettlement lastSettlement = null;
    private boolean placeSettlementPossible = true;
    private boolean placeRoadPossible = true;
    private boolean roadToSettlement = false;
    private boolean placeCity = false;
    private boolean nonSeaRoad = false;
    private boolean nonSeaStartSettlement = false;
    private boolean dontShowOptionPanesAgain = true;//toont al dan niet de popupvensters
    private ArrayList<ITile> list;
    private ArrayList<ICommand> commands = new ArrayList<ICommand>();
    private Bank bank;
    private int numberStructures = 0;
    private boolean isStructureFase = true;
    private Dices dices = new Dices();
    private Map<Integer, Integer> idToIndex = new TreeMap<Integer, Integer>();
    private Color[] colors = {Color.red, Color.GREEN, Color.blue, Color.ORANGE, Color.MAGENTA};//Color.BLACK,Color.PINK, Color.YELLOW,Color.cyan
    private Color[] usedColors = new Color[4];
    private ArrayList<IObserver> observers = new ArrayList<IObserver>();
    private boolean buildVillage = false;
    private boolean buildRoad = false;
    private boolean buildCity = false;
    private String info = "";
    private String changes = "";
    private int maxPlayers;
    private int numberFreeRoadsPlaced = 0;    //clientside freeroads
    private int startBuilding = 1;
    //server
    private boolean gameFase = false;
    private Boolean isServer;
    private int lastRolledNumber = 0;
    private boolean isSpectatorMode;
    private ArrayList<DevCardType> cards;
    private int startPositionNumbers;
    private int startingPlayer;
    private int playersLoggedIn;
    private ArrayList<Integer> playersformessages = new ArrayList<Integer>();
    private ArrayList<String> messages = new ArrayList<String>();
    //trade
    private int respondedPlayers = 0;
    private int fastestPlayerId = -1;
    private long lowestReactionTime = -1;
    private boolean tradeOver = false;
    private boolean[] playersAnswered = {false, false, false, false};
    //dobbelstenen
    private int number1 = 0;
    private int number2 = 0;
    private int seed;
    private Random diceRnd;
    //server testing
    private boolean[] beginVillage;
    private boolean[] makeRoad;
    private boolean[] rollDice;
    private boolean[][] mayPlayDevCardType;
    private boolean[] maySteal;
    private boolean mayPlaceThief = false;
    private int howmanyPlayed = 0;

    /**
     * Enige constructor, maakt een veld aan met emptyvakjes en zeevakjes
     *
     * @param selectModel ons linkerpaneel tijdens slepen tiles
     */
    public Board() {
        log.debug("Entering Board constructor\nLeaving Board constructor");
    } //aanmaken modelveld

    public Board(Boolean isServer, int id) {
        log.debug("Entering Board constructor ( isServer= {} )", isServer);
        this.id = id;
        bank = new Bank(19);
        this.isServer = isServer;
        tiles = new ITile[XDIM][YDIM];
        for (int i = 0; i < XDIM; i++) {
            for (int j = 0; j < NUMBERTILES[i]; j++) {
                if (i == 0 || i == XDIM - 1 || j == 0 || j == NUMBERTILES[i] - 1) {
                    tiles[i][j] = new Tile(TileType.SEA);
                    log.trace("tile x= {} y= {} is now of type {}", i, j, TileType.SEA);
                } //waterttype = haven/zee
                else {
                    tiles[i][j] = new Tile(TileType.EMPTY);
                    log.trace("tile x= {} y= {} is now of type {}", i, j, TileType.EMPTY);
                } //grondtype
                tiles[i][j].setNumber(-2);
                log.trace("Tilex= {} y= {} has now number {}", i, j, -2);
            }
        }
        seed = (int) System.currentTimeMillis();
        diceRnd = new Random(seed);
        dices = new Dices();
        cards = new ArrayList<DevCardType>();
        for (int i = 0; i < 15; i++) {
            cards.add(DevCardType.KNIGHT);
        }
        for (int i = 0; i < 4; i++) {
            cards.add(DevCardType.VICTORYPOINT);
        }
        for (int i = 0; i < 4; i++) {
            cards.add(DevCardType.MONOPOLY);
        }
        for (int i = 0; i < 4; i++) {
            cards.add(DevCardType.RESOURCES);
        }
        for (int i = 0; i < 4; i++) {
            cards.add(DevCardType.ROAD);
        }
        shuffle(cards);
        playersLoggedIn = 0;
        log.info("EmptyBoard completed");
        log.debug("Leaving board constructor");
    }

    /**
     * Hermaakt het startbord uit de meegegeven lijst van getallen.
     *
     * @param startList lijst van getallen die startbord voorstelt
     */
    public Board(ArrayList<Integer> startList, int id) {
        this.id = id;
        bank = new Bank(19);
        isServer = true;
        tiles = new ITile[XDIM][YDIM];
        for (int i = 0; i < XDIM; i++) {
            for (int j = 0; j < NUMBERTILES[i]; j++) {
                if (i == 0 || i == XDIM - 1 || j == 0 || j == NUMBERTILES[i] - 1) {
                    tiles[i][j] = new Tile(TileType.SEA);
                    log.trace("tile x= {} y= {} is now of type {}", i, j, TileType.SEA);
                } //waterttype = haven/zee
                else {
                    tiles[i][j] = new Tile(TileType.EMPTY);
                    log.trace("tile x= {} y= {} is now of type {}", i, j, TileType.EMPTY);
                } //grondtype
                tiles[i][j].setNumber(-2);
                log.trace("Tilex= {} y= {} has now number {}", i, j, -2);
            }
        }

        cards = new ArrayList<DevCardType>();
        for (int i = 0; i < 15; i++) {
            cards.add(DevCardType.KNIGHT);
        }
        for (int i = 0; i < 4; i++) {
            cards.add(DevCardType.VICTORYPOINT);
        }
        for (int i = 0; i < 4; i++) {
            cards.add(DevCardType.MONOPOLY);
        }
        for (int i = 0; i < 4; i++) {
            cards.add(DevCardType.RESOURCES);
        }
        for (int i = 0; i < 4; i++) {
            cards.add(DevCardType.ROAD);
        }
        setMaxPlayers(startList.get(1));
        currentPlayer = startList.get(2);
        startingPlayer = currentPlayer;
        beginVillage = new boolean[maxPlayers];
        makeRoad = new boolean[maxPlayers];
        beginVillage[startingPlayer] = true;
        makeRoad[startingPlayer] = true;
        //PlayersTurn[startingPlayer]=true;        
        TileType[] types = TileType.values();
        int k = 3;
        for (int i = 0; i < XDIM; i++) {
            for (int j = 0; j < YDIM; j++) {
                if (startList.get(k) != -1) {
                    tiles[i][j].setType(types[startList.get(k)]);
                }
                k++;
            }
        }
        setThiefInDesert();
        placeNumbers(startList.get(0));
        playersLoggedIn = 0;
        seed = startList.get(k);
        diceRnd = new Random(seed);
        shuffle(cards);
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;


        mayPlayDevCardType = new boolean[maxPlayers][5];
        rollDice = new boolean[maxPlayers];
        //PlayCard2Stuff=new boolean[maxPlayers];
        //PlayersTurn=new boolean[maxPlayers];
        maySteal = new boolean[maxPlayers];
        for (int i = 0; i < maxPlayers; i++) {

            //PlayCard2Stuff[i]=false;
            for (int j = 0; j < 5; j++) {
                mayPlayDevCardType[i][j] = false;
            }
            rollDice[i] = false;
            //PlayersTurn[i]=false;
            maySteal[i] = false;
        }
        mayPlaceThief = false;


    }

    public void setIsServer(Boolean isServer) {
        log.debug("setIsServer = {}", isServer);
        this.isServer = isServer;
    }

    /**
     * Client-only Zet de client als huidige speler. Geeft een kleur aan de
     * speler. Stelt de meegegeven speler opnieuw in zodat hij observed kan
     * worden. Stelt ook een random seed in voor de dobbelstenen zodanig dat
     * elke speler niet hetzelfde getal dobbelt. Koppelt aan elke speler de bank
     * die observed wordt.
     *
     * @param player
     */
    public void setCurrentPlayer(IPlayer player) {
        log.debug("Entering setCurrentPlayer ( player= {} )", player);

        currentPlayer = idToIndex.get(player.getId());
        player.setColor(usedColors[currentPlayer]);
        players.set(currentPlayer, player);
        for (IPlayer p : players) {
            p.setBank(bank);
            log.trace("player {} has now a bank.", p.getName());
        }

        log.debug("Leaving setCurrentPlayer");
    }

    /**
     * Voegt een Speler toe aan de lijst van spelers. De algemene bank wordt
     * toegekend aan de speler. Er wordt een kleur toegevoegd aan de speler. De
     * spelerindex wordt bijgehouden.
     *
     * @param player nieuwe Speler.
     */
    public IPlayer addPlayer(int playerId, String name) {
        log.debug("Entering addPlayer ( player= {} )", playerId);
        IPlayer player = null;
        if (!checkIfPlayerExists(playerId) && players.size() <= maxPlayers) {
            Color c = colors[rand.nextInt(colors.length)];
            while (c == usedColors[0] || c == usedColors[1] || c == usedColors[2] || c == usedColors[3]) {
                c = colors[rand.nextInt(colors.length)];
            }
            player = new Player(playerId, name, bank, c);
            players.add(player);
            usedColors[players.size() - 1] = c;
            idToIndex.put(playerId, players.size() - 1);
            currentPlayer = rand.nextInt(players.size());
            log.info("Player {} added to board", player);
            log.debug("Leaving addPlayer");
            playersLoggedIn++;
        } else if (checkIfPlayerExists(playerId)) {
            player = nameToPlayer(playerId);
            playersLoggedIn++;
        }

        return player;
    }

    /**
     * Voegt een Gebruiker toe aan het bord.
     *
     * @param name gebruikersnaam
     * @param color gebruikerskleur
     */
    public void AddUser(int id, String name, Color color) {
        IPlayer player = new Player(id, name, bank, color);
        players.add(player);
        usedColors[players.size() - 1] = color;
        idToIndex.put(id, players.size() - 1);
    }

    /**
     * Kiest een willekeurige Speler die eerst mag beginnen.
     */
    public void pickStartingPlayer() {
        currentPlayer = rand.nextInt(players.size());
        startingPlayer = currentPlayer;

        beginVillage = new boolean[maxPlayers];
        makeRoad = new boolean[maxPlayers];
        beginVillage[startingPlayer] = true;
        makeRoad[startingPlayer] = true;
        //PlayersTurn[startingPlayer]=true;
    }

    /**
     * Controleert als de speler al aanwezig is in het spel
     *
     * @param playername spelernaam
     * @return true als speler al bestaat in Board
     */
    public boolean checkIfPlayerExists(int playername) {
        return idToIndex.containsKey(playername);
    }

    /**
     * Wordt niet meer gebruikt.
     *
     * @param player
     */
    public void addClientPlayer(IPlayer player) {
        log.debug("Entering addCientPlayer ( player= {} )", player);

        currentPlayer = players.size() - 1;
        player.setColor(colors[currentPlayer]);
        players.add(player);

        log.debug("Leaving addClientPlayer");
    }

    /**
     * Geeft een tegel terug die op positie (x,y) staat. Er wordt gecontroleerd
     * op de invoerwaarden.
     *
     * @param x x-coördinaat van de tegel
     * @param y y-coördinaat van de tegel
     * @return tegel op positie (x,y)
     */
    public ITile getTile(int x, int y) {
        log.debug("Entering getTile ( x= {} y= {} )");
        if (x >= 0 && x <= XDIM && y >= 0 && y <= YDIM) {
            return tiles[x][y];
        } else {
            log.warn("returning null, tile x= {} y= {} does not exist or is out of range", x, y);
            return null;
        }
    }

    /**
     * Plaatst een tegel van het type
     * <code>type</code> op positie (x,y).
     *
     * @param x x-coördinaat van de tegel
     * @param y y-coördinaat van de tegel
     * @param type voorgedefinieerd grondstoftype
     * @see TileType
     */
    public void setTile(int x, int y, TileType type) {
        log.debug("Entering setTile ( x= {} y= {} type= {} )", x, y, type);
        tiles[x][y] = new Tile(type);
        log.debug("Leaving setTile");
    }

    /**
     * plaatst het juiste nummer op een tegel gegeven tegel aangeduid door een
     * index berekent de plaats van een tegel in de spiraal uit een gewone rij
     *
     * @param i nummer van de tile waaruit het echte tileObject kan afgeleidt
     * worden
     * @param rowTiles rij van tegels ofwel de buitenste circel tegels ofwel de
     * binnecste circel
     * @param wichnumber duidt de index van het getal aan dat moet geplaatst
     * worden
     * @param n duidt het aantal tegels aan die al een nummer voordien hebben
     * gekregen
     * @return
     */
    private int placeNumber(int i, Point[] rowTiles, int wichnumber, int n) {
        log.debug("Entering placeNumber ( i= {} rowTiles.size()= {} wichnumber= {} n= {}", i, rowTiles.length, wichnumber, n);

        if (tiles[rowTiles[(i + n) % rowTiles.length].x][rowTiles[(i + n) % rowTiles.length].y].getType() != TileType.DESERT) { //als tegel desert is geen nummer leggen,anders wel
            tiles[rowTiles[(i + n) % rowTiles.length].x][rowTiles[(i + n) % rowTiles.length].y].setNumber(getalfiches[wichnumber]); //plaats getalfiche
            wichnumber++;
        }
        log.debug("returning placeNumber = {}", wichnumber);
        return wichnumber;
    }

    /**
     * loopt alle tegels af en roept de functie setTile ervan op wordt
     * opgeroepen als er op een tegel geklikt wordt aflopen van de tegels begint
     * bij de aangeklikte tegel als deze een hoekpunt van het veld is
     *
     * @param x xCoordinaat van de tegel waarop geklikt is
     * @param y yCoordinaat van de tegel waarop geklikt is
     */
    private void placeNumbers(int n) {
        startPositionNumbers = n;
        log.debug("Entering placeNumbers ( n= {} )", n);

        if (n % 2 == 0) { //testen of het gaat om hoekpunt
            int wichnumber = 0; // om houdt bij aan welk getal men zit

            for (int i = 0; i < outerRecources.length; i++) {
                wichnumber = placeNumber(i, outerRecources, wichnumber, n);
            }
            n /= 2;
            for (int i = 0; i < innerRecources.length; i++) {
                wichnumber = placeNumber(i, innerRecources, wichnumber, n);
            }

            placeNumber(0, new Point[]{pointMiddle}, wichnumber, 0);

        }
        log.info("placed numbers");
        log.debug("Leaving placeNumbers");
    }

    /**
     * Geeft alle tegels met nummer
     * <code>number</code> op.
     *
     * @param number dobbelbaar getal
     * @return lijst van tegels met <code>number</code> op
     */
    private ArrayList<ITile> getTilesWithNumber(int number) {
        log.debug("Entering getTilesWithNumber ( number= {} )", number);

        ArrayList<ITile> list = new ArrayList<ITile>();

        for (int y = 0; y < YDIM; y++) {
            for (int x = 0; x < XDIM; x++) {
                if (tiles[x][y] != null && tiles[x][y].getNumber() == number) {
                    list.add(tiles[x][y]);
                    log.trace("Add tile x= {} y= {} to list");
                }
            }
        }

        log.debug("returning from getTilesWithNumber list.size()= {}", list.size());
        return list;
    }

    /**
     * Geeft Spelers die een dorp hebben op tegels met nummer
     * <code>number</code> hun grondstoffen. Houdt bij welk getal gedobbeld
     * werd. Geeft door dat er mogelijk grondstoffen verdeeld zijn aan zijn
     * Observers.
     *
     * @param number gedobbelde getal
     */
    public void giveResources(int number) {
        //players.get(currentPlayer).resetChangedResources();
        log.debug("Entering giveResources ( number= {} )", number);
        clearLastRolledNumber();
        list = getTilesWithNumber(number);
        if (!list.isEmpty()) {
            for (ITile tile : list) {
                if (!tile.hasThief()) { //bij tile met struikrover: geen grondstoffen
                    tile.setRolled(true); //tile aanduiden
                }
            }
        }
        notifyObservers();
        lastRolledNumber = number;
        notifyPlayerObservers();
        log.info("board gave resources");
        log.debug("Leaving giveResources");
    }

    public int[] giveResources() {

        if (rollDice[currentPlayer]) {
            number1 = diceRnd.nextInt(6) + 1;
            number2 = diceRnd.nextInt(6) + 1;
            //if(!rolled){
            int number = number1 + number2;
            list = getTilesWithNumber(number);
            lastRolledNumber = number;
            if (!list.isEmpty()) {
                for (ITile tile : list) {
                    if (!tile.hasThief()) { //bij tile met struikrover: geen grondstoffen
                        ISettlement[] settlements = tile.getSettlements();
                        for (ISettlement settlement : settlements) {
                            if (settlement != null) {
                                for (int i = 0; i < settlement.settlementSize(); i++) {
                                    settlement.getPlayer().addResource(tile.getType());
                                }
                            }
                        }

                    }
                }
            }
            if (number == 7) {
                mayPlaceThief = true;
            }

            rollDice[currentPlayer] = false;
            return new int[]{number1, number2};

        }
        return null;
    }

    /**
     * Maakt een random bord aan. Roept de functie op om de getallen te plaatsen
     * en de struikrover in de woestijn te zetten.
     */
    public void placeField() {
        log.debug("Entering placeField");

        ArrayList<Tile> tiles = new ArrayList<Tile>();
        tiles.add(new Tile(TileType.WOOD, 4));
        tiles.add(new Tile(TileType.IRON, 3));
        tiles.add(new Tile(TileType.WOOL, 4));
        tiles.add(new Tile(TileType.STONE, 3));
        tiles.add(new Tile(TileType.WHEAT, 4));
        tiles.add(new Tile(TileType.DESERT, 1));

        for (int i = 0; i < outerRecources.length; i++) {
            rand = placeRandomTile(tiles, outerRecources[i].x, outerRecources[i].y, rand);
        }
        for (int i = 0; i < innerRecources.length; i++) {
            rand = placeRandomTile(tiles, innerRecources[i].x, innerRecources[i].y, rand);
        }
        rand = placeRandomTile(tiles, 3, 3, rand);

        placeNumbers(rand.nextInt(6) * 2);

        setThiefInDesert();

        log.info("Random Board created");
        log.debug("Leaving placeField");
    }

    /**
     * plaatst 1 tegel random op het board
     *
     * @param v nog niet geplaatste tegels
     * @param x xcoordinaat plaats op het bord
     * @param y ycoordinaat plaats op het bord
     * @param rand random getal
     */
    private Random placeRandomTile(ArrayList<Tile> v, int x, int y, Random rand) {
        log.debug("Entering placeRandomTile ( v.size()= {} x= {} y={} rand= {} )", v.size(), x, y, rand);

        int n = rand.nextInt(v.size());
        while (v.get(n).getNumber() == 0) {
            n = rand.nextInt(v.size());
        }
        tiles[x][y] = new Tile(v.get(n).getType());
        tiles[x][y].setNumber(-2);
        v.get(n).setNumber(v.get(n).getNumber() - 1);

        log.trace("tile x= {} y= {} is now of type= {}", x, y, v.get(n).getType());
        log.debug("Leaving placeRandomTile");
        return rand;
    }

    /**
     * Plaatst de Rover op de woestijntegel.
     */
    private void setThiefInDesert() {
        log.debug("Entering setThiefInDesert");

        for (int y = 0; y < YDIM; y++) {
            for (int x = 0; x < XDIM; x++) {
                if (tiles[x][y] != null && tiles[x][y].getType() == TileType.DESERT) {
                    tiles[x][y].setThief(true);
                    log.trace("found desert & set thief");
                }
            }
        }
        log.info("thief placed in desert");

        log.debug("Leaving setThiefInDesert");
    }

    /**
     * Verplaatst de Rover naar de opgegeven locatie. Controleert als de nieuwe
     * locatie geen zeetegel is en de Rover nog niet bevat.
     *
     * @param x x-coördinaat van een tegel
     * @param y y-coördinaat van een tegel
     * @return true als het verplaatsen gelukt is
     */
    public boolean changeThiefLocation(int x, int y) {
        //players.get(currentPlayer).resetChangedResources();
        log.debug("Entering changeThiefLocation ( x= {} y= {})", x, y);
        if (mayPlaceThief) {
            if ((tiles[x][y].getType() != TileType.SEA)) {
                for (int i = 0; i < XDIM; i++) {
                    for (int j = 0; j < YDIM; j++) {
                        if ((i != x || y != j) && tiles[i][j] != null && tiles[i][j].hasThief()) {
                            tiles[i][j].setThief(false);
                            log.trace("thief removed from tile x= {} y= {}", x, y);
                            tiles[x][y].setThief(true);
                            log.trace("thief set at tile x= {} y= {}", x, y);
                            tiles[x][y].setRedThief(false);
                            notifyObservers();

                            log.info("Thief moved from x= {} y= {} to x= {} y= {}", i, j, x, y);
                            log.debug("Leaving changeThiefLocation");


                            HashSet<Integer> players = getTile(i, j).containsOtherPlayer(getCurrentPlayerId());
                            getCurrentPlayer().setOtherPlayerState(false);
                            maySteal[currentPlayer] = true;

                            mayPlaceThief = false;
                            return true;
                        }

                    }
                }


            }
        }

        log.warn("ThiefLocation not changed");
        return false;
    }

    public boolean canChangeThiefLocationClient(int x, int y) {
        log.debug("Entering changeThiefLocation ( x= {} y= {})", x, y);
        for (int i = 0; i < XDIM; i++) {
            for (int j = 0; j < YDIM; j++) {
                if ((i != x || y != j) && tiles[i][j] != null && tiles[i][j].hasThief() && (tiles[x][y].getType() != TileType.SEA)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void changeThiefLocationClient(int x, int y) {
        log.debug("Entering changeThiefLocation ( x= {} y= {})", x, y);
        for (int i = 0; i < XDIM; i++) {
            for (int j = 0; j < YDIM; j++) {
                if ((i != x || y != j) && tiles[i][j] != null && tiles[i][j].hasThief()) {
                    tiles[i][j].setThief(false);
                    tiles[x][y].setThief(true);
                    notifyObservers();
                    HashSet<Integer> players = getTile(i, j).containsOtherPlayer(getCurrentPlayerId());
                    //getCurrentPlayer().setOtherPlayerState(false);
                    mayPlaceThief = false;
                }
            }
        }
        notifyPlayerObservers();
        setMessages("", "changeThiefLocation");
    }

    /**
     * Controleert als het plaatsen van een nederzetting in de startfase
     * mogelijk is. Houdt bij als het niet mogelijk is. Controleert ook als het
     * niet op de zee ligt.
     *
     * @param x x-coördinaat van de tegel
     * @param y y-coördinaat van de tegel
     * @param index nederzettingindex (index tussen 0 en 5, startend van uiterst
     * linkse punt)
     */
    public void checkPossibleStartSettlement(int x, int y, int index) {
        log.debug("Entering checkPossibleStartSettlement ( x= {} y= {} index= {})", x, y, index);

        if (!getTile(x, y).settlementPossible(index, players.get(currentPlayer).getId())) {
            placeSettlementPossible = false;
        }
        if (!checkSea(x, y)) {
            nonSeaStartSettlement = true;
        }

        log.trace("placeSettlementPossible = {} \n nonSeaStartSettlement = {}", placeSettlementPossible, nonSeaStartSettlement);
        log.debug("Leaving checkPossibleStartSettlement");
    }

    /**
     * Controleert als de opgegeven tegel geen zee is.
     *
     * @param x x-coördinaat van de tegel
     * @param y y-coördinaat van de tegel
     * @return true als het zee is
     */
    private boolean checkSea(int x, int y) {
        log.debug("Returning from checkSea ( x= {} y= {} ) = {}", x, y, getTile(x, y).getType().equals(TileType.SEA));
        return getTile(x, y).getType().equals(TileType.SEA);
    }

    /**
     * Controleert als het plaatsen van een nederzetting in de speelfase
     * mogelijk is. Houdt bij als het niet mogelijk is.
     *
     * @param x x-coördinaat van de tegel
     * @param y y-coördinaat van de tegel
     * @param index nederzettingindex (index tussen 0 en 5, startend van uiterst
     * linkse punt)
     */
    public void checkPossibleSettlement(int x, int y, int index) {
        log.debug("Entering checkPossibleSettlement ( x= {} y= {} index= {}", x, y, index);
        if (players.get(currentPlayer).buildCityPossible() && (getTile(x, y).cityPossible(index, players.get(currentPlayer).getId()))) {
            {
                System.out.println("placecity true");
                placeCity = true;
            }
        } else if (players.get(currentPlayer).buildSettlementPossible()) {
            if (!getTile(x, y).settlementPossible(index, players.get(currentPlayer).getId())) {
                placeSettlementPossible = false;
            }
            if (getTile(x, y).roadToSettlement(index, players.get(currentPlayer).getId())) {
                roadToSettlement = true;
            }
        }
        log.trace("placeSettlementPossible = {} \n roadToSettlement = {}", placeSettlementPossible, roadToSettlement);
        log.debug("Leaving checkPossibleSettlement");
    }

    /**
     * Plaatst in aangrenzende tegels (meegekregen uit view) de weg op de juiste
     * index.[SPEELFASE]. Controleert als plaatsen van nederzetting mogelijk is.
     * Past de laatst geplaatste nederzetting aan met de nieuwe indien nodig.
     * Zorgt ervoor dat de verbruikte grondstoffen verwijderd worden. Verwittigt
     * Observers als er zich veranderingen voordoen.
     *
     * @param tellers lijst van {x-coördinaat tegel, y-coördinaat tegel,
     * nederzettingindex op tegel}
     * @return true als plaatsen van nederzetting gelukt is
     */
    public boolean setSettlements(ArrayList<int[]> tellers) {

        log.debug("Entering setSettlements ( tellers.size()= {} )", tellers.size());
        if (placeSettlementPossible && roadToSettlement) {

            for (int i = 0; i < tellers.size(); i++) {
                getTile(tellers.get(i)[0], tellers.get(i)[1]).setSettlement(new Village(players.get(currentPlayer)), tellers.get(i)[2]);

                log.info("Village placed at x= {} y= {} player= {}", tellers.get(i)[0], tellers.get(i)[1], players.get(currentPlayer).getName());
            }
            players.get(currentPlayer).settlementBuilt();
            notifyPlayerObservers();
            notifyObservers();
            placeSettlementPossible = true;
            roadToSettlement = false;

            log.debug("Leaving setSettlements = true");

            return true;


        } else {
            setMessages("", "noVillagePossible");
        }
        placeSettlementPossible = true;
        roadToSettlement = false;
        log.debug("Leaving setSettlements = false");
        return false;
    }

    public boolean setCities(ArrayList<int[]> tellers) {
        //players.get(currentPlayer).resetChangedResources();
        log.debug("Entering setCities ( tellers.size()= {} )", tellers.size());

        if (placeCity) {
            for (int i = 0; i < tellers.size(); i++) {
                getTile(tellers.get(i)[0], tellers.get(i)[1]).setSettlement(new City(players.get(currentPlayer)), tellers.get(i)[2]);
                log.info("City placed at x= {} y= {} player= {}", tellers.get(i)[0], tellers.get(i)[1], players.get(currentPlayer).getName());
            }
            players.get(currentPlayer).CityBuilt();
            placeCity = false;
            notifyPlayerObservers();
            notifyObservers();
            placeSettlementPossible = true;
            roadToSettlement = false;

            log.debug("Leaving setCities = true");
            return true;

        } else {
            setMessages("", "noCityPossible");
        }
        placeSettlementPossible = true;
        roadToSettlement = false;
        log.debug("Leaving setCities = false");
        return false;
    }

    /**
     * Client Controleert als het plaatsen van een weg in de startfase mogelijk
     * is. Houdt bij als het niet mogelijk is. Geeft door waar de laatste
     * nederzetting geplaatst werd.
     *
     * @param x x-coördinaat van de tegel
     * @param y y-coördinaat van de tegel
     * @param index wegindex (index tussen 0 en 5, startend van linkerbovenkant)
     */
    public void checkPossibleStartRoad(int x, int y, int index) {
        log.debug("Entering checkPossibleStartRoad ( x= {} y= {} index= {} )", x, y, index);
        if (!getTile(x, y).startRoadPossible((index), lastSettlement, players.get(currentPlayer).getId())) {
            placeRoadPossible = false;
        }
        if (!getTile(x, y).getType().equals(TileType.SEA)) {
            nonSeaRoad = true;
        }

        log.trace("placeRoadPossible= {} nonSeaRoad= {} ", placeRoadPossible, nonSeaRoad);
        log.debug("Leaving checkPossibleStartRoad");
    }

    /**
     * Server Controleert als het plaatsen van een weg in de startfase mogelijk
     * is. Houdt bij als het niet mogelijk is. Geeft door waar de laatste
     * nederzetting geplaatst werd.
     *
     * @param x x-coördinaat van de tegel
     * @param y y-coördinaat van de tegel
     * @param index wegindex (index tussen 0 en 5, startend van linkerbovenkant)
     */
    public void checkPossibleStartRoad(int x, int y, int index, int player) {
        log.debug("Entering checkPossibleStartRoad ( x= {} y= {} index= {} )", x, y, index);
        if (!getTile(x, y).startRoadPossible((index), lastSettlement, player)) {
            placeRoadPossible = false;
        }
        if (!getTile(x, y).getType().equals(TileType.SEA)) {
            nonSeaRoad = true;
        }

        log.trace("placeRoadPossible= {} nonSeaRoad= {} ", placeRoadPossible, nonSeaRoad);
        log.debug("Leaving checkPossibleStartRoad");
    }

    public void checkPossibleFreeRoad(int x, int y, int index, int player) {
        //log.debug("Entering checkPossibleStartRoad ( x= {} y= {} index= {} )" , x, y, index);
        if (getTile(x, y).roadPossible((index), player)) {
            placeRoadPossible = true;

        }
        if (!getTile(x, y).getType().equals(TileType.SEA)) {
            nonSeaRoad = true;
        }

        log.trace("placeRoadPossible= {} nonSeaRoad= {} ", placeRoadPossible, nonSeaRoad);
        log.debug("Leaving checkPossibleStartRoad");
    }

    public void setPlaceRoadPossible(boolean placeRoadPossible) {
        this.placeRoadPossible = placeRoadPossible;
    }

    /**
     * Client Controleert als het plaatsen van een weg in de startfase mogelijk
     * is. Houdt bij als het niet mogelijk is. Geeft door waar de laatste
     * nederzetting geplaatst werd.
     *
     * @param x x-coördinaat van de tegel
     * @param y y-coördinaat van de tegel
     * @param index wegindex (index tussen 0 en 5, startend van linkerbovenkant)
     */
    public void checkPossibleRoad(int x, int y, int index) {
        log.debug("Entering checkPossibleRoad ( x= {} y= {} index= {}", x, y, index);

        if (getTile(x, y).roadPossible(index, players.get(currentPlayer).getId()) && players.get(currentPlayer).buildRoadPossible()) {
            placeRoadPossible = true;
        }
        if (!getTile(x, y).getType().equals(TileType.SEA)) {
            nonSeaRoad = true;
        }

        log.trace("placeRoadPossible= {} nonSeaRoad= {}", placeRoadPossible, nonSeaRoad);
        log.debug("Leaving checkPossibleRoad");

    }

    /**
     * Plaatst een weg op de tegels waarvan de indices zijn meegegeven in
     * tellers. Controleert als het wel degelijk mogelijk is om één te plaatsen.
     * Indien gelukt, wordt er een nieuwe weg aangemaakt met als eigenaar de
     * huidige Speler. Verwittigt observers als er veranderingen zijn.
     *
     * @param tellers houdt de tegels en de positie van de nieuwe road binnen de
     * tile bij waar road moeten gemaakt worden
     */
    public boolean setRoads(ArrayList<int[]> tellers) {
        log.debug("Entering setRoads ( tellers.size()= {} )", tellers.size());
        //players.get(currentPlayer).resetChangedResources();
        if (placeRoadPossible && nonSeaRoad) {
            if (askConfirmation("Plaatsen straat", "Weet je zeker dat je hier een straat wilt plaatsen?")) {
                Road road = new Road(players.get(currentPlayer));
                for (int i = 0; i < tellers.size(); i++) {
                    getTile(tellers.get(i)[0], tellers.get(i)[1]).setRoad(road, tellers.get(i)[2]);
                    log.info("Street placed at x= {} y= {} player= {}", tellers.get(i)[0], tellers.get(i)[1], players.get(currentPlayer).getName());
                }
                players.get(currentPlayer).roadBuilt();
                notifyPlayerObservers();
                notifyObservers();

                log.debug("Leaving setRoads= true");
                return true;
            }
        } else {
            setMessages("", "noStreetPossible");
        }
        placeRoadPossible = false;
        nonSeaRoad = false;
        log.debug("Leave setRoad= false");
        return false;
    }

    /**
     * Server Plaatst in aangrenzende tegels (meegekregen uit view) de weg op de
     * juiste index. Controleert als het mogelijk is om de weg te plaatsen.
     * Zorgt ervoor dat de constructieaantallen aangepast worden. Verwittigt
     * observers als er veranderingen zijn.
     *
     * @param tellers lijst van {x-coördinaat tegel, y-coördinaat tegel,
     * wegindex op tegel}
     * @return true als plaatsen van weg gelukt is
     */
    public boolean setStartRoads(ArrayList<int[]> tellers, int playerId) {
        log.debug("Entering setStartRoads ( tellers.size()= {} )", tellers.size());

        if (!makeRoad[currentPlayer]) {
            return false;
        }

        if (placeRoadPossible && nonSeaRoad) {
            //if(askConfirmation("Plaatsen startstraat","Weet je zeker dat je hier een startstraat wilt plaatsen?")){
            Road road = new Road(nameToPlayer(playerId));
            for (int i = 0; i < tellers.size(); i++) {
                getTile(tellers.get(i)[0], tellers.get(i)[1]).setStartRoad(road, tellers.get(i)[2]);
                log.info("Start road placed at x= {} y= {} player= {}", tellers.get(i)[0], tellers.get(i)[1], playerId);
            }
            nameToPlayer(playerId).startRoadBuilt();
            nonSeaRoad = false;
            notifyObservers();

            log.debug("Leaving setStartRoads=true");
            startBuilding++;
            makeRoad[currentPlayer] = false;
            return true;
            //}

        } else {
            setMessages("", "noStreetPossible");
        }
        placeRoadPossible = true;
        nonSeaRoad = false;
        log.debug("Leaving setStartRoads=false");
        return false;
    }

    /**
     * Plaatst in aangrenzende tegels (meegekregen uit view) de weg op de juiste
     * index [STARTFASE]. Controleert als plaatsen van nederzetting mogelijk is.
     * Past de laatst geplaatste nederzetting aan met de nieuwe indien nodig.
     * Zorgt ervoor dat de constructieaantallen aangepast worden. Verwittigt
     * observers als er veranderingen zijn.
     *
     * @param tellers lijst van {x-coördinaat tegel, y-coördinaat tegel,
     * nederzettingindex op tegel}
     * @return true als plaatsen van nederzetting gelukt is
     */
    public ISettlement setStartSettlements(ArrayList<int[]> tellers) {
        //endGame();
        //System.out.println("hoi");
        //players.get(currentPlayer).resetChangedResources();
        log.debug("setStartSettlements ( tellers.size()= {} )", tellers.size());
        //if(startBuilding%(2*players.size())!=(startingPlayer*2+1)%(2*players.size()))
        //    return null;
        if (!beginVillage[currentPlayer]) {
            System.out.println("mag nu niet bouwen door vreemde omstandigheden");

            return null;
        }
        System.out.println("beginvvillage[startingplayer]: " + beginVillage[startingPlayer]);
        if (placeSettlementPossible && nonSeaStartSettlement) {
            //if(askConfirmation("Plaatsen startdorp","Weet je zeker dat je hier een startdorp wilt plaatsen?")){
            ISettlement settlement = new Village(players.get(currentPlayer));
            for (int i = 0; i < tellers.size(); i++) {

                getTile(tellers.get(i)[0], tellers.get(i)[1]).setSettlement((settlement), tellers.get(i)[2]);
                log.info("Start settlement placed at x= {} y= {} player= {}", tellers.get(i)[0], tellers.get(i)[1], players.get(currentPlayer).getName());

                if (((!isServer && lastSettlement != null) || numberStructures >= players.size())
                        && getTile(tellers.get(i)[0], tellers.get(i)[1]).getType() != TileType.DESERT
                        && getTile(tellers.get(i)[0], tellers.get(i)[1]).getType() != TileType.SEA) {
                    players.get(currentPlayer).addResource(getTile(tellers.get(i)[0], tellers.get(i)[1]).getType());
                    notifyPlayerObservers();
                    beginVillage[currentPlayer] = false;

                }
            }
            /*   if(lastSettlement != null){
             for(int i=0;i<tellers.size();i++){
             if(getTile(tellers.get(i)[0], tellers.get(i)[1]).getType().getValue()<5)
             bank.removeResource(getTile(tellers.get(i)[0], tellers.get(i)[1]).getType());
             System.out.println("remove");
             }
             }*/
            //lastSettlement=settlement;
            players.get(currentPlayer).startSettlementBuilt();
            nonSeaStartSettlement = false;
            notifyObservers();
            log.debug("Leaving setStartSettlements= true");
            startBuilding++;
            return settlement;
            //}

        } else {
            setMessages("", "noVillagePossible");
        }
        placeSettlementPossible = true;
        nonSeaStartSettlement = false;
        log.debug("Leaving setStartSettlements= false");
        return null;
    }

    /**
     * Start de gamefase.
     */
    public void initiateGameFase(int playerId) {
        placeRoadPossible = false;
        placeSettlementPossible = true;
        gameFase = true;
        nameToPlayer(playerId).goGameFase();

        log.debug("Game fase initiated");
    }

    /**
     * Toont een pop-upscherm met de vraag als je wel zeker iets wil bouwen op
     * die locatie.
     *
     * @param titel titel van het pop-upscherm
     * @param vraag vraag in pop-upscherm
     * @return antwoord op vraag (ja/nee)
     */
    private boolean askConfirmation(String titel, String vraag) {
        log.debug("Entering askConfirmation ( titel= {} vraag= {}", titel, vraag);

        JCheckBox checkbox = new JCheckBox("Toon dit bericht niet meer");
        String message = vraag;
        Object[] params = {message, checkbox};
        if (dontShowOptionPanesAgain) {
            log.debug("Return from askConfirmation= true");
            return true;
        } else {
            boolean showOptionPane = JOptionPane.showOptionDialog(null, params, titel, JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null, new String[]{"Ja", "Nee"}, "default") == JOptionPane.YES_OPTION;
            dontShowOptionPanesAgain = checkbox.isSelected();

            log.debug("Return rom askConfirmation= {}", showOptionPane);
            return showOptionPane;
        }
    }

    private void notifyPlayerObservers() {

        log.debug("Entering notifyPlayerObservers");

        for (IPlayer player : players) {
            log.trace("player {}", player.getName());
            player.notifyYourObservers();

        }


        log.debug("Leaving notifyPlayerObservers");
    }

    /**
     * Voegt het commando toe.
     *
     * @param command commando
     */
    public void addCommand(ICommand command) {
        log.debug("Command added to board");
        commands.add(command);
    }

    /**
     * Geeft terug hoeveel commando's er zijn gebeurt.
     *
     * @return aantal commando's
     */
    public int getCommandSize() {
        log.debug("return from getCommandSize= {}", commands.size());
        return commands.size();
    }

    /**
     * Ga naar volgende speler op het servermodel, berekent eerst wie het wordt
     * volgens de spellogica.
     *
     * @param player speler die het oproept
     */
    public void nextPlayer(int player) {
        log.debug("Entering nextPlayer");

        if (getPlayerPlace(player) == currentPlayer) {
            maySteal[currentPlayer] = true;
            //PlayersTurn[currentPlayer]=false;
            //rolled=true;
            for (int i = 0; i < maxPlayers; i++) {
                beginVillage[i] = false;
                makeRoad[i] = false;
                rollDice[i] = false;
            }
            if (isStructureFase) {
                if (numberStructures == players.size() - 1 || numberStructures == 2 * players.size() - 1) {
                } else if (numberStructures >= players.size()) {
                    currentPlayer = (players.size() + currentPlayer - 1) % players.size();
                } else {
                    currentPlayer = (currentPlayer + 1) % players.size();
                }
                numberStructures++;
                if (numberStructures == players.size() * 2) {
                    isStructureFase = false;
                    rollDice[currentPlayer] = true;
                }
                beginVillage[currentPlayer] = true;
                makeRoad[currentPlayer] = true;
            } else {
                currentPlayer = (currentPlayer + 1) % players.size();
                rollDice[currentPlayer] = true;
            }
            //PlayersTurn[currentPlayer]=true;
            log.info("Next player's( {} ) turn", currentPlayer);
            log.debug("Leaving nextPlayer");
        }
    }

    /**
     * Client-only Voegt het dorp/de stad toe aan het Board zonder te testen
     * aangezien dit al op de server gebeurt is.
     *
     * @param tellers lijst van {x-coördinaat tegel, y-coördinaat tegel,
     * nederzettingindex op tegel}
     * @param player eigenaar van het dorp/de stad
     */
    public void setSettlementsWithoutTesting(ArrayList<int[]> tellers, int playerId) {
        log.debug("Entering setSettlementsWithoutTesting ( tellers.size()= {} , player= {} )", tellers.size(), playerId);

        IPlayer p = nameToPlayer(playerId);
        for (int i = 0; i < tellers.size(); i++) {
            getTile(tellers.get(i)[0], tellers.get(i)[1]).setSettlement(new Village(p), tellers.get(i)[2]);
            log.info("Settlement set without testing at x= {} y= {} player= {}", tellers.get(i)[0], tellers.get(i)[1], playerId);
        }
//        p.settlementBuilt();
    }

    public void setCityWithoutTesting(ArrayList<int[]> tellers, int playerId) {
        log.debug("Entering setCityWithoutTesting ( tellers.size()= {} , player= {} )", tellers.size(), playerId);

        IPlayer p = nameToPlayer(playerId);
        for (int i = 0; i < tellers.size(); i++) {
            getTile(tellers.get(i)[0], tellers.get(i)[1]).setSettlement(new City(p), tellers.get(i)[2]);
            log.info("City set without testing at x= {} y= {} player= {}", tellers.get(i)[0], tellers.get(i)[1], playerId);
        }
//        p.CityBuilt();
    }

    /**
     * Geeft de naam van de speler die aan beurt is.Of de eigenaar van het board
     * op de clients
     *
     * @return speler aan beurt.
     */
    public String getCurrentPlayerName() {
        if (isSpectatorMode) {
            return null;
        } else {
            log.debug("return getCurrentPlayer= {}", players.get(currentPlayer).getName());
            return players.get(currentPlayer).getName();
        }
    }

    public IPlayer getCurrentPlayer() {

        return players.get(currentPlayer);
    }

    /**
     * Client-only Plaatst een weg op de opgegeven locatie zonder te
     * controleren.(reeds op server gebeurd)
     *
     * @param tellers lijst van {x-coördinaat tegel, y-coördinaat tegel,
     * nederzettingindex op tegel}
     * @param playerId eigenaar van weg
     */
    public void setRoadsWithoutTesting(ArrayList<int[]> tellers, int playerId) {
        log.debug("Entering setRoadsWithoutTesting tellers.size()= {} player= {}", tellers.size(), playerId);

        IPlayer p = nameToPlayer(playerId);
        Road road = new Road(p);
        for (int i = 0; i < tellers.size(); i++) {
            getTile(tellers.get(i)[0], tellers.get(i)[1]).setRoad(road, tellers.get(i)[2]);
            log.info("Road set without testing at x= {} y= {} player= {}", tellers.get(i)[0], tellers.get(i)[1], playerId);
        }
        //      p.roadBuilt();
        log.debug("Leaving setRoadsWithoutTesting");
    }

    /**
     * plaats een vrij verkregen weg na het kaartje stratenbouw te spelen
     *
     * @param tellers
     * @param playerId
     * @return
     */
    public boolean setFreeRoads(ArrayList<int[]> tellers, int playerId) {
        log.debug("Entering setRoadsWithoutTesting tellers.size()= {} player= {}", tellers.size(), playerId);

        if (placeRoadPossible && nonSeaRoad && mayPlayDevCardType[currentPlayer][DevCardType.ROAD.getValue()]) {//&&PlayCard2Stuff[currentPlayer]){
            IPlayer p = nameToPlayer(playerId);


            Road road = new Road(p);
            for (int i = 0; i < tellers.size(); i++) {
                getTile(tellers.get(i)[0], tellers.get(i)[1]).setRoad(road, tellers.get(i)[2]);
                log.info("Road set without testing at x= {} y= {} player= {}", tellers.get(i)[0], tellers.get(i)[1], playerId);
            }
            //      p.roadBuilt();
            log.debug("Leaving setRoadsWithoutTesting");
            //PlayCard[currentPlayer]=false;

            if (howmanyPlayed > 0) {
                mayPlayDevCardType[currentPlayer][DevCardType.ROAD.getValue()] = false;
                howmanyPlayed = 0;
            } else {
                howmanyPlayed++;
            }
            return true;

        }
        return false;
    }

    /**
     * Client-only Plaatst een startweg zonder te controleren.
     *
     * @param tellers lijst van {x-coördinaat tegel, y-coördinaat tegel,
     * nederzettingindex op tegel}
     * @param playerId eigenaar van de weg
     */
    public void setStartRoadsWithoutTesting(ArrayList<int[]> tellers, int playerId) {
        log.debug("Entering setStartRoadsWithoutTesting tellers.size()= {} player= {}", tellers.size(), playerId);
        IPlayer p = nameToPlayer(playerId);
        Road road = new Road(p);
        for (int i = 0; i < tellers.size(); i++) {
            getTile(tellers.get(i)[0], tellers.get(i)[1]).setStartRoad(road, tellers.get(i)[2]);
            log.info("Start road set without testing at x= {} y= {} player= {}", tellers.get(i)[0], tellers.get(i)[1], playerId);
        }

        log.debug("Leaving setStartRoadsWithoutTesting");
    }

    /**
     * Plaatst een startdorp op de opgegeven locaties.
     *
     * @param tellers lijst van {x-coördinaat tegel, y-coördinaat tegel,
     * nederzettingindex op tegel}
     * @param player eigenaar van het startdorp
     */
    public void setStartSettlementsWithoutTesting(ArrayList<int[]> tellers, ISettlement settlement) {
        //log.debug("Entering setStartSettlementsWithoutTesting ( tellers.size()= {} player= {} )", tellers.size(), player);

        //IPlayer p=nameToPlayer(player);
        for (int i = 0; i < tellers.size(); i++) {
            getTile(tellers.get(i)[0], tellers.get(i)[1]).setSettlement(settlement, tellers.get(i)[2]);
            //log.info("Start settlement set without testing at x= {} y= {} player= {}", tellers.get(i)[0],tellers.get(i)[1],player);
        }

        log.debug("Leaving setSTartSettlementWithoutTesting");
    }

    /**
     * maakt dorp clientside
     *
     * @param tellers
     * @param playerId
     */
    public void setStartSettlementsWithoutTesting(ArrayList<int[]> tellers, int playerId) {
        //log.debug("Entering setStartSettlementsWithoutTesting ( tellers.size()= {} player= {} )", tellers.size(), player);

        //IPlayer p=nameToPlayer(player);
        Village village = new Village(nameToPlayer(playerId));
        for (int i = 0; i < tellers.size(); i++) {
            getTile(tellers.get(i)[0], tellers.get(i)[1]).setSettlement(village, tellers.get(i)[2]);
            //log.info("Start settlement set without testing at x= {} y= {} player= {}", tellers.get(i)[0],tellers.get(i)[1],player);
        }
        lastSettlement = village;
        log.debug("Leaving setSTartSettlementWithoutTesting");
    }

//    public void placeThiefRed() {
//        log.debug("placeThiefRed");
//        
//        for (int i = 0 ; i < XDIM ; i++){
//            for (int j = 0 ; j < YDIM ; j++){
//                if (tiles[i][j] != null && tiles[i][j].hasThief()){
//                    tiles[i][j].setRedThief(true);
//                    log.trace("set thief red at x= {} y= {}", i , j);
//                    notifyObservers();
//                }
//            }
//        }
//    }
    /**
     * Geeft alle commando's vanaf index
     * <code>begin</code> (begin inbegrepen)
     *
     * @param begin startindex
     * @return lijst van commando's
     */
    public ArrayList<ICommand> getCommands(int begin) {
        log.debug("Entering getCommands ( begin= {})", begin);

        ArrayList<ICommand> newCommands = new ArrayList<ICommand>();
        for (int i = begin; i < commands.size(); i++) {
            newCommands.add(commands.get(i));
            log.trace("command with index = {} added", i);
        }

        log.debug("Leaving getCommands");
        log.info("actions synchronized");
        return newCommands;
    }

    public Bank getBank() {
        log.debug("Return bank");
        return bank;
    }

    public int[] getBankValues() {
        log.debug("return bankValues");
        int[] values = new int[5 + 2 * players.size()];
        int[] bankValues = bank.getResourceValues();
        for (int i = 0; i < 5; i++) {
            values[i] = bankValues[i];
        }
        for (int i = 0; i < players.size(); i++) {
            values[i + 5] = players.get(i).getScore();
        }
        for (int i = 0; i < players.size(); i++) {
            values[i + 5 + players.size()] = players.get(i).getNumberKnights();
        }
        return values;//bank.getResourceValues();
    }

    public void setBankValues(int[] values) {
        log.debug("set bankValues");
        bank.setResourceValues(values);
    }

    /**
     * Geeft alle spelernamen terug.
     *
     * @return alle spelernamen
     */
    public String[] getPlayerNames() {
        log.debug("getPlayerNames");

        String[] playernames = new String[players.size()];
        for (int i = 0; i < players.size(); i++) {
            playernames[i] = players.get(i).getName();
            log.trace("player {} added to list", players.get(i).getName());
        }

        log.debug("returning playernames");
        return playernames;
    }

    /**
     * Geeft de namen van de tegenspelers terug.
     *
     * @param id eigen speler
     * @return namen tegenspelers
     */
    public String[] getOtherPlayerNames(int id) {
        log.debug("getOtherPlayerNames");

        String[] playernames = new String[players.size() - 1];
        int j = 0;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId() != id) {
                playernames[j] = players.get(i).getName();
                log.trace("player {} added to list", players.get(i).getName());
                j++;
            }
        }

        log.debug("returning Otherplayernames");
        return playernames;
    }

    /**
     * Geeft alle spelerid's terug.
     *
     * @return alle spelerid's
     */
    public int[] getPlayerIds() {
        log.debug("getPlayerIds");

        int[] playerids = new int[players.size()];
        for (int i = 0; i < players.size(); i++) {
            playerids[i] = players.get(i).getId();
            log.trace("player {} added to list", players.get(i).getName());
        }

        log.debug("returning playerids");
        return playerids;
    }

    public int[] getOtherPlayerIds(int id) {
        log.debug("getOtherPlayerIds");

        int[] playerids = new int[players.size() - 1];
        int j = 0;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId() != id) {
                playerids[j] = players.get(i).getId();
                log.trace("player {} added to list", players.get(i).getName());
                j++;
            }
        }

        log.debug("returning otherplayerids");
        return playerids;
    }

    public void addObserver(IObserver obs) {
        log.debug("Observer added to board");
        observers.add(obs);
    }

    public void removeObserver(IObserver obs) {
        log.debug("Observer removed from board");
        observers.remove(obs);
    }

    public void notifyObservers(String arg) {
        log.debug("Entering notifyObservers ( arg= {} )", arg);
        for (IObserver obs : observers) {
            obs.update(this, arg);
            log.trace("update observer= {} with arg= {}", obs, arg);
        }
    }

    public void notifyObservers() {
        log.debug("Entering notifyObservers");

        for (IObserver obs : observers) {
            obs.update(this, null);
            log.trace("update observer= {}", obs);
        }

    }

    public Dices getDices() {
        log.debug("getDices");
        //dices.generate();
        return dices;
    }

    public void setDices(int dice1, int dice2) {
        dices.setDices(dice1, dice2);
        log.debug("dices set dice1= {} dice2 = {}", dice1, dice2);
    }

    public Color[] getColors() {
        log.debug("returning colors");
        return colors;
    }

    /**
     * bepaald de juiste highlightmethode en voert hem uit
     *
     * @param i bepaald welke highlightmethode moet aangeroepen worden
     * @param start al dan niet startfase
     * @return
     */
    public boolean highlight(int i, boolean start) {
        log.debug("Entering highlight ( i= {} start= {} )", i, start);
        if ((buildRoad && i == 0) || (buildVillage && i == 1) || (buildCity && i == 2)) {
            deselectHighlight();
        } else {
            deselectHighlight();
            if (i == 0) {
                buildRoad = true;
                //log.debug("returning from highlight= {}", highlightRoads(start));
                return highlightRoads(start, false);
            } else if (i == 1) {

                buildVillage = true;
                log.debug("returning from highlight= {}", highlightVillages(start));
                return highlightVillages(start);
            } else if (i == 2) {
                System.out.println("highlight cities");
                highlightCities();
                buildCity = true;
            }
        }

        log.debug("returning from highlight = false");
        return false;

    }

    /**
     * loopt alle dorpen af om te kijken of ze gehighlight mogen worden
     *
     * @param start zit men in startfase
     * @return
     */
    private boolean highlightVillages(boolean start) {
        log.debug("Entering highlightVillages ( start= {} )", start);

        boolean b = false;
        for (int x = 1; x < XDIM - 1; x++) { // 0 is zee
            for (int y = 1; y < NUMBERTILES[x] - 1; y++) {
                if (x % 2 == 1 || (y == 1 || y == NUMBERTILES[x] - 2)) {
                    for (int k = 0; k < 6; k++) {
                        ArrayList<int[]> tellers = berekentegels(x, y, k);
                        tellers.add(new int[]{x, y, k});
                        for (int[] tel : tellers) {
                            if (start) {
                                checkPossibleStartSettlement(tel[0], tel[1], tel[2]);
                            } else {
                                checkPossibleSettlement(tel[0], tel[1], tel[2]);
                            }
                        }
                        if (start) {
                            if (showPossibleStartVillages(tellers)) {
                                b = true;
                            }
                        }
                        if (showPossiblePlacesVillages(tellers)) {
                            b = true;
                        }
                        //showPossiblePlacesCities(x,y,k);
                    }
                }
            }
        }
        log.debug("returning from highlightVillages= {}", b);
        return b;
    }
//    public void highlightRoadsWithoutTesting(boolean start){
//        placeRoadPossible=false;
//        nonSeaRoad=false;
//        for (int x = 1 ; x < XDIM-1 ; x++){ // 0 is zee
//            for (int y = 1 ; y < NUMBERTILES[x]-1 ; y++){
//                for(int k=0;k<6;k++){
//                    showPossibleRoads(x,y,k,start);
//                }
//            }
//        }
//    }

    /**
     * loopt alle straten af om te kijken of ze gehighlight mogen worden
     *
     * @param start zit men in startfase
     * @param freeroad gaat het over een weg verkregen door een
     * ontwikkelingskaartje
     * @return
     */
    public boolean highlightRoads(boolean start, boolean freeroad) {
        log.debug("Entering highlightRoads ( start= {} )", start);
        placeRoadPossible = false;
        nonSeaRoad = false;
        boolean b = false;
        for (int x = 1; x < XDIM - 1; x++) { // 0 is zee
            for (int y = 1; y < NUMBERTILES[x] - 1; y++) {
                for (int k = 0; k < 6; k++) {
                    if (start) {
                        checkPossibleStartRoad(x, y, k);
                    } else {
                        checkRoad(x, y, k, freeroad);
                        if (k == 0) {
                            checkRoad(x, y - 1, (k + 3) % 6, freeroad);
                        }
                        if (k == 1 && x <= 3) {
                            checkRoad(x - 1, y - 1, (k + 3) % 6, freeroad);
                        }
                        if (k == 1 && x > 3) {
                            checkRoad(x - 1, y, (k + 3) % 6, freeroad);
                        }
                        if (k == 2 && x > 3) {
                            checkRoad(x - 1, y + 1, (k + 3) % 6, freeroad);
                        }
                        if (k == 2 && x <= 3) {
                            checkRoad(x - 1, y, (k + 3) % 6, freeroad);
                        }
                        if (k == 3) {
                            checkRoad(x, y + 1, (k + 3) % 6, freeroad);
                        }
                        if (k == 4 && x < 3) {
                            checkRoad(x + 1, y + 1, (k + 3) % 6, freeroad);
                        }
                        if (k == 4 && x >= 3) {
                            checkRoad(x + 1, y, (k + 3) % 6, freeroad);
                        }
                        if (k == 5 && x < 3) {
                            checkRoad(x + 1, y, (k + 3) % 6, freeroad);
                        }
                        if (k == 5 && x >= 3) {
                            checkRoad(x + 1, y - 1, (k + 3) % 6, freeroad);
                        }

                    }
                    if (showPossibleRoads(x, y, k, start)) {
                        b = true;
                    }
                }
            }
        }
        log.debug("returning from highlightRoads= {}", b);
        return b;
    }

    private void checkRoad(int x, int y, int k, boolean free) {
        if (free) {
            checkPossibleFreeRoad(x, y, k);
        } else {
            checkPossibleRoad(x, y, k);
        }
    }

    /**
     * llopt alle steden af om te kijken of ze gehighlight mogen worden
     */
    private void highlightCities() {
        log.debug("highlight Cities");

        for (int x = 1; x < XDIM - 1; x++) { // 0 is zee
            for (int y = 1; y < NUMBERTILES[x] - 1; y++) {
                if (x % 2 == 1 || (y == 1 || y == NUMBERTILES[x] - 2)) {
                    for (int k = 0; k < 6; k++) {
                        ArrayList<int[]> tellers = berekentegels(x, y, k);
                        tellers.add(new int[]{x, y, k});
                        for (int[] tel : tellers) {
                            //if(getTile(x, y).cityPossible(k, players.get(currentPlayer).getName())){
                            checkPossibleSettlement(tel[0], tel[1], tel[2]);

                            //}
                        }
                        showPossiblePlacesCities(tellers);
                    }
                }
            }
        }
    }

    /**
     * highlight de stad indien mogelijk
     *
     * @param tellers
     * @return
     */
    public void showPossiblePlacesCities(ArrayList<int[]> tellers) {
        log.debug("Entering showPossibleCities ( tellers.size()= {}", tellers.size());

        if (placeCity) {

            for (int i = 0; i < tellers.size(); i++) {
                getTile(tellers.get(i)[0], tellers.get(i)[1]).setTempCity(tellers.get(i)[2]);
            }

            notifyObservers();
        }
        placeSettlementPossible = true;
        roadToSettlement = false;
        placeCity = false;
        log.debug("leaving showPossiblePlacesCities");
    }

    /**
     * deselecteerd alles wat gehighlight is
     */
    public void deselectHighlight() {
        log.debug("Entering deselectHighlight");
        for (int x = 1; x < XDIM - 1; x++) { // 0 is zee
            for (int y = 1; y < NUMBERTILES[x] - 1; y++) {
                //if(x%2==1||(y==1||y==NUMBERTILES[x]-2)){
                getTile(x, y).deselect();
                //}
            }
        }
        buildRoad = false;
        buildVillage = false;
        buildCity = false;


        notifyObservers();
        log.debug("leaving deselectHighlight");
    }

    /**
     * kijkt of een startdorp mag gebouwd worden (om te highlighten)
     *
     * @param tellers
     * @return
     */
    private boolean showPossibleStartVillages(ArrayList<int[]> tellers) {
        log.debug("Entering showPossibleStartVillages ( tellers.size()= {}", tellers.size());

        if (placeSettlementPossible && nonSeaStartSettlement) {
            for (int i = 0; i < tellers.size(); i++) {
                getTile(tellers.get(i)[0], tellers.get(i)[1]).setTempSettlement(tellers.get(i)[2]);
            }
            nonSeaStartSettlement = false;
            log.debug("leaving showPossibleStartVillages= true");
            return true;
        }
        log.debug("leaving showPossibleStartVillages= false");
        return false;
    }

    /**
     * kijkt of een dorp mag gebouwd worden (om te highlighten)
     *
     * @param tellers
     * @return
     */
    private boolean showPossiblePlacesVillages(ArrayList<int[]> tellers) {
        log.debug("Entering showPossiblePlacesVillages ( tellers.size()= {}", tellers.size());
        if (placeSettlementPossible && roadToSettlement) {
            for (int i = 0; i < tellers.size(); i++) {
                getTile(tellers.get(i)[0], tellers.get(i)[1]).setTempSettlement(tellers.get(i)[2]);
            }
            notifyObservers();
            placeSettlementPossible = true;
            roadToSettlement = false;
            log.debug("Leaving showPossiblePlacesVillages= true");
            return true;
        }
        placeSettlementPossible = true;
        roadToSettlement = false;
        log.debug("Leaving showPossiblePlacesVillages= false");
        return false;
    }

    /**
     * geeft de plaats van een dorp maar nu gezien via aangrenzende tegels
     *
     * @param x
     * @param y
     * @param k
     * @return
     */
    private ArrayList<int[]> berekentegels(int x, int y, int k) {
        log.debug("Entering berekentegels ( x= {} y= {} k= {}", x, y, k);

        ArrayList<int[]> tellers = new ArrayList<int[]>();
        tellers.add(new int[3]);
        tellers.add(new int[3]);
        if (x <= 2 || (x == 3 && k >= 1 && k <= 3)) {
            if (k == 0) {
                tellers.get(0)[0] = x;
                tellers.get(0)[1] = y - 1;
                tellers.get(0)[2] = (k + 6 - 2) % 6;

                tellers.get(1)[0] = x + 1;
                tellers.get(1)[1] = y;
                tellers.get(1)[2] = (k + 2) % 6;
            } else if (k == 1) {
                tellers.get(0)[0] = x;
                tellers.get(0)[1] = y - 1;
                tellers.get(0)[2] = (k + 2) % 6;

                tellers.get(1)[0] = x - 1;
                tellers.get(1)[1] = y - 1;
                tellers.get(1)[2] = (k + 4) % 6;
            } else if (k == 2) {
                tellers.get(0)[0] = x - 1;
                tellers.get(0)[1] = y;
                tellers.get(0)[2] = (k + 4) % 6;

                tellers.get(1)[0] = x - 1;
                tellers.get(1)[1] = y - 1;
                tellers.get(1)[2] = (k + 2) % 6;
            } else if (k == 3) {
                tellers.get(0)[0] = x - 1;
                tellers.get(0)[1] = y;
                tellers.get(0)[2] = (k + 2) % 6;

                tellers.get(1)[0] = x;
                tellers.get(1)[1] = y + 1;
                tellers.get(1)[2] = (k + 4) % 6;
            } else if (k == 4) {
                tellers.get(0)[0] = x + 1;
                tellers.get(0)[1] = y + 1;
                tellers.get(0)[2] = (k + 4) % 6;

                tellers.get(1)[0] = x;
                tellers.get(1)[1] = y + 1;
                tellers.get(1)[2] = (k + 2) % 6;
            } else if (k == 5) {
                tellers.get(0)[0] = x + 1;
                tellers.get(0)[1] = y + 1;
                tellers.get(0)[2] = (k + 2) % 6;

                tellers.get(1)[0] = x + 1;
                tellers.get(1)[1] = y;
                tellers.get(1)[2] = (k + 4) % 6;
            }
        } else if (x >= 4 || (x == 3 && (k == 0 || k == 4 || k == 5))) {
            if (k == 0) {
                tellers.get(0)[0] = x;
                tellers.get(0)[1] = y - 1;
                tellers.get(0)[2] = (k + 6 - 2) % 6;

                tellers.get(1)[0] = x + 1;
                tellers.get(1)[1] = y - 1;
                tellers.get(1)[2] = (k + 2) % 6;
            } else if (k == 1) {
                tellers.get(0)[0] = x;
                tellers.get(0)[1] = y - 1;
                tellers.get(0)[2] = (k + 2) % 6;

                tellers.get(1)[0] = x - 1;
                tellers.get(1)[1] = y;
                tellers.get(1)[2] = (k + 4) % 6;
            } else if (k == 2) {
                tellers.get(0)[0] = x - 1;
                tellers.get(0)[1] = y + 1;
                tellers.get(0)[2] = (k + 4) % 6;

                tellers.get(1)[0] = x - 1;
                tellers.get(1)[1] = y;
                tellers.get(1)[2] = (k + 2) % 6;
            } else if (k == 3) {
                tellers.get(0)[0] = x - 1;
                tellers.get(0)[1] = y + 1;
                tellers.get(0)[2] = (k + 2) % 6;

                tellers.get(1)[0] = x;
                tellers.get(1)[1] = y + 1;
                tellers.get(1)[2] = (k + 4) % 6;
            } else if (k == 4) {
                tellers.get(0)[0] = x + 1;
                tellers.get(0)[1] = y;
                tellers.get(0)[2] = (k + 4) % 6;

                tellers.get(1)[0] = x;
                tellers.get(1)[1] = y + 1;
                tellers.get(1)[2] = (k + 2) % 6;
            } else if (k == 5) {
                tellers.get(0)[0] = x + 1;
                tellers.get(0)[1] = y;
                tellers.get(0)[2] = (k + 2) % 6;

                tellers.get(1)[0] = x + 1;
                tellers.get(1)[1] = y - 1;
                tellers.get(1)[2] = (k + 4) % 6;
            }
        }

        log.debug("returning from berekentegels");
        return tellers;

    }

    public boolean waitForOtherPlayers() {
        log.debug("returning getNumberOfPlayers= {}", players.size());
        return (playersLoggedIn < maxPlayers);
    }

    public ArrayList<IPlayer> getAllPlayers() {
        log.debug("returning players size={}", players.size());
        return players;
    }

    /**
     * kijkt of een weg mag gebouwd worden (om te highlighten)
     *
     * @param x xcoordinaat
     * @param y ycoordinaat
     * @param k nummer van de plaats op de xy-tegel
     * @param start in startfase of niet
     * @return
     */
    private boolean showPossibleRoads(int x, int y, int k, boolean start) {
        log.debug("Entering showPossibleRoads ( x= {} y= {} k= {} start= {} )", x, y, k, start);

        if (placeRoadPossible && nonSeaRoad) {
            getTile(x, y).setTempRoad(k);
            notifyObservers();
            if (start) {
                placeRoadPossible = true;
            } else {
                placeRoadPossible = false;
            }
            nonSeaRoad = false;
            log.debug("returning from showPossibleRoads= true");
            return true;
        }

        if (start) {
            placeRoadPossible = true;
        } else {
            placeRoadPossible = false;
        }
        nonSeaRoad = false;
        log.debug("returning from showPossibleRoads= false");
        return false;
    }

    /**
     *
     * @return aantal grondstoffen, overwinningspunten, ridders van iedereen
     */
    public int[] getPlayerValues() {
        int[] values = new int[players.size() * 3 + 1];
        for (int i = 0; i < players.size(); i++) {
            values[i] = players.get(i).getTotalResources();
        }
        for (int i = 0; i < players.size(); i++) {
            values[i + players.size()] = players.get(i).getScore();
        }
        for (int i = 0; i < players.size(); i++) {
            values[i + players.size() * 2] = players.get(i).getNumberKnights();
        }
        for (int i = 0; i < values.length; i++) {
            //  System.out.print(values[i]+" ");
        }
        int playerwithKnight = -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).hasKnightTrophy()) {
                playerwithKnight = i;
            }
        }
        values[players.size() * 3] = playerwithKnight;
        log.debug("returning PlayerValues");
        return values;
    }

    /**
     * Wist het laatst gerolde getal.
     */
    public void clearLastRolledNumber() {
        list = getTilesWithNumber(lastRolledNumber);
        if (!list.isEmpty()) {
            for (ITile tile : list) {
                tile.setRolled(false); //tile aanduiding wegdoen
            }
        }
        log.debug("LastRolledNumber cleared");
    }

    public void setMessages(String info, String changes) {
        log.debug("Entering setMessages ( info= {} changes= {}", info, changes);

        this.info = info;

        this.changes = changes;

        notifyObservers("");
    }

    public String getInfo() {
        log.debug("getInfo");
        return info;
    }

    public String getChanges() {
        log.debug("getChanges");
        return changes;
    }

    public Color getOwnColor() {
        return players.get(currentPlayer).getColor();
    }

    public boolean isGameFase() {
        log.debug("Returning isGameFase = {}", gameFase);
        return gameFase;
    }

    public boolean isBuildVillage() {
        log.debug("Returning isBuildVillage = {}", buildVillage);
        return buildVillage;
    }

    public boolean isBuildRoad() {
        log.debug("Returning isBuildRoad = {}", buildRoad);
        return buildRoad;
    }

    public boolean isBuildCity() {
        log.debug("Returning isBuildCity = {}", buildCity);
        return buildCity;
    }

    public IPlayer getPlayer(int playerId) {
        return players.get(idToIndex.get(playerId));
    }

    public int getPlayerPlace(int playerId) {
        return idToIndex.get(playerId);
    }

    public int[] getMyResources(int playerId) {
        return nameToPlayer(playerId).getResourceValues();

    }

    public void setMyResources(int[] resourceValues) {
        players.get(currentPlayer).setResourceValues(resourceValues);
    }

    private IPlayer nameToPlayer(int playerId) {
        return players.get(idToIndex.get(playerId));
    }

    /**
     * Zet de spectator mode on/off.
     *
     * @param spectator
     */
    public void setSpectatorMode(boolean spectator) {
        this.isSpectatorMode = spectator;
    }

    /**
     * als speler kan betalen neem zijn grondstoffen af en verwijder de gekochte
     * kaart van de stapel
     *
     * @param playerId speler die de kaart koopt
     * @param card kaart die gekocht wordt
     * @return true als gekocht, anders false
     */
    public DevCardType buyDevCard(int playerId, DevCardType card) {
        DevCardType _card = cards.get(cards.size() - 1);
        if (nameToPlayer(playerId).addCard(_card)) {
            cards.remove(cards.size() - 1);
            return _card;
        }
        return null;
    }

    /**
     * steekst de kaarten in een random volgorde
     *
     * @param cards
     */
    private void shuffle(ArrayList<DevCardType> cards) {
        for (int i = 0; i < cards.size(); i++) {
            int n = diceRnd.nextInt(cards.size());
            DevCardType temp = cards.get(n);
            cards.set(n, cards.get(cards.size() - 1));
            cards.set(cards.size() - 1, temp);
        }
    }

    /**
     * geeft de huidige speler een kaart, kan alleen opgeroepen worden nadat hij
     * hem al betaald heefd
     *
     * @return de kaart die hij krijgt
     */
    public DevCardType getCard() {

        DevCardType card = cards.get(cards.size() - 1);
//        cards.remove(cards.size()-1);
//        players.get(currentPlayer).addCard(card);

        return card;
    }

    public boolean removeDevCard(int playerId, DevCardType card) {
        //System.out.println("removedev");
        boolean b = true;
        for (int i = 0; i < 5; i++) {   //geen dev card kopen als je al 1 aan het spelen bent
            if (mayPlayDevCardType[idToIndex.get(playerId)][i]) {
                b = false;
            }
        }
        if (b && !mayPlaceThief) {
            return nameToPlayer(playerId).removeCard(card.getValue());
        } else {
            return false;
        }
    }

    /**
     * steelt een random grondstof, wordt opgeroepen na verplaatsen van de
     * struikrover
     *
     * @param playerId
     * @param stealerId
     */
    public void StealResources(int playerId, int stealerId) {
        int stealerindex = idToIndex.get(stealerId);
        if (maySteal[stealerindex]) {
            System.out.println("Steal opgeroepen");
            int k = nameToPlayer(playerId).removeRandomResource();
            nameToPlayer(stealerId).addResource(TileType.values()[k]);
            maySteal[stealerindex] = false;
        }
    }

    /**
     * steelt de grondstoffen van iedereen en geeft ze allemaal aan de speler
     * die steelt
     *
     * @param playerId de speler die steelt
     * @param i stelt de grondstof voor die gestolen wordt
     * @return
     */
    public boolean StealMonopolyResources(int playerId, int i) {
        System.out.println("Monopoly opgeroepen");
        if (mayPlayDevCardType[currentPlayer][DevCardType.MONOPOLY.getValue()]) {
            for (int j = 0; j < players.size(); j++) {
                int k = players.get(j).removeResources(i);
                nameToPlayer(playerId).addResource(TileType.values()[i], k);
            }
//            if(howmanyPlayed==0) {
//                howmanyPlayed++;
//            }
//            else{
            mayPlayDevCardType[currentPlayer][DevCardType.MONOPOLY.getValue()] = false;
//            howmanyPlayed=0;
//            }
            //PlayCard2Stuff[currentPlayer]=false;
            //PlayCard[currentPlayer]=false;
            return true;
        }
        return false;
    }

    /**
     * geeft ridder aan speler en geeft de riddertrophy aan hem indien nodig,
     * verwijderd hem dan bij de speler die hem eerst had
     *
     * @param player1
     */
    public void addKnight(int player1) {
        IPlayer player = getPlayer(player1);
        player.addKnight();
        boolean b = true;
        for (IPlayer p : players) {
            if (!player.equals(p)) {
                if (player.getNumberKnights() < 3) {
                    b = false;
                } else if (player.getNumberKnights() <= p.getNumberKnights()) {
                    b = false;
                    p.removeKnightTrophy();
                }
            }
        }
        if (b) {
            player.giveKnightTrophy();
            player.addVictoryPoint();
            if (player.addVictoryPoint()) {
                endGame();
            }


        }
        mayPlaceThief = true;
    }

    public void checkPossibleFreeRoad(int x, int y, int index) {
        log.debug("Entering checkPossibleRoad ( x= {} y= {} index= {}", x, y, index);
        // System.out.println("check");
        // System.out.println("roadpossible= "+getTile(x, y).roadPossible(index,players.get(currentPlayer).getId()));
        // System.out.println("buildroadPossible= "+players.get(currentPlayer).buildRoadPossible());
        if (getTile(x, y).roadPossible(index, players.get(currentPlayer).getId())) {
            placeRoadPossible = true;
        }
        if (!getTile(x, y).getType().equals(TileType.SEA)) {
            nonSeaRoad = true;
        }

        log.trace("placeRoadPossible= {} nonSeaRoad= {}", placeRoadPossible, nonSeaRoad);
        log.debug("Leaving checkPossibleRoad");

    }

    /**
     * Voert een ruil uit met de bank. Er wordt niet gecontroleerd als de ruil
     * mogelijk is. Het is de bedoeling dat dit clientside gebeurt.
     *
     * @param trade het ruilvoorstel
     * @param playerId de speler die wil ruilen met de bank
     */
    public void executeBankTrade(TradeOffer trade, int playerId) {
        //testen of het geldige trade is
        boolean realTrade = true;
        int offersum = 0;
        int requestsum = 0;
        int[] offer = trade.getOffer();
        int[] request = trade.getRequest();
        TileType[] types = TileType.values();
        for (int i = 0; i < TileType.NUMBER_OF_RESOURCES; i++) {
            if (offer[i] % 4 != 0) {//we hebben geen havens dus ruilconstante is altijd 4
                realTrade = false;
            }
            offersum += offer[i];
            requestsum += request[i];
        }
        //trade uitvoeren
        if (realTrade && offersum != 0 && offersum / requestsum == 4) {
            for (int i = 0; i < TileType.NUMBER_OF_RESOURCES; i++) {
                //offer
                //bank.addResource(types[i], offer[i]);=> gaat vanzelf
                nameToPlayer(playerId).removeResource(types[i], offer[i]);

                //request
                nameToPlayer(playerId).addResource(types[i], request[i]);
                //bank.removeResource(types[i], request[i]); => gaat vanzelf
            }
        }
    }

    /**
     * Voert een ruil uit tussen 2 spelers. Er wordt gecontroleerd als de
     * spelers genoeg grondstoffen hebben voor de ruil.
     *
     * @param trade het ruilvoorstel
     * @param starter de speler die het voorstel deed
     * @param receiver de speler die het voorstel kreeg en aanvaardde
     */
    public void executePlayerTrade(TradeOffer trade, int starter, int receiver) {
        int[] offer = trade.getOffer();
        int[] request = trade.getRequest();
        System.out.println("check Trade possible before execute");
        if (getPlayer(starter).tradePossible(offer) && getPlayer(receiver).tradePossible(request)) {
            TileType[] types = TileType.values();
            for (int i = 0; i < TileType.NUMBER_OF_RESOURCES; i++) {
                //offer
                nameToPlayer(starter).removeResource(types[i], offer[i]);
                nameToPlayer(receiver).addResource(types[i], offer[i]);


                //request
                nameToPlayer(receiver).removeResource(types[i], request[i]);
                nameToPlayer(starter).addResource(types[i], request[i]);

            }
            System.out.println("Trade executed");
        }
    }

    public boolean GiveResources(int playerId, int resource) {
        if (mayPlayDevCardType[currentPlayer][DevCardType.RESOURCES.getValue()]) {
            if (howmanyPlayed == 0) {
                howmanyPlayed++;
            } else {
                mayPlayDevCardType[currentPlayer][DevCardType.RESOURCES.getValue()] = false;
                howmanyPlayed = 0;
            }
            //PlayCard[currentPlayer]=false;
            nameToPlayer(playerId).addResource(TileType.values()[resource]);
            return true;
        }
        return false;
    }

    /**
     * Zet het startbord om in een lijst van getallen. Hierin wordt bijgehouden:
     * - waar het bord begonnen is met tegels te leggen; - het maximum aantal
     * spelers; - wie als eerste aan de beurt was; - de volgorde van de types
     * van de tegels; - de seed van de dobbelstenen.
     *
     * @return lijst van getallen die het startbord voorstelt
     */
    public ArrayList<Integer> writeStartForm() {
        ArrayList<Integer> startList = new ArrayList<Integer>();
        startList.add(startPositionNumbers);
        startList.add(maxPlayers);
        startList.add(startingPlayer);
        for (int i = 0; i < XDIM; i++) {
            for (int j = 0; j < YDIM; j++) {
                if (tiles[i][j] != null) {
                    startList.add(tiles[i][j].getType().getValue());
                } else {
                    startList.add(-1);
                }

            }
        }
        startList.add(seed);
        return startList;
    }

    /**
     * Geeft het maximum aantal spelers terug die meespelen.
     *
     * @return max aantal spelers
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getNumberFreeRoadsPlaced() {
        return numberFreeRoadsPlaced;
    }

    public void setNumberFreeRoadsPlaced(int numberFreeRoadsPlaced) {
        this.numberFreeRoadsPlaced = numberFreeRoadsPlaced;
    }

    /**
     * geeft de speler een overwinningspunt en roept endGame op als hij 1à
     * overwinningspunten heeft
     *
     * @param player
     */
    public void addVictoryPoint(int player) {
        if (getPlayer(player).addVictoryPoint()) {
            endGame();
        }

    }

    public void endGame() {
        notifyObservers("endGame");
    }

    /**
     * Geeft het commando terug op index i.
     *
     * @param i index in de verzameling
     * @return commando
     */
    public ICommand getCommand(int i) {
        ICommand cmd = null;
        if (i >= 0 && i < commands.size()) {
            cmd = commands.get(i);
        }
        return cmd;
    }

    /**
     * geeft alle berichten vanop server aan clients vanaf index i
     *
     * @param begin
     * @return berichten samen met van wie ze afkomstig zijn
     */
    public PlayerWithMessage getMessages(int begin) {

        PlayerWithMessage sc = new PlayerWithMessage();
        for (int i = begin; i < messages.size(); i++) {
            sc.addMessage(messages.get(i), playersformessages.get(i));
        }
        return sc;

    }

    /**
     * voegt extra berichten toe op de server
     *
     * @param sc
     */
    public void addMessages(PlayerWithMessage sc) {
        for (int i = 0; i < sc.size(); i++) {
            messages.add(sc.getMessage(i));
            playersformessages.add(sc.getPlayer(i));
        }
    }

    /**
     * Geeft terug als de speler zich in de gamefase bevindt.
     *
     * @param id spelerid
     * @return true als de speler zich in de gamefase bevindt
     */
    public boolean isGamePhase(int id) {
        return getPlayer(id).isGamePhase();
    }

    /**
     * Geeft de id terug van de speler die aan de beurt is.
     *
     * @return spelerid
     */
    public int getCurrentPlayerId() {
        if (isSpectatorMode) {
            return 0;
        } else {
            log.debug("return getCurrentPlayer= {}", players.get(currentPlayer).getName());
            return players.get(currentPlayer).getId();
        }
    }

    public void setLastSettlement(ISettlement lastSettlement) {
        this.lastSettlement = lastSettlement;
    }

    /**
     * Verwerkt het antwoord van een speler op een multitradevoorstel. Er wordt
     * bijgehouden hoeveel spelers reeds geantwoord hebben, welke spelers, hun
     * reactietijd op het voorstel (berekend op clientside). Als het de eerste
     * speler is die het voorstel geaccepteerd heeft, wordt dit opgeslagen.
     * Indien een andere speler sneller gereageerd heeft, ook al komt zijn
     * antwoord later aan, zal hij toch onthouden worden als de snelste speler.
     * Als iedereen een antwoord gestuurd heeft, wordt de ruil uitgevoerd met de
     * snelste speler. Als niemand het voorstel aanvaard heeft, gebeurt er
     * niets. Als de laatste speler geantwoord heeft, wordt er een
     * TradeEndedCommand aangemaakt en toegevoegd aan de lijst met commando's.
     *
     * @param reactiontime reactietijd van de speler (berekent op clientside)
     * @param rc antwoordcommando van de speler
     */
    public void tradeAnswered(long reactiontime, ResponseCommand rc) {
        respondedPlayers++;
        playersAnswered[idToIndex.get(rc.getPlayerid())] = true;
        if (rc.isAccepted()) {
            if (fastestPlayerId == -1 || lowestReactionTime > reactiontime) {
                lowestReactionTime = reactiontime;
                fastestPlayerId = rc.getPlayerid();
            }
        }
        if (respondedPlayers == players.size() - 1) {
            if (fastestPlayerId != -1) {
                rc.getTrade().executeTrade(this, fastestPlayerId);
                System.out.println("Traded with " + getPlayer(fastestPlayerId).getName());
            } else {
                System.out.println("Niemand wil traden.");
            }
            respondedPlayers = 0;
            fastestPlayerId = -1;
            tradeOver = true;
            commands.add(new TradeEndedCommand());
            for (int i = 0; i < 4; i++) {
                playersAnswered[i] = false;
            }
            System.out.println("Trading over.");
        }

    }

    /**
     * Geeft terug als een speler al geantwoord heeft op een ruilvoorstel.
     *
     * @param playerid id van de speler
     * @return
     */
    public boolean playerAnswered(int playerid) {
        return playersAnswered[idToIndex.get(playerid)];
    }

    /**
     * Geeft weer als het ruilen voorbij is of niet.
     *
     * @return true als het ruilen voorbij is
     */
    public boolean isTradeOver() {
        return tradeOver;
    }

    /**
     * Stelt het laatst gerolde getal in de dobbelstenen.
     */
    public void setLastRolledNumber() {
        dices.setDices(number1, number2);
    }

    /**
     * Stelt in als het ruilen voorbij is of niet. Indien het ruilen voorbij is,
     * wordt er een TradeEndedCommand aangemaakt en toegevoegd aan de lijst met
     * commando's.
     *
     * @param tradeOver true als ruilen voorbij is
     */
    public void setTradeOver(boolean tradeOver) {
        if (tradeOver) {
            commands.add(new TradeEndedCommand());
        }
        this.tradeOver = tradeOver;
    }

    public void setPlayCard2Stuff(int playerPlace, int value) {
        mayPlayDevCardType[playerPlace][value] = true;
    }

    public int getCardSize() {
        return cards.size();
    }

    public IPlayer getPlayerFromPosition(int index) {
        return players.get(index);
    }

    public int getHowmanyPlayed() {
        return howmanyPlayed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
