import java.util.*;

//http://journal.stuffwithstuff.com/2014/12/21/rooms-and-mazes/

public class MysteryDungeon {
    private static final int WIDTH = 79; //63
    private static final int HEIGHT = 31; //25
    private int numRooms;
    private int numRegions;

    private Random ran;
    private int[][] regions;
    private boolean[][] seen;

    private static final boolean SHOW_ALL = true;
    //set to true for testing purposes

    private Set<Integer> seenRooms;
    private Set<Integer> seenHallways;
    private Set<Integer> seenWalls;

    private Player player;
    private Goal goal;
    Set<FlatOccupant> flatOccupants;
    Set<Enemy> enemies;
    private FlatOccupant[][] flatAt;
    private Creature[][] creatureAt;
    private static final int MIN_ENEMIES = 5;
    private static final int MAX_ENEMIES = 10;

    MysteryDungeonGame game;

    /** Directions UP, LEFT, DOWN, RIGHT, and STAY for movement */
    enum Direction{
        UP (0, -1),
        LEFT (-1, 0),
        DOWN (0, 1),
        RIGHT (1, 0),
        STAY (0, 0);

        private final int dx, dy;

        Direction(int x, int y){
            dx = x;
            dy = y;
        }

        int dx(){
            return dx;
        }

        int dy(){
            return dy;
        }
    }


    /** Generates new MysteryDungeon floor, populating it with rooms and hallways. */
    public MysteryDungeon(long seed, MysteryDungeonGame g, Player p) {
        game = g;
        player = p;

        ran = new Random(seed);
        regions = new int[WIDTH][HEIGHT];

        seen = new boolean[WIDTH][HEIGHT];
        seenRooms = new HashSet<>();
        seenHallways = new HashSet<>();
        seenWalls = new HashSet<>();

        flatAt = new FlatOccupant[WIDTH][HEIGHT];
        creatureAt = new Creature[WIDTH][HEIGHT];
        flatOccupants = new HashSet<>();
        enemies = new HashSet<>();

        fillFrame();
    }


    private void fillFrame() {
        Queue<Position> endPoints = new LinkedList<>();
        makeRooms();
        makeHallways(endPoints);
        connectRegions();
        trimHallways(endPoints);
        addWalls();
        addExit();
        addPlayer();
        addEnemies();
        addItems();
    }

    private void makeRooms(){
//        int numRooms = (int)(15 + ran.nextGaussian());
        numRooms = 15 + ran.nextInt(16);
        ArrayList <int[]> rooms = new ArrayList<>(50);
        int tries = 0;
        while(rooms.size() < numRooms && tries < 1000){
            tries++;
            int[] newRoom = makeRoom();
            if(validRoom(newRoom, rooms)){
                rooms.add(newRoom);
                addRoomToFrame(newRoom, rooms.size());
            }
        }
        numRooms = rooms.size();
    }

    private int[] makeRoom() {
        int x = 2 * ran.nextInt(WIDTH/2) + 1;
        int y = 2 * ran.nextInt(HEIGHT/2) + 1;
//        int length = 2 * (int)(3 + ran.nextGaussian()) + 1;
//        int height = 2 * (int)(3 + ran.nextGaussian()) + 1;
        int length = 2 * (2 + ran.nextInt(3)) + 1;
        int height = 2 * (2 + ran.nextInt(3)) + 1;
        return new int[]{x, y, length, height};
    }

    private boolean validRoom(int[] newRoom, ArrayList <int[]> rooms){
        if (!(newRoom[0] > 0
                && newRoom[0] + newRoom[2] < WIDTH
                && newRoom[1] > 0
                && newRoom[1] + newRoom[3] < HEIGHT)){
            return false;
        }
        for(int[] room: rooms){
            if (!(newRoom[0] > room[0] + room[2]
                    || newRoom[0] + newRoom[2] < room[0]
                    || newRoom[1] > room[1] + room[3]
                    || newRoom[1] + newRoom[3] < room[1])){
                return false;
            }
        }
        return true;
    }

