public abstract class Creature extends Occupant{
    boolean isEnemy;
    boolean isPlayer = false;
    int maxHP, attack;
    double curHP;
    Creature curTarget;
    Type type;

    Creature(int i, int j){
        super(i, j);
        canBeWalkedOn = false;
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
        c.attackedFor(attack, game);
    }

    void attackedFor(int a, MysteryDungeonGame game){
        curHP = Math.max(0, curHP - a);
        checkHealth(game);
    }

    abstract int expGiven();
}