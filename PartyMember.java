public abstract class PartyMember extends Creature{
    double hPRegen = 0.005;
    int maxBelly = 100;
    double curBelly = maxBelly;
    Player player;

    PartyMember(int i, int j){
        super(i, j);
        isEnemy = false;
    }

    void move(int dx, int dy, MysteryDungeonGame game){
        super.move(dx, dy, game);
    }

    void checkHealth(MysteryDungeonGame game){
        if(curBelly <= 0){
            curHP = Math.max(0, curHP - 1);
        } else {
            curBelly = Math.max(0, curBelly - 0.1);
            if(curHP < maxHP){
                curHP = Math.min(maxHP, curHP + maxHP * hPRegen);
            }
        }
        if(curHP == 0){
            game.outOfHP(this);
            player.party.remove(this);
        }
    }

    void useFromBag(Item i){
        i.used(this);
        player.bag.remove(i);
    }

}