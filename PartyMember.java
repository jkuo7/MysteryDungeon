import java.awt.Color;

public abstract class PartyMember extends Creature{
    double hPRegen = 0.005;
    int maxBelly = 100;
    double curBelly = maxBelly;
    Player player;
    boolean critical = false;

    PartyMember(int i, int j){
        super(i, j);
        isEnemy = false;
    }

    void move(int dx, int dy, MysteryDungeonGame game){
        super.move(dx, dy, game);
        checkCritical(game);
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
            game.addMessage(String.format("%s fainted!", name), Color.RED);
        }
    }

    abstract void checkCritical(MysteryDungeonGame game);

    void take(Coin c, MysteryDungeonGame game, MysteryDungeon dungeon){
        player.money += c.value;
        dungeon.removeFlat(c);
        game.addMessage(String.format("%s picked up %s", name, c.name));
    }

    abstract void take(Item i, MysteryDungeonGame game, MysteryDungeon dungeon);

    void useFromBag(Item i, MysteryDungeonGame game){
        i.used(this);
        player.bag.remove(i);
        game.addMessage(String.format("%s used %s", name, i.name));
        checkCritical(game);
    }

    public String toString(){
        String color;
        if(critical){
            color = "<html><font color=\"red\">";
        } else {
            color = String.format("<html><font color=\"#%02x%02x%02x\">",
                    textColor.getRed(), textColor.getGreen(), textColor.getBlue());
        }
        return String.format("%s%s (%s) (HP: %d/%d, Belly: %d/%d)</font></html>",
                color, name, symbol, (int) Math.ceil(curHP), maxHP, (int) Math.ceil(curBelly), maxBelly);
    }

}