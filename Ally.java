import java.awt.Color;

public class Ally extends PartyMember{
    int allyNumber;
    static int allyTotal = 1;
    boolean swapped;

    Ally(int i, int j, Player p, Pokemon poke){
        super(i, j, poke);
        player = p;
        player.party.add(this);
        player.allies.add(this);
        allyNumber = allyTotal;
        allyTotal++;
    }

    void checkHealth(MysteryDungeonGame game){
        super.checkHealth(game);
        if(curHP == 0){
            player.allies.remove(this);
        }
    }

    void checkCritical(MysteryDungeonGame game){
        if(curHP <= maxHP * 0.2 && !critical){
            critical = true;
            game.addMessage(String.format("%s is low on health", name), Color.RED);
        } else if (curHP > maxHP * 0.2 && critical) {
            critical = false;
        }
    }

    void take(Item i, MysteryDungeonGame game, MysteryDungeon dungeon){
        player.putInBag(i, game, dungeon, this);
    }

}