public abstract class Creature extends Occupant{
    boolean isEnemy;
    boolean isPlayer = false;
    int maxHP = 100;
    double curHP = maxHP;

    Creature(int i, int j){
        super(i, j);
        canBeWalkedOn = false;
    }

    void move(int dx, int dy, MysteryDungeonGame game){
        x += dx;
        y += dy;
        checkHealth(game);
    }

    abstract void checkHealth(MysteryDungeonGame game);

}