    private void addRoomToFrame(int[] newRoom, int region) {
        for(int dx = 0; dx < newRoom[2]; dx++){
            for(int dy = 0; dy < newRoom[3]; dy++){
                regions[newRoom[0] + dx][newRoom[1] + dy] = region;
            }
        }
    }

    private void makeHallways(Queue<Position> endPoints) {
        int region = numRooms;
        for(int x = 1; x < WIDTH; x += 2){
            for(int y = 1; y < HEIGHT; y += 2){
                if(regions[x][y] == 0){
                    region++;
                    makeHallway(x, y, region, endPoints);
                }
            }
        }
        numRegions = region;
    }

    class Position{
        public final int x;
        public final int y;

        public Position(int a, int b){
            x = a;
            y = b;
        }

        public Position translate(int dx, int dy){
            return new Position(this.x + dx, this.y + dy);
        }

        public boolean equals(Position second){
            return this.x == second.x && this.y == second.y;
        }

        public String toString(){
            return "(" + x + "," + y + ")";
        }
    }

    private void makeHallway(int x, int y, int region, Queue<Position> endPoints){
        Queue<Position> nodes = new LinkedList<>();
        nodes.add(new Position(x, y));
        int count = 0;
        while(!nodes.isEmpty() && count < 15){
            Position current = nodes.remove();
            endPoints.add(current);
            Position west = current;
            Position east = west;
            while(checkNextTwo(west.x, west.y, -1, 0) && ran.nextDouble() < 0.75){
                west = west.translate(-2, 0);
            }
            while(checkNextTwo(east.x, east.y, 1, 0) && ran.nextDouble() < 0.75){
                east = east.translate(2, 0);
            }
            for(int col = west.x; col <= east.x; col++) {
                regions[col][west.y] = region;
                count++;
            }
            boolean pass = false;
            if(checkNextTwo(east.x, east.y, 0, -1) && ran.nextDouble() < 0.5){
                regions[east.x][east.y - 1] = region;
                regions[east.x][east.y - 2] = region;
                nodes.add(new Position(east.x, east.y - 2));
                pass = true;
                count += 2;
            }
            if(checkNextTwo(east.x, east.y, 0, 1) && ran.nextDouble() < 0.5) {
                regions[east.x][east.y + 1] = region;
                regions[east.x][east.y + 2] = region;
                nodes.add(new Position(east.x, east.y + 2));
                pass = true;
                count += 2;
            }
            if (!pass){
                endPoints.add(east);
            }
            if(!west.equals(east)){
                pass = false;
                if (checkNextTwo(west.x, west.y, 0, -1) && ran.nextDouble() < 0.5) {
                    regions[west.x][west.y - 1] = region;
                    regions[west.x][west.y - 2] = region;
                    nodes.add(new Position(west.x, west.y - 2));
                    pass = true;
                    count += 2;
                }
                if (checkNextTwo(west.x, west.y, 0, 1) && ran.nextDouble() < 0.5) {
                    regions[west.x][west.y + 1] = region;
                    regions[west.x][west.y + 2] = region;
                    nodes.add(new Position(west.x, west.y + 2));
                    pass = true;
                    count += 2;
                }
                if (!pass){
                    endPoints.add(west);
                }
            }
        }
        while(!nodes.isEmpty()){
            endPoints.add(nodes.remove());
        }
    }

    private boolean checkNextTwo (int x, int y, int dx, int dy){
        return x + 2 * dx > 0 && x + 2 * dx < WIDTH && y + 2 * dy > 0 && y + 2 * dy < HEIGHT &&
                regions[x + dx][y + dy] == 0 && regions[x + 2 * dx][y + 2 * dy] == 0;
    }

