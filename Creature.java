import java.util.LinkedHashSet;
import java.util.Set;

public abstract class Creature extends Occupant{
    boolean isEnemy;
    boolean isPlayer = false;
    int maxHP, attack, def;
    double curHP;
    Creature curTarget;
    Type type;
    Set<Attack> attacks = new LinkedHashSet<>(4);


    Creature(int i, int j){
        super(i, j);
        canBeWalkedOn = false;
        def = 10;
    }
    void move(int dx, int dy, MysteryDungeonGame game){
        move(dx, dy);
        checkHealth(game);
    }

    void move(int dx, int dy){
        x += dx;
        y += dy;
    }

    abstract void checkHealth(MysteryDungeonGame game);

    void attacks(Attack a, Creature c, MysteryDungeonGame game){
        a.usedOn(this, c, game);
        if(c.curHP <= 0) {
            game.addMessage(String.format("%s fainted!", c.name));
        }
    }

    void attackedFor(int a, MysteryDungeonGame game){
        curHP = Math.max(0, curHP - a);
        checkHealth(game);
    }

    abstract int expGiven();
}