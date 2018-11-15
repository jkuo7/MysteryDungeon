import java.awt.Color;

public class Ally extends PartyMember{
    static Color darkGreen = new Color(0,127,0);
    int allyNumber;
    static int allyTotal = 1;

    Ally(int i, int j, Player p){
        super(i, j);
        symbol = "⍥";
        textColor = darkGreen;
        player = p;
        player.party.add(this);
        player.allies.add(this);
        allyNumber = allyTotal;
        allyTotal++;
        name = "Ally " + allyNumber;
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
            game.addMessage(String.format("%s is low on health", name));
        } else if (curHP > maxHP * 0.2 && critical) {
            critical = false;
        }
    }

    void take(Coin c, MysteryDungeon dungeon){
        player.take(c, dungeon);
    }

    void take(Item i, MysteryDungeon dungeon){
        player.putInBag(i, dungeon);
    }

}