    private void connectRegions(){
        ArrayList<Connector> connectors = findConnectors();
        WeightedQuickUnionPathCompressionUF merged = new WeightedQuickUnionPathCompressionUF(numRegions + 1);
        for(int i = connectors.size(); i > 0; i--){
            int conIndex = ran.nextInt(i);
            Connector con = connectors.get(conIndex);
            for(Position p: con.cons){
                if(ran.nextDouble() < 0.05){
                    regions[p.x][p.y] = numRegions + 1;
                }
            }
            if(!merged.connected(con.smaller, con.larger)){
                Position q = con.cons.get(ran.nextInt(con.cons.size()));
                regions[q.x][q.y] = numRegions + 1;
                merged.union(con.smaller, con.larger);
            }
            connectors.set(conIndex, connectors.get(i - 1));
        }
    }

    private ArrayList<Connector> findConnectors(){
        ArrayList<Connector> connectors = new ArrayList<>();
        HashMap<String, Connector> connectorsMap = new HashMap<>();
        for(int x = 1; x < WIDTH - 1; x ++){
            for(int y = 1; y < HEIGHT - 1; y ++){
                if(regions[x][y] == 0){
                    int[] neighbors = largestNeighbors(x, y);
                    int smaller = neighbors[0];
                    int larger = neighbors[1];
                    if(smaller != 0 && smaller != larger){
                        String key = smaller + "," + larger;
                        Connector con = connectorsMap.getOrDefault(key, new Connector(smaller, larger));
                        if(con.cons.isEmpty()){
                            connectors.add(con);
                        }
                        con.cons.add(new Position(x, y));
                        connectorsMap.put(key, con);
                    }
                }
            }
        }
        return connectors;
    }

    class Connector{
        int smaller;
        int larger;
        ArrayList<Position> cons;

        Connector(int s, int l){
            smaller = s;
            larger = l;
            cons = new ArrayList<>();
        }
    }

    private int[] largestNeighbors(int x, int y){
        int smaller = Math.min(regions[x][y - 1], regions[x - 1][y]);
        int larger = Math.max(regions[x][y - 1], regions[x - 1][y]);
        int c = regions[x][y + 1];
        int d = regions[x + 1][y];
        if (c > smaller){
            smaller = Math.min(c, larger);
            larger = Math.max(c, larger);
        }
        if (d > smaller){
            smaller = Math.min(d, larger);
            larger = Math.max(d, larger);
        }
        return new int[]{smaller, larger};
    }

    private void trimHallways(Queue<Position> endPoints){
        while(!endPoints.isEmpty()){
            Position p = endPoints.remove();
            if(countOpenSpaces(p.x, p.y) == 3){ // && ran.nextDouble() >= 0.1
                regions[p.x][p.y] = 0;
                endPoints.add(getFloorSpace(p.x, p.y));
            }
        }
    }

    private int countOpenSpaces(int x, int y){
        int openSpaces = 0;
        if (regions[x][y - 1] == 0){
            openSpaces++;
        }
        if (regions[x][y + 1] == 0){
            openSpaces++;
        }
        if (regions[x - 1][y] == 0){
            openSpaces++;
        }
        if (regions[x + 1][y] == 0){
            openSpaces++;
        }
        return openSpaces;
    }

    private Position getFloorSpace(int x, int y){
        if (regions[x][y - 1] != 0){
            return new Position(x, y - 1);
        }
        if (regions[x][y + 1] != 0){
            return new Position(x, y + 1);
        }
        if (regions[x - 1][y] != 0){
            return new Position(x - 1, y);
        }
        return new Position(x + 1, y);
    }

    private void addWalls(){
        for(int x = 0; x < WIDTH; x++){
            for(int y = 0; y<HEIGHT; y++){
                if(regions[x][y] == 0 && willBeWall(x, y)){
                    regions[x][y] = -1;
                }
            }
        }
    }

    private boolean willBeWall(int x, int y){
        return y > 0 && regions[x][y - 1] > 0 ||
                y < HEIGHT - 1 && regions[x][y + 1] > 0 ||
                x > 0 && regions[x - 1][y] > 0 ||
                x < WIDTH - 1 && regions[x + 1][y] > 0 ||
                y > 0 && x > 0 && regions[x - 1][y - 1] > 0||
                y > 0 && x < WIDTH - 1 && regions[x + 1][y - 1] > 0||
                y < HEIGHT - 1 && x > 0 && regions[x - 1][y + 1] > 0||
                y < HEIGHT - 1 && x < WIDTH - 1 && regions[x + 1][y + 1] > 0;
    }

