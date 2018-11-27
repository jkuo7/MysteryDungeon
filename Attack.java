public enum Attack {

    TACKLE (Type.NORMAL, 30, "Tackle"),
    EMBER (Type.FIRE, 30, "Ember"),
    BUBBLE (Type.WATER, 30, "Bubble"),
    THUNDER_SHOCK(Type.ELECTRIC, 30, "Thunder Shock"),
    VINE_WHIP(Type.GRASS, 30, "Vine Whip");

    private Type type;
    private int power;
    private String name;

    Attack(Type t, int p, String n){
        type = t;
        power = p;
        name = n;
    }

    Type getType() {
        return type;
    }

    int getPower() {
        return power;
    }

    int calculateDamage(int atk, Type atkType, int def, Type defType){
        int base = (atk * power) / def;
        return (int)(base * type.multiplierAgainst(defType) * type.stab(atkType));
    }

    public String toString(){
        return String.format("<html>%s</html>",
                inHTML());
    }

    String inHTML(){
        return String.format("<span color=\"#%02x%02x%02x\">%s</span>",
                type.getColor().getRed(), type.getColor().getGreen(), type.getColor().getBlue(), name);
    }

    String getName(){
        return name;
    }

}
