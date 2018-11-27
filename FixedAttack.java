public enum FixedAttack implements Attack{

    NORMAL_ATTACK (Type.NORMAL, 5, "normal attack");

    private Type type;
    private int power;
    private String name;

    FixedAttack(Type t, int p, String n){
        type = t;
        power = p;
        name = n;
    }

    public void usedOn(Creature attacker, Creature defender, MysteryDungeonGame game){
        defender.attackedFor(power, game);
        game.addMessage(String.format("%s used %s on %s", attacker.name, name, defender.name));
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
}