    private void addExit(){
        int x, y;
        do{
            x = 1 + ran.nextInt(WIDTH - 2);
            y = 1 + ran.nextInt(HEIGHT - 2);
        } while(!isRoom(x, y));
        goal = new Goal(x, y);
        flatOccupants.add(goal);
        flatAt[x][y] = goal;
    }

    private void addPlayer(){
        int x, y;
        do{
            x = 1 + ran.nextInt(WIDTH - 2);
            y = 1 + ran.nextInt(HEIGHT - 2);
        } while(!isRoom(x, y) || (x == goal.x && y == goal.y));
        player.x = x;
        player.y = y;
        creatureAt[x][y] = player;
        markSeen();
        for(Ally a: player.allies){
            addAlly(a);
        }
    }

    private void addAlly(Ally a){
        int x, y;
        do{
            x = player.x + ran.nextInt(2);
            y = player.y + ran.nextInt(2);
        } while(!isRoom(x, y) || (x == goal.x && y == goal.y) || creatureAt[x][y] != null);

//         for(Direction dir: Direction.values()){
//             int x = player.x + dir.dx();
//             int y = player.y + dir.dy();
//             if(isRoom(x, y) && creatureAt[x][y] == null){
        a.x = x;
        a.y = y;
        creatureAt[x][y] = a;
//                 return;
//             }
//         }
    }

    private void addEnemies(){
        int numEnemies = MIN_ENEMIES + ran.nextInt(MAX_ENEMIES - MIN_ENEMIES);
        for(int i = 0; i < numEnemies; i++) {
            addEnemy();
        }
    }

    private void addEnemy(){
        int x, y;
        do {
            x = 1 + ran.nextInt(WIDTH - 2);
            y = 1 + ran.nextInt(HEIGHT - 2);
        } while (!isRoom(x, y) || (x == goal.x && y == goal.y) || creatureAt[x][y] != null);
        Enemy e = new Enemy(x, y);
        creatureAt[x][y] = e;
        enemies.add(e);
    }

    private void addNewEnemies(){
        for(int i = enemies.size(); i < MAX_ENEMIES; i++){
            double rate = i < MIN_ENEMIES ? 0.25 : 0.5;
            if(ran.nextDouble() < rate){
                return;
            } else {
                addEnemy();
            }
        }
    }

    private void addItems(){
        addCoins();
        addApples();
        addBerries();
    }

    private void addCoins(){
        int numCoins = 5 + ran.nextInt(15);
        int x, y;
        for(int i = 0; i <= numCoins; i++){
            do{
                x = 1 + ran.nextInt(WIDTH - 2);
                y = 1 + ran.nextInt(HEIGHT - 2);
            } while(!isRoom(x, y) || (flatAt[x][y] != null));
            Coin c = new Coin(x, y, 10 + 10 * ran.nextInt(20));
            flatAt[x][y] = c;
            flatOccupants.add(c);
        }
    }

    private void addApples(){
        int numApples = 3 + ran.nextInt(5);
        int x, y;
        for(int i = 0; i <= numApples; i++){
            do{
                x = 1 + ran.nextInt(WIDTH - 2);
                y = 1 + ran.nextInt(HEIGHT - 2);
            } while(!isRoom(x, y) || (flatAt[x][y] != null));
            Apple a = new Apple(x, y, ran);
            flatAt[x][y] = a;
            flatOccupants.add(a);
        }
    }

    private void addBerries(){
        int numBerries = 3 + ran.nextInt(5);
        int x, y;
        for(int i = 0; i <= numBerries; i++){
            do{
                x = 1 + ran.nextInt(WIDTH - 2);
                y = 1 + ran.nextInt(HEIGHT - 2);
            } while(!isRoom(x, y) || (flatAt[x][y] != null));
            Berry b = new Berry(x, y, ran);
            flatAt[x][y] = b;
            flatOccupants.add(b);
        }
    }

