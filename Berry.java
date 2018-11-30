import java.awt.Color;
import java.util.Random;

public class Berry extends Item{
    int restored;
    int increased;
    int restoredHunger;
    Kinds kind;

    enum Kinds{
        ORAN (100, 10, Color.BLUE, "Oran Berry",
                "Eating it restores HP. Additionally, it increases your max HP. \nIf your max HP is 100 or more, your max HP won't increase unless you eat it when your HP is full."),
        LEPPA (10, 0, Color.RED, "Leppa Berry",
                "Eating it restores PP to one move."),
        SITRUS (200, 5, Color.YELLOW, "Sitrus Berry",
                "Eating it restores HP. It also slightly increases \nyour max HP if you eat it when your HP is full.");

        private final int restored, increased;
        private final Color color;
        private final String name, description;

        Kinds(int r, int i, Color c, String n, String d){
            restored = r;
            increased = i;
            color = c;
            name = n;
            description = d;
        }

        int getRestored(){
            return restored;
        }

        int getIncreased() {
            return increased;
        }

        Color getColor() {
            return color;
        }

        String getName() {
            return name;
        }

        String getDescription() {
            return description;
        }

        private static final Kinds[] values = values();
    }

    Berry(int i, int j, Random ran){
        super(i, j);
        symbol = "\uD83C\uDF53";
        int index = (int) Math.min(Math.floor(-Math.log10(ran.nextDouble()/5)), Kinds.values.length - 1);
        kind = Kinds.values[index];
        restored = kind.getRestored();
        increased = kind.getIncreased();
        textColor = kind.getColor();
        name = kind.getName();
        description = kind.getDescription();
        restoredHunger = 2;
    }

    boolean used(PartyMember p, MysteryDungeonGame game){
        p.curBelly = Math.min(p.maxBelly, p.curBelly + restoredHunger);
        switch(kind){
            case ORAN:
                if(Math.ceil(p.curHP) == p.maxHP){
                    p.maxHP += increased;
                } else {
                    if (p.maxHP < 100){
                        p.maxHP += increased;
                    }
                    p.curHP = Math.min(p.maxHP, p.curHP + restored);
                }
                break;
            case SITRUS:
                if(Math.ceil(p.curHP) == p.maxHP){
                    p.maxHP += increased;
                } else {
                    p.curHP = Math.min(p.maxHP, p.curHP + restored);
                }
                break;
            case LEPPA:
                Object[] attacks = p.getAttacks();
                LearnedAttack a = (LearnedAttack) game.promptInput("Choose an attack to restore PP to", "Attacks", attacks, attacks[0]);
                if(a!= null && a.curPP != a.maxPP){
                    a.curPP = Math.min(a.maxPP, a.curPP + restored);
                } else {
                    return false;
                }
                break;
        }
        return true;
    }

}