import java.util.*;

public enum Pokemon {
    TORCHIC (Type.FIRE, "Torchic", "⍥",
            new LinkedList<>(Arrays.asList(VariableAttack.TACKLE, VariableAttack.EMBER))),
    PIPLUP (Type.WATER, "Piplup", "⍥",
            new LinkedList<>(Arrays.asList(VariableAttack.TACKLE, VariableAttack.BUBBLE))),
    SNIVY (Type.GRASS, "Snivy", "⍥",
            new LinkedList<>(Arrays.asList(VariableAttack.TACKLE, VariableAttack.VINE_WHIP))),
    PIKACHU (Type.ELECTRIC, "Pikachu", "⍥",
            new LinkedList<>(Arrays.asList(VariableAttack.TACKLE, VariableAttack.THUNDER_SHOCK))),
    BIDOOF (Type.NORMAL, "Bidoof", "♉",
            new LinkedList<>(Arrays.asList(VariableAttack.TACKLE)));

    private Type type;
    private String name, symbol;
    private List<Attack> startAttacks;

    Pokemon(Type t, String n, String s, List<Attack> a){
        type = t;
        name = n;
        symbol = s;
        startAttacks = a;
    }

    public String toString(){
        return name;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public List<Attack> getStartAttacks() {
        return startAttacks;
    }
}