    private boolean isRoom(int x, int y){
        return regions[x][y] > 0 && regions[x][y] <= numRooms;
    }

    private boolean isHallway(int x, int y){
        return regions[x][y] > numRooms;
    }

    private boolean isWall(int x, int y){
        return regions[x][y] == -1;
    }


    /** If the given space has been seen already, returns its identity.
     * Otherwise, identifies it as empty space. */
    int spaceAt(int x, int y){
        if(!seen(x,y)){
            return 0;
        }
        return regions[x][y];
    }

    boolean seen(int x, int y){
        if(SHOW_ALL || seen[x][y]){
            return true;
        } else if(isRoom(x, y)){
            seen[x][y] = seenRooms.contains(regions[x][y]);
        } else if(isHallway(x, y)){
            seen[x][y] = seenHallways.contains(x + WIDTH * y);
        } else if(isWall(x, y)){
            seen[x][y] = seenWalls.contains(x + WIDTH * y) || seenWall(x, y);
        }
        return seen[x][y];
    }

    private boolean seenWall(int x, int y){
        if(wallNextToSeen(x, y)){
            seenWalls.add(x + WIDTH * y);
            return true;
        }
        return false;
    }

    private boolean wallNextToSeen(int x, int y){
        return y > 0 && regions[x][y - 1] > 0 && seen(x, y - 1) ||

                y < HEIGHT - 1 && regions[x][y + 1] > 0 && seen(x, y + 1) ||

                x > 0 && regions[x - 1][y] > 0 && seen(x - 1, y) ||

                x < WIDTH - 1 && regions[x + 1][y] > 0 && seen(x + 1, y) ||

                y > 0 && x > 0 && regions[x - 1][y - 1] > 0 && seen(x - 1, y - 1)
                        && (isRoom(x - 1, y - 1) || isWall(x, y - 1) && isWall(x - 1, y)) ||

                y > 0 && x < WIDTH - 1 && regions[x + 1][y - 1] > 0  && seen(x + 1, y - 1)
                        && (isRoom(x + 1, y - 1) || isWall(x, y - 1) && isWall(x + 1, y)) ||

                y < HEIGHT - 1 && x > 0 && regions[x - 1][y + 1] > 0  && seen(x - 1, y + 1)
                        && (isRoom(x - 1, y + 1) || isWall(x, y + 1) && isWall(x - 1, y)) ||

                y < HEIGHT - 1 && x < WIDTH - 1 && regions[x + 1][y + 1] > 0  && seen(x + 1, y + 1)
                        && (isRoom(x + 1, y + 1) || isWall(x, y + 1) && isWall(x + 1, y));
    }


    /** Moves the player in the given direction */
    public boolean playerTurn(Direction dir){
        if(dir != Direction.STAY &&
                (regions[player.x + dir.dx()][player.y + dir.dy()] <= 0 ||
                        (creatureAt[player.x + dir.dx()][player.y + dir.dy()] != null &&
                                creatureAt[player.x + dir.dx()][player.y + dir.dy()].isEnemy))) {
            return false;
        }
        movePlayer(dir);
        for(Ally a: player.allies){
            moveAlly(a, dir);
        }
        for(Enemy e: enemies){
            moveEnemy(e);
        }
        game.playerMoved();
        checkForFlats();
//         checkForFlats(player);
        if(game.moves() % 10 == 0){
            addNewEnemies();
            game.repaintDungeon();
        }
        return true;
    }

    void movePlayer(Direction dir){
        if(dir == Direction.STAY || creatureAt[player.x + dir.dx()][player.y + dir.dy()] == null) {
            moveCreature(player, dir);
        } else {
            swapWithAlly(dir);
        }
        markSeen();
    }

    void moveAlly(Ally a, Direction dir){
        List<Direction> open = getOpenDirections(a);
        List<Direction> toPlayer = towardPlayer(a);
        if(open.size() == 0){
            moveCreature(a, Direction.STAY);
        } else if(toPlayer.size() > 0 && validMove(a, toPlayer.get(0)) && ran.nextDouble() < 0.75){
            moveCreature(a, toPlayer.get(0));
        } else if(toPlayer.size() > 1 && validMove(a, toPlayer.get(1)) && ran.nextDouble() < 1){
            moveCreature(a, toPlayer.get(1));
        } else {
            moveCreature(a, open.get(ran.nextInt(open.size())));
        }
        checkForFlats(a);
    }

