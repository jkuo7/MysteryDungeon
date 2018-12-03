public enum VariableAttack implements Attack{

    TACKLE (Type.NORMAL, 30, 50, "Tackle"),
    EMBER (Type.FIRE, 30, 50,  "Ember"),
    BUBBLE (Type.WATER, 30, 50,  "Bubble"),
    THUNDER_SHOCK(Type.ELECTRIC, 30, 50,  "Thunder Shock"),
    VINE_WHIP(Type.GRASS, 30, 50,  "Vine Whip");

    private Type type;
    private int power, maxPP;
    private String name;

    VariableAttack(Type t, int p, int m, String n){
        type = t;
        power = p;
        maxPP = m;
        name = n;
    }

    public void usedOn(Creature attacker, Creature defender, MysteryDungeonGame game){
        game.addMessage(String.format("%s used %s on %s", attacker.name, name, defender.name));
        defender.attackedFor(damageAgainst(attacker, defender, game, true), game);
    }

    int damageAgainst(Creature attacker, Creature defender, MysteryDungeonGame game, boolean print){
        int base = (attacker.attack * power) / defender.def;
        return (int)(base * defender.type.multiplierFrom(type, game, print) * attacker.type.stab(type));
    }

    public int damage(Creature attacker, Creature defender, MysteryDungeonGame game){
        return damageAgainst(attacker, defender, game, false);
    }

    public String toString(){
        return String.format("<html>%s</html>",
                inHTML());
    }

    public String inHTML(){
        return String.format("<span color=\"#%02x%02x%02x\">%s</span>",
                type.getColor().getRed(), type.getColor().getGreen(), type.getColor().getBlue(), name);
    }

    public String getName(){
        return name;
    }

    public int getMaxPP(){
        return maxPP;
    }

    public void usePP(){}
}
