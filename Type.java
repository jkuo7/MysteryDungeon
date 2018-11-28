import java.awt.Color;
import java.util.*;

public enum Type {
    NORMAL(new Color(105,42,22), new HashSet<>(Arrays.asList()),
            new HashSet<>(Arrays.asList()), new HashSet<>(Arrays.asList())),
    FIRE (Color.RED, new HashSet<>(Arrays.asList("WATER")),
            new HashSet<>(Arrays.asList("FIRE", "GRASS")), new HashSet<>(Arrays.asList())),
    WATER (Color.BLUE, new HashSet<>(Arrays.asList("ELECTRIC")),
            new HashSet<>(Arrays.asList("FIRE", "WATER")), new HashSet<>(Arrays.asList())),
    ELECTRIC (new Color(233,189,25), new HashSet<>(Arrays.asList("GRASS")),
            new HashSet<>(Arrays.asList("WATER", "ELECTRIC")), new HashSet<>(Arrays.asList())),
    GRASS (new Color(0,127,0), new HashSet<>(Arrays.asList("FIRE")),
            new HashSet<>(Arrays.asList("ELECTRIC", "GRASS")), new HashSet<>(Arrays.asList()));

    private Set<String> weakTo, resists, immuneTo;
    private Color color;

    Type(Color c, Set<String> w, Set<String> r, Set<String> i){
        color = c;
        weakTo = w;
        resists = r;
        immuneTo = i;
    }

    Color getColor(){
        return color;
    }

    double multiplierFrom(Type attackType, MysteryDungeonGame game){
        if(immuneTo.contains(attackType.name())){
            game.addMessage("There was little effect...");
            return 0.5;
        } else if(weakTo.contains(attackType.name())){
            game.addMessage("It's super effective!");
            return 1.4;
        } else if(resists.contains(attackType.name())){
            game.addMessage("It's not very effective");
            return 0.7;
        }
        return 1;
    }

    double stab(Type attackType){
        if(this == attackType){
            return 1.5;
        }
        return 1;
    }

//    void weakAgainst(Type attackType, MysteryDungeonGame game){
//        if(immuneTo.contains(attackType.name())){
//            game.addMessage("There was little effect...");
//        } else if(weakTo.contains(attackType.name())){
//            game.addMessage("It's super effective!");
//        } else if(resists.contains(attackType.name())){
//            game.addMessage("It's not very effective");
//        }
//    }

    boolean isWeakTo(Type attackType){
        return weakTo.contains(attackType.name());
    }

    boolean resists(Type attackType){
        return resists.contains(attackType.name());
    }

    boolean isImmuneTo(Type attackType){
        return immuneTo.contains(attackType.name());
    }

}