    List<Direction> towardPlayer(Ally a){
        List<Direction> toPlayer = new ArrayList<>(2);
        int hDist = a.x - player.x;
        int vDist = a.y - player.y;
        if(Math.abs(hDist) > Math.abs(vDist)){
            if(vDist != 0) {
                toPlayer.add(vDist > 0 ? Direction.UP : Direction.DOWN);
            }
            toPlayer.add(hDist > 0 ? Direction.LEFT : Direction.RIGHT);
        } else if(vDist != 0){
            if(hDist != 0) {
                toPlayer.add(hDist > 0 ? Direction.LEFT : Direction.RIGHT);
            }
            toPlayer.add(vDist > 0 ? Direction.UP : Direction.DOWN);
        }
        return toPlayer;
    }

    boolean validMove(Creature c, Direction d){
        return regions[c.x + d.dx()][c.y + d.dy()] > 0 && creatureAt[c.x + d.dx()][c.y + d.dy()] == null;
    }

    void moveEnemy(Enemy e){
        List<Direction> open = getOpenDirections(e);
        if(open.size() == 0){
            moveCreature(e, Direction.STAY);
        } else if (!validMove(e, e.lastDir) || ran.nextDouble() < 0.5){
            e.lastDir = open.get(ran.nextInt(open.size()));
        }
        moveCreature(e, e.lastDir);
        checkForFlats(e);
    }

    List<Direction> getOpenDirections(Creature c){
        List<Direction> open = new ArrayList<>(4);
        for(Direction d: Direction.values()){
            if(validMove(c, d)){
                open.add(d);
            }
        }
        return open;
    }

    void moveCreature(Creature c, Direction dir){
        creatureAt[c.x][c.y] = null;
        creatureAt[c.x + dir.dx()][c.y + dir.dy()] = c;
        c.move(dir.dx(), dir.dy(), game);
    }

    void swapWithAlly(Direction dir){
        creatureAt[player.x][player.y] = creatureAt[player.x + dir.dx()][player.y + dir.dy()];
        creatureAt[player.x + dir.dx()][player.y + dir.dy()].move(- dir.dx(), - dir.dy());
        creatureAt[player.x + dir.dx()][player.y + dir.dy()] = player;
        player.move(dir.dx(), dir.dy(), game);
    }

    void checkForFlats(Enemy e){
        if(flatAt[e.x][e.y] != null){
            flatAt[e.x][e.y].walkedOn(e, game, this);
        }
    }


    void checkForFlats(Ally c){
        if(flatAt[c.x][c.y] != null){
            flatAt[c.x][c.y].walkedOn(c, game, this);
        }
    }

    void checkForFlats(){
        if(flatAt[player.x][player.y] != null){
            flatAt[player.x][player.y].walkedOn(player, game, this);
        }
    }

    private void markSeen(){
        if(isRoom(player.x, player.y)){
            seenRooms.add(regions[player.x][player.y]);
        } else {
            for(int dx = -1; dx <= 1; dx++){
                for(int dy = -1; dy <= 1; dy++){
                    seenHallways.add(player.x + dx + WIDTH * (player.y + dy));
                }
            }
        }
    }

    public void removeFlat(FlatOccupant f){
        flatAt[f.x][f.y] = null;
        flatOccupants.remove(f);
    }

    public void removeCreature (Creature c){
        creatureAt[c.x][c.y] = null;
        if(c.isEnemy) {
            Enemy e = (Enemy) c;
            enemies.remove(e);
            if (e.held != null) {
                flatAt[e.x][e.y] = e.held;
                flatOccupants.add(e.held);
            }
        }
    }

    /** Returns next long from Random to be seed for next floor */
    public long nextFloorSeed(){
        return ran.nextLong();
    }

}