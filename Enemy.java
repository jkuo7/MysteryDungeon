import java.awt.Color;

public class Enemy extends Creature{
    Holdable held;

    Enemy(int i, int j){
        super(i, j);
        isEnemy = true;
        symbol = "♉";
        textColor = Color.BLUE;
    }

    void take(Holdable h, MysteryDungeon dungeon){
        if(held == null){
            held = h;
            dungeon.removeFlat(h);
        }
    }

    void checkHealth(MysteryDungeonGame game){
        if(curHP == 0){
            game.outOfHP(this);
        }
    }

}