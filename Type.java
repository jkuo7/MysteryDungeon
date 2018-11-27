import java.awt.Color;
import java.util.*;

public enum Type {
    NORMAL(new Color(105,42,22), new HashSet<>(Arrays.asList()),
            new HashSet<>(Arrays.asList()), new HashSet<>(Arrays.asList("FIRE"))),
    FIRE (Color.RED, new HashSet<>(Arrays.asList("GRASS")),
            new HashSet<>(Arrays.asList("FIRE", "WATER")), new HashSet<>(Arrays.asList())),
    WATER (Color.BLUE, new HashSet<>(Arrays.asList("FIRE")),
            new HashSet<>(Arrays.asList("GRASS", "WATER")), new HashSet<>(Arrays.asList())),
    ELECTRIC (new Color(233,189,25), new HashSet<>(Arrays.asList("WATER")),
            new HashSet<>(Arrays.asList("ELECTRIC", "GRASS")), new HashSet<>(Arrays.asList())),
    GRASS (new Color(0,127,0), new HashSet<>(Arrays.asList("WATER")),
            new HashSet<>(Arrays.asList("FIRE", "GRASS")), new HashSet<>(Arrays.asList()));

    private Set<String> strong, weak, immune;
    private Color color;

    Type(Color c, Set<String> s, Set<String> w, Set<String> i){
        color = c;
        strong = s;
        weak = w;
        immune = i;
    }

    Color getColor(){
        return color;
    }

    double multiplierAgainst(Type other){
        if(immune.contains(other.name())){
            return 0.5;
        } else if(strong.contains(other.name())){
            return 1.4;
        } else if(weak.contains(other.name())){
            return 0.7;
        }
        return 1;
    }

    double stab(Type attackerType){
        if(this == attackerType){
            return 1.5;
        }
        return 1;
    }

    void effectiveAgainst(Type other, MysteryDungeonGame game){
        if(immune.contains(other.name())){
            game.addMessage("There was little effect...");
        } else if(strong.contains(other.name())){
            game.addMessage("It's super effective!");
        } else if(weak.contains(other.name())){
            game.addMessage("It's not very effective");
        }
    }
}
