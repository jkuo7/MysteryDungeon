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

    void attacks(Creature c, MysteryDungeonGame game){
        c.attackedFor(5, game);
    }

    void specialAttacks(Attack a, Creature c, MysteryDungeonGame game){
        c.attackedFor(a.calculateDamage(attack, type, c.def, c.type), game);
    }

    void attackedFor(int a, MysteryDungeonGame game){
        curHP = Math.max(0, curHP - a);
        checkHealth(game);
    }

    abstract int expGiven();
}