import java.awt.Color;
import java.util.*;

public class Player extends PartyMember{
    int money = 0;
    boolean critical = false;
    static Color pink = new Color(255,65,140);
    Set<Item> bag;
    Set<PartyMember> party;
    Set<Ally> allies;
    int bagLimit = 20;

    Player(int i, int j){
        super(i, j);
        isPlayer = true;
        symbol = "⍥";
        textColor = pink;
        bag = new LinkedHashSet<>();
        party = new LinkedHashSet<>();
        allies = new LinkedHashSet<>();
        player = this;
        party.add(this);
    }

    void move(int dx, int dy, MysteryDungeonGame game){
        super.move(dx, dy, game);
        checkCritical(game);
    }

    void checkCritical(MysteryDungeonGame game){
        if(curHP <= maxHP * 0.2 && !critical){
            critical = true;
            game.setCritical(true);
        } else if (curHP > maxHP * 0.2 && critical) {
            game.setCritical(false);
            critical = false;
        }
    }

    void take(Coin c, MysteryDungeon dungeon){
        money += c.value;
        dungeon.removeFlat(c);
    }

    void take(Item i, MysteryDungeon dungeon){
        dungeon.game.askItem(i);
    }

    void putInBag(Item i, MysteryDungeon dungeon){
        if(bag.size() <= bagLimit){
            bag.add(i);
            dungeon.removeFlat(i);
        }
    }

    void use(Item i, MysteryDungeon dungeon){
        i.used(this);
        dungeon.removeFlat(i);
    }

    Object[] getBag(){
        return bag.toArray();
    }


    public String toString(){
        return String.format("Player (HP: %d/%d, Belly: %d/%d)", (int) Math.ceil(curHP), maxHP, (int) Math.ceil(curBelly), maxBelly);
    }

    Object[] getParty(){
        return party.toArray();
    }
}