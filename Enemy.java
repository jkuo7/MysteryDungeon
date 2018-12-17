public class Enemy extends Creature{
    Holdable held;
    MysteryDungeon.Direction lastDir = MysteryDungeon.Direction.STAY;
    int enemyNumber;
    static int enemyTotal = 1;

    Enemy(int i, int j, Pokemon p){
        super(i, j, p);
        isEnemy = true;
        curHP = maxHP;
        enemyNumber = enemyTotal;
        enemyTotal++;
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
            game.addMessage(String.format("%s fainted!", name));
        }
    }

    int expGiven(){
        return (enemyNumber/10 + 1) * 25;
    }

}