import java.awt.Color;
import java.util.*;

public enum Type {
    NORMAL(new Color(165,42,42), new HashSet<>(Arrays.asList()),
            new HashSet<>(Arrays.asList())),
    FIRE (Color.RED, new HashSet<>(Arrays.asList("GRASS")),
            new HashSet<>(Arrays.asList("FIRE", "WATER"))),
    WATER (Color.BLUE, new HashSet<>(Arrays.asList("FIRE")),
            new HashSet<>(Arrays.asList("GRASS", "WATER"))),
    ELECTRIC (new Color(233,189,25), new HashSet<>(Arrays.asList("WATER")),
            new HashSet<>(Arrays.asList("ELECTRIC", "GRASS"))),
    GRASS (new Color(0,127,0), new HashSet<>(Arrays.asList("WATER")),
            new HashSet<>(Arrays.asList("FIRE", "GRASS")));

    private Set<String> strong, weak;
    private Color color;

    Type(Color c, Set<String> s, Set<String> w){
        color = c;
        strong = s;
        weak = w;
    }

    Color getColor(){
        return color;
    }

    double multiplierAgainst(Type other){
        if(strong.contains(other.name())){
            return 1.4;
        } else if(weak.contains(other.name())){
            return 0.7;
        }
        return 1;
    }
}
