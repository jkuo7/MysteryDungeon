import java.awt.Color;
import java.util.Random;

public class Trap extends FlatOccupant{
    boolean revealed = true;
    boolean deactivated = false;
    Kinds kind;
    Random ran;

    enum Kinds{
        HUNGER ("Hunger Trap", 0.3,
                "Stepping on it makes you hungry!"),
        SPIKY ("Spiky Trap", 1,
                "Stepping on this trap damages with its spikes.");

        private final String name, description;
        private final double breakChance;

        Kinds(String n, double b, String d){
            name = n;
            breakChance = b;
            description = d;
        }

        String getName() {
            return name;
        }

        String getDescription() {
            return description;
        }

        private static final Kinds[] values = values();
    }

    Trap(int i, int j, Random r){
        super(i, j);
        symbol = "✖";
        textColor = Color.RED;
        ran = r;
        int index = ran.nextInt(Kinds.values.length);
        kind = Kinds.values[index];
        name = kind.getName();
        isTrap = true;
    }

    String symbol(){
        return revealed ? symbol : "";
    }

    void walkedOn(Ally a, MysteryDungeonGame game, MysteryDungeon dungeon){
        if(!deactivated){
            activatedTrap(a, game);
        }
    }

    void walkedOn(Enemy e, MysteryDungeonGame game, MysteryDungeon dungeon){}

    void walkedOn(Player p, MysteryDungeonGame game, MysteryDungeon dungeon){
        if(!deactivated){
            activatedTrap(p, game);
        }
    }

    void activatedTrap(PartyMember pm, MysteryDungeonGame game){
        revealed = true;
        String effect = "";
        switch(kind){
            case HUNGER: pm.curBelly = Math.max(0, pm.curBelly - 10);
                effect = "Belly decreased by 10";
                break;
            case SPIKY: pm.curHP = Math.max(0, pm.curHP - 25);
                effect = "Took 25 damage";
                break;
        }
        pm.activatedTrap(this, game, effect);
        if(ran.nextDouble() < kind.breakChance){
            deactivated = true;
        }
    }

}