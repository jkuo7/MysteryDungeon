import java.util.*;

public enum Pokemon {
    TORCHIC (Type.FIRE, "Torchic", "⍥", 100, 5, 5,
            Arrays.asList(VariableAttack.TACKLE, VariableAttack.EMBER),
            makeLevelUpAttacks(
                    new ArrayList<>(Arrays.asList(2, 4, 6)),
                    new ArrayList<>(Arrays.asList(VariableAttack.VINE_WHIP, VariableAttack.BUBBLE, VariableAttack.THUNDER_SHOCK)))),
    PIPLUP (Type.WATER, "Piplup", "⍥", 100, 5, 5,
            Arrays.asList(VariableAttack.TACKLE, VariableAttack.BUBBLE),
            makeLevelUpAttacks(
                    new ArrayList<>(Arrays.asList(2, 4, 6)),
                    new ArrayList<>(Arrays.asList(VariableAttack.EMBER, VariableAttack.THUNDER_SHOCK, VariableAttack.VINE_WHIP)))),
    SNIVY (Type.GRASS, "Snivy", "⍥", 100, 5, 5,
            Arrays.asList(VariableAttack.TACKLE, VariableAttack.VINE_WHIP),
            makeLevelUpAttacks(
                    new ArrayList<>(Arrays.asList(2, 4, 6)),
                    new ArrayList<>(Arrays.asList(VariableAttack.THUNDER_SHOCK, VariableAttack.EMBER, VariableAttack.BUBBLE)))),
    PIKACHU (Type.ELECTRIC, "Pikachu", "⍥", 100, 5, 5,
            Arrays.asList(VariableAttack.TACKLE, VariableAttack.THUNDER_SHOCK),
            makeLevelUpAttacks(
                    new ArrayList<>(Arrays.asList(2, 4, 6)),
                    new ArrayList<>(Arrays.asList(VariableAttack.BUBBLE, VariableAttack.VINE_WHIP, VariableAttack.EMBER)))),
    BIDOOF (Type.NORMAL, "Bidoof", "♉", 100, 5, 5,
            Arrays.asList(VariableAttack.TACKLE),
            makeLevelUpAttacks(
                    new ArrayList<>(Arrays.asList()),
                    new ArrayList<>(Arrays.asList())));

    private Type type;
    private String name, symbol;
    private int maxHP, atk, def;
    private List<Attack> startAttacks;
    private HashMap<Integer, Attack> levelUpAttacks;

    Pokemon(Type t, String n, String s, int h, int a, int d, List<Attack> l, HashMap<Integer, Attack> m){
        type = t;
        name = n;
        symbol = s;
        maxHP = h;
        atk = a;
        def = d;
        startAttacks = l;
        levelUpAttacks = m;
    }

    public String toString(){
        return String.format("<html><span color=\"#%02x%02x%02x\">%s</span></html>",
                type.getColor().getRed(), type.getColor().getGreen(), type.getColor().getBlue(), name);
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

    public int getMaxHP() {
        return maxHP;
    }

    public int getAtk() {
        return atk;
    }

    public int getDef() {
        return def;
    }

    public List<Attack> getStartAttacks() {
        return startAttacks;
    }

    private static HashMap<Integer, Attack> makeLevelUpAttacks(ArrayList<Integer> levels, ArrayList<Attack> attacks){
        int size = Math.min(levels.size(), attacks.size());
        HashMap<Integer, Attack> m = new HashMap<>(size);
        for(int i = 0; i < size; i++){
            m.put(levels.get(i), attacks.get(i));
        }
        return m;
    }

    boolean learnsMoveAtLevel(int l){
        return levelUpAttacks.containsKey(l);
    }

    Attack moveLearnedAtLevel(int l){
        return levelUpAttacks.get(l);
    }
}
