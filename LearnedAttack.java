public class LearnedAttack implements Attack{

    Attack attack;
    int curPP, maxPP;

    LearnedAttack(Attack a){
        attack = a;
        maxPP = attack.getMaxPP();
        curPP = maxPP;
    }

    public void usedOn(Creature attacker, Creature defender, MysteryDungeonGame game){
        attack.usedOn(attacker, defender, game);
        usePP();
    }

    public int damage(Creature attacker, Creature defender, MysteryDungeonGame game){
        return attack.damage(attacker, defender, game);
    }

    public String toString(){
        return String.format("<html>%s</html>",
                inHTML());
    }

    public String inHTML(){
        return String.format("%s (%d/%d)",
                attack.inHTML(), curPP, maxPP);
    }

    public int getMaxPP(){
        return maxPP;
    }

    public void usePP(){
        curPP -= 1;
    }

    public String getName(){
        return attack.getName();
    }

    boolean hasPP(){
        return curPP > 0;
    }
}