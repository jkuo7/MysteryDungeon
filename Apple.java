import java.awt.Color;
import java.util.Random;

public class Apple extends Item{
    int restored;
    int increased;
    Kinds kind;

    enum Kinds{
        APPLE (50, 5, Color.RED, "Apple",
                "Eating it somewhat fills your Belly. If you eat it \nwhen your Belly is full, it increases your Belly's capacity slightly."),
        BIG_APPLE (100, 10, Color.RED, "Big Apple",
                "Eating it fills your Belly significantly. If you eat it \nwhen your Belly is full, it increases your Belly's capacity slightly."),
        GOLDEN_APPLE (Integer.MAX_VALUE, 50, new Color(255, 215, 0), "Golden Apple",
                "It's one of the Golden Items, with a golden glow. They say eating it totally \nfills your Belly and also increases your Belly's capacity significantly during the adventure.");

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

    Apple(int i, int j, Random ran){
        super(i, j);
        symbol = "\uD83C\uDF4E";
        int index = (int) Math.min(Math.floor(-Math.log10(ran.nextDouble()/4)), Kinds.values.length - 1);
        kind = Kinds.values[index];
        restored = kind.getRestored();
        increased = kind.getIncreased();
        textColor = kind.getColor();
        name = kind.getName();
        description = kind.getDescription();
    }

    void used(PartyMember p){
        if(Math.ceil(p.curBelly) == p.maxBelly || kind == Kinds.GOLDEN_APPLE){
            p.maxBelly += increased;
        } else {
            p.curBelly = Math.min(p.maxBelly, p.curBelly + restored);
        }
    }

}