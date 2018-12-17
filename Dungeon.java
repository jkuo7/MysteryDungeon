import java.awt.Color;
import java.util.*;

public enum Dungeon{
    VOLCANO ("Volcano", Color.BLACK, new Color(94,64,64), new Color(255,100,100),
            new ArrayList<>(Arrays.asList(Pokemon.TORCHIC, Pokemon.BIDOOF))),
    BEACH ("Beach", Color.BLACK, new Color(64,64,94), new Color(100, 175, 175),
                    new ArrayList<>(Arrays.asList(Pokemon.PIPLUP, Pokemon.BIDOOF))),
    FOREST ("Forest", Color.BLACK, new Color(64,94,64), new Color(100, 175, 100),
                            new ArrayList<>(Arrays.asList(Pokemon.SNIVY, Pokemon.BIDOOF))),
    PLAINS ("Plains", Color.BLACK, new Color(124,94,64), new Color(200, 122, 85),
                                    new ArrayList<>(Arrays.asList(Pokemon.PIKACHU, Pokemon.BIDOOF)));

    private String name;
    private ArrayList<Pokemon> enemies;
    private Color floor, wall, blank;

    Dungeon(String n, Color b, Color w, Color f, ArrayList<Pokemon> e){
        name = n;
        enemies = e;
        floor = f;
        wall = w;
        blank = b;
    }

    public String toString(){
        return name;
    }

    int numEnemies(){
        return enemies.size();
    }

    Pokemon getEnemy(int i){
        return enemies.get(i);
    }

    private static final Dungeon[] values = values();

    static Dungeon[] getValues(){
        return values;
    }

    Color getFloorColor(){
        return floor;
    }

    Color getWallColor(){
        return wall;
    }

    Color getBlankColor(){
        return blank;
    }
}