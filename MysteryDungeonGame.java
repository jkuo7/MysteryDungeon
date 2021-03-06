import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Queue;
import java.util.LinkedList;
import java.util.EnumSet;

public class MysteryDungeonGame extends JKGame {
    private static final int TILE_SIZE = 16; //20
    private static final int BOARD_WIDTH = 79;
    private static final int BOARD_HEIGHT = 31;

    private static final int HUD_HEIGHT = TILE_SIZE;

    private static final int GAME_WIDTH = BOARD_WIDTH * TILE_SIZE;
    private static final int GAME_HEIGHT = BOARD_HEIGHT * TILE_SIZE + HUD_HEIGHT;

    private boolean gameNotStart = true;
    private boolean flicker = true;
    private boolean betweenFloors = false;
    private int countdown;

    private boolean critical = false;
    private boolean criticalFlicker = false;

    private int curFloor = 1;
    private int maxFloor;

    private int moves = 0;

    private Timer timer;
    private Timer messageTimer;

    private Dungeon dungeonKind;
    private MysteryDungeon dungeon;

    private Player player;
    private static final int NUM_ALLIES = 3;

    private Queue<String> messages;
    private Queue<Color> msgColors;
    private String nextMsg = "";
    private Color nextColor;

    private int statsWidth;
    private int infoWidth;

    public MysteryDungeonGame(){
        this.setBackground(Color.BLACK);
        messages = new LinkedList<>();
        msgColors = new LinkedList<>();
        dungeonKind = Dungeon.PLAINS;//Dungeon.getValues()[(int)(Math.random() * Dungeon.getValues().length)];

        bindKeyStrokeTo("enter.pressed", KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), startGameAc());
        messageTimer = new Timer(500, (ae) -> {
            repaintMessages();
            if(critical){
                repaintStats();
            }
        });

