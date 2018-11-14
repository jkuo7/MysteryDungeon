import java.awt.Color;

public class Coin extends Holdable{
    int value;
    static Color gold= new Color(255, 215, 0);

    Coin(int i, int j, int v){
        super(i, j);
        value = v;
        symbol = "⍟";
        textColor = gold;
    }

    void walkedOn(Ally a, MysteryDungeonGame game, MysteryDungeon dungeon){
        a.take(this, dungeon);
    }

    void walkedOn(Player p, MysteryDungeonGame game, MysteryDungeon dungeon){
        p.take(this, dungeon);
    }
}