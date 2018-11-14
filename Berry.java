import java.awt.Color;
import java.util.Random;

public class Berry extends Item{
    int restoredHP;
    int increasedHP;
    int restoredHunger;
    boolean isOran;

    private static int[] restores = {100, 200};
    private static int[] increases = {10, 5};
    private static Color[] colors = {Color.BLUE, Color.YELLOW};
    private static String[] names = {"Oran Berry", "Sitrus Berry"};
    private static String[] descriptions = {"Eating it restores HP. Additionally, it increases your max HP. \nIf your max HP is 100 or more, your max HP won't increase unless you eat it when your HP is full.",
            "Eating it restores HP. It also slightly increases \nyour max HP if you eat it when your HP is full. "};

    Berry(int i, int j, Random ran){
        super(i, j);
        symbol = "\uD83C\uDF53";
        int index = (int) Math.min(Math.floor(-Math.log10(ran.nextDouble()/5)), restores.length - 1);
        restoredHP = restores[index];
        increasedHP = increases[index];
        textColor = colors[index];
        name = names[index];
        description = descriptions[index];
        restoredHunger = 2;
        isOran = index == 0;
    }

    void used(PartyMember p){
        p.curBelly = Math.min(p.maxBelly, p.curBelly + restoredHunger);
        if(Math.ceil(p.curHP) == p.maxHP){
            p.maxHP += increasedHP;
        } else {
            if(name.equals("Oran Berry") && p.maxHP < 100){
                p.maxHP += increasedHP;
            }
            p.curHP = Math.min(p.maxHP, p.curHP + restoredHP);
        }
    }

}