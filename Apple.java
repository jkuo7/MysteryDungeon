import java.awt.Color;
import java.util.Random;

public class Apple extends Item{
    int restored;
    int increased;

    private static int[] restores = {50, 100, Integer.MAX_VALUE};
    private static int[] increases = {5, 10, 50};
    private static Color[] colors = {Color.RED, Color.RED, new Color(255, 215, 0) };
    private static String[] names = {"Apple", "Big Apple", "Golden Apple"};
    private static String[] descriptions = {"Eating it somewhat fills your Belly. If you eat it \nwhen your Belly is full, it increases your Belly's capacity slightly.",
            "Eating it fills your Belly significantly. If you eat it \nwhen your Belly is full, it increases your Belly's capacity slightly.",
            "It's one of the Golden Items, with a golden glow. They say eating it totally \nfills your Belly and also increases your Belly's capacity significantly during the adventure."};

    Apple(int i, int j, Random ran){
        super(i, j);
        symbol = "\uD83C\uDF4E";
        int index = (int) Math.min(Math.floor(-Math.log10(ran.nextDouble()/4)), restores.length - 1);
        restored = restores[index];
        increased = increases[index];
        textColor = colors[index];
        name = names[index];
        description = descriptions[index];
    }

    void used(PartyMember p){
        if(Math.ceil(p.curBelly) == p.maxBelly || name.equals("Golden Apple")){
            p.maxBelly += increased;
        } else {
            p.curBelly = Math.min(p.maxBelly, p.curBelly + restored);
        }
    }

}