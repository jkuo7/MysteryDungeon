import java.awt.Color;
import java.util.Random;

public class Berry extends Item{
    int restoredHP;
    int increasedHP;
    int restoredHunger;
    Kinds kind;

    enum Kinds{
        ORAN (100, 10, Color.BLUE, "Oran Berry",
                "Eating it restores HP. Additionally, it increases your max HP. \nIf your max HP is 100 or more, your max HP won't increase unless you eat it when your HP is full."),
        SITRUS (200, 5, Color.YELLOW, "Sitrus Berry",
                "Eating it restores HP. It also slightly increases \nyour max HP if you eat it when your HP is full.");

        private final int restoredHP, increasedHP;
        private final Color color;
        private final String name, description;

        Kinds(int r, int i, Color c, String n, String d){
            restoredHP = r;
            increasedHP = i;
            color = c;
            name = n;
            description = d;
        }

        int getRestoredHP(){
            return restoredHP;
        }

        int getIncreasedHP() {
            return increasedHP;
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
        restoredHP = kind.getRestoredHP();
        increasedHP = kind.getIncreasedHP();
        textColor = kind.getColor();
        name = kind.getName();
        description = kind.getDescription();
        restoredHunger = 2;
    }

    void used(PartyMember p){
        p.curBelly = Math.min(p.maxBelly, p.curBelly + restoredHunger);
        if(Math.ceil(p.curHP) == p.maxHP){
            p.maxHP += increasedHP;
        } else {
            if(kind == Kinds.ORAN && p.maxHP < 100){
                p.maxHP += increasedHP;
            }
            p.curHP = Math.min(p.maxHP, p.curHP + restoredHP);
        }
    }

}