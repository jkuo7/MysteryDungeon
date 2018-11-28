public enum FixedAttack implements Attack{

    NORMAL_ATTACK (Type.NORMAL, 5, 0, "a normal attack");

    private Type type;
    private int power, maxPP;
    private String name;

    FixedAttack(Type t, int p, int m, String n){
        type = t;
        power = p;
        maxPP = m;
        name = n;
    }

    public void usedOn(Creature attacker, Creature defender, MysteryDungeonGame game){
        game.addMessage(String.format("%s used %s on %s", attacker.name, name, defender.name));
        defender.attackedFor(power, game);
    }

    public int damage(Creature attacker, Creature defender, MysteryDungeonGame game){
        return power;
    }

    public String toString(){
        return String.format("<html>%s</html>",
                inHTML());
    }

    public String inHTML(){
        return String.format("<span color=\"#%02x%02x%02x\">%s</span>",
                type.getColor().getRed(), type.getColor().getGreen(), type.getColor().getBlue(), name);
    }

    public int getMaxPP(){
        return maxPP;
    }

    public void usePP(){}

}