        timer = new Timer(750, (ae) -> repaint());
        timer.start();
    }

    /** Returns action to start game */
    private Action startGameAc(){
        return new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent ae){
                if(gameNotStart){
                    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), null);
                    getActionMap().put("enter.pressed", null);
                    startGame();
                }
            }
        };
    }

    /** Starts the game */
    private void startGame(){
        initiatePlayer();
        dungeon = new MysteryDungeon(341634642L, this, player, dungeonKind);

        Integer[] floors = {3, 4, 5, 6, 7, 8, 9, 10};
        Integer floor = null;
        while(floor == null) {
            floor = (Integer) promptInput("How many floors would you like to go through?",
                    "Choose Number of Floors", floors, floors[0]);
        }
        maxFloor = floor;

        gameNotStart = false;
        timer.stop();
        timer = new Timer(500, (ae) ->{
            repaint();
            countdown--;
            if(countdown == 0){
                toNextFloor();
            }
        });
        repaint();
        bindKeyStrokes();
        messageTimer.start();
    }

    private void initiatePlayer(){
        Pokemon poke = null;
        while(poke == null){
            poke = (Pokemon) promptInput("Which Pokemon would you like to play as?",
                    "Choose a Pokemon to Play As", PartyMember.POSSIBLE_ALLIES, PartyMember.POSSIBLE_ALLIES[0]);
        }

        player = new Player(0,0, poke);
        EnumSet<Pokemon> pokemonInParty = EnumSet.of(player.pokemon);

        String playerName = "";
        while (playerName == null || playerName.equals("")) {
            playerName = (String) promptInput("What is your name?",
                    "Enter Your Name", null, null);
        }
        player.name = playerName;

        for (int i = 0; i < NUM_ALLIES; i++) {
            if (pokemonInParty.size() >= PartyMember.POSSIBLE_ALLIES.length) {
                pokemonInParty.clear();
            }
            do {
                poke = PartyMember.POSSIBLE_ALLIES[(int)(Math.random() * PartyMember.POSSIBLE_ALLIES.length)];
            } while(pokemonInParty.contains(poke));
            Ally a = new Ally(0, 0, player, poke);
            pokemonInParty.add(a.pokemon);
        }
    }

    /** Binds keys to game actions */
    void bindKeyStrokes(){
        bindKeyStrokeTo("a.pressed", KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), moveAc(MysteryDungeon.Direction.LEFT));
        bindKeyStrokeTo("d.pressed", KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), moveAc(MysteryDungeon.Direction.RIGHT));
        bindKeyStrokeTo("w.pressed", KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), moveAc(MysteryDungeon.Direction.UP));
        bindKeyStrokeTo("s.pressed", KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), moveAc(MysteryDungeon.Direction.DOWN));

        bindKeyStrokeTo("q.pressed", KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), moveAc(MysteryDungeon.Direction.UP_LEFT));
        bindKeyStrokeTo("e.pressed", KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), moveAc(MysteryDungeon.Direction.UP_RIGHT));
        bindKeyStrokeTo("z.pressed", KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0), moveAc(MysteryDungeon.Direction.DOWN_LEFT));
        bindKeyStrokeTo("x.pressed", KeyStroke.getKeyStroke(KeyEvent.VK_X, 0), moveAc(MysteryDungeon.Direction.DOWN_RIGHT));

        bindKeyStrokeTo("space.pressed", KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), normalAttackAc());
        bindKeyStrokeTo("o.pressed", KeyStroke.getKeyStroke(KeyEvent.VK_O, 0), bagAc());
        bindKeyStrokeTo("p.pressed", KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), partyAc());
        bindKeyStrokeTo("i.pressed", KeyStroke.getKeyStroke(KeyEvent.VK_I, 0), inspectAc());
        bindKeyStrokeTo("k.pressed", KeyStroke.getKeyStroke(KeyEvent.VK_K, 0), attackAc());
    }

    private Action moveAc(MysteryDungeon.Direction dir){
        return new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent ae){
                if(!betweenFloors){
                    if(player.facing == dir) {
                        dungeon.playerMoves(dir);
                    } else {
                        player.facing = dir;
                        repaintPlayer();
                    }
                }
            }
        };
    }

    private Action normalAttackAc(){
        return new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent ae){
                if(!betweenFloors){
                    dungeon.playerAttacks(FixedAttack.NORMAL_ATTACK);
                }
            }
        };
    }

    private Action bagAc(){
        return new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent ae){
                if(!betweenFloors){
                    showBag();
                }
            }
        };
    }

    private Action partyAc(){
        return new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent ae){
                if(!betweenFloors){
                    showParty();
                }
            }
        };
    }

    private Action inspectAc(){
        return new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent ae){
                if(!betweenFloors){
                    inspectFloor();
                }
            }
        };
    }

    private Action attackAc(){
        return new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent ae){
                if(!betweenFloors){
                    showAttacks();
                }
            }
        };
    }

    void playerMoved(){
        moves++;
        repaintGame();
    }

    int moves(){
        return moves;
    }

    private void repaintGame(){
        repaintDungeon();
        repaintHUD();
        if(!critical){
            repaintStats();
        }
    }

    void outOfHP(Creature c){
        if(c.equals(player)){
            gameOver(String.format("Ran out of HP!\nFloors cleared: %d\n$%d collected", curFloor - 1, player.money),
                    "Game Over!");
        } else {
            dungeon.removeCreature(c);
        }
    }

    void setCritical(boolean b){
        if(b){
            critical = true;
        } else {
            critical = false;
            repaintStats();
        }
    }

    void askItem(Item i){
        Object[] options = {"Put in bag", "Use", "Cancel"};

        Object selectedValue = JOptionPane.showOptionDialog(this,
                i,
                "Found: " + i.name,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if(selectedValue.equals(JOptionPane.YES_OPTION)){
            player.putInBag(i, this, dungeon, player);
        } else if(selectedValue.equals(JOptionPane.NO_OPTION)){
            player.use(i, this, dungeon);
        }
        repaintGame();
    }

    private void showBag(){
        Item i = (Item) promptInput("Choose an item to use", "Bag", player.getBag(), "Choose an item to use");
        if(i!= null){
            Object[] party = player.getParty();
            PartyMember pm = (PartyMember) promptInput("Who should use it?", "Party", party, party[0]);
            if(pm != null){
                pm.useFromBag(i, this);
                repaintStats();
            }
        }
    }

    private void showParty(){
        Object[] party = player.getParty();
        JOptionPane.showMessageDialog(this, party, "Party", JOptionPane.INFORMATION_MESSAGE);
    }

    private void inspectFloor(){
        FlatOccupant item = dungeon.flatUnderPlayer();
        if(item != null){
            JOptionPane.showMessageDialog(this, item, item.name, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showAttacks(){
        LearnedAttack a = (LearnedAttack) promptInput("Choose an attack to use", "Attacks", player.getAttacks(), player.lastAttack);
        if(a!= null && a.hasPP()){
            player.lastAttack = a;
            dungeon.playerAttacks(a);
        }
    }

    /** Helper method to prompt for a confirmation */
    boolean promptConfirm(String prompt, String title){
        return JOptionPane.showConfirmDialog(this,
                prompt, title, JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION;
    }

    /** Helper method to prompt for an input from choices */
    Object promptInput(String prompt, String title, Object[] choices, Object def){
        return JOptionPane.showInputDialog(this, prompt, title, JOptionPane.PLAIN_MESSAGE,
                null, choices, def);
    }

    /** Helper method to prompt for an option from choices */
    int promptOption(String prompt, String title, Object[] choices){
        return JOptionPane.showOptionDialog(this, prompt, title, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, choices, null);
    }

    void askNextFloor(){
        if(promptConfirm("Go to the next floor?", "Stairs Reached")){
            if(curFloor == maxFloor){
                gameOver(String.format("Congratulations!\nCleared %d floors in %d moves\n$%d collected",
                        maxFloor, moves, player.money),
                        "You Win!");
            } else {
                changeFloors();
            }
        }
    }

    private void changeFloors(){
        betweenFloors = true;
        flicker = true;
        curFloor++;
        countdown = 6;
        messageTimer.stop();

        timer.start();
        dungeon = new MysteryDungeon(dungeon.nextFloorSeed(), this, player, dungeonKind);
    }

    private void toNextFloor(){
        timer.stop();
        betweenFloors = false;
        addMessage(String.format("Reached floor %d!", curFloor));
        messageTimer.start();
        repaintGame();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(gameNotStart){
            paintTitleScreen(g2d);
        } else if(betweenFloors) {
            paintBetweenFloors(g2d);
        } else {
            paintDungeon(g2d);
            paintHUD(g2d);
        }
    }

    private void paintTitleScreen(Graphics2D g2d){
        if(flicker){
            g2d.setFont(new Font("Consolas", Font.BOLD, 30));
            g2d.setColor(Color.WHITE);
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString("WASD to move", (GAME_WIDTH - fm.stringWidth("WASD to move"))/2, (GAME_HEIGHT - fm.getHeight()) / 2 + fm.getAscent() - 150);
            g2d.drawString("Q/E/Z/X to move NW/NE/SW/SE", (GAME_WIDTH - fm.stringWidth("Q/E/Z/X to move NW/NE/SW/SE"))/2, (GAME_HEIGHT - fm.getHeight()) / 2 + fm.getAscent() - 100);
            g2d.drawString("SPACE/K to normal/special attack", (GAME_WIDTH - fm.stringWidth("SPACE/K to normal/special attack"))/2, (GAME_HEIGHT - fm.getHeight()) / 2 + fm.getAscent() - 50);
            g2d.drawString("O to open bag", (GAME_WIDTH - fm.stringWidth("O to open bag"))/2, (GAME_HEIGHT - fm.getHeight()) / 2 + fm.getAscent());
            g2d.drawString("P to view party", (GAME_WIDTH - fm.stringWidth("P to view party"))/2, (GAME_HEIGHT - fm.getHeight()) / 2 + fm.getAscent() + 50);
            g2d.drawString("I to inspect floor", (GAME_WIDTH - fm.stringWidth("I to inspect floor"))/2, (GAME_HEIGHT - fm.getHeight()) / 2 + fm.getAscent() + 100);
            g2d.drawString("-Press Enter to Start-", (GAME_WIDTH - fm.stringWidth("-Press Enter to Start-"))/2, (GAME_HEIGHT - fm.getHeight()) / 2 + fm.getAscent() + 150);
        }
        flicker = !flicker;
    }

    private void paintBetweenFloors(Graphics2D g2d){
        if(flicker){
            g2d.setFont(new Font("Consolas", Font.BOLD, 30));
            g2d.setColor(Color.WHITE);
            FontMetrics fm = g2d.getFontMetrics();
            String floor = "Floor " + curFloor;
            g2d.drawString(dungeonKind.toString(), (GAME_WIDTH - fm.stringWidth(dungeonKind.toString()))/2, (GAME_HEIGHT - fm.getHeight()) / 2 + fm.getAscent() - 25);
            g2d.drawString(floor, (GAME_WIDTH - fm.stringWidth(floor))/2, (GAME_HEIGHT - fm.getHeight()) / 2 + fm.getAscent() + 25);
        }
        flicker = !flicker;
    }

    private void paintDungeon(Graphics2D g2d){
        for(int x = 0; x < BOARD_WIDTH; x++){
            for(int y = 0; y < BOARD_HEIGHT; y++){
                int space = dungeon.spaceAt(x, y);
                paintSpace(g2d, x, y, space);
            }
        }
        paintOccupants(g2d);
    }

    private void paintSpace(Graphics2D g2d, int x, int y, int space){
        Color bg;
        switch(space){
            case 0: bg = dungeonKind.getBlankColor();
                break;
            case -1: bg = dungeonKind.getWallColor();
                break;
            default: bg = dungeonKind.getFloorColor();
                break;
        }
        g2d.setColor(bg);
        g2d.fillRect(x * TILE_SIZE, y * TILE_SIZE + HUD_HEIGHT, TILE_SIZE, TILE_SIZE);
    }

    private void paintOccupants(Graphics2D g2d){
        g2d.setFont(new Font("Monaco", Font.BOLD, TILE_SIZE - 2));
        FontMetrics fm = g2d.getFontMetrics();
        for(Occupant o: dungeon.flatOccupants){
            if(dungeon.seen(o.x, o.y)) {
                paintOccupant(g2d, fm, o);
            }
        }
        for(Occupant o: dungeon.enemies){
            if(dungeon.seen(o.x, o.y)) {
                paintOccupant(g2d, fm, o);
            }
        }
        for(Occupant o: player.party){
            paintOccupant(g2d, fm, o);
        }
        paintPartySquares(g2d);
    }

    private void paintOccupant(Graphics2D g2d, FontMetrics fm, Occupant o){
        g2d.setColor(o.textColor);

        if(o.facing.angle() == 0){
            g2d.drawString(o.symbol(), o.x * TILE_SIZE + (TILE_SIZE - fm.stringWidth(o.symbol))/2, o.y * TILE_SIZE + HUD_HEIGHT + (TILE_SIZE - fm.getHeight()) / 2 + fm.getAscent());
        } else {
            g2d.translate(o.x * TILE_SIZE + TILE_SIZE/2, o.y * TILE_SIZE + HUD_HEIGHT + TILE_SIZE/2);
            g2d.rotate(Math.toRadians(o.facing.angle()));
            g2d.drawString(o.symbol(), -TILE_SIZE/2 + (TILE_SIZE - fm.stringWidth(o.symbol))/2, -TILE_SIZE/2 + (TILE_SIZE - fm.getHeight()) / 2 + fm.getAscent());
            g2d.rotate(-Math.toRadians(o.facing.angle()));
            g2d.translate(-(o.x * TILE_SIZE + TILE_SIZE/2), -(o.y * TILE_SIZE + HUD_HEIGHT + TILE_SIZE/2));
        }
    }

    private void paintPartySquares(Graphics2D g2d){
        g2d.setColor(Color.GREEN);
        g2d.drawRect(player.x * TILE_SIZE, player.y * TILE_SIZE + HUD_HEIGHT, TILE_SIZE - 1, TILE_SIZE - 1);
        g2d.setColor(Color.CYAN);
        for(Ally a: player.allies){
            g2d.drawRect(a.x * TILE_SIZE, a.y * TILE_SIZE + HUD_HEIGHT, TILE_SIZE - 1, TILE_SIZE - 1);
        }
    }

    private void paintHUD(Graphics2D g2d){
        g2d.setFont(new Font("Consolas", Font.PLAIN, 14));
        FontMetrics fm = g2d.getFontMetrics();
        if(!critical){
            paintHUDStats(g2d, fm, Color.WHITE);
        } else if(criticalFlicker){
            paintHUDStats(g2d, fm, Color.RED);
        }
        paintHUDInfo(g2d, fm);
    }

    private void paintHUDInfo(Graphics2D g2d, FontMetrics fm){
        g2d.setColor(Color.WHITE);

        String level = String.format("Lv. %-3d", player.level);
        String floor = String.format("FLOOR: %2d/%-2d", curFloor, maxFloor);
        String money = String.format("$%-4d", player.money);
        String items = String.format("ITEMS: %2d/%-2d", player.bag.size(), player.bagLimit);
        String info = level + "     " + money + "     " + items + "     " + floor + "     ";

        if(infoWidth == 0){
            infoWidth = fm.stringWidth(info);
        }

        g2d.drawString(info, TILE_SIZE + statsWidth, HUD_HEIGHT - fm.getHeight() + fm.getAscent());
        paintMsg(g2d, fm);
    }

    private void paintHUDStats(Graphics2D g2d, FontMetrics fm, Color color){
        g2d.setColor(color);
        String hp = String.format("HP: %3d/%-3d", (int) Math.ceil(player.curHP), player.maxHP);
        String belly = String.format("BELLY: %3d/%-3d", (int) Math.ceil(player.curBelly), player.maxBelly);
        String hud = hp + "     " + belly + "     ";

        if(statsWidth == 0){
            statsWidth = fm.stringWidth(hud);
        }

        g2d.drawString(hud, TILE_SIZE, HUD_HEIGHT - fm.getHeight() + fm.getAscent());
    }

    private void paintMsg(Graphics2D g2d, FontMetrics fm){
        g2d.setColor(nextColor);
        g2d.drawString(nextMsg, GAME_WIDTH - TILE_SIZE - fm.stringWidth(nextMsg), HUD_HEIGHT - fm.getHeight() + fm.getAscent());
    }

    /** Adds a message to be displayed in top-right corner in default color of white */
    void addMessage(String msg){
        addMessage(msg, Color.WHITE);
    }

    /** Adds a message to be displayed in top-right corner in given color */
    void addMessage(String msg, Color color){
        messages.add(msg);
        msgColors.add(color);
    }

    /** Helper function to repaint only the stats portion of the HUD */
    private void repaintStats(){
        repaint(0, 0, statsWidth, HUD_HEIGHT);
    }

    /** Helper function to repaint only the info portion of the HUD */
    private void repaintHUD(){
        repaint(statsWidth, 0, infoWidth, HUD_HEIGHT);
    }

    /** Helper function to repaint only the messages portion of the HUD */
    private void repaintMessages() {
        criticalFlicker = !criticalFlicker;
        if(messages.isEmpty() && nextMsg.equals("")){
            return;
        }
        if (criticalFlicker) {
            nextColor = msgColors.remove();
            nextMsg = messages.remove();
        } else {
            nextMsg = "";
        }
        repaint(statsWidth + infoWidth, 0, GAME_WIDTH - statsWidth - infoWidth, HUD_HEIGHT);
    }

    /** Helper function to repaint only the dungeon */
    void repaintDungeon(){
        repaint(0, HUD_HEIGHT, GAME_WIDTH, GAME_HEIGHT - HUD_HEIGHT);
    }

    /** Helper function to repaint only the player (for rotations) */
    private void repaintPlayer(){
        repaint(player.x * TILE_SIZE, player.y * TILE_SIZE + HUD_HEIGHT, TILE_SIZE, TILE_SIZE);
    }

    public static void main(String[] args){
        GameLauncher.startGUI(new MysteryDungeonGame(), "Mystery Dungeon");
    }

    /** Returns the dimensions of the game board */
    public Dimension getPreferredSize(){
        return new Dimension(GAME_WIDTH, GAME_HEIGHT);
    }

}