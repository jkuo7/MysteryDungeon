import java.awt.Color;
import java.util.*;

public class Player extends PartyMember{
    int money = 0;
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
        name = "Player";
        player = this;
        party.add(this);
    }

    void checkCritical(MysteryDungeonGame game){
        if(curHP <= maxHP * 0.2 && !critical){
            critical = true;
            game.setCritical(true);
            game.addMessage(String.format("%s is low on health", name));
//             game.addMessage(String.format("<html><font color=\"red\">%s is low on health</font></html>"), name);
        } else if (curHP > maxHP * 0.2 && critical) {
            game.setCritical(false);
            critical = false;
        }
    }

    void take(Coin c, MysteryDungeon dungeon){
        money += c.value;
        dungeon.removeFlat(c);
        dungeon.game.addMessage(String.format("%s picked up %s", name, c.name));
    }

    void take(Item i, MysteryDungeon dungeon){
        dungeon.game.askItem(i);
    }

    void putInBag(Item i, MysteryDungeon dungeon){
        if(bag.size() < bagLimit){
            bag.add(i);
            dungeon.removeFlat(i);
        }
        dungeon.game.addMessage(String.format("%s picked up %s", name, i.name));
    }

    void use(Item i, MysteryDungeon dungeon){
        i.used(this);
        dungeon.removeFlat(i);
        dungeon.game.addMessage(String.format("%s found and used %s", name, i.name));
    }

    Object[] getBag(){
        return bag.toArray();
    }

    Object[] getParty(){
        return party.toArray();
    }
}