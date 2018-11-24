import java.awt.Color;
import java.util.Random;

public class Trap extends FlatOccupant{
    boolean revealed = false;
    boolean broken = false;
    Kinds kind;
    static Random ran;

    enum Kinds{
        HUNGER ("Hunger Trap", 0.3, "Belly decreased by 10",
                "Stepping on it makes you hungry!"),
        SPIKY ("Spiky Trap", 1, "Took 25 damage",
                "Stepping on this trap damages with its spikes."),
        WARP ("Warp Trap", 1, "Warped to elsewhere on the floor",
                "Stepping on it warps you to somewhere else on the floor.");

        private final String name, description, effect;

        private final double breakChance;

        Kinds(String n, double b, String e, String d){
            name = n;
            breakChance = b;
            effect = e;
            description = d;
        }

        String getName() {
            return name;
        }

        String getDescription() {
            return description;
        }

        String getEffect(){
            return effect;
        }

        double getBreakChance() {
            return breakChance;
        }

        private static final Kinds[] values = values();
    }

    Trap(int i, int j, Random r){
        super(i, j);
        symbol = "✖";
        textColor = Color.RED;
        if(ran == null) {ran = r;}
        int index = ran.nextInt(Kinds.values.length);
        kind = Kinds.values[index];
        name = kind.getName();
        isTrap = true;
    }

    String symbol(){
        return revealed ? symbol : "";
    }

    void walkedOn(Ally a, MysteryDungeonGame game, MysteryDungeon dungeon){
        if(!broken){
            activatedTrap(a, game, dungeon);
        }
    }

    void walkedOn(Enemy e, MysteryDungeonGame game, MysteryDungeon dungeon){}

    void walkedOn(Player p, MysteryDungeonGame game, MysteryDungeon dungeon){
        if(!broken){
            activatedTrap(p, game, dungeon);
        }
    }

    void activatedTrap(PartyMember pm, MysteryDungeonGame game, MysteryDungeon dungeon){
        revealed = true;
        switch(kind){
            case HUNGER: pm.curBelly = Math.max(0, pm.curBelly - 10); break;
            case SPIKY: pm.curHP = Math.max(0, pm.curHP - 25); break;
            case WARP: dungeon.warpPartyMember(pm); break;
        }
        pm.activatedTrap(this, game, kind.getEffect());
        if(ran.nextDouble() < kind.getBreakChance()){
            broken = true;
            textColor = Color.GREEN;
        }
    }

}