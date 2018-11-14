import java.awt.Color;

public class Ally extends PartyMember{
    //     Player player;
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
    }

    void take(Coin c, MysteryDungeon dungeon){
        player.take(c, dungeon);
    }

    void take(Item i, MysteryDungeon dungeon){
        player.putInBag(i, dungeon);
    }

    public String toString(){
        return String.format("Ally %d (HP: %d/%d, Belly: %d/%d)", allyNumber, (int) Math.ceil(curHP), maxHP, (int) Math.ceil(curBelly), maxBelly);
    }

}