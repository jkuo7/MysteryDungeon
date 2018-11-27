import java.awt.Color;
import java.util.*;

public class Player extends PartyMember{
    int money = 0;
    Set<Item> bag;
    Set<PartyMember> party;
    Set<Ally> allies;
    int bagLimit = 20;
    Attack lastAttack;


    Player(int i, int j){
        super(i, j);
        isPlayer = true;
        symbol = "⍥";
        type = Type.FIRE;
        textColor = type.getColor();
        bag = new LinkedHashSet<>();
        party = new LinkedHashSet<>();
        allies = new LinkedHashSet<>();
        name = "Player";
        player = this;
        party.add(this);
        attacks.add(Attack.TACKLE);
        attacks.add(Attack.EMBER);
        setAttacksString();
    }

    void checkCritical(MysteryDungeonGame game){
        if(curHP <= maxHP * 0.2 && !critical){
            critical = true;
            game.setCritical(true);
            game.addMessage(String.format("%s is low on health", name), Color.RED);
        } else if (curHP > maxHP * 0.2 && critical) {
            game.setCritical(false);
            critical = false;
        }
    }

    void take(Item i, MysteryDungeonGame game, MysteryDungeon dungeon){
        game.askItem(i);
    }

    void putInBag(Item i, MysteryDungeonGame game, MysteryDungeon dungeon, PartyMember pm){
        if(bag.size() < bagLimit){
            bag.add(i);
            dungeon.removeFlat(i);
            game.addMessage(String.format("%s picked up %s", pm.name, i.name));
        } else {
            game.addMessage("Bag is full!");
        }
    }

    void use(Item i, MysteryDungeonGame game, MysteryDungeon dungeon){
        i.used(this);
        dungeon.removeFlat(i);
        game.addMessage(String.format("%s found and used %s", name, i.name));
        checkCritical(game);
    }

    Object[] getBag(){
        return bag.toArray();
    }

    Object[] getParty(){
        return party.toArray();
    }

    Object[] getAttacks(){
        return attacks.toArray();
    }